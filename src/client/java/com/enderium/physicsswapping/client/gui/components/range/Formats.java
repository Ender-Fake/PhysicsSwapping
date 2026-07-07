package com.enderium.physicsswapping.client.gui.components.range;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Formats {

    private static DecimalFormat createFormat(int count) {
        DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        decimalSymbols.setDecimalSeparator('.');
        return new DecimalFormat("0." + ("#".repeat(count)), decimalSymbols);
    }

    private static final DecimalFormat[] FORMATS = new DecimalFormat[11];


    public static final DecimalFormat ZERO;
    public static final DecimalFormat THREE = createFormat(3);
    public static final DecimalFormat FIVE = createFormat(5);


    static {
        FORMATS[0] = new DecimalFormat("0");
        for (int i = 1; i < 11; i++) {
            FORMATS[i] = createFormat(i);
        }

        ZERO = FORMATS[0];
    }

    public static DecimalFormat getFormat(int count) {
        if (count < 0) return FORMATS[0];
        if (count > 11) return FORMATS[10];
        return FORMATS[count];
    }


}
