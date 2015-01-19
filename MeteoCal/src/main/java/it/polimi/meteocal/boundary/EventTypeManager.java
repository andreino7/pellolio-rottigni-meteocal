/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.boundary.UserManager;
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
    
    public boolean isMine(EventType e){
        if (e.getPersonalized()){
            return e.getOwner().equals(userManager.getLoggedUser());
        }
        return false;
        
    }
    
    public void update(EventType e){
        em.merge(e);
    }
    
    public void save(EventType e){
        em.persist(e);
    }
    
    public List<EventType> findDefaultTypes(){
        return (List<EventType>) em.createNamedQuery(EventType.findDefaultTypes, EventType.class).getResultList();

    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
