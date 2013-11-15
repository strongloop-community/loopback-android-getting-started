package com.strongloop.android.loopback.guide.util;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.TextView;

public class HtmlFragment extends Fragment {

	private ViewGroup rootView;

	protected ViewGroup getRootView() { return rootView; }
	protected void setRootView(ViewGroup value) { rootView = value; }
	
	public HtmlFragment() {
		super();
	}

	protected void setHtmlText(int textViewId, int stringResourceId) {
	    TextView text = (TextView)getRootView().findViewById(textViewId);
	    String htmlContent = getString(stringResourceId);
	    text.setText(Html.fromHtml(htmlContent));
	}

}