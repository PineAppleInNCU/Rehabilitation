package com.example.user.test_fix_sensor_version2.pebble;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.user.test_fix_sensor_version2.R;
import com.example.user.test_fix_sensor_version2.display;
import com.example.user.test_fix_sensor_version2.record;

/**
 * Created by User on 2016/8/31.
 */
public class pebble_MainActivity extends AppCompatActivity {
    private Button record;
    private Button display;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pebble_main_activity);

        context=this;

        init();
    }
    public void init(){
        record=(Button)findViewById(R.id.button16);
        display=(Button)findViewById(R.id.button17);

        record.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(context, com.example.user.test_fix_sensor_version2.pebble.pebble_record.class);
                startActivity(intent);
            }
        });
        display.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(context, com.example.user.test_fix_sensor_version2.pebble.pebble_display.class);
                startActivity(intent);
            }
        });

    }

}
