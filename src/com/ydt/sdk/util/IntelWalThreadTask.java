package com.ydt.sdk.util;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by panguixiang on 2/5/15.
 */
public class IntelWalThreadTask extends ThreadTask{


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
            paramMap.put("intelplanId",params[1]);
            paramMap.put("imei",params[2]);
            paramMap.put("packageName",params[4]);
            connectionUtil.httpGet(paramMap,params[3]);
        }catch (Exception e) {
            Log.i("---IntelWalThreadTask--http-error---",
            		"---IntelWalThreadTask--http-"+params[3]+"-error---"+ e.getMessage());
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
