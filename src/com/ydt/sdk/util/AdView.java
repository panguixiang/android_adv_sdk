package com.ydt.sdk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ydt.sdk.WebViewActivity;


/**
 * Created by panguixiang on 1/20/15.
 */
public class AdView extends RelativeLayout{

    protected Activity context;
    private List<View> listViews; // 图片组
    RelativeLayout ovalLayout;
    List<Model> list;
    int index=0,timer=3000,maxWidth=0,maxHeight=0;
    String publisherID="";
    String innlinepid="";
    String imei="";
    List<Integer> origList = null;
    private TextView close;
    public AdView(Activity context, String publisherID, String innlinepid, RelativeLayout ovalLayout) {
        super(context);
        this.context=context;
        WindowManager wm = context.getWindowManager();
        this.maxWidth= wm.getDefaultDisplay().getWidth();
        this.maxHeight=(wm.getDefaultDisplay().getHeight())/8;
        ovalLayout.setLayoutParams(new LayoutParams(this.maxWidth, this.maxHeight));
        listViews = new ArrayList<View>();
        origList = new ArrayList<Integer>();
        this.ovalLayout=ovalLayout;
        this.publisherID=publisherID;
        this.innlinepid=innlinepid;
        this.imei = CreateUnquIdenti.createMdf5(context);
        PageTask task = new PageTask();
        task.execute("");
    }
    private class PageTask extends AsyncTask<String,Integer,Map<String, Object>> {

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Map<String, Object> doInBackground(String... params) {
            Map<String, Object> map = OriginalUtil.createAdUi(publisherID,innlinepid,
                    "",imei,context,maxWidth,maxHeight);
            if(map.get("auto_value") != null) {
                timer = Integer.parseInt((String)map.get("auto_value"));
            }
            return map;
        }
        //用来接收异步数据更新UI

        @Override
        protected void onPostExecute(final Map<String, Object> map) {
            try {
                Object objMap = map.get("adverList");
                list = (List<Model>) objMap;
                View view = null;
                for (Model model : list) {
                    final int link_type = model.getLinkType();
                    final int id = model.getOriginId();
                    if(model.getAdv_type()==2) {
                        view = (View) model.getMedia();
                        setViewClickListener(link_type,view, model.getMediaLink(),  id);
                    }
                    else if (model.getAdv_type() == 3) { //gif类型
                        view = (View) model.getMedia();
                        setViewClickListener(link_type,view, model.getMediaLink(),  id);
                    }
                    else if (model.getAdv_type() == 5 ) {//video类型
                        final Object[] array = (Object[]) model.getMedia();
                        view = (View) array[0];
                        view.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                        new VideoSurfaceDemo(context, (String)array[2], (String)array[1],
                                                 publisherID, innlinepid, id, imei,link_type);
                            }
                        });

                    }
                    origList.add(id);
                    listViews.add(view);
                }
            } catch (Exception e) {
                Log.i("AdView-error","-AdView--error-----"+e.getMessage()+"----------");
            }
        }

    }


    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        int orig_id=0;
        public void run () {
            if(listViews.size()>0) {
                orig_id = origList.get(index);
                scrollView(listViews.get(index),orig_id);
                if(orig_id>0) {
                    origList.set(origList.indexOf(orig_id), 0);
                }
                if(index<(listViews.size()-1)) {
                    index=index+1;
                } else {
                    index=0;
                }

            }
            handler.postDelayed(this,timer);
        }
    };

    private void scrollView(View view,int orig_id) {
        if(close==null && listViews.size()>0) {
            close = new TextView(context);
            close.setText("close");
            ovalLayout.addView(close);
            close.setOnClickListener(closeClickListener);
        }
        if(ovalLayout.getChildAt(1)!=null) {
            ovalLayout.removeViewAt(0);
        }
        //ovalLayout.removeAllViewsInLayout();
        ovalLayout.addView(view);
        close.bringToFront();
        if(view instanceof GifView) {
            ((GifView)view).setStart(1);
        }
        if(origList.get(origList.size()-1)>0) { //第一次轮播 显示后发送有效填充
            ThreadTask task = new ThreadTask();
            task.execute(publisherID,innlinepid,orig_id+"",imei,
                    ContactAdServerUrl.app_index_server_fill_url,context.getPackageName());
        }
    }


    private void setViewClickListener(int linkType,View view, final String media_link, final int originID) {
        if(linkType==1) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, WebViewActivity.class);
                    intent.putExtra("url", media_link);
                    intent.putExtra("publisherID",publisherID);
                    intent.putExtra("innlinepid", innlinepid);
                    intent.putExtra("originID", originID);
                    intent.putExtra("imei", imei);
                    context.startActivity(intent);
                }
            });
        } else if(linkType==2) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SampDownloadManager(context,media_link,
                            publisherID, innlinepid, originID+"", imei);
                }
            });
        }
    }

    private View.OnClickListener closeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            handler.removeCallbacks(runnable);
            ovalLayout.removeAllViews();
            ovalLayout.destroyDrawingCache();
        }
    };

    public void showAdView() {
        handler.postDelayed(runnable, timer);
    }
}
