/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.boundary.EventTypeManager;
import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.schedule.Visibility;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Filippo
 */
@Named(value = "eventTypePageBean")
@ViewScoped
public class EventTypePageBean implements Serializable{

    @EJB
    EventTypeManager evManager;
    @EJB
    UserManager userManager;
    @Inject
    ScheduleBean scBean;
    
    private String param;
    
    private EventType eventType;
    private boolean mine;
    private boolean newType;
    private String eventId;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public boolean isNewType() {
        return newType;
    }

    public void setNewType(boolean newType) {
        this.newType = newType;
    }

    
    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }
    
    
    
    @PostConstruct
    public void postConstruct(){
        initParam();
        boolean number=true;
        try{
            Integer.parseInt(param);
        }catch (Exception e){
            number=false;
        }
        if (!number){
            System.out.println("!number");
             eventType=new EventType(-1);
             eventType.setOwner(userManager.getLoggedUser());
             eventType.setPersonalized(true);
             newType = true;
             mine = true;
        }else{
              eventType= evManager.findEventTypeforId(Integer.parseInt(param));
        }
        if (eventType==null){
            redirect();
        }
        checkPermissions();
    }
    
        
    private void redirect() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(EventPageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initParam() {

        param = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        eventId = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eventId");
        String partial = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("javax.faces.partial.ajax");
        if (param == null && partial == null) {
            redirect();
            return;

        }
        if ("true".equals(partial)){

        }
       
    }
    


    private void checkPermissions() {
       if (eventType.getPersonalized()){
           if(evManager.isMine(eventType)){
               mine= true;
           }
       }
    }
    
    public String converToText(boolean b){
        if (b){
            return "Allowed";
        }
        return "Not Allowed";
    }

    public void save(){
        System.out.println("save");
        if (newType) {
            evManager.save(eventType);
            newType=false;
        } else {
            evManager.update(eventType);
        }
        reset();
        scBean.postConstruct();
    }
    
    public void update() {
        evManager.update(eventType);
        scBean.postConstruct();
    }
    
    public void reset(){
        this.eventType=evManager.findEventTypeforId(eventType.getId());
    }
    
}
