package com.example.user.test_fix_sensor_version2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button record;
    private Button display;
    private Button use_pebble;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;

        init();
    }
    public void init(){
        record=(Button)findViewById(R.id.button);
        display=(Button)findViewById(R.id.button2);
        use_pebble=(Button)findViewById(R.id.button15);

        record.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(context,record.class);
                startActivity(intent);
            }
        });
        display.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(context,display.class);
                startActivity(intent);
            }
        });
        use_pebble.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(context,com.example.user.test_fix_sensor_version2.pebble.pebble_MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
