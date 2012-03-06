package hw11.ehoughl.uw;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TasksSQLiteOpenHelper extends SQLiteOpenHelper 
{
    private final static String DB_NAME = "tasks";
    private final static int DB_VERSION = 3;

    private final static String TABLE_NAME = TasksDatabase.Task.TABLE_NAME;
    private final static String TABLE_ROW_ID = TasksDatabase.Task.ID;
    private final static String TABLE_ROW_NAME = TasksDatabase.Task.NAME;
    private final static String TABLE_ROW_DETAILS = TasksDatabase.Task.DETAILS;

    public TasksSQLiteOpenHelper(Context context) 
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) 
    {	
        String createTableQueryString = 
                        "CREATE TABLE " + 
                        TABLE_NAME + " (" + 
                        TABLE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
                        TABLE_ROW_NAME + " TEXT, " +
                        TABLE_ROW_DETAILS + " TEXT" + ");";
        
        db.execSQL(createTableQueryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}