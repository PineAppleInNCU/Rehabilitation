package com.example.user.test_fix_sensor_version2.pebble;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.test_fix_sensor_version2.R;
import com.example.user.test_fix_sensor_version2.display_3;

/**
 * Created by User on 2016/8/31.
 */
public class pebble_display_2 extends Activity {
    private Context context;
    private TextView action_name;
    private Button start_test;
    private Button fix_action;
    private Button detail_data;
    private Button delete;
    private Button back;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_to_test);
        context = this;

        init();

    }
    public void init(){
        action_name=(TextView)findViewById(R.id.textView3);
        start_test=(Button)findViewById(R.id.button6);
        fix_action=(Button)findViewById(R.id.button9);
        //detail_data=(Button)findViewById(R.id.button13);
        delete=(Button)findViewById(R.id.button10);
        back=(Button)findViewById(R.id.button11);

        extras = getIntent().getExtras();
        action_name.setText(extras.getString("action_name"));

        //進入動作測試頁面
        start_test.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context,com.example.user.test_fix_sensor_version2.pebble.pebble_display_3.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        //刪除動作
        delete.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){


                finish();
            }
        });


    }
}
