/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entity;

import it.polimi.meteocal.security.SearchResult;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Filippo
 */
@Entity
@Table(name = "User")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByPartOfEmail", query = "SELECT u FROM User u WHERE u.name LIKE :part OR u.surname LIKE :part OR u.email LIKE :part"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
    @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"),
    @NamedQuery(name = "User.findByProfilePhoto", query = "SELECT u FROM User u WHERE u.profilePhoto = :profilePhoto"),
    @NamedQuery(name = "User.findByPhoneNumber", query = "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "User.findByClearance", query = "SELECT u FROM User u WHERE u.clearance = :clearance"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")})
public class User implements Serializable,SearchResult {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    private Collection<WeatherNotification> weatherNotificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    private Collection<ResponseNotification> responseNotificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    private Collection<ResponseNotification> responseNotificationCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    private Collection<AdminNotification> adminNotificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    private Collection<ChangedEventNotification> changedEventNotificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    private Collection<InviteNotification> inviteNotificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    private Collection<InviteNotification> inviteNotificationCollection1;
    public static final String findByPartOfEmail="User.findByPartOfEmail";
    public static final String findAll="User.findAll";

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "Clearance")
    private String clearance;
    @OneToMany(mappedBy = "owner")
    private Collection<EventType> eventTypeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Collection<Calendar> calendarCollection;
    private static final long serialVersionUID = 1L;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "Surname")
    private String surname;
    @Size(max = 70)
    @Column(name = "ProfilePhoto")
    private String profilePhoto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PhoneNumber")
    private int phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "Password")
    private String password;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public User(String email, String name, String surname, int phoneNumber, String clearance, String password) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.clearance = clearance;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entity.User[ email=" + email + " ]";
    }

    public String getClearance() {
        return clearance;
    }

    public void setClearance(String clearance) {
        this.clearance = clearance;
    }

    @XmlTransient
    public Collection<EventType> getEventTypeCollection() {
        return eventTypeCollection;
    }

    public void setEventTypeCollection(Collection<EventType> eventTypeCollection) {
        this.eventTypeCollection = eventTypeCollection;
    }

    @XmlTransient
    public Collection<Calendar> getCalendarCollection() {
        return calendarCollection;
    }

    public void setCalendarCollection(Collection<Calendar> calendarCollection) {
        this.calendarCollection = calendarCollection;
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
    public Collection<ResponseNotification> getResponseNotificationCollection1() {
        return responseNotificationCollection1;
    }

    public void setResponseNotificationCollection1(Collection<ResponseNotification> responseNotificationCollection1) {
        this.responseNotificationCollection1 = responseNotificationCollection1;
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

    @XmlTransient
    public Collection<InviteNotification> getInviteNotificationCollection1() {
        return inviteNotificationCollection1;
    }

    public void setInviteNotificationCollection1(Collection<InviteNotification> inviteNotificationCollection1) {
        this.inviteNotificationCollection1 = inviteNotificationCollection1;
    }

    @Override
    public String getTitle() {
        return this.name+" "+this.surname;
    }

 

    @Override
    public String getPrime() {
        return this.email;
    }

    @Override
    public String getGroup() {
return "User";    }
    
}
