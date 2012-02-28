/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.versioning.decorator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.geotools.data.Query;
import org.geotools.util.DateRange;
import org.opengis.filter.identity.Version;

public class VersionDetail {
    public enum VersionType {
        Action, Date, DateRange, Index
    }

    private VersionType type;

    private Version.Action action;

    private Date date;

    private DateRange range;

    private Integer index;

    private static final String DATE_PREFIX = "date:";

    private static final String RANGE_PREFIX = "start:";

    private static final String RANGE_SEPARATOR = " end:";

    private static final String DATE_FORMAT = "EEE MMM dd kk:mm:ss ZZZ yyyy";

    /**
     * Creates a QueryVersion object describing the version filtering parameters
     * of the given Query object. If no version parameters can be determined,
     * returns null.
     * 
     * @param query
     * @return
     */
    public static VersionDetail extractVersionDetails(Query query) {
        String versionString = query.getVersion();
        if (versionString == null) {
            return null;
        } else if (versionString.startsWith(DATE_PREFIX)) {
            // dow mon dd hh:mm:ss zzz yyyy
            DateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
            try {
                Date date = fmt.parse(versionString.substring(DATE_PREFIX
                        .length()));
                return new VersionDetail(date);
            } catch (ParseException ex) {
                return null;
            }
        } else if (versionString.startsWith(RANGE_PREFIX)) {
            int sepIndex = versionString.indexOf(RANGE_SEPARATOR);
            if (sepIndex == -1) {
                return null;
            }
            DateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
            try {
                Date start = fmt.parse(versionString.substring(
                        RANGE_PREFIX.length(), sepIndex));
                Date end = fmt.parse(versionString.substring(sepIndex
                        + RANGE_SEPARATOR.length()));
                return new VersionDetail(start, end);
            } catch (ParseException ex) {
                return null;
            }
        } else if (Version.Action.FIRST.name().equals(versionString)) {
            return new VersionDetail(Version.Action.FIRST);
        } else if (Version.Action.LAST.name().equals(versionString)) {
            return new VersionDetail(Version.Action.LAST);
        } else if (Version.Action.ALL.name().equals(versionString)) {
            return new VersionDetail(Version.Action.ALL);
        } else if (Version.Action.NEXT.name().equals(versionString)) {
            return new VersionDetail(Version.Action.NEXT);
        } else if (Version.Action.PREVIOUS.name().equals(versionString)) {
            return new VersionDetail(Version.Action.PREVIOUS);
        } else {
            try {
                int index = Integer.parseInt(versionString);
                return new VersionDetail(index);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
    }

    public static boolean hasDateRange(Query query) {
        String versionString = query.getVersion();
        return versionString.startsWith(DATE_PREFIX);
    }

    public static DateRange getDateRange(Query query) {
        if (hasDateRange(query)) {
            String versionString = query.getVersion();
            int sepIndex = versionString.indexOf(RANGE_SEPARATOR);
            if (sepIndex == -1) {
                return null;
            }
            DateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
            try {
                Date start = fmt.parse(versionString.substring(
                        RANGE_PREFIX.length(), sepIndex));
                Date end = fmt.parse(versionString.substring(sepIndex
                        + RANGE_SEPARATOR.length()));
                return new DateRange(start, end);
            } catch (ParseException ex) {
                return null;
            }
        }
        return null;
    }

    public static boolean hasDate(Query query) {
        return false;
    }

    public static Date getDate(Query query) {
        return null;
    }

    public static boolean hasAction(Query query) {
        return false;

    }

    public static Version.Action getAction(Query query) {
        return null;

    }

    public static boolean hasIndex(Query query) {
        return false;

    }

    public static Integer getIndex(Query query) {
        return null;

    }

    public VersionDetail(Version.Action action) {
        this.action = action;
        this.type = VersionType.Action;
    }

    public VersionDetail(Date date) {
        this.date = date;
        this.type = VersionType.Date;
    }

    public VersionDetail(Date startDate, Date endDate) {
        this.range = new DateRange(startDate, endDate);
        this.type = VersionType.DateRange;
    }

    public VersionDetail(int index) {
        this.index = new Integer(index);
        this.type = VersionType.Index;
    }

    public VersionType getType() {
        return type;
    }

    public Version.Action getAction() {
        if (this.type.equals(VersionType.Action)) {
            return this.action;
        }
        return null;
    }

    public DateRange getRange() {
        if (this.type.equals(VersionType.DateRange)) {
            return this.range;
        }
        return null;
    }

    public Date getDate() {
        if (this.type.equals(VersionType.Date))
            return this.date;
        return null;
    }

    public Integer getIndex() {
        return this.type.equals(VersionType.Index) ? this.index : null;
    }

}
