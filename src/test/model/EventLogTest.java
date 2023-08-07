package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventLogTest {

    private Event testEvent1;
    private Event testEvent2;
    private Event testEvent3;

    @BeforeEach
    void runBefore() {
        testEvent1 = new Event("Aponia");
        testEvent2 = new Event("Elysia");
        testEvent3 = new Event("gg");
        EventLog testEventLog = EventLog.getInstance();
        testEventLog.logEvent(testEvent1);
        testEventLog.logEvent(testEvent2);
        testEventLog.logEvent(testEvent3);
    }

    @Test
    public void testLogEvent() {
        List<Event> testEventList = new ArrayList<>();

        EventLog testEventLog = EventLog.getInstance();
        for (Event event : testEventLog) {
            testEventList.add(event);
        }

        assertTrue(testEventList.contains(testEvent1));
        assertTrue(testEventList.contains(testEvent2));
        assertTrue(testEventList.contains(testEvent3));
    }

    @Test
    public void testClear() {
        EventLog testEventLog = EventLog.getInstance();
        testEventLog.clear();
        Iterator<Event> testIterator = testEventLog.iterator();
        assertTrue(testIterator.hasNext());
        assertEquals("Event log cleared.", testIterator.next().getDescription());
        assertFalse(testIterator.hasNext());
    }
}
