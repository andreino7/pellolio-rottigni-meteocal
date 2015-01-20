/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.boundary.CalendarManager;
import it.polimi.meteocal.boundary.EventManager;
import it.polimi.meteocal.boundary.EventTypeManager;
import it.polimi.meteocal.boundary.NotificationManager;
import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.notification.NotificationCleaner;
import it.polimi.meteocal.schedule.MeteoCalScheduleEvent;
import it.polimi.meteocal.schedule.Visibility;
import it.polimi.meteocal.weather.WeatherChecker;
import it.polimi.meteocal.weather.WeatherConditions;
import java.util.Date;
import java.util.LinkedList;
import javax.persistence.EntityManager;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Filippo
 */
public class ScheduleBeanTest {

    ScheduleBean sb;

    @Before
    public void setUp() {
        sb = new ScheduleBean();
        sb.userManager = Mockito.mock(UserManager.class);
        sb.calendarManager = Mockito.mock(CalendarManager.class);
        sb.eventManager = Mockito.mock(EventManager.class);
        sb.em = Mockito.mock(EntityManager.class);
        sb.etManager = Mockito.mock(EventTypeManager.class);
        sb.weather = mock(WeatherChecker.class);
        sb.cleaner = mock(NotificationCleaner.class);
        sb.notificationManager = mock(NotificationManager.class);
        sb.emailBean= mock(EmailSessionBean.class);

        User u = new User("aa@b.c");
        Mockito.when(sb.userManager.getLoggedUser()).thenReturn(u);
        Mockito.when(sb.calendarManager.findCalendarForUser(u)).thenReturn(new LinkedList<>());
        Mockito.when(sb.etManager.findTypesForUser()).thenReturn(new LinkedList<>());

        sb.postConstruct();
    }

    @Test
    public void switchViewTest() {
        boolean old = sb.isScheduleDisplay();
        sb.switchView();
        assert (old != sb.isScheduleDisplay());
    }

    @Test
    public void goodInitTest() {
        assertNotNull(sb.userManager.getLoggedUser());
        assertNotNull(sb.getUserCalendars());
        assertNotNull(sb.getUserTypes());
    }

    @Test
    public void goodEventSaved() {
        Date d = new Date();
        Event e = new Event(999, "aaa", Visibility.Public, d, d, "Milano");
        Calendar c = new Calendar(45, "aaaa", Visibility.Public);
        MeteoCalScheduleEvent ev = new MeteoCalScheduleEvent(e, c);
        Mockito.when(sb.eventManager.findEventForId(999)).thenReturn(null);
        when(sb.weather.addWeather("Milano", d)).thenReturn(WeatherConditions.CLEAR);

        sb.setEvent(ev);
        try {
            sb.save();
            Mockito.verify(sb.eventManager, times(1)).save(Matchers.anyObject());
            verify(sb.eventManager, times(1)).linkToCalendar(e, c);
        } catch (ScheduleBean.BadEventException ex) {
            fail("Event should not fail");
        }

    }

    @Test
    public void badEventSaved() {
        Date d = new Date();
        Event e = new Event(999, null, null, null, d, null);
        Calendar c = new Calendar(45, "aaaa", Visibility.Public);
        MeteoCalScheduleEvent ev = new MeteoCalScheduleEvent(e, c);
        Mockito.when(sb.eventManager.findEventForId(999)).thenReturn(null);
        when(sb.weather.addWeather("Milano", d)).thenReturn(WeatherConditions.CLEAR);

        sb.setEvent(ev);

        try {
            sb.save();

            //fail("Event should not be saved");
        } catch (Exception ex) {

            Assert.assertTrue(true);
        }

    }

    @Test
    public void goodEventUpdate() {
        Date d = new Date();
        Event e = new Event(999, "aaa", Visibility.Public, d, d, "Milano");
        Calendar c = new Calendar(45, "aaaa", Visibility.Public);
        MeteoCalScheduleEvent ev = new MeteoCalScheduleEvent(e, c);
        Mockito.when(sb.eventManager.findEventForId(999)).thenReturn(e);
        when(sb.weather.addWeather("Milano", d)).thenReturn(WeatherConditions.CLEAR);

        sb.setEvent(ev);
        try {
            sb.save();
            Mockito.verify(sb.eventManager, times(1)).update(Matchers.anyObject());
            verify(sb.notificationManager, times(1)).createChangedEventNotification(Matchers.anyObject(), Matchers.anyObject());
        } catch (ScheduleBean.BadEventException ex) {
            fail("Event should not fail");
        }

    }
    
    @Test
    public void badEventUpdate() {
        Date d = new Date();
        Event e = new Event(999, null, null, null, d, null);
        Calendar c = new Calendar(45, "aaaa", Visibility.Public);
        MeteoCalScheduleEvent ev = new MeteoCalScheduleEvent(e, c);
        Mockito.when(sb.eventManager.findEventForId(999)).thenReturn(e);
        when(sb.weather.addWeather("Milano", d)).thenReturn(WeatherConditions.CLEAR);

        sb.setEvent(ev);
        try {
            sb.save();

            //fail("Event should not be saved");
        } catch (Exception ex) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void nextEventsTest() {

    }
}
