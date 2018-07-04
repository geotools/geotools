/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf.cv;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.imageio.Identification;
import org.geotools.imageio.netcdf.utilities.NetCDFCRSUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFTimeUtilities;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.SimpleInternationalString;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.TimeCS;
import org.opengis.referencing.datum.TemporalDatum;
import org.opengis.temporal.Position;
import ucar.nc2.dataset.CoordinateAxis;

/** A {@link CoordinateVariable} used to wrap Coordinates expressing climatological time */
public class ClimatologicalTimeCoordinateVariable extends CoordinateVariable<Date> {

    private static final String ORIGIN_DATE = "0000-01-01T00:00:00";

    private static final Logger LOGGER =
            Logger.getLogger(ClimatologicalTimeCoordinateVariable.class.toString());

    public ClimatologicalTimeCoordinateVariable(CoordinateAxis coordinateAxis) {
        super(Date.class, coordinateAxis);
        init();
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    protected synchronized TemporalCRS buildCoordinateReferenceSystem() {

        String t_datumName = new Identification("ISO8601", null, null, null).getName();
        TemporalCRS temporalCRS = null;
        String axisName = coordinateAxis.getFullName();
        try {
            String t_csName = "time_CS";
            final Map<String, String> csMap = Collections.singletonMap("name", t_csName);

            TimeCS timeCS =
                    NetCDFCRSUtilities.FACTORY_CONTAINER
                            .getCSFactory()
                            .createTimeCS(
                                    csMap,
                                    DefaultCoordinateSystemAxis.getPredefined(
                                            axisName, AxisDirection.FUTURE));

            // Creating the Temporal Datum
            if (t_datumName == null) {
                t_datumName = "Unknown";
            }
            final Map<String, String> datumMap = Collections.singletonMap("name", t_datumName);
            final Position timeOrigin =
                    new DefaultPosition(new SimpleInternationalString(ORIGIN_DATE));
            final TemporalDatum temporalDatum =
                    NetCDFCRSUtilities.FACTORY_CONTAINER
                            .getDatumFactory()
                            .createTemporalDatum(datumMap, timeOrigin.getDate());

            // Finally creating the Temporal CoordinateReferenceSystem
            String crsName = "time_CRS";
            final Map<String, String> crsMap = Collections.singletonMap("name", crsName);
            temporalCRS =
                    NetCDFCRSUtilities.FACTORY_CONTAINER
                            .getCRSFactory()
                            .createTemporalCRS(crsMap, temporalDatum, timeCS);
        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable to parse temporal CRS", e);
            }
            temporalCRS = null;
        } catch (ParseException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable to parse temporal CRS", e);
            }
            temporalCRS = null;
        }
        return temporalCRS;
    }

    @Override
    protected Date convertValue(Object o) {
        String coordVal = (String) o;
        Calendar cal = new GregorianCalendar(NetCDFTimeUtilities.UTC_TIMEZONE);
        cal.set(Calendar.YEAR, Integer.parseInt(coordVal.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.parseInt(coordVal.substring(4, 6)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(coordVal.substring(6, 8)));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(coordVal.substring(8, 10)));
        cal.set(Calendar.MINUTE, Integer.parseInt(coordVal.substring(10, 12)));
        cal.set(Calendar.SECOND, Integer.parseInt(coordVal.substring(12, 14)));
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
