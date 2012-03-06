package hw11.ehoughl.uw;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.SupportActivity;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;
import android.content.ContentResolver;
import android.database.Cursor;

public class TaskListFragment extends ListFragment
{
	private OnTaskSelectedListener taskSelectedListener;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) 
    {
    	// Get the Content Resolver
        ContentResolver cr = getActivity().getContentResolver();
        
        // Query the Content Resolver
        Cursor c = cr.query(Uri.withAppendedPath(TasksDatabase.CONTENT_URI, String.valueOf(id)), TasksDatabase.Task.PROJECTION, null, null, null);
        
        if (c.moveToFirst()) 
        {
            String taskDetails = c.getString(2);
            String taskId = c.getString(0);
            taskSelectedListener.onTaskSelected(taskDetails, taskId);
        }
        
        c.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        String[] projection = { TasksDatabase.Task.ID, TasksDatabase.Task.NAME };
        
        // Setup our mapping from the cursor result to the display field
        String[] from = { TasksDatabase.Task.NAME };
        int[] to = { R.id.title };

        Cursor c = getActivity().managedQuery(TasksDatabase.CONTENT_URI, projection, null, null, null);

        CursorAdapter adapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.list_item, c, from, to);

        setListAdapter(adapter);
    }

    public interface OnTaskSelectedListener 
    {
        public void onTaskSelected(String taskDetails, String taskId);
    }

    @Override
    public void onAttach(SupportActivity activity) 
    {
        super.onAttach(activity);
        
        try 
        {
            taskSelectedListener = (OnTaskSelectedListener) activity;
        } 
        catch (ClassCastException e) 
        {
            throw new ClassCastException(activity.toString() + " must implement OnTaskSelectedListener");
        }
    }
}
