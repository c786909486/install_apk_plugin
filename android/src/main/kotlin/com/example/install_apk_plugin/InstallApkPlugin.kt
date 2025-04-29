package com.example.install_apk_plugin

import android.content.Context
import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File

/** InstallApkPlugin */
public class InstallApkPlugin: FlutterPlugin, MethodCallHandler {

  private lateinit var channel : MethodChannel

  private var context:Context?=null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "install_apk_plugin")
    context = flutterPluginBinding.applicationContext
    channel.setMethodCallHandler(this);
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when(call.method){
      "getPlatformVersion"->{
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
      }

      "installApk"->{
        val path = call.argument<String>("path")
        val file = File(path)
        if (file.exists()){
          InstallUtils.installApp(context!!,file)
        }

      }

      else ->{
        result.notImplemented()
      }
    }

  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
