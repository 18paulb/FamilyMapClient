package cs240.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;

import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity {

    private final int PERSON_ITEM_VIEW_TYPE = 0;
    private final int EVENT_ITEM_VIEW_TYPE = 1;


    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        search = findViewById(R.id.searchBar);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                System.out.println(newText);

                DataCache cache = DataCache.getInstance();

                ArrayList<Event> events = cache.searchFilterEvents(newText.toLowerCase(), cache.getFilteredEvents());
                ArrayList<Person> people = cache.searchFilterPeople(newText.toLowerCase(), cache.getPeople());

                SearchAdaptor adaptor = new SearchAdaptor(events, people);
                recyclerView.setAdapter(adaptor);

                return false;
            }
        });
        Iconify.with(new FontAwesomeModule());
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

    private class SearchAdaptor extends RecyclerView.Adapter<SearchViewHolder> {

        ArrayList<Event> events;
        ArrayList<Person> people;

        SearchAdaptor(ArrayList<Event> events, ArrayList<Person> people) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;

            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.family_item, parent, false);
            }
            else {
                view = getLayoutInflater().inflate(R.layout.life_event_item, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < people.size()) {
                holder.bind(people.get(position));
            } else {
                holder.bind(events.get(position - people.size()));
            }

        }

        @Override
        public int getItemCount() {
            return events.size() + people.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //For Event
        private TextView name;
        private TextView location;
        private TextView eventType;
        private TextView year;
        private TextView relationship;

        private int viewType;
        private Event event;
        private Person person;

        DataCache cache = DataCache.getInstance();


        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                name = itemView.findViewById(R.id.name);
                relationship = itemView.findViewById(R.id.relationship);
            }
            else {
                name = itemView.findViewById(R.id.name);
                location = itemView.findViewById(R.id.location);
                eventType = itemView.findViewById(R.id.eventType);
            }
        }

        private void bind(Event event) {
            this.event = event;

            Person person = cache.getPerson(event.getPersonID());
            name.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));
            location.setText(String.format("%s %s (%s)", event.getCity(), event.getCountry(), Integer.toString(event.getYear())));
            eventType.setText(String.format("%s: ", event.getEventType()));

            ImageView image = itemView.findViewById(R.id.image);

            image.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_map_marker)
                    .colorRes(R.color.black)
                    .sizeDp(40)
            );
        }

        private void bind(Person person) {
            this.person = person;
            name.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));

            ImageView image = itemView.findViewById(R.id.image);

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

        }

        @Override
        public void onClick(View v) {

            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                System.out.println("Person clicked");
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);

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
            else {
                System.out.println("Event clicked");
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);

                intent.putExtra("eventID", event.getEventID());
                intent.putExtra("associatedPersonID", event.getPersonID());

                startActivity(intent);
            }

        }
    }
}