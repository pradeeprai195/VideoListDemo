package com.example.videolistdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.example.videolistdemo.pref.IOPref;

import java.io.File;
import java.util.ArrayList;

import im.ene.toro.PlayerSelector;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.media.VolumeInfo;
import im.ene.toro.widget.Container;

import static im.ene.toro.media.PlaybackInfo.INDEX_UNSET;
import static im.ene.toro.media.PlaybackInfo.TIME_UNSET;

public class MainActivity extends AppCompatActivity implements CacheListener {

    private Context context;
    private MainActivityAdapter videoRecyclerAdapter;
    private ArrayList<VideoDictionary> videoArrayList;
    Container my_fancy_videos;
    private PlaybackInfo baseInfo;

    // Orientation helper stuff
    PlayerSelector selector = PlayerSelector.DEFAULT;  // backup current selector.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    protected void initView() {
        context = this;
        videoArrayList = new ArrayList<>();
        my_fancy_videos = findViewById(R.id.my_fancy_videos);

        initData();
    }

    protected void initData() {
        videoArrayList.add(new VideoDictionary("Video1","http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4","video"));
        videoArrayList.add(new VideoDictionary("Image1","http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/2_qwpgis.jpg","image"));
        videoArrayList.add(new VideoDictionary("Video2","http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4","video"));
        videoArrayList.add(new VideoDictionary("Image2","http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/1_ybonak.jpg","image"));
        videoArrayList.add(new VideoDictionary("Video3","http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70/v1481795676/4_nvnzry.mp4","video"));

        boolean isMuteByDefault = IOPref.getInstance().getBoolean(context, IOPref.PreferenceKey.isMute, true);

        for (int i = 0; i < videoArrayList.size(); i++) {

            HttpProxyCacheServer proxy = App.getProxy(context);
            proxy.registerCacheListener(this, videoArrayList.get(i).getUrl());
            String proxyUrl = proxy.getProxyUrl(videoArrayList.get(i).getUrl());
            videoArrayList.get(i).setProxyUrl(proxyUrl);

            videoArrayList.get(i).setMute(isMuteByDefault);
            videoArrayList.get(i).setTimeStamp(System.currentTimeMillis() + i * 60_000);
        }

        videoRecyclerAdapter = new MainActivityAdapter(context, videoArrayList, isMuteByDefault, System.currentTimeMillis());

        my_fancy_videos.setAdapter(videoRecyclerAdapter);
        my_fancy_videos.setLayoutManager(new LinearLayoutManager(context));
        my_fancy_videos.setCacheManager(videoRecyclerAdapter);
        my_fancy_videos.setPlayerSelector(selector);

        if (isMuteByDefault) {
            my_fancy_videos.setPlayerInitializer(new Container.Initializer() {
                @NonNull
                @Override
                public PlaybackInfo initPlaybackInfo(int order) {
                    VolumeInfo volumeInfo = new VolumeInfo(true, 0.75f);
                    return new PlaybackInfo(INDEX_UNSET, TIME_UNSET, volumeInfo);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        videoRecyclerAdapter = null;
        super.onDestroy();
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {

    }
}
