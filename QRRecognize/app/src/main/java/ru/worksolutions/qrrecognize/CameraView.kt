package ru.worksolutions.qrrecognize

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

import java.io.IOException

class CameraView(context: Context, private val mCamera: Camera) : SurfaceView(context), SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder

    init {
        mCamera.setDisplayOrientation(90)
        //get the holder and set this class as the callback, so we can get camera data here
        mHolder = holder
        mHolder.addCallback(this)
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        try {
            //when the surface is created, we can set the camera to draw images in this surfaceholder
            mCamera.setPreviewDisplay(surfaceHolder)
            mCamera.startPreview()
        } catch (e: IOException) {
            Log.d("ERROR", "Camera error on surfaceCreated " + e.message)
        }

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i2: Int, i3: Int) {
        //before changing the application orientation, you need to stop the preview, rotate and then start it again
        if (mHolder.surface == null)
        //check if the surface is ready to receive camera data
            return

        try {
            mCamera.stopPreview()
        } catch (e: Exception) {
            //this will happen when you are trying the camera if it's not running
        }

        //now, recreate the camera preview
        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        } catch (e: IOException) {
            Log.d("ERROR", "Camera error on surfaceChanged " + e.message)
        }

    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        //our app has only one screen, so we'll destroy the camera in the surface
        //if you are unsing with more screens, please move this code your activity
        mCamera.stopPreview()
        mCamera.release()
    }
}