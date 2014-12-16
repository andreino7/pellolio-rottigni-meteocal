/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.EventType;
import java.util.Date;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author Filippo
 */
public class MeteoCalScheduleEvent extends DefaultScheduleEvent {
    Calendar calendar;
    EventType type;

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
    
    public MeteoCalScheduleEvent(String s,Date d1,Date d2){
        super(s, d1, d2);
    }
}
