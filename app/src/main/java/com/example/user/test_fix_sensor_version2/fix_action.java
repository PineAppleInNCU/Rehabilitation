package com.example.user.test_fix_sensor_version2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 2016/9/9.
 */
public class fix_action extends Activity {
    private Context context;
    private TextView action_name;
    private EditText numbers_of_action_point;
    private Button back;
    private Button confirm;
    private String actionName;
    private String numActionPoint;

    private Bundle extra = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fix_action);

        context=this;

        init();

    }
    private void init(){

        extra=getIntent().getExtras();

        numbers_of_action_point=(EditText)findViewById(R.id.editText2);
        back=(Button)findViewById(R.id.button4);
        confirm=(Button)findViewById(R.id.button3);
        action_name=(TextView)findViewById(R.id.textView25);
        action_name.setText(extra.getString("action_name"));


        //back
        back.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        //back//
        confirm.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                //detect that if there same action has existed

                    actionName=extra.getString("action_name");
                    numActionPoint=numbers_of_action_point.getText().toString();

                    Bundle extras = new Bundle();
                    extras.putString("actionName",actionName);
                    extras.putString("numActionPoint",numActionPoint);

                    Intent intent = new Intent(context,fix_action_2.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();

            }
        });

    }


}
