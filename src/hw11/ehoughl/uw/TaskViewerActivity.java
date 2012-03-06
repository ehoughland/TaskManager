package hw11.ehoughl.uw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class TaskViewerActivity extends FragmentActivity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_fragment);

        Intent launchingIntent = getIntent();
        String content = launchingIntent.getData().toString();

        TaskViewerFragment viewer = (TaskViewerFragment) getSupportFragmentManager().findFragmentById(R.id.taskview_fragment);

        viewer.updateTask(content);
    }

}