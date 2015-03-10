package com.ydt.sdk.util;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by panguixiang on 1/23/15.
 */
public class ThreadTask extends AsyncTask<String,Integer,Map<String, Object>> {


    @Override
    protected void onPreExecute() {
    }

    //doInBackground方法内部执行后台任务,不可在此方法内修改UI
    @Override
    protected Map<String, Object> doInBackground(String... params) {
        UrlConnectionUtil connectionUtil = new UrlConnectionUtil();
        Map<String,Object> paramMap = new HashMap<String, Object>();
        try{
            paramMap.put("publisherid",params[0]);
            paramMap.put("innlinepid",params[1]);
            paramMap.put("originid",params[2]);
            paramMap.put("imei",params[3]);
            paramMap.put("packageName",params[5]);
            connectionUtil.httpGet(paramMap,params[4]);
        }catch (Exception e) {
        	  Log.i("---ThreadTask--http-error---",
              		"---ThreadTask--http-"+params[4]+"-error---"+ e.getMessage());
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
