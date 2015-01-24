/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.weather;

import it.polimi.meteocal.weather.WeatherChecker;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Andrea
 */
public class WeatherCheckerTest {
    
    private WeatherChecker weatherChecker;
    private final String city = "Prevalle,IT";
    private JsonObject obj;
    List<Date> dates = new ArrayList<>();

    
    public WeatherCheckerTest() {

    }
    
    
    @Before
    public void setUp() throws ParseException {
        weatherChecker = new WeatherChecker();
        weatherChecker.connection = mock(WeatherConnection.class);
        try (JsonReader jsonReader = Json.createReader(new StringReader("{\"cod\":\"200\",\"message\":0.0124,\"city\":{\"id\":\"6534559\",\"name\":\"Prevalle\",\"coord\":{\"lon\":10.4215,\"lat\":45.5493},\"country\":\"Italy\",\"population\":0},\"cnt\":10,\"list\":[{\"dt\":1421665200,\"temp\":{\"day\":265.35,\"min\":262.68,\"max\":265.35,\"night\":262.77,\"eve\":263.26,\"morn\":265.35},\"pressure\":818.69,\"humidity\":81,\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"speed\":0.71,\"deg\":65,\"clouds\":36},{\"dt\":1421751600,\"temp\":{\"day\":267.3,\"min\":262.05,\"max\":267.57,\"night\":263.15,\"eve\":265.07,\"morn\":262.05},\"pressure\":818.16,\"humidity\":86,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":0.51,\"deg\":78,\"clouds\":0},{\"dt\":1421838000,\"temp\":{\"day\":267.71,\"min\":262.85,\"max\":268.89,\"night\":268.89,\"eve\":267.94,\"morn\":262.85},\"pressure\":818.43,\"humidity\":80,\"weather\":[{\"id\":601,\"main\":\"Snow\",\"description\":\"snow\",\"icon\":\"13d\"}],\"speed\":0.62,\"deg\":62,\"clouds\":88,\"snow\":4},{\"dt\":1421924400,\"temp\":{\"day\":271.8,\"min\":268.8,\"max\":271.8,\"night\":269.72,\"eve\":270.44,\"morn\":268.8},\"pressure\":812.84,\"humidity\":91,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"speed\":0.77,\"deg\":55,\"clouds\":56},{\"dt\":1422010800,\"temp\":{\"day\":274.49,\"min\":269.47,\"max\":274.49,\"night\":269.63,\"eve\":272.13,\"morn\":269.47},\"pressure\":875.92,\"humidity\":0,\"weather\":[{\"id\":600,\"main\":\"Snow\",\"description\":\"light snow\",\"icon\":\"13d\"}],\"speed\":0.79,\"deg\":23,\"clouds\":73,\"rain\":0.32,\"snow\":0.07},{\"dt\":1422097200,\"temp\":{\"day\":273.58,\"min\":268.5,\"max\":273.58,\"night\":268.66,\"eve\":271.23,\"morn\":268.5},\"pressure\":877.01,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":0.94,\"deg\":21,\"clouds\":6},{\"dt\":1422183600,\"temp\":{\"day\":272.56,\"min\":267.83,\"max\":272.56,\"night\":269.54,\"eve\":270.79,\"morn\":267.83},\"pressure\":881.16,\"humidity\":0,\"weather\":[{\"id\":600,\"main\":\"Snow\",\"description\":\"light snow\",\"icon\":\"13d\"}],\"speed\":0.65,\"deg\":2,\"clouds\":64,\"snow\":0.45},{\"dt\":1422270000,\"temp\":{\"day\":270.94,\"min\":265.14,\"max\":270.94,\"night\":265.14,\"eve\":268.15,\"morn\":268.93},\"pressure\":879.13,\"humidity\":0,\"weather\":[{\"id\":600,\"main\":\"Snow\",\"description\":\"light snow\",\"icon\":\"13d\"}],\"speed\":0.78,\"deg\":17,\"clouds\":29,\"snow\":0.31},{\"dt\":1422356400,\"temp\":{\"day\":270.13,\"min\":261.84,\"max\":270.13,\"night\":261.84,\"eve\":266.98,\"morn\":262.5},\"pressure\":884.22,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":0.65,\"deg\":78,\"clouds\":4,\"snow\":0.02},{\"dt\":1422442800,\"temp\":{\"day\":269.89,\"min\":260.65,\"max\":269.89,\"night\":269.85,\"eve\":269.53,\"morn\":260.65},\"pressure\":884.65,\"humidity\":0,\"weather\":[{\"id\":600,\"main\":\"Snow\",\"description\":\"light snow\",\"icon\":\"13d\"}],\"speed\":0.86,\"deg\":12,\"clouds\":88,\"rain\":0.25,\"snow\":1.42}]}"))) {
            obj = jsonReader.readObject();
        }
        when(weatherChecker.connection.askWeather(city)).thenReturn(obj);
         for (int i = 19; i < 31; i++) {
            String string = ("January "+i+", 2015");
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(string);
            dates.add(DateManipulator.toDefaultDate(date));
        }
    }
    
    @Test
    public void addWeatherTest() {
        assertEquals(WeatherConditions.CLOUD, weatherChecker.addWeather(city, dates.get(0)));
        assertEquals(WeatherConditions.CLEAR, weatherChecker.addWeather(city, dates.get(1)));
        assertEquals(WeatherConditions.SNOW, weatherChecker.addWeather(city, dates.get(2)));
        assertEquals(WeatherConditions.CLOUD, weatherChecker.addWeather(city, dates.get(3)));
        assertEquals(WeatherConditions.SNOW, weatherChecker.addWeather(city, dates.get(4)));
        assertEquals(WeatherConditions.CLEAR, weatherChecker.addWeather(city, dates.get(5)));
        assertEquals(WeatherConditions.SNOW, weatherChecker.addWeather(city, dates.get(6)));
        assertEquals(WeatherConditions.SNOW, weatherChecker.addWeather(city, dates.get(7)));
        assertEquals(WeatherConditions.CLEAR, weatherChecker.addWeather(city, dates.get(8)));
        assertEquals(WeatherConditions.SNOW, weatherChecker.addWeather(city, dates.get(9)));
        assertEquals(WeatherConditions.UNAVAILABLE, weatherChecker.addWeather(city, dates.get(10)));
        
    } 

    

    

    
    
    @Test
    public void getForecastTest() {
        Map<Date, WeatherConditions> forecasts = weatherChecker.getForecast(city);
        assertEquals(WeatherConditions.CLOUD, forecasts.get(dates.get(0)));
        assertEquals(WeatherConditions.CLEAR, forecasts.get(dates.get(1)));
        assertEquals(WeatherConditions.SNOW, forecasts.get(dates.get(2)));
        assertEquals(WeatherConditions.CLOUD, forecasts.get(dates.get(3)));
        assertEquals(WeatherConditions.SNOW, forecasts.get(dates.get(4)));
        assertEquals(WeatherConditions.CLEAR, forecasts.get(dates.get(5)));
        assertEquals(WeatherConditions.SNOW, forecasts.get(dates.get(6)));
        assertEquals(WeatherConditions.SNOW, forecasts.get(dates.get(7)));
        assertEquals(WeatherConditions.CLEAR, forecasts.get(dates.get(8)));
        assertEquals(WeatherConditions.SNOW, forecasts.get(dates.get(9)));
    }
    
    @Test
    public void getWeatherForecastTest() {
        List<Forecast> forecasts = weatherChecker.getWeatherForecast(city);
        assertEquals(WeatherConditions.CLOUD.toString().toLowerCase(), forecasts.get(0).getCondition());
        assertEquals(WeatherConditions.CLEAR.toString().toLowerCase(), forecasts.get(1).getCondition());
        assertEquals(WeatherConditions.SNOW.toString().toLowerCase(), forecasts.get(2).getCondition());
        assertEquals(WeatherConditions.CLOUD.toString().toLowerCase(), forecasts.get(3).getCondition());
        assertEquals(WeatherConditions.SNOW.toString().toLowerCase(), forecasts.get(4).getCondition());
        assertEquals(WeatherConditions.CLEAR.toString().toLowerCase(), forecasts.get(5).getCondition());
        assertEquals(WeatherConditions.SNOW.toString().toLowerCase(), forecasts.get(6).getCondition());
        assertEquals(WeatherConditions.SNOW.toString().toLowerCase(), forecasts.get(7).getCondition());
        assertEquals(WeatherConditions.CLEAR.toString().toLowerCase(), forecasts.get(8).getCondition());
        assertEquals(WeatherConditions.SNOW.toString().toLowerCase(), forecasts.get(9).getCondition());
        assertEquals(dates.get(0), forecasts.get(0).getDate());
        assertEquals(dates.get(1), forecasts.get(1).getDate());
        assertEquals(dates.get(2), forecasts.get(2).getDate());
        assertEquals(dates.get(3), forecasts.get(3).getDate());
        assertEquals(dates.get(4), forecasts.get(4).getDate());
        assertEquals(dates.get(5), forecasts.get(5).getDate());
        assertEquals(dates.get(6), forecasts.get(6).getDate());
        assertEquals(dates.get(7), forecasts.get(7).getDate());
        assertEquals(dates.get(8), forecasts.get(8).getDate());
        assertEquals(dates.get(9), forecasts.get(9).getDate());
    }
    

    
    public void isValidCityFormatTest() {
        assertTrue(weatherChecker.isValidCityFormat("Prevalle,IT"));
        assertTrue(weatherChecker.isValidCityFormat("prevalle,IT"));
        assertTrue(weatherChecker.isValidCityFormat("Prevalle,iT"));
        assertTrue(weatherChecker.isValidCityFormat("Prevalle,It"));
        assertTrue(weatherChecker.isValidCityFormat("Prevalle,BS,IT"));
        assertTrue(weatherChecker.isValidCityFormat("PREVALLE,IT"));
        assertTrue(weatherChecker.isValidCityFormat("Prevalle,it"));
        assertFalse(weatherChecker.isValidCityFormat("Pre3valle,IT"));
        assertFalse(weatherChecker.isValidCityFormat("PrevalleIT"));
        assertFalse(weatherChecker.isValidCityFormat("Prevalle,IT,"));
        assertFalse(weatherChecker.isValidCityFormat("Prevalle,"));
        assertFalse(weatherChecker.isValidCityFormat("Prevalle,33"));
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

  /*  private JsonObject createJsonObject() {
        JsonBuilderFactory factory = Json.createBuilderFactory(config);
        JsonObject value = factory.createObjectBuilder(); 
    }*/
}
