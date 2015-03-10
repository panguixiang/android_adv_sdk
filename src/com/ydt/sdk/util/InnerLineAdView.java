package com.ydt.sdk.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.content.SharedPreferences;
import com.ydt.sdk.R;
import com.ydt.sdk.WebViewActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by panguixiang on 1/20/15.
 */
public class InnerLineAdView extends View{

    protected Activity context;
    int index=0,timer=300,screenWidth=0,screenHeight=0,inner_screen_type;
    String publisherID="";
    String innlinepid="";
    String imei="",static_url="",jsonArrayListStr="";
    List<Integer> origList = new ArrayList<Integer>();
    List<Model> modelList = new ArrayList<Model>();
    Dialog loadingDialog;
    public InnerLineAdView(Activity context, String publisherID, String innlinepid, int inner_screen_type) {
        super(context);
        this.context=context;
        this.publisherID=publisherID;
        this.innlinepid=innlinepid;
        this.imei = CreateUnquIdenti.createMdf5(context);
        this.inner_screen_type=inner_screen_type;
        loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(true);
        loadingDialog.setOnKeyListener(dialogOnkeyListener);
       // loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        WindowManager wm = context.getWindowManager();
        if(inner_screen_type==2) {//普通插屏
            this.screenWidth = (wm.getDefaultDisplay().getWidth()/2+50);
            this.screenHeight = (wm.getDefaultDisplay().getHeight()/3+50);
        } else  {//全屏插屏
            this.screenWidth = (wm.getDefaultDisplay().getWidth()-5);
            this.screenHeight = (wm.getDefaultDisplay().getHeight()-5);
        }
        PageTask task = new PageTask();
        task.execute("");
    }
    class PageTask extends AsyncTask<String,Integer,Map<String, Object>> {
        @Override
        protected void onPreExecute() {
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Map<String, Object> doInBackground(String... params) {
            String className = context.getLocalClassName();
            SharedPreferences mSharedPreferences  =
                    context.getSharedPreferences(className, context.MODE_WORLD_READABLE);

            jsonArrayListStr = mSharedPreferences.getString("adverList","");
            if(jsonArrayListStr.length()<10) {
                OriginalUtil.innlineHttp(publisherID,innlinepid,
                        "",imei,context,inner_screen_type, className,screenWidth,screenHeight);
            }
            jsonArrayListStr = mSharedPreferences.getString("adverList","");
            timer = mSharedPreferences.getInt("auto_value",0);
            static_url = mSharedPreferences.getString("static_url","");
            addViewList(jsonArrayListStr);
            OriginalUtil.innlineHttp(publisherID,innlinepid,
                    "",imei,context,inner_screen_type,className,screenWidth,screenHeight);
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... progresses) {
        }

        @Override
        protected void onCancelled() {
        }

    }

    private boolean addViewList(String jsonArrayListStr) {
        JSONArray jsonList = null;
        if(jsonArrayListStr==null || jsonArrayListStr.length()<10) {
            return false;
        }
        JSONTokener jsonParser = new JSONTokener(jsonArrayListStr);
        try {
            jsonList = (JSONArray)jsonParser.nextValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(jsonList==null || jsonList.length()==0) {
            return false;
        }
        JSONObject json=null;
        try {
        for(int i =0 ;i<jsonList.length();i++) {

                json=(JSONObject)jsonList.get(i);
                if(json==null) {
                    continue;
                }
                if(json.getInt("orig_type")==1 || json.getInt("orig_type")==4 || json.getInt("orig_type")==6) {
                    continue;
                }
                Model model = new Model();
                if(json.getInt("orig_type")==2) {
                    ImageView image = new ImageView(context);
                    Drawable drawable =
                            Drawable.createFromStream(new URL(static_url+json.getString("media")).openStream(), "image.png");
                    image.setImageDrawable(drawable);
                    image.setClickable(true);
                    model.setAdv_type(2);
                    model.setMedia(image);
                    setClickListener(context, image, json);
                }
                else if(json.getInt("orig_type")==3) {//Gif 动画
                    GifView image = new GifView(context,json.getString("media"));
                    image.setClickable(true);
                    model.setAdv_type(3);
                    model.setMedia(image);
                    setClickListener(context, image, json);
                }
                else if(json.getInt("orig_type")==5) {
                    ImageView image = new ImageView(context);
                    Drawable drawable =
                            Drawable.createFromStream(new URL(static_url+json.getString("icon")).openStream(), "image.png");
                    image.setImageDrawable(drawable);
                    image.setClickable(true);
                    model.setAdv_type(5);
                    Object[] list = new Object[3];
                    list[0]=image;
                    list[1]=json.getString("media");
                    list[2]=json.getString("media_link");
                    model.setMedia(list);
                }
                model.setLinkType(json.getInt("link_type"));
                model.setOriginId(json.getInt("id"));
                model.setMediaLink(json.getString("media_link"));
                origList.add(json.getInt("id"));
                modelList.add(model);
            }
        } catch (Exception e) {
        	Log.i("-InnerLineAdView-createView-error","-InnerLineAdView-createView-error--"+e.getMessage()+"----------");
            e.printStackTrace();
        }
        return true;
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        int orig_id=0;
        public void run () {
            if(modelList.size()>0) {
                scrollView(modelList.get(index));
                orig_id = origList.get(index);
                if(orig_id>0) {
                    origList.set(origList.indexOf(orig_id), 0);
                }
                if(index<(modelList.size()-1)) {
                    index=index+1;
                } else {
                    index=0;
                }

            }
            handler.postDelayed(this,timer);
        }
    };

    private void scrollView(Model model) {
        if(model.getAdv_type()==3) {
            GifView gifView = (GifView)model.getMedia();
            gifView.setStart(1);
            loadingDialog.setContentView(gifView, new LinearLayout.LayoutParams(
                    screenWidth , screenHeight));// 设置布局
        } else if(model.getAdv_type()==2){
            loadingDialog.setContentView((View) model.getMedia(), new LinearLayout.LayoutParams(
                    screenWidth , screenHeight));// 设置布局
        } else if(model.getAdv_type()==5) {
            final Object[] array = (Object[]) model.getMedia();
            View view = (View) array[0];
            loadingDialog.setContentView(view, new LinearLayout.LayoutParams(
                    screenWidth , screenHeight));// 设置布局
            final int originId=model.getOriginId();
            final int link_type=model.getLinkType();
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new VideoSurfaceDemo(context, (String)array[2], (String)array[1],
                            publisherID, innlinepid, originId, imei,link_type);
                }
            });
        }

        if(origList.get(origList.size()-1)>0) { //第一次轮播 显示后发送有效填充
            ThreadTask task = new ThreadTask();
            task.execute(publisherID,innlinepid,model.getOriginId()+"",imei,
                    ContactAdServerUrl.app_index_server_fill_url,context.getPackageName());
        }

    }

    private void setClickListener(Activity context,View view, final JSONObject json){
         OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(json.getInt("link_type")==1) {
                        Intent intent = new Intent();
                        intent.setClass(getContext(), WebViewActivity.class);
                        intent.putExtra("url", json.getString("media_link"));
                        intent.putExtra("publisherID",publisherID);
                        intent.putExtra("innlinepid", innlinepid);
                        intent.putExtra("originID", json.getInt("id"));
                        intent.putExtra("imei",imei);
                        getContext().startActivity(intent);
                    } else if(json.getInt("link_type")==2){
                        new SampDownloadManager(getContext(),json.getString("media_link"),
                                publisherID, innlinepid, json.getInt("id")+"", imei);
                    }
                } catch (Exception e) {
                	Log.i("-InnerLineAdView-onclick-error","-InnerLineAdView-onclick-error--"+e.getMessage()+"----------");
                }

            }
        };
        view.setOnClickListener(clickListener);
    }

    public void innerStart() {
        handler.post(runnable);
        loadingDialog.show();
    }

    private DialogInterface.OnKeyListener dialogOnkeyListener
            = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            if(KeyEvent.KEYCODE_BACK==i) {
                handler.removeCallbacks(runnable);
                loadingDialog.hide();
            }
            return false;
        }
    };

}
