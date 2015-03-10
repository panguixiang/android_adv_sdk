package com.ydt.sdk.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by panguixiang on 2/5/15.
 */
public class IntelWalDownload {

    private DownloadManager downloadManager;
    private static final String DL_ID = "intelwal_downloadId";
    private Context context;
    private Activity activty;
    private long enqueue;
    private String publisherid="",intelplanId="",imei="";

    public IntelWalDownload(Context context,String down_url,
                               String publisherid, String intelplanId, String imei) {
        this.context = context;
        this.publisherid=publisherid;
        this.intelplanId=intelplanId;
        this.imei=imei;
        createSDCardDir();
        activty = (Activity)context;
        downloadManager = (DownloadManager)activty.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(down_url));
        //设置标题
        String fileName=down_url.substring(down_url.lastIndexOf("/")+1,down_url.length());
        request.setTitle(fileName);
        //request.setDescription("--download--");
        request.setDestinationInExternalPublicDir("/download/",fileName);
        enqueue = downloadManager.enqueue(request);
        activty.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        Toast.makeText(context.getApplicationContext(), "begin download " + fileName,
                Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueue);
                Cursor c = downloadManager.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {

                        IntelWalThreadTask task = new IntelWalThreadTask();
                        task.execute(publisherid,intelplanId,imei,
                                ContactAdServerUrl.app_index_server_download,context.getPackageName());
                    }
                }
            }
        }
    };



    public void createSDCardDir(){
        String path="";
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir =Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            path=sdcardDir.getPath()+"/download";
            File path1 = new File(path);
            if (!path1.exists() || !path1.isDirectory()) {
                //若不存在download文件夹，创建文件夹
                path1.mkdirs();
            }
        }
    }
}
