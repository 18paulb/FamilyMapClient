package cs240.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import Model.Event;
import Model.Person;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.GetAllEventResult;
import Result.GetAllPersonResult;
import Result.LoginResult;
import Result.RegisterResult;

public class LoginTest {

    @Before
    public void clearDatabase() {
        ServerProxy.clear("localhost", "8080");

        //Registers First
        RegisterRequest regRequest = new RegisterRequest("asdf", "asdf", "asdf" ,"asdf", "adsf", "m");

        try {
            ServerProxy.register("localhost", "8080", regRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginTestPass() {

        LoginRequest request = new LoginRequest();
        request.setUsername("asdf");
        request.setPassword("asdf");

        LoginResult result = new LoginResult();
        try {
            result = ServerProxy.login("localhost", "8080", request);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(result.isSuccess());
        assertTrue(result.getUsername().equals(request.getUsername()));
    }

    @Test
    public void loginTestFail() {
        LoginRequest request = new LoginRequest();
        request.setUsername("wrong");
        request.setPassword("wrong");

        LoginResult result = new LoginResult();
        try {
            result = ServerProxy.login("localhost", "8080", request);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertFalse(result.isSuccess());
    }
    

    @Test
    public void loginEventsCheck1UserTest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("asdf");
        request.setPassword("asdf");

        LoginResult result = new LoginResult();
        try {
            result = ServerProxy.login("localhost", "8080", request);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllEventResult val = ServerProxy.getEvents("localhost", "8080", result.getAuthtoken());

        ArrayList <Event> events = val.getData();

        for (Event event : events) {
            assertEquals("asdf", event.getAssociatedUsername());
        }
    }

    @Test
    public void loginEventsCheck2DifferentUsersTest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("asdf");
        request.setPassword("asdf");

        LoginResult result = new LoginResult();
        try {
            result = ServerProxy.login("localhost", "8080", request);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllEventResult val = ServerProxy.getEvents("localhost", "8080", result.getAuthtoken());

        ArrayList <Event> events = val.getData();

        for (Event event : events) {
            assertEquals("asdf", event.getAssociatedUsername());
        }


        //Registers A Second User
        RegisterRequest regRequest = new RegisterRequest("fdas", "fdas", "fdas" ,"fdas", "fdas", "f");
        try {
            ServerProxy.register("localhost", "8080", regRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        request = new LoginRequest();
        request.setUsername("fdas");
        request.setPassword("fdas");

        result = new LoginResult();
        try {
            result = ServerProxy.login("localhost", "8080", request);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        val = ServerProxy.getEvents("localhost", "8080", result.getAuthtoken());

        events = val.getData();

        for (Event event : events) {
            assertEquals("fdas", event.getAssociatedUsername());
        }
    }

    @Test
    public void loginPeopleCheck1UserTest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("asdf");
        request.setPassword("asdf");

        LoginResult result = new LoginResult();
        try {
            result = ServerProxy.login("localhost", "8080", request);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllPersonResult persons = ServerProxy.getPersons("localhost", "8080", result.getAuthtoken());

        ArrayList<Person> people = persons.getData();

        for (Person person : people) {
            assertEquals("asdf", person.getAssociatedUsername());
        }
    }

    @Test
    public void loginPeopleCheck2UsersTest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("asdf");
        request.setPassword("asdf");

        LoginResult result = new LoginResult();
        try {
            result = ServerProxy.login("localhost", "8080", request);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllPersonResult persons = ServerProxy.getPersons("localhost", "8080", result.getAuthtoken());

        ArrayList<Person> people = persons.getData();

        for (Person person : people) {
            assertEquals("asdf", person.getAssociatedUsername());
        }

        //Registers A Second User
        RegisterRequest regRequest = new RegisterRequest("fdas", "fdas", "fdas" ,"fdas", "fdas", "f");
        try {
            ServerProxy.register("localhost", "8080", regRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        request = new LoginRequest();
        request.setUsername("fdas");
        request.setPassword("fdas");

        result = new LoginResult();
        try {
            result = ServerProxy.login("localhost", "8080", request);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        persons = ServerProxy.getPersons("localhost", "8080", result.getAuthtoken());

        people = persons.getData();

        for (Person person : people) {
            assertEquals("fdas", person.getAssociatedUsername());
        }
    }
}
