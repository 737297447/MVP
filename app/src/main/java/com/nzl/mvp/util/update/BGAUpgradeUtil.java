package com.nzl.mvp.util.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.nzl.mvp.App;

import java.io.File;
import java.io.InputStream;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/12/16 上午10:48
 * 描述:应用升级工具类
 *
 *   //RxJava + Retrofit 下载新版 apk 文件，RxBus 监听下载进度
 *  //https://github.com/737297447/BGAUpdate-Android
 *  //compile 'cn.bingoogolapple:bga-update:1.0.1@aar'
 *
 *
 */
public class BGAUpgradeUtil {
    private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";


    private BGAUpgradeUtil() {
    }

    /**
     * 监听下载进度
     *
     * @return
     */
    public static Observable<BGADownloadProgressEvent> getDownloadProgressEventObservable() {
        return RxUpdateUtil.getDownloadEventObservable();
    }

    /**
     * apk 文件是否已经下载过，如果已经下载过就直接安装
     *
     * @param version 新 apk 文件版本号
     * @return
     */
    public static boolean isApkFileDownloaded(String version) {
        File apkFile = StorageUtil.getApkFile(version);
        if (apkFile.exists()) {
            installApk(apkFile);
            return true;
        }
        return false;
    }

    /**
     * 下载新版 apk 文件
     *
     * @param url     apk 文件路径
     * @param version 新 apk 文件版本号
     * @return
     */
    public static Observable<File> downloadApkFile(final String url, final String version) {
        return Observable.defer(new Func0<Observable<InputStream>>() {
            @Override
            public Observable<InputStream> call() {
                try {
                    return Observable.just(Engine.getInstance().getDownloadApi().downloadFile(url).execute().body().byteStream());
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        }).map(new Func1<InputStream, File>() {
            @Override
            public File call(InputStream inputStream) {
                return StorageUtil.saveApk(inputStream, version);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 安装 apk 文件
     *
     * @param apkFile
     */
    public static void installApk(File apkFile) {
        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            installApkIntent.setDataAndType(FileProvider.getUriForFile(App.getInstance(),
//                    PermissionUtil.getFileProviderAuthority(), apkFile), MIME_TYPE_APK);
//            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            openFile(apkFile, App.getInstance());

        } else {
            installApkIntent.setDataAndType(Uri.fromFile(apkFile), MIME_TYPE_APK);
        }

        if (App.getInstance().getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            App.getInstance().startActivity(installApkIntent);
        }
    }

    public static void openFile(File var0, Context var1) {
        Intent var2 = new Intent();
        var2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        var2.setAction(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            Uri uriForFile = FileProvider.getUriForFile(var1, var1.getApplicationContext().getPackageName() + ".provider", var0);
            var2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            var2.setDataAndType(uriForFile, var1.getContentResolver().getType(uriForFile));
        }else{
            var2.setDataAndType(Uri.fromFile(var0), getMIMEType(var0));
        }
        try {
            var1.startActivity(var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(var1, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }



    /**
     * 删除之前升级时下载的老的 apk 文件
     */
    public static void deleteOldApk() {
        File apkDir = StorageUtil.getApkFileDir();
        if (apkDir == null || apkDir.listFiles() == null || apkDir.listFiles().length == 0) {
            return;
        }

        // 删除文件
        StorageUtil.deleteFile(apkDir);
    }
}
