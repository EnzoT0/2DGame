package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EventTest {
    private Event testEvent;
    private Date testDate;

    @BeforeEach
    void runBefore() {
        testEvent = new Event("Character has achieved something");
        testDate = Calendar.getInstance().getTime();
    }

    @Test
    public void testEvent() {
        assertEquals("Character has achieved something", testEvent.getDescription());
        assertTrue(testDate.getTime() - 100 <= testEvent.getDate().getTime()
                && testEvent.getDate().getTime() <= testDate.getTime() + 100);
    }


    @Test
    public void testEquals1() {
        // two different objects equal cases
        Event event1 = new Event("Aponia");
        Event event2 = new Event("Aponia");

        assertTrue(event1.equals(event2));
        assertTrue(event2.equals(event1));
        assertTrue(event1.getDate().equals(event2.getDate()));
        assertTrue(event2.getDate().equals(event1.getDate()));

        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testEquals2() {
        // same objects equal cases
        Event event1 = new Event("Aponia");

        assertTrue(event1.equals(event1));
        assertTrue(event1.getDate().equals(event1.getDate()));

        assertEquals(event1.hashCode(), event1.hashCode());
    }

    @Test
    public void testEquals3() {
        // two different objects equal cases
        Event event1 = new Event("Aponia");
        Event event2 = new Event("Elysia");

        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));

        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testEquals4() {
        // two different objects not same class equal cases
        Event event1 = new Event("Aponia");
        Position pos = new Position(1, 3);

        assertFalse(event1.equals(pos));
        assertFalse(pos.equals(event1));

        assertNotEquals(event1.hashCode(), pos.hashCode());
    }

    @Test
    public void testEquals5() {
        // null case
        Event event1 = new Event("Aponia");

        assertFalse(event1.equals(null));
    }


    @Test
    public void testToString() {
        assertEquals(testDate.toString() + "\n" + "Character has achieved something", testEvent.toString());
    }

}
