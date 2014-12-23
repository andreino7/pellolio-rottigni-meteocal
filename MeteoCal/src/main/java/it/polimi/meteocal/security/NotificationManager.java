/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.entity.AdminNotification;
import it.polimi.meteocal.entity.ChangedEventNotification;
import it.polimi.meteocal.entity.InviteNotification;
import it.polimi.meteocal.entity.ResponseNotification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.WeatherNotification;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author Filippo
 */
@Stateless
public class NotificationManager {
    
    @PersistenceContext
    private EntityManager em;
    
    public List<Notification> getNotificationForUser(User u){
         List<Notification> res= new LinkedList<>();
         res.addAll(em.createNamedQuery(WeatherNotification.findByReceiver, WeatherNotification.class).setParameter("user", u.getEmail()).getResultList());
         res.addAll(em.createNamedQuery(ResponseNotification.findByReceiver, ResponseNotification.class).setParameter("user", u.getEmail()).getResultList());
         res.addAll(em.createNamedQuery(InviteNotification.findByReceiver, ResponseNotification.class).setParameter("user", u.getEmail()).getResultList());
         res.addAll(em.createNamedQuery(AdminNotification.findByReceiver, ResponseNotification.class).setParameter("user", u.getEmail()).getResultList());
         res.addAll(em.createNamedQuery(ChangedEventNotification.findByReceiver, ResponseNotification.class).setParameter("user", u.getEmail()).getResultList());
         System.out.println(res);
         return res;
    }
    
    public void createWeatherNotification(WeatherNotification notification) {
        em.persist(notification);
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
