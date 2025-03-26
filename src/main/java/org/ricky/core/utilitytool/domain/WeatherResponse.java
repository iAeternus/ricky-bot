package org.ricky.core.utilitytool.domain;

import lombok.Data;

import java.util.List;

@Data
public class WeatherResponse {
    private String status;
    private String count;
    private String info;
    private String infocode;
    private List<LiveWeather> lives;

    public boolean isValid() {
        return "1".equals(status) && "OK".equals(info);
    }
}