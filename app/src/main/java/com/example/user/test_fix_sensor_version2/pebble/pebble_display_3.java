package com.example.user.test_fix_sensor_version2.pebble;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.test_fix_sensor_version2.R;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;

/**
 * Created by User on 2016/8/31.
 */
public class pebble_display_3 extends Activity {
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
    //pebble
    private PebbleKit.PebbleDataReceiver dataReceiver;
    final int AppKeyAge_x = 1;
    final int AppKeyAge_y = 2;
    final int AppKeyAge_z = 3;
    // The UUID of the watchapp
    final UUID appUuid = UUID.fromString("6136584b-35cb-4a20-82ba-0bcdf097db06");
    private String TAG="test";
    //pebble//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing);

        context=this;

        init();


        //pebble
        // Create a new receiver to get AppMessages from the C app
        dataReceiver = new PebbleKit.PebbleDataReceiver(appUuid) {

            @Override
            public void receiveData(Context context, int transaction_id,
                                    PebbleDictionary dict) {
                // A new AppMessage was received, tell Pebble
                PebbleKit.sendAckToPebble(context, transaction_id);
                // If the tuple is present...
                Long ageValue_x = dict.getInteger(AppKeyAge_x);
                if(ageValue_x != null) {
                    // Read the integer value
                    int age = ageValue_x.intValue();
                    gravity[0]=(float)age/100;
                    now_x_sensor.setText("X:"+gravity[0]);
                    Log.i("recieve_data_test:","success!");
                }
                // If the tuple is present...
                Long ageValue_y = dict.getInteger(AppKeyAge_y);
                if(ageValue_y != null) {
                    // Read the integer value
                    int age = ageValue_y.intValue();
                    gravity[1]=(float)age/100;
                    now_y_sensor.setText("Y:"+gravity[1]);
                    Log.i("recieve_data_test:","success!");
                }
                // If the tuple is present...
                Long ageValue_z = dict.getInteger(AppKeyAge_z);
                if(ageValue_z != null) {
                    // Read the integer value
                    int age = ageValue_z.intValue();
                    gravity[2]=(float)age/100;
                    now_z_sensor.setText("Z:"+gravity[2]);
                    Log.i("recieve_data_test:","success!");
                }
                //starting to test the action
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
        };
    }
    public void init(){
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

        //on click listener
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

                }
            }
        });
        //on click listener//
    }
    @Override
    public void onResume() {
        super.onResume();

        // Register the receiver
        PebbleKit.registerReceivedDataHandler(getApplicationContext(), dataReceiver);
    }
    @Override
    protected void onPause() {
        super.onPause();

        try {
            unregisterReceiver(dataReceiver);
        } catch(Exception e) {
            Log.w(TAG, "Receiver did not need to be unregistered");
        }
    }

}
