package com.strongloop.android.loopback.guide.lessons;

import java.util.List;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.guide.DisplayFileList;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.util.HtmlFragment;
import com.strongloop.android.loopback.Container;
import com.strongloop.android.loopback.ContainerRepository;

public class LessonFourFragment extends HtmlFragment {

    public static class ContainerModel extends Model {
    	private String name;
    	private int size;
    	private Date atime;
    	private Date ctime;
    	private Date mtime;
    	
    	public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public Date getAtime() {
			return atime;
		}
		public void setAtime(Date atime) {
			this.atime = atime;
		}
		public Date getCtime() {
			return ctime;
		}
		public void setCtime(Date ctime) {
			this.ctime = ctime;
		}
		public Date getMtime() {
			return mtime;
		}
		public void setMtime(Date mtime) {
			this.mtime = mtime;
		}
    }

	public static final String ID = null;

    private void addContainer() {
    	System.out.println("Adding a container");
    	GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();
        
    	ContainerRepository repository = adapter.createRepository(ContainerRepository.class);
		repository.create(getName(), new ObjectCallback<Container>() {
			@Override
			public void onSuccess(Container object) {
				showResult("Saved!");
			}
			@Override
			public void onError(Throwable t) {
				Log.e(getTag(), "Cannot save Container", t);
				showResult("Failed.");
			}
		});
    }
    
    private void getContainers() {
        final GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();

        ContainerRepository repository = adapter.createRepository(ContainerRepository.class);
        repository.getAll(new ListCallback<Container>(){

			@Override
			public void onSuccess(final List<Container> containerList) {
				System.out.println("Containers fetched successfully-"+ containerList);
            	list.setAdapter(new ContainerListAdapter(getActivity(), containerList));
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                
					@Override
		            public void onItemClick(AdapterView<?> parent, View view,
		                                    int position, long id) {
		            	Intent intent = new Intent(getActivity(), DisplayFileList.class);
		            	intent.putExtra(ID, containerList.get(position).getName());
		            	startActivity(intent);
		            }
		        });
			}
	          @Override
	            public void onError(Throwable t) {
	            	System.out.print("onError");
	                Log.e(getTag(), "Fetching containers failed", t);
	                showResult("Failed.");
	            }
	        });  
    }

    private void showResult(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Basic ListAdapter implementation using our custom Model type.
     */
    private static class ContainerListAdapter extends ArrayAdapter<Container> {
        public ContainerListAdapter(Context context, List<Container> list) {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	System.out.print("getView");
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, null);
            }
            Container model = getItem(position);
            if (model == null) return convertView;
            System.out.println(android.R.id.text1);
            TextView textView = (TextView)convertView.findViewById(
                    android.R.id.text1);
            textView.setText(
            		model.getName());
            
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
    	System.out.print("onCreateView");
        setRootView((ViewGroup) inflater.inflate(
                R.layout.fragment_containers, container, false));

        list = (ListView)getRootView().findViewById(R.id.list);

        setHtmlText(R.id.content, R.string.containers_content);

        installButtonClickHandler();
        return getRootView();
    }

    private void installButtonClickHandler() {
    	System.out.println("Install Button");
    	final Button button = (Button) getRootView().findViewById(R.id.addContainer);
    	final Button button1 = (Button) getRootView().findViewById(R.id.getContainers);
    	button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	addContainer();
            }
        });
    	button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	System.out.println("Install Button get");
            	getContainers();
            }
        });
    }
    
    private String getName() {
		final EditText widget = (EditText) getRootView().findViewById(R.id.containerName);
		return widget.getText().toString();
	}
}
