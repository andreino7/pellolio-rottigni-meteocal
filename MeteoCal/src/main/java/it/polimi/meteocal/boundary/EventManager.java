/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.Event;
import java.util.Date;
import java.util.List;
import it.polimi.meteocal.entity.EventCalendar;
import it.polimi.meteocal.entity.InviteNotification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.schedule.Visibility;
import it.polimi.meteocal.gui.EmailSessionBean;
import it.polimi.meteocal.notification.NotificationStatus;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
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

    @EJB
    private EmailSessionBean emailBean;
    
    Event selEvent;

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
        List<Event> events = q.getResultList();
        return events;
    }

    public void linkToCalendar(Event e, Calendar c) {
        EventCalendar ec = new EventCalendar();
        ec.setEvent(e);
        ec.setCalendar(c);
        em.persist(ec);
    }

    public boolean invitePermission(Event e) {
        return e.getVisibility().equals(Visibility.Public) || e.getEventOwner().equals(userManager.getLoggedUser());
    }

    public void toggleLink(Event e, Calendar c) {
        EventCalendar ec = em.createNamedQuery(EventCalendar.findEventCalendarForEventAndCalendar, EventCalendar.class).setParameter("event", e.getId()).setParameter("calendar", c.getId()).getSingleResult();
        if (ec != null) {
            em.remove(ec);
        }
    }

    public boolean isMyEvent(String id) {
        Event ev = findEventForId(id);
        if (ev!=null){
        return ev.getEventOwner().equals(userManager.getLoggedUser());
        }
        return false;
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
        InviteNotification i = null;
        try {
            i = (InviteNotification) em.createNamedQuery(InviteNotification.findByReceiverAndEvent, InviteNotification.class).setParameter("user", u.getEmail()).setParameter("event", e.getId()).getSingleResult();
        } catch (NoResultException ex) {

        }
        return i != null;
    }

    public void inviteUsersToEvent(List<User> toInvite, Event e) {
      /*  List<User> toInviteUsers= new LinkedList<>();
        for (String s: toInvite){
            User u1= userManager.findUserforId(s);
            if (u1!=null) {
                toInviteUsers.add(u1);
            }
        }
*/        
        
        for (User u : toInvite) {
            if (!UserAlreadyInvited(e, u)) {
                InviteNotification invite = new InviteNotification(0, NotificationStatus.UNREADSEEN.toString());
                invite.setReceiver(u);
                invite.setSender(userManager.getLoggedUser());
                invite.setAbout(e);
                em.persist(invite);
                emailBean.sendInviteEmail(u.getEmail(), e.getTitle(), userManager.getLoggedUser().getTitle());
            }
        }
    }

    public List<Event> findFutureEventsForCalendars(List<String> calendarsId) {
        List<Event> res = new LinkedList<>();

        if (calendarsId != null) {
            for (String c : calendarsId) {
                res.addAll(em.createNamedQuery(EventCalendar.findFutureEventsForCalendar, Event.class).setParameter("now", java.util.Calendar.getInstance().getTime()).setParameter("calendar", Integer.parseInt(c)).getResultList());
            }
        }
        res.sort(new Comparator<Event>() {

            @Override
            public int compare(Event o1, Event o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        return res;

    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
