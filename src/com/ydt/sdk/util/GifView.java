package com.ydt.sdk.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.Log;
import android.view.View;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by panguixiang on 1/22/15.
 */
public class GifView extends View {
    private Movie mMovie;
    private long mMovieStart;
    private Context context;
    private int start=0;
    private String media = "";
    public GifView(Context context, String media) {
        super(context);
        this.context = context;
        this.media = media;
        urlGif();
    }

    private void urlGif(){
        try {
            URL url = new URL(media);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            InputStream in = connection.getInputStream();
            byte[] array = streamToBytes(in);
            mMovie = Movie.decodeByteArray(array, 0, array.length);
            in.close();
        } catch (Exception e) {
        	Log.i("http-gif-error","-http--gif--"+media+"--error-----"+e.getMessage()+"----------");
        }
    }

    int relTime;
    @Override
    public void onDraw(Canvas canvas) {
        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) { // first time
            mMovieStart = now;
        }
        if (mMovie != null && start>0) {
            int dur = mMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }
            relTime = (int) ((now - mMovieStart) % dur);
            mMovie.setTime(relTime);
            mMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }
    public void setStart(int start){
        this.start=start;
    }

    private static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (java.io.IOException e) {
        	Log.i("gif-InputStream-error","-gif-InputStream-error--"+e.getMessage()+"----------");
        } finally {
            return os.toByteArray();
        }
    }


}