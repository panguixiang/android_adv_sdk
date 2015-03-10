package com.ydt.sdk;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.ydt.sdk.util.ContactAdServerUrl;
import com.ydt.sdk.util.UrlConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;


public class WebViewActivity extends Activity {

    LinearLayout layout;
    WebView webView;
    String publisherID="";
    String innlinepid="";
    String imei="";
    int originID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("url");


        layout = new LinearLayout(this);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
        layout.addView(webView);
        setContentView(layout);

        if(!bundle.containsKey("intelwal")) {//非积分墙列表过来的
            publisherID = bundle.getString("publisherID");
            innlinepid = bundle.getString("innlinepid");
            originID =  bundle.getInt("originID");
            imei =  bundle.getString("imei");
            PageTask task = new PageTask();
            task.execute("");
        }
    }


    class PageTask extends AsyncTask<String,Integer,Map<String, Object>> {
        @Override
        protected void onPreExecute() {
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Map<String, Object> doInBackground(String... params) {
            UrlConnectionUtil connectionUtil = new UrlConnectionUtil();
            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put("originid",originID+"");
            paramMap.put("publisherid",publisherID);
            paramMap.put("innlinepid",innlinepid);
            paramMap.put("imei", imei);
            paramMap.put("packageName", (WebViewActivity.this).getPackageName());
            JSONObject json = null;
            try {
                String result = connectionUtil.httpGet(paramMap,ContactAdServerUrl.app_index_server_click_url);
                if(result==null || result.length()<10){
                    return null;
                }
                JSONTokener jsonParser = new JSONTokener(result);
                json = (JSONObject)jsonParser.nextValue();
                if(json.getInt("code")==200) {
                    Log.i("WebViewActivity-successful", "---click-successful---------------");
                }
            } catch (Exception e) {
                try{
                    Log.i("WebViewActivity-error", "---click-error---------------"+json.getString("message"));
                }catch (JSONException err) {
                    Log.i("WebViewActivity-error", "---click-JSONException------------"+err.getMessage());
                }

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
}
