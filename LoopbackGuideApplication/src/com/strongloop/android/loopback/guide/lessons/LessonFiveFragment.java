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
import android.app.Activity;
import android.widget.ImageView;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.guide.CoverFragment;
import com.strongloop.android.loopback.guide.DisplayFileList;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.R.id;
import com.strongloop.android.loopback.guide.R.layout;
import com.strongloop.android.loopback.guide.R.string;
import com.strongloop.android.loopback.guide.lessons.LessonFourFragment.ContainerModel;
import com.strongloop.android.loopback.guide.util.HtmlFragment;



/**
 * Implementation for Lesson Two: Existing Data? No Problem.
 */
public class LessonFiveFragment extends HtmlFragment {

    /**
     * Unlike Lesson One, our CarModel class is based _entirely_ on an existing schema.
     *
     * In this case, every field in Oracle that's defined as a NUMBER type becomes a Number,
     * and each field defined as a VARCHAR2 becomes a String.
     *
     * When we load these models from Oracle, LoopBack uses these property setters and getters
     * to know what data we care about. If we left off `extras`, for example, LoopBack would
     * simply omit that field.
     */
    public static class FileModel extends Model {
    	private String containername;
    	private String name;
    	private int size;
    	private Date atime;
    	private Date ctime;
    	private Date mtime;
    	
    	public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
    	
    	public String getContainerName(String containername) {
			return containername;
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

    /**
     * Our custom ModelRepository subclass. See Lesson One for more information.
     */
    public static class FileRepository extends ModelRepository<FileModel> {
        public FileRepository() {
            super("file", FileModel.class);
        }
    }

	public static final String ID = null;

    /**
     * Loads all Car models from the server. To make full use of this, return to your (running)
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
     * basis. In your next project, try connecting a schema-less model (e.g. our Note example)
     * to a Mongo data source, while connecting a legacy model (e.g. this Car example) to
     * an Oracle data source.
     */
    private void addFile() {
    	System.out.println("Adding a file");
    }
    
    private void getFiles() {
        // 1. Grab the shared RestAdapter instance.
        GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();

        // 2. Instantiate our CarRepository.See LessonOneView for further discussion.
        FileRepository repository = adapter.createRepository(FileRepository.class);
        // 3. Rather than instantiate a model directly like we did in Lesson One, we'll query
        //    the server for all Cars, filling out our ListView with the results. In this case,
        //    the Repository is really the workhorse; the Model is just a simple container.
        repository.findAll(new ModelRepository.FindAllCallback<LessonFiveFragment.FileModel>() {
        	@Override
            public void onSuccess(final List<FileModel> models) {
            	System.out.println("Files fetched successfully");
            	System.out.print(models);
                list.setAdapter(new FileListAdapter(getActivity(), models));
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                
					@Override
		            public void onItemClick(AdapterView<?> parent, View view,
		                                    int position, long id) {
		            	System.out.println("Intent");
		            	System.out.println(position);
		            	//System.out.println(repository.findById(position));
		            	//System.out.println(name);
		            	Intent intent = new Intent(getActivity(), DisplayFileList.class);
		            	intent.putExtra(ID, models.get(position).getName());
		            	startActivity(intent);
		            }
		        });
            }

            @Override
            public void onError(Throwable t) {
            	System.out.print("onError");
                Log.e(getTag(), "Fetching files failed", t);
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
    private static class FileListAdapter extends ArrayAdapter<FileModel> {
        public FileListAdapter(Context context, List<FileModel> list) {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	System.out.print("getView");
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, null);
            }
            FileModel model = getItem(position);
            if (model == null) return convertView;
            System.out.println(android.R.id.text1);
            TextView textView = (TextView)convertView.findViewById(
                    android.R.id.text1);
            textView.setText(
                    model.getName() + " :- " + model.getSize() );
            
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
                R.layout.fragment_files, container, false));

        list = (ListView)getRootView().findViewById(R.id.list);

        setHtmlText(R.id.content, R.string.file_storage_content);

        installButtonClickHandler();
        return getRootView();
    }

    private void installButtonClickHandler() {
    	System.out.print("Install Button");
        final Button button = (Button) getRootView().findViewById(R.id.getFiles);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getFiles();
            }
        });
    }
    
   
}
