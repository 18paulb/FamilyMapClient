package cs240.client;

import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.Event;
import Model.Person;
import Request.RegisterRequest;
import Result.GetAllEventResult;
import Result.GetAllPersonResult;
import Result.RegisterResult;

public class FilterEventsTest {

    DataCache cache = DataCache.getInstance();

    @Before
    public void clearDatabase() {
        ServerProxy.clear("localhost", "8080");
    }

    @Before
    public void registerUserAndInitializeDataCache() {
        //Registers First
        RegisterRequest regRequest = new RegisterRequest("asdf", "asdf", "asdf" ,"asdf", "adsf", "m");
        RegisterResult result = new RegisterResult();
        try {
            result = ServerProxy.register("localhost", "8080", regRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllPersonResult persons = ServerProxy.getPersons("localhost", "8080", result.getAuthtoken());
        cache.setPeople(persons.getData());

        GetAllEventResult events = ServerProxy.getEvents("localhost", "8080", result.getAuthtoken());
        cache.setEvents(events.getData());

        cache.setUserID(result.getPersonID());
    }

    @Test
    public void filterMaleEventsTest() {
        cache.setSettings("MaleEvents", true);
        cache.setSettings("FemaleEvents", false);
        cache.setSettings("FatherLines", true);
        cache.setSettings("MotherLines", true);

        ArrayList<Event> filteredEvents = cache.getFilteredEvents();

        for (Event event : filteredEvents) {
            Person person = cache.getPerson(event.getPersonID());
            assertNotEquals(person.getGender(), "f");
        }
    }

    @Test
    public void filterFemaleEventsTest() {
        cache.setSettings("MaleEvents", false);
        cache.setSettings("FemaleEvents", true);
        cache.setSettings("FatherLines", true);
        cache.setSettings("MotherLines", true);

        ArrayList<Event> filteredEvents = cache.getFilteredEvents();

        for (Event event : filteredEvents) {
            Person person = cache.getPerson(event.getPersonID());
            assertNotEquals(person.getGender(), "m");
        }

    }
}
