/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entity;

import it.polimi.meteocal.security.SearchResult;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Filippo
 */
@Entity
@Table(name = "Event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByTitle", query = "SELECT e FROM Event e WHERE e.title = :title"),
    @NamedQuery(name = "Event.findByPartOfTitle", query = "SELECT e FROM Event e WHERE e.title LIKE :part AND e.visibility='public'"),
    @NamedQuery(name = "Event.findByVisibility", query = "SELECT e FROM Event e WHERE e.visibility = :visibility"),
    @NamedQuery(name = "Event.findByDate", query = "SELECT e FROM Event e WHERE e.date = :date"),
    @NamedQuery(name = "Event.findByEndDate", query = "SELECT e FROM Event e WHERE e.endDate = :endDate"),
    @NamedQuery(name = "Event.findByLocation", query = "SELECT e FROM Event e WHERE e.location = :location"),
    @NamedQuery(name = "Event.findByDay", query = "SELECT e FROM Event e WHERE e.date >= :date1 AND e.date < :date2") })
public class Event implements Serializable,SearchResult {
    @JoinColumn(name = "EventOwner", referencedColumnName = "Email")
    @ManyToOne(optional = false)
    private User eventOwner;
    @Size(max = 45)
    @Column(name = "Weather")
    private String weather;
    public static final String findByPartOfTitle="Event.findByPartOfTitle";
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "about")
    private Collection<WeatherNotification> weatherNotificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "about")
    private Collection<ResponseNotification> responseNotificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "about")
    private Collection<AdminNotification> adminNotificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "about")
    private Collection<ChangedEventNotification> changedEventNotificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "about")
    private Collection<InviteNotification> inviteNotificationCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Visibility")
    private String visibility;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EndDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "Location")
    private String location;
    @JoinColumn(name = "Type", referencedColumnName = "ID")
    @ManyToOne
    private EventType type;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Collection<EventCalendar> eventCalendarCollection;

    public Event() {
    }

    public Event(Integer id) {
        this.id = id;
    }

    public Event(Integer id, String title, String visibility, Date date, Date endDate, String location) {
        this.id = id;
        this.title = title;
        this.visibility = visibility;
        this.date = date;
        this.endDate = endDate;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override 
    public String getTitle() {
        return title; 
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @XmlTransient
    public Collection<EventCalendar> getEventCalendarCollection() {
        return eventCalendarCollection;
    }

    public void setEventCalendarCollection(Collection<EventCalendar> eventCalendarCollection) {
        this.eventCalendarCollection = eventCalendarCollection;
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Event: id :"+id+"  title: "+title+"  " + getDate();
    }

    @XmlTransient
    public Collection<WeatherNotification> getWeatherNotificationCollection() {
        return weatherNotificationCollection;
    }

    public void setWeatherNotificationCollection(Collection<WeatherNotification> weatherNotificationCollection) {
        this.weatherNotificationCollection = weatherNotificationCollection;
    }

    @XmlTransient
    public Collection<ResponseNotification> getResponseNotificationCollection() {
        return responseNotificationCollection;
    }

    public void setResponseNotificationCollection(Collection<ResponseNotification> responseNotificationCollection) {
        this.responseNotificationCollection = responseNotificationCollection;
    }

    @XmlTransient
    public Collection<AdminNotification> getAdminNotificationCollection() {
        return adminNotificationCollection;
    }

    public void setAdminNotificationCollection(Collection<AdminNotification> adminNotificationCollection) {
        this.adminNotificationCollection = adminNotificationCollection;
    }

    @XmlTransient
    public Collection<ChangedEventNotification> getChangedEventNotificationCollection() {
        return changedEventNotificationCollection;
    }

    public void setChangedEventNotificationCollection(Collection<ChangedEventNotification> changedEventNotificationCollection) {
        this.changedEventNotificationCollection = changedEventNotificationCollection;
    }

    @XmlTransient
    public Collection<InviteNotification> getInviteNotificationCollection() {
        return inviteNotificationCollection;
    }

    public void setInviteNotificationCollection(Collection<InviteNotification> inviteNotificationCollection) {
        this.inviteNotificationCollection = inviteNotificationCollection;
    }



    @Override
    public String getGroup() {
        return "Event";
    }

    @Override
    public String getPrime() {
        return String.valueOf(this.id);
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public User getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(User eventOwner) {
        this.eventOwner = eventOwner;
    }

   
}
