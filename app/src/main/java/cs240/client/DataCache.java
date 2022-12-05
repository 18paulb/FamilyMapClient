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

    private ArrayList<Person> people;
    private ArrayList<Event> events;
    private ArrayList<String> authTokens = new ArrayList<>();

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

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Event> getEventsOfPerson(String personID) {
        ArrayList<Event> events = new ArrayList<>();

        //Add events
        for (Event event : this.events) {
            if (event.getPersonID().equals(personID)) {
                events.add(event);
            }
        }

        //Sort events - Bubble Sort
        int size = events.size();

        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (events.get(j).getYear() > events.get(j + 1).getYear()) {
                    Event temp = events.get(j);
                    events.set(j, events.get(j + 1));
                    events.set(j + 1, temp);
                }
            }
        }
        return events;
    }

    public ArrayList<Person> getFamilyOfPerson(String personID) {
        ArrayList<Person> family = new ArrayList<>();

        Person person = getPerson(personID);

        //Gets Mother, Father, Spouse
        if (person.getFatherID() != null) {
            family.add(getPerson(person.getFatherID()));
        }
        if (person.getMotherID() != null) {
            family.add(getPerson(person.getMotherID()));
        }
        if (person.getSpouseID() != null) {
            family.add(getPerson(person.getSpouseID()));
        }

        //Gets Children
        for (Person tmp : this.people) {
            if (tmp.getFatherID() != null && tmp.getFatherID().equals(personID)) {
                family.add(tmp);
            }
            if (tmp.getMotherID() != null && tmp.getMotherID().equals(personID)) {
                family.add(tmp);
            }
        }

        return family;
    }

    public Person getPerson(String id) {
        for (Person person : this.people) {
            if (person.getPersonID().equals(id)) {
                return person;
            }
        }
        return null;
    }

    public Event getEvent(String id) {
        for (Event event : this.events) {
            if (event.getEventID().equals(id)) {
                return event;
            }
        }
        return null;
    }

    public String getRelationToPerson(String personID, String relativeID) {
        Person person = getPerson(personID);
        Person relative = getPerson(relativeID);

        if (person.getFatherID()!= null && person.getFatherID().equals(relativeID)) {
            return "Father";
        }
        if (person.getMotherID()!= null && person.getMotherID().equals(relativeID)) {
            return "Mother";
        }
        if (person.getSpouseID()!= null && person.getSpouseID().equals(relativeID)) {
            return "Spouse";
        }

        if (relative.getFatherID()!= null && relative.getFatherID().equals(personID)) {
            return "Child";
        }
        if (relative.getMotherID()!= null && relative.getMotherID().equals(personID)) {
            return "Child";
        }

        return "No Relation";

    }

    //Map<EventID, Event> events;
    //Map<PersonID, List<Event>> personEvents;
    //Set<PersonID> paternalAncestors;
    //Set<PersonID> maternalAncestors;
}
