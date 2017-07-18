package ru.worksolutions.qrrecognize

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.*
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic

import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import org.apache.commons.collections4.queue.CircularFifoQueue
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever
import java.io.ByteArrayOutputStream

import java.io.File
import java.io.FileNotFoundException
import java.util.*

class MainActivity : AppCompatActivity(), BarcodeRetriever {
    val TAG = "MainActivity"

    private lateinit var tvResult: TextView

    override fun onRetrieved(p0: Barcode) {
        Log.d(TAG, "Barcode read: " + p0.displayValue)
        runOnUiThread {
            tvResult.text = "Retrieved: " + p0.displayValue
//            val builder = AlertDialog.Builder(this@MainActivity)
//                    .setTitle("code retrieved")
//                    .setMessage(p0.displayValue)
//            builder.show()
        }
    }

    override fun onRetrievedMultiple(p0: Barcode, p1: MutableList<BarcodeGraphic>) {

    }

    override fun onBitmapScanned(p0: SparseArray<Barcode>) {
        val x = 0
    }

    override fun onRetrievedFailed(p0: String) {
        val x = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tv_information) as TextView

        val barcodeCapture: BarcodeCapture = getSupportFragmentManager().findFragmentById(R.id.barcode) as BarcodeCapture
        barcodeCapture.isShowDrawRect = true
        barcodeCapture.setRetrieval(this);

//        detector = BarcodeDetector.Builder(applicationContext).setBarcodeFormats(Barcode.DATA_MATRIX or Barcode.QR_CODE or Barcode.EAN_13)
//                .build()
//
//        try {
//            mCamera = Camera.open()//you can use open(int) to use different cameras
//        } catch (e: Exception) {
//            Log.d("ERROR", "Failed to get camera: " + e.message)
//        }
//
//
//
//        if (mCamera != null) {
//            mCameraView = CameraView(this, mCamera!!)//create a SurfaceView to show camera data
//            mCamera!!.Size(mCameraView!!.width, mCameraView!!.height)
//            val camera_view = findViewById(R.id.camera_view) as FrameLayout
//            camera_view.addView(mCameraView)//add the SurfaceView to the layout
//        }
//
//        mCamera!!.setPreviewCallback { bs, camera ->
//            Log.e("MainActivity", bitmapQueue.size.toString())
//            Log.e(LOG_TAG, "PREVIEW CALLBACK")
//            val parameters = camera.parameters;
//
//            val width = parameters.previewSize.width;
//            val height = parameters.previewSize.height;
//
//            val yuv = YuvImage(bs, parameters.previewFormat, width, height, null);
//
//
//
//            val out = ByteArrayOutputStream();
//            yuv.compressToJpeg(Rect(0, 0, width, height), 80, out);
//
//            val bytes = out.toByteArray();
//            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size);
//            handleImage(bitmap)
//
//        }

    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            REQUEST_WRITE_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                takePicture()
//            } else {
//                Toast.makeText(this@MainActivity, "Permission Denied!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    fun handleImage(bitmap: Bitmap) {
////        rotateImage(bitmap, 90f)
////        imageView.setImageBitmap(bitmap)
//        bitmapQueue.add(bitmap)
//        Log.e(LOG_TAG, "handleImage()")
//        val frame = Frame.Builder().setBitmap(bitmap).build()
//        val barcodes = detector.detect(frame)
//        for (index in 0..barcodes.size() - 1) {
//            val code = barcodes.valueAt(index)
//
//            Toast.makeText(this, code.displayValue, Toast.LENGTH_LONG).show()
//            Log.e("MAIN ACTIVITY", code.displayValue)
//
////            scanResults!!.text = scanResults!!.text.toString() + code.displayValue + "\n"
//
//            //Required only if you need to extract the type of barcode
//            val type = barcodes.valueAt(index).valueFormat
//            when (type) {
//                Barcode.CONTACT_INFO -> Log.e(LOG_TAG, code.contactInfo.title)
//                Barcode.EMAIL -> Log.i(LOG_TAG, code.email.address)
//                Barcode.ISBN -> Log.i(LOG_TAG, code.rawValue)
//                Barcode.PHONE -> Log.i(LOG_TAG, code.phone.number)
//                Barcode.PRODUCT -> Log.i(LOG_TAG, code.rawValue)
//                Barcode.SMS -> Log.i(LOG_TAG, code.sms.message)
//                Barcode.TEXT -> Log.i(LOG_TAG, code.rawValue)
//                Barcode.URL -> Log.i(LOG_TAG, "url: " + code.url.url)
//                Barcode.WIFI -> Log.i(LOG_TAG, code.wifi.ssid)
//                Barcode.GEO -> Log.i(LOG_TAG, code.geoPoint.lat.toString() + ":" + code.geoPoint.lng)
//                Barcode.CALENDAR_EVENT -> Log.i(LOG_TAG, code.calendarEvent.description)
//                Barcode.DRIVER_LICENSE -> Log.i(LOG_TAG, code.driverLicense.licenseNumber)
//                else -> Log.i(LOG_TAG, code.rawValue)
//            }
//
//        }
//    }


//    private fun takePicture() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val photo = File(Environment.getExternalStorageDirectory(), "picture.jpg")
//        imageUri = Uri.fromFile(photo)
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//        startActivityForResult(intent, PHOTO_REQUEST)
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        if (imageUri != null) {
//            outState.putString(SAVED_INSTANCE_URI, imageUri!!.toString())
//            outState.putString(SAVED_INSTANCE_RESULT, scanResults!!.text.toString())
//        }
//        super.onSaveInstanceState(outState)
//    }

//    private fun launchMediaScanIntent() {
//        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//        mediaScanIntent.data = imageUri
//        this.sendBroadcast(mediaScanIntent)
//    }
//
//    @Throws(FileNotFoundException::class)
//    private fun decodeBitmapUri(ctx: Context, uri: Uri): Bitmap? {
//        val targetW = 600
//        val targetH = 600
//        val bmOptions = BitmapFactory.Options()
//        bmOptions.inJustDecodeBounds = true
//        BitmapFactory.decodeStream(ctx.contentResolver.openInputStream(uri), null, bmOptions)
//        val photoW = bmOptions.outWidth
//        val photoH = bmOptions.outHeight
//
//        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)
//        bmOptions.inJustDecodeBounds = false
//        bmOptions.inSampleSize = scaleFactor
//
//        return BitmapFactory.decodeStream(ctx.contentResolver
//                .openInputStream(uri), null, bmOptions)
//    }

    companion object {
        private val LOG_TAG = "Barcode Scanner API"
        private val PHOTO_REQUEST = 10
        private val REQUEST_WRITE_PERMISSION = 20
        private val SAVED_INSTANCE_URI = "uri"
        private val SAVED_INSTANCE_RESULT = "result"
    }
}