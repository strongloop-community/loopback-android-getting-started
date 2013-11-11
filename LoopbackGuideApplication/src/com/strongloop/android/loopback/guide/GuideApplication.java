package com.strongloop.android.loopback.guide;

import android.app.Application;

import com.strongloop.android.loopback.RestAdapter;

public class GuideApplication extends Application {
	RestAdapter adapter;

	public RestAdapter getLoopBackAdapter() {
		if (adapter == null) {
			adapter = new RestAdapter(
					getApplicationContext(), "http://10.0.2.2:3000/");
		}
		return adapter;
	}
}
