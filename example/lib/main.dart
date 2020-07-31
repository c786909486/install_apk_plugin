import 'dart:io';

import 'package:base_flutter/base_flutter.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:install_apk_plugin/install_apk_plugin.dart';
import 'package:path_provider/path_provider.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  HttpGo.HttpGogetInstance(baseUrl:"http://47.97.204.71:8081/fslrsy_test");
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await InstallApkPlugin.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: MainPage(),
    );
  }


}

class MainPage extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Center(
        child: RaisedButton(child: Text("下载"),onPressed: (){
          download(context);
        },),
      ),
    );
  }

  Future<void> download(context) async {
    Directory appDocDir = await getExternalStorageDirectory();
    AppDownloadUtils().showDownloadDialog(context, "http://221.226.30.132:8088/templates/posApp.apk", appDocDir.path+"/posApp.apk",(path){
      print("下载成功:$path}");
//      OpenFile.open(path);
      InstallApkPlugin.installApk(path);
    },(error){
    });
  }

}
