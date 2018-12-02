package ch.epfl.sweng.eventmanager.test.repository;

import android.graphics.Bitmap;

import ch.epfl.sweng.eventmanager.repository.CloudFunction;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.EventLocation;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.repository.data.Position;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;
import ch.epfl.sweng.eventmanager.test.ObservableMap;
import ch.epfl.sweng.eventmanager.test.ticketing.MockStacks;
import ch.epfl.sweng.eventmanager.users.DummyInMemorySession;

/**
 * @author Louis Vialar
 */
public class MockEventsRepository implements EventRepository, CloudFunction {
    public static final Map<Integer, EventTicketingConfiguration> CONFIG_BY_EVENT;
    public static final String EVENT_EMAIL = "events@not-really-epfl.ch";
    private static int CURRENT_EVENT_ID = 1000;

    static {
        Map<Integer, EventTicketingConfiguration> configurationMap = new HashMap<>();
        configurationMap.put(1, MockStacks.BASIC_CONFIGURATION);
        configurationMap.put(2, MockStacks.MULTI_CONFIGURATION);
        configurationMap.put(3, MockStacks.AUTH_MULTI_CONFIGURATION);

        CONFIG_BY_EVENT = Collections.unmodifiableMap(configurationMap);
    }

    private final ObservableMap<Integer, Event> events = new ObservableMap<>();
    private final ObservableMap<Integer, Bitmap> eventImages = new ObservableMap<>();
    private final ObservableMap<Integer, List<Spot>> spots = new ObservableMap<>();
    private final ObservableMap<Integer, List<ScheduledItem>> scheduledItems = new ObservableMap<>();
    private final ObservableMap<Integer, List<Zone>> zones = new ObservableMap<>();

    {
        String orgaEmail = EVENT_EMAIL;
        // Init events

        TypeToken<List<Spot>> spotsToken = new TypeToken<List<Spot>>() {
        };

        String jsonSpots = "[ {\n" +
                "  \"position\" : {\n" +
                "    \"latitude\" : 46.517799,\n" +
                "    \"longitude\" : 6.566737\n" +
                "  },\n" +
                "  \"spotType\" : \"SCENE\",\n" +
                "  \"title\" : \"Grande scène\"\n" +
                "}, {\n" +
                "  \"position\" : {\n" +
                "    \"latitude\" : 46.520433,\n" +
                "    \"longitude\" : 6.567822\n" +
                "  },\n" +
                "  \"spotType\" : \"BAR\",\n" +
                "  \"title\" : \"Satellite\"\n" +
                "}, {\n" +
                "  \"position\" : {\n" +
                "    \"latitude\" : 46.523,\n" +
                "    \"longitude\" : 6.567822\n" +
                "  },\n" +
                "  \"spotType\" : \"NURSERY\",\n" +
                "  \"title\" : \"test3\"\n" +
                "}, {\n" +
                "  \"position\" : {\n" +
                "    \"latitude\" : 46.520433,\n" +
                "    \"longitude\" : 6.5679\n" +
                "  },\n" +
                "  \"spotType\" : \"WC\",\n" +
                "  \"title\" : \"test4\"\n" +
                "}, {\n" +
                "  \"position\" : {\n" +
                "    \"latitude\" : 46.520433,\n" +
                "    \"longitude\" : 6.55555\n" +
                "  },\n" +
                "  \"spotType\" : \"INFORMATION\",\n" +
                "  \"title\" : \"test5\"\n" +
                "} ]\n";


        TypeToken<List<Zone>> zonesToken = new TypeToken<List<Zone>>() {
        };

        String jsonZone = "[ {\n      \"positions\" : [ {\n        " +
                "\"latitude\" : 46.51859,\n        \"longitude\" " +
                ": 6.561272\n      }, {\n        \"latitude\" : 46.522148,\n " +
                "       \"longitude\" : 6.563289\n      }, {\n       " +
                " \"latitude\" : 46.52144,\n        \"longitude\" :" +
                "6.5717\n      }, {\n        \"latitude\" : 46.518295,\n" +
                "        \"longitude\" : 6.571958\n      }, {\n       " +
                " \"latitude\" : 46.517365,\n        \"longitude\" :" +
                " 6.566036\n      } ]\n    } ]";

        Map<String, Map<String, String>> usersMap = new HashMap<>();
        Map<String, String> userUids = new HashMap<>();
        userUids.put("key1", DummyInMemorySession.DUMMY_UID);
        usersMap.put("admin", userUids);

        addEvent(new Event(1, "Event with scheduled items", "Description", new Date(1550307600L), new Date(1550422800L),
                orgaEmail, null, new EventLocation("EPFL", Position.EPFL), usersMap, "JapanImpact",
                CONFIG_BY_EVENT.get(1)));

        addEvent(new Event(2, "Event without items", "Description", new Date(1550307600L), new Date(1550422800L),
                orgaEmail, null, new EventLocation("EPFL", Position.EPFL), usersMap, "JapnImpact",
                CONFIG_BY_EVENT.get(2)));

        addEvent(new Event(3, "Event without items B", "Description", new Date(1550307600L), new Date(1550422800L),
                orgaEmail, null, new EventLocation("EPFL", Position.EPFL), usersMap, "JapanImpact",
                CONFIG_BY_EVENT.get(3)));

        addZones(1, new Gson().fromJson(jsonZone, zonesToken.getType()));
        addSpots(1, new Gson().fromJson(jsonSpots, spotsToken.getType()));

        List<ScheduledItem> items;
        String jsonSchedule = "[ {\n" +
                "  \"artist\" : \"David Guetta\",\n" +
                "  \"date\" : 1744913000000,\n" +
                "  \"description\" : \"Incredible stage performance by famous DJ David Guetta!\",\n" +
                "  \"duration\" : 1.5,\n" +
                "  \"genre\" : \"Electro/Dance\",\n" +
                "  \"id\" : \"b52a251a-6ee5-4ef2-af5b-4475a8244aaf\",\n" +
                "  \"itemLocation\" : \"Polyv\"\n" +
                "}, {\n" +
                "  \"artist\" : \"ABBA\",\n" +
                "  \"date\" : 1744904900000,\n" +
                "  \"description\" : \"Wow! This is the great comeback of the well-known success group!\",\n" +
                "  \"duration\" : 2,\n" +
                "  \"genre\" : \"Rock\",\n" +
                "  \"id\" : \"d41e8f29-51db-4924-8363-1bcc1e98631d\",\n" +
                "  \"itemLocation\" : \"CE\"\n" +
                "}, {\n" +
                "  \"artist\" : \"Daft Punk\",\n" +
                "  \"date\" : 1744904900000,\n" +
                "  \"description\" : \"Le retour des frenchies !\",\n" +
                "  \"duration\" : 1.5,\n" +
                "  \"genre\" : \"Pop/House\",\n" +
                "  \"id\" : \"90b8b958-9915-4647-809d-470122c28265\",\n" +
                "  \"itemLocation\" : \"Polyv\"\n" +
                "}, {\n" +
                "  \"artist\" : \"Black M\",\n" +
                "  \"date\" : 1741980110000,\n" +
                "  \"description\" : \"Le retour !\",\n" +
                "  \"genre\" : \"R&B/Pop\",\n" +
                "  \"id\" : \"7a9207df-202b-4e83-93a5-8466563466ca\",\n" +
                "  \"itemLocation\" : \"CO\"\n" +
                "} ]\n";




        TypeToken<List<ScheduledItem>> scheduleToken = new TypeToken<List<ScheduledItem>>() {
        };
        items = new Gson().fromJson(jsonSchedule, scheduleToken.getType());

        this.scheduledItems.put(1, items);
    }

