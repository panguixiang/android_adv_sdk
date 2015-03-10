package com.ydt.sdk.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by panguixiang on 12/31/14.
 */
public class CreateUnquIdenti {

    public static String createMdf5(Context c) {
        StringBuffer condition = new StringBuffer();
        TelephonyManager TelephonyMgr = (TelephonyManager)c.getSystemService(c.TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        if(szImei!=null && szImei.length()>0) {
            condition.append(szImei);
        }
        String m_szDevIDShort = "45" +
                Build.BOARD.length()%10 +
                Build.BRAND.length()%10 +
                Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 +
                Build.HOST.length()%10 +
                Build.ID.length()%10 +
                Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 +
                Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 +
                Build.TYPE.length()%10 +
                Build.USER.length()%10 ;
        condition.append(m_szDevIDShort);
        String m_szAndroidID = Settings.Secure.getString(c.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if(m_szAndroidID!=null && m_szAndroidID.length()>0) {
            condition.append(m_szAndroidID);
        }
        WifiManager wm = (WifiManager)c.getSystemService(c.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        if(m_szWLANMAC!=null && m_szWLANMAC.length()>0) {
            condition.append(m_szWLANMAC);
        }
        return md5(condition.toString());
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (Exception e) {
        	Log.i("create-imei-md5-error","-create-imei-md5--error-----"+e.getMessage()+"----------");
           
        }
        return "";
    }
}
