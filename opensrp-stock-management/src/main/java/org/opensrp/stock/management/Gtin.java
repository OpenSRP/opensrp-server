package org.opensrp.stock.management;

import org.apache.commons.lang3.StringUtils;

public class Gtin {

    private String gtin;

    private Gtin() {}

    /**
     * Creates a new Gtin value.
     * @param gtin the gtin
     */
    public Gtin(String gtin) throws  Exception {
        if (!StringUtils.isNumeric(gtin)) {
            throw new Exception("GTIN should be numeric");
        }
        if (gtin.length() < 8 || gtin.length() > 14) {
            throw new Exception("Invalid GTIN length");
        }
        this.gtin = gtin;
    }
}

