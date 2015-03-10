package com.ydt.sdk.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import java.util.Map;

public class UrlConnectionUtil {

  private HttpURLConnection conn;
  private URL url;
  private InputStream is;

  public String httpGet(Map<String,Object> paramMap, String server_url) throws Exception{
      StringBuffer sb = new StringBuffer();
      server_url+=prepareParam(paramMap);
      url = new URL(server_url);
      conn = (HttpURLConnection)url.openConnection();
      conn.connect();
      is=conn.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String line=null;
      while((line=br.readLine())!=null) {
          sb.append(line);
      }
      return sb.toString();

  }

    public String httpPost(Map<String,Object> paramMap, String server_url){
        String line ;
        StringBuffer sb = new StringBuffer();
        try{
            URL url = new  URL(server_url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            String paramStr = prepareParam(paramMap);
            conn.setDoInput(true );
            conn.setDoOutput(true );
            OutputStream os = conn.getOutputStream();
            os.write(paramStr.toString().getBytes("utf-8" ));
            os.close();
            BufferedReader br = new  BufferedReader( new InputStreamReader(conn.getInputStream()));
            while ( (line =br.readLine()) !=  null  ){
                sb.append(line);
            }
            br.close();
        }catch(Exception e) {
        	 Log.i("-httpPost-error-", "--httpPost-"+server_url+"-error-----" + e.getMessage() + "----------");
        } finally {
            return sb.toString();
        }
    }

    private  static  String prepareParam(Map<String,Object> paramMap){
        StringBuffer sb = new  StringBuffer();
        if (paramMap.isEmpty()){
            return   ""  ;
        }else {
            for (String key: paramMap.keySet()){
                String value = (String)paramMap.get(key);
                if (sb.length()< 1 ){
                    sb.append(key).append("=" ).append(value);
                }else {
                    sb.append("&" ).append(key).append( "=" ).append(value);
                }
            }
            return  sb.toString();
        }
    }


}
