/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.entity.User;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Filippo
 */
@Stateless
public class EventTypeManager {
    
    @PersistenceContext
    private EntityManager em;

     @EJB
    private UserManager userManager;
    
    
    public EventType findEventTypeforId(Integer id){
        if (id != null) {
            return em.find(EventType.class, id);
        } else {
            return null;
        }
    }
    
    public List<EventType> findTypesForUser(){
        User u=userManager.getLoggedUser();
        return (List<EventType>) em.createNamedQuery(EventType.findAllTypesForUser, EventType.class).setParameter("user", u.getEmail()).getResultList();
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
