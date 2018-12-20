package com.example.videolistdemo;

import android.net.Uri;

public class VideoDictionary {

    private String title;
    private String url;
    private String proxyUrl;
    private long index;
    private Uri mediaUri;
    private long timeStamp;
    private boolean isMute;
    private String type;

    public VideoDictionary(String title, String url, String type) {
        this.title = title;
        this.url = url;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public Uri getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(Uri mediaUri) {
        this.mediaUri = mediaUri;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
