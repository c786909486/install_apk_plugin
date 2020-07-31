package com.example.install_apk_plugin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URLConnection

/**
 *@packageName com.yyt.hongtaiposapp.utils
 *@author kzcai
 *@date 2020/3/16
 */
object InstallAppUtils {

    fun installApp(context: Context, file: File) {
        val intent: Intent = validatedFileIntent(context, file)!!
        context.startActivity(intent)
    }

    fun buildIntent(context:Context,file: File):Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val apkUri: Uri = FileProvider.getUriForFile(context, context.packageName + ".fileProvider", file)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            //                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return intent
    }

    @Synchronized
    fun validatedFileIntent(context: Context?, file: File): Intent? {
        var intent: Intent = buildIntent(context!!, file)
        if (validateIntent(context!!, intent)) {
            return intent
        }
        var mime: String? = null
        var inputFile: FileInputStream? = null
        try {
            inputFile = FileInputStream(file)
            mime = URLConnection.guessContentTypeFromStream(inputFile) // fails sometime
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputFile != null) {
                try {
                    inputFile.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        if (mime == null) {
            mime = URLConnection.guessContentTypeFromName(file.absolutePath) // fallback to check file extension
        }
        if (mime != null) {
            intent = buildIntent(context, file)
            if (validateIntent(context, intent)) return intent
        }
        return null
    }

    private fun validateIntent(context: Context, intent: Intent): Boolean {
        val manager = context.packageManager
        val infos = manager.queryIntentActivities(intent, 0)
        return infos.size > 0
    }
}