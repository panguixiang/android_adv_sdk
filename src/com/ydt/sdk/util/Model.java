package com.ydt.sdk.util;

/**
 * Created by panguixiang on 1/19/15.
 */
public class Model {

    private int adv_type;
    private Object media;
    private String mediaLink;
    private int originId;
    private int linkType;

    public void setAdv_type(int adv_type) {
        this.adv_type=adv_type;
    }

    public int getAdv_type(){
        return this.adv_type;
    }

    public void setMedia(Object media) {
        this.media=media;
    }

    public Object getMedia() {
        return this.media;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink=mediaLink;
    }
    public String getMediaLink() {
        return this.mediaLink;
    }

    public void setOriginId(int originId) {
        this.originId=originId;
    }
    public int getOriginId() {
        return this.originId;
    }
    public void setLinkType(int linkType) {
        this.linkType=linkType;
    }
    public int getLinkType() {
        return this.linkType;
    }


}
