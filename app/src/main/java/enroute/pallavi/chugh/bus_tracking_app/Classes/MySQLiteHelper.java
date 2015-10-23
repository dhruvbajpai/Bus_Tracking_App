package enroute.pallavi.chugh.bus_tracking_app.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dhruv on 22-Oct-15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "infolist";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "_name";
    public static final String COLUMN_ROUTE = "_route";

    public static final String DATABASE_NAME = "loader.db";
    public static final int DATABASE_VERSION = 3;

//    public static final String DATABASE_CREATE = "create table "
//            + TABLE_NAME + "(" + COLUMN_ID
//            + " integer primary key autoincrement, " + COLUMN_NAME
//            + " text not null, " + COLUMN_ROUTE +
//            " INTEGER not null);";
public static final String DATABASE_CREATE = "create table infolist(_id integer primary key autoincrement, _route integer not null, _name text not null);";
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.d("TAG", "ON UPGRADE AND DROP TABLE CALLED");
        onCreate(db);
    }
    public boolean insertName  (String name, int route)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_ROUTE, route);
        /*contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);*/
        db.insert(TABLE_NAME, null, contentValues);
        Log.d("TAG","INSERT CALLED");
        return true;
    }

    public boolean deleteRoute(int route)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = COLUMN_ROUTE + "=" + route+";";
        String whereargs[] = null;
        db.delete(TABLE_NAME, where, whereargs);
        Log.d("TAG", "DELETE ROUTE CALLED");
        return true;
    }
    public Cursor getData(int route){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from infolist where _route= "+route+" ;", null );
        Log.d("TAG","GET DATA CALLED");
       // Cursor res = db.rawQuery("select * from infolist;",null);
        return res;
    }
}
