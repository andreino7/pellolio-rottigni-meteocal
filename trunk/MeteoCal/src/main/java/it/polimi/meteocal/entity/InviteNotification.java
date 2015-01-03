/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entity;

import it.polimi.meteocal.security.Notification;
import it.polimi.meteocal.security.NotificationType;
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
@Table(name = "InviteNotification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InviteNotification.findAll", query = "SELECT i FROM InviteNotification i"),
    @NamedQuery(name = "InviteNotification.findById", query = "SELECT i FROM InviteNotification i WHERE i.id = :id"),
    @NamedQuery(name = "InviteNotification.findByReceiverAndEvent", query = "SELECT i FROM InviteNotification i WHERE i.about.id = :event AND i.receiver.email = :user"),
    @NamedQuery(name = "InviteNotification.findByReceiver", query = "SELECT w FROM InviteNotification w WHERE w.receiver.email = :user"),    
    @NamedQuery(name = "InviteNotification.findByAbout", query = "SELECT i FROM InviteNotification i WHERE i.about.id = :event"),    
    @NamedQuery(name = "InviteNotification.findByState", query = "SELECT i FROM InviteNotification i WHERE i.state = :state")})
public class InviteNotification implements Serializable,Notification {
    @Basic(optional = false)
    @NotNull
    @Column(name = "CreationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private final Date creationDate;
    public static final String findByReceiver= "InviteNotification.findByReceiver";
    public static final String findByAbout= "InviteNotification.findByAbout";
    public static final String findByReceiverAndEvent= "InviteNotification.findByReceiverAndEvent";
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refers")
    private Collection<ResponseNotification> responseNotificationCollection;
    @JoinColumn(name = "About", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Event about;
    @JoinColumn(name = "Receiver", referencedColumnName = "Email")
    @ManyToOne(optional = false)
    private User receiver;
    @JoinColumn(name = "Sender", referencedColumnName = "Email")
    @ManyToOne(optional = false)
    private User sender;

    public InviteNotification() {
        this.creationDate = new Date();
    }

    public InviteNotification(Integer id) {
        this.creationDate = new Date();
        this.id = id;
    }

    public InviteNotification(Integer id, String state) {
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

    @XmlTransient
    public Collection<ResponseNotification> getResponseNotificationCollection() {
        return responseNotificationCollection;
    }

    public void setResponseNotificationCollection(Collection<ResponseNotification> responseNotificationCollection) {
        this.responseNotificationCollection = responseNotificationCollection;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
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
        if (!(object instanceof InviteNotification)) {
            return false;
        }
        InviteNotification other = (InviteNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entity.InviteNotification[ id=" + id + " ]";
    }

    @Override
    public String getText() {
        return "You've been invited to: "+about.getTitle().toUpperCase()+" by: "+sender.getTitle().toUpperCase();
    }

    @Override
    public NotificationType getType() {
        return NotificationType.INVITE;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    
}
