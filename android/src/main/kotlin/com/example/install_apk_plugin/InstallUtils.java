package com.example.install_apk_plugin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

/**
 * @author kzcai
 * @packageName com.example.install_apk_plugin
 * @date 2020/10/18
 */
public class InstallUtils {

    static  void installApp(Context context, File file){
        Intent intent = validatedFileIntent(context, file);
        context.startActivity(intent);
    }

    static Intent validatedFileIntent(Context context, File file){
        Intent intent = buildIntent(context,file);
        if (validateIntent(context, intent)) {
            return intent;
        }
        String mime = null;
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
            mime = URLConnection.guessContentTypeFromStream(inputFile); // fails sometime
        } catch ( Exception e) {
            e.printStackTrace();
        } finally {
            if (inputFile != null) {
                try {
                    inputFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (mime == null) {
            mime = URLConnection.guessContentTypeFromName(file.getAbsolutePath()); // fallback to check file extension
        }
        if (mime != null) {
            intent = buildIntent(context, file);
            if (validateIntent(context, intent)) {
                return intent;
            }
        }
        return null;
    }

    static Intent buildIntent(Context context, File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    static boolean validateIntent(Context context,Intent intent){
        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
        return infos.size() > 0;
    }
}
