package com.strongloop.android.loopback.guide.lessons;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.User;
import com.strongloop.android.loopback.UserRepository;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.util.HtmlFragment;

/**
 * Implementation for User Lesson. Create, Login, Logout
 */
public class LessonUserFragment extends HtmlFragment {
	
	
	UserRepository userRepo;
	User user;
	
	/**
	 * Creates a user on the server with the given email and password.
	 */
	private void sendCreateRequest() {
		
		// Get the email and password
		String email = getEmail();
		String password = getPassword();
		
		// Create the user from the local repository
		user = userRepo.createUser(email, password);
		
		// Save the user to the server
		user.save(new User.Callback() {

			@Override
			public void onSuccess() {
				showResult("Saved!");
			}

			@Override
			public void onError(Throwable t) {
				Log.e(getTag(), "Cannot save User.", t);
				showResult("Failed.");
			}
		});
	}

	/**
	 * Login the user with the given email and password.
	 */
	private void sendLoginRequest() {
	
		// Get the email and password
		String email = getEmail();
		String password = getPassword();
				
		// Login the user using the repository
		userRepo.loginUser(email, password, 
			new UserRepository.LoginCallback() {
			
			@Override
			public void onSuccess(User newUser) {
				// Local copy of newly logged in user 
				user = newUser;
				showResult("Logged in!");
			}
			
			@Override
			public void onError(Throwable t) {
				showResult("Failed to login in");
			}
		});			
	}
	
	/**
	 * Logout the currently logged in user.
	 */
	private void sendLogoutRequest() {
				
		// Logout the current user
		user.logout(new User.Callback() {
			
			@Override
			public void onSuccess() {
				showResult("Logged out!");
			}
			
			@Override
			public void onError(Throwable t) {
				showResult("Error logging out.");
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
        		R.layout.fragment_lesson_user, container, false));

        setHtmlText(R.id.content, R.string.lessonUser_content);

        installButtonHandlers();

	    // Grab the shared RestAdapter instance.
		GuideApplication app = (GuideApplication)getActivity().getApplication();
		RestAdapter adapter = app.getLoopBackAdapter();
		
		// Create user repository based on model repository
		userRepo = adapter.createRepository(UserRepository.class);
		        
        return getRootView();
	}

	private void installButtonHandlers()
	{
        installCreateButtonClickHandler();
        installLoginButtonClickHandler();	
        installLogoutButtonClickHandler();
	}
	
	private void installCreateButtonClickHandler() {
		final Button button = (Button) getRootView().findViewById(R.id.createButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendCreateRequest();
            }
        });
	}

	private void installLoginButtonClickHandler() {
		final Button button = (Button) getRootView().findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendLoginRequest();
            }
        });
	}

	private void installLogoutButtonClickHandler() {
		final Button button = (Button) getRootView().findViewById(R.id.logoutButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendLogoutRequest();
            }
        });
	}
	
	//
	// Properties for accessing form values
	//

	private String getEditText(int id) {
		final EditText widget = (EditText)getRootView().findViewById(id);
		return widget.getText().toString();
	}
	
	private String getEmail() {
		return getEditText(R.id.editEmail);
	}
	
	private String getPassword() {
		return getEditText(R.id.editPassword);		
	}
}
