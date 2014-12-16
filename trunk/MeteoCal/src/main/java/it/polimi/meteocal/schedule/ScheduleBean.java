/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventCalendar;
import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.security.UserManager;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Filippo
 */
@Named(value = "scheduleBean")
@SessionScoped
public class ScheduleBean implements Serializable {

    private ScheduleModel model;
    @EJB
    private UserManager userManager;
    @PersistenceContext
    private EntityManager em;

    private Integer calendarId;

    private List<EventType> userTypes;
    private List<Calendar> userCalendars;
    private List<String> chosenCalendars;
    private MeteoCalScheduleEvent event = new MeteoCalScheduleEvent();
    private String title;
    private String style;
    private String colorCalendarString;
    private String color;
    private List<String> colors;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getColors() {
       List<String> ls= new LinkedList<String>();
        ls.add("Red");
        ls.add("Green");
        return ls;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }
    

    public String getColorCalendarString() {
        return "<p:selectOneMenu id=\"car\" rendered=\"true\" value=\"#{scheduleBean.color}\">\n"
                + "            <f:selectItem itemLabel=\"Select One\" itemValue=\"\" />\n"
                + "            <f:selectItems value=\"#{scheduleBean.colors}\" itemLabel=\" \"/>\n"
                + "        </p:selectOneMenu>";
    }

    public String getStyle() {
        return "<input></input>";
    }

    public List<EventType> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(List<EventType> userTypes) {
        this.userTypes = userTypes;
    }

    public List<String> getChosenCalendars() {
        return chosenCalendars;
    }

    public void setChosenCalendars(List<String> chosenCalendars) {
        this.chosenCalendars = chosenCalendars;
        updateScheduleModel();
        System.out.println("xxx");
    }

    public Integer getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Integer id) {
        Calendar c;
        c = (Calendar) em.createNamedQuery(Calendar.findById, Calendar.class).setParameter("id", id).getSingleResult();
        event.setCalendar(c);
    }

    public String getTitle() {
        if (event != null) {
            title = event.getTitle();
        }
        return title;
    }

    public List<Calendar> getUserCalendars() {
        return userCalendars;
    }

    public void setTitle(String title) {
        event.setTitle(title);
        System.out.println(title);

        this.title = title;
    }

    public ScheduleBean() {
        model = new DefaultScheduleModel();

        System.out.println("aaaaaaa");
    }

    public ScheduleModel getModel() {

        return model;
    }

    @PostConstruct
    private void postConstruct() {
        User user = userManager.getLoggedUser();

        List<Event> evList;

        evList = (List<Event>) em.createNamedQuery(EventCalendar.findEventsForUser, Event.class).setParameter("userSelected", user.getEmail()).getResultList();

        for (Event ev : evList) {
            DefaultScheduleEvent scheduleEvent = new DefaultScheduleEvent(ev.getTitle(), ev.getDate(), ev.getEndDate());

            model.addEvent(scheduleEvent);

        }

        userCalendars = (List<Calendar>) em.createNamedQuery(Calendar.findByOwner, Calendar.class).setParameter("ownerEmail", user.getEmail()).getResultList();
        userTypes = (List<EventType>) em.createNamedQuery(EventType.findAllTypesForUser, EventType.class).setParameter("user", user.getEmail()).getResultList();
        /* userCalendars=new HashMap<String,Calendar>();
        
         for (Calendar c:Calendars){
         userCalendars.put(c.getTitle(), c);
         }*/
    }

    public MeteoCalScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = (MeteoCalScheduleEvent) event;
    }

    public void addEvent() {
        if (event.getId() == null) {
            model.addEvent(event);

        } else {
            model.updateEvent(event);
        }
        event = new MeteoCalScheduleEvent(); //reset dialog form
    }

    public void onEventSelect(SelectEvent e) {
        event = (MeteoCalScheduleEvent) e.getObject();
    }

    public void onDateSelect(SelectEvent e) {
        Date date = (Date) e.getObject();
        event = new MeteoCalScheduleEvent("", date, date);
    }

    public void onGeolocation() {
        String Lat;
        Lat = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("Lat");
        String Long;
        Long = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("Long");

        System.out.println(Lat + "de  " + Long);

    }

    public void updateScheduleModel() {
        model = new DefaultScheduleModel();

        List<Integer> chosenCalendarsId = new LinkedList<Integer>();

        if (chosenCalendars != null & !chosenCalendars.isEmpty()) {
            List<Event> evList = (List<Event>) em.createNamedQuery(EventCalendar.findEventsForCalendars, Event.class).setParameter("calendars", chosenCalendars).getResultList();

            for (Event ev : evList) {
                DefaultScheduleEvent scheduleEvent = new DefaultScheduleEvent(ev.getTitle(), ev.getDate(), ev.getEndDate());
                scheduleEvent.setStyleClass("");
                model.addEvent(scheduleEvent);
            }
        }
        //FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("schedule");

    }

}
