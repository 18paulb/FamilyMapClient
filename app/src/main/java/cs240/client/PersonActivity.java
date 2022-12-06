package cs240.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {

    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private ExpandableListView list;

    private RelativeLayout item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        gender = findViewById(R.id.personGender);
        list = findViewById(R.id.expandableList);

        Intent intent = getIntent();

        if (intent != null) {
            firstName.setText(intent.getStringExtra("firstName"));
            lastName.setText(intent.getStringExtra("lastName"));
            gender.setText(intent.getStringExtra("gender"));
        }

        //Sets Adaptor
        DataCache cache = DataCache.getInstance();
        //ArrayList events = cache.getEventsOfPerson(intent.getStringExtra("personID"));
        ArrayList people = cache.getFamilyOfPerson(intent.getStringExtra("personID"));

        ArrayList events = cache.getFilteredEvents();
        ArrayList finalEvents = new ArrayList();

        for (int i = 0; i < events.size(); ++i) {
            Event event = (Event) events.get(i);
            if (event.getPersonID().equals(intent.getStringExtra("personID"))) {
                finalEvents.add(event);
            }
        }




        list.setAdapter(new ExpandableListAdapter(finalEvents, people));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return true;
    }


    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENTS_POSITION = 0;
        private static final int PEOPLE_POSITION = 1;

        private ArrayList<Event> events;
        private ArrayList<Person> people;

        ExpandableListAdapter(ArrayList<Event> events, ArrayList<Person> people) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENTS_POSITION:
                    return events.size();
                case PEOPLE_POSITION:
                    return people.size();
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position");
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENTS_POSITION:
                    return getString(R.string.lifeEvents);
                case PEOPLE_POSITION:
                    return getString(R.string.family);
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch(groupPosition) {
                case EVENTS_POSITION:
                    return events.get(childPosition);
                case PEOPLE_POSITION:
                    return people.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position");
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.title);

            switch (groupPosition) {
                case EVENTS_POSITION:
                    titleView.setText(R.string.lifeEvents);
                    break;
                case PEOPLE_POSITION:
                    titleView.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position");
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView = null;

            switch (groupPosition) {
                case EVENTS_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.life_event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PEOPLE_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.family_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position");
            }

            return itemView;
        }

        private void initializeEventView(View eventView, final int childPosition) {
            Event event = events.get(childPosition);

            TextView eventType = eventView.findViewById(R.id.eventType);
            eventType.setText(String.format("%s: ", event.getEventType()));

            TextView location = eventView.findViewById(R.id.location);
            location.setText(String.format("%s %s (%d)",event.getCity(), event.getCountry(), event.getYear()));

            TextView name = eventView.findViewById(R.id.name);
            name.setText(String.format("%s %s", firstName.getText(), lastName.getText()));

            ImageView image = eventView.findViewById(R.id.image);

            image.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_map_marker)
                    .colorRes(R.color.black)
                    .sizeDp(40)
            );


            eventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);

                    intent.putExtra("eventID", event.getEventID());
                    intent.putExtra("associatedPersonID", event.getPersonID());

                    startActivity(intent);

                }
            });
        }

        private void initializePersonView(View personView, final int personPosition) {
            Person person = people.get(personPosition);

            TextView name = personView.findViewById(R.id.name);
            name.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));

            TextView relation = personView.findViewById(R.id.relationship);

            DataCache cache = DataCache.getInstance();
            String personID = getIntent().getStringExtra("personID");
            relation.setText(cache.getRelationToPerson(personID, people.get(personPosition).getPersonID()));

            ImageView image = personView.findViewById(R.id.image);

            if (person.getGender().equals("m")) {
                image.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_male)
                        .colorRes(R.color.teal_200)
                        .sizeDp(40)
                );
            }
            else {
                image.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_female)
                        .colorRes(R.color.purple_200)
                        .sizeDp(40)
                );
            }

            personView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);

                    intent.putExtra("personID", person.getPersonID());
                    intent.putExtra("firstName", person.getFirstName());
                    intent.putExtra("lastName", person.getLastName());

                    if (person.getGender().equals("f")) {
                        intent.putExtra("gender", "Female");
                    }
                    if (person.getGender().equals("m")) {
                        intent.putExtra("gender", "Male");
                    }

                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}