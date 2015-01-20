/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.boundary.CalendarManager;
import it.polimi.meteocal.boundary.EventCalendarManager;
import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.schedule.Visibility;
import java.util.Date;
import java.util.LinkedList;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Filippo
 */
public class PublicCalendarBeanTest {

    PublicCalendarBean pb;
    private HttpServletRequest request;
    private final String param = "1";
    private Event defaultEvent;
    private FacesContext context;
    private Calendar defaultCalendar;
    private User u;

    @Before
    public void setUp() {
        pb = new PublicCalendarBean();
        pb.ecManager = mock(EventCalendarManager.class);
        pb.userManager = mock(UserManager.class);
        pb.cm = mock(CalendarManager.class);
        pb.em = mock(EntityManager.class);
        request = mock(HttpServletRequest.class);
        context = ContextMocker.mockFacesContext();
        defaultEvent = new Event(1, "testing event", Visibility.Private, new Date(), new Date(), "Prevalle,IT");
        ExternalContext extContext = mock(ExternalContext.class);
        when(context.getExternalContext()).thenReturn(extContext);
        when(extContext.getRequest()).thenReturn(request);
        when(request.getParameter("id")).thenReturn("aa@b.c");
        u = new User("aa@b.c", "a", "b", 000, "USER", "password");
        defaultCalendar = new Calendar(1, "TestCal", Visibility.Public);
        when(pb.userManager.findUserforId("aa@b.c")).thenReturn(u);
        when(pb.cm.findPublicCalendarForUser(Matchers.anyObject())).thenReturn(new LinkedList<>());
        when(pb.cm.findCalendarForUser(Matchers.anyObject())).thenReturn(new LinkedList<>());
        pb.postConstruct();
    }

    @Test
    public void goodInit() {
        Assert.assertNotNull(pb.getLoggedUserCalendars());
        Assert.assertNotNull(pb.getModel());
        Assert.assertNotNull(pb.getUser());
    }

}
