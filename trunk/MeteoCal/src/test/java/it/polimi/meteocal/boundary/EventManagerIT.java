/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.gui.EmailSessionBean;
import it.polimi.meteocal.gui.LoginBean;
import it.polimi.meteocal.gui.RegistrationBean;
import it.polimi.meteocal.schedule.Visibility;
import it.polimi.meteocal.weather.DateManipulator;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Filippo
 */
@RunWith(Arquillian.class)
public class EventManagerIT {

    @EJB 
    EventManager evm;
    
    @Inject
    RegistrationBean rb;

    @EJB
    CalendarManager cm;
    
    @PersistenceContext
    EntityManager em;
    
    User u1,u2,u3;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManager.class)
                .addClass(RegistrationBean.class)
                .addClass(EventManager.class)
                .addClass(CalendarManager.class)
                .addPackage(User.class.getPackage())
                .addPackage(RegistrationBean.class.getPackage())
                .addAsResource("test-persistence.xml", "/META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Before
    public void populateDB() {
        evm.emailBean= mock(EmailSessionBean.class);
        evm.userManager=mock(UserManager.class);
        
        
        
        
        
    }
    
    @Test
    public void saveAndFindEvent(){
        when(evm.userManager.getLoggedUser()).thenReturn(u1);
        
        String mail = "zz@x.y";
        String password = "password";
        u1 = new User(mail, "aa", "bb", 346, "USER", password);

        rb.setUser(u1);
        try {
            rb.register();
            assertTrue(true);
        } catch (Exception e) {
            fail("User Should be registered");
        }
        
        u2 = new User(mail+"z", "aa", "bb", 346, "USER", password);

        rb.setUser(u2);
        try {
            rb.register();
            assertTrue(true);
        } catch (Exception e) {
            fail("User Should be registered");
        }
        
        u3 = new User(mail+"zz", "aa", "bb", 346, "USER", password);

        rb.setUser(u3);
        try {
            rb.register();
            assertTrue(true);
        } catch (Exception e) {
            fail("User Should be registered");
        }
        
        when(evm.userManager.getLoggedUser()).thenReturn(u1);
        
        
        Event e;
        
        for(int i=-1;i<2;i++){
            e=new Event(0, "Prova", Visibility.Private, addDays(new Date(), i), addDays(new Date(), i), "Milan , IT");
            try{
              evm.save(e);
            }catch(Exception ex){
                fail("EventNotSaved");
            }
        }
       
        List<Event> ls= evm.findByDay(addDays(new Date(), -1), addDays(new Date(), 1));
        
        assertNotNull(ls);
        assertEquals(evm.getNonParticipant(ls.get(0)).size(), 3);
        
        assertEquals(evm.getParticipant(ls.get(0)).size(), 0);
        
        
    }
    
    @Test 
    public void testWithCalendars(){
        Calendar c1= new Calendar(0, "PRova", Visibility.Public);
        
        
        List<User> users= em.createNamedQuery(User.findAll, User.class).getResultList();
        List<Event> events= em.createNamedQuery("Event.findAll", Event.class).getResultList();
        c1.setOwner(users.get(0));
        cm.save(c1);
        
        evm.linkToCalendar(events.get(0), c1);
        
        
        assertEquals(evm.getParticipant(events.get(0)).size(), 1);
        assertEquals(evm.getNonParticipant(events.get(0)).size(), 2);

        assertEquals(evm.findEventForId(events.get(0).getId()), events.get(0));
    }
    
    
    
    
    
    private Date addDays(Date d, int amount) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(d);
        cal.add(java.util.Calendar.DATE, amount);
        return DateManipulator.toDefaultDate(cal.getTime());
    }
}
