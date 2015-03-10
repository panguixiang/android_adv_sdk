package com.ydt.sdk.util;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.content.Context;
import android.widget.TextView;
import com.ydt.sdk.WebViewActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by panguixiang on 2/3/15.
 */
public class IntelWalRelative extends RelativeLayout{

    private ImageView imageView;
    private TextView titleView;
    private TextView descView;
    private TextView intel_desc;
    private TextView intelwal;
    private ImageButton imageButton;
    private Context context;
    private String  publisherID="",imei="";

    /**
     *
     * @param context
     * @param array 积分墙列表JsonArray
     * @param imgUrl 静态资源服务器地址
     * @param layoutParentBackground 积分墙列表头部背景图
     * @param intel_h1 积分墙列表头部h1标题
     * @param changeIntelBackground 积分墙列表头部右边按钮（换一批）
     * @param publisherID
     * @param imei
     * @param buttonBackground 积分墙列表头部左边按钮
     * @param click_button 列表按钮
     * @param closeImageUrl 插屏广告关闭按钮
     * @throws Exception
     */
    public IntelWalRelative(Context context,int adver_types,String imgUrl,String intel_h1,JSONArray array,
                  String changeIntelBackground, String publisherID, String imei,
                  String layoutParentBackground, String buttonBackground, String click_button,String closeImageUrl) throws Exception{
        super(context);
        this.context=context;
        this.publisherID=publisherID;
        this.imei=imei;

        this.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        if(adver_types==1) {
            RelativeLayout layout01 = new RelativeLayout(context);
            layout01.setId(layout01.generateViewId());
            layout01.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            Drawable layout01Drawable =
                    Drawable.createFromStream(new URL(imgUrl+layoutParentBackground).openStream(), "image.png");
            layout01.setBackgroundDrawable(layout01Drawable);
            this.addView(layout01);
            createTopBanner(context, layout01, imgUrl, buttonBackground,intel_h1, changeIntelBackground);
            createList(array, layout01, imgUrl, click_button);
        } else if(adver_types==2) {
//            ImageView closeImageView = new ImageView(context);
//            Drawable closeImageDrawable =
//                    Drawable.createFromStream(new URL(imgUrl+closeImageUrl).openStream(), "image.png");
//            closeImageView.setImageDrawable(closeImageDrawable);
//            closeImageView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                    LayoutParams.WRAP_CONTENT));
//            LayoutParams closeImageViewRule = (RelativeLayout.LayoutParams)closeImageView.getLayoutParams();
//            closeImageViewRule.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            closeImageViewRule.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            closeImageViewRule.setMargins(-20, -60, 0, 0);
//            closeImageView.setLayoutParams(closeImageViewRule);
//            this.addView(closeImageView);

            createDialogList(array, imgUrl, click_button);
        }



    }

    /**
     * 构造普通列表积分墙头部banner
     * @param context
     * @param layout01
     * @param imgUrl
     * @param buttonBackground
     * @param intel_h1
     * @param changeIntelBackground
     * @throws Exception
     */
    private static void createTopBanner(Context context, RelativeLayout layout01, String imgUrl, String buttonBackground,
                                       String intel_h1, String changeIntelBackground) throws Exception{
        ImageButton button = new ImageButton(context);
        button.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        LayoutParams buttonViewRule = (RelativeLayout.LayoutParams)button.getLayoutParams();
        buttonViewRule.setMargins(5, 16, 0, 0);
        button.setLayoutParams(buttonViewRule);
        Drawable buttonDrawable =
                Drawable.createFromStream(new URL(imgUrl+buttonBackground).openStream(), "image.png");
        button.setBackgroundDrawable(buttonDrawable);


        TextView textView = new TextView(context);
        textView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        LayoutParams textViewRule = (RelativeLayout.LayoutParams)textView.getLayoutParams();
        textViewRule.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(textViewRule);
        textView.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        textView.setTextSize(30);
        textView.setTextColor(Color.rgb(236,235,33));
        textView.setText(intel_h1);


        ImageView changeIntelWal = new ImageView(context);
        changeIntelWal.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        LayoutParams button01Rule = (RelativeLayout.LayoutParams)changeIntelWal.getLayoutParams();
        button01Rule.setMargins(0, 18, 0, 0);
        button01Rule.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        changeIntelWal.setLayoutParams(button01Rule);
        Drawable changeIntelWalDrawable =
                Drawable.createFromStream(new URL(imgUrl+changeIntelBackground).openStream(), "image.png");
        changeIntelWal.setImageDrawable(changeIntelWalDrawable);
        layout01.addView(button);
        layout01.addView(textView);
        layout01.addView(changeIntelWal);
    }

    /**
     * 构造列表积分墙
     * @param array 积分墙列表JsonArray
     * @param layout
     * @param imgUrl
     * @throws Exception
     */
    private void createList(JSONArray array, RelativeLayout layout, String imgUrl, String click_button) throws Exception{
        RelativeLayout nowRelative = layout;
        RelativeLayout layout02;
        JSONObject json = null;
        for(int i=0;i<array.length();i++) {
            json = array.getJSONObject(i);
            layout02 = new RelativeLayout(context);
            layout02.setId(layout02.generateViewId());
            layout02.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            LayoutParams layout02Rule = (RelativeLayout.LayoutParams)layout02.getLayoutParams();
            layout02Rule.addRule(RelativeLayout.BELOW,nowRelative.getId());
            layout02Rule.setMargins(5, 18, 0, 0);
            layout02.setLayoutParams(layout02Rule);
            layout02.setBackgroundColor(Color.rgb(241,241,241));
            nowRelative = layout02;
            //

            imageView = new ImageView(context);
            imageView.setId(imageView.generateViewId());
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(90,90));
            LayoutParams imageViewRule = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
            imageViewRule.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            imageViewRule.setMargins(30, 10, 0, 0);
            imageView.setLayoutParams(imageViewRule);
            Drawable drawable =
                    Drawable.createFromStream(new URL(imgUrl+json.getString("adver_img")).openStream(), "image.png");
            imageView.setImageDrawable(drawable);
            layout02.addView(imageView);


            titleView = new TextView(context);
            titleView.setId(titleView.generateViewId());
            titleView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            LayoutParams titleViewRule = (RelativeLayout.LayoutParams)titleView.getLayoutParams();
            titleViewRule.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            titleViewRule.setMargins(10, 0, 0, 0);
            titleView.setLayoutParams(titleViewRule);
            titleView.setTextSize(24);
            titleView.setTextColor(Color.rgb(205,198,198));
            titleView.setText(json.getString("adver_title"));
            titleView.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
            layout02.addView(titleView);

            descView = new TextView(context);
            descView.setId(descView.generateViewId());
            descView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            LayoutParams descViewRule = (RelativeLayout.LayoutParams)descView.getLayoutParams();
            descViewRule.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            descViewRule.addRule(RelativeLayout.BELOW,titleView.getId());
            descViewRule.setMargins(10, 0, 0, 0);
            descView.setLayoutParams(descViewRule);
            descView.setTextColor(Color.rgb(205,210,210));
            descView.setTextSize(18);
            descView.setText(json.getString("adver_desc"));
            layout02.addView(descView);

            intel_desc = new TextView(context);
            intel_desc.setId(intel_desc.generateViewId());
            intel_desc.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            LayoutParams intel_descViewRule = (RelativeLayout.LayoutParams)intel_desc.getLayoutParams();
            intel_descViewRule.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            intel_descViewRule.addRule(RelativeLayout.BELOW,descView.getId());
            intel_descViewRule.setMargins(10, 0, 0, 0);
            intel_desc.setLayoutParams(intel_descViewRule);
            intel_desc.setTextColor(Color.rgb(248,83,83));
            intel_desc.setTextSize(18);
            intel_desc.setText(json.getString("intel_desc"));
            layout02.addView(intel_desc);


            intelwal = new TextView(context);
            intelwal.setId(intelwal.generateViewId());
            intelwal.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            LayoutParams intelwalViewRule = (RelativeLayout.LayoutParams)intelwal.getLayoutParams();
            intelwalViewRule.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            intelwalViewRule.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            intelwalViewRule.setMargins(0,0,10,0);
            intelwal.setLayoutParams(intelwalViewRule);
            intelwal.setTextSize(25);
            intelwal.setTextColor(0xffff00ff);
            intelwal.setText(json.getString("integral"));
            layout02.addView(intelwal);


            imageButton = new ImageButton(context);
            imageButton.setId(imageButton.generateViewId());
            imageButton.setLayoutParams(new RelativeLayout.LayoutParams(60,60));
            LayoutParams imageButtonRule = (RelativeLayout.LayoutParams)imageButton.getLayoutParams();
            imageButtonRule.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageButtonRule.addRule(RelativeLayout.BELOW,intelwal.getId());
            imageButtonRule.addRule(RelativeLayout.ALIGN_BOTTOM,imageView.getId());
            imageButtonRule.setMargins(0,0,10,0);
            imageButton.setLayoutParams(imageButtonRule);
            Drawable drawable2 =
                    Drawable.createFromStream(new URL(imgUrl+click_button).openStream(), "image.png");
            imageButton.setImageDrawable(drawable2);
            addOnclickListener(json.getInt("id"), imageButton, json.getString("adv_url"), json.getInt("link_type"));
            layout02.addView(imageButton);

            this.addView(layout02);
        }
    }




    /**
     * 构造插屏积分墙列表
     * @param array 积分墙列表JsonArray
     * @param imgUrl
     * @param click_button 插屏积分墙列表点击按钮
     * @throws Exception
     */
    private void createDialogList(JSONArray array,String imgUrl, String click_button) throws Exception{
        RelativeLayout nowRelative = this;
        RelativeLayout layout02;
        JSONObject json = null;
        for(int i=0;i<array.length();i++) {
            json = array.getJSONObject(i);
            layout02 = new RelativeLayout(context);
            layout02.setId(layout02.generateViewId());
            layout02.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            LayoutParams layout02Rule = (RelativeLayout.LayoutParams)layout02.getLayoutParams();
            layout02Rule.addRule(RelativeLayout.BELOW,nowRelative.getId());
            layout02Rule.setMargins(5, 0, 0, 0);
            layout02.setLayoutParams(layout02Rule);
            layout02.setBackgroundColor(Color.rgb(241,241,241));
            nowRelative = layout02;

            imageView = new ImageView(context);
            imageView.setId(imageView.generateViewId());
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(90,90));
            LayoutParams imageViewRule = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
            imageViewRule.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            imageViewRule.setMargins(30, 10, 0, 0);
            imageView.setLayoutParams(imageViewRule);
            Drawable drawable =
                    Drawable.createFromStream(new URL(imgUrl+json.getString("adver_img")).openStream(), "image.png");
            imageView.setImageDrawable(drawable);
            layout02.addView(imageView);


            titleView = new TextView(context);
            titleView.setId(titleView.generateViewId());
            titleView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            LayoutParams titleViewRule = (RelativeLayout.LayoutParams)titleView.getLayoutParams();
            titleViewRule.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            titleViewRule.setMargins(10, 0, 0, 0);
            titleView.setLayoutParams(titleViewRule);
            titleView.setTextSize(24);
            titleView.setTextColor(Color.rgb(205,198,198));
            titleView.setText(json.getString("adver_title"));
            titleView.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
            layout02.addView(titleView);

            descView = new TextView(context);
            descView.setId(descView.generateViewId());
            descView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            LayoutParams descViewRule = (RelativeLayout.LayoutParams)descView.getLayoutParams();
            descViewRule.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            descViewRule.addRule(RelativeLayout.BELOW,titleView.getId());
            descViewRule.setMargins(10, 0, 0, 0);
            descView.setLayoutParams(descViewRule);
            descView.setTextColor(Color.rgb(205,210,210));
            descView.setTextSize(18);
            descView.setText(json.getString("adver_desc"));
            layout02.addView(descView);

            intel_desc = new TextView(context);
            intel_desc.setId(intel_desc.generateViewId());
            intel_desc.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            LayoutParams intel_descViewRule = (RelativeLayout.LayoutParams)intel_desc.getLayoutParams();
            intel_descViewRule.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            intel_descViewRule.addRule(RelativeLayout.BELOW,descView.getId());
            intel_descViewRule.setMargins(10, 0, 0, 0);
            intel_desc.setLayoutParams(intel_descViewRule);
            intel_desc.setTextColor(Color.rgb(248,83,83));
            intel_desc.setTextSize(18);
            intel_desc.setText(json.getString("intel_desc"));
            layout02.addView(intel_desc);


            intelwal = new TextView(context);
            intelwal.setId(intelwal.generateViewId());
            intelwal.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            LayoutParams intelwalViewRule = (RelativeLayout.LayoutParams)intelwal.getLayoutParams();
            intelwalViewRule.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            intelwalViewRule.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            intelwalViewRule.setMargins(0,0,10,0);
            intelwal.setLayoutParams(intelwalViewRule);
            intelwal.setTextSize(25);
            intelwal.setTextColor(0xffff00ff);
            intelwal.setText(json.getString("integral"));
            layout02.addView(intelwal);


            imageButton = new ImageButton(context);
            imageButton.setId(imageButton.generateViewId());
            imageButton.setLayoutParams(new RelativeLayout.LayoutParams(60,60));
            LayoutParams imageButtonRule = (RelativeLayout.LayoutParams)imageButton.getLayoutParams();
            imageButtonRule.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageButtonRule.addRule(RelativeLayout.BELOW,intelwal.getId());
            imageButtonRule.addRule(RelativeLayout.ALIGN_BOTTOM,imageView.getId());
            imageButtonRule.setMargins(0,0,10,0);
            imageButton.setLayoutParams(imageButtonRule);
            Drawable drawable2 =
                    Drawable.createFromStream(new URL(imgUrl+click_button).openStream(), "image.png");
            imageButton.setImageDrawable(drawable2);
            addOnclickListener(json.getInt("id"), imageButton, json.getString("adv_url"), json.getInt("link_type"));
            layout02.addView(imageButton);

            this.addView(layout02);
        }
    }


    private void addOnclickListener(final int intelPlanId, View view,
                                    final String click_url,final int link_type) {
//intelwal
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(link_type==1) {//web
                    Intent intent = new Intent();
                    intent.setClass(context, WebViewActivity.class);
                    intent.putExtra("url", click_url);
                    intent.putExtra("intelwal", "intelwal");
                    context.startActivity(intent);
                } else if(link_type==2) {//dowload
                    new IntelWalDownload(context,click_url,
                            publisherID, intelPlanId+"",imei);
                }
//                UrlConnectionUtil connectionUtil = new UrlConnectionUtil();
//                Map<String,Object> paramMap = new HashMap<String, Object>();
//                paramMap.put("intelPlanId",intelPlanId+"");
//                paramMap.put("publisherid",publisherID);
//                paramMap.put("imei", imei);
//                paramMap.put("packageName", context.getPackageName());
//                JSONObject json = null;
//                try {
//                    String result = connectionUtil.httpGet(paramMap,ContactAdServerUrl.integral_plans_server_click);
//                    if(result!=null && result.length()>10){
//                        JSONTokener jsonParser = new JSONTokener(result);
//                        json = (JSONObject)jsonParser.nextValue();
//                        if(json.getInt("code")==200) {
//                            Log.i("intelwalplan list", "---click-successful---------------");
//                        }
//                    }
//                } catch (Exception e) {
//                    try{
//                        Log.i("intelwalplan list", "---click-error---------------"+json.getString("message"));
//                    }catch (JSONException err) {
//                        Log.i("intelwalplan list", "---click-JSONException------------"+err.getMessage());
//                    }
//
//                }
            }
        });
    }

}
