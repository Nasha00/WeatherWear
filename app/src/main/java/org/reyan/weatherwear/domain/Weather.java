package org.reyan.weatherwear.domain;

/**
 * Created by reyan on 11/4/15.
 */
public class Weather {

    private String cityName = "";  // San Diego
    private String countryName = "";  // US

    private String iconCode = "";  // 02d, 02n, etc.

    private double tempF;  // Fahrenheit degree
    private double tempC;  // Centigrade degree

    private double windDegrees;  // degree
    private double windSpeedKPH;  // kph
    private double windSpeedMPH;  // mph

    private double humidity;  // %
    private double pressure;  // kPa

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }

    public String getIconCode() { return iconCode; }
    public void setIconCode(String iconCode) { this.iconCode = iconCode; }

    public double getTempF() { return tempF; }
    public void setTempF(double tempF) { this.tempF = tempF; }

    public double getTempC() { return tempC; }
    public void setTempC(double tempC) { this.tempC = tempC; }

    public double getWindDegrees() { return windDegrees; }
    public void setWindDegrees(double windDegrees) { this.windDegrees = windDegrees; }

    public double getWindSpeedKPH() { return windSpeedKPH; }
    public void setWindSpeedKPH(double windSpeedKPH) { this.windSpeedKPH = windSpeedKPH; }

    public double getWindSpeedMPH() { return windSpeedMPH; }
    public void setWindSpeedMPH(double windSpeedMPH) { this.windSpeedMPH = windSpeedMPH; }

    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }

    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }

}
