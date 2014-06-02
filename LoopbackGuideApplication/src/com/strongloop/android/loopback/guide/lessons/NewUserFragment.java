package com.strongloop.android.loopback.guide.lessons;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.util.HtmlFragment;
import com.strongloop.android.loopback.User;
import com.strongloop.android.loopback.UserRepository;

/**
 * Implementation for Lesson Two: Existing Data? No Problem.
 */
public class NewUserFragment extends HtmlFragment {
    public static class UserModel extends Model {
    	private String email;
    	private String password;
    	
    	public void setEmail(String email) {
			this.email = email;
		}
		public String getEmail() {
			return email;
		}
    	
    	public String getPassword(String password) {
			return password;
		}
		public String getPassword() {
			return password;
		}
    }

	public static final String ID = null;
	 public static class Customer extends User {
	    }
	
	    /**
	     * Repository for our custom User sub-class.
	     */
	    public static class CustomerRepository extends UserRepository<Customer> {
	        public interface LoginCallback extends UserRepository.LoginCallback<Customer> {
	        }
	
	        public CustomerRepository() {
	            super("customer", null, Customer.class);
	        }
	    }
	    
	
	private void createUser(){
		GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();
        CustomerRepository userRepo = adapter.createRepository(CustomerRepository.class);
        final EditText username_widget = (EditText) getRootView().findViewById(R.id.username_new);
        String email =  username_widget.getText().toString();
        System.out.println("Email" + email);
        final EditText password_widget = (EditText) getRootView().findViewById(R.id.password_new);
        String password =  password_widget.getText().toString();
        System.out.println("Password" + password);
    	User user = userRepo.createUser(email, password);
    	user.save(new User.Callback() {
    		@Override
 			public void onSuccess() {
    			System.out.println("Success");
				Toast.makeText(getActivity(), "Created new user", Toast.LENGTH_SHORT).show();
 			}

 			@Override
 			public void onError(Throwable t) {
 				System.out.println("Fail");
				Toast.makeText(getActivity(), "Cannot create this user. Try again!", Toast.LENGTH_SHORT).show();
 			}
    	});
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	System.out.print("onCreateView");
        setRootView((ViewGroup) inflater.inflate(
                R.layout.fragment_create_new_user, container, false));
        
//        setHtmlText(R.id.content, R.string.create_new_account);

        installButtonClickHandler();
        return getRootView();
    }

    private void installButtonClickHandler() {
    	System.out.print("Login Button");
    	final Button create_button = (Button) getRootView().findViewById(R.id.create_user);
    	create_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	System.out.println("Creating new account");
            	createUser();
            }
        });
    }
}
