package com.strongloop.android.loopback.guide.lessons;

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
import android.widget.Toast;

import com.strongloop.android.loopback.File;
import com.strongloop.android.loopback.FileRepository;
import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.util.HtmlFragment;

public class LessonFileFragment extends HtmlFragment {
    
    // For testing the file model upload and download. The file is expected to be
    // in the Android Pictures directory. 
    private String lbTestUploadFile = "lbTestUploadFile.jpg";
    private String lbTestDownloadFile = "lbTestDownloadFile.jpg";
   
    /**
     * Saves the desired Ammo model to the server with all values pulled from the UI.
     */
    private void sendUploadRequest() {
        // 1. Grab the shared RestAdapter instance.
        GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();

        FileRepository repository = adapter.createRepository(FileRepository.class, "containers");
        
        // 3. From that prototype, create a new AmmoModel. We pass in an empty dictionary to defer
        //    setting any values.
        File fileModel = repository.createModel(null);

        // 4. Pull model values from the UI.
        fileModel.setName("facekick.gif");
        fileModel.setContainer("container1");
        fileModel.setUrl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() );

        // 5. Save!
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

    private void sendDownloadRequest() {
        GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();

        FileRepository repository = adapter.createRepository(FileRepository.class, "containers");

        java.io.File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        
        repository.download(storageDir.toString(), "container1", "facekick.jpg", 
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
        
        // 3. From that prototype, create a new AmmoModel. We pass in an empty dictionary to defer
        //    setting any values.
        //File fileModel = repository.createModel(null);

        // 4. Pull model values from the UI.
        //fileModel.setName("namedontmattertest");

        // Test download code Start
        /***
        AsyncHttpClient testClient = new AsyncHttpClient();
        testClient.get("http://192.168.1.4:3000/containers/container1/download/birthdaywolves.jpg", new BinaryHttpResponseHandler() {
            
            @Override
            public void onStart() {
                Log.e("blah", "start");
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] imageData, Throwable e) {
                Log.e("blah", "no!");
            }

            @Override
            public String[] getAllowedContentTypes() {
                // Allowing all data for debug purposes
                return new String[]{".*"};
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                    byte[] binaryData) {
                Log.e("blah", "here");
                // TODO Auto-generated method stub
                saveFile(binaryData, lbTestDownloadFile);
                
            }
            
            
            //public void getAllowedContentTypes {
            //    Log.e("blah", "what?");
            //}
        });            
        ***/
        // Test download code End
        
        // 5. Save!
        /**
        fileModel.save(new Model.Callback() {

            @Override
            public void onSuccess() {
                showResult("Saved!");
            }

            @Override
            public void onError(Throwable t) {
                Log.e(getTag(), "Cannot save File model.", t);
                showResult("Failed.");
            }
        });
        **/
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

        java.io.File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        java.io.File uploadFile = new java.io.File( storageDir + "/" + lbTestUploadFile);
        
        installButtonHandlers();
        boolean exists = uploadFile.exists();
        
        //String resourcePath = getActivity().getResources().
        /***
        try {
            files = assetManager.list("files");
            File assetFile = new File("assets/"  + files[0]);
            
            exists = assetFile.exists();
            int t=3;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ***/
        
        /**
        String fileName = fileDir.toString() + "/ftf_dog01.jpg";
        File uploadFile = new File(fileName);
        boolean doesExist = uploadFile.exists();
        **/
        
        
        // Grab the shared RestAdapter instance.
        GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();
        
        // Create user repository based on model repository
        //userRepo = adapter.createRepository(UserRepository.class);
                
        return getRootView();
    }

    private void installButtonHandlers()
    {
        installUploadButtonClickHandler();
        installDownloadButtonClickHandler();
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
