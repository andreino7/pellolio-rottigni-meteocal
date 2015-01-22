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
 * @author Filippo
 */
@Entity
@Table(name = "AdminNotification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdminNotification.findAll", query = "SELECT a FROM AdminNotification a"),
    @NamedQuery(name = "AdminNotification.findById", query = "SELECT a FROM AdminNotification a WHERE a.id = :id"),
    @NamedQuery(name = "AdminNotification.findByReceiver", query = "SELECT w FROM AdminNotification w WHERE w.receiver.email = :user"),    
    @NamedQuery(name = "AdminNotification.findByAbout", query = "SELECT a FROM AdminNotification a WHERE a.about.id = :event"),    
    @NamedQuery(name = "AdminNotification.findByState", query = "SELECT a FROM AdminNotification a WHERE a.state = :state")})
public class AdminNotification implements Serializable,Notification {
    @Basic(optional = false)
    @NotNull
    @Column(name = "CreationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private final Date creationDate;
    public static final String findByReceiver= "AdminNotification.findByReceiver";
    public static final String findByAbout= "AdminNotification.findByAbout";   
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
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

    public AdminNotification() {
        this.creationDate = new Date();        
    }

    public AdminNotification(Integer id) {
        this.creationDate = new Date();
        this.id = id;
    }

    public AdminNotification(Integer id, String state) {
        this.creationDate = new Date();
        this.id = id;
        this.state = state;
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
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdminNotification)) {
            return false;
        }
        AdminNotification other = (AdminNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entity.AdminNotification[ id=" + id + " ]";
    }

    @Override
    public String getText() {
        return "Your Event "+about.getTitle()+" has been changed by the admin";
    }

    @Override
    public NotificationType getType() {
        return NotificationType.ADMIN;
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
