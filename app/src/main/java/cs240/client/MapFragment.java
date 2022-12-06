package cs240.client;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap map;

    private TextView firstName;
    private TextView lastName;
    private TextView eventType;
    private TextView eventLoc;
    private TextView eventYear;
    private RelativeLayout info;
    private ImageView image;

    private Intent intent = null;

    private Bundle data = new Bundle();

    private ArrayList<Polyline> lines = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<>();

    private Map<String, Float> colors = new HashMap<>();

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
        image = view.findViewById(R.id.image);

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
        Intent intent;
        switch(menu.getItemId()) {
            case R.id.settings:
                System.out.println("Settings Clicked");
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.search:
                System.out.println("Search Clicked");
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
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

        for (Event event : cache.getFilteredEvents()) {
            createMarker(map, event);
        }

        //Populates the Person Info on Event Activity
        if (requireActivity().getClass().equals(EventActivity.class)) {
            Intent intent = getActivity().getIntent();

            Event event = cache.getEvent(intent.getStringExtra("eventID"));
            Person person = cache.getPerson(intent.getStringExtra("associatedPersonID"));

            getData().putString("personID", person.getPersonID());
            if (person.getGender().equals("f")) {
                getData().putString("gender", "Female");
            }
            if (person.getGender().equals("m")) {
                getData().putString("gender", "Male");
            }

            LatLng tmp = new LatLng(event.getLatitude(), event.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(tmp));

            firstName.setText(String.format("%s", person.getFirstName()));
            lastName.setText(String.format("%s",  person.getLastName()));
            eventType.setText(String.format("%s: ", event.getEventType().toUpperCase()));;
            eventLoc.setText(String.format("%s %s", event.getCity(), event.getCountry()));
            eventYear.setText(String.valueOf(event.getYear()));

            if (person.getGender().equals("m")) {
                image.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                        .colorRes(R.color.teal_200)
                        .sizeDp(40)
                );
            }
            else {
                image.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                        .colorRes(R.color.purple_200)
                        .sizeDp(40)
                );
            }

            drawLinesOfPerson(person.getPersonID(), event);
        }
    }

    //TODO: Implement maternal, paternal
    @Override
    public void onResume() {
        super.onResume();

        System.out.println("Map is reloaded");
        //Filters Gender
        DataCache cache = DataCache.getInstance();

        //Sets all markers to invisible
        for (Marker marker : markers) {
            marker.setVisible(false);
        }

        if (map != null) {

            ArrayList<Event> events = new ArrayList<>();

            events.addAll(cache.getFilteredEvents());

            //Makes events that are filtered be shown on map
            for (Event event : events) {
                for (Marker marker : markers) {
                    Event tag = (Event) marker.getTag();
                    if (tag.getEventID().equals(event.getEventID())) {
                        marker.setVisible(true);
                        break;
                    }
                    else {
                        continue;
                    }
                }
            }

        }

        //Removes Polylines on load
        for (Polyline line : lines) {
            line.remove();
        }

        //Removes Person/Event Info from Bottom
        firstName.setText(String.format("%s", "Click on Marker to see event details"));
        lastName.setText(String.format("%s",  ""));
        eventType.setText(String.format("%s", ""));;
        eventLoc.setText(String.format("%s", ""));
        eventYear.setText("");
        image.setImageDrawable(null);
    }

    @Override
    public void onMapLoaded() {

    }

    //FIXME: Look into COMPLETED ASTEROIDS
    @Override
    public boolean onMarkerClick(Marker marker) {

        //This populates the info section
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

        if (person.getGender().equals("m")) {
            image.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                    .colorRes(R.color.teal_200)
                    .sizeDp(40)
            );
        }
        else {
            image.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                    .colorRes(R.color.purple_200)
                    .sizeDp(40)
            );
        }

        for (Polyline line : lines) {
            line.remove();
        }

        //Draws Lines
        drawLinesOfPerson(person.getPersonID(), event);

        return false;
    }

    private Bundle getData() {
        return data;
    }

    public void linesThroughAncestors(Event startEvent, float width, DataCache cache) {
        Person person = cache.getPerson(startEvent.getPersonID());

        if (person.getMotherID() != null) {
            Person mother = cache.getPerson(person.getMotherID());

            ArrayList<Event> motherEvents = cache.getEventsOfPerson(mother.getPersonID());

            for (Marker marker : markers) {
                Event tag = (Event) marker.getTag();
                if (tag.getEventID().equals(motherEvents.get(0).getEventID()) && marker.isVisible()) {
                    drawLine(startEvent, motherEvents.get(0), Color.GRAY, width);
                }
            }

            linesThroughAncestors(motherEvents.get(0), width-4, cache);

        }
        if (person.getFatherID() != null) {
            Person father = cache.getPerson(person.getFatherID());

            ArrayList<Event> fatherEvents = cache.getEventsOfPerson(father.getPersonID());

            for (Marker marker : markers) {
                Event tag = (Event) marker.getTag();
                if (tag.getEventID().equals(fatherEvents.get(0).getEventID()) && marker.isVisible()) {
                    drawLine(startEvent, fatherEvents.get(0), Color.GRAY, width);
                }
            }

            linesThroughAncestors(fatherEvents.get(0), width-4, cache);
        }

    }

    //Later for line drawing
     public void drawLine(Event start, Event stop, int hue, float width) {
        LatLng startPoint = new LatLng(start.getLatitude(), start.getLongitude());
        LatLng endPoint = new LatLng(stop.getLatitude(), stop.getLongitude());

        PolylineOptions options = new PolylineOptions()
            .add(startPoint)
            .add(endPoint)
            .color(hue)
            .width(width);

        Polyline line = map.addPolyline(options);
        lines.add(line);
     }

    public void createMarker(GoogleMap map, Event event) {
        LatLng tmp = new LatLng(event.getLatitude(), event.getLongitude());

        String eventType = event.getEventType().toLowerCase();

        float random = (float) (0 + Math.random() * (360));

        if (colors.containsKey(eventType)) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(tmp)
                    .title("Marker in " + event.getCity())
                    .icon(BitmapDescriptorFactory.defaultMarker(colors.get(eventType)))
            );
            marker.setTag(event);
            markers.add(marker);
        } else {
            colors.put(eventType, random);
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(tmp)
                    .title("Marker in " + event.getCity())
                    .icon(BitmapDescriptorFactory.defaultMarker(colors.get(eventType)))
            );
            marker.setTag(event);
            markers.add(marker);
        }
    }

    public void drawLinesOfPerson(String personID, Event event) {
        DataCache cache = DataCache.getInstance();
        Person person = cache.getPerson(personID);

        //Draw Spouse Line - GOOD
        if (person.getSpouseID() != null && cache.getSettings().get("SpouseLines")) {
            ArrayList<Event> spouseEvents = cache.getEventsOfPerson(person.getSpouseID());
            Person spouse = cache.getPerson(person.getSpouseID());

            if (spouse.getGender().equals("m") && cache.getSettings().get("MaleEvents")) {
                drawLine(event, spouseEvents.get(0), Color.RED, 16.0F);
            }
            if (spouse.getGender().equals("f") && cache.getSettings().get("FemaleEvents")) {
                drawLine(event, spouseEvents.get(0), Color.RED, 16.0F);
            }
        }

        //Draws lines between all life events
        if (cache.getSettings().get("LifeStory")) {
            ArrayList<Event> lifeEvents = cache.getEventsOfPerson(person.getPersonID());
            Event currEvent = event;
            for (Event tmp : lifeEvents) {
                if (tmp.getEventID().equals(event.getEventID())) {
                    currEvent = tmp;
                    continue;
                }
                drawLine(currEvent, tmp, Color.BLUE, 16.0F);
                currEvent = tmp;
            }
        }

        //Draw Lines to All the Ancestors with lines getting progressively smaller after each generation
        if (cache.getSettings().get("FamilyLines")) {
            linesThroughAncestors(event, 16.0F, cache);
        }
    }

}