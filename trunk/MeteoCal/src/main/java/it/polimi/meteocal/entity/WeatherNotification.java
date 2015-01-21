/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entity;

import it.polimi.meteocal.interfaces.Notification;
import it.polimi.meteocal.notification.NotificationType;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andrea
 */
@Entity
@Table(name = "WeatherNotification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeatherNotification.findAll", query = "SELECT w FROM WeatherNotification w"),
    @NamedQuery(name = "WeatherNotification.findById", query = "SELECT w FROM WeatherNotification w WHERE w.id = :id"),
    @NamedQuery(name = "WeatherNotification.findByState", query = "SELECT w FROM WeatherNotification w WHERE w.state = :state"),
    @NamedQuery(name = "WeatherNotification.findByReceiver", query = "SELECT w FROM WeatherNotification w WHERE w.receiver.email = :user"),
    @NamedQuery(name = "WeatherNotification.findByAbout", query = "SELECT w FROM WeatherNotification w WHERE w.about.id = :event"),
    @NamedQuery(name = "WeatherNotification.findByReceiverAndEvent", query = "SELECT w FROM WeatherNotification w WHERE w.about.id = :event AND w.receiver.email = :user"),
    @NamedQuery(name = "WeatherNotification.findBySuggestedDate", query = "SELECT w FROM WeatherNotification w WHERE w.suggestedDate = :suggestedDate")})
public class WeatherNotification implements Serializable, Notification {
    @Basic(optional = false)
    @NotNull
    @Column(name = "CreationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private final Date creationDate;
    
    public static final String findByReceiver= "WeatherNotification.findByReceiver";
    public static final String findByAbout= "WeatherNotification.findByAbout";
    public static final String findByReceiverAndEvent="WeatherNotification.findByReceiverAndEvent";



    
    @JoinColumn(name = "About", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Event about;
    @JoinColumn(name = "Receiver", referencedColumnName = "Email")
    @ManyToOne(optional = false)
    private User receiver;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 45)
    @Column(name = "State")
    private String state;
    @Column(name = "SuggestedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date suggestedDate;

    public WeatherNotification() {
        this.creationDate = new Date();
    }

    public WeatherNotification(Integer id) {
        this.creationDate = new Date();
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    public Date getSuggestedDate() {
        return suggestedDate;
    }

    public void setSuggestedDate(Date suggestedDate) {
        this.suggestedDate = suggestedDate;
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
        if (!(object instanceof WeatherNotification)) {
            return false;
        }
        WeatherNotification other = (WeatherNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entity.WeatherNotification[ id=" + id + " ]";
    }

    @Override
    public Event getAbout() {
        return about;
    }

    public void setAbout(Event about) {
        this.about = about;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    @Override
    public String getText() {
        if (about.getEventOwner().equals(receiver)) {
            return "For your event " + about.getTitle().toUpperCase() + " is forecasted " + about.getWeather() + ".Click to modify the event" ;
        } else {
            return "For the event " + about.getTitle().toUpperCase() + " is forecasted " + about.getWeather();
        }
    }

    @Override
    public NotificationType getType() {
        return NotificationType.WEATHER;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public int compareTo(Notification o) {
        if (o.getCreationDate().after(creationDate)) {
            return -1;
        } else {
            if (o.getCreationDate().before(creationDate)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    

}
