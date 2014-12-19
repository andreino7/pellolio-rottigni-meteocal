/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.Event;
import java.util.Date;
import java.util.List;
import it.polimi.meteocal.entity.EventCalendar;
import it.polimi.meteocal.entity.InviteNotification;
import it.polimi.meteocal.entity.User;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Filippo
 */
@Stateless
public class EventManager {
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private UserManager userManager;
    
    public Event findEventForId(String id) {
        if (id != null) {
            return em.find(Event.class, Integer.parseInt(id));
        } else {
            return null;
        }
    }
    
    public Event findEventForId(Integer id) {
        if (id != null) {
            return em.find(Event.class, id);
        } else {
            return null;
        }
    }
    
    public void save(Event e) {
        System.out.println(e);
        em.persist(e);
    }
    
    public void update(Event e) {
        em.merge(e);
    }
    
    public List<Event> findByDay(Date d1, Date d2) {
        System.out.println("d1: " + d1);
        System.out.println("d2: " + d2);
        Query q = em.createNamedQuery("Event.findByDay");
        q.setParameter("date1", d1);
        q.setParameter("date2", d2);
        List<Event> prova = q.getResultList();
        List<Event> events = q.getResultList();
        return events;
    }
    
    public void linkToCalendar(Event e, Calendar c) {
        EventCalendar ec = new EventCalendar();
        ec.setEvent(e);
        ec.setCalendar(c);
        em.persist(ec);
    }
    
    public void toggleLink(Event e, Calendar c) {
        EventCalendar ec = em.createNamedQuery(EventCalendar.findEventCalendarForEventAndCalendar, EventCalendar.class).setParameter("event", e.getId()).setParameter("calendar", c.getId()).getSingleResult();
        if (ec != null) {
            em.remove(ec);
        }
    }
    
    public boolean isMyEvent(String id) {
        Event ev = findEventForId(id);
        return ev.getEventOwner().equals(userManager.getLoggedUser());
    }
    
    public List<User> getParticipant(Event e) {
        List<User> ls = (List<User>) em.createNamedQuery(EventCalendar.findParticipant, User.class).setParameter("event", e.getId()).getResultList();
        return ls;
    }
    
    public List<User> getNonParticipant(Event e) {
        List<User> ls = (List<User>) em.createNamedQuery(EventCalendar.findNonParticipant, User.class).setParameter("event", e.getId()).getResultList();
        return ls;
    }
    
    public List<User> getNonParticipantByPart(Event e, String query) {
        List<User> ls = (List<User>) em.createNamedQuery(EventCalendar.findNonParticipantByPart, User.class).setParameter("event", e.getId()).setParameter("part", "%" + query + "%").getResultList();
        return ls;        
    }
    
    public boolean UserAlreadyInvited(Event e, User u) {
        InviteNotification i=null;
        try{
            i=(InviteNotification) em.createNamedQuery(InviteNotification.findByReceiverAndEvent, InviteNotification.class).setParameter("user", u.getEmail()).setParameter("event", e.getId()).getSingleResult();
        }catch(NoResultException ex){
             
        }
        return i!=null;
    }
    
    public void inviteUsersToEvent(List<User> toInvite, Event e) {
        for (User u : toInvite) {
            if (!UserAlreadyInvited(e, u)) {
                InviteNotification invite = new InviteNotification(0, "UNREAD");
                invite.setReceiver(u);
                invite.setSender(userManager.getLoggedUser());
                invite.setAbout(e);
                em.persist(invite);
            }
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
