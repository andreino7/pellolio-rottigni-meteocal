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
    private EntityManager em;

    public Calendar findCalendarForId(String id) {
        if (id != null) {
            System.out.println(id);
            return em.find(Calendar.class, Integer.parseInt(id));
        } else {
            return null;
        }
    }
    
    public List<Calendar> findCalendarForUser(User user) {
        return em.createNamedQuery("Calendar.findByOwner").setParameter("ownerEmail", user.getEmail()).getResultList();
    }
    
    public void save(Calendar c){
        em.persist(c);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}