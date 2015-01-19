/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.weather;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Andrea
 */
@Stateless
public class WeatherConnection {
    
    
    private static final String APPID ="9d8f6daef04fa46457a706f694d929b7";
    private static final String BASEURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    @EJB
    protected WeatherCounter counter;
    
    

    public JsonObject askWeather(String city) {
        Client client = ClientBuilder.newClient();
        String cityNoSpace = city.replaceAll("\\s+", "_");
        System.out.println(cityNoSpace);
        String url = String.format("%s%s&cnt=10&mode=json&%s", BASEURL, cityNoSpace, APPID );
        System.out.println(url);
        if (counter.minuteTokenAvailable() && counter.dayTokenAvailable()) {
            try {
                JsonObject model = client.target(url)
                        .request(MediaType.APPLICATION_JSON)
                        .get(JsonObject.class);
                return model;
            } catch (Exception e) {
                Logger.getGlobal().log(Level.WARNING, e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }
    
}
