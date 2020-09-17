package com.chauk.masbahati.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.chauk.masbahati.utils.Tasbih;
import java.util.ArrayList;

/**
 * Created by Win8.1User on 5/24/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="masbha.db";
    private static final int SCHEMA_VERSION=2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        Log.d("bdd","calling constructor of open helper");
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE tasbih (id INTEGER PRIMARY KEY AUTOINCREMENT,count INTEGER);");
        Log.d("bdd","onCreate");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS tasbih ");
        }
        Log.d("bdd","onUpgrade");
    }




    public long insert(int nb_tasbih){
        long inserted_record_id;
        ContentValues cv = new ContentValues();
        cv.put("count", nb_tasbih);
        inserted_record_id=getWritableDatabase().insert("tasbih", "count", cv);
        Log.d("MainActivity","insert of a new record returns id= "+inserted_record_id);
        return inserted_record_id;

    }

    public void updateById(int id, int newCount){
        ContentValues cv = new ContentValues();
        int updated_record_id;
        cv.put("count", newCount);
        updated_record_id=this.getWritableDatabase().update("tasbih",cv, "id ="+id, null);
        Log.d("MainActivity","update of record id= "+id+" returns:"+updated_record_id);
    }


    public ArrayList<Tasbih> getTasbih(){
        ArrayList<Tasbih> arrayList=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from tasbih",null);
        while(res.moveToNext()){
            Tasbih t = new Tasbih();
            t.setId(res.getInt(0));
            t.setCounter(res.getInt(1));

           arrayList.add(t);

        }
        return arrayList;
    }

    public Tasbih getTasbihById(int id){
      //  ArrayList<tasbih> arrayList=new ArrayList<>();
        Tasbih t = new Tasbih();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from tasbih where id = "+id,null);
        while(res.moveToNext()){

            t.setId(id);
            t.setCounter(res.getInt(1));

          //  arrayList.add(t);

        }
        return t;
    }
}
