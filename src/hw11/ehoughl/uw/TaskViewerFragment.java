package hw11.ehoughl.uw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TaskViewerFragment extends Fragment 
{
    private TextView tv = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        tv = (TextView) inflater.inflate(R.layout.task_view, container, false);
        return tv;
    }

    public void updateTask(String newTask) 
    {
        if (tv != null) 
        {
            tv.setText(newTask);
        }
    }
}

