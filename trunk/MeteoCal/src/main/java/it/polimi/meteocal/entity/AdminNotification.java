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
@Table(name = "AdminNotification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdminNotification.findAll", query = "SELECT a FROM AdminNotification a"),
    @NamedQuery(name = "AdminNotification.findById", query = "SELECT a FROM AdminNotification a WHERE a.id = :id"),
    @NamedQuery(name = "AdminNotification.findByReceiver", query = "SELECT w FROM AdminNotification w WHERE w.receiver.email = :user"),    
    @NamedQuery(name = "AdminNotification.findByState", query = "SELECT a FROM AdminNotification a WHERE a.state = :state")})
public class AdminNotification implements Serializable,Notification {
    public static final String findByReceiver= "AdminNotification.findByReceiver";
   
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

    public AdminNotification() {
    }

    public AdminNotification(Integer id) {
        this.id = id;
    }

    public AdminNotification(Integer id, String state) {
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
    
}
