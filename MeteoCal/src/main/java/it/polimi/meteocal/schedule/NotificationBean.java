/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.security.Notification;
import it.polimi.meteocal.security.NotificationManager;
import it.polimi.meteocal.security.UserManager;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Filippo
 */
@Named(value = "notificationBean")
@RequestScoped
public class NotificationBean {

    @EJB
    NotificationManager notificationManager; 
    @EJB
    UserManager userManager;        
    
    MenuModel model;

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
        
    }
    
    
    /**
     * Creates a new instance of NotificationBean
     */
    public NotificationBean() {
        model=new DefaultMenuModel();
        
        
    }
    
  //  @PostConstruct
    public void postConstruct(){
        
        List<Notification> notif= notificationManager.getNotificationForUser(userManager.getLoggedUser());
        
        for (Notification n:notif){
            DefaultMenuItem item = new DefaultMenuItem(n.getText());
            model.addElement(item);
        }
    }
    
}
