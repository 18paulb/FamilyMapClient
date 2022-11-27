package cs240.client;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import Model.Event;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback{

    private GoogleMap map;
/*
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.

        @Override
        public void onMapReady(GoogleMap googleMap) {

            DataCache cache = DataCache.getInstance();

            for (Event event : cache.getEvents()) {
                LatLng tmp = new LatLng(event.getLatitude(), event.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(tmp).title("Marker in " + event.getCity()));
            }

            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };
*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_map, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setOnMapLoadedCallback(this);

        DataCache cache = DataCache.getInstance();

        for (Event event : cache.getEvents()) {
            LatLng tmp = new LatLng(event.getLatitude(), event.getLongitude());

            if (event.getEventType().equals("Birth")) {
                googleMap.addMarker(new MarkerOptions()
                        .position(tmp)
                        .title("Marker in " + event.getCity())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                );
            }

            if (event.getEventType().equals("Marriage")) {
                googleMap.addMarker(new MarkerOptions()
                        .position(tmp)
                        .title("Marker in " + event.getCity())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                );
            }

            if (event.getEventType().equals("Death")) {
                googleMap.addMarker(new MarkerOptions()
                        .position(tmp)
                        .title("Marker in " + event.getCity())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );
            }
        }


        //map.addPolyline()

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    /*
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
     */

    @Override
    public void onMapLoaded() {

    }

}