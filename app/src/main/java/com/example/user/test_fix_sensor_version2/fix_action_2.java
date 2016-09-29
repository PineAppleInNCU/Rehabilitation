package com.example.user.test_fix_sensor_version2;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.test_fix_sensor_version2.DB.DBhelper;

/**
 * Created by User on 2016/9/9.
 */
public class fix_action_2 extends Activity implements SensorEventListener {

    private Context context;
    private SensorEventListener mainListener;
    private int nowStage=1;
    //最後要儲存到資料庫的資料
    private String actionName;
    private int NumActionPoint;
    private String actionData="";
    private String diffrence_x="";
    private String diffrence_y="";
    private String diffrence_z="";
    //最後要儲存到資料庫的資料//
    //顯示在畫面最上方的三軸值
    private TextView action_name;
    private TextView x_sensor;
    private TextView y_sensor;
    private TextView z_sensor;
    //顯示在畫面最上方的三軸值//
    //分別為   now_stage/total_stage，現在的三軸值
    private TextView stages;
    private TextView now_x_sensor;
    private TextView now_y_sensor;
    private TextView now_z_sensor;
    //分別為   now_stage/total_stage，現在的三軸值//
    //按鈕們
    private Button start_recording;
    private Button start_test;
    private Button end_test;
    private Button confirm_action;
    //按鈕們//
    //editText
    private EditText x_upbound;//上界值與下界值
    private EditText x_lowbound;
    private EditText y_upbound;
    private EditText y_lowbound;
    private EditText z_upbound;
    private EditText z_lowbound;
    //editText//
    //sensor listener and vibrator
    private SensorManager aSensorManager;
    private Sensor aSensor;
    private float gravity[]=new float[3];

    private Vibrator myVibrator;
    //
    //boolean
    private boolean starting_recording=false;
    private boolean starting_test=false;
    //
    //數值資料
    private float float_x_sensor;
    private float float_y_sensor;
    private float float_z_sensor;
    private float float_x_upbound;
    private float float_x_lowbound;
    private float float_y_upbound;
    private float float_y_lowbound;
    private float float_z_upbound;
    private float float_z_lowbound;
    //
    //資料庫操作
    private DBhelper dbhelper;
    private SQLiteDatabase db;
    //資料庫操作//

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_actions);

        context=this;
        mainListener=this;

        init();
    }
    //initialize
    public void init(){
        //database
        dbhelper = new DBhelper(context);
        db = dbhelper.getWritableDatabase();
        //database//
        //get bundle from last class
        Bundle extras=getIntent().getExtras();
        actionName=extras.getString("actionName");
        NumActionPoint=Integer.parseInt(extras.getString("numActionPoint"));
        //
        //initialize the textview and button
        action_name=(TextView)findViewById(R.id.textView14);
        x_sensor=(TextView)findViewById(R.id.textView8);
        y_sensor=(TextView)findViewById(R.id.textView9);
        z_sensor=(TextView)findViewById(R.id.textView10);
        now_x_sensor=(TextView)findViewById(R.id.textView15);
        now_y_sensor=(TextView)findViewById(R.id.textView16);
        now_z_sensor=(TextView)findViewById(R.id.textView17);
        stages=(TextView)findViewById(R.id.textView2);
        start_recording = (Button)findViewById(R.id.button8);
        start_test= (Button)findViewById(R.id.button4);
        confirm_action= (Button)findViewById(R.id.button7);
        x_upbound=(EditText)findViewById(R.id.editText2);//上界值與下界值
        x_lowbound=(EditText)findViewById(R.id.editText3);;
        y_upbound=(EditText)findViewById(R.id.editText4);;
        y_lowbound=(EditText)findViewById(R.id.editText5);;
        z_upbound=(EditText)findViewById(R.id.editText6);;
        z_lowbound=(EditText)findViewById(R.id.editText7);;
        //
        //取得listener and vibrator
        aSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        aSensor = aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //取得listener//
        action_name.setText(actionName);
        x_sensor.setText("Empty");
        y_sensor.setText("Empty");
        z_sensor.setText("Empty");
        now_x_sensor.setText("Empty");
        now_y_sensor.setText("Empty");
        now_z_sensor.setText("Empty");

        stages.setText("動作紀錄:"+nowStage+"/"+NumActionPoint);

        //on click listener
        //固定住瞬間的三軸值
        start_recording.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){

                //讓gsensor開始跳動，並且更改按鈕的字樣
                if(!starting_recording) {
                    start_recording.setText("結束紀錄");

                    aSensorManager.registerListener((SensorEventListener) context, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    starting_recording=true;
                }
                //讓gsensor停止跳動，並且把那瞬間的sensor值記錄下來
                else {
                    start_recording.setText("開始紀錄");

                    aSensorManager.unregisterListener(mainListener);
                    starting_recording=false;

                    x_sensor.setText("X："+gravity[0]);
                    y_sensor.setText("Y："+gravity[1]);
                    z_sensor.setText("Z："+gravity[2]);

                    float_x_sensor=gravity[0];
                    float_y_sensor=gravity[1];
                    float_z_sensor=gravity[2];
                }
            }
        });
        //
        //開始測試該動作是否符合需求
        start_test.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){

                if(!starting_test){

                    if(x_upbound.getText().toString().length()==0 ||
                            x_lowbound.getText().toString().length()==0 ||
                            y_upbound.getText().toString().length()==0 ||
                            y_lowbound.getText().toString().length()==0 ||
                            z_upbound.getText().toString().length()==0 ||
                            z_lowbound.getText().toString().length()==0
                            ){
                        //驗證有沒有輸入值
                        Toast.makeText(context, "請輸入誤差值", Toast.LENGTH_SHORT).show();
                        //驗證有沒有輸入值//
                    }
                    else{
                        start_test.setText("結束測試");
                        starting_test=true;
                        //將輸入的值放入變數
                        float_x_upbound=Float.parseFloat(x_upbound.getText().toString());
                        float_x_lowbound=Float.parseFloat(x_lowbound.getText().toString());
                        float_y_upbound=Float.parseFloat(y_upbound.getText().toString());
                        float_y_lowbound=Float.parseFloat(y_lowbound.getText().toString());
                        float_z_upbound=Float.parseFloat(z_upbound.getText().toString());
                        float_z_lowbound=Float.parseFloat(z_lowbound.getText().toString());
                        //將輸入的值放入變數//
                        //註冊Gsensor監聽者
                        aSensorManager.registerListener((SensorEventListener) context, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
                        //註冊Gsensor監聽者//
                    }

                }
                else{
                    start_test.setText("開始測試");
                    starting_test=false;
                    aSensorManager.unregisterListener(mainListener);
                }

            }
        });
        //
        //動作確認
        confirm_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                //動作還未記錄完畢
                if(nowStage!=NumActionPoint){
                    actionData+=float_x_sensor+"@"+float_y_sensor+"@"+float_z_sensor+"&";
                    diffrence_x+=x_upbound.getText().toString()+"@"+x_lowbound.getText().toString()+"&";
                    diffrence_y+=y_upbound.getText().toString()+"@"+y_lowbound.getText().toString()+"&";
                    diffrence_z+=z_upbound.getText().toString()+"@"+z_lowbound.getText().toString()+"&";



                    x_sensor.setText("Empty");
                    y_sensor.setText("Empty");
                    z_sensor.setText("Empty");
                    now_x_sensor.setText("Empty");
                    now_y_sensor.setText("Empty");
                    now_z_sensor.setText("Empty");


                    nowStage++;
                    stages.setText("動作紀錄:"+nowStage+"/"+NumActionPoint);
                }
                //動作記錄完畢
                else{
                    actionData+=float_x_sensor+"@"+float_y_sensor+"@"+float_z_sensor;
                    diffrence_x+=x_upbound.getText().toString()+"@"+x_lowbound.getText().toString();
                    diffrence_y+=y_upbound.getText().toString()+"@"+y_lowbound.getText().toString();
                    diffrence_z+=z_upbound.getText().toString()+"@"+z_lowbound.getText().toString();

                    //進行動作紀錄
                    ContentValues cv=new ContentValues();
                    cv.put("actiondata",actionData);
                    cv.put("number_point",NumActionPoint);
                    cv.put("difference_x",diffrence_x);
                    cv.put("difference_y",diffrence_y);
                    cv.put("difference_z",diffrence_z);
                    // 執行SQL語句
                    long id = db.update("actions",cv,"actionname=?",new String[]{actionName});
                    Toast.makeText(context, "_id：" + id, Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });
        //動作確認//
        //on click listener//

    }
    //initialize//
    //sensor
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {//這是甚麼?
        // TODO Auto-generated method stub
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        gravity[0] = event.values[0];
        gravity[1] = event.values[1];
        gravity[2] = event.values[2];

        //display the sensor on the textview
        now_x_sensor.setText("X："+gravity[0]);
        now_y_sensor.setText("Y："+gravity[1]);
        now_z_sensor.setText("Z："+gravity[2]);
        //display the sensor on the textview//
        //假若正在測試動作，則要啟動震動偵測
        if(starting_test){
            if((gravity[0]>(float_x_sensor-float_x_lowbound))  &&
                    (gravity[0]<(float_x_sensor+float_x_upbound))  &&
                    (gravity[1]>(float_y_sensor-float_y_lowbound))  &&
                    (gravity[1]<(float_y_sensor+float_y_upbound))  &&
                    (gravity[2]>(float_z_sensor-float_z_lowbound))  &&
                    (gravity[2]<(float_z_sensor+float_z_upbound))
                    ){//在範圍內就震動

                myVibrator= (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
                myVibrator.vibrate(1000);
                //寫錯，float_x_sensor要用現在的三軸值代替

                //動作正確就更改字的顏色
                now_x_sensor.setTextColor(getColor(R.color.colorAccent));
                now_y_sensor.setTextColor(getColor(R.color.colorAccent));
                now_z_sensor.setTextColor(getColor(R.color.colorAccent));
                //動作正確就更改字的顏色
            }
            else{
                now_x_sensor.setTextColor(getColor(R.color.black));
                now_y_sensor.setTextColor(getColor(R.color.black));
                now_z_sensor.setTextColor(getColor(R.color.black));
            }
        }
        //假若正在測試動作，則要啟動震動偵測//

    }
    @Override
    protected void onResume() {
        super.onResume();
        // aSensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        aSensorManager.unregisterListener(this);
    }
    //sensor//
}
