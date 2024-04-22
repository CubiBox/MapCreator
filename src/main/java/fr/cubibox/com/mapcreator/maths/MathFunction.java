package fr.cubibox.com.mapcreator.maths;

import java.math.BigDecimal;

public class MathFunction {

    public static float round(float var){
        try {
            return BigDecimal.valueOf(var)
                    .setScale(0, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
        }
        catch (NumberFormatException nfe){
            return 0;
        }
    }

    public static float round(double var){
        try {
            return BigDecimal.valueOf(var)
                    .setScale(0, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
        }
        catch (NumberFormatException nfe){
            return 0;
        }
    }
}
