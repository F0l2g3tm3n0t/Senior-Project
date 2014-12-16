package lab.kultida.utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

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
                        TABLE_ChatRoom_Message + "TEXT," +
                        TABLE_ChatRoom_Date + " TEXT," +
                        TABLE_ChatRoom_Time + " TEXT," +
                        TABLE_ChatRoom_FromMe + " TEXT" +
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

    public long InsertData(String TABLE_NAME, String[] attribute, String[] data) {
        if(attribute.length != data.length) return -1;
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data
            SQLiteStatement insertCmd;
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
            String strSQL = "INSERT INTO " + TABLE_NAME + " ("+ column +") " +  "VALUES" + " (" + value + ")";
            Log.d("INSERT","SQL LITE : " + strSQL);
            insertCmd = db.compileStatement(strSQL);
            insertCmd.executeUpdateDelete();
            db.close();
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }









    public String getTABLE_ChatRoom(){
        return TABLE_ChatRoom;
    }

    public String[] getTable_ChatRoom_Column(){
        return new String[]{TABLE_ChatRoom_User, TABLE_ChatRoom_Message, TABLE_ChatRoom_Time, TABLE_ChatRoom_Date};
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

    public String getTABLE_USER() {
        return TABLE_USER;
    }
}
