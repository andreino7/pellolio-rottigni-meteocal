/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.boundary.CalendarManager;
import it.polimi.meteocal.boundary.EventCalendarManager;
import it.polimi.meteocal.boundary.EventManager;
import it.polimi.meteocal.boundary.EventTypeManager;
import it.polimi.meteocal.boundary.NotificationManager;
import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.entity.AdminNotification;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.ChangedEventNotification;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.entity.InviteNotification;
import it.polimi.meteocal.entity.ResponseNotification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.WeatherNotification;
import it.polimi.meteocal.interfaces.Notification;
import it.polimi.meteocal.notification.NotificationType;
import it.polimi.meteocal.schedule.Visibility;
import it.polimi.meteocal.weather.DateManipulator;
import it.polimi.meteocal.weather.Forecast;
import it.polimi.meteocal.weather.WeatherChecker;
import it.polimi.meteocal.weather.WeatherConditions;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Andrea
 */
public class EventPageBeanTest {
/*
    private EventPageBean pageBean;
    private HttpServletRequest request;
    private final String param = "1";
    private Event defaultEvent;
    private FacesContext context;
    private final List<Forecast> forecasts;
    private User defaultUser;

    public EventPageBeanTest() {
        this.forecasts = new LinkedList<>();
    }

    @Before
    public void setUp() {
        defaultUser = new User();
        pageBean = new EventPageBean();
        pageBean.calendarManager = mock(CalendarManager.class);
        pageBean.ecManager = mock(EventCalendarManager.class);
        pageBean.eventManager = mock(EventManager.class);
        pageBean.eventTypeManager = mock(EventTypeManager.class);
        pageBean.notifManager = mock(NotificationManager.class);
        pageBean.userManager = mock(UserManager.class);
        pageBean.weather = mock(WeatherChecker.class);
        request = mock(HttpServletRequest.class);
        context = ContextMocker.mockFacesContext();
        defaultEvent = new Event(1, "testing event", Visibility.Private, addDays(new Date(), 1), addDays(new Date(), 2), "Prevalle,IT");
        ExternalContext extContext = mock(ExternalContext.class);
        when(context.getExternalContext()).thenReturn(extContext);
        when(extContext.getRequest()).thenReturn(request);
        when(request.getParameter("id")).thenReturn("1");
        when(pageBean.eventManager.findEventForId("1")).thenReturn(defaultEvent);
        when(pageBean.weather.getWeatherForecast("Prevalle,IT")).thenReturn(forecasts);
        when(pageBean.eventManager.isMyEvent("1")).thenReturn(Boolean.TRUE);
        when(pageBean.userManager.getLoggedUser()).thenReturn(defaultUser);
        List<User> part = new ArrayList<>();
        part.add(defaultUser);
        when(pageBean.eventManager.getParticipant(defaultEvent)).thenReturn(part);
        initForecasts();
    }

    @After
    public void tearDown() {
        context.release();
    }

    @Test
    public void testPostConstructFromWeatherNotification() {
        WeatherNotification not = mock(WeatherNotification.class);
        Date d = addDays(new Date(), 3);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("notificationType")).thenReturn("WEATHER");
        when(request.getParameter("notificationID")).thenReturn("1");
        when(pageBean.notifManager.findNotificationByIdAndType("1", NotificationType.WEATHER)).thenReturn(not);
        when(pageBean.eventManager.isMyEvent(param)).thenReturn(true);
        List<User> partecipants = new LinkedList<>();
        partecipants.add(new User());
        when(pageBean.eventManager.getParticipant(defaultEvent)).thenReturn(partecipants);
        when(pageBean.eventTypeManager.findTypesForUser()).thenReturn(null);
        when(not.getSuggestedDate()).thenReturn(d);
        pageBean.postConstruct();
        assertEquals(defaultEvent, pageBean.getEvent());
        assertEquals(d, pageBean.getSuggestedDate());
        assertTrue(pageBean.isThereSuggestedDate());
        assertTrue(pageBean.isOwnedEvent());
        assertTrue(pageBean.isWeatherNotification());
        assertFalse(pageBean.isChangedEventNotification());
        assertFalse(pageBean.isAdminNotification());
        assertFalse(pageBean.isInviteNotification());
        assertFalse(pageBean.isResponseNotification());
        for (Forecast f: forecasts) {
            if (DateManipulator.toDefaultDate(f.getDate()).equals(DateManipulator.toDefaultDate(pageBean.getSuggestedDate()))) {
                assertEquals(f.getCondition(), pageBean.getSuggestedWeather().getCondition());
            }
        }
    }

    @Test
    public void testPostConstructFromAdminNotification() {
        AdminNotification not = mock(AdminNotification.class);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("notificationType")).thenReturn("ADMIN");
        when(request.getParameter("notificationID")).thenReturn("1");
        when(pageBean.notifManager.findNotificationByIdAndType("1", NotificationType.ADMIN)).thenReturn(not);
        when(pageBean.eventManager.isMyEvent(param)).thenReturn(true);
        List<User> partecipants = new LinkedList<>();
        partecipants.add(new User());
        when(pageBean.eventManager.getParticipant(defaultEvent)).thenReturn(partecipants);
        when(pageBean.eventTypeManager.findTypesForUser()).thenReturn(null);
        pageBean.postConstruct();
        assertEquals(defaultEvent, pageBean.getEvent());
        assertNull(pageBean.getSuggestedDate());
        assertFalse(pageBean.isThereSuggestedDate());
        assertTrue(pageBean.isOwnedEvent());
        assertFalse(pageBean.isWeatherNotification());
        assertFalse(pageBean.isChangedEventNotification());
        assertTrue(pageBean.isAdminNotification());
        assertFalse(pageBean.isInviteNotification());
        assertFalse(pageBean.isResponseNotification());
    }

    @Test
    public void testPostConstructFromChangedEventNotification() {
        ChangedEventNotification not = mock(ChangedEventNotification.class);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("notificationType")).thenReturn("CHANGED");
        when(request.getParameter("notificationID")).thenReturn("1");
        when(pageBean.notifManager.findNotificationByIdAndType("1", NotificationType.CHANGED)).thenReturn(not);
        when(pageBean.eventManager.isMyEvent(param)).thenReturn(true);
        List<User> partecipants = new LinkedList<>();
        partecipants.add(new User());
        when(pageBean.eventManager.getParticipant(defaultEvent)).thenReturn(partecipants);
        when(pageBean.eventTypeManager.findTypesForUser()).thenReturn(null);
        pageBean.postConstruct();
        assertEquals(defaultEvent, pageBean.getEvent());
        assertNull(pageBean.getSuggestedDate());
        assertFalse(pageBean.isThereSuggestedDate());
        assertTrue(pageBean.isOwnedEvent());
        assertFalse(pageBean.isWeatherNotification());
        assertTrue(pageBean.isChangedEventNotification());
        assertFalse(pageBean.isAdminNotification());
        assertFalse(pageBean.isInviteNotification());
        assertFalse(pageBean.isResponseNotification());
    }

    @Test
    public void testPostConstructFromInviteNotification() {
        InviteNotification not = mock(InviteNotification.class);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("notificationType")).thenReturn("INVITE");
        when(request.getParameter("notificationID")).thenReturn("1");
        when(pageBean.notifManager.findNotificationByIdAndType("1", NotificationType.INVITE)).thenReturn(not);
        when(pageBean.eventManager.isMyEvent(param)).thenReturn(true);
        List<User> partecipants = new LinkedList<>();
        partecipants.add(new User());
        when(pageBean.eventManager.getParticipant(defaultEvent)).thenReturn(partecipants);
        when(pageBean.eventTypeManager.findTypesForUser()).thenReturn(null);
        pageBean.postConstruct();
        assertEquals(defaultEvent, pageBean.getEvent());
        assertNull(pageBean.getSuggestedDate());
        assertFalse(pageBean.isThereSuggestedDate());
        assertTrue(pageBean.isOwnedEvent());
        assertFalse(pageBean.isWeatherNotification());
        assertFalse(pageBean.isChangedEventNotification());
        assertFalse(pageBean.isAdminNotification());
        assertTrue(pageBean.isInviteNotification());
        assertFalse(pageBean.isResponseNotification());
    }

    @Test
    public void testPostConstructFromResponseNotification() {
        ResponseNotification not = mock(ResponseNotification.class);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("notificationType")).thenReturn("RESPONSE");
        when(request.getParameter("notificationID")).thenReturn("1");
        when(pageBean.notifManager.findNotificationByIdAndType("1", NotificationType.RESPONSE)).thenReturn(not);
        when(pageBean.eventManager.isMyEvent(param)).thenReturn(true);
        List<User> partecipants = new LinkedList<>();
        partecipants.add(new User());
        when(pageBean.eventManager.getParticipant(defaultEvent)).thenReturn(partecipants);
        when(pageBean.eventTypeManager.findTypesForUser()).thenReturn(null);
        pageBean.postConstruct();
        assertEquals(defaultEvent, pageBean.getEvent());
        assertNull(pageBean.getSuggestedDate());
        assertFalse(pageBean.isThereSuggestedDate());
        assertTrue(pageBean.isOwnedEvent());
        assertFalse(pageBean.isWeatherNotification());
        assertFalse(pageBean.isChangedEventNotification());
        assertFalse(pageBean.isAdminNotification());
        assertFalse(pageBean.isInviteNotification());
        assertTrue(pageBean.isResponseNotification());
    }

    @Test
    public void correctWeatherUpdateTest() {
        when(request.getParameter("id")).thenReturn("1");
        pageBean.postConstruct();
        for (Forecast f : forecasts) {
            if (DateManipulator.toDefaultDate(f.getDate()).equals(DateManipulator.toDefaultDate(defaultEvent.getDate()))) {
                assertEquals(f.getCondition().toUpperCase(), defaultEvent.getWeather());
            }
        }
    }

    @Test
    public void postConstructClickingOnEvent() {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("notificationType")).thenReturn(null);
        when(request.getParameter("notificationID")).thenReturn(null);
        when(pageBean.eventManager.isMyEvent(param)).thenReturn(true);
        pageBean.postConstruct();
        assertFalse(pageBean.isWeatherNotification());
        assertFalse(pageBean.isChangedEventNotification());
        assertFalse(pageBean.isAdminNotification());
        assertFalse(pageBean.isInviteNotification());
        assertFalse(pageBean.isResponseNotification());
        assertFalse(pageBean.isThereSuggestedDate());
        assertTrue(pageBean.isOwnedEvent());
        assertNotNull(pageBean.getForecasts());
        assertNotNull(pageBean.getEvent());
    }

    private Date addDays(Date d, int amount) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(d);
        cal.add(java.util.Calendar.DATE, amount);
        return DateManipulator.toDefaultDate(cal.getTime());
    }

    private void initForecasts() {
        List<WeatherConditions> conditions = Arrays.asList(WeatherConditions.values());
        int index = conditions.size();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            forecasts.add(new Forecast(conditions.get(rand.nextInt(index)), addDays(new Date(), i), i, i));
        }
    } 
*/
}
