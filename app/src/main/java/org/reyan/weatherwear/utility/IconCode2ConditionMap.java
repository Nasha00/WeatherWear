package org.reyan.weatherwear.utility;

import java.util.HashMap;

/**
 * Created by reyan on 11/10/15.
 */
public class IconCode2ConditionMap {

    public static final HashMap<String, String> ICON_CODE_2_CONDITION_MAP =
            new HashMap<String, String>();

    static {
        ICON_CODE_2_CONDITION_MAP.put("01d", "clear sky day");
        ICON_CODE_2_CONDITION_MAP.put("01n", "clear sky night");
        ICON_CODE_2_CONDITION_MAP.put("02d", "few clouds day");
        ICON_CODE_2_CONDITION_MAP.put("02n", "few clouds night");
        ICON_CODE_2_CONDITION_MAP.put("03d", "scattered clouds day");
        ICON_CODE_2_CONDITION_MAP.put("03n", "scattered clouds night");
        ICON_CODE_2_CONDITION_MAP.put("04d", "broken clouds day");
        ICON_CODE_2_CONDITION_MAP.put("04n", "broken clouds night");
        ICON_CODE_2_CONDITION_MAP.put("09d", "shower rain day");
        ICON_CODE_2_CONDITION_MAP.put("09n", "shower rain night");
        ICON_CODE_2_CONDITION_MAP.put("10d", "rain day");
        ICON_CODE_2_CONDITION_MAP.put("10n", "rain night");
        ICON_CODE_2_CONDITION_MAP.put("11d", "thunderstorm day");
        ICON_CODE_2_CONDITION_MAP.put("11n", "thunderstorm night");
        ICON_CODE_2_CONDITION_MAP.put("13d", "snow day");
        ICON_CODE_2_CONDITION_MAP.put("13n", "snow night");
        ICON_CODE_2_CONDITION_MAP.put("50d", "mist day");
        ICON_CODE_2_CONDITION_MAP.put("50n", "mist night");
    }

}
