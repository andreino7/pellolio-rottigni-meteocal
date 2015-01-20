/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.boundary.NotificationManager;
import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.entity.ChangedEventNotification;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.interfaces.Notification;
import it.polimi.meteocal.notification.NotificationStatus;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 *
 * @author Filippo
 */
public class NotificationBeanTest {

    NotificationBean nb;

    @Before
    public void setUp() {
        nb = new NotificationBean();
        nb.notificationManager = mock(NotificationManager.class);
        nb.userManager = mock(UserManager.class);

        User u = new User("aa@b.c");
        when(nb.userManager.getLoggedUser()).thenReturn(u);
        List<Notification> ls = new LinkedList<>();
        when(nb.notificationManager.getNotificationForUser(u)).thenReturn(ls);

        nb.postConstruct();
    }

    @Test
    public void goodInit() {
        Assert.assertEquals(nb.getUnReadSeenNotificationNumber(), 0);
        Assert.assertNotNull(nb.getModel());
    }

    @Test
    public void notifNumber() {
        User u = new User("aa@b.c");
        when(nb.userManager.getLoggedUser()).thenReturn(u);
        List<Notification> ls = new LinkedList<>();
        ChangedEventNotification n= new ChangedEventNotification(1, NotificationStatus.UNREADSEEN.toString());
        n.setAbout(new Event(1));
        ls.add(n);
        when(nb.notificationManager.getNotificationForUser(u)).thenReturn(ls);

        nb.postConstruct();
        
        Assert.assertEquals(nb.getUnReadSeenNotificationNumber(), 1);
    }
}
