/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.security.UserManager;
import javax.inject.Named;
import java.io.Serializable;
import java.security.Principal;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Filippo
 */
@Named(value = "userProfile")
@RequestScoped
public class userProfile implements Serializable {

    /**
     * Creates a new instance of userProfile
     */
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private UserManager userManager;

     @Inject
    Principal principal;
  
    private User user;
    private boolean ownprofile;
    private String value;
    private String name;
    private String surname;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        user.setSurname(surname);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        user.setName(name);
    }
    

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
         init();
        
    }
    

    public boolean isOwnprofile() {
        return ownprofile;
    }

    public void setOwnprofile(boolean ownprofile) {
        this.ownprofile = ownprofile;
    }
    
    

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
   
    
    
    
    public userProfile() {
        
    }
    
    @PostConstruct
    public void postConstruct(){
        initParam();
        this.user=new User();
        ownprofile= userManager.getLoggedUser().equals(userManager.findUserforId(value));
    }
    
    
    public void init(){
        
        this.user = userManager.findUserforId(value);
        
        ownprofile = em.find(User.class, principal.getName()).equals(this.user);
        
        
    }
     
    public void initParam(){
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

        value=request.getParameter("id");
       
    }
    
    public void reset(){
       
      this.user= userManager.findUserforId(user.getEmail());
    
}
    
    public void save() {
        System.err.println(user.getName());

        userManager.update(user); 
    }
    
}
