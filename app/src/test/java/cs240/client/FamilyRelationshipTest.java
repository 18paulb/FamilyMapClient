package cs240.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import Model.Person;
import Request.RegisterRequest;
import Result.GetAllEventResult;
import Result.GetAllPersonResult;
import Result.RegisterResult;

public class FamilyRelationshipTest {

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


        cache.setUserID(result.getPersonID());

        GetAllPersonResult persons = ServerProxy.getPersons("localhost", "8080", result.getAuthtoken());
        cache.setPeople(persons.getData());

        GetAllEventResult events = ServerProxy.getEvents("localhost", "8080", result.getAuthtoken());
        cache.setEvents(events.getData());
    }

    @Test
    public void familyRelationshipUserTest() {
        //User should have no children and one parent and father

        Person user = cache.getPerson(cache.getUserID());

        Person father = new Person();
        Person mother = new Person();
        ArrayList<Person> people = cache.getPeople();

        for (Person person : people) {

            //Check if it's the user
            if (person.getPersonID().equals(cache.getUserID())) {
                continue;
            }

            //Check if it's the user's father
            if (person.getPersonID().equals(user.getFatherID())) {
                father = person;
            }

            //Check if it's the user's mother
            if (person.getPersonID().equals(user.getMotherID())) {
                mother = person;
            }
        }

        assertEquals("Father", cache.getRelationToPerson(user.getPersonID(), father.getPersonID()));
        assertEquals("Mother", cache.getRelationToPerson(user.getPersonID(), mother.getPersonID()));
    }

    @Test
    public void familyRelationshipWithChildTest() {
        Person user = cache.getPerson(cache.getUserID());

        //The person we are testing is the user's father
        Person testPerson = cache.getPerson(user.getFatherID());

        Person father = new Person();
        Person mother = new Person();
        ArrayList<Person> people = cache.getPeople();

        for (Person person : people) {

            //Check if it's the user
            if (person.getPersonID().equals(testPerson.getPersonID())) {
                continue;
            }

            //Check if it's the user's father
            if (person.getPersonID().equals(testPerson.getFatherID())) {
                father = person;
            }

            //Check if it's the user's mother
            if (person.getPersonID().equals(testPerson.getMotherID())) {
                mother = person;
            }
        }

        assertEquals("Father", cache.getRelationToPerson(testPerson.getPersonID(), father.getPersonID()));
        assertEquals("Mother", cache.getRelationToPerson(testPerson.getPersonID(), mother.getPersonID()));
        assertEquals("Child", cache.getRelationToPerson(testPerson.getPersonID(), user.getPersonID()));
        assertEquals("Spouse", cache.getRelationToPerson(testPerson.getPersonID(), testPerson.getSpouseID()));
    }
}
