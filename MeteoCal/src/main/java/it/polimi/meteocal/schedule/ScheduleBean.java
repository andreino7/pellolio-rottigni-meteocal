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
import it.polimi.meteocal.security.CalendarManager;
import it.polimi.meteocal.security.EventManager;
import it.polimi.meteocal.security.UserManager;
import it.polimi.meteocal.security.WeatherChecker;
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

    private static final Integer eventNotInDB = 0;
    private MeteoCalScheduleModel model;
    private User user;
    @EJB
    private UserManager userManager;

    @EJB
    private CalendarManager calendarManager;

    @EJB
    private EventManager eventManager;

    @EJB
    private WeatherChecker weather;

    @PersistenceContext
    private EntityManager em;

    private Integer calendarId;
    private String geoLoc;

    private List<String> visibilities = new LinkedList<String>();
    private List<EventType> userTypes;
    private List<Calendar> userCalendars;
    private List<String> chosenCalendars;
    private MeteoCalScheduleEvent event = new MeteoCalScheduleEvent();
    private String title;
    private String style;
    private String colorCalendarString;
    private String color;
    private List<String> colors;

    public String getGeoLoc() {
        return geoLoc;
    }

    public void setGeoLoc(String geoLoc) {
        this.geoLoc = geoLoc;
    }

    public List<String> getVisibilities() {
        return visibilities;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getColors() {
        List<String> ls = new LinkedList<String>();
        ls.add("Red");
        ls.add("Green");
        return ls;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getColorCalendarString() {
        return "<p:selectOneMenu id=\"car\" rendered=\"true\" value=\"#{scheduleBean.color}\">\n"
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
        model = new MeteoCalScheduleModel();

        System.out.println("aaaaaaa");
    }

    public ScheduleModel getModel() {

        return model;
    }

    @PostConstruct
    private void postConstruct() {
        user = userManager.getLoggedUser();

        updateScheduleModel();

        visibilities.add(Visibility.Private);
        visibilities.add(Visibility.Public);
        event = new MeteoCalScheduleEvent(eventNotInDB, "", null, null, null, null);
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
        if (event.getLocation() != null) {
            this.save();
            if (event.getId() == null) {
                model.addEvent(event);

            } else {
                model.updateEvent(event);
            }
        }else{
            System.out.println("No Location");
        }
        event = new MeteoCalScheduleEvent(); //reset dialog form
    }

    public void onEventSelect(SelectEvent e) {
        System.out.println("Event Selected");
        ScheduleEvent ev = (ScheduleEvent) e.getObject();

        event = model.getMeteoEvent(ev.getId());
        geoLoc = event.getLocation();

    }

    public void onDateSelect(SelectEvent e) {
        Date date = (Date) e.getObject();
        event = new MeteoCalScheduleEvent(eventNotInDB, "", date, date, null, null);
        geoLoc = "";
    }

    public void onGeolocation() {
        String City;
        City = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("City");
        String Country;
        Country = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("Country");

        event.setLocation(City + "," + Country);

    }

    public void updateScheduleModel() {
        model = new MeteoCalScheduleModel();

        if (chosenCalendars != null) {
            for (String c : chosenCalendars) {
                List<Event> evList = (List<Event>) em.createNamedQuery(EventCalendar.findEventsForCalendar, Event.class).setParameter("calendar", Integer.parseInt(c)).getResultList();

                for (Event ev : evList) {
                    MeteoCalScheduleEvent scheduleEvent = new MeteoCalScheduleEvent(ev, calendarManager.findCalendarForId(c));
                    scheduleEvent.setStyleClass("sunny red");
                    scheduleEvent.setDescription("sunny");
                    model.addEvent(scheduleEvent);
                }
            }
        }
        //FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("schedule");

    }

    public void save() {

        if (eventManager.findEventForId(event.getDbId()) != null) {
            Event ev = eventManager.findEventForId(event.getDbId());
            ev.setId(event.getDbId());
            ev.setTitle(event.getTitle());
            ev.setDate(event.getStartDate());
            ev.setEndDate(event.getEndDate());
            ev.setType(event.getType());
            ev.setLocation(event.getLocation());
            ev.setVisibility(event.getVisibility());
            ev.setWeather(weather.addWeather(event.getLocation(), event.getStartDate()).toString());
            eventManager.update(ev);
            if (event.getCalendar()!=event.getOld()){
                eventManager.linkToCalendar(ev, event.getCalendar());
                eventManager.toggleLink(ev, event.getOld());
            }

        } else {
            //TODO location and visibility
            Event ev = new Event(event.getDbId(), event.getTitle(), "", event.getStartDate(), event.getEndDate(), "");
            ev.setType(event.getType());
            ev.setLocation(event.getLocation());
            ev.setVisibility(event.getVisibility());
            ev.setWeather(weather.addWeather(event.getLocation(), event.getStartDate()).toString());
            ev.setEventOwner(user);
            eventManager.save(ev);
            eventManager.linkToCalendar(ev, event.getCalendar());

        }

    }

}
