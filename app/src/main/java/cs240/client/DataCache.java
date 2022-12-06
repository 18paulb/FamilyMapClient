package cs240.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Model.Person;
import Model.Event;

public class DataCache {

    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {}

    private Map<String, Boolean> settings = new HashMap<>();

    private ArrayList<Person> people;
    private ArrayList<Event> events;
    private ArrayList<String> authTokens = new ArrayList<>();

    private String userID;

    public void addToken(String authToken) {
        authTokens.add(authToken);
    }

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

        //Sort events by Year - Bubble Sort
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

    public Map<String, Boolean> getSettings() {
        return settings;
    }

    public void setSettings(String key, boolean val) {
            this.settings.put(key, val);
    }

    public Set<String> getPaternalAncestors(String personID, Set<String> ancestors, boolean firstRun) {
        Person person = getPerson(personID);

        if (personID == null) {
            return ancestors;
        }

        if (firstRun) {
            getPaternalAncestors(person.getFatherID(), ancestors, false);
        } else {
            ancestors.add(person.getPersonID());
            getPaternalAncestors(person.getFatherID(), ancestors, false);
            getPaternalAncestors(person.getMotherID(), ancestors, false);
        }
        return ancestors;
    }

    public Set<String> getMaternalAncestors(String personID, Set<String> ancestors, boolean firstRun) {
        Person person = getPerson(personID);

        if (personID == null) {
            return ancestors;
        }

        if (firstRun) {
            getMaternalAncestors(person.getMotherID(), ancestors, false);
        } else {
            ancestors.add(person.getPersonID());
            getMaternalAncestors(person.getFatherID(), ancestors, false);
            getMaternalAncestors(person.getMotherID(), ancestors, false);
        }
        return ancestors;
    }

    public ArrayList<Event> searchFilterEvents(String s, ArrayList<Event> all) {
        ArrayList<Event> events = new ArrayList<>();
        DataCache cache = DataCache.getInstance();

        if (s.equals("")) {
            return events;
        }

        for (Event event : all) {
            String city = event.getCity().toLowerCase();
            String country = event.getCountry().toLowerCase();
            String year = Integer.toString(event.getYear()).toLowerCase();
            String eventType = event.getEventType().toLowerCase();

            if (city.contains(s) || country.contains(s) || year.contains(s) || eventType.contains(s)) {
                events.add(event);
            }
        }

        return events;
    }

    public ArrayList<Person> searchFilterPeople(String s, ArrayList<Person> all) {
        ArrayList<Person> people = new ArrayList<>();

        if (s.equals("")) {
            return people;
        }

        for (Person person : all) {
            String firstName = person.getFirstName().toLowerCase();
            String lastName = person.getLastName().toLowerCase();

            if (firstName.contains(s) || lastName.contains(s)) {
                people.add(person);
            }
        }
        return people;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<Event> getFilteredEvents() {
        ArrayList<Event> events = new ArrayList<>();

        if (settings.get("FatherLines")) {
            Set<String> paternalAncestors = getPaternalAncestors(getUserID(), new HashSet<>(), true);
            for (String id : paternalAncestors) {
                events.addAll(getEventsOfPerson(id));
            }
        }

        if (settings.get("MotherLines")) {
            Set<String> maternalAncestors = getMaternalAncestors(getUserID(), new HashSet<>(), true);
            for (String id : maternalAncestors) {
                events.addAll(getEventsOfPerson(id));
            }
        }

        //Adds user and spouse TODO: Account for gender
        events.addAll(getEventsOfPerson(getUserID()));
        Person user = getPerson(getUserID());

        if (user.getSpouseID() != null) {
            events.addAll(getEventsOfPerson(user.getSpouseID()));
        }

        //From the filtered maternal/paternal events, filters even further
        for (int i = 0; i < events.size(); ++i) {
            Person person = getPerson(events.get(i).getPersonID());
            //If male filter is set, adds males

            if ((settings.get("MaleEvents") && person.getGender().equals("m")) || (settings.get("FemaleEvents") && person.getGender().equals("f"))) {
                continue;
            }
            else if (settings.get("MaleEvents") && !person.getGender().equals("m")) {
                events.remove(i);
                i--;
            }
            //If female filter is set, adds females
            else if (settings.get("FemaleEvents") && !person.getGender().equals("f")) {
                events.remove(i);
                i--;
            }
            else if (!settings.get("MaleEvents") && person.getGender().equals("m")) {
                events.remove(i);
                i--;
            }
            else if (!settings.get("FemaleEvents") && person.getGender().equals("f")) {
                events.remove(i);
                i--;
            }
        }

        return events;
    }

    public ArrayList<Person> getFilteredPeople() {
        ArrayList<Person> people = new ArrayList<>();

        if (settings.get("FatherLines")) {
            Set<String> paternalAncestors = getPaternalAncestors(getUserID(), new HashSet<>(), true);
            for (String id : paternalAncestors) {
                people.add(getPerson(id));
            }
        }

        if (settings.get("MotherLines")) {
            Set<String> maternalAncestors = getMaternalAncestors(getUserID(), new HashSet<>(), true);
            for (String id : maternalAncestors) {
                people.add(getPerson(id));
            }
        }

        //Adds user and spouse
        people.add(getPerson(getUserID()));
        Person user = getPerson(getUserID());
        if (user.getSpouseID() != null) {
            Person spouse = getPerson(user.getSpouseID());
            people.add(spouse);
        }

        return people;
    }
}
