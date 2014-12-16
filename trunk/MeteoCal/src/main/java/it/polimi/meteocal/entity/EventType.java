/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entity;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "EventType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventType.findAll", query = "SELECT e FROM EventType e"),
    @NamedQuery(name = "EventType.findById", query = "SELECT e FROM EventType e WHERE e.id = :id"),
    @NamedQuery(name = "EventType.findAllTypesForUser", query = "SELECT e FROM EventType e WHERE e.personalized=0 OR e.owner.email= :user"),
    @NamedQuery(name = "EventType.findByTitle", query = "SELECT e FROM EventType e WHERE e.title = :title")})
    

public class EventType implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "Sun")
    private byte[] sun;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "Rain")
    private byte[] rain;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "Snow")
    private byte[] snow;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "Cloud")
    private byte[] cloud;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "Personalized")
    private byte[] personalized;
    public static final String findAllTypesForUser="EventType.findAllTypesForUser";
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
    @JoinColumn(name = "Owner", referencedColumnName = "Email")
    @ManyToOne
    private User owner;
    @OneToMany(mappedBy = "type")
    private Collection<Event> eventCollection;

    public EventType() {
    }

    public EventType(Integer id) {
        this.id = id;
    }

    public EventType(Integer id, String title, byte[] sun, byte[] rain, byte[] snow, byte[] cloud, byte[] personalized) {
        this.id = id;
        this.title = title;
        this.sun = sun;
        this.rain = rain;
        this.snow = snow;
        this.cloud = cloud;
        this.personalized = personalized;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @XmlTransient
    public Collection<Event> getEventCollection() {
        return eventCollection;
    }

    public void setEventCollection(Collection<Event> eventCollection) {
        this.eventCollection = eventCollection;
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
        if (!(object instanceof EventType)) {
            return false;
        }
        EventType other = (EventType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entity.EventType[ id=" + id + " ]";
    }

    public byte[] getSun() {
        return sun;
    }

    public void setSun(byte[] sun) {
        this.sun = sun;
    }

    public byte[] getRain() {
        return rain;
    }

    public void setRain(byte[] rain) {
        this.rain = rain;
    }

    public byte[] getSnow() {
        return snow;
    }

    public void setSnow(byte[] snow) {
        this.snow = snow;
    }

    public byte[] getCloud() {
        return cloud;
    }

    public void setCloud(byte[] cloud) {
        this.cloud = cloud;
    }

    public byte[] getPersonalized() {
        return personalized;
    }

    public void setPersonalized(byte[] personalized) {
        this.personalized = personalized;
    }
    
}