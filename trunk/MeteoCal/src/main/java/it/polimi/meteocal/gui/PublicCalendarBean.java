/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.boundary.EventCalendarManager;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventCalendar;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.boundary.EventManager;
import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.schedule.Visibility;
import it.polimi.meteocal.schedule.MeteoCalScheduleEvent;
import it.polimi.meteocal.schedule.MeteoCalScheduleModel;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author Andrea
 */
@Named(value = "publicCalendarBean")
@ViewScoped
public class PublicCalendarBean implements Serializable {
    
    private String value; 
    private User user;
    private MeteoCalScheduleModel model;
    private MeteoCalScheduleEvent event = new MeteoCalScheduleEvent();
    private boolean common;
    private String geoLoc;
    private List<Calendar> loggedUserCalendars;
    private EventManager eventManager;
    @EJB
    private UserManager userManager;
    @PersistenceContext
    private EntityManager em;
    @EJB
    private EventCalendarManager ecManager;

    public MeteoCalScheduleEvent getEvent() {
        return event;
    }

    public List<Calendar> getLoggedUserCalendars() {
        return loggedUserCalendars;
    }

    public void setLoggedUserCalendars(List<Calendar> loggedUserCalendars) {
        this.loggedUserCalendars = loggedUserCalendars;
    }


    public MeteoCalScheduleModel getModel() {
        return model;
    }

    
    public String getValue() {
        return value;
    }

    public User getUser() {
        return user;
    }

    public void setValue(String value) {
        this.value = value;

    }
    
    public void init() {
        this.user = userManager.findUserforId(value);
        updateScheduleModel();
    }
    
    public PublicCalendarBean(){
        this.model= new MeteoCalScheduleModel();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateScheduleModel() {
        this.model = new MeteoCalScheduleModel();
        this.user = em.find(User.class, user.getEmail());
        List<Calendar> calendars = em.createNamedQuery(Calendar.findPublicByOwner, Calendar.class).setParameter("ownerEmail", user.getEmail()).getResultList();
        List<Event> allEvents = new LinkedList<>();
        List<Event> allLoggedEvents = new LinkedList<>();
        loggedUserCalendars = em.createNamedQuery(Calendar.findByOwner, Calendar.class).setParameter("ownerEmail", userManager.getLoggedUser().getEmail()).getResultList();
        for (Calendar calendar: loggedUserCalendars) {
            allLoggedEvents.addAll(em.createNamedQuery(EventCalendar.findEventsForCalendar, Event.class).setParameter("calendar", calendar.getId()).getResultList());
        }
        for (Calendar calendar : calendars) {
            allEvents.addAll(em.createNamedQuery(EventCalendar.findEventsForCalendar, Event.class).setParameter("calendar", calendar.getId()).getResultList());
        }
        for (Event e: allEvents){
            if (allLoggedEvents.contains(e)) {
                common = true;
                MeteoCalScheduleEvent se = new MeteoCalScheduleEvent(e, common);
                se.setStyleClass("blue");
                model.addEvent(se);
            } else {
                if (e.getVisibility().equals(Visibility.Private)) {
                    common = false;
                    MeteoCalScheduleEvent se = new MeteoCalScheduleEvent(e, common);
                    se.setStyleClass("red");
                    model.addEvent(se);
                } else {
                    common = false;
                    MeteoCalScheduleEvent se = new MeteoCalScheduleEvent(e, common);
                    se.setStyleClass("green");
                    model.addEvent(se);
                }
            }
        }
    }
    
       
    @PostConstruct
    public void postConstruct() {
        initParam();
        init();
    }
    
    
    public void initParam() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        value = request.getParameter("id");
    }
    
    public void onEventSelect(SelectEvent e) {
        System.out.println("select: " + e);
        event = (MeteoCalScheduleEvent) model.getMeteoEvent(((MeteoCalScheduleEvent) e.getObject()).getId());
    /*    System.out.println(e);
        Object o = e.getObject();
        System.out.println(o);
        ScheduleEvent ev = (ScheduleEvent) e.getObject();
       
        if (ev != null) {
            System.out.println("qua");
            event = model.getMeteoEvent(ev.getId());
            geoLoc = event.getLocation();        
        } else {
            System.err.println("qua");
        } */
        List<ScheduleEvent> lista = model.getEvents();
        if (lista.isEmpty()) {
            System.out.println("vuota");
        } else {
            for (ScheduleEvent a: lista) {
                System.out.println(a.getId());
            }
        }

    } 
    
    public void add() {
        Event e = em.find(Event.class, event.getDbId());
        if (e != null) {
            //TODO controlla se c'è già
            EventCalendar ec = new EventCalendar(-1);
            ec.setCalendar(event.getCalendar());
            ec.setEvent(e);
            ecManager.save(ec);
            updateScheduleModel();
        } 
    }
    
    
    

    
    
    
    
   
    
    
  
}