    private void addEvent(Event event) {
        events.put(event.getId(), event);
        eventImages.put(event.getId(), event.getImage());
    }

    private void addZones(int event, List<Zone> list) {
        zones.put(event, list);
    }

    private void addSpots(int event, List<Spot> list) {
        spots.put(event, list);
    }


    @Override
    public LiveData<Collection<Event>> getEvents() {
        return events.values();
    }

    @Override
    public LiveData<Event> getEvent(int eventId) {
        return events.get(eventId);
    }

    @Override
    public LiveData<Bitmap> getEventImage(Event event) {
        return eventImages.get(event.getId());
    }

    @Override
    public LiveData<List<Spot>> getSpots(int eventId) {
        return spots.get(eventId);
    }

    @Override
    public LiveData<List<ScheduledItem>> getScheduledItems(int eventId) {
        return scheduledItems.get(eventId);
    }

    @Override
    public LiveData<List<Zone>> getZones(int eventId) {
        return zones.get(eventId);
    }

    @Override
    public LiveData<Bitmap> getSpotImage(Spot spot) {
        return null;
    }

    @Override
    public Task<Event> createEvent(Event event) {
        event.setId(CURRENT_EVENT_ID++);
        return updateEvent(event);
    }

    @Override
    public Task<Event> updateEvent(Event event) {
        events.put(event.getId(), event);
        return Tasks.call(() -> event);
    }

    @Override
    public Task<Boolean> addUserToEvent(String email, int eventId, String role) {
        Event ev = events.get(eventId).getValue();
        if (ev == null)
            return Tasks.call(() -> false);

        if (!ev.getUsers().containsKey(role))
            ev.getUsers().put(role, new HashMap<>());

        ev.getUsers().get(role).put(String.valueOf(System.currentTimeMillis()), email);
        events.put(eventId, ev);

        return Tasks.call(() -> true);
    }
}
