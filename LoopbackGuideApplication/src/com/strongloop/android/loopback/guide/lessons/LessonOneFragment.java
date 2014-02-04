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
	 * This custom subclass of Model is the closest thing to a "schema" the Note model has.
	 *
	 * When we save an instance of NoteModel, LoopBack uses the property getters and setters
	 * of the subclass to customize the request it makes to the server. The server handles
	 * this freeform request appropriately, saving our freeform model to the database just
	 * as we expect.
	 *
	 * Note: in a regular application, this class would be defined as top-level (non-static)
	 * class in a file of its own. We are keeping it as a static nested class only to make
	 * it easier to follow this guide.
	 */
	public static class NoteModel extends Model {
		private String user;
		private String comment;
		private Boolean reviewed;
		
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		public Boolean getReviewed() {
			return reviewed;
		}
		public void setReviewed(Boolean reviewed) {
			this.reviewed = reviewed;
		}
		
	}

	/**
	 * The ModelRepository provides an interface to the Model's "type" on the server. For instance,
	 * we'll (SPOILER!) see in Lessons Two how the ModelRepository is used for queries;
	 * in Lesson Three we'll use it for custom, collection-level behaviour: those locations within
	 * the collection closest to the given coordinates.
	 *
	 * This subclass, however, provides an additional benefit: it acts as glue within the LoopBack
	 * interface between a RestAdapter representing the _server_ and a named collection or
	 * type of model within it. In this case, that type of model is named "note", and it contains
	 * NoteModel instances.
	 *
	 * Note: in a regular application, this class would be defined as top-level (non-static)
	 * class in a file of its own. We are keeping it as a static nested class only to make
	 * it easier to follow this guide.
	 */
	public static class NoteRepository extends ModelRepository<NoteModel> {
		public NoteRepository() {
			super("note", "notes", NoteModel.class);
		}
	}

	/**
	 * Saves the desired Note model to the server with all values pulled from the UI.
	 */
	private void sendRequest() {
	    // 1. Grab the shared RestAdapter instance.
		GuideApplication app = (GuideApplication)getActivity().getApplication();
		RestAdapter adapter = app.getLoopBackAdapter();

	    // 2. Instantiate our NoteRepository. For the intrepid, notice that we could create this
	    //    once (say, in onCreateView) and use the same instance for every request.
		//    Additionally, the shared adapter is associated with the prototype, so we'd only
		//    have to do step 1 in onCreateView also. This more verbose version is presented
		//    as an example; making it more efficient is left as a rewarding exercise for the reader.
		NoteRepository repository = adapter.createRepository(NoteRepository.class);

	    // 3. From that prototype, create a new NoteModel. We pass in an empty dictionary to defer
		//    setting any values.
		NoteModel model = repository.createModel(null);

	    // 4. Pull model values from the UI.
		model.setUser(getUser());
		model.setComment(getComment());
		model.setReviewed(isReviewed());

		// 5. Save!
		model.save(new Model.Callback() {

			@Override
			public void onSuccess() {
				showResult("Saved!");
			}

			@Override
			public void onError(Throwable t) {
				Log.e(getTag(), "Cannot save Note model.", t);
				showResult("Failed.");
			}
		});
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

	private String getUser() {
		final EditText widget = (EditText) getRootView().findViewById(R.id.editUser);
		return widget.getText().toString();
	}

	private String getComment() {
		final EditText widget = (EditText) getRootView().findViewById(R.id.editCaliber);
		return widget.getText().toString();
	}

	private Boolean isReviewed() {
		final CheckBox widget = (CheckBox) getRootView().findViewById(R.id.editArmorPiercing);
		return widget.isChecked();
	}
}
