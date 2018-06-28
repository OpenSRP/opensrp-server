package org.opensrp.stock.management;

import org.apache.commons.lang3.StringUtils;

public class Code {
    private final String code;

    protected Code() {
        this.code = "";
    }

    private Code(String code) {
        this.code = code.replaceAll("\\s", "");
    }

    public boolean isBlank() {
        return StringUtils.isBlank(this.code);
    }

    /**
     * Code equality ignores whitespace and case.
     * @param object the Code to test against.
     * @return true if both represent the same code, false otherwise.
     */
    @Override
    public final boolean equals(Object object) {
        if (null == object) {
            return false;
        }

        if (!(object instanceof Code)) {
            return false;
        }

        return this.code.equalsIgnoreCase(((Code) object).code);
    }

    @Override
    public final int hashCode() {
        return code.toUpperCase().hashCode();
    }

    public String toString() {
        return code;
    }

    public static final Code code(String code) {
        String workingCode = (null == code) ? "" : code;
        return new Code(workingCode);
    }
}

