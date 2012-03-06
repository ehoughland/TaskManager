package hw11.ehoughl.uw.providers;

import java.util.HashMap;
import hw11.ehoughl.uw.TasksDatabase;
import hw11.ehoughl.uw.TasksSQLiteOpenHelper;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class TasksContentProvider extends ContentProvider 
{
    public static final String TAG = TasksContentProvider.class.getSimpleName();

    public static final String AUTHORITY = "hw11.ehoughl.uw.providers.TasksContentProvider";

    private static final int TASK = 1;
    private static final int TASK_ID = 2;

    TasksSQLiteOpenHelper mSQLHelper;

    private static final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static HashMap<String, String> tasksProjectionMap;

    static 
    {
        mURIMatcher.addURI(AUTHORITY, "tasks", TASK);
        mURIMatcher.addURI(AUTHORITY, "tasks/#", TASK_ID);

        tasksProjectionMap = new HashMap<String, String>();
        tasksProjectionMap.put(TasksDatabase.Task.ID, TasksDatabase.Task.ID);
        tasksProjectionMap.put(TasksDatabase.Task.NAME, TasksDatabase.Task.NAME);
        tasksProjectionMap.put(TasksDatabase.Task.DETAILS, TasksDatabase.Task.DETAILS);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) 
    {
        SQLiteDatabase db = mSQLHelper.getWritableDatabase();
        int count;
        
        switch (mURIMatcher.match(uri)) 
        {
        	case TASK:
        		count = db.delete(TasksDatabase.Task.TABLE_NAME, selection, selectionArgs);
            break;

        	default:
        		throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) 
    {
        switch (mURIMatcher.match(uri)) 
        {
        	case TASK:
        		return TasksDatabase.CONTENT_TYPE;

        	default:
        		throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues cv) 
    {
        if (mURIMatcher.match(uri) != TASK) 
        {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mSQLHelper.getWritableDatabase();
        long rowID = db.insert(TasksDatabase.Task.TABLE_NAME, null, cv);
        
        if (rowID > 0) 
        {
            Uri noteUri = ContentUris.withAppendedId(TasksDatabase.CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        else 
        {
            Log.e(TAG, "insert() Error inserting task");
        }

        return null;
    }

    @Override
    public boolean onCreate() 
    {
        mSQLHelper = new TasksSQLiteOpenHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) 
    {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TasksDatabase.Task.TABLE_NAME);
        
        switch (mURIMatcher.match(uri)) 
        {
        	case TASK_ID:
            qb.appendWhere(TasksDatabase.Task.ID + "="
                    + uri.getLastPathSegment());
            break;
            
        	case TASK:
        		break;

        	default:
        		throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mSQLHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) 
    {
        SQLiteDatabase db = mSQLHelper.getWritableDatabase();

        int count;
        switch (mURIMatcher.match(uri)) {
        case TASK:
            count = db.update(TasksDatabase.Task.TABLE_NAME, values, selection, selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;

    }

}
