package hw11.ehoughl.uw;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddEditTaskActivity extends Activity
{
	EditText editTextName;
	EditText editTextDetails;
	String id;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle passed) 
    {
        super.onCreate(passed);
        setContentView(R.layout.add_edit_task);
        
        Bundle b = getIntent().getExtras();
        
        if(b != null)
        {
        	editTextName = (EditText)findViewById(R.id.editTextName);
        	editTextDetails = (EditText)findViewById(R.id.editTextDetails);
        	
        	id = b.get("id").toString();
        	editTextName.setText(b.get("name").toString());
        	editTextDetails.setText(b.get("details").toString());
        }
    }
    
    public void onClickAddEdit(View v)
    {
    	if(id.equals(""))
    	{
    		//adding a new record
    		ContentValues cv = new ContentValues();
            cv.put(TasksDatabase.Task.NAME, editTextName.getText().toString());
            cv.put(TasksDatabase.Task.DETAILS, editTextDetails.getText().toString());
            this.getContentResolver().insert(TasksDatabase.CONTENT_URI, cv);
    	}
    	else
    	{
    		//editing an existing record
    		ContentResolver cr = this.getContentResolver();
    		ContentValues cv = new ContentValues();
        	cv.put("name", editTextName.getText().toString());
        	cv.put("details", editTextDetails.getText().toString());
        	cr.update(TasksDatabase.CONTENT_URI, cv, "_id = " + id, null);
    	}
    	
    	AddEditTaskActivity.this.finish();
    }
    
    public void onClickCancel(View v)
    {
    	AddEditTaskActivity.this.finish();
    }
}
