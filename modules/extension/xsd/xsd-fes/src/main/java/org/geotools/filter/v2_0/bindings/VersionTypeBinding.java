/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0.bindings;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.namespace.QName;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.impl.DatatypeConverterImpl;
import org.geotools.xsd.InstanceComponent;
import org.geotools.xsd.SimpleBinding;
import org.opengis.filter.identity.Version;

/** Binding for FES 2.0 {@code VersionType} mapping to {@link Version} */
public class VersionTypeBinding implements SimpleBinding {

    @Override
    public QName getTarget() {
        return FES.VersionType;
    }

    @Override
    public Class<?> getType() {
        return Version.class;
    }

    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    @Override
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        final String content = (String) value;
        if (null == content || content.length() == 0) {
            return new Version();
        }
        try {
            Version.Action versionAction = Version.Action.valueOf(content);
            return new Version(versionAction);
        } catch (IllegalArgumentException e) {
            try {
                Integer index = Integer.valueOf(content);
                return new Version(index);
            } catch (NumberFormatException nfe) {
                Calendar dateTime = DatatypeConverterImpl.getInstance().parseDateTime(content);
                return new Version(dateTime.getTime());
            }
        }
    }

    @Override
    public String encode(Object object, String value) throws Exception {
        Version version = (Version) object;
        if (version.isEmpty()) {
            return null;
        }
        if (version.isDateTime()) {
            Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
            cal.setTimeInMillis(version.getDateTime().getTime());
            String dateTime = DatatypeConverterImpl.getInstance().printDateTime(cal);
            return dateTime;
        }
        if (version.isIndex()) {
            return String.valueOf(version.getIndex());
        }
        if (version.isVersionAction()) {
            return String.valueOf(version.getVersionAction());
        }
        throw new IllegalArgumentException("Empty version union");
    }
}
