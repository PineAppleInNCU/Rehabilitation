package com.example.user.test_fix_sensor_version2.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 2016/8/24.
 */
public class DBhelper extends SQLiteOpenHelper {

    private static final int DATEBASE_VERSION =2;
    private static final String DATABASE_NAME = "actions.db";
    public static final String TABLE_ACTIONS = "actions";//table  isn't equal to database name
    public static final String TABLE_ACTIONS_2="pebble_actions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "actionname";
    public static final String COLUMN_ACTIONDATA = "actiondata";
    public static final String COLUMN_NUMBERS_OF_POINTE = "number_point";
    public static final String COLUMN_DIFFERENCE_X= "difference_x";
    public static final String COLUMN_DIFFERENCE_Y= "difference_y";
    public static final String COLUMN_DIFFERENCE_Z= "difference_z";


    public static final String DROP_TABLE_SQL="DROP TABLE IF EXISTS actions";
    public static final String CREATE_TABLE_SQL="  CREATE TABLE   " + TABLE_ACTIONS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_NAME
            + "   TEXT ,  " + COLUMN_ACTIONDATA + "   TEXT  ,  "+ COLUMN_NUMBERS_OF_POINTE + " INTEGER  , "
            +  COLUMN_DIFFERENCE_X + " TEXT  ,   "  +  COLUMN_DIFFERENCE_Y + " TEXT  ,   "  +  COLUMN_DIFFERENCE_Z + " TEXT    );";


    //為了可以一次建立兩張table
    public static final String CREATE_TABLE_SQL_2="  CREATE TABLE   " + TABLE_ACTIONS_2 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_NAME
            + "   TEXT ,  " + COLUMN_ACTIONDATA + "   TEXT  ,  "+ COLUMN_NUMBERS_OF_POINTE + " INTEGER  , "
            +  COLUMN_DIFFERENCE_X + " TEXT  ,   "  +  COLUMN_DIFFERENCE_Y + " TEXT  ,   "  +  COLUMN_DIFFERENCE_Z + " TEXT    );";


    public DBhelper(Context context){
        super(context,DATABASE_NAME,null,DATEBASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_SQL);
        db.execSQL(CREATE_TABLE_SQL_2);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db , int oldVersion , int newVersion){
        onDropTable(db);
        onCreate(db);
    }
    public void onDropTable(SQLiteDatabase db) {
        db.execSQL(DROP_TABLE_SQL);
    }

}

