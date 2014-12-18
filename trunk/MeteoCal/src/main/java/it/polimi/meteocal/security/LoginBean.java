/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Filippo
 */
@Named
@RequestScoped
public class LoginBean {

    
    private String email;
    private String password;
    
    public LoginBean() {
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

     public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.email, this.password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Invalid Email or password."));
            return "login";
        }
        return "/user/home.xhtml?faces-redirect=true";
    }
  

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
            request.getSession().invalidate();
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Logout failed."));
        }
        try {
         context.getExternalContext().redirect("faces/login.xhtml");
        }catch(Exception e){
            
        }
    }
    
    @PostConstruct
    public void ciao() {
        System.out.println("ciao");
    }
    
}

