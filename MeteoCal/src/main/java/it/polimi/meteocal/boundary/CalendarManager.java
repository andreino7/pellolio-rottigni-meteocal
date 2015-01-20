/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Filippo
 */
@Stateless
public class CalendarManager {

    @PersistenceContext
    EntityManager em;

    public Calendar findCalendarForId(String id) {
        if (id != null) {
            return em.find(Calendar.class, Integer.parseInt(id));
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    public List<Calendar> findCalendarForUser(User user) {
        if (user!=null){
           return em.createNamedQuery("Calendar.findByOwner").setParameter("ownerEmail", user.getEmail()).getResultList();
        }else{
            throw new IllegalArgumentException();
        }
    }
    
    public void save(Calendar c){
        em.persist(c);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
