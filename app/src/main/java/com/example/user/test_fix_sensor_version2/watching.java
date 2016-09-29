package com.example.user.test_fix_sensor_version2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

/**
 * Created by User on 2016/9/8.
 */
public class watching  extends Activity {

    private Context context;
    private SeekBar seekBar;
    private Button play;
    private Button pause;
    private Button restart;
    private Button stop;
    private VideoView videoView;
    private String actionname;
    private String filepath;
    private boolean isPlaying;
    private final Handler handler = new Handler();//looper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watching);
        context = this;

        //請求run time 時的 user permission
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  ) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

        }
        if ( checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED  ) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    2);
        }
        //請求run time 時的 user permission//
        init();
    }
    //大部分物件的初始化
    public void init(){

        play=(Button)findViewById(R.id.button4);
        pause=(Button)findViewById(R.id.button5);
        restart=(Button)findViewById(R.id.button6);
        stop=(Button)findViewById(R.id.button7);
        videoView=(VideoView)findViewById(R.id.videoView);
        Bundle extras=getIntent().getExtras();

        //get the whole file path
        actionname = extras.getString("action_name");
        String sdPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath();//I don't know why , but this line is must to br ad
        filepath=sdPath+"/Pictures/Rehabilitation";//total file path
        filepath=filepath+"/"+actionname+".mp4";
        //get the whole file path//

        //將所有按鈕設置同一個按鍵監聽>>不錯的玩法
        play.setOnClickListener(click);
        pause.setOnClickListener(click);
        restart.setOnClickListener(click);
        stop.setOnClickListener(click);

        //影片的初始化
        File file = new File(filepath);
        if (!file.exists()) {
            Toast.makeText(this, "此檔案不存在！",Toast.LENGTH_SHORT).show();
            play.setEnabled(false);
            pause.setEnabled(false);
            restart.setEnabled(false);
            stop.setEnabled(false);

            return;
        }
        else{
            videoView.setVideoPath(file.getAbsolutePath());
            seekBar=(SeekBar)findViewById(R.id.seekBar);
            seekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    seekChange(view);
                    return false;
                }
            });

        }
        //影片的初始化//


    }
    //大部分物件的初始化//
    //按鈕事件監聽
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {//button 在 view 階層下面

            switch (v.getId()) {
                case R.id.button4:
                    play(0);
                    break;
                case R.id.button5:
                    pause();
                    break;
                case R.id.button6:
                    restart();
                    break;
                case R.id.button7:
                    stop();
                    break;
                default:
                    break;
            }
        }
    };
    //按鈕事件監聽//
    protected void play(int msec){
        //videoView.seekTo(msec);

        videoView.start();//很奇怪的機制，當videoView.start()之後，才可以讀取影片的長度
        seekBar.setMax(videoView.getDuration());
        startPlayProgressUpdater();//讓進度條跟著影片進度走
        //按下開始後，將開始按鈕設為不可按
        play.setEnabled(false);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play.setEnabled(true);
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                play(0);
                isPlaying = false;
                return false;
            }
        });
        //按下開始後，將開始按鈕設為不可按//
    }
    //以播放進度改變seekbar目前的位置
    public void startPlayProgressUpdater() {
        seekBar.setProgress(videoView.getCurrentPosition());
        if (videoView.isPlaying()){
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();//每一秒更新一次進度條
                }
            };
            handler.postDelayed(notification,500);
        }
    }
    // This is event handler thumb moving event
    private void seekChange(View v){
        if(videoView.isPlaying()){
            SeekBar sb = (SeekBar)v;
            videoView.seekTo(sb.getProgress());
        }
    }
    protected void pause(){
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
            play.setEnabled(true);//將play案件設為可以按
            Toast.makeText(this, "暫停播放", Toast.LENGTH_SHORT).show();
        }
    }
    protected void restart(){
        if (videoView != null && videoView.isPlaying()) {
            videoView.seekTo(0);
            Toast.makeText(context, "重新播放", Toast.LENGTH_SHORT).show();
            return;
        }
        isPlaying = false;
        play(0);
    }
    protected void stop(){
        if (videoView != null ) {
            //videoView.stopPlayback();
            isPlaying = false;
            play.setEnabled(true);//將play案件設為可以按
            videoView.seekTo(0);
            videoView.pause();
            seekBar.setProgress(videoView.getCurrentPosition());
            Toast.makeText(context, "停止播放", Toast.LENGTH_SHORT).show();
        }
    }
    //要求權限動作的回調
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {//在android6之後才需要'
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else {
                Toast.makeText(context, "需要允許開啟寫入外部記憶體，才能觀看影片！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else {
                Toast.makeText(context, "超級問號!???", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    //要求權限動作的回調//
}
