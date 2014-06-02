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
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.User;
import com.strongloop.android.loopback.UserRepository;

/**
 * Implementation for Lesson Two: Existing Data? No Problem.
 */
public class LoginFragment extends HtmlFragment {
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
	    
	private void login() {
		GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();
        CustomerRepository userRepo = adapter.createRepository(CustomerRepository.class);
        final EditText username_widget = (EditText) getRootView().findViewById(R.id.username);
        String email =  username_widget.getText().toString();
        System.out.println("Email" + email);
        final EditText password_widget = (EditText) getRootView().findViewById(R.id.password);
        String password =  password_widget.getText().toString();
        System.out.println("Password" + password);
        
		userRepo.loginUser(email, "strongloop", 
				new CustomerRepository.LoginCallback() {

					@Override
					public void onError(Throwable t) {
						String e_message = "Incorrect username or password. Try again";
						System.out.println(e_message);
						Toast.makeText(getActivity(), e_message, Toast.LENGTH_SHORT).show();
						
					}
					@Override
					public void onSuccess(AccessToken token,
							Customer currentUser) {
						System.out.println("Success");
						Toast.makeText(getActivity(), "You are now logged in!", Toast.LENGTH_SHORT).show();
					}
		});
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	System.out.print("onCreateView");
        setRootView((ViewGroup) inflater.inflate(
                R.layout.fragment_login, container, false));
        
        //setHtmlText(R.id.content, R.string.login_content);

        installButtonClickHandler();
        return getRootView();
    }

    private void installButtonClickHandler() {
    	System.out.print("Login Button");
    	final Button login_button = (Button) getRootView().findViewById(R.id.login);
    	
    	login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	System.out.println("Logging in");
            	login();
            }
        });
    	
    }
}
