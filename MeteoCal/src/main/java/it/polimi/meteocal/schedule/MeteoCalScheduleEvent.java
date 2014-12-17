/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.security.EventManager;
import java.util.Date;
import javax.ejb.EJB;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author Filippo
 */
public class MeteoCalScheduleEvent extends DefaultScheduleEvent {
    private Calendar calendar;
    private EventType type;
    private Integer dbId;
    private String location;
    private String visibility;
    
    

    public String getLocation() {
        return location;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setLocation(String Location) {
        this.location = Location;
    }
    
    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }
    
    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
        

    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        System.out.println(calendar);
    }
    
    public MeteoCalScheduleEvent(){
        super();
    }
    
    public MeteoCalScheduleEvent(Integer id,String s,Date d1,Date d2,Calendar c, EventType t){
        super(s, d1, d2);
        this.dbId=id;
        this.calendar=c;
        this.type=t;
    }
    public MeteoCalScheduleEvent(DefaultScheduleEvent e){
        super();
        this.setTitle(e.getTitle());
        this.setEndDate(e.getEndDate());
        this.setStartDate(e.getEndDate());
        this.setStyleClass(e.getStyleClass());
        
        
    }
    
   
    
    public String toString(){
        return "MeteocalEvent id:"+getId()+" Calendar: "+calendar.getTitle();
    }
    
}
