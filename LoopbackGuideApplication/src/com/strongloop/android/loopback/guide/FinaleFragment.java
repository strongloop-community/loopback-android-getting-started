package com.strongloop.android.loopback.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongloop.android.loopback.guide.util.HtmlFragment;

public class FinaleFragment extends HtmlFragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
        		R.layout.fragment_finale, container, false));

        setHtmlText(R.id.finale_content, R.string.finale_content);
        setHtmlText(R.id.finale_callToAction, R.string.finale_callToAction);
        
        return getRootView();
	}
}
