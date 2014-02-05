package com.strongloop.android.loopback.guide.lessons;

import java.util.List;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.strongloop.android.loopback.Container;
import com.strongloop.android.loopback.ContainerRepository;
import com.strongloop.android.loopback.File;
import com.strongloop.android.loopback.FileRepository;
import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.util.HtmlFragment;

public class LessonFileFragment extends HtmlFragment {
    
    ContainerRepository containerRepo;
    FileRepository fileRepo;
    
    Container containerModel; // currently created container
    File fileModel; // currently created file
    
    
    private void sendCreateContainerRequest()
    {
        String containerName = getContainerName();
        containerModel = containerRepo.createContainer(containerName);
        containerModel.save( new Model.Callback() {

            @Override
            public void onSuccess() {
                showResult("Container created!");                
            }

            @Override
            public void onError(Throwable t) {
                showResult("Failed.");
            }            
        });
    }
    
    /**
     * Saves the desired Ammo model to the server with all values pulled from the UI.
     */
    private void sendUploadRequest() {
        
        // From that prototype, create a new File model. 
        File fileModel = fileRepo.createModel(null);

        // Set file properties
        fileModel.setName("facekick.gif");
        fileModel.setContainer("container1");
        fileModel.setUrl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() );

        // Upload!
        fileModel.upload(new Model.Callback() {

            @Override
            public void onSuccess() {
                showResult("Uploaded!");
            }

            @Override
            public void onError(Throwable t) {
                Log.e(getTag(), "Cannot save File model.", t);
                showResult("Failed.");
            }
        });
    }

    private void sendGetContainerRequest() {
        
        String containerName = getContainerName();        
        containerRepo.get(containerName, new ContainerRepository.ContainerCallback() {

            @Override
            public void onError(Throwable t) {
                showResult("Failed.");
            }

            @Override
            public void onSuccess(Container container) {
                containerModel = container;
                showResult("Container created!");                                
            }            
        });
    }
    
    private void sendGetFileRequest() {
  
        containerModel.getFile( "f1.txt", new FileRepository.FileCallback() {

            @Override
            public void onError(Throwable t) {
                showResult("Failed.");
            }

            @Override
            public void onSuccess(File file) {
                fileModel = file;
                showResult("File successfully retrieved!");
            }            
        });
    }
    
    
    private void sendDownloadRequest() {
        
        java.io.File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        
        fileRepo.download(storageDir.toString(), "container1", "facekick.jpg", 
                new FileRepository.FileCallback()  {

            @Override
            public void onSuccess(File file) {
                showResult("Downloaded!");
                
                // called after writing file, to see in file viewer
                new SingleMediaScanner(getActivity(), file.getUrl()); 
            }

            @Override
            public void onError(Throwable t) {
                Log.e(getTag(), "Cannot download File.", t);
                showResult("Failed.");
            }
            
        });        
    }
    
   private void sendDeleteContainerRequest() {
   
       if ( containerModel != null ) {
           containerModel.delete(new Model.Callback() {

            @Override
            public void onSuccess() {
                showResult("Deleted!");
            }

            @Override
            public void onError(Throwable t) {
                Log.e(getTag(), "Cannot delete Container.", t);
                showResult("Failed.");
            }
               
           });
       }
   }
   
   private void sendGetAllRequest() {
        
        containerRepo.getAll( new ContainerRepository.AllContainersCallback()  {

            @Override
            public void onSuccess(List<Container> containerList) {
                showResult("All Containers Retrieved!");                
            }

            @Override
            public void onError(Throwable t) {
                Log.e(getTag(), "Cannot get containers.", t);
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
                R.layout.fragment_lesson_file, container, false));

        setHtmlText(R.id.content, R.string.lessonFile_content);

        installButtonHandlers();
        
        // Grab the shared RestAdapter instance.
        GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();
        
        containerRepo = adapter.createRepository(ContainerRepository.class, "containers");
        fileRepo = adapter.createRepository(FileRepository.class, "containers");
                
        return getRootView();
    }

    private void installButtonHandlers()
    {
        installCreateButtonClickHandler();
        installGetContainerButtonClickHandler();
        installGetFileButtonClickHandler();
        installUploadButtonClickHandler();
        installDownloadButtonClickHandler();
        installDeleteContainerButtonClickHandler();
        installGetAllButtonClickHandler();
    }

    private void installCreateButtonClickHandler() {
        final Button button = (Button) getRootView().findViewById(R.id.createButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendCreateContainerRequest();
            }
        });
    }

    private void installGetContainerButtonClickHandler() {
        final Button button = (Button) getRootView().findViewById(R.id.getContainerButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendGetContainerRequest();
            }
        });
    }
    
    private void installGetFileButtonClickHandler() {
        final Button button = (Button) getRootView().findViewById(R.id.getFileButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendGetFileRequest();
            }
        });
    }
    
    private void installUploadButtonClickHandler() {
        final Button button = (Button) getRootView().findViewById(R.id.uploadButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendUploadRequest();
            }
        });
    }

    private void installDownloadButtonClickHandler() {
        final Button button = (Button) getRootView().findViewById(R.id.downloadButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendDownloadRequest();
            }
        });
    }

    private void installDeleteContainerButtonClickHandler() {
        final Button button = (Button) getRootView().findViewById(R.id.deleteContainerButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendDeleteContainerRequest();
            }
        });
    }
    
    private void installGetAllButtonClickHandler() {
        final Button button = (Button) getRootView().findViewById(R.id.getAllButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendGetAllRequest();
            }
        });
    }
    
    private String getContainerName() {
        final EditText widget = (EditText) getRootView().findViewById(R.id.editContainerName);
        return widget.getText().toString();        
    }
    

    private class SingleMediaScanner implements MediaScannerConnectionClient 
    { 
        private MediaScannerConnection mMs; 
        private String path; 
        SingleMediaScanner(Context context, String f) 
        { 
            path = f; 
            mMs = new MediaScannerConnection(context, this); 
            mMs.connect(); 
        } 
        @Override 
        public void onMediaScannerConnected() 
        { 
            mMs.scanFile(path, null); 
        } 
        @Override 
        public void onScanCompleted(String path, Uri uri) 
        { 
            mMs.disconnect(); 
        } 
    }    
}
