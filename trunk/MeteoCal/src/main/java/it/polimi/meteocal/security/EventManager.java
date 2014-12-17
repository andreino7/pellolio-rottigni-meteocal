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
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Filippo
 */
@Stateless
public class EventManager {



    @PersistenceContext
    private EntityManager em;

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
    
    public void save(Event e){
        System.out.println(e);
        em.persist(e);
    }
    
    public void update(Event e){
        em.merge(e);
    }
    
    public List<Event> findByDay(Date d1, Date d2) {
        Query q = em.createNamedQuery("Event.findByDay");
        q.setParameter("date1", d1);
        q.setParameter("date2", d2);
        List<Event> events = q.getResultList();
        return events;
    }

    public void linkToCalendar(Event e,Calendar c){
        EventCalendar ec= new EventCalendar();
        ec.setEvent(e);
        ec.setCalendar(c);
        em.persist(ec);
    }
    
    public void toggleLink(Event e,Calendar c){
        EventCalendar ec=em.createNamedQuery(EventCalendar.findEventCalendarForEventAndCalendar,EventCalendar.class).setParameter("event", e.getId()).setParameter("calendar", c.getId()).getSingleResult();
        if (ec!=null){
            em.remove(ec);
        }
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
