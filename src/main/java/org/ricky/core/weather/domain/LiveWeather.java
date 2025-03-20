package org.ricky.core.weather.domain;

import lombok.Data;

@Data
public class LiveWeather {
    private String province;
    private String city;
    private String adcode;
    private String weather;
    private String temperature;
    private String winddirection;
    private String windpower;
    private String humidity;
    private String reporttime;
    private String temperature_float;
    private String humidity_float;
}