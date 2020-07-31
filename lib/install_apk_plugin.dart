import 'dart:async';

import 'package:flutter/services.dart';

class InstallApkPlugin {
  static const MethodChannel _channel =
      const MethodChannel('install_apk_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }


  static void installApk(String path){
    _channel.invokeMethod("installApk",{"path":path});
  }
}
