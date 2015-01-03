/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.schedule.DateManipulator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Andrea
 */
@Stateless
public class WeatherChecker {

    private static final String APPID ="9d8f6daef04fa46457a706f694d929b7";
    private static final String BASEURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";

   
    private String commonTranslationOperation(JsonObject inner) {
        JsonArray weather = inner.getJsonArray(JsonWeatherParam.WEATHER.toString().toLowerCase());
        JsonObject mainWeather = weather.getJsonObject(0);
        JsonString s = mainWeather.getJsonString(JsonWeatherParam.MAIN.toString().toLowerCase());
        return s.getString();
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
        return "no weather information";
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
    

    
    private JsonObject getWeather(String city) {
        Client client = ClientBuilder.newClient();
        String url = String.format("%s%s&cnt=10&mode=json&%s", BASEURL, city, APPID );
        System.out.println(url);
        JsonObject model =  client.target(url)
                            .request(MediaType.TEXT_PLAIN)
                            .get(JsonObject.class);
        return model;
    }
    
    public WeatherConditions addWeather(String city, Date date) {
        JsonObject model = getWeather(city);
        String s = translateWeather(model, DateManipulator.toDefaultDate(date));
        return toWeatherConditions(s);
    }
    
    public Map<Date, WeatherConditions> getForecast(String city) {
        JsonObject model = getWeather(city);
        return translateWeather(model);
    }
    
    public List<Forecast> getWeatherForecast(String city) {
        JsonObject model = getWeather(city);
        return translateWeather2(model);
    }

    private WeatherConditions toWeatherConditions(String s) {
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
    
    private List<Forecast> translateWeather2(JsonObject model) {
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
            forecast.add(new Forecast(mainWeather, d, tmin, tmax));
        } 
        return forecast;
    }
}
