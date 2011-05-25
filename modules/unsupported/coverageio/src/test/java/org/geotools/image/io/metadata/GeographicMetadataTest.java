/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.metadata;

import java.util.Arrays;
import java.util.Collection;
import javax.imageio.metadata.IIOMetadataFormat;

import org.junit.*;
import org.opengis.metadata.spatial.PixelOrientation;
import static org.junit.Assert.*;


/**
 * Tests {@link GeographicMetadata}.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Cédric Briançon
 */
public final class GeographicMetadataTest {
    /**
     * Tests the geographic metadata format.
     */
    @Test
    public void testFormat() {
        final GeographicMetadata metadata = new GeographicMetadata();
        final Collection formats = Arrays.asList(metadata.getMetadataFormatNames());
        assertTrue(formats.contains(GeographicMetadataFormat.FORMAT_NAME));
        final IIOMetadataFormat format = metadata.getMetadataFormat(GeographicMetadataFormat.FORMAT_NAME);
        assertTrue(format instanceof GeographicMetadataFormat);

        final Collection crsChilds = Arrays.asList(format.getChildNames("crs"));
        assertTrue(crsChilds.contains("cs"));
        assertTrue(crsChilds.contains("datum"));
        assertEquals(IIOMetadataFormat.DATATYPE_STRING, format.getAttributeDataType("datum", "name"));
    }

    /**
     * Tests the setting of values in the metadata object.
     */
    @Test
    public void testSetting() {
        final GeographicMetadata metadata = new GeographicMetadata();
        final ImageReferencing referencing = metadata.getReferencing();
        referencing.addAxis("latitude",  "north", "degrees");
        referencing.addAxis("longitude", "east",  "degrees");
        referencing.setCoordinateSystem("WGS84", "geographic");
        referencing.setDatum("WGS84", "geodetic");        

        final ImageGeometry geometry = metadata.getGeometry();
        geometry.setPixelOrientation(PixelOrientation.CENTER.identifier());
        geometry.setOrdinateRange(0, -90.0,  90.0);
        geometry.setOrdinateRange(1, -180.0, 180.0);
        geometry.setGridRange(0, 0, 800);
        geometry.setGridRange(1, 0, 600);
        geometry.addOffsetVector(new double[]{12.5, 0.0});
        geometry.addOffsetVector(new double[]{ 0.0, 7.5});

        final String bandName = "temperature";
        metadata.addBand(bandName);
        assertTrue(metadata.getNumBands() == 1);
        assertTrue(metadata.getBand(0).getName().equals(bandName));
        final String text = metadata.toString();
        assertTrue(text.indexOf("name=\"latitude\"" ) >= 0);
        assertTrue(text.indexOf("name=\"longitude\"") >= 0);
        
        assertEquals(geometry.getPixelOrientation(), "center");
        assertTrue(geometry.getDimension() == 2);
        assertEquals(7.5, geometry.getOffsetVector(1)[1], 1E-6);
    }
}
