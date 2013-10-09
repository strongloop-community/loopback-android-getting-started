package com.strongloop.android.loopback.example;

import java.util.List;

import com.google.common.collect.ImmutableMap;
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
        
        initializeServerWithData();
        
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
    
    public static class Car extends Model {
        private String name;
        private int milage;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getMilage() {
            return milage;
        }
        
        public void setMilage(int milage) {
            this.milage = milage;
        }
    }
    
    public static class CarPrototype extends ModelPrototype<Car> {
        
        public CarPrototype() {
            super("cars", Car.class);
        }
    }
    
    private ModelAdapter<Car> adapter;
    private CarPrototype prototype;
    
    public ModelAdapter<Car> getAdapter() {
        if (adapter == null) {
            // NOTE: "10.0.2.2" is the "localhost" of the Android emulator's 
            // host computer.
            adapter = new ModelAdapter<Car>(getActivity(), 
                    "http://10.0.2.2:3000");
        }
        return adapter;
    }
    
    private CarPrototype getPrototype() {
        if (prototype == null) {
            prototype = getAdapter().createPrototype(CarPrototype.class);
        }
        return prototype;
    }
    
    private void refresh() {
        // Equivalent http JSON endpoint request : http://localhost:3000/cars
        CarPrototype prototype = getPrototype();
        prototype.findAll(new ModelPrototype.FindAllCallback<Car>() {

            @Override
            public void onSuccess(List<Car> cars) {
                list.setAdapter(new ListAdapter(getActivity(), cars));
            }

            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }
        });
    }
    
    private void create() {
        CarPrototype prototype = getPrototype();
        Car model = prototype.createModel(null);
        model.setName("Telsa Model S");
        model.setMilage(9);
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
        CarPrototype prototype = getPrototype();
        prototype.findById(2, new ModelPrototype.FindCallback<Car>() {

            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }

            @Override
            public void onSuccess(Car model) {
                if (model == null) {
                    MainActivity.showGuideMessage(getActivity(), 
                            R.string.message_failure_find);
                }
                else {
                    model.setMilage(7777);
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
        CarPrototype prototype = getPrototype();
        prototype.findById(2, new ModelPrototype.FindCallback<Car>() {

            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }

            @Override
            public void onSuccess(Car model) {
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
    
    private void initializeServerWithData() {
        final CarPrototype prototype = getPrototype();
        prototype.findAll(new ModelPrototype.FindAllCallback<Car>() {

            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }
            
            @Override
            public void onSuccess(List<Car> cars) {
                if (cars.isEmpty()) {
                    Model.Callback saveCallback = new Model.Callback() {
                        
                        @Override
                        public void onSuccess() { }
                        
                        @Override
                        public void onError(Throwable t) { }
                        
                    };
                    
                    prototype.createModel(ImmutableMap.of(
                            "name", "Toyota FJ",
                            "milage", 200))
                            .save(saveCallback);
                    prototype.createModel(ImmutableMap.of(
                            "name", "VW Bug", 
                            "milage", 300))
                            .save(saveCallback);
                    prototype.createModel(ImmutableMap.of(
                            "name", "Honda Accord",
                            "milage", 400))
                            .save(saveCallback);
                }
            }
        });
    }
    
    private static class ListAdapter extends ArrayAdapter<Car> {
        
        public ListAdapter(Context context, List<Car> list) {
            super(context, 0, list);
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, null);
            }
            
            Car model = getItem(position);
            if (model != null) {
                TextView textView = (TextView)convertView.findViewById(
                        android.R.id.text1);
                textView.setText(model.getName() + " - " + model.getMilage());
            }
            return convertView;
        }
    }
}
