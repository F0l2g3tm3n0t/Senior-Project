package lab.kultida.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ekapop on 16/12/2557.
 */
public class DataBase extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Database";
    // Table Name
    private final String TABLE_ChatRoom = "CHATROOM";
    private final String TABLE_ChatRoom_User = "user";
    private final String TABLE_ChatRoom_Message = "message";
    private final String TABLE_ChatRoom_Date = "date";
    private final String TABLE_ChatRoom_Time = "time";
    private final String TABLE_ChatRoom_FromMe = "fromMe";

    private final String TABLE_USER = "USER";
    private final String TABLE_USER_Name = "name";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL(
                "CREATE TABLE " + TABLE_ChatRoom +
                        "(" +
                        TABLE_ChatRoom_User +" TEXT, " +
                        TABLE_ChatRoom_Message + " TEXT, " +
                        TABLE_ChatRoom_Date + " TEXT," +
                        TABLE_ChatRoom_Time + " TEXT," +
                        TABLE_ChatRoom_FromMe + "   " +
                        ");"
        );

        Log.d("CREATE TABLE", "Create Table Chatroom Successfully.");

        db.execSQL(
                "CREATE TABLE " + TABLE_USER +
                        "(" +
                        TABLE_USER_Name +" TEXT" +
                        ");"
        );

        Log.d("CREATE TABLE", "Create Table User Successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ChatRoom);
        onCreate(db);
    }

    public long insertData(String TABLE_NAME, String[] attribute, String[] data) {
        if(attribute.length != data.length) return -1;
        SQLiteDatabase db = null;
        try {
            String column = "";
            String value = "";
            if(attribute.length != data.length) return -1;
            for(int i = 0;i < attribute.length;i++){
                if(i != 0 ) {
                    column = column + ",";
                    value = value + ",";
                }
                column = column + attribute[i];
                value = value + "\'" + data[i] + "\'";
            }

            String strSQL = "INSERT INTO " + TABLE_NAME + " ("+ column +") " +  "VALUES" + " (" + value + ")";
            Log.d("Database - InsertData","SQL LITE : " + strSQL);
            db = this.getWritableDatabase();
            SQLiteStatement insertCmd = db.compileStatement(strSQL);
            insertCmd.executeUpdateDelete();
            db.close();
            return 1;
        } catch (Exception e) {
            if(db != null) db.close();
            return -1;
        }
    }

    public JSONArray selectAllDataChatroom(String[] OrderBy){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String order = "";
            for(int i = 0;i < OrderBy.length;i++){
                if(i != 0){
                    order = order + ", ";
                }
                order = order + OrderBy[i];
            }

            String strSQL = "SELECT  * FROM " + TABLE_ChatRoom + " ORDER BY " + order;
            Log.d("Database - SelectAllData","SQL LITE : " + strSQL);
            db = this.getReadableDatabase();
            cursor = db.rawQuery(strSQL, null);
            JSONArray data_frame_array = null;
            if(cursor != null){
                ArrayList<JSONObject> MyArrJson = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        JSONObject data_frame = new JSONObject();
                        JSONObject data = new JSONObject();
                        data.put("user",cursor.getString(0));
                        data.put("message",cursor.getString(1));
                        data.put("date",cursor.getString(2));
                        data.put("time",cursor.getString(3));
                        data_frame.put("fromMe",cursor.getString(4));
                        data_frame.put("data",data);
                        MyArrJson.add(data_frame);
                    } while (cursor.moveToNext());
                }
                data_frame_array = new JSONArray(MyArrJson);
                cursor.close();
            }
            db.close();
            return data_frame_array;
        } catch (Exception e) {
            if(db != null) db.close();
            if(cursor != null) cursor.close();
            return null;
        }
    }

    public JSONArray selectAllDataUser(String[] OrderBy){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String order = "";
            for(int i = 0;i < OrderBy.length;i++){
                if(i != 0){
                    order = order + ", ";
                    order = order + OrderBy[i];
                }
            }

            String strSQL = "SELECT  * FROM " + TABLE_USER + " ORDER BY " + order;
            Log.d("Database - SelectAllData","SQL LITE : " + strSQL);
            db = this.getReadableDatabase();
            cursor = db.rawQuery(strSQL, null);
            JSONArray data_frame_array = null;
            if(cursor != null){
                ArrayList<String> MyArrJson = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        MyArrJson.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
                data_frame_array = new JSONArray(MyArrJson);
                cursor.close();
            }
            db.close();
            return data_frame_array;
        } catch (Exception e) {
            if(db != null) db.close();
            if(cursor != null) cursor.close();
            return null;
        }
    }

    public long deleteData(String TABLE_NAME,String[] attribute, String[] data){
        SQLiteDatabase db = null;
        try {
            String column = "";
            String value = "";
            for(int i = 0;i < attribute.length;i++){
                if(i != 0 ) {
                    column = column + ",";
                    value = value + ",";
                }
                column = column + attribute[i];
                value = value + "\'" + data[i] + "\'";
            }

            String strSQL = "DELETE FROM " + TABLE_NAME + " WHERE" +  " ("+ column +") " +  "VALUES" + " (" + value + ")";
            Log.d("Database - DeleteData","SQL LITE : " + strSQL);
            db = this.getWritableDatabase();
            SQLiteStatement insertCmd = db.compileStatement(strSQL);
            insertCmd.executeUpdateDelete();
            db.close();
            return 1;
        } catch (Exception e) {
            if(db != null) db.close();
            return -1;
        }
    }

    public long delelteAllData(String TABLE_NAME){
        SQLiteDatabase db = null;
        try {
            String strSQL = "DELETE FROM " + TABLE_NAME;
            Log.d("Database - DeleteAllData","SQL LITE : " + strSQL);
            db = this.getWritableDatabase();
            SQLiteStatement insertCmd = db.compileStatement(strSQL);
            insertCmd.executeUpdateDelete();
            db.close();
            return 1;
        } catch (Exception e) {
            if(db != null) db.close();
            return -1;
        }
    }









    public String getTABLE_ChatRoom(){
        return TABLE_ChatRoom;
    }

    public String[] getTable_ChatRoom_Column(){
        return new String[]{TABLE_ChatRoom_User, TABLE_ChatRoom_Message, TABLE_ChatRoom_Date, TABLE_ChatRoom_Time, TABLE_ChatRoom_FromMe};
    }
    
    public String getTABLE_ChatRoom_User() {
        return TABLE_ChatRoom_User;
    }

    public String getTABLE_ChatRoom_Message() {
        return TABLE_ChatRoom_Message;
    }

    public String getTABLE_ChatRoom_Date() {
        return TABLE_ChatRoom_Date;
    }

    public String getTABLE_ChatRoom_Time() {
        return TABLE_ChatRoom_Time;
    }

    public String getTABLE_ChatRoom_FromMe() {
        return TABLE_ChatRoom_FromMe;
    }



    public String getTABLE_USER_Name() {
        return TABLE_USER_Name;
    }

    public String[] getTable_USER_Column(){
        return new String[]{TABLE_USER};
    }

    public String getTABLE_USER() {
        return TABLE_USER;
    }
}
