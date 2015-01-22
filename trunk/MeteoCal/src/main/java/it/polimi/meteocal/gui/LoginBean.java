/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.boundary.UserManager;
import java.io.IOException;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
@Named(value = "loginBean")
@RequestScoped
public class LoginBean {
    
    @EJB
    UserManager userManager;

    
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
        if ("ADMIN".equals(userManager.getLoggedUser().getClearance())){
            return "/admin/adminHome.xhtml?faces-redirect=true";
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
    
    public void checkAlreadyLogged(){
        if (userManager.isLoggedIn()){
            if ("ADMIN".equals(userManager.getLoggedUser().getClearance())){
            redirectTo("/MeteoCal/faces/admin/adminHome.xhtml");
        }else{
                redirectTo("/MeteoCal/faces/user/home.xhtml");
            }
        }
        
    }
    
    public void redirectTo(String url){
     try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(EventPageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

