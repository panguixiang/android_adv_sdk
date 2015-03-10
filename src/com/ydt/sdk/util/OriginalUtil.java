package com.ydt.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

import java.net.URL;
import java.util.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by panguixiang on 1/19/15.
 */
public class OriginalUtil {

    /**
     * banner广告http get请求
     * @param publisherid
     * @param innlinepid
     * @param cityCode
     * @param imei
     * @param context
     * @param maxwidth
     * @param maxlength
     * @return
     */
    public static Map<String, Object> createAdUi(String publisherid, String innlinepid, String cityCode,
                     String imei, final Context context, int maxwidth, int maxlength) {
        JSONObject json=null;
        String result="";
        Map<String, Object> map = new HashMap<String, Object>();
        List adList = new ArrayList<Object>();

        try {
            UrlConnectionUtil connectionUtil = new UrlConnectionUtil();
            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put("publisherid",publisherid);
            paramMap.put("innlinepid",innlinepid);
            paramMap.put("cityCode",cityCode);
            paramMap.put("imei",imei);
            paramMap.put("wifi",isWifi(context));
            paramMap.put("packageName",context.getPackageName());
            paramMap.put("maxwidth",maxwidth+"");
            paramMap.put("maxlength", maxlength+"");
            result = connectionUtil.httpGet(paramMap, ContactAdServerUrl.app_index_server_url);
            Log.i("http", "--http-"+ContactAdServerUrl.app_index_server_url+"--result------" +result + "----------");
            JSONTokener jsonParser = new JSONTokener(result);
            json = (JSONObject)jsonParser.nextValue();
            if(json.getInt("code")!=200) {
                return map;
            }
            String auto_value = json.getString("auto_value");
            JSONArray jsonList = json.getJSONArray("adverList");
            String imgUrl = json.getString("static_url");
            map.put("auto_value",auto_value);
            if(jsonList==null || jsonList.length()==0) {
                map.put("adverList", adList);
                return map;
            }
            JSONObject orignAdv=null;
            for(int i =0 ;i<jsonList.length();i++) {
                orignAdv=(JSONObject)jsonList.get(i);
                if(orignAdv==null) {
                   continue;
                }
                if(orignAdv.getInt("orig_type")==1 || orignAdv.getInt("orig_type")==4 || orignAdv.getInt("orig_type")==6) {
                    continue;
                }
                Model model = new Model();
                if(orignAdv.getInt("orig_type")==2) {
                    ImageView image = new ImageView(context);
                    Drawable drawable =
                            Drawable.createFromStream(new URL(imgUrl+orignAdv.getString("media")).openStream(), "image.png");
                    image.setImageDrawable(drawable);
                    image.setClickable(true);
                    model.setAdv_type(2);
                    model.setMedia(image);
                }
                else if(orignAdv.getInt("orig_type")==3) {//Gif 动画
                    GifView image = new GifView(context,imgUrl+orignAdv.getString("media"));
                    image.setClickable(true);
                    model.setAdv_type(3);
                    model.setMedia(image);
                }
                else if(orignAdv.getInt("orig_type")==5) {
                    ImageView image = new ImageView(context);
                    Drawable drawable =
                            Drawable.createFromStream(new URL(imgUrl+orignAdv.getString("icon")).openStream(), "image.png");
                    image.setImageDrawable(drawable);
                    image.setClickable(true);
                    model.setAdv_type(5);
                    Object[] list = new Object[3];
                    list[0]=image;
                    list[1]=orignAdv.getString("media");
                    list[2]=orignAdv.getString("media_link");
                    model.setMedia(list);
                }
                model.setLinkType(orignAdv.getInt("link_type"));
                model.setOriginId(orignAdv.getInt("id"));
                model.setMediaLink(orignAdv.getString("media_link"));
                adList.add(model);
            }
            map.put("adverList", adList);
        } catch (Exception e) {
            Log.i("http-error", "---http-"+ContactAdServerUrl.app_index_server_url+"---error-----" + e.getMessage() + "----------");
        } finally {
            return map;
        }
       }

    /**
     * 插屏广告http get请求
     * @param publisherid
     * @param innlinepid
     * @param cityCode
     * @param imei
     * @param context
     * @param inner_screen_type
     * @param className
     * @param maxwidth
     * @param maxlength
     */
    public static void innlineHttp(String publisherid, String innlinepid,String cityCode,String imei,
                 final Context context, int inner_screen_type, String className, int maxwidth, int maxlength) {
        String result="";
        JSONObject json = null;
        JSONTokener jsonParser=null;
        try {
            UrlConnectionUtil connectionUtil = new UrlConnectionUtil();
            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put("publisherid",publisherid);
            paramMap.put("innlinepid",innlinepid);
            paramMap.put("cityCode",cityCode);
            paramMap.put("imei",imei);
            paramMap.put("packageName",context.getPackageName());
            paramMap.put("wifi",isWifi(context));
            paramMap.put("inner_screen_type",inner_screen_type+"");
            paramMap.put("maxwidth",maxwidth+"");
            paramMap.put("maxlength", maxlength+"");
            result = connectionUtil.httpGet(paramMap, ContactAdServerUrl.app_innerline_server_index);
            Log.i("http", "-http--"+ContactAdServerUrl.app_innerline_server_index+"--result-----" + result + "----------");
            jsonParser = new JSONTokener(result);
            json = (JSONObject)jsonParser.nextValue();
            if(json.getInt("code")==200) {
                SharedPreferences mSharedPreferences =
                        context.getSharedPreferences(className, context.MODE_WORLD_READABLE);
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putInt("auto_value", json.getInt("auto_value"));
                edit.putString("adverList", json.getString("adverList"));
                edit.putString("static_url", json.getString("static_url"));
                edit.commit();
            }
        } catch  (Exception e) {
            Log.i("http-error", "-http--"+ContactAdServerUrl.app_innerline_server_index+"--error-----" + e.getMessage() + "----------");
        }
    }

        private static String isWifi(Context mContext) {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null
                    && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return "wifi";
            }
            return null;
        }




}
