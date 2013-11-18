package com.strongloop.android.loopback.guide.lessons;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.util.HtmlFragment;

/**
 * Implementation for Lesson Two: Existing Data? No Problem.
 */
public class LessonTwoFragment extends HtmlFragment {

    /**
     * Insert custom subclasses of Model and ModelRepository
     */

    /**
     * Loads all Weapon models from the server. To make full use of this, return to your (running)
     * Sample Application and restart it with the DB environment variable set to "oracle".
     * For example, on most *nix flavors (including Mac OS X), that looks like:
     *
     * 1. Stop the current server with Ctrl-C.
     * 2. DB=oracle slc run app
     *
     * What does this do, you ask? Without that environment variable, the Sample Application uses
     * simple, in-memory storage for all models. With the environment variable, it uses a custom-made
     * Oracle adapter with a demo Oracle database we host for this purpose. If you have existing
     * data, it's that easy to pull into LoopBack. No need to leave it behind.
     *
     * Advanced users: LoopBack supports multiple data sources simultaneously, albeit on a per-model
     * basis. In your next project, try connecting a schema-less model (e.g. our Ammo example)
     * to a Mongo data source, while connecting a legacy model (e.g. this Weapon example) to
     * an Oracle data source.
     */
    private void sendRequest() {
        /**
         * Insert implementation here.
         */
    }

    private void showResult(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Basic ListAdapter implementation using our custom Model type.
     * Replace all occurrences of Model in the code below with your
     * custom subclass.
     */
    private static class WeaponListAdapter extends ArrayAdapter<Model> {
        public WeaponListAdapter(Context context, List<Model> list) {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, null);
            }

            Model model = getItem(position);
            if (model == null) return convertView;

            TextView textView = (TextView)convertView.findViewById(
                    android.R.id.text1);
            textView.setText("TODO - build your own text for each item here");

            return convertView;
        }
    }

    //
    // GUI glue
    //
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
                R.layout.fragment_lesson_two, container, false));

        list = (ListView)getRootView().findViewById(R.id.list);

        setHtmlText(R.id.content, R.string.lessonTwo_content);

        installButtonClickHandler();

        return getRootView();
    }

    private void installButtonClickHandler() {
        final Button button = (Button) getRootView().findViewById(R.id.sendRequest);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendRequest();
            }
        });
    }
}
