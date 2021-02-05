package edu.rosehulman.todoheap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import java.lang.Exception

class GetImageTask(private var consumer: ImageConsumer): AsyncTask<String, Void, Bitmap>() {

    //This is taken from photoviewer, but should still work here
    override fun doInBackground(vararg urls: String?): Bitmap? {
        val inStream = java.net.URL(urls[0]).openStream()
        return try {
            Log.d("ProfilePicDebug", "GetImageTask Getting Image ${urls[0]}")
            BitmapFactory.decodeStream(inStream)
        } catch (e: Exception) {
            null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        Log.d("ProfilePicDebug", "GetImageTask sending Image back")
        consumer.onImageLoaded(result)
    }

    interface ImageConsumer {
        fun onImageLoaded(image: Bitmap?)
    }
}