package com.ydt.sdk.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by panguixiang on 2/5/15.
 */
public class IntegralWall {

    private Dialog loadingDialog;
    private  Context context;
    private String publisherID="";
    private int adver_type,showView=1;
    private String imei="";
    private IntelWalRelative relativeView = null;

    public IntegralWall(Context context, String publisherID, int adver_type) {
        this.context = context;
        this.publisherID = publisherID;
        this.adver_type = adver_type;
        this.imei=CreateUnquIdenti.createMdf5(context);
        ThreadTask task = new ThreadTask();
        task.execute("");
    }



    private class ThreadTask extends AsyncTask<String,Integer,Map<String, Object>> {

        @Override
        protected void onPreExecute() {
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Map<String, Object> doInBackground(String... params) {
            UrlConnectionUtil connectionUtil = new UrlConnectionUtil();
            Map<String,Object> paramMap = new HashMap<String, Object>();
            String result="";
            JSONObject json;
            try{
                paramMap.put("publisherid",publisherID);
                paramMap.put("adver_type",adver_type+"");
                paramMap.put("packageName",context.getPackageName());
                result = connectionUtil.httpGet(paramMap,ContactAdServerUrl.integral_server_index);
                Log.i("-IntegralWall-http-result","-IntegralWall-http-result--"+result+"----------");
                JSONTokener jsonParser = new JSONTokener(result);
                json = (JSONObject)jsonParser.nextValue();
                if(json.getInt("code")==200) {
                    relativeView = new IntelWalRelative(context,adver_type,json.getString("static_url"),json.getString("intel_h1"),
                            json.getJSONArray("wallList"),json.getString("changeIntelBackground"),publisherID,
                            imei,json.getString("layoutParentBackground"),json.getString("buttonBackground"),json.getString("click_button"),json.getString("closeImageUrl"));
                }

            }catch (Exception e) {
            	Log.i("-IntegralWall-http-error","-IntegralWall-http-error--"+e.getMessage()+"----------");
            } finally {
                return null;
            }
        }
        @Override
        protected void onProgressUpdate(Integer... progresses) {
        }
        //用来接收异步数据更新UI

        @Override
        protected void onPostExecute(final Map<String, Object> map) {
        }

        @Override
        protected void onCancelled() {
        }

    }

    public boolean showIntegralWal() {
        if(relativeView==null) {
            return false;
        }
        if(adver_type==1){
            ((Activity)context).setContentView(relativeView);
            showView=2;
        }
        if(adver_type==2) {
            if(loadingDialog==null) {
                loadingDialog = new Dialog(context);// 创建自定义样式dialog
                loadingDialog.setContentView(relativeView,
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));
            }
            loadingDialog.show();
        }
        return true;
    }


    public boolean onKeyBack(int keyCode, int layout_id){

        if(keyCode == KeyEvent.KEYCODE_BACK && showView==2) {
            ((Activity)context).setContentView(layout_id);
            showView=1;
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && adver_type==2) {
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return true;
    }

}
