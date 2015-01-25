/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.schedule.Visibility;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.ChangedEventNotification;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventCalendar;
import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.boundary.CalendarManager;
import it.polimi.meteocal.boundary.EventManager;
import it.polimi.meteocal.boundary.EventTypeManager;
import it.polimi.meteocal.notification.NotificationCleaner;
import it.polimi.meteocal.boundary.NotificationManager;
import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.schedule.MeteoCalScheduleEvent;
import it.polimi.meteocal.schedule.MeteoCalScheduleModel;
import it.polimi.meteocal.weather.DateManipulator;
import it.polimi.meteocal.weather.WeatherChecker;
import it.polimi.meteocal.weather.WeatherConditions;
import it.polimi.meteocal.weather.WeatherTimer;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.PostActivate;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
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
    private static final int maxCalendars = 8;
    private MeteoCalScheduleModel model;
    private User user;
    private boolean scheduleDisplay = true;

    @EJB
    UserManager userManager;

    @EJB
    CalendarManager calendarManager;

    @EJB
    EventManager eventManager;

    @EJB
    EventTypeManager etManager;

    @EJB
    NotificationManager notificationManager;

    @EJB
    WeatherChecker weather;
    
    @EJB
    WeatherTimer weatherTimer;

    @EJB
    EmailSessionBean emailBean;

    @EJB
    NotificationCleaner cleaner;

    @PersistenceContext
    EntityManager em;

    private Integer calendarId;
    private String geoLoc;

    private List<String> visibilities;
    private List<EventType> userTypes;
    private List<Calendar> userCalendars;
    private List<String> chosenCalendars;
    private List<Event> nextEvents;
    private MeteoCalScheduleEvent event = new MeteoCalScheduleEvent(eventNotInDB, "", null, null, null, null);
    private String title;
    private Calendar newCalendar;
    private boolean modifiableEvent= true;
    private EventType newEventType;

    List<String> colorclass;
    Map<Integer, String> colorForCalendar;
    
    public void print() {
        System.out.println("andrea");
        postConstruct();
    }

    public boolean isModifiableEvent() {
        return modifiableEvent;
    }

    public void setModifiableEvent(boolean modifiableEvent) {
        this.modifiableEvent = modifiableEvent;
    }

    public List<Event> getNextEvents() {
        nextEvents = eventManager.findFutureEventsForCalendars(chosenCalendars);
        return nextEvents;
    }

    public boolean isScheduleDisplay() {
        return scheduleDisplay;
    }

    public void setScheduleDisplay(boolean scheduleDisplay) {
        this.scheduleDisplay = scheduleDisplay;
    }

    public void initNewCalendar(){
        newCalendar=new Calendar();
        newCalendar.setOwner(user);
    }
    
    public Calendar getNewCalendar() {
        return newCalendar;
    }

    public void setNewCalendar(Calendar newCalendar) {
        this.newCalendar = newCalendar;
    }

    public String getGeoLoc() {
        return geoLoc;
    }

    public void setGeoLoc(String geoLoc) {
        this.geoLoc = geoLoc;
    }

    public List<String> getVisibilities() {
        return visibilities;
    }

    public List<EventType> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(List<EventType> userTypes) {
        this.userTypes = userTypes;
    }



    public String getClassForCalendar(Calendar c) {
        return colorForCalendar.get(c.getId());
    }

    public String getColorBoxForCalendar(Calendar c) {
        return "<div class=\" colorbox " + getClassForCalendar(c) + "\" ></div>";
        
    }
    
    public String getLinkForCalendar(Calendar cal){
        return "<a href=\"calendar.xhtml?id="+cal.getId()+"\" > "+cal.getTitle()+"</a>";
    }

    public List<String> getChosenCalendars() {
        return chosenCalendars;
    }

    public void setChosenCalendars(List<String> chosenCalendars) {
        this.chosenCalendars = chosenCalendars;
        updateScheduleModel();
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

        this.title = title;
    }

    public ScheduleBean() {
        model = new MeteoCalScheduleModel();

    }

    public ScheduleModel getModel() {

        return model;
    }

    @PostConstruct
    public void postConstruct() {
        user = userManager.getLoggedUser();
        visibilities = new LinkedList<>();
        visibilities.add(Visibility.Private);
        visibilities.add(Visibility.Public);
        event = new MeteoCalScheduleEvent(eventNotInDB, "", null, null, null, null);

        userCalendars = calendarManager.findCalendarForUser(userManager.getLoggedUser());
        userTypes = etManager.findTypesForUser();
        chosenCalendars = new LinkedList<>();

        int i = 0;
        for (Calendar c : userCalendars) {
            if (i < maxCalendars) {
                chosenCalendars.add(c.getId().toString());
                i++;
            }
        }

        updateScheduleModel();
    }

    public MeteoCalScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = (MeteoCalScheduleEvent) event;
    }

    public void addEvent() {
        try {
            this.save();
            updateScheduleModel();
            event = new MeteoCalScheduleEvent(eventNotInDB, "", new Date(), new Date(), null, null);//reset dialog form
            modifiableEvent=true;
            
        } catch (BadEventException ex) {

        }
    }
    


    public void onEventSelect(SelectEvent e) {
        ScheduleEvent ev = (ScheduleEvent) e.getObject();

        event = model.getMeteoEvent(ev.getId());
        modifiableEvent= (eventManager.isMyEvent(event.getDbId().toString()) && !(event.getStartDate().before(new Date())));
        geoLoc = event.getLocation();

    }

    public void onDateSelect(SelectEvent e) {
        Date date = (Date) e.getObject();
        System.out.println(date);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Calendar calAux = java.util.Calendar.getInstance();
        cal.setTime(date);
        calAux.setTime(new Date());
        int hour = calAux.get(java.util.Calendar.HOUR_OF_DAY);
        cal.set(java.util.Calendar.HOUR_OF_DAY, hour);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 1);
        calAux.set(java.util.Calendar.MINUTE, 0);
        calAux.set(java.util.Calendar.SECOND, 0);
        calAux.set(java.util.Calendar.MILLISECOND, 0);
        System.out.println(cal.getTime());
        event = new MeteoCalScheduleEvent(eventNotInDB, "", cal.getTime(), cal.getTime(), null, null);
        modifiableEvent = cal.getTime().after(calAux.getTime());
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
        System.out.println("qua");
        model = new MeteoCalScheduleModel();
        colorclass = new LinkedList<>(Arrays.asList("red", "green", "aqua", "blue", "yellow", "sienna", "violet", "purple"));
        colorForCalendar = new HashMap<>();
        Random r = new Random();

        if (chosenCalendars != null) {
            while (chosenCalendars.size()>colorclass.size())
            {
                chosenCalendars.remove(chosenCalendars.size()-1);
            }
            for (String c : chosenCalendars) {
                Integer colid = r.nextInt(colorclass.size());
                colorForCalendar.put(Integer.parseInt(c), colorclass.get(colid));
                colorclass.remove(colorclass.get(colid));

                List<Event> evList = (List<Event>) em.createNamedQuery(EventCalendar.findEventsForCalendar, Event.class).setParameter("calendar", Integer.parseInt(c)).getResultList();

                for (Event ev : evList) {
                    MeteoCalScheduleEvent scheduleEvent = new MeteoCalScheduleEvent(ev, calendarManager.findCalendarForId(c));
                    scheduleEvent.setStyleClass(ev.getWeather() + " " + colorForCalendar.get(Integer.parseInt(c))+" "+ (ev.getDate().before(new Date())?"old":""));
                    scheduleEvent.setDescription(ev.getWeather());

                    model.addEvent(scheduleEvent);
                }
            }
        }
        //FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("schedule");

    }

    public class BadEventException extends Exception {

    }

    public void save() throws BadEventException {
        Event ev;
        if (event.getStartDate().after(new Date())) {
            if (eventManager.findEventForId(event.getDbId()) != null) {
                ev = eventManager.findEventForId(event.getDbId());
                ev.setId(event.getDbId());
                ev.setTitle(event.getTitle());
                ev.setDate(event.getStartDate());
                ev.setEndDate(event.getEndDate());
                ev.setType(event.getType());
                ev.setLocation(event.getLocation());
                ev.setVisibility(event.getVisibility());
                try {
                   // ev.setWeather(weather.addWeather(event.getLocation(), event.getStartDate()).toString());
                    String cityNoSpace = event.getLocation().replaceAll("\\s+", "_");
                    if (weather.isValidCityFormat(cityNoSpace)) {
                        ev.setWeather(weather.addWeather(cityNoSpace, event.getStartDate()).toString());
                    } else {
                        ev.setWeather(WeatherConditions.UNAVAILABLE.toString());
                        ev.setLocation("Milan,IT");
                    }
                    eventManager.update(ev);
                    if (event.getCalendar() != event.getOld()) {
                        eventManager.toggleLink(ev, event.getOld());
                        eventManager.linkToCalendar(ev, event.getCalendar());

                    }
                    weatherTimer.setTimer(ev.getId(), DateManipulator.subtractDays(ev.getDate(), 3));
                    weatherTimer.setTimer(ev.getId(), DateManipulator.subtractDays(ev.getDate(), 1));
                    sendChangeEventNotification(ev);
                } catch (Exception ex) {
                    errorOccurred();
                }
            } else {
                //TODO location and visibility
                ev = new Event(event.getDbId(), event.getTitle(), "", event.getStartDate(), event.getEndDate(), "");
                ev.setType(event.getType());
                ev.setLocation(event.getLocation());
                ev.setVisibility(event.getVisibility());
                ev.setEventOwner(user);

                try {
                    String cityNoSpace = event.getLocation().replaceAll("\\s+", "_");
                    if (weather.isValidCityFormat(cityNoSpace)) {
                        ev.setWeather(weather.addWeather(cityNoSpace, event.getStartDate()).toString());
                    } else {
                        ev.setWeather(WeatherConditions.UNAVAILABLE.toString());
                    }
                    ev.setEventOwner(user);

    // ev.setWeather(weather.addWeather(event.getLocation(), event.getStartDate()).toString());
                    eventManager.save(ev);
                    eventManager.linkToCalendar(ev, event.getCalendar());
                    cleaner.setTimer(ev.getId(), ev.getEndDate());
                    weatherTimer.setTimer(ev.getId(), DateManipulator.subtractDays(ev.getDate(), 3));
                    weatherTimer.setTimer(ev.getId(), DateManipulator.subtractDays(ev.getDate(), 1));
                } catch (Exception ex) {
                    errorOccurred();

                }
            }
        }
    }

   

    public void saveNewCalendar() {
        calendarManager.save(newCalendar);
        postConstruct();
        initNewCalendar();
    }

    private void sendChangeEventNotification(Event ev) {
        List<User> partecipant = eventManager.getParticipant(ev);
        notificationManager.createChangedEventNotification(ev, partecipant);
    }

    public void switchView() {
        scheduleDisplay = !scheduleDisplay;
    }
    
    private void errorOccurred() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../error.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(EventPageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void removeEvent(){
        if (this.event!=null){
            eventManager.removeEvent(eventManager.findEventForId(event.getDbId()),event.getCalendar());
        }
        updateScheduleModel();
    }
    
    public void selectCalendar(Calendar c){
        newCalendar=c;
    }
    
    public Date today(){
        return new Date();
    }
    
    

}
