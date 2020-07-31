package ssu.groupname.baseapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.darkf.new370.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {
    private PlayerView playerView;
    private Context mContext;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    ExoPlayer player;
    ProgressBar loading;

    public void forwardpage (View view) {
        Intent home_activity = new Intent (this, home.class);
        startActivity(home_activity);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();
        playerView = (PlayerView)findViewById(R.id.video_view);
        loading = (ProgressBar)findViewById(R.id.loading);

    }
    @Override
    public void onStart() {
        super.onStart();
        //--------------------------------------
        //Creating default track selector
        //and init the player

        TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mContext),
                new DefaultTrackSelector(adaptiveTrackSelection),
                new DefaultLoadControl());

        //init the player
        playerView.setPlayer(player);

        //-------------------------------------------------
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "Exo2"), defaultBandwidthMeter);

        //-----------------------------------------------
        //Create media source
        String hls_url = "https://video-weaver.sjc02.hls.ttvnw.net/v1/playlist/CrMDbWW-smGJ1LXAdaCSWX_MsTOHpauxS-Lf3p2FXN5Bcu4yC__iqb_st143RQsAIrTHpzOEUdcA6dyJ4Jyrt1b_VDvI27xOlMwUqwE1iXuzBTKMQ-VWENhMIyS9RSxGV0M0C_VPSLtYOX75CKkC_i1gbZW1U_vNLg1KwWqbn0gym9bErchPcy32z7NSOoyKyfgSGDwSlp_2HYSsgRT5aN6slKc_kr-JXtvRJV8jx5zfG6P7YW2YhbSfXMaiSQBRs8fyqEnlGd_g85A2IKXn7DksfE5RwsW7289GZrlRTOYm3y7nH8H5fLio8su_L5llmJ3cOXMR8fdb_Me--iIqylTK5Hgi_Q8ZwTNxTqvT-Qu7ScGBaD6wuhYEhwal9A7qjM44OP_eoYlQiCggMoW9YZg1dmhOs5h8DP-NTiJopArEx8sA3pavv5Hfvgu66vYeb8dcUTMvAykhs4Uyrvn42XbVynMWkTUIuM2AUgV9kQ4rrJVG3gyes5HauIIA3svm6ybGoukrb1z0zKfGPhKIxXL1IuWL3yr2SsndRzvtdC7XA0HZEVZHRG74Lrqb2vwXh-7cl4-qEhA7VQLvbmm_zUdIa--Ek5WmGgw1ima-4OxyK0rpDS4.m3u8";
        Uri uri = Uri.parse(hls_url);
        Handler mainHandler = new Handler();
        MediaSource mediaSource = new HlsMediaSource(uri,
                dataSourceFactory, mainHandler, null);
        player.prepare(mediaSource);


        player.setPlayWhenReady(playWhenReady);
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case ExoPlayer.STATE_READY:
                        loading.setVisibility(View.GONE);
                        break;
                    case ExoPlayer.STATE_BUFFERING:
                        loading.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, true, false);

    }
    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }
    @Override
    public void onStop () {
        super.onStop();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
}
