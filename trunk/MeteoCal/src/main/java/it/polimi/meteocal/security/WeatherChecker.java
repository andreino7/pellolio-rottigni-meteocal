/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
        return toDefaultDate(date);
    }
    
    private Date toDefaultDate(Date date) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 00);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        c.set(Calendar.MILLISECOND, 00);
        return c.getTime();
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
        String s = translateWeather(model, date);
        return toWeatherConditions(s);
    }
    
    public Map<Date, WeatherConditions> getForecast(String city) {
        JsonObject model = getWeather(city);
        return translateWeather(model);
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
}
