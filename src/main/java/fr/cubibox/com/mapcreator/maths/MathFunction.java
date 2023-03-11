package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;

import java.math.BigDecimal;

public class MathFunction {

    public static float round(float var){
        return BigDecimal.valueOf(var)
                .setScale(0, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();
    }

    public static float round(double var){
        return BigDecimal.valueOf(var)
                .setScale(0, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();
    }
}
