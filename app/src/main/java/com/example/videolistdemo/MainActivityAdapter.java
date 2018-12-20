package com.example.videolistdemo;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.danikula.videocache.CacheListener;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import im.ene.toro.CacheManager;
import im.ene.toro.PlayerSelector;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.exoplayer.Playable;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.media.VolumeInfo;
import im.ene.toro.widget.Container;

import static java.lang.String.format;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MyViewHolder> implements CacheManager, PlayerSelector {

    private Context context;
    private boolean isMuteByDefault;
    private ArrayList<VideoDictionary> dataArrayList;
    private boolean isErrorView = false;

    private final long initTimeStamp;
    @SuppressWarnings("WeakerAccess")
    PlayerSelector origin;
    // Keep a cache of the Playback order that is manually paused by User.
    // So that if User scroll to it again, it will not start play.
    // Value will be updated by the ViewHolder.
    final AtomicInteger lastUserPause = new AtomicInteger(-1);

    Uri mediaUri;

    public MainActivityAdapter(Context context, ArrayList<VideoDictionary> dataArrayList, boolean isMuteByDefault, long initTimeStamp) {
        this.context = context;
        this.initTimeStamp = initTimeStamp;
        this.isMuteByDefault = isMuteByDefault;
        this.dataArrayList = dataArrayList;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_player, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolders, int position) {
        if (dataArrayList.get(position).getType().equalsIgnoreCase("video")) {

            viewHolders.rlVideo.setVisibility(View.VISIBLE);
            viewHolders.image.setVisibility(View.GONE);

            String url = dataArrayList.get(position).getUrl();
            this.mediaUri = Uri.parse(url);

            if (dataArrayList.get(position).isMute()) {
                viewHolders.imageView_sound.setImageResource(R.drawable.mute);
                viewHolders.getCurrentPlaybackInfo().setVolumeInfo(new VolumeInfo(true, 0.75f));
            } else {
                viewHolders.imageView_sound.setImageResource(R.drawable.unmute);
                viewHolders.getCurrentPlaybackInfo().setVolumeInfo(new VolumeInfo(false, 1.0f));
            }

           // viewHolders.imageView_sound.setOnClickListener(new ClickHandler(dataArrayList.get(position), viewHolders, position));

            viewHolders.imageView_sound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolders.isPlaying()) {
                        viewHolders.pause();
                    } else {
                        viewHolders.play();
                    }
                }
            });


        } else {
            viewHolders.rlVideo.setVisibility(View.GONE);
            viewHolders.image.setVisibility(View.VISIBLE);
            Glide.with(context).load(dataArrayList.get(position).getUrl()).into(viewHolders.image);
        }
    }

    private class ClickHandler implements View.OnClickListener {

        int position;
        MyViewHolder viewHolders;
        VideoDictionary videoDictionary;

        public ClickHandler(VideoDictionary videoDictionary, MyViewHolder viewHolders, int position) {
            this.position = position;
            this.videoDictionary = videoDictionary;
            this.viewHolders = viewHolders;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageView_sound:
                    ImageView imageView = (ImageView) v;
                    checkAndSetMute(videoDictionary, viewHolders, imageView);
                    break;
            }
        }

    }

    private void checkAndSetMute(VideoDictionary videoDictionary, MyViewHolder viewHolders, ImageView imageView) {
        if (videoDictionary.isMute()) {
            imageView.setImageResource(R.drawable.unmute);
            videoDictionary.setMute(false);
            viewHolders.helper.setVolumeInfo(new VolumeInfo(false, 1.0f));
        } else {
            imageView.setImageResource(R.drawable.mute);
            videoDictionary.setMute(true);
            viewHolders.helper.setVolumeInfo(new VolumeInfo(true, 0.75f));
        }
    }

    @Override
    public int getItemCount() {
        int size = (dataArrayList != null ? dataArrayList.size() : 0);
        if (isErrorView) {
            size = 1;
        }
        return size;
    }

    private VideoDictionary getItem(final int position) {
        return dataArrayList.get(position);
    }

    @Nullable
    @Override
    public Object getKeyForOrder(int order) {
        return getItem(order);
    }

    @Nullable
    @Override
    public Integer getOrderForKey(@NonNull Object key) {
        return key instanceof VideoDictionary ? dataArrayList.indexOf(key) : null;
    }

    @NonNull
    @Override
    public Collection<ToroPlayer> select(@NonNull Container container, @NonNull List<ToroPlayer> items) {
        Collection<ToroPlayer> originalResult = origin.select(container, items);
        ArrayList<ToroPlayer> result = new ArrayList<>(originalResult);
        if (lastUserPause.get() >= 0) {
            for (Iterator<ToroPlayer> it = result.iterator(); it.hasNext(); ) {
                if (it.next().getPlayerOrder() == lastUserPause.get()) {
                    it.remove();
                    break;
                }
            }
        }

        return result;
    }

    @NonNull
    @Override
    public PlayerSelector reverse() {
        return origin.reverse();
    }

    private final Playable.EventListener listener = new Playable.DefaultEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            super.onPlayerStateChanged(playWhenReady, playbackState);
            Log.d("onPlayerStateChanged", ":" + format(Locale.getDefault(), "STATE: %d・PWR: %s", playbackState, playWhenReady));
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder implements ToroPlayer, CacheListener {

        private final ImageView image;
        private final ImageView imageThumb;
        private final RelativeLayout rlVideo;
        public ImageView imageView_sound;
        public PlayerView video_view;
        public ExoPlayerViewHelper helper;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            video_view = itemView.findViewById(R.id.video_view);
            imageView_sound = itemView.findViewById(R.id.imageView_sound);
            rlVideo = itemView.findViewById(R.id.rlVideo);
            image = itemView.findViewById(R.id.image);
            imageThumb = itemView.findViewById(R.id.imageThumb);
        }

        @Override
        public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
            //Log.d("onCacheAvailable",":"+url +" percentsAvailable:"+percentsAvailable);
        }

        @NonNull
        @Override
        public View getPlayerView() {
            return video_view;
        }

        @NonNull
        @Override
        public PlaybackInfo getCurrentPlaybackInfo() {
            return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
        }

        @Override
        public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
            if (helper == null) {
                helper = new ExoPlayerViewHelper(this, mediaUri);
                helper.addEventListener(listener);
            }
            helper.initialize(container, playbackInfo);
            imageThumb.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.drawable.exo_icon_play).into(imageThumb);
        }

        @Override
        public void play() {
            if (helper != null) helper.play();
            imageThumb.setVisibility(View.GONE);
        }

        @Override
        public void pause() {
            if (helper != null) helper.pause();
        }

        @Override
        public boolean isPlaying() {
            return helper != null && helper.isPlaying();
        }

        @Override
        public void release() {
            if (helper != null) {
                helper.removeEventListener(listener);
                helper.release();
                helper = null;
            }
        }

        @Override
        public boolean wantsToPlay() {
            return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
        }

        @Override
        public int getPlayerOrder() {
            return getAdapterPosition();
        }
    }
}
