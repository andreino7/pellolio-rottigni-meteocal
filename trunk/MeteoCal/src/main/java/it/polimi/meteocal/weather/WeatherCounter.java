/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.weather;


import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author Andrea
 */
@Singleton
public class WeatherCounter {
    
    private static final int MAXMINUTEREQUESTNUMBER=3000;
    private static final int MAXDAYREQUESTNUMBER=4000000;
    private static int dayCounter;
    private static int minuteCounter;

    
 

    
    
    @Schedule(second = "0", minute = "*/1", hour = "*", persistent = false)
    public void minuteTimeOut() {
        System.out.println("update minute counter");
        updateMinuteCounter(true);
    }
    
    
    @Schedule(second = "0", minute = "0", hour = "0", timezone="GMT", persistent = false)
    private void dayTimeOut() {
        updateDayCounter(true);
    }

    private synchronized void updateMinuteCounter(Boolean reset) {
        if (reset) {
            minuteCounter = 0;
        } else {
            minuteCounter = minuteCounter + 1;
        }
    }
    
    private synchronized void updateDayCounter(Boolean reset) {
        if (reset) {
            dayCounter = 0;
        } else {
            dayCounter = dayCounter + 1;
        }
    }
    
    public boolean dayTokenAvailable() {
        return dayCounter < MAXDAYREQUESTNUMBER;
    }
    
    public boolean minuteTokenAvailable() {
        return minuteCounter < MAXMINUTEREQUESTNUMBER;
    }
    
            
}
