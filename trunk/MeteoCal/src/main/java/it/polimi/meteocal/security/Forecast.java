/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Andrea
 */
public class Forecast implements Comparable<Forecast>{
   
    private String condition;
    private Date date;
    private int tmin;
    private int tmax;
    private final static int KELVINCONSTANT = 273;

 

    public Forecast(String condition, Date date, int tmin, int tmax) {
        this.condition = condition.toLowerCase();
        this.date = date;
        this.tmin = tmin - KELVINCONSTANT;
        this.tmax = tmax - KELVINCONSTANT;

    }

    
    
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTmin() {
        return tmin;
    }

    public void setTmin(int tmin) {
        this.tmin = tmin;
    }

    public int getTmax() {
        return tmax;
    }

    public void setTmax(int tmax) {
        this.tmax = tmax;
    }
    
    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d", Locale.US);
        return dateFormat.format(date);
    }

    @Override
    public int compareTo(Forecast o) {
        if (o.getDate().after(this.date)) {
            return -1;
        } else {
            if (o.getDate().before(this.date)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    
    
}
