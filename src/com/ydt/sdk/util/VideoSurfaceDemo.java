package com.ydt.sdk.util;

import android.util.Log;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.*;
import android.media.MediaPlayer;
import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import com.ydt.sdk.R;
import android.media.AudioManager;
/**
 * Created by panguixiang on 1/27/15.
 */
public class VideoSurfaceDemo implements OnCompletionListener,OnErrorListener,OnInfoListener,
        OnPreparedListener,SurfaceHolder.Callback{
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private Context context;
    private int originID=0,link_type=0;
    private Dialog mDialog;
    private String media="",media_url = "",url="",publisherID="",innlinepid="",imei="";
    //private boolean readyToPlay = false;

    public VideoSurfaceDemo(Context context, String media_url, String media,
                            String publisherID, String innlinepid, int originID, String imei, int link_type){
        this.context=context;
        this.media=media;
        this.media_url=media_url;
        this.publisherID=publisherID;
        this.innlinepid=innlinepid;
        this.originID=originID;
        this.imei=imei;
        this.link_type=link_type;
        createPlayer();
    }


    protected void createPlayer() {
        surfaceView = new SurfaceView(context);
        mDialog = new Dialog(context, R.style.dialog);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = -80;
        wl.y = 60;
        window.setAttributes(wl);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.setContentView(surfaceView);
        mDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        mDialog.show();
        //给SurfaceView添加CallBack监听
        holder = surfaceView.getHolder();
        holder.addCallback(this);

        //下面开始实例化MediaPlayer对象
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnInfoListener(this);
        player.setOnPreparedListener(this);
        //player.setOnSeekCompleteListener(this);
        //player.setOnVideoSizeChangedListener(this);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.v("Begin:::", "surfaceDestroyed called");
        //然后指定需要播放文件的路径，初始化MediaPlayer
//        String dataPath =
//                Environment.getExternalStorageDirectory().getPath()+"/w.mp4";
        try {
            player.setDataSource(media);
            Log.v("Next:::", "surfaceDestroyed called");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // 当Surface尺寸等参数改变时触发
        Log.v("Surface Change:::", "surfaceChanged called");
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 当SurfaceView中的Surface被创建的时候被调用
        //在这里我们指定MediaPlayer在当前的Surface中进行播放
        player.setDisplay(holder);
        //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
        player.prepareAsync();

    }
    //终止播放时触发
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        player.stop();
        Log.v("Surface Destory:::", "surfaceDestroyed called");
    }
//    @Override
//    public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
//        // 当video大小改变时触发
//        //这个方法在设置player的source后至少触发一次
//        Log.v("Video Size Change", "onVideoSizeChanged called");
//
//    }
//    @Override
//    public void onSeekComplete(MediaPlayer arg0) {
//        // seek操作完成时触发
//        Log.v("Seek Completion", "onSeekComplete called");
//
//    }
    //开始播放时触发
    @Override
    public void onPrepared(MediaPlayer player) {
        // 当prepare完成后，该方法触发，在这里我们播放视频
        //首先取得video的宽和高
        player.start();
//        ThreadTask task = new ThreadTask();
//        task.execute(publisherID,innlinepid,originID+"",imei,ContactAdServerUrl.app_index_server_player_begin);
    }
    @Override
    public boolean onInfo(MediaPlayer player, int whatInfo, int extra) {
        // 当一些特定信息出现或者警告时触发
        switch(whatInfo){
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                break;
        }
        return false;
    }
    @Override
    public boolean onError(MediaPlayer player, int whatError, int extra) {
        Log.v("Play Error:::", "onError called");
        switch (whatError) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.v("Play Error:::", "MEDIA_ERROR_SERVER_DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.v("Play Error:::", "MEDIA_ERROR_UNKNOWN");
                break;
            default:
                break;
        }
        return false;
    }
    @Override
    public void onCompletion(MediaPlayer player) {
        // 当MediaPlayer播放完成后触发
        Log.v("Play Over:::", "onComletion called");
        mDialog.cancel();
//        ThreadTask task = new ThreadTask();
//        task.execute(publisherID,innlinepid,originID+"",imei,ContactAdServerUrl.app_index_server_player_end);
    }

}