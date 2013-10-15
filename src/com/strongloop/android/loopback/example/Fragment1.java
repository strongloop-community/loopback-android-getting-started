package com.strongloop.android.loopback.example;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelAdapter;
import com.strongloop.android.loopback.ModelRepository;

public class Fragment1 extends Fragment {
    
    private ListView list;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment1, 
                container, false);
        
        list = (ListView)rootView.findViewById(R.id.list);
        
        Button refreshButton = (Button)rootView.findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        
        Button createButton = (Button)rootView.findViewById(R.id.create);
        createButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                create();
            }
        });
        
        Button updateButton = (Button)rootView.findViewById(R.id.update);
        updateButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                update();
            }
        });
        
        Button deleteButton = (Button)rootView.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        
        return rootView;
    }
    
    private ModelAdapter<Model> adapter;
    private ModelRepository<Model> repository;
    
    public ModelAdapter<Model> getAdapter() {
        if (adapter == null) {
            // NOTE: "10.0.2.2" is the "localhost" of the Android emulator's
            // host computer.
            adapter = new ModelAdapter<Model>(getActivity(), 
                    "http://10.0.2.2:3000");
        }
        return adapter;
    }
    
    private ModelRepository<Model> getRepository() {
        if (repository == null) {
            repository = getAdapter().createRepository("weapon");
        }
        return repository;
    }
    
    private void refresh() {
        // Equivalent http JSON endpoint request: http://localhost:3000/weapons
        ModelRepository<Model> repository = getRepository();
        repository.findAll(new ModelRepository.FindAllCallback<Model>() {

            @Override
            public void onSuccess(List<Model> models) {
                list.setAdapter(new ListAdapter(getActivity(), models));
            }

            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }
        });
    }
    
    private void create() {
        ModelRepository<Model> repository = getRepository();
        Model model = repository.createModel(
                ImmutableMap.of("name", "New Weapon", "effectiveRange", 99));
        model.save(new Model.Callback() {
            
            @Override
            public void onSuccess() {
                MainActivity.showGuideMessage(getActivity(), 
                        R.string.message_success_create);
            }
            
            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }
        });
    }
    
    private void update() {
        ModelRepository<Model> repository = getRepository();
        repository.findById(2, new ModelRepository.FindCallback<Model>() {

            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }

            @Override
            public void onSuccess(Model model) {
                if (model == null) {
                    MainActivity.showGuideMessage(getActivity(), 
                            R.string.message_failure_find);
                }
                else {
                    model.put("effectiveRange", 22);
                    model.save(new Model.Callback() {
                        
                        @Override
                        public void onSuccess() {
                            MainActivity.showGuideMessage(getActivity(), 
                                    R.string.message_success_update);
                        }
                        
                        @Override
                        public void onError(Throwable t) {
                            MainActivity.showGuideMessage(getActivity(), t);
                        }
                    });
                }
            }
        });
    }
    
    private void delete() {
        ModelRepository<Model> repository = getRepository();
        repository.findById(2, new ModelRepository.FindCallback<Model>() {

            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }

            @Override
            public void onSuccess(Model model) {
                if (model == null) {
                    MainActivity.showGuideMessage(getActivity(),
                            R.string.message_failure_find);
                }
                else {
                    model.destroy(new Model.Callback() {
                        
                        @Override
                        public void onSuccess() {
                            MainActivity.showGuideMessage(getActivity(), 
                                    R.string.message_success_delete);
                        }
                        
                        @Override
                        public void onError(Throwable t) {
                            MainActivity.showGuideMessage(getActivity(), t);
                        }
                    });
                }
            }
        });
    }
    
    private static class ListAdapter extends ArrayAdapter<Model> {
        public ListAdapter(Context context, List<Model> list) {
            super(context, 0, list);
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, null);
            }
            
            Model model = getItem(position);
            if (model != null) {
                TextView textView = (TextView)convertView.findViewById(
                        android.R.id.text1);
                textView.setText(
                        model.get("name") + " - " + model.get("effectiveRange"));
            }
            return convertView;
        }
    }
}
