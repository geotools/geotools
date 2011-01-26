/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import java.io.IOException;
import java.net.URL;

import org.junit.*;
import static org.junit.Assert.*;

import org.geotools.image.io.metadata.GeographicMetadata;
import org.geotools.image.io.text.DefaultTextMetadataParser;
import org.geotools.image.io.text.TextMetadataParser;
import org.geotools.image.io.text.TextMetadataTest;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.Datum;


/**
 * Tests the {@link MetadataReader} implementation.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Cédric Briançon
 */
public final class MetadataReaderTest {
    /**
     * The input resource. It should be an xml file which contains metadata declaration.
     */
    private final URL in = TextMetadataTest.class.getResource("metadata.txt");

    /**
     * Tests the formatting.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testFormat() throws IOException {
        final GeographicMetadata metadata = new GeographicMetadata();
        final MetadataReader parser = new MetadataReader();
        final DefaultTextMetadataParser textMetadata = new DefaultTextMetadataParser();
        textMetadata.setGeographicMetadata(metadata);
        /*
         * Do not add a COORDINATE_REFERENCE_SYSTEM property, because we want
         * to test the MetadataReader capability to create it from scratch.
         */
        textMetadata.addAlias(TextMetadataParser.X_MINIMUM,          "XMinimum");
        textMetadata.addAlias(TextMetadataParser.X_MAXIMUM,          "XMaximum");
        textMetadata.addAlias(TextMetadataParser.Y_MINIMUM,          "YMinimum");
        textMetadata.addAlias(TextMetadataParser.Y_MAXIMUM,          "YMaximum");
        textMetadata.addAlias(TextMetadataParser.X_RESOLUTION,       "XResolution");
        textMetadata.addAlias(TextMetadataParser.Y_RESOLUTION,       "YResolution");
        textMetadata.addAlias(TextMetadataParser.UNIT,               "Unit");
        textMetadata.addAlias(TextMetadataParser.PROJECTION,         "Projection");
        textMetadata.addAlias(TextMetadataParser.CENTRAL_MERIDIAN,   "Central meridian");
        textMetadata.addAlias(TextMetadataParser.LATITUDE_OF_ORIGIN, "Latitude of origin");
        textMetadata.addAlias(TextMetadataParser.FALSE_EASTING,      "False easting");
        textMetadata.addAlias(TextMetadataParser.FALSE_NORTHING,     "False northing");
        textMetadata.addAlias(TextMetadataParser.ELLIPSOID,          "Ellipsoid");
        textMetadata.addAlias(TextMetadataParser.DATUM,              "Datum");
        textMetadata.addAlias(TextMetadataParser.WIDTH,              "Width");
        textMetadata.addAlias(TextMetadataParser.HEIGHT,             "Height");
        textMetadata.addAlias(TextMetadataParser.COORDINATE_REFERENCE_SYSTEM, "Coordinate Reference System");
        textMetadata.addAlias(TextMetadataParser.COORDINATE_SYSTEM,  "Coordinate System");
        textMetadata.load(in);
        parser.setGeographicMetadata(metadata);

        final GridEnvelope range = parser.getGridRange();
        assertEquals("Width",  800, range.getHigh(0));
        assertEquals("Height", 600, range.getHigh(1));

        final CoordinateSystemAxis axisOne = parser.getAxis(0);
        assertTrue(axisOne.getDirection() == AxisDirection.valueOf("east"));
        assertTrue(axisOne.getName().getCode().equalsIgnoreCase("x"));

        final Datum datum = parser.getDatum();
        assertTrue(datum.getName().getCode().equals("Clarke 1866"));

        final CoordinateReferenceSystem notExpectedCRS = DefaultGeographicCRS.WGS84;
        final CoordinateReferenceSystem     createdCRS = parser.getCoordinateReferenceSystem();
        assertFalse(notExpectedCRS == createdCRS);
    }
}
