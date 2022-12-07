package cs240.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import Model.Event;
import Request.RegisterRequest;
import Result.GetAllEventResult;
import Result.GetAllPersonResult;
import Result.RegisterResult;

public class SearchTest {

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
    }

    @Test
    public void searchReturnResultsTest() {
        ArrayList<Event> foundEvents = cache.searchFilterEvents("a", cache.getEvents());

        assertTrue(foundEvents.size() > 0);
    }

    @Test
    public void searchReturnNoResultsTest() {
        ArrayList<Event> foundEvents =  cache.searchFilterEvents("aasdfafewqzsfq234wqfcdzf", cache.getEvents());

        assertEquals(foundEvents.size(), 0);
    }
}
