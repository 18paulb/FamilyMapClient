package cs240.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

import Model.Event;
import Model.Person;
import Request.LoginRequest;
import Result.GetAllEventResult;
import Result.GetAllPersonResult;
import Result.LoginResult;

@RunWith(AndroidJUnit4.class)
public class LoginTest {
    @Test
    public void loginTest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("sheila");
        request.setPassword("parker");

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
    public void loginEventsTest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("sheila");
        request.setPassword("parker");

        LoginResult result = new LoginResult();
        try {
            result = ServerProxy.login("localhost", "8080", request);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllEventResult val = ServerProxy.getEvents("localhost", "8080", result.getAuthtoken());

        ArrayList <Event> events = val.getData();

        ArrayList<String> correctEvents = new ArrayList<>();
        correctEvents.add("Sheila_Birth");
        correctEvents.add("Sheila_Marriage");
        correctEvents.add("Sheila_Asteroids");
        correctEvents.add("Other_Asteroids");
        correctEvents.add("Sheila_Death");
        correctEvents.add("Davis_Birth");
        correctEvents.add("Blaine_Birth");
        correctEvents.add("Betty_Death");
        correctEvents.add("BYU_graduation");
        correctEvents.add("Rodham_Marriage");
        correctEvents.add("Mrs_Rodham_Backflip");
        correctEvents.add("Mrs_Rodham_Java");
        correctEvents.add("Jones_Frog");
        correctEvents.add("Jones_Marriage");
        correctEvents.add("Mrs_Jones_Barbecue");
        correctEvents.add("Mrs_Jones_Surf");

        assertEquals(events.size(), correctEvents.size());

        for (int i = 0; i < correctEvents.size(); ++i) {
            boolean contains = false;
            for (int j = 0; j < events.size(); ++j) {
                if (correctEvents.get(i).equals(events.get(i).getEventID())) {
                    contains = true;
                }
            }
            assertTrue(contains);
        }
    }

    @Test
    public void loginPeopleTest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("sheila");
        request.setPassword("parker");

        LoginResult result = new LoginResult();
        try {
            result = ServerProxy.login("localhost", "8080", request);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllPersonResult persons = ServerProxy.getPersons("localhost", "8080", result.getAuthtoken());

        ArrayList<Person> people = persons.getData();

        ArrayList<String> correctPeople = new ArrayList<>();
        correctPeople.add("Sheila_Parker");
        correctPeople.add("Davis_Hyer");
        correctPeople.add("Blaine_McGary");
        correctPeople.add("Betty_White");
        correctPeople.add("Ken_Rodham");
        correctPeople.add("Mrs_Rodham");
        correctPeople.add("Frank_Jones");
        correctPeople.add("Mrs_Jones");

        assertEquals(people.size(), correctPeople.size());

        for (int i = 0; i < correctPeople.size(); ++i) {
            boolean contains = false;
            for (int j = 0; j < people.size(); ++j) {
                if (correctPeople.get(i).equals(people.get(i).getPersonID())) {
                    contains = true;
                }
            }
            assertTrue(contains);
        }
    }
}
