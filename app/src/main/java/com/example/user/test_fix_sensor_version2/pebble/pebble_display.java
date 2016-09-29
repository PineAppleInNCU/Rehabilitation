package com.example.user.test_fix_sensor_version2.pebble;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.user.test_fix_sensor_version2.DB.DBhelper;
import com.example.user.test_fix_sensor_version2.DB.pebble_DBhelper;
import com.example.user.test_fix_sensor_version2.R;
import com.example.user.test_fix_sensor_version2.display_2;
import com.getpebble.android.kit.PebbleKit;

/**
 * Created by User on 2016/8/31.
 */
public class pebble_display extends Activity {
    private Context context;
    private ListView listView = null;
    private pebble_DBhelper dBhelper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;
    private Cursor maincursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecting);

        context=this;

        init();
    }
    public void init(){
        dBhelper=new pebble_DBhelper(context);
        db=dBhelper.getWritableDatabase();

        listView =(ListView)findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.emptyView));
        listView.setOnItemClickListener(new MyOnItemClickListener());

        // 重新整理ListView
        refreshListView();
    }
    // 重新整理ListView（將資料重新匯入）
    private void refreshListView() {
        if (maincursor == null) {
            // 1.取得查詢所有資料的cursor
            maincursor = db.rawQuery(
                    "SELECT _id, actionname,actiondata,number_point,difference_x,difference_y,difference_z  FROM pebble_actions", null);
            // 2.設定ListAdapter適配器(使用SimpleCursorAdapter)
            adapter = new SimpleCursorAdapter(context, R.layout.row,
                    maincursor,
                    new String[] { "_id", "actionname", "actiondata","number_point","difference_x","difference_y","difference_z" },
                    new int[] { R.id.action_Id, R.id.actionName},
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            // 3.注入適配器
            listView.setAdapter(adapter);
        } else {
            if (maincursor.isClosed()) { // 彌補requery()不會檢查cursor closed的問題
                maincursor = null;
                refreshListView();
            } else {
                maincursor.requery(); // 若資料龐大不建議使用此法（應改用 CursorLoader）
                adapter.changeCursor(maincursor);
                adapter.notifyDataSetChanged();
            }
        }
    }
    // OnItemClick 監聽器
    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // 取得 Cursor
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            String action_name=cursor.getString(1);
            String action_data=cursor.getString(2);
            int number_point = cursor.getInt(3);
            String difference_x=cursor.getString(4);
            String difference_y=cursor.getString(5);
            String difference_z=cursor.getString(6);
            Intent intent = new Intent(context,com.example.user.test_fix_sensor_version2.pebble.pebble_display_2.class);
            Bundle extras = new Bundle();
            extras.putString("action_name",action_name);
            extras.putString("action_data",action_data);
            extras.putInt("number_point",number_point);
            extras.putString("difference_x",difference_x);
            extras.putString("difference_y",difference_y);
            extras.putString("difference_z",difference_z);
            intent.putExtras(extras);

            startActivity(intent);

            //lookdata(cursor);//display dialog
        }
    }
}
