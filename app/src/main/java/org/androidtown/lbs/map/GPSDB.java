package org.androidtown.lbs.map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GPSDB {

    private DBHelper helper;
    String dbName = "gpsdb.db";
    int dbVersion = 1; // 데이터베이스 버전
    private SQLiteDatabase db;
    String tag = "SQLite"; // Log 에 사용할 tag
    Context mContext;

    GPSDB(Context mContext){
        helper = new DBHelper(mContext, dbName, null, dbVersion);
        this.mContext = mContext;

        try{
            db = helper.getWritableDatabase();
        }catch(SQLiteException sqle){
            Log.e(tag, "데이터베이스를 얻어올 수 없음");
        }
    }

    void deleteall(){
        Cursor c = db.rawQuery("select * from gpstable;", null);
        while(c.moveToNext()) {
            int id = c.getInt(0);
            db.execSQL("delete from gpstable where id=" + id +";");
        }

        Log.e(tag,"delete 완료");
    }

    void insert(double lati, double longi, CharSequence type){
        try {
            db.execSQL("insert into gpstable (latitude,longitude,type) values(" + lati + "," + longi + ",'" + type + "');");
            Log.e(tag, "insert 성공");
        }catch(Exception e){
            Log.e(tag, "insert 실패");
        }
    }

    void get(GoogleMap map){
        MarkerOptions marker = new MarkerOptions();
        Cursor c = db.rawQuery("select * from gpstable;", null);
        while(c.moveToNext()) {

            int id = c.getInt(0);
            double lati = c.getDouble(1);
            double longi = c.getDouble(2);
            String type = c.getString(3);

            marker.position(new LatLng(lati, longi));
            marker.title(type);
            marker.draggable(true);
            map.addMarker(marker);
        }
    }


}