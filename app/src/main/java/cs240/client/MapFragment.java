package cs240.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.io.Serializable;

import Model.Event;
import Model.Person;
import ViewModels.MainActivityViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap map;

    private TextView firstName;
    private TextView lastName;
    private TextView eventType;
    private TextView eventLoc;
    private TextView eventYear;
    private RelativeLayout info;

    private Intent intent = null;

    private Bundle data = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        eventType = view.findViewById(R.id.eventType);
        eventLoc = view.findViewById(R.id.eventLocation);
        eventYear = view.findViewById(R.id.eventYear);
        info = view.findViewById(R.id.info);


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().length() != 0 && lastName.getText().length() != 0 && eventType.getText().length() != 0) {
                    intent = new Intent(getActivity(), PersonActivity.class);

                    String personID = getData().getString("personID");
                    intent.putExtra("personID", personID);

                    intent.putExtra("firstName", firstName.getText());
                    intent.putExtra("lastName", lastName.getText());
                    intent.putExtra("gender", getData().getString("gender"));
                    startActivity(intent);
                }
            }
        });

        Iconify.with(new FontAwesomeModule());

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (requireActivity().getClass().equals(EventActivity.class)) {
            return;
        }

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        searchItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.white)
                .actionBarSize()
        );

        MenuItem settingsItem = menu.findItem(R.id.settings);

        settingsItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_cog)
                .colorRes(R.color.white)
                .actionBarSize()
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.settings:
                System.out.println("Settings Clicked");
                return true;
            case R.id.search:
                System.out.println("Search Clicked");
                return true;
            default: return super.onOptionsItemSelected(menu);
        }
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

        if (requireActivity().getClass().equals(EventActivity.class)) {
            Intent intent = getActivity().getIntent();

            Event event = cache.getEvent(intent.getStringExtra("eventID"));
            Person person = cache.getPerson(intent.getStringExtra("associatedPersonID"));

            LatLng tmp = new LatLng(event.getLatitude(), event.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(tmp));

            firstName.setText(String.format("%s", person.getFirstName()));
            lastName.setText(String.format("%s",  person.getLastName()));
            eventType.setText(String.format("%s: ", event.getEventType().toUpperCase()));;
            eventLoc.setText(String.format("%s %s", event.getCity(), event.getCountry()));
            eventYear.setText(String.valueOf(event.getYear()));
        }


        //map.addPolyline()

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onMapLoaded() {

    }

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

        getData().putString("personID", event.getPersonID());

        if (person.getGender().equals("f")) {
            getData().putString("gender", "Female");
        }
        if (person.getGender().equals("m")) {
            getData().putString("gender", "Male");
        }

        firstName.setText(String.format("%s", person.getFirstName()));
        lastName.setText(String.format("%s",  person.getLastName()));
        eventType.setText(String.format("%s: ", event.getEventType().toUpperCase()));;
        eventLoc.setText(String.format("%s %s", event.getCity(), event.getCountry()));
        eventYear.setText(String.valueOf(event.getYear()));

        return false;
    }

    private Bundle getData() {
        return data;
    }


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