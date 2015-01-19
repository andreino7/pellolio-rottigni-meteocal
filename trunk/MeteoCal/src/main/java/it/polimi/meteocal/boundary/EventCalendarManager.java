/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Andrea
 */
@Stateless
public class EventCalendarManager {

    @PersistenceContext
    private EntityManager em;
    
    public void save(EventCalendar ec) {
        em.persist(ec);
    }
    
    public List<Event> findAllEventForCalendars(List<Calendar> calendars) {
        List<Event> events = new LinkedList<>();
        for (Calendar c: calendars) {
           events.addAll(em.createNamedQuery(EventCalendar.findEventsForCalendar).setParameter("calendar", c.getId()).getResultList());
        }
        return events;
    }
}
