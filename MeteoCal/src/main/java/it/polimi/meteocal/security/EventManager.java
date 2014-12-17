/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.entity.Event;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Filippo
 */
@Stateless
public class EventManager {

    @PersistenceContext
    private EntityManager em;

    public Event findEventForId(String id) {
        if (id != null) {
            return em.find(Event.class, Integer.parseInt(id));
        } else {
            return null;
        }
    }
    
    public Event findEventForId(Integer id) {
        if (id != null) {
            return em.find(Event.class, id);
        } else {
            return null;
        }
    }
    
    public void save(Event e){
        em.persist(e);
    }
    
    public void update(Event e){
        em.merge(e);
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
