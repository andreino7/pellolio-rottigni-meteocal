/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.boundary.CalendarManager;
import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.notification.NotificationType;
import it.polimi.meteocal.schedule.Visibility;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
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
@Named(value = "calendarPageBean")
@ViewScoped
public class CalendarPageBean implements Serializable{
    
    String param;
    
    @EJB
    CalendarManager cm;
    
     @Inject
    ScheduleBean sb;
     
     @EJB 
             UserManager um;
    
    Calendar calendar;
    List<String> visibilities = new LinkedList<>();

    public List<String> getVisibilities() {
        return visibilities;
    }

    

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    
    /**
     * Creates a new instance of CalendarPageBean
     */
    public CalendarPageBean() {
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
             calendar=new Calendar(-1);
             calendar.setOwner(um.getLoggedUser());
        }else{
              calendar= cm.findCalendarForId(param);

        }
        
visibilities = new LinkedList<>();
visibilities.add(Visibility.Private);
        visibilities.add(Visibility.Public);
        if (calendar==null){
            redirect();
        }
        
    }
    
    private void initParam() {

        param = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        String partial = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("javax.faces.partial.ajax");
        if (param == null && partial == null) {
            redirect();
            return;

        }
        if ("true".equals(partial)){

        }
       
    }

    
     private void redirect() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(EventPageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
      public void save() {
          if(calendar.getId()!=-1){
        cm.update(calendar);
          }else{
              cm.save(calendar);
          }
        sb.postConstruct();
    }

    public void reset() {
        calendar = cm.findCalendarForId(calendar.getId().toString());
    }
    
    public void remove() {
        if (calendar.getId()!=-1){
        cm.remove(calendar);
        sb.postConstruct();
        redirect();
        }
    }
}
