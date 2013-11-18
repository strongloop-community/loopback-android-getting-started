package com.strongloop.android.loopback.guide.lessons;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.util.HtmlFragment;

/**
 * Implementation for Lesson One: One Model, Hold the Schema
 */
public class LessonOneFragment extends HtmlFragment {

	/**
     * Insert custom subclasses of LBModel and LBModelPrototype here.
     */

    /**
     * Saves the desired Ammo model to the server with all values pulled from the UI.
     */
    private void sendRequest() {
        /**
         * Insert implementation here.
         */
    }

	void showResult(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}

	//
	// GUI glue
	//

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
        		R.layout.fragment_lesson_one, container, false));

        setHtmlText(R.id.content, R.string.lessonOne_content);

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

	//
	// Properties for accessing form values
	//

	private String getName() {
		final EditText widget = (EditText) getRootView().findViewById(R.id.editName);
		return widget.getText().toString();
	}

	private String getCaliber() {
		final EditText widget = (EditText) getRootView().findViewById(R.id.editCaliber);
		return widget.getText().toString();
	}

	private Boolean isArmorPiercing() {
		final CheckBox widget = (CheckBox) getRootView().findViewById(R.id.editArmorPiercing);
		return widget.isChecked();
	}
}
