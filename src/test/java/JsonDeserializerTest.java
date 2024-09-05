import org.junit.jupiter.api.Test;
import ru.clevertec.deserializer.JsonDeserializer;
import test_models.Car;

import static org.junit.jupiter.api.Assertions.*;

public class JsonDeserializerTest {
    @Test
    void testDeserializeStringField() {
        String json = """
        {
            "model": "Tesla Model X"
        }
        """;

        Car car = JsonDeserializer.deserialize(json, Car.class);

        assertEquals("Tesla Model X", car.getModel());
        assertNull(car.getEngine());
        assertNull(car.getOwners());
    }

    @Test
    void testDeserializeIntField() {
        String json = """
        {
            "year": 2003
        }
        """;

        Car car = JsonDeserializer.deserialize(json, Car.class);

        assertEquals(2003, car.getYear());
        assertNull(car.getModel());
        assertNull(car.getEngine());
        assertNull(car.getOwners());
    }

    @Test
    void testDeserializeBooleanField() {
        String json = """
        {
            "engine": {
                "electric": true
            }
        }
        """;

        Car car = JsonDeserializer.deserialize(json, Car.class);

        assertNotNull(car.getEngine());
        assertTrue(car.getEngine().isElectric());
        assertEquals(0, car.getEngine().getHorsepower());
        assertNull(car.getModel());
    }

    @Test
    void testDeserializeNestedObject() {
        String json = """
        {
            "engine": {
                "horsepower": 350,
                "electric": false
            }
        }
        """;

        Car car = JsonDeserializer.deserialize(json, Car.class);

        assertNotNull(car.getEngine());
        assertEquals(350, car.getEngine().getHorsepower());
        assertFalse(car.getEngine().isElectric());
        assertNull(car.getModel());
    }

    @Test
    void testDeserializeListOfObjects() {
        String json = """
        {
            "owners": [
                {
                    "full_name": "Pavel Bandarenka",
                    "age": 21
                },
                {
                    "full_name": "Ne Pavel Bandarenka",
                    "age": 50
                }
            ]
        }
        """;

        Car car = JsonDeserializer.deserialize(json, Car.class);

        assertNotNull(car.getOwners());
        assertEquals(2, car.getOwners().size());
        assertEquals("Pavel Bandarenka", car.getOwners().get(0).getName());
        assertEquals(21, car.getOwners().get(0).getAge());
        assertEquals("Ne Pavel Bandarenka", car.getOwners().get(1).getName());
        assertEquals(50, car.getOwners().get(1).getAge());
    }

    @Test
    void testDeserializeWithJsonFieldAnnotation() {
        String json = """
        {
            "owners": [
                {
                    "full_name": "Pavel Bandarenka",
                    "age": 35
                }
            ]
        }
        """;

        Car car = JsonDeserializer.deserialize(json, Car.class);

        assertNotNull(car.getOwners());
        assertEquals(1, car.getOwners().size());
        assertEquals("Pavel Bandarenka", car.getOwners().get(0).getName());
        assertEquals(35, car.getOwners().get(0).getAge());
    }


    @Test
    void testDeserializeWithMissingFields() {
        String json = """
        {
            "model": "Moscwich"
        }
        """;

        Car car = JsonDeserializer.deserialize(json, Car.class);

        assertEquals("Moscwich", car.getModel());
        assertEquals(0, car.getYear());
        assertNull(car.getEngine());
        assertNull(car.getOwners());
    }
}
