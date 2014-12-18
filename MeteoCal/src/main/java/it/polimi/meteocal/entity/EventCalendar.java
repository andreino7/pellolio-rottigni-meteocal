/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Filippo
 */
@Entity
@Table(name = "EventCalendar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventCalendar.findEventsForUser", query = "SELECT e FROM Event e WHERE e.id IN(SELECT ec.event.id FROM EventCalendar ec WHERE ec.calendar.id IN(SELECT c.id FROM Calendar c WHERE c.owner.email = :userSelected))"),
    @NamedQuery(name = "EventCalendar.findAll", query = "SELECT e FROM EventCalendar e"),
    @NamedQuery(name = "EventCalendar.findParticipant", query = "SELECT u FROM User u WHERE u.email IN( SELECT c.owner.email FROM Calendar c WHERE c.id IN(SELECT ec.calendar.id FROM EventCalendar ec WHERE ec.event.id = :event))"),
    @NamedQuery(name = "EventCalendar.findEventsForCalendars", query = "SELECT ev FROM Event ev WHERE ev.id IN( SELECT e.event.id FROM EventCalendar e WHERE e.calendar.id IN :calendars)"),
    @NamedQuery(name = "EventCalendar.findEventsForCalendar", query = "SELECT ev FROM Event ev WHERE ev.id IN( SELECT e.event.id FROM EventCalendar e WHERE e.calendar.id = :calendar)"),
    @NamedQuery(name = "EventCalendar.findEventCalendarForEventAndCalendar", query = "SELECT e FROM EventCalendar e WHERE e.event.id = :event AND e.calendar.id = :calendar"),
    @NamedQuery(name = "EventCalendar.findById", query = "SELECT e FROM EventCalendar e WHERE e.id = :id")})
public class EventCalendar implements Serializable {
    public static final String findParticipant="EventCalendar.findParticipant";
    public static final String findEventsForUser="EventCalendar.findEventsForUser";
    public static final String findEventsForCalendars="EventCalendar.findEventsForCalendars";
    public static final String findEventsForCalendar="EventCalendar.findEventsForCalendar";
    public static final String findEventCalendarForEventAndCalendar="EventCalendar.findEventCalendarForEventAndCalendar";

    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "Calendar", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Calendar calendar;
    @JoinColumn(name = "Event", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Event event;

    public EventCalendar() {
    }

    public EventCalendar(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventCalendar)) {
            return false;
        }
        EventCalendar other = (EventCalendar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entity.EventCalendar[ id=" + id + " ]";
    }
    
}
