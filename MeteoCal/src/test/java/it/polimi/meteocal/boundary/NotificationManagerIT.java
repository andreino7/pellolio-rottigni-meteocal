/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.entity.AdminNotification;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.ChangedEventNotification;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventCalendar;
import it.polimi.meteocal.entity.InviteNotification;
import it.polimi.meteocal.entity.ResponseNotification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.WeatherNotification;
import it.polimi.meteocal.gui.EmailSessionBean;
import it.polimi.meteocal.gui.LoginBean;
import it.polimi.meteocal.gui.NotificationBean;
import it.polimi.meteocal.gui.RegistrationBean;
import it.polimi.meteocal.interfaces.Notification;
import it.polimi.meteocal.notification.NotificationStatus;
import it.polimi.meteocal.schedule.Visibility;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.event.ChangeEvent;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author Andrea
 */
@RunWith(Arquillian.class)
public class NotificationManagerIT {

    @EJB
    NotificationManager cut;

    @PersistenceContext
    EntityManager em;

    @EJB
    UserManager userPopulator;

    @EJB
    EventManager eventManager;

    @EJB
    EventCalendarManager ecManager;

    @EJB
    CalendarManager calMan;

    private User user;
    private Event event;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(NotificationManager.class)
                .addClass(NotificationBean.class)
                .addClass(UserManager.class)
                .addClass(CalendarManager.class)
                .addClass(EventManager.class)
                .addClass(EmailSessionBean.class)
                .addClass(EventCalendarManager.class)
                .addPackage(User.class.getPackage())
                .addPackage(Notification.class.getPackage())
                .addAsResource("test-persistence.xml", "/META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    private User user2;
    private User user3;

    @Test
    public void BeanShouldBeInjected() {
        assertNotNull(cut);
        assertNotNull(userPopulator);
        assertNotNull(eventManager);
        assertNotNull(ecManager);
        assertNotNull(calMan);

    }

    @Test
    public void EntityManagerShouldBeInjected() {
        assertNotNull(em);
    }
    
    

    @Test
    public void testChangeEvent() {
        String mail = "zz@x.y";
        String mail2 = "cc@c.c";
        String mail3 = "aa@a.a";
        String password = "password";
        user = new User(mail, "aa", "bb", 346, "USER", password);
        user2 = new User(mail2, "aa", "bb", 346, "USER", password);
        user3 = new User(mail3, "aa", "bb", 346, "USER", password);
        userPopulator.save(user);
        userPopulator.save(user2);
        userPopulator.save(user3);
        event = new Event(0, "default event", Visibility.Private, new Date(), new Date(), "Prevalle,IT");
        event.setEventOwner(user2);
        eventManager.save(event);
        Calendar cal1 = new Calendar();
        cal1.setOwner(user);
        cal1.setTitle("calendarTest");
        cal1.setVisibility(Visibility.Private);
        calMan.save(cal1);
        Calendar cal2 = new Calendar();
        cal2.setOwner(user2);
        cal2.setTitle("calendarTest2");
        cal2.setVisibility(Visibility.Public);
        calMan.save(cal2);
        Calendar cal3 = new Calendar();
        cal3.setOwner(user3);
        cal3.setTitle("calendarTest3");
        cal3.setVisibility(Visibility.Public);
        calMan.save(cal3);
        EventCalendar evCal1 = new EventCalendar();
        evCal1.setCalendar(cal1);
        evCal1.setEvent(event);
        ecManager.save(evCal1);
        EventCalendar evCal2 = new EventCalendar();
        evCal2.setCalendar(cal2);
        evCal2.setEvent(event);
        ecManager.save(evCal2);
        EventCalendar evCal3 = new EventCalendar();
        evCal3.setCalendar(cal3);
        evCal3.setEvent(event);
        ecManager.save(evCal3);
        List<User> users = eventManager.getParticipant(event);
        cut.createChangedEventNotification(event, users);
        List<Notification> notsUser = cut.getNotificationForUser(user);

        List<Notification> notsUser2 = cut.getNotificationForUser(user2);


        List<Notification> notsUser3 = cut.getNotificationForUser(user3);

        if (existChangeForUser(user, notsUser)) {
            assertTrue(true);
        } else {
            fail("should exist a notification");
        }
        if (!existChangeForUser(user2, notsUser2)) {
            assertTrue(true);
        } else {
            fail("should not exist a notification for the owner");
        }
        if (existChangeForUser(user3, notsUser3)) {
            assertTrue(true);
        } else {
            fail("should exist a notification");
        }
    }

    private boolean existChangeForUser(User us, List<Notification> notsUser) {
        if (event.getEventOwner().equals(us)) {
            for (Notification n : notsUser) {
                if (n instanceof ChangedEventNotification && n.getAbout().equals(event)) {
                    System.out.println(n);
                    return true;
                }
            }
            return false;
        } else {
            for (Notification n : notsUser) {
                if (n instanceof ChangedEventNotification && n.getAbout().equals(event)) {
                    return true;
                }
            }
            return false;
        }
    }
    
/*    @Test
    public void testAllNotificationSaved() {
        WeatherNotification wn = new WeatherNotification();
        wn.setAbout(event);
        wn.setReceiver(user);
        wn.setState(NotificationStatus.UNREADSEEN.toString());
        wn.setSuggestedDate(new Date());
        cut.createWeatherNotification(wn);
        ResponseNotification rn = new ResponseNotification();
        rn.setAbout(event);
        rn.setAnswer(true);
        rn.setReceiver(user2);
        rn.setRefers(no);
        cut.createResponseNotification(null);
    } */

}
