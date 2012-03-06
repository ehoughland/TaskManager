package hw11.ehoughl.uw;

import android.net.Uri;
import android.provider.BaseColumns;
import hw11.ehoughl.uw.providers.TasksContentProvider;

public class TasksDatabase implements BaseColumns 
{
    public static final Uri CONTENT_URI = Uri.parse("content://" + TasksContentProvider.AUTHORITY + "/tasks");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hw11.ehoughl.uw.tasks";

    public static final class Task 
    {    
        public static final String TABLE_NAME = "task";

        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String DETAILS = "details";

        public static final String[] PROJECTION = new String[] {TasksDatabase.Task.ID, TasksDatabase.Task.NAME, TasksDatabase.Task.DETAILS };
    } 

}