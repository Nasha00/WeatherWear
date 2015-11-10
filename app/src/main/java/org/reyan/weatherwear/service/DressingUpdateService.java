package org.reyan.weatherwear.service;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.reyan.weatherwear.activity.MainActivity;
import org.reyan.weatherwear.domain.Dressing;
import org.reyan.weatherwear.domain.Weather;
import org.reyan.weatherwear.utility.IconCode2ConditionMap;

/**
 * Created by reyan on 11/4/15.
 */
public class DressingUpdateService {

    public static boolean updateDressing(MainActivity mainActivity) {

        //Gather gender and style information from settings
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        boolean isMale = settings.getBoolean("gender", true);
        int temperature_preference =
                Integer.parseInt(settings.getString("temperature_preference", "-1"));
        int dressing_style =
                Integer.parseInt(settings.getString("dressing_style", "-1"));

        Weather weather = mainActivity.getWeather();
        Dressing dressing = mainActivity.getDressing();
        dressing.reset();

        //Get weather condition information
        String condition =
                IconCode2ConditionMap.ICON_CODE_2_CONDITION_MAP.get(weather.getIconCode());
        double tempC = weather.getTempC();
        double windSpeedKPH = weather.getWindSpeedKPH();

        if (temperature_preference == 0) {
            //Prefer Colder
            tempC = tempC + 5;
        } else if (temperature_preference == 1) {
            //Prefer Warmer
            tempC = tempC - 5;
        }

        /*
        class WrongWeatherConditonException extends Exception {
            public WrongWeatherConditonException(String msg){
                super(msg);
            }
        }*/

        if (dressing_style == -1) {
            //Casual
            switch (condition) {
                case "clear sky day":
                case "few clouds day":
                    dressing.setGlasses("sun glasses");
                    if (windSpeedKPH >= 10) {
                        dressing.setHat("hat");
                    }
                    if (tempC >= 26) {
                        if (isMale) {
                            dressing.setUpper("t-shirt");
                            dressing.setLower("shorts");
                        } else {
                            dressing.setDress("dress");
                        }
                        dressing.setShoes("sandal");
                    } else if (tempC >= 13) {
                        dressing.setUpper("long sleeve shirts");
                        dressing.setLower("trousers");
                        dressing.setShoes("sneaker");
                    } else {
                        dressing.setUpper("long sleeve shirts and jacket");
                        dressing.setLower("trousers");
                        dressing.setShoes("boot");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                case "clear sky night":
                case "few clouds night":
                case "scattered clouds day":
                case "scattered clouds night":
                case "broken clouds day":
                case "broken clouds night":
                case "mist day":
                case "mist night":
                    if (windSpeedKPH >= 10) {
                        dressing.setHat("hat");
                    }
                    if (tempC >= 26) {
                        if (isMale) {
                            dressing.setUpper("t-shirt");
                            dressing.setLower("shorts");
                        } else {
                            dressing.setDress("dress");
                        }
                        dressing.setShoes("sandal");
                    } else if (tempC >= 13) {
                        dressing.setUpper("long sleeve shirts");
                        dressing.setLower("trousers");
                        dressing.setShoes("sneaker");
                    } else {
                        dressing.setUpper("long sleeve shirts and jacket");
                        dressing.setLower("trousers");
                        dressing.setShoes("boot");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                case "shower rain day":
                case "shower rain night":
                case "rain day":
                case "rain night":
                case "thunderstorm day":
                case "thunderstorm night":
                    dressing.setHat("hat");
                    dressing.setUmbrella("umbrella");
                    if (tempC >= 26) {
                        dressing.setUpper("t-shirt");
                        dressing.setLower("shorts");
                        dressing.setShoes("sandal");
                    } else if (tempC >= 13) {
                        dressing.setUpper("long sleeve shirts");
                        dressing.setLower("trousers");
                        dressing.setShoes("sneaker");
                    } else {
                        dressing.setUpper("long sleeve shirts and jacket");
                        dressing.setLower("trousers");
                        dressing.setShoes("boot");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                case "snow day":
                case "snow night":
                    dressing.setHat("hat");
                    if (tempC >= 26) {
                        dressing.setUpper("t-shirt");
                        dressing.setLower("shorts");
                        dressing.setShoes("sandal");
                    } else if (tempC >= 13) {
                        dressing.setUpper("long sleeve shirts");
                        dressing.setLower("trousers");
                        dressing.setShoes("sneaker");
                    } else {
                        dressing.setUpper("long sleeve shirts and jacket");
                        dressing.setLower("trousers");
                        dressing.setShoes("boot");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                /*
                default:
                    throw new WrongWeatherConditonException("Wrong weather condition");
                */
            }
        } else if (dressing_style == 0){
            //Formal
            switch (condition) {
                case "clear sky day":
                case "few clouds day":
                    dressing.setGlasses("sun glasses");
                    if (windSpeedKPH >= 10) {
                        dressing.setHat("hat");
                    }
                    if (tempC >= 26) {
                        if (isMale) {
                            dressing.setUpper("dress shirt");
                            dressing.setLower("suit pants");
                        } else {
                            dressing.setDress("formal dress");
                        }
                        dressing.setShoes("sandal");
                    } else if (tempC >= 13) {
                        dressing.setUpper("suit jacket");
                        dressing.setLower("suit pant");
                        dressing.setShoes("dress shoes");
                    } else {
                        dressing.setUpper("suit jacket");
                        dressing.setLower("suit pant");
                        dressing.setShoes("dress shoes");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                case "clear sky night":
                case "few clouds night":
                case "scattered clouds day":
                case "scattered clouds night":
                case "broken clouds day":
                case "broken clouds night":
                case "mist day":
                case "mist night":
                    if (windSpeedKPH >= 10) {
                        dressing.setHat("hat");
                    }
                    if (tempC >= 26) {
                        if (isMale) {
                            dressing.setUpper("dress shirt");
                            dressing.setLower("suit pants");
                        } else {
                            dressing.setDress("formal dress");
                        }
                        dressing.setShoes("sandal");
                    } else if (tempC >= 13) {
                        dressing.setUpper("suit jacket");
                        dressing.setLower("suit pant");
                        dressing.setShoes("dress shoes");
                    } else {
                        dressing.setUpper("suit jacket");
                        dressing.setLower("suit pant");
                        dressing.setShoes("dress shoes");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                case "shower rain day":
                case "shower rain night":
                case "rain day":
                case "rain night":
                case "thunderstorm day":
                case "thunderstorm night":
                    dressing.setHat("hat");
                    dressing.setUmbrella("umbrella");
                    if (tempC >= 26) {
                        if (isMale) {
                            dressing.setUpper("dress shirt");
                            dressing.setLower("suit pants");
                        } else {
                            dressing.setDress("formal dress");
                        }
                        dressing.setShoes("sandal");
                    } else if (tempC >= 13) {
                        dressing.setUpper("suit jacket");
                        dressing.setLower("suit pant");
                        dressing.setShoes("dress shoes");
                    } else {
                        dressing.setUpper("suit jacket");
                        dressing.setLower("suit pant");
                        dressing.setShoes("dress shoes");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                case "snow day":
                case "snow night":
                    dressing.setHat("hat");
                    if (tempC >= 26) {
                        if (isMale) {
                            dressing.setUpper("dress shirt");
                            dressing.setLower("suit pants");
                        } else {
                            dressing.setDress("formal dress");
                        }
                        dressing.setShoes("sandal");
                    } else if (tempC >= 13) {
                        dressing.setUpper("suit jacket");
                        dressing.setLower("suit pant");
                        dressing.setShoes("dress shoes");
                    } else {
                        dressing.setUpper("suit jacket");
                        dressing.setLower("suit pant");
                        dressing.setShoes("dress shoes");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                /*
                default:
                    throw new WrongWeatherConditonException("Wrong weather condition");
                */
            }

        }else {
            //Sports
            switch (condition) {
                case "clear sky day":
                case "few clouds day":
                    dressing.setGlasses("sun glasses");
                    if (windSpeedKPH >= 10) {
                        dressing.setHat("hat");
                    }
                    if (tempC >= 26) {
                        if (isMale) {
                            dressing.setUpper("t-shirt");
                            dressing.setLower("shorts");
                        } else {
                            dressing.setDress("dress");
                        }
                        dressing.setShoes("flip-flop");
                    } else if (tempC >= 13) {
                        dressing.setUpper("long sleeve shirts");
                        dressing.setLower("trousers");
                        dressing.setShoes("running shoe");
                    } else {
                        dressing.setUpper("long sleeve shirts and jacket");
                        dressing.setLower("trousers");
                        dressing.setShoes("running shoe");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                case "clear sky night":
                case "few clouds night":
                case "scattered clouds day":
                case "scattered clouds night":
                case "broken clouds day":
                case "broken clouds night":
                case "mist day":
                case "mist night":
                    if (windSpeedKPH >= 10) {
                        dressing.setHat("hat");
                    }
                    if (tempC >= 26) {
                        dressing.setUpper("t-shirt");
                        dressing.setLower("shorts");
                        dressing.setShoes("flip-flop");
                    } else if (tempC >= 13) {
                        dressing.setUpper("long sleeve shirts");
                        dressing.setLower("trousers");
                        dressing.setShoes("running shoe");
                    } else {
                        dressing.setUpper("long sleeve shirts and jacket");
                        dressing.setLower("trousers");
                        dressing.setShoes("running shoe");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                case "shower rain day":
                case "shower rain night":
                case "rain day":
                case "rain night":
                case "thunderstorm day":
                case "thunderstorm night":
                    dressing.setHat("hat");
                    dressing.setUmbrella("umbrella");
                    if (tempC >= 26) {
                        dressing.setUpper("t-shirt");
                        dressing.setLower("shorts");
                        dressing.setShoes("running shoe");
                    } else if (tempC >= 13) {
                        dressing.setUpper("long sleeve shirts");
                        dressing.setLower("trousers");
                        dressing.setShoes("running shoe");
                    } else {
                        dressing.setUpper("long sleeve shirts and jacket");
                        dressing.setLower("trousers");
                        dressing.setShoes("running shoe");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                case "snow day":
                case "snow night":
                    dressing.setHat("hat");
                    if (tempC >= 26) {
                        dressing.setUpper("t-shirt");
                        dressing.setLower("shorts");
                        dressing.setShoes("running shoe");
                    } else if (tempC >= 13) {
                        dressing.setUpper("long sleeve shirts");
                        dressing.setLower("trousers");
                        dressing.setShoes("running shoe");
                    } else {
                        dressing.setUpper("long sleeve shirts and jacket");
                        dressing.setLower("trousers");
                        dressing.setShoes("running shoe");
                        dressing.setGlove("glove");
                        dressing.setNeck("scarf");
                    }
                    break;
                /*
                default:
                    throw new WrongWeatherConditonException("Wrong weather condition");
                */
            }
        }
        return true;
    }
}
