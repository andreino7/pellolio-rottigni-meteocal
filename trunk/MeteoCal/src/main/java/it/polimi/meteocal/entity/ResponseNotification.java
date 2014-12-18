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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "ResponseNotification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResponseNotification.findAll", query = "SELECT r FROM ResponseNotification r"),
    @NamedQuery(name = "ResponseNotification.findById", query = "SELECT r FROM ResponseNotification r WHERE r.id = :id"),
    @NamedQuery(name = "ResponseNotification.findByReceiver", query = "SELECT w FROM ResponseNotification w WHERE w.receiver.email = :user"),

    @NamedQuery(name = "ResponseNotification.findByState", query = "SELECT r FROM ResponseNotification r WHERE r.state = :state")})
public class ResponseNotification implements Serializable,Notification {
    @Basic(optional = false)
    @NotNull
    @Column(name = "Answer")
    private boolean answer;
    public static final String findByReceiver= "ResponseNotification.findByReceiver";
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
    @JoinColumn(name = "Refers", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private InviteNotification refers;
    @JoinColumn(name = "Sender", referencedColumnName = "Email")
    @ManyToOne(optional = false)
    private User sender;

    public ResponseNotification() {
    }

    public ResponseNotification(Integer id) {
        this.id = id;
    }

    public ResponseNotification(Integer id, String state, boolean answer) {
        this.id = id;
        this.state = state;
        this.answer = answer;
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

    public InviteNotification getRefers() {
        return refers;
    }

    public void setRefers(InviteNotification refers) {
        this.refers = refers;
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
        if (!(object instanceof ResponseNotification)) {
            return false;
        }
        ResponseNotification other = (ResponseNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entity.ResponseNotification[ id=" + id + " ]";
    }



    @Override
    public String getText() {
        
       
        
        return  sender.getTitle()+" has answered: "+(answer?"Yes":"NO") +" to your invite at the event: "+about.getTitle();
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
    
}
