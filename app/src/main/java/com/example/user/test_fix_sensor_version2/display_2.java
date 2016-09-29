package com.example.user.test_fix_sensor_version2;

import java.io.*;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.test_fix_sensor_version2.DB.DBhelper;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by User on 2016/8/25.
 */
public class display_2 extends Activity {
    //request for permission
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private Context context;
    private TextView action_name;
    private Button start_test;
    private Button fix_action;
   // private Button detail_data;
    private Button delete;
    private Button back;
    private Button import_video;
    private Button watching;
    private Button make_video;


    private String filepath;

    Bundle extras;

    //database
    private DBhelper dbhelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_to_test);
        context = this;
        verifyStoragePermissions(this);

        init();

    }
    public void init(){
        //initialize database
        dbhelper=new DBhelper(context);
        db=dbhelper.getWritableDatabase();

        action_name=(TextView)findViewById(R.id.textView3);
        start_test=(Button)findViewById(R.id.button6);
        fix_action=(Button)findViewById(R.id.button9);
        //detail_data=(Button)findViewById(R.id.button13);
        delete=(Button)findViewById(R.id.button10);
        back=(Button)findViewById(R.id.button11);
        import_video=(Button)findViewById(R.id.button18);
        watching=(Button)findViewById(R.id.button14);
        make_video=(Button)findViewById(R.id.button19);

        extras = getIntent().getExtras();
        action_name.setText(extras.getString("action_name"));

        //測試動作
        start_test.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context,display_3.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        //刪除動作
        delete.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                int id=extras.getInt("_id");

                db.delete("actions","_id="+id,null);//第三個參數要放字串陣列!

                finish();
            }
        });
        //返回
        back.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        //返回//
        //import video
        import_video.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                final String mimeType = "video/*";
                final PackageManager packageManager = context.getPackageManager();
                final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(mimeType);
                List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.size() > 0) {



                    // 如果有可用的Activity
                    Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                    picker.setType(mimeType);
                    picker.addCategory(Intent.CATEGORY_OPENABLE);//看這部分的文件!!
                    if ( picker.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent,1);
                    }
                    // 使用Intent Chooser
                    //Intent destIntent = Intent.createChooser(picker, "選取影片");
                    //startActivityForResult(destIntent,1);//1為requestcode

                } else {
                    // 沒有可用的Activity
                    Toast.makeText(context, "抱歉，自製檔案選取器製作中，建議下載檔案管理類的程式", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //import video//
        //make video
        make_video.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String sdPath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath();//I don't know why , but this line is must to br ad
                filepath=sdPath+"/Pictures/Rehabilitation";//total file path
                filepath=filepath+ "/"+ extras.getString("action_name") +".mp4";
                //開啟影片意圖
                //請求run time 時的 user permission
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            1);
                }
                else{//假若已經給予權限
                    File file;
                    file = new File(filepath);
                    String action;
                    action = MediaStore.ACTION_VIDEO_CAPTURE;
                    Intent it = new Intent(action);
                    // 輸出參數：相機拍照後存入指定路徑
                    it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//輸出    putExtra("變數名稱","值")
                    // 回調型 intent
                    startActivityForResult(it, 100);
                }
                //開啟影片意圖//
            }
        });
        //make video//
        //watching video
        watching.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=null;
                intent=new Intent(context,watching.class);
                intent.putExtras(extras);

                startActivity(intent);
                //喚醒播放影片的意圖//
            }
        });
        //watching video//

        //fix action
        fix_action.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=null;
                intent=new Intent(context,fix_action.class);
                intent.putExtras(extras);

                startActivity(intent);
            }
        });
        //fix action//
    }

    //intent callback
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {

        String sdPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath();//I don't know why , but this line is must to br ad
        filepath=sdPath+"/Pictures/Rehabilitation";//total file path
        try {

            //create a new direcotry
            File theDir = new File(filepath);
            // if the directory does not exist, create it
            if (!theDir.exists()) {
                boolean result = false;
                try{
                    theDir.mkdir();
                    result = true;
                }
                catch(SecurityException se){
                    //handle it
                }
                if(result) {
                    System.out.println("DIR created");
                }
            }
            //create a new directory//
            //copy file
            FileInputStream in = (FileInputStream) getContentResolver().openInputStream(data.getData());
            File outFile = new File(filepath+ "/"+ extras.getString("action_name") +".mp4");//以動作名稱為檔名
            FileOutputStream out = new FileOutputStream(outFile);
            FileChannel inChannel = in.getChannel();
            FileChannel outChannel = out.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            in.close();
            out.close();
            //copy file //
        }
        catch ( Exception ex ){
            Log.e("error",ex.toString());
        }
    }

    //
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    //請求run time 時的 user permission，假若使用者點選同意，則開啟錄影
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {//在android6之後才需要'
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                File file;
                file = new File(filepath);
                String action;
                action = MediaStore.ACTION_VIDEO_CAPTURE;
                Intent it = new Intent(action);
                // 輸出參數：相機拍照後存入指定路徑
                it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//輸出    putExtra("變數名稱","值")
                // 回調型 intent
                startActivityForResult(it, 100);
            }
            else {
                Toast.makeText(context, "需要允許開啟相機，才能拍攝影片！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


}
