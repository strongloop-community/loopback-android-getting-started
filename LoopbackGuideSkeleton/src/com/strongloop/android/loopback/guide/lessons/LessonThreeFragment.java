package com.strongloop.android.loopback.guide.lessons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.util.HtmlFragment;
import com.strongloop.android.remoting.adapters.Adapter;

/**
 * Implementation for Lesson Three: Don't Outgrow, Outbuild.
 */
public class LessonThreeFragment extends HtmlFragment {
    final LatLng MY_POSITION = new LatLng(37.56572191237293, -122.32357978820802);

    /**
     * Loads all Locations from the server, passing them to our MapView
     * to be displayed as pins.
     */
    private void sendRequest() {
        /**
         * Insert implementation here.
         */
    }

    private void showResult(final String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display a list of locations in the GUI.
     */
    private void displayLocations(final JSONArray response) {
        // Convert JSON response to MarkerOptions
        final List<MarkerOptions> locations = convertLocationsToMarkerOptions(response);

        if (canDisplayMap) {
            // If we have a map, then display these markers as pins
            final GoogleMap map = mapFragment.getMap();

            // remove previous markers
            map.clear();

            // add markers for locations returned by the server
            for (final MarkerOptions loc: locations) {
                map.addMarker(loc);
            }

            // Move the camera to our location
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(MY_POSITION, 4));
        } else {
            // Otherwise show a list of locations
            final ListView list = listFragment.getList();
            list.setAdapter(new LocationListAdapter(
                    getActivity(), locations, MY_POSITION));
        }
    }

    private List<MarkerOptions> convertLocationsToMarkerOptions(
            final JSONArray locations) {

        final List<MarkerOptions> result =
                new ArrayList<MarkerOptions>(locations.length());

        for (int ix = 0; ix < locations.length(); ix++) {
            try {
                final JSONObject loc = locations.getJSONObject(ix);
                result.add(createMarkerOptionsForLocation(loc));
            } catch (final JSONException e) {
                Log.w("LessonThreeFragment", "Skipping invalid location object.", e);
            }
        }

        return result;
    }

    private MarkerOptions createMarkerOptionsForLocation(final JSONObject location)
            throws JSONException {
        final JSONObject posJson = location.getJSONObject("geo");
        final LatLng geoPos = new LatLng(
                posJson.getDouble("lat"),
                posJson.getDouble("lng"));

        return new MarkerOptions()
        .position(geoPos)
        .title(location.getString("name"))
        .snippet(location.getString("city"));
    }

    /**
     * Custom ListAdapter for displaying locations.
     */
    private static class LocationListAdapter extends ArrayAdapter<MarkerOptions> {
        private final LatLng myPosition;

        public LocationListAdapter(final Context context, final List<MarkerOptions> list, final LatLng myPosition) {
            super(context, 0, list);
            this.myPosition = myPosition;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_2, null);
            }

            final MarkerOptions model = getItem(position);
            if (model == null) return convertView;

            final TextView textView1 = (TextView)convertView.findViewById(
                    android.R.id.text1);
            textView1.setText(model.getTitle());

            final TextView textView2 = (TextView)convertView.findViewById(
                    android.R.id.text2);
            textView2.setText(
                    model.getSnippet() +
                    " (" + getDistanceString(model.getPosition()) + ")");

            return convertView;
        }

        @SuppressLint("DefaultLocale")
        private String getDistanceString(final LatLng position) {
            final float[] distanceResult = new float[1];
            Location.distanceBetween(
                    myPosition.latitude, myPosition.longitude,
                    position.latitude, position.longitude,
                    distanceResult);
            final float distance = distanceResult[0];

            return distance < 1000
                    ? String.format("%dm", Math.round(distance))
                            : String.format("%.1fkm", distance/1000);
        }
    }

    //
    // GUI glue
    //

    private boolean canDisplayMap;
    private SupportMapFragment mapFragment;
    private LocationListFragment listFragment;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
                R.layout.fragment_lesson_three, container, false));

        setHtmlText(R.id.content, R.string.lessonThree_content);

        installButtonClickHandler();

        setUpMapIfNeeded();

        return getRootView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void installButtonClickHandler() {
        final Button button = (Button) getRootView().findViewById(R.id.sendRequest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                sendRequest();
            }
        });
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * may not be called again so we should call this method in {@link #onResume()}
     * to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        canDisplayMap = detectGoogleMapsSupport();

        if (!canDisplayMap) {
            setupAlternateView();
            return;
        }

        setupMapView();
    }

    private void setupMapView() {
        // Do a null check to confirm that we have not already
        // instantiated the fragment.
        if (mapFragment == null)
            mapFragment = new SupportMapFragment();
        replaceMapFrame(mapFragment);
    }

    private void setupAlternateView() {
        // Do a null check to confirm that we have not already
        // instantiated the fragment.
        if (listFragment == null)
            listFragment = new LocationListFragment();
        replaceMapFrame(listFragment);
    }

    private void replaceMapFrame(final Fragment newFragment) {
        final FragmentManager fragmentManager = getChildFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.map_frame, newFragment);
        transaction.commit();
    }


    boolean detectGoogleMapsSupport() {
        // 1. Check for OpenGL ES 2.0
        final ActivityManager activityManager =
                (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        if (configurationInfo.reqGlEsVersion < 0x20000) {
            Log.w("LessonThreeFragment", "Device does not support OpenGL ES 2.0");
            return false;
        }

        // 2. Check for Google Play Services availability
        final int playServicesStatus =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (playServicesStatus != ConnectionResult.SUCCESS
                && Build.HARDWARE.contains("golfdish")) {
            // It's not possible to install or update Google Play on an emulator
            Log.w("LessonThreeFragment",
                    "Detected an emulator with missing or outdated Play Services.");
            return false;
        }

        Log.i("LessonThreeFragment", "Locations will be displayed in Google Maps.");
        return true;
    }

    public static class LocationListFragment extends Fragment {
        ViewGroup rootView;

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                final Bundle savedInstanceState) {

            rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_location_list, container, false);
            return rootView;
        }

        public ListView getList() {
            return (ListView) rootView.findViewById(R.id.list);
        }
    }
}
