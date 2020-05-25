package com.example.wpam.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.wpam.R
import com.example.wpam.graphic.GraphicOverlay
import com.example.wpam.graphic.TextGraphic
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.Executors


private const val REQUEST_CODE_PERMISSIONS = 10
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class CameraFragment : Fragment(), LifecycleOwner {

    private val cameraViewModel: CameraViewModel by viewModel()
    private val currencyViewModel: CurrencyViewModel by sharedViewModel()
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var viewFinder: TextureView
    private lateinit var size: Size
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_camera, container, false)

        val display = activity!!.windowManager.defaultDisplay
        size = Size(display.width, display.height)

        viewFinder = root.findViewById(R.id.view_finder)

        if (allPermissionsGranted()) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }

        root.retake_button.setOnClickListener {
            displayCameraPreview()
        }

        currencyViewModel.loading.observe(this@CameraFragment, Observer {
            if (it) {
                showLoader()
            } else {
                hideLoader()
            }
        })

        return root
    }


    private fun startCamera() {

        val previewConfig = PreviewConfig.Builder().apply {
            setTargetResolution(size)
        }.build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener {

            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                setTargetResolution(size)
            }.build()

        val imageCapture = ImageCapture(imageCaptureConfig)

        root.capture_button.setOnClickListener {
            currencyViewModel.startLoading()
            imageCapture.takePicture(executor, object : ImageCapture.OnImageCapturedListener() {
                override fun onCaptureSuccess(image: ImageProxy?, rotationDegrees: Int) {
                    val imageAnalyzer = YourImageAnalyzer()
                    imageAnalyzer.analyze(image, rotationDegrees)
                    super.onCaptureSuccess(image, rotationDegrees)
                }
            })
        }

        cameraViewModel.image.observe(this@CameraFragment, Observer {
            displayImagePreview()
            val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
            val imageToProcess = FirebaseVisionImage.fromBitmap(
                Bitmap.createScaledBitmap(
                    it!!.bitmap,
                    size.width,
                    size.height,
                    false
                )
            )

            detector.processImage(imageToProcess).addOnSuccessListener { firebaseVisionText ->
                graphic_overlay.clear()
                for (block in firebaseVisionText.textBlocks) {
                    for (line in block.lines) {
                        for (element in line.elements) {
                            val regex = "^\\d*[.,]?\\d{0,2}".toRegex()
                            var numberValue = regex.find(element.text, 0)?.value
                            if (numberValue != null) {
                                // if price contains comma, replace it with dot to be convertable to float
                                numberValue = numberValue.replace(',', '.');

                                val convertedPrice =
                                    "${String.format(
                                        "%.2f",
                                        (numberValue.toFloat() * currencyViewModel.conversionRate.value!!)
                                    )} ${currencyViewModel.wantedCurrency.value!!.shortName}"

                                val textGraphic: GraphicOverlay.Graphic =
                                    TextGraphic(
                                        graphic_overlay,
                                        convertedPrice,
                                        element.boundingBox
                                    )
                                graphic_overlay.add(textGraphic)
                            }
                        }
                    }
                }
                currencyViewModel.stopLoading()
            }
        })

        CameraX.bindToLifecycle(this, preview, imageCapture)
    }

    private fun showLoader() {
        activity!!.runOnUiThread {
            retake_button.visibility = View.INVISIBLE
            loading_spinner.visibility = View.VISIBLE
        }

    }

    private fun hideLoader() {
        activity!!.runOnUiThread {
            retake_button.visibility = View.VISIBLE
            loading_spinner.visibility = View.INVISIBLE
        }
    }

    private fun displayCameraPreview() {
        graphic_overlay.clear()
        activity!!.runOnUiThread {
            camera_preview.visibility = View.VISIBLE
            image_preview.visibility = View.INVISIBLE
        }
    }

    private fun displayImagePreview() {
        image_view.setImageBitmap(cameraViewModel.image.value?.bitmap)
        activity!!.runOnUiThread {
            camera_preview.visibility = View.INVISIBLE
            image_preview.visibility = View.VISIBLE
        }
    }

    private fun updateTransform() {
        val matrix = Matrix()

        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        viewFinder.setTransform(matrix)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(
                    this.context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context!!, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    inner class YourImageAnalyzer : ImageAnalysis.Analyzer {
        private fun degreesToFirebaseRotation(degrees: Int): Int = when (degrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
        }

        override fun analyze(imageProxy: ImageProxy?, degrees: Int) {
            val mediaImage = imageProxy?.image
            val imageRotation = degreesToFirebaseRotation(degrees)
            if (mediaImage != null) {
                cameraViewModel.image.postValue(
                    FirebaseVisionImage.fromMediaImage(
                        mediaImage,
                        imageRotation
                    )
                )
            }
        }
    }
}
