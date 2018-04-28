/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2017, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Corresponds to one detail item of an WFS exception report. Contained by {@link WFSException}.
 *
 * @author awaterme
 */
public final class ExceptionDetails implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 7073282354241139071L;

    private String code;

    private String locator;

    private List<String> texts;

    public ExceptionDetails(String code, String locator, List<String> texts) {
        super();
        this.code = code;
        this.locator = locator;
        if (texts == null) {
            texts = Collections.emptyList();
        } else {
            texts = new ArrayList<>(texts);
        }
        this.texts = texts;
    }

    public String getCode() {
        return code;
    }

    public List<String> getTexts() {
        return Collections.unmodifiableList(texts);
    }

    public String getLocator() {
        return locator;
    }

    @Override
    public String toString() {
        return "ExceptionDetails [code=" + code + ", locator=" + locator + ", texts=" + texts + "]";
    }
}
