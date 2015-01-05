/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.security.Notification;
import it.polimi.meteocal.security.NotificationManager;
import it.polimi.meteocal.security.UserManager;
import java.util.LinkedList;
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
    private List<Notification> notif;
    private final List<Notification> unRead = new LinkedList<>();

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
        
    }
    
    public int getUnReadNotificationNumber() {
        return unRead.size();
    }
    
    public boolean isThereNotificationUnRead() {
        return !unRead.isEmpty();
    }
    
    
    /**
     * Creates a new instance of NotificationBean
     */
    public NotificationBean() {
        model=new DefaultMenuModel();
        
        
    }
    
    public void onItemSelect() {
        System.out.println("item select");
    }
    
    @PostConstruct
    public void postConstruct(){
        
        notif= notificationManager.getNotificationForUser(userManager.getLoggedUser());
        for (Notification n: notif) {
            if (n.getState().equals("UNREAD")) {
                unRead.add(n);
            }
        }
        for (Notification n:notif){
            DefaultMenuItem item = new DefaultMenuItem(n.getText());
            item.setOutcome(n.getAbout().getPageLink()+"?id="+n.getAbout().getId());
            item.setParam("notificationID", n.getId());
            item.setParam("notificationType", n.getType());
            item.setIcon("prova");
            item.setIconPos("right");
            if ("UNREAD".equals(n.getState())) {
                item.setStyle("background: #F6E3CE; margin-top: -5px; margin-bottom: 5px; margin-left: -5px; width: 180px;");
            } else {
                item.setStyle("margin-top: -5px; margin-bottom: 5px; margin-left: -5px; width: 180px;");
            }
            model.addElement(item);
        }
    }
    
}