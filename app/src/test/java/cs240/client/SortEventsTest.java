package cs240.client;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import Model.Event;
import Request.RegisterRequest;
import Result.GetAllEventResult;
import Result.GetAllPersonResult;
import Result.RegisterResult;

public class SortEventsTest {

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
    public void sortedEventsCheck1UserTest() {
        ArrayList<Event> events = cache.getEventsOfPerson("asdf");

        for (int i = 0; i < events.size(); ++i) {
            if (i+1 != events.size()) {
                assertTrue(events.get(i).getYear() <= events.get(i+1).getYear());
            }
        }
    }


    @Test
    public void sortedEventsCheck2UsersTest() {
        ArrayList<Event> firstEvents = cache.getEventsOfPerson("asdf");

        for (int i = 0; i < firstEvents.size(); ++i) {
            if (i+1 != firstEvents.size()) {
                assertTrue(firstEvents.get(i).getYear() <= firstEvents.get(i+1).getYear());
            }
        }

        //Registers New User and sets new DataCache with new Events
        //Registers First
        RegisterRequest regRequest = new RegisterRequest("fdas", "fdas", "fdas" ,"fdas", "fdas", "f");
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

        ArrayList<Event> secondEvents = cache.getEventsOfPerson("fdas");

        for (int i = 0; i < secondEvents.size(); ++i) {
            if (i+1 != secondEvents.size()) {
                assertTrue(secondEvents.get(i).getYear() <= secondEvents.get(i+1).getYear());
            }
        }
    }
}
