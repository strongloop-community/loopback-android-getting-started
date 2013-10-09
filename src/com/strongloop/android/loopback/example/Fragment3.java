package com.strongloop.android.loopback.example;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelAdapter;
import com.strongloop.android.loopback.ModelPrototype;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContractItem;

public class Fragment3 extends Fragment {

    private TextView result1;
    private TextView result2;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
            Bundle savedInstanceState) {
        
    
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment3, 
                container, false);
        
        result1 = (TextView)rootView.findViewById(
                R.id.custom_method_1_response);
        result2 = (TextView)rootView.findViewById(
                R.id.custom_method_2_response);
        
        Button button1 = (Button)rootView.findViewById(R.id.custom_method_1);
        button1.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                findProductLowestInventory();
            }
        });
        
        Button button2 = (Button)rootView.findViewById(R.id.custom_method_2);
        button2.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                findProductLowestPrice();
            }
        });
        
        return rootView;
    }
    
    private ModelAdapter<Model> adapter;
    private ModelPrototype<Model> prototype;
    
    public ModelAdapter<Model> getAdapter() {
        if (adapter == null) {
            // NOTE: "10.0.2.2" is the "localhost" of the Android emulator's 
            // host computer.
            adapter = new ModelAdapter<Model>(getActivity(), 
                    "http://10.0.2.2:3000");
            adapter.getContract().addItem(
                    new RestContractItem("/products", "GET"), 
                    "products.filter");
        }
        return adapter;
    }
    
    private ModelPrototype<Model> getPrototype() {
        if (prototype == null) {
            prototype = getAdapter().createPrototype("products");
        }
        return prototype;
    }
    
    private void findProductLowestInventory() {
        ModelPrototype<Model> prototype = getPrototype();
        prototype.invokeStaticMethod("filter", 
                ImmutableMap.of("filter[order]", "inventory ASC", 
                        "filter[limit]", 1), 
                new Adapter.JsonArrayCallback() {
            
            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }

            @Override
            public void onSuccess(JSONArray response) {
                JSONObject object = response.optJSONObject(0);
                if (object == null) {
                    result1.setText("None");
                }
                else {
                    result1.setText(
                        object.opt("name") + ": " + object.opt("inventory"));
                }
            }
        });
    }

    private void findProductLowestPrice() {
        ModelPrototype<Model> prototype = getPrototype();
        prototype.invokeStaticMethod("filter", 
                ImmutableMap.of("filter[order]", "price ASC", 
                        "filter[limit]", 1), 
                new Adapter.JsonArrayCallback() {
            
            @Override
            public void onError(Throwable t) {
                MainActivity.showGuideMessage(getActivity(), t);
            }

            @Override
            public void onSuccess(JSONArray response) {
                JSONObject object = response.optJSONObject(0);
                if (object == null) {
                    result2.setText("None");
                }
                else {
                    result2.setText(
                        object.opt("name") + ": $" + object.opt("price"));
                }
            }
        });
    }
}
