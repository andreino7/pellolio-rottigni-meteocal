/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.weather;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;

/**
 *
 * @author Andrea
 */
@Stateless
public class WeatherChecker {

    @EJB
    WeatherConnection connection;

    
        
    
    public WeatherConditions addWeather(String city, Date date) {
        if(city!=null && isValidCityFormat(city) && date!=null) {
            JsonObject model = connection.askWeather(city);
            String s = new String();
            if (model != null) {
                s = translateWeather(model, DateManipulator.toDefaultDate(date));
            }
            return toWeatherConditions(s);
        } else {
            return null;
        }
    }
    
    public Map<Date, WeatherConditions> getForecast(String city) {
        if (city != null && isValidCityFormat(city)) {
            JsonObject model = connection.askWeather(city);
            if (model != null) {
                return translateWeather(model);
            } else {
                return null;
            }
        } else {
            return null;
        }
        
    }
    
    public List<Forecast> getWeatherForecast(String city) {
        JsonObject model = connection.askWeather(city);
        if (city!=null && isValidCityFormat(city)) {
            if (model != null) {
                return translateWeatherInList(model);
            } else {
                return null;
            }
        } else {
           return null;
        }
    }
   

    
    private String translateWeather(JsonObject model, Date date) {
        JsonArray list = model.getJsonArray(JsonWeatherParam.LIST.toString().toLowerCase());
        Iterator i = list.iterator();
        while (i.hasNext()) {            
            JsonObject inner = (JsonObject) i.next();
            JsonNumber dt = inner.getJsonNumber(JsonWeatherParam.DT.toString().toLowerCase());
            Date d = timestampConverter(dt.intValue());
            if (d.equals(date)) {
                return commonTranslationOperation(inner);
            } 
        } 
        return "";
    }
    
    private String commonTranslationOperation(JsonObject inner) {
        JsonArray weather = inner.getJsonArray(JsonWeatherParam.WEATHER.toString().toLowerCase());
        JsonObject mainWeather = weather.getJsonObject(0);
        JsonString s = mainWeather.getJsonString(JsonWeatherParam.MAIN.toString().toLowerCase());
        return s.getString();
    }
    
    private Map<Date, WeatherConditions> translateWeather(JsonObject model) {
        JsonArray list = model.getJsonArray(JsonWeatherParam.LIST.toString().toLowerCase());
        Iterator i = list.iterator();
        Map<Date, WeatherConditions> weatherForecast = new TreeMap<>();
        while (i.hasNext()) {            
            JsonObject inner = (JsonObject) i.next();
            JsonNumber dt = inner.getJsonNumber(JsonWeatherParam.DT.toString().toLowerCase());
            Date d = timestampConverter(dt.intValue());
                weatherForecast.put(d,toWeatherConditions(commonTranslationOperation(inner)));
        } 
       return weatherForecast;
    }
    
    private Date timestampConverter(int timestamp) {
        Date date = new Date((long) timestamp*1000);
        return DateManipulator.toDefaultDate(date);
    }


    private WeatherConditions toWeatherConditions(String s) {
        System.out.println(s);
        switch (s) {
            case ("Clear"):
                return WeatherConditions.CLEAR;
            case ("Rain"):
                return WeatherConditions.RAIN;
            case ("Clouds"):
                return WeatherConditions.CLOUD;
            case ("Snow"):
                return WeatherConditions.SNOW;
            default:
                return WeatherConditions.UNAVAILABLE;
        }
    }
    
    private List<Forecast> translateWeatherInList(JsonObject model) {
        JsonArray list = model.getJsonArray(JsonWeatherParam.LIST.toString().toLowerCase());
        Iterator i = list.iterator();
        List<Forecast> forecast = new ArrayList<>();
        while (i.hasNext()) {            
            JsonObject inner = (JsonObject) i.next();
            JsonNumber dt = inner.getJsonNumber(JsonWeatherParam.DT.toString().toLowerCase());
            Date d = timestampConverter(dt.intValue());
            JsonObject temp = inner.getJsonObject(JsonWeatherParam.TEMP.toString().toLowerCase());
            JsonNumber min = temp.getJsonNumber(JsonWeatherParam.MIN.toString().toLowerCase());
            JsonNumber max = temp.getJsonNumber(JsonWeatherParam.MAX.toString().toLowerCase());
            int tmin=min.intValue();
            int tmax=max.intValue();
            String mainWeather = commonTranslationOperation(inner);
            forecast.add(new Forecast(toWeatherConditions(mainWeather), d, tmin, tmax));
        } 
        return forecast;
    }
    
    public boolean isValidCityFormat(String city) {
        if (city != null) {
            String regex = "[A-Za-z_]+[,]{1}[A-Za-z]{2}([,]{1}[A-Za-z]{2})?";
            return city.matches(regex);
        } else {
            return false;
        }
    }

}
