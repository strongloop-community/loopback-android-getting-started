package com.strongloop.android.loopback.example;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class WelcomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
            Bundle savedInstanceState) {
        
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.welcome,
                container, false);
        
        Button startButton = (Button)rootView.findViewById(R.id.start);
        startButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                getActivity().getActionBar().getTabAt(1).select();
            }
        });
        
        return rootView;
    }

} 