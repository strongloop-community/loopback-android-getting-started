package com.strongloop.android.loopback.guide;

import android.app.Application;

import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.remoting.adapters.RestContractItem;

public class GuideApplication extends Application {
    RestAdapter adapter;

    public RestAdapter getLoopBackAdapter() {
        if (adapter == null) {
            // Instantiate the shared RestAdapter. In most circumstances,
            // you'll do this only once; putting that reference in a singleton
            // is recommended for the sake of simplicity.
            // However, some applications will need to talk to more than one
            // server - create as many Adapters as you need.
            adapter = new RestAdapter(
                    getApplicationContext(), "http://10.0.2.2:3000/api");

            // This boilerplate is required for Lesson Three.
            adapter.getContract().addItem(
                    new RestContractItem("locations/nearby", "GET"),
                    "location.nearby");
        }
        return adapter;
    }
}
