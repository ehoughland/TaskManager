package hw11.ehoughl.uw;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class TaskListActivity extends FragmentActivity implements TaskListFragment.OnTaskSelectedListener, ActionBar.TabListener
{
	final static private String APP_KEY = "ugf2j5yknmlcisb";
	final static private String APP_SECRET = "lemapgq3348lwwr";
	final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;
	private DropboxAPI<AndroidAuthSession> mDBApi;
	private int tabSelectedCount = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasklist_fragment);
		
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tabAdd = getSupportActionBar().newTab();
        tabAdd.setText("Add");
        tabAdd.setTabListener(this);
        getSupportActionBar().addTab(tabAdd);
        
        ActionBar.Tab tabEdit = getSupportActionBar().newTab();
        tabEdit.setText("Edit");
        tabEdit.setTabListener(this);
        getSupportActionBar().addTab(tabEdit);
        
        ActionBar.Tab tabDelete = getSupportActionBar().newTab();
        tabDelete.setText("Delete");
        tabDelete.setTabListener(this);
        getSupportActionBar().addTab(tabDelete);
        
        //todo: add dropbox to options menu and do this stuff on item selected there.
        //then add the stuff to sharedprefs. on load, check prefs for dropbox enabled and such.
        
        //authenticate dropbox
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().startAuthentication(TaskListActivity.this);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();

	    if (mDBApi.getSession().authenticationSuccessful()) 
	    {
	        try 
	        {
	            // MANDATORY call to complete auth.
	            // Sets the access token on the session
	            mDBApi.getSession().finishAuthentication();

	            AccessTokenPair tokens = mDBApi.getSession().getAccessTokenPair();

	            // Provide your own storeKeys to persist the access token pair
	            // A typical way to store tokens is using SharedPreferences
	            storeKeys(tokens.key, tokens.secret);
	        } 
	        catch (IllegalStateException e) 
	        {
	            Log.i("DbAuthLog", "Error authenticating", e);
	        }
	    }
	}
	
	private void storeKeys(String key, String secret)
	{
		//add to preferences
		
	}

	@Override
	public void onTaskSelected(String taskDetails, String taskId)
	{
		TaskViewerFragment viewer = (TaskViewerFragment) getSupportFragmentManager().findFragmentById(R.id.taskview_fragment);
		setHiddenId(taskId);
		
		if (viewer == null || !viewer.isInLayout()) 
		{
			Intent showContent = new Intent(getApplicationContext(), TaskViewerActivity.class);
			showContent.setData(Uri.parse(taskDetails));
			startActivity(showContent);
		}  
		else 
		{
			viewer.updateTask(taskDetails);
		}
	}

	public void setHiddenId(String id)
	{
		TextView tvHiddenId = (TextView)findViewById(R.id.hiddenId);
		tvHiddenId.setText(id);
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		tabSelectedCount++;
		
		if(tabSelectedCount == 1)
			return;
		
		TextView tvHiddenId = (TextView)findViewById(R.id.hiddenId);
		String id = tvHiddenId.getText().toString();
		
		if(id == "")
		{
			if(tab.getText() == "Add")
			{
				//add a task
				Intent addEditTaskActivity = new Intent(getApplicationContext(), AddEditTaskActivity.class);
				
				Bundle b = new Bundle();
				b.putString("id", "");
				b.putString("name", "");
				b.putString("details", "");
				
				addEditTaskActivity.putExtras(b);
				
				startActivity(addEditTaskActivity);
				
				tvHiddenId.setText("");
				TextView tvTaskDetails = (TextView)findViewById(R.id.taskView);
			    tvTaskDetails.setText("");
			}
			else
			{
				Context context = getApplicationContext();
				CharSequence text = "You don't have a task selected.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
		else
		{
			if(tab.getText() == "Add")
			{
				//add a task
				Intent addEditTaskActivity = new Intent(getApplicationContext(), AddEditTaskActivity.class);
				
				Bundle b = new Bundle();
				b.putString("id", "");
				b.putString("name", "");
				b.putString("details", "");
				
				addEditTaskActivity.putExtras(b);
				
				startActivity(addEditTaskActivity);
				
				tvHiddenId.setText("");
				TextView tvTaskDetails = (TextView)findViewById(R.id.taskView);
			    tvTaskDetails.setText("");
			}
			else if(tab.getText() == "Delete")
			{
				//confirm deletion
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Are you sure you want to delete the selected task?")
				       .setCancelable(false)
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				        	   ContentResolver cr = getContentResolver();
				        	   TextView tvHiddenId = (TextView)findViewById(R.id.hiddenId);
				        	   String _id = tvHiddenId.getText().toString();
						       cr.delete(TasksDatabase.CONTENT_URI, "_id = " + _id, null);
						       tvHiddenId.setText("");
						       TextView tvTaskDetails = (TextView)findViewById(R.id.taskView);
						       tvTaskDetails.setText("");
				           }
				       })
				       .setNegativeButton("No", new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				                dialog.cancel();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
			else if(tab.getText() == "Edit")
			{
				String taskName = "";
	            String taskDetails = "";
	            
				Intent addEditTaskActivity = new Intent(getApplicationContext(), AddEditTaskActivity.class);
		        ContentResolver cr = getContentResolver();
		        Cursor c = cr.query(Uri.withAppendedPath(TasksDatabase.CONTENT_URI, String.valueOf(id)), TasksDatabase.Task.PROJECTION, null, null, null);
		        
		        if (c.moveToFirst()) 
		        {
		        	taskName = c.getString(1);
		            taskDetails = c.getString(2);
		        }
		        
		        c.close();
		        
				Bundle b = new Bundle();
				b.putString("id", id);
				b.putString("name", taskName);
				b.putString("details", taskDetails);
				
				addEditTaskActivity.putExtras(b);
				startActivity(addEditTaskActivity);
				
				TextView tvTaskDetails = (TextView)findViewById(R.id.taskView);
			    tvTaskDetails.setText("");
				tvHiddenId.setText("");
			}
		}
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) 
	{
		onTabSelected(tab, ft);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
	{
		//not implemented
	}
}