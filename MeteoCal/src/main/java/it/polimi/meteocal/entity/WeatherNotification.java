/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entity;

import it.polimi.meteocal.security.Notification;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Filippo
 */
@Entity
@Table(name = "WeatherNotification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeatherNotification.findAll", query = "SELECT w FROM WeatherNotification w"),
    @NamedQuery(name = "WeatherNotification.findById", query = "SELECT w FROM WeatherNotification w WHERE w.id = :id"),
    @NamedQuery(name = "WeatherNotification.findByReceiver", query = "SELECT w FROM WeatherNotification w WHERE w.receiver.email = :user"),

    @NamedQuery(name = "WeatherNotification.findByState", query = "SELECT w FROM WeatherNotification w WHERE w.state = :state")})
public class WeatherNotification implements Serializable,Notification {
    public static final String findByReceiver= "WeatherNotification.findByReceiver";
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "State")
    private String state;
    @JoinColumn(name = "About", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Event about;
    @JoinColumn(name = "Receiver", referencedColumnName = "Email")
    @ManyToOne(optional = false)
    private User receiver;

    public WeatherNotification() {
    }

    public WeatherNotification(Integer id) {
        this.id = id;
    }

    public WeatherNotification(Integer id, String state) {
        this.id = id;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

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
    public String getText() {
        return "The forecasted weather conditions aren't suitable for your event: "+about.getTitle()+" ";
    }
    
}
