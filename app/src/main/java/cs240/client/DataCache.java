package cs240.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.Person;
import Model.Event;

public class DataCache {

    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {}

    private String userFirstName;
    private String userLastName;

    private ArrayList<Person> people;
    private ArrayList<Event> events;
    private ArrayList<String> authTokens;

    public void addToken(String authToken) {
        authTokens.add(authToken);
    }

    //String is the person ID
    private Map<String, Person> mappedPerson;

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    //Map<EventID, Event> events;
    //Map<PersonID, List<Event>> personEvents;
    //Set<PersonID> paternalAncestors;
    //Set<PersonID> maternalAncestors;
}
