package com.strongloop.android.loopback.guide.lessons;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Date;

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
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.util.HtmlFragment;
import com.strongloop.android.loopback.Container;
import com.strongloop.android.loopback.ContainerRepository;
import com.strongloop.android.loopback.File;

/**
 * Implementation for Lesson Two: Existing Data? No Problem.
 */
public class LessonFiveFragment extends HtmlFragment {
    public static class FileModel extends Model {
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

	public static final String ID = null;

	private void addFile() {
		GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();
        ContainerRepository containerRepo = adapter.createRepository(ContainerRepository.class);
        containerRepo.get("con1", new ObjectCallback<Container>() {
			@Override
			public void onSuccess(Container container) {
				//Tried uploading file from local emulator FileSystem
				//java.io.File localFile = new java.io.File("/data/chandrika/testfile");
				//container.upload(localFile, new ObjectCallback<File>() {
				
				//Uploading in-memory content
				String fileName = "hello.txt";
				byte[] content = null;
				try {
					content = "Hello world".getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String contentType = "text/plain";
				 
				// same as container.getFileRepository().upload(fileName,...);
				container.upload(fileName, content, contentType, new ObjectCallback<File>() {
					@Override
					public void onSuccess(File object) {
						showResult("Saved!");
					}
					@Override
					public void onError(Throwable t) {
						Log.e(getTag(), "Cannot save File", t);
						showResult("Failed.");
					}
				});
			}
			@Override
			public void onError(Throwable t) {
				System.out.print("onError");
                Log.e(getTag(), "Fetching container failed", t);
                showResult("Failed.");
			}
        });
    }
    
	
	 private void removeFile() {
		 GuideApplication app = (GuideApplication)getActivity().getApplication();
	        RestAdapter adapter = app.getLoopBackAdapter();
	        ContainerRepository containerRepo = adapter.createRepository(ContainerRepository.class);
	        containerRepo.get("con1", new ObjectCallback<Container>() {
				@Override
				public void onSuccess(Container container) {
					 
					// same as container.getFileRepository().upload(fileName,...);
					container.getFile("e", new ObjectCallback<File>() {
						@Override
						public void onSuccess(File object) {
							object.delete(new VoidCallback(){

								@Override
								public void onSuccess() {
									showResult("Deleted");
								}

								@Override
								public void onError(Throwable t) {
									Log.e(getTag(), "Cannot save File", t);
									showResult("Failed.");
								}
								
							});
							 
						}
						@Override
						public void onError(Throwable t) {
							Log.e(getTag(), "Cannot save File", t);
							showResult("Failed.");
						}
					});
				}
				@Override
				public void onError(Throwable t) {
					System.out.print("onError");
	                Log.e(getTag(), "Fetching container failed", t);
	                showResult("Failed.");
				}
	        });
	    }
	 
	 private void getFile() {
		 GuideApplication app = (GuideApplication)getActivity().getApplication();
	        RestAdapter adapter = app.getLoopBackAdapter();
	        ContainerRepository containerRepo = adapter.createRepository(ContainerRepository.class);
	        containerRepo.get("con1", new ObjectCallback<Container>() {
				@Override
				public void onSuccess(Container container) {
					 
					// same as container.getFileRepository().upload(fileName,...);
					container.getFile("a", new ObjectCallback<File>() {
						@Override
						public void onSuccess(File object) {
//							java.io.File localFile = new java.io.File("path/to/file.txt");
							object.download(new File.DownloadCallback() {
							    @Override
							    public void onSuccess(byte[] content, String contentType) {
							    	System.out.println(content.toString());
							    	showResult(content.toString());
							    }
							    @Override
							    public void onError(Throwable error) {
							        // download failed
							    }
							});
						}
						@Override
						public void onError(Throwable t) {
							Log.e(getTag(), "Cannot save File", t);
							showResult("Failed.");
						}
					});
				}
				@Override
				public void onError(Throwable t) {
					System.out.print("onError");
	                Log.e(getTag(), "Fetching container failed", t);
	                showResult("Failed.");
				}
	        });
	    }
	
	
    private void getFiles() {
        // 1. Grab the shared RestAdapter instance.
        GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();
        ContainerRepository containerRepo = adapter.createRepository(ContainerRepository.class);
        containerRepo.get("con1", new ObjectCallback<Container>() {
			@Override
			public void onSuccess(Container container) {
				container.getAllFiles(new ListCallback<File>(){
					@Override
					public void onSuccess(List<File> fileList) {
						System.out.println("Files fetched successfully-"+ fileList);
		            	list.setAdapter(new FileListAdapter(getActivity(), fileList));
					}
					@Override
					public void onError(Throwable t) {
						System.out.print("onError");
		                Log.e(getTag(), "Fetching container failed", t);
		                showResult("Failed.");
					}
				});
			}
			@Override
			public void onError(Throwable t) {
				System.out.print("onError");
                Log.e(getTag(), "Fetching container failed", t);
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
    private static class FileListAdapter extends ArrayAdapter<File> {
        public FileListAdapter(Context context, List<File> list) {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	System.out.print("getView");
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, null);
            }
            File model = getItem(position);
            if (model == null) return convertView;
            System.out.println(android.R.id.text1);
            TextView textView = (TextView)convertView.findViewById(
                    android.R.id.text1);
            textView.setText(model.getName());
            
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
    	final Button button = (Button) getRootView().findViewById(R.id.addFile);
    	final Button button1 = (Button) getRootView().findViewById(R.id.getFiles);
    	final Button button2 = (Button) getRootView().findViewById(R.id.getFile);
    	button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	removeFile();
            }
        });
    	button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	addFile();
            }
        });
    	button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getFiles();
            }
        });
    }
}
