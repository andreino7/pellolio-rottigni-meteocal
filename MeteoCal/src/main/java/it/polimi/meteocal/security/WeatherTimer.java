/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.entity.Event;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author Andrea
 */
@Singleton
public class WeatherTimer {

   @EJB
   private WeatherChecker weather;
   @EJB
   private EventManager eventManager;
   
   private Calendar calendarSetUp(Calendar cal) {
       cal.setTime(new Date());
       cal.set(Calendar.HOUR_OF_DAY, 00);
       cal.set(Calendar.MINUTE, 00);
       cal.set(Calendar.SECOND, 00);
       cal.set(Calendar.MILLISECOND, 00);
       return cal;
   }
   
   private List<Event> findEventToCheck(Date d1, Date d2) {
       return eventManager.findByDay(d1,d2);
   }
   
   private void checkWeather() {
       Calendar cal = Calendar.getInstance();
       calendarSetUp(cal);
       cal.add(Calendar.DATE, 3);
       Date d1 = cal.getTime();
       cal.add(Calendar.DATE, 1);
       Date d2 = cal.getTime();
       List<Event> events = findEventToCheck(d1, d2);
       for (Event e: events) {
           weather.addWeather(e.getLocation(), d2);
       }
       for (Event e: events) {
           
       }
   }
}
