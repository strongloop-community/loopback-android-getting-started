package com.strongloop.android.loopback.example;

import java.util.List;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelAdapter;
import com.strongloop.android.loopback.ModelPrototype;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment2 extends Fragment {
    
    private ListView list;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
            Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment2, 
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
    
    public static class Ammo extends Model {
        private String name;
        private int caliber;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getCaliber() {
            return caliber;
        }
        
        public void setCaliber(int caliber) {
            this.caliber = caliber;
        }
    }
    
    public static class AmmoPrototype extends ModelPrototype<Ammo> {
        
        public AmmoPrototype() {
            super("ammo", "ammo", Ammo.class);
        }
    }
    
    private ModelAdapter<Ammo> adapter;
    private AmmoPrototype prototype;
    
    public ModelAdapter<Ammo> getAdapter() {
        if (adapter == null) {
            // NOTE: "10.0.2.2" is the "localhost" of the Android emulator's 
            // host computer.
            adapter = new ModelAdapter<Ammo>(getActivity(),
                    "http://10.0.2.2:3000");
        }
        return adapter;
    }
    
    private AmmoPrototype getPrototype() {
        if (prototype == null) {
            prototype = getAdapter().createPrototype(AmmoPrototype.class);
        }
        return prototype;
    }
    
    private void refresh() {
        // Equivalent http JSON endpoint request : http://localhost:3000/ammo
        AmmoPrototype prototype = getPrototype();
        prototype.findAll(new ModelPrototype.FindAllCallback<Ammo>() {

            @Override
            public void onSuccess(List<Ammo> ammo) {
                list.setAdapter(new ListAdapter(getActivity(), ammo));
            }

            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }
        });
    }
    
    private void create() {
        AmmoPrototype prototype = getPrototype();
        Ammo model = prototype.createModel(null);
        model.setName("MP5 Milspec");
        model.setCaliber(9);
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
        AmmoPrototype prototype = getPrototype();
        prototype.findById(1, new ModelPrototype.FindCallback<Ammo>() {

            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }

            @Override
            public void onSuccess(Ammo model) {
                if (model == null) {
                    MainActivity.showGuideMessage(getActivity(), 
                            R.string.message_failure_find);
                }
                else {
                    model.setCaliber(22);
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
        AmmoPrototype prototype = getPrototype();
        prototype.findById(1, new ModelPrototype.FindCallback<Ammo>() {

            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }

            @Override
            public void onSuccess(Ammo model) {
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
    
    private static class ListAdapter extends ArrayAdapter<Ammo> {
        
        public ListAdapter(Context context, List<Ammo> list) {
            super(context, 0, list);
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, null);
            }
            
            Ammo model = getItem(position);
            if (model != null) {
                TextView textView = (TextView)convertView.findViewById(
                        android.R.id.text1);
                textView.setText(model.getName() + " - " + model.getCaliber());
            }
            return convertView;
        }
    }
}
