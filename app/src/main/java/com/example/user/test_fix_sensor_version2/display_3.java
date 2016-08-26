package com.example.user.test_fix_sensor_version2;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by User on 2016/8/25.
 */
public class display_3 extends Activity implements SensorEventListener {
    private Context context;
    private Bundle extras;
    private int nowStage=0;

    private TextView action_name;
    private TextView action_number;
    private TextView goal_x_sensor;
    private TextView goal_y_sensor;
    private TextView goal_z_sensor;
    private TextView now_x_sensor;
    private TextView now_y_sensor;
    private TextView now_z_sensor;
    private TextView nowStage_totalStage;

    private Button start_test;
    private Button leave;

    //總資料
    private String actionName;
    private String actionData;
    private int NumActionPoint;
    private String differenceX;
    private String differenceY;
    private String differenceZ;

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

    private SensorManager aSensorManager;
    private Sensor aSensor;
    private Vibrator myVibrator;
    private float gravity[]=new float[3];

    private boolean starting_test=false;

    String[] tempvalue;
    String[][] singlevalue;
    float[][] value;
    String[] tempvalue_difference_x;
    String[][] singlevalue_difference_x;
    String[] tempvalue_difference_y;
    String[][] singlevalue_difference_y;
    String[] tempvalue_difference_z;
    String[][] singlevalue_difference_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing);

        context=this;

        init();
    }
    public void init(){
        //取得listener
        aSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        aSensor = aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //取得listener//

        action_name=(TextView)findViewById(R.id.textView4);
        action_number=(TextView)findViewById(R.id.textView5);
        goal_x_sensor=(TextView)findViewById(R.id.textView7);
        goal_y_sensor=(TextView)findViewById(R.id.textView18);
        goal_z_sensor=(TextView)findViewById(R.id.textView19);
        now_x_sensor=(TextView)findViewById(R.id.textView21);
        now_y_sensor=(TextView)findViewById(R.id.textView22);
        now_z_sensor=(TextView)findViewById(R.id.textView23);
        nowStage_totalStage=(TextView)findViewById(R.id.textView24);

        start_test=(Button)findViewById(R.id.button5);
        leave=(Button)findViewById(R.id.button12);



        extras=getIntent().getExtras();

        actionName=extras.getString("action_name");
        actionData=extras.getString("action_data");
        NumActionPoint=extras.getInt("number_point");
        differenceX=extras.getString("difference_x");
        differenceY=extras.getString("difference_y");
        differenceZ=extras.getString("difference_z");


        //拆解資料
        tempvalue = actionData.split("&");
        singlevalue = new String[NumActionPoint][3];//第一個inex是動作的點數目
        value = new float[NumActionPoint][3];
        for (int i = 0; i < NumActionPoint; i++) {
            singlevalue[i] = tempvalue[i].split("@");
            value[i][0] = Float.parseFloat(singlevalue[i][0]);//x
            value[i][1] = Float.parseFloat(singlevalue[i][1]);//y
            value[i][2] = Float.parseFloat(singlevalue[i][2]);//z
        }

        tempvalue_difference_x =differenceX.split("&");
        singlevalue_difference_x = new String[NumActionPoint][2];
        for(int i=0;i<NumActionPoint;i++){
            singlevalue_difference_x[i]=tempvalue_difference_x[i].split("@");
        }

        tempvalue_difference_y =differenceX.split("&");
        singlevalue_difference_y = new String[NumActionPoint][2];
        for(int i=0;i<NumActionPoint;i++){
            singlevalue_difference_y[i]=tempvalue_difference_y[i].split("@");
        }

        tempvalue_difference_z =differenceX.split("&");
        singlevalue_difference_z = new String[NumActionPoint][2];
        for(int i=0;i<NumActionPoint;i++){
            singlevalue_difference_z[i]=tempvalue_difference_z[i].split("@");
        }
        //拆解資料//
        //初始化顯示
        action_name.setText(actionName);
        action_number.setText("動作測試："+nowStage+"/"+NumActionPoint);
        goal_x_sensor.setText(singlevalue[nowStage][0]);
        goal_y_sensor.setText(singlevalue[nowStage][1]);
        goal_z_sensor.setText(singlevalue[nowStage][2]);
        nowStage_totalStage.setText("測試中！");
        //初始化顯示//


        //開始測試該動作是否符合需求
        start_test.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                if(!starting_test){

                    starting_test=true;

                    float_x_upbound=Float.parseFloat(singlevalue_difference_x[nowStage][0]);
                    float_x_lowbound=Float.parseFloat(singlevalue_difference_x[nowStage][1]);
                    float_y_upbound=Float.parseFloat(singlevalue_difference_y[nowStage][0]);
                    float_y_lowbound=Float.parseFloat(singlevalue_difference_y[nowStage][1]);
                    float_z_upbound=Float.parseFloat(singlevalue_difference_z[nowStage][0]);
                    float_z_lowbound=Float.parseFloat(singlevalue_difference_z[nowStage][1]);

                    //註冊Gsensor監聽者
                    aSensorManager.registerListener((SensorEventListener) context, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    //註冊Gsensor監聽者//
                }
            }
        });
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {//這是甚麼?
        // TODO Auto-generated method stub
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        gravity[0] = event.values[0];
        gravity[1] = event.values[1];
        gravity[2] = event.values[2];

        //display the sensor on the textview
        now_x_sensor.setText("X："+gravity[0]);
        now_y_sensor.setText("Y："+gravity[1]);
        now_z_sensor.setText("Z："+gravity[2]);
        //display the sensor on the textview//
        if(starting_test && nowStage<NumActionPoint){
            if((gravity[0]>(value[nowStage][0]-float_x_lowbound))  &&
                    (gravity[0]<(value[nowStage][0]+float_x_upbound))  &&
                    (gravity[1]>(value[nowStage][1]-float_y_lowbound))  &&
                    (gravity[1]<(value[nowStage][1]+float_y_upbound))  &&
                    (gravity[2]>(value[nowStage][2]-float_z_lowbound))  &&
                    (gravity[2]<(value[nowStage][2]+float_z_upbound))
                    ){//在範圍內就震動

                myVibrator= (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
                myVibrator.vibrate(1000);
                //寫錯，float_x_sensor要用現在的三軸值代替

                //動作正確就更改字的顏色
                now_x_sensor.setTextColor(getColor(R.color.colorAccent));
                now_y_sensor.setTextColor(getColor(R.color.colorAccent));
                now_z_sensor.setTextColor(getColor(R.color.colorAccent));
                //動作正確就更改字的顏色


                //動作正確後，換到下一個動作 ，可以整合到function裡
                nowStage++;
                action_number.setText("動作測試："+nowStage+"/"+NumActionPoint);
                if(nowStage<NumActionPoint) {
                    float_x_upbound = Float.parseFloat(singlevalue_difference_x[nowStage][0]);
                    float_x_lowbound = Float.parseFloat(singlevalue_difference_x[nowStage][1]);
                    float_y_upbound = Float.parseFloat(singlevalue_difference_y[nowStage][0]);
                    float_y_lowbound = Float.parseFloat(singlevalue_difference_y[nowStage][1]);
                    float_z_upbound = Float.parseFloat(singlevalue_difference_z[nowStage][0]);
                    float_z_lowbound = Float.parseFloat(singlevalue_difference_z[nowStage][1]);
                    goal_x_sensor.setText(singlevalue[nowStage][0]);
                    goal_y_sensor.setText(singlevalue[nowStage][1]);
                    goal_z_sensor.setText(singlevalue[nowStage][2]);
                }


            }
            else{
                now_x_sensor.setTextColor(getColor(R.color.black));
                now_y_sensor.setTextColor(getColor(R.color.black));
                now_z_sensor.setTextColor(getColor(R.color.black));
            }
        }
        else{
            nowStage_totalStage.setText("測試完畢！");
        }

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

}
