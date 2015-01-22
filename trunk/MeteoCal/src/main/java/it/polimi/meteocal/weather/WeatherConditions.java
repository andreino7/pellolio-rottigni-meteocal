/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.weather;

/**
 *
 * @author Andrea
 */
public enum WeatherConditions {
    CLEAR, RAIN, CLOUD, SNOW, UNAVAILABLE;
    
    private String title;
    
    static {
        CLEAR.title = "CLEAR";
        RAIN.title = "RAIN";
        CLOUD.title = "SNOW";
        SNOW.title = "CLOUD";
    }

    public String getTitle() {
        return title;
    }

}
