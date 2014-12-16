/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;


import it.polimi.meteocal.entity.User;
import java.security.Principal;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author miglie
 */
@Stateless
public class UserManager {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    Principal principal;

    public void save(User user) {
        user.setClearance("USER");
        em.persist(user);
    }

    public void update(User user){
        em.merge(user);
    }
    
    public void unregister() {
        em.remove(getLoggedUser());
    }

    public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }
    
    public boolean isLoggedIn(){
        if(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()!=null) {
          return true;
        }

        return false;
    }
    
    public User findUserforId(String id){
        if (id!=null ){
        return   em.find(User.class, id);
        }else{return null;}

    }
}
