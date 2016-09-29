package com.example.user.test_fix_sensor_version2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.test_fix_sensor_version2.DB.DBhelper;

/**
 * Created by User on 2016/8/24.
 */
public class record extends Activity {
    private Context context;
    private EditText action_name;
    private EditText numbers_of_action_point;
    private Button back;
    private Button confirm;
    private String actionName;
    private String numActionPoint;

    //database
    private DBhelper dBhelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    private boolean has_same_name=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        context=this;

        init();

    }
    public void init(){
        //initialize database
        dBhelper=new DBhelper(context);
        db=dBhelper.getWritableDatabase();

        action_name=(EditText)findViewById(R.id.editText);
        numbers_of_action_point=(EditText)findViewById(R.id.editText2);
        back=(Button)findViewById(R.id.button4);
        confirm=(Button)findViewById(R.id.button3);

        //data operation
        //find all actionname that has used
        cursor=db.rawQuery("SELECT actionname FROM actions",null);



        back.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        confirm.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                //detect that if there same action has existed
                int rows_num=cursor.getCount();
                if(rows_num!=0){
                    cursor.moveToFirst();
                    for(int i=0;i<rows_num;i++){
                        if(action_name.getText().toString().equals(cursor.getString(0))){
                            has_same_name=true;
                        }
                        cursor.moveToNext();
                    }
                }
                if(action_name.getText().toString().length()==0 ||
                        numbers_of_action_point.getText().toString().length()==0){
                    Toast.makeText(context, "請輸入動作名稱與動作點的數目", Toast.LENGTH_SHORT).show();
                }
                else if(has_same_name){
                    Toast.makeText(context, "該動作已存在，請輸入不同動作名稱", Toast.LENGTH_SHORT).show();
                    has_same_name=false;
                }
                else{
                    actionName=action_name.getText().toString();
                    numActionPoint=numbers_of_action_point.getText().toString();

                    Bundle extras = new Bundle();
                    extras.putString("actionName",actionName);
                    extras.putString("numActionPoint",numActionPoint);

                    Intent intent = new Intent(context,record_2.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
