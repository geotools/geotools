package com.esri.sde.sdk.client;

import java.util.Calendar;
import java.util.Date;

public class SeDate {

    @Deprecated
    public SeDate(Date date) {
    }

    public SeDate(Calendar cal) {
    }

    public String toWhereStr(SeConnection connection) {
        throw new UnsupportedOperationException("dummy method");
    }

}
