package com.etrade.eo.core.json;

import com.etrade.eo.core.json.serializers.GsonTypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GsonJsonUtilTest {

    private static final String TEST_GENERIC_JSON   = readResourceFile("generic.json");
    private static final String TEST_PERSON_JSON    = readResourceFile("person.json");

    private static final JsonUtil jsonUtil;

    static {
        final Set<GsonTypeAdapter<?>> gsonTypeAdapters = new HashSet<>();
        gsonTypeAdapters.add(GsonModule.provideBigDecimalGsonTypeAdapter());
        jsonUtil = new GsonJsonUtil(GsonModule.provideGson(gsonTypeAdapters));
    }

    @Test
    public void serializeGeneric() {
        TestObjectHolder<Person> holder = new TestObjectHolder<>("TestObjectHolder", new Person("Chuong", "Mai"));
        Person person = holder.getTestObject();

        List<Person> ancestors = new ArrayList<>();
        ancestors.add(new Person("Adam", null));
        ancestors.add(new Person("Eve", null));
        person.setAncestors(ancestors);
        String json = jsonUtil.serialize(holder);

        Assert.assertEquals(TEST_GENERIC_JSON.replaceAll("\\s", ""), json);
    }

    @Test
    public void deserializeGeneric() {
        TestObjectHolder<Person> holder = jsonUtil.deserialize(TEST_GENERIC_JSON, new TypeToken<TestObjectHolder<Person>>(){});

        Assert.assertNotNull(holder);
        Assert.assertEquals("TestObjectHolder", holder.message);

        Person person = holder.getTestObject();
        Assert.assertNotNull(person);
        Assert.assertEquals("Chuong", person.getFirstName());
        Assert.assertEquals("Mai", person.getLastName());

        Assert.assertEquals(2, person.getAncestors().size());
        Assert.assertEquals("Adam", person.getAncestors().get(0).getFirstName());
        Assert.assertEquals("Eve", person.getAncestors().get(1).getFirstName());
    }

    @Test
    public void serialize() {
        Person person = new Person("Chuong", "Mai");

        List<Person> ancestors = new ArrayList<>();
        ancestors.add(new Person("Adam", null));
        ancestors.add(new Person("Eve", null));
        person.setAncestors(ancestors);

        String json = jsonUtil.serialize(person);
        Assert.assertEquals(TEST_PERSON_JSON.replaceAll("\\s", ""), json);
    }

    @Test
    public void deserialize() {
        Person person = jsonUtil.deserialize(TEST_PERSON_JSON, Person.class);
        validatePerson(person);
    }

    @Test
    public void deserializeStream() throws Exception {
        final InputStream personStream = new ByteArrayInputStream(TEST_PERSON_JSON.getBytes(UTF_8));
        final Person person = jsonUtil.deserialize(personStream, UTF_8.name(), Person.class);
        validatePerson(person);
    }

    private static void validatePerson(final Person person) {
        Assert.assertNotNull(person);
        Assert.assertEquals("Chuong", person.getFirstName());
        Assert.assertEquals("Mai", person.getLastName());

        Assert.assertEquals(2, person.getAncestors().size());
        Assert.assertEquals("Adam", person.getAncestors().get(0).getFirstName());
        Assert.assertEquals("Eve", person.getAncestors().get(1).getFirstName());
    }

    private static class TestObjectHolder<T> {

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("testObject")
        @Expose
        private T testObject;

        TestObjectHolder(String message, T testObject) {
            this.message = message;
            this.testObject = testObject;
        }

        T getTestObject() {
            return testObject;
        }
    }

    private static class Person {
        @SerializedName("firstName")
        @Expose
        private String firstName;

        @SerializedName("lastName")
        @Expose
        private String lastName;

        @SerializedName("ancestors")
        @Expose
        private List<Person> ancestors;

        Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        String getFirstName() {
            return firstName;
        }

        String getLastName() {
            return lastName;
        }

        List<Person> getAncestors() {
            return ancestors;
        }

        void setAncestors(List<Person> ancestors) {
            this.ancestors = ancestors;
        }
    }

    private static String readResourceFile(final String resourcePath) {
        final InputStream fileStream = GsonJsonUtilTest.class.getResourceAsStream(resourcePath);
        return new Scanner(fileStream).useDelimiter("\\A").next();
    }
}
