package com.strongloop.android.loopback.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strongloop.android.loopback.guide.lessons.LessonFourFragment;
import com.strongloop.android.loopback.guide.util.HtmlFragment;;

public class DisplayFileList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(LessonFourFragment.ID);
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        // Set the text view as the activity layout
        setContentView(textView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends HtmlFragment {

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                  Bundle savedInstanceState) {
              View rootView = inflater.inflate(R.layout.fragment_cover,
                      container, false);
              return rootView;
        }
    }
}