/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.weather;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Andrea
 */
public class DateManipulator {
      
    public static Date toDefaultDate(Date date) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 00);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        c.set(Calendar.MILLISECOND, 00);
        return c.getTime();
    }
    
    public static Date toNewEndDate(Date oldStartdate, Date startDate, Date oldEndDate) {
        long startDifference = startDate.getTime() - oldStartdate.getTime();
        Date endDate = new Date(oldEndDate.getTime() + startDifference);
        return endDate;
    }

    public static Date subtractDays(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -i);
        System.out.println(cal.getTime());
        return cal.getTime();
    }
    
    public static Date addDays(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, i);
        return cal.getTime();
    }

}
