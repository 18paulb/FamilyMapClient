package cs240.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import Model.Event;
import Model.Person;
import ViewModels.MainActivityViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap map;

    private TextView personName;
    private TextView eventType;
    private TextView eventLoc;
    private TextView eventYear;

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

        //personName = view.findViewById(R.id.personName);
        //eventType = view.findViewById(R.id.eventType);
        //eventLoc = view.findViewById(R.id.eventLocation);
        //eventYear = view.findViewById(R.id.eventYear);


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setOnMapLoadedCallback(this);
        map.setOnMarkerClickListener(this);

        DataCache cache = DataCache.getInstance();

        for (Event event : cache.getEvents()) {
            LatLng tmp = new LatLng(event.getLatitude(), event.getLongitude());

            if (event.getEventType().equals("Birth")) {
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(tmp)
                        .title("Marker in " + event.getCity())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                );
                marker.setTag(event);
            }

            if (event.getEventType().equals("Marriage")) {
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(tmp)
                        .title("Marker in " + event.getCity())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                );
                marker.setTag(event);
            }

            if (event.getEventType().equals("Death")) {
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(tmp)
                        .title("Marker in " + event.getCity())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );
                marker.setTag(event);
            }
        }


        //map.addPolyline()

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //Intent intent = new Intent(MainActivity.class, PersonActivity.class);

        return false;
    }

    //FIXME: ALL this will be put into the Event Activity
    /*
    @Override
    public boolean onMarkerClick(Marker marker) {

        Event event = (Event) marker.getTag();

        String id = event.getPersonID();

        DataCache cache = DataCache.getInstance();

        Person person = new Person();

        for (Person tmp : cache.getPeople()) {
            if (tmp.getPersonID().equals(id)) {
                person = tmp;
            }
        }
        //personName.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));
        //eventType.setText(event.getEventType());;
        //eventLoc.setText(String.format("%s %s", event.getCity(), event.getCountry()));
        //eventYear.setText(String.valueOf(event.getYear()));

        return false;
    }

     */


    //Later for line drawing
    /*
     void drawAndRemoveLine(Event start, Event stop, float hue, float width) {
        LatLng start
        LatLng end

        PolylineOptions options = new PolyLineOptions()
            .add(startPoint)
            .add(endPoint)
            .color(hue)
            .width(width)

        Polyline line = map.addPolyLine(options)

        or

        line.remove()
     }
     */

}