/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.entity.User;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.persistence.Query;

/**
 *
 * @author miglie
 */
@Named(value = "registrationBean")
@RequestScoped
public class RegistrationBean {

    @EJB
    private UserManager um;

    private User user;
    
    private UIComponent inputEmail;

    public RegistrationBean() {
    }

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public UIComponent getInputEmail() {
        return inputEmail;
    }
    
    public void setInputEmail(UIComponent inputEmail) {
        this.inputEmail = inputEmail;
    }

    public String register() {
         if (um.notExist(user)) {
            um.save(user);
            return "user/home?faces-redirect=true";           
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage("This Email address is already registered");
            context.addMessage(inputEmail.getClientId(context), msg);
            return "index?faces-redirect=false"; 
        } 
    }

}
