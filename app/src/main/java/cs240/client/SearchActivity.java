package cs240.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity {

    private final int EVENT_ITEM_VIEW_TYPE = 0;
    private final int PERSON_ITEM_VIEW_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        DataCache cache = DataCache.getInstance();
        ArrayList<Event> events = cache.getEvents();
        ArrayList<Person> people = cache.getPeople();

        SearchAdaptor adaptor = new SearchAdaptor(events, people);
        recyclerView.setAdapter(adaptor);
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

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;

            if (viewType == EVENT_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.life_event_item, parent, false);
            }
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.family_item, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < events.size()) {
                holder.bind(events.get(position));
            } else {
                //FIXME: I do not understand this
                holder.bind(people.get(position - events.size()));
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
        private TextView relationship;

        private int viewType;
        private Event event;
        private Person person;

        DataCache cache = DataCache.getInstance();


        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if (viewType == EVENT_ITEM_VIEW_TYPE) {
                name = itemView.findViewById(R.id.name);
                location = itemView.findViewById(R.id.location);
                eventType = itemView.findViewById(R.id.eventType);
            }
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                name = itemView.findViewById(R.id.name);
                relationship = itemView.findViewById(R.id.relationship);
            }
        }

        private void bind(Event event) {
            this.event = event;

            Person person = cache.getPerson(event.getPersonID());
            name.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));
            location.setText(String.format("%s %s", event.getCity(), event.getCountry()));
            eventType.setText(String.format("%s: ", event.getEventType()));
        }

        private void bind(Person person) {
            this.person = person;
            name.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));
        }

        @Override
        public void onClick(View v) {
            if (viewType == EVENT_ITEM_VIEW_TYPE) {
                System.out.println("Event clicked");
            }
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                System.out.println("Person clicked");
            }
        }
    }
}