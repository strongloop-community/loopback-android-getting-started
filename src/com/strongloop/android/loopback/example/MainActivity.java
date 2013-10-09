package com.strongloop.android.loopback.example;

import org.apache.http.conn.HttpHostConnectException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab()
                .setTag("0")
                .setText(R.string.welcome)
                .setTabListener(new TabListener(new WelcomeFragment())));
        actionBar.addTab(actionBar.newTab()
                .setTag("1")
                .setIcon(R.drawable.num_one)
                .setTabListener(new TabListener(new Fragment1())));
        actionBar.addTab(actionBar.newTab()
                .setTag("2")
                .setIcon(R.drawable.num_two)
                .setTabListener(new TabListener(new Fragment2())));
        actionBar.addTab(actionBar.newTab()
                .setTag("3")
                .setIcon(R.drawable.num_three)
                .setTabListener(new TabListener(new Fragment3())));
        actionBar.addTab(actionBar.newTab()
                .setTag("4")
                .setIcon(R.drawable.num_four)
                .setTabListener(new TabListener(new Fragment4())));
    }
    
    private static class TabListener implements ActionBar.TabListener {
        public Fragment fragment;

        public TabListener(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // Do nothing
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.replace(android.R.id.content, fragment);
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }
    }
    
    public static void showGuideMessage(Context context, Throwable t) {
        if (t instanceof HttpHostConnectException) {
            showGuideMessage(context, R.string.message_error_connection);
        }
        else {
            Log.e("LoopBackExample", "Error", t);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage(t.getLocalizedMessage());
            alert.setPositiveButton("OK", null);
            alert.show();
        }
    }
    
    public static void showGuideMessage(Context context, int stringId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(stringId);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
