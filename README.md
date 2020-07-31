# install_apk_plugin

用于安装android apk

# 1、添加权限
`    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
`
# 2、设置provider资源文件
新建provider_paths.xml文件,路径自行定义

`<paths xmlns:android="http://schemas.android.com/apk/res/android">
     <external-path
         name="external_root"
         path="." />
 </paths>`

# 3、添加provider
` <provider
                 android:name="androidx.core.content.FileProvider"
                 android:authorities="${applicationId}.fileProvider"
                 android:exported="false"
                 android:grantUriPermissions="true"
               >
             <meta-data
                     android:name="android.support.FILE_PROVIDER_PATHS"
                     android:resource="@xml/provider_paths"
                      />
         </provider>`