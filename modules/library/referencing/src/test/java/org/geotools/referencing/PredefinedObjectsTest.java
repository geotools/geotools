/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.measure.unit.SI;

import org.opengis.referencing.datum.VerticalDatumType;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.cs.DefaultTimeCS;
import org.geotools.referencing.cs.DefaultVerticalCS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.datum.DefaultVerticalDatum;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests <cite>Well Know Text</cite> (WKT) formatting for some hard-coded, predefined objects.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class PredefinedObjectsTest {
    /**
     * Tests {@link DefaultCoordinateSystemAxis} constants.
     */
    @Test
    public void testAxis() {
        // Test Well Know Text
        assertEquals("x",         "AXIS[\"x\", EAST]",         DefaultCoordinateSystemAxis.X        .toWKT(0));
        assertEquals("y",         "AXIS[\"y\", NORTH]",        DefaultCoordinateSystemAxis.Y        .toWKT(0));
        assertEquals("z",         "AXIS[\"z\", UP]",           DefaultCoordinateSystemAxis.Z        .toWKT(0));
        assertEquals("Longitude", "AXIS[\"Longitude\", EAST]", DefaultCoordinateSystemAxis.LONGITUDE.toWKT(0));
        assertEquals("Latitude",  "AXIS[\"Latitude\", NORTH]", DefaultCoordinateSystemAxis.LATITUDE .toWKT(0));
        assertEquals("Altitude",  "AXIS[\"Altitude\", UP]",    DefaultCoordinateSystemAxis.ALTITUDE .toWKT(0));
        assertEquals("Time",      "AXIS[\"Time\", FUTURE]",    DefaultCoordinateSystemAxis.TIME     .toWKT(0));

        assertEquals("Longitude", "AXIS[\"Geodetic longitude\", EAST]",  DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE .toWKT(0));
        assertEquals("Longitude", "AXIS[\"Spherical longitude\", EAST]", DefaultCoordinateSystemAxis.SPHERICAL_LONGITUDE.toWKT(0));
        assertEquals("Latitude",  "AXIS[\"Geodetic latitude\", NORTH]",  DefaultCoordinateSystemAxis.GEODETIC_LATITUDE  .toWKT(0));
        assertEquals("Latitude",  "AXIS[\"Spherical latitude\", NORTH]", DefaultCoordinateSystemAxis.SPHERICAL_LATITUDE .toWKT(0));

        // Test localization
        assertEquals("English", "Time",  DefaultCoordinateSystemAxis.TIME.getAlias().iterator().next().toInternationalString().toString(Locale.ENGLISH));
        assertEquals("French",  "Temps", DefaultCoordinateSystemAxis.TIME.getAlias().iterator().next().toInternationalString().toString(Locale.FRENCH ));
        // TODO: remove cast and use static import once we are allowed to compile for J2SE 1.5.
        //       It will make the line much shorter!!

        // Test geocentric
        assertFalse("X",         DefaultCoordinateSystemAxis.X        .equals(DefaultCoordinateSystemAxis.GEOCENTRIC_X,        false));
        assertFalse("Longitude", DefaultCoordinateSystemAxis.LONGITUDE.equals(DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE,  true ));
        assertFalse("Longitude", DefaultCoordinateSystemAxis.LONGITUDE.equals(DefaultCoordinateSystemAxis.SPHERICAL_LONGITUDE, true ));
        assertFalse("Longitude", DefaultCoordinateSystemAxis.LONGITUDE.equals(DefaultCoordinateSystemAxis.SPHERICAL_LONGITUDE, false));

        // Test aliases in the special "longitude" and "latitude" cases.
        assertTrue ("Longitude", DefaultCoordinateSystemAxis.LONGITUDE.equals(DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE,  false));
        assertTrue ("Latitude",  DefaultCoordinateSystemAxis.LATITUDE .equals(DefaultCoordinateSystemAxis.GEODETIC_LATITUDE,   false));
        assertFalse("Lon/Lat",   DefaultCoordinateSystemAxis.LATITUDE .equals(DefaultCoordinateSystemAxis.LONGITUDE,           false));
    }

    /**
     * Tests {@link AbstractCS}.
     */
    @Test
    public void testCoordinateSystems() {
        // Test dimensions
        assertEquals("Cartesian 2D",   2, DefaultCartesianCS  .PROJECTED  .getDimension());
        assertEquals("Cartesian 3D",   3, DefaultCartesianCS  .GEOCENTRIC .getDimension());
        assertEquals("Ellipsoidal 2D", 2, DefaultEllipsoidalCS.GEODETIC_2D.getDimension());
        assertEquals("Ellipsoidal 3D", 3, DefaultEllipsoidalCS.GEODETIC_3D.getDimension());
        assertEquals("Vertical",       1, DefaultVerticalCS   .DEPTH      .getDimension());
        assertEquals("Temporal",       1, DefaultTimeCS       .DAYS       .getDimension());
    }

    /**
     * Test {@link AbstractDatum} and well-know text formatting.
     */
    @Test
    public void testDatum() {
        // WGS84 components and equalities
        assertEquals("Ellipsoid",     DefaultEllipsoid.WGS84,         DefaultGeodeticDatum.WGS84.getEllipsoid());
        assertEquals("PrimeMeridian", DefaultPrimeMeridian.GREENWICH, DefaultGeodeticDatum.WGS84.getPrimeMeridian());
        assertFalse ("VerticalDatum", DefaultVerticalDatum.GEOIDAL.equals(DefaultVerticalDatum.ELLIPSOIDAL));
        assertEquals("Geoidal",       VerticalDatumType.GEOIDAL,     DefaultVerticalDatum.GEOIDAL    .getVerticalDatumType());
        assertEquals("Ellipsoidal",   VerticalDatumType.ELLIPSOIDAL, DefaultVerticalDatum.ELLIPSOIDAL.getVerticalDatumType());

        // Test WKT
        assertEquals("Ellipsoid",     "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]",  DefaultEllipsoid.WGS84          .toWKT(0));
        assertEquals("PrimeMeridian", "PRIMEM[\"Greenwich\", 0.0]",                     DefaultPrimeMeridian.GREENWICH  .toWKT(0));
        assertEquals("VerticalDatum", "VERT_DATUM[\"Geoidal\", 2005]",                  DefaultVerticalDatum.GEOIDAL    .toWKT(0));
        assertEquals("VerticalDatum", "VERT_DATUM[\"Ellipsoidal\", 2002]",              DefaultVerticalDatum.ELLIPSOIDAL.toWKT(0));
        assertEquals("GeodeticDatum", "DATUM[\"WGS84\", "+
                                      "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]]", DefaultGeodeticDatum.WGS84      .toWKT(0));

        // Test properties
        final Map<String,Object> properties = new HashMap<String,Object>();
        properties.put("name",          "This is a name");
        properties.put("scope",         "This is a scope");
        properties.put("scope_fr",      "Valide dans ce domaine");
        properties.put("remarks",       "There is remarks");
        properties.put("remarks_fr",    "Voici des remarques");

        DefaultGeodeticDatum datum = new DefaultGeodeticDatum(properties,
                DefaultEllipsoid.createEllipsoid("Test", 1000, 1000, SI.METER),
                new DefaultPrimeMeridian("Test", 12));

        assertEquals("name",          "This is a name",         datum.getName   ().getCode());
        assertEquals("scope",         "This is a scope",        datum.getScope  ().toString(null));
        assertEquals("scope_fr",      "Valide dans ce domaine", datum.getScope  ().toString(Locale.FRENCH));
        assertEquals("remarks",       "There is remarks",       datum.getRemarks().toString(null));
        assertEquals("remarks_fr",    "Voici des remarques",    datum.getRemarks().toString(Locale.FRENCH));
    }

    /**
     * Tests {@link AbstractCRS}.
     */
    @Test
    public void testCoordinateReferenceSystems() {
        // Test dimensions
        assertEquals("WGS84 2D", 2, DefaultGeographicCRS.WGS84   .getCoordinateSystem().getDimension());
        assertEquals("WGS84 3D", 3, DefaultGeographicCRS.WGS84_3D.getCoordinateSystem().getDimension());

        // Test WKT
        assertEquals("WGS84",
                "GEOGCS[\"WGS84(DD)\", " +
                "DATUM[\"WGS84\", "+
                "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], "+
                "PRIMEM[\"Greenwich\", 0.0], "+
                "UNIT[\"degree\", 0.017453292519943295], "+
                "AXIS[\"Geodetic longitude\", EAST], "+
                "AXIS[\"Geodetic latitude\", NORTH]]",
                DefaultGeographicCRS.WGS84.toWKT(0));
    }

    /**
     * Test serialization of various objects.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        serialize(DefaultCoordinateSystemAxis.X);
        serialize(DefaultCoordinateSystemAxis.GEOCENTRIC_X);
        serialize(DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE);
        serialize(DefaultCartesianCS.PROJECTED);
        serialize(DefaultCartesianCS.GEOCENTRIC);
        serialize(DefaultEllipsoidalCS.GEODETIC_2D);
        serialize(DefaultEllipsoidalCS.GEODETIC_3D);
        serialize(DefaultPrimeMeridian.GREENWICH);
        serialize(DefaultGeodeticDatum.WGS84);
    }

    /**
     * Test the serialization of the given object.
     */
    private static void serialize(final Object object) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream out  = new ByteArrayOutputStream();
        final ObjectOutputStream    outs = new ObjectOutputStream(out);
        outs.writeObject(object);
        outs.close();

        final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        final Object test = in.readObject();
        in.close();

        assertEquals("Serialization", object, test);
        assertEquals("Serialization", object.hashCode(), test.hashCode());
    }
}
