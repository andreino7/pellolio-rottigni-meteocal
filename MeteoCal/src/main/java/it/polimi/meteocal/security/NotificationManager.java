/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

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
         return res;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
