package cs240.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.core.app.RemoteInput;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

import Model.Event;
import Model.Person;
import Request.RegisterRequest;
import Result.GetAllEventResult;
import Result.GetAllPersonResult;
import Result.RegisterResult;

public class RegisterTest {

    @Before
    public void clearDatabase() {
        ServerProxy.clear("localhost", "8080");
    }

    @Test
    public void registerTestPass() {
        RegisterRequest request = new RegisterRequest("asdfl", "asdf", "asdf" ,"asdf", "adsf", "m");

        RegisterResult result = new RegisterResult();
        try {
            result = ServerProxy.register("localhost", "8080", request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(result.isSuccess());
    }

    @Test
    public void registerTestFail() {
        //Should already exist
        RegisterRequest request = new RegisterRequest("asdf", "asdf", "asdf" ,"asdf", "adsf", "m");

        RegisterResult result = new RegisterResult();
        try {
            result = ServerProxy.register("localhost", "8080", request);
            result = ServerProxy.register("localhost", "8080", request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertFalse(result.isSuccess());
    }

    @Test
    public void registerEventsCheck1UserTest() {
        RegisterRequest request = new RegisterRequest("asdf", "asdf", "asdf" ,"asdf", "adsf", "m");

        RegisterResult result = new RegisterResult();
        try {
            result = ServerProxy.register("localhost", "8080", request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllEventResult val = ServerProxy.getEvents("localhost", "8080", result.getAuthtoken());

        ArrayList<Event> events = val.getData();

        for (Event event : events) {
            assertTrue(event.getAssociatedUsername().equals("asdf"));
        }
    }

    @Test
    public void registerEventsCheck2UsersTest() {
        RegisterRequest request = new RegisterRequest("asdf", "asdf", "asdf" ,"asdf", "adsf", "m");

        RegisterResult result = new RegisterResult();
        try {
            result = ServerProxy.register("localhost", "8080", request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllEventResult val = ServerProxy.getEvents("localhost", "8080", result.getAuthtoken());

        ArrayList<Event> events = val.getData();

        for (Event event : events) {
            assertTrue(event.getAssociatedUsername().equals("asdf"));
        }


        //Register 2nd User
        request = new RegisterRequest("fdas", "fdas", "fdas" ,"fdas", "fdas", "f");
        try {
            result = ServerProxy.register("localhost", "8080", request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        val = ServerProxy.getEvents("localhost", "8080", result.getAuthtoken());

        events = val.getData();

        for (Event event : events) {
            assertTrue(event.getAssociatedUsername().equals("fdas"));
        }
    }

    @Test
    public void registerPeopleCheck1UserTest() {
        RegisterRequest request = new RegisterRequest("asdf", "asdf", "asdf" ,"asdf", "adsf", "m");

        RegisterResult result = new RegisterResult();
        try {
            result = ServerProxy.register("localhost", "8080", request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllPersonResult val = ServerProxy.getPersons("localhost", "8080", result.getAuthtoken());

        ArrayList<Person> people = val.getData();

        for (Person person : people) {
            assertEquals("asdf", person.getAssociatedUsername());
        }
    }

    @Test
    public void registerPeopleCheck2UsersTest() {
        RegisterRequest request = new RegisterRequest("asdf", "asdf", "asdf" ,"asdf", "adsf", "m");

        RegisterResult result = new RegisterResult();
        try {
            result = ServerProxy.register("localhost", "8080", request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllPersonResult val = ServerProxy.getPersons("localhost", "8080", result.getAuthtoken());

        ArrayList<Person> people = val.getData();

        for (Person person : people) {
            assertEquals("asdf", person.getAssociatedUsername());
        }

        //Registers and Checks People for Second User

        request = new RegisterRequest("fdas", "fdas", "fdas" ,"fdas", "fdas", "f");
        try {
            result = ServerProxy.register("localhost", "8080", request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        val = ServerProxy.getPersons("localhost", "8080", result.getAuthtoken());

        people = val.getData();

        for (Person person : people) {
            assertEquals("fdas", person.getAssociatedUsername());
        }
    }
}
