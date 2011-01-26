/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;

import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;

import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.geometry.GeneralDirectPosition;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link GeocentricTranslation} class.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class GeocentricTranslationTest {
    /**
     * Test case using example from EPSG Guidance Note number 7 part 2 (May 2005),
     * section 2.4.3.1.
     */
    @Test
    public void testTranslation() throws FactoryException, TransformException {
        final String        classification = "Geocentric translations (geog2d domain)";
        final MathTransformFactory factory = ReferencingFactoryFinder.getMathTransformFactory(null);
        final ParameterValueGroup    param = factory.getDefaultParameters(classification);

        param.parameter("dx").setValue( 84.87);
        param.parameter("dy").setValue( 96.49);
        param.parameter("dz").setValue(116.95);

        final MathTransform test = factory.createParameterizedTransform(param);
        final GeneralDirectPosition position = new GeneralDirectPosition(3);
        position.setOrdinate(0, 3771793.97);
        position.setOrdinate(1,  140253.34);
        position.setOrdinate(2, 5124304.35);
        assertSame(position, test.transform(position, position));
        assertEquals(3771878.84, position.getOrdinate(0), 1E-5);
        assertEquals( 140349.83, position.getOrdinate(1), 1E-5);
        assertEquals(5124421.30, position.getOrdinate(2), 1E-5);
    }

    /**
     * Test case using example from EPSG Guidance Note number 7 part 2 (May 2005),
     * section 2.4.3.2.1.
     */
    @Test
    public void testSevenParam() throws FactoryException, TransformException {
        final String        classification = "Position Vector transformation (geog2d domain)";
        final MathTransformFactory factory = ReferencingFactoryFinder.getMathTransformFactory(null);
        final ParameterValueGroup    param = factory.getDefaultParameters(classification);

        param.parameter("dx") .setValue(0.000);
        param.parameter("dy") .setValue(0.000);
        param.parameter("dz") .setValue(4.5);
        param.parameter("ex") .setValue(0.000);
        param.parameter("ey") .setValue(0.000);
        param.parameter("ez") .setValue(0.554);
        param.parameter("ppm").setValue(0.219);

        final MathTransform test = factory.createParameterizedTransform(param);
        final GeneralDirectPosition position = new GeneralDirectPosition(3);
        position.setOrdinate(0, 3657660.66);
        position.setOrdinate(1,  255768.55);
        position.setOrdinate(2, 5201382.11);
        assertSame(position, test.transform(position, position));
        assertEquals(3657660.78, position.getOrdinate(0), 1E-2);
        assertEquals( 255778.43, position.getOrdinate(1), 1E-5);
        assertEquals(5201387.75, position.getOrdinate(2), 1E-2);
    }

    /**
     * Test case using example from EPSG Guidance Note number 7 part 2 (May 2005),
     * section 2.4.3.2.2.
     */
    @Test
    public void testFrameRotation() throws FactoryException, TransformException {
        final String        classification = "Coordinate Frame rotation (geog2d domain)";
        final MathTransformFactory factory = ReferencingFactoryFinder.getMathTransformFactory(null);
        final ParameterValueGroup    param = factory.getDefaultParameters(classification);

        param.parameter("dx") .setValue( 0.000);
        param.parameter("dy") .setValue( 0.000);
        param.parameter("dz") .setValue( 4.5);
        param.parameter("ex") .setValue( 0.000);
        param.parameter("ey") .setValue( 0.000);
        param.parameter("ez") .setValue(-0.554);
        param.parameter("ppm").setValue( 0.219);

        final MathTransform test = factory.createParameterizedTransform(param);
        final GeneralDirectPosition position = new GeneralDirectPosition(3);
        position.setOrdinate(0, 3657660.66);
        position.setOrdinate(1,  255768.55);
        position.setOrdinate(2, 5201382.11);
        assertSame(position, test.transform(position, position));
        assertEquals(3657660.78, position.getOrdinate(0), 1E-2);
        assertEquals( 255778.43, position.getOrdinate(1), 1E-5);
        assertEquals(5201387.75, position.getOrdinate(2), 1E-2);
    }

    /**
     * Tests the creation with geocentric transforms.
     * Note: the expected values here are approximatives since we didn't used
     * an external source of test points for this test.
     */
    @Test
    public void testGeotoolsExtensions() throws FactoryException, TransformException {
        final String        classification = "Coordinate Frame rotation (geog2d domain)";
        final MathTransformFactory factory = ReferencingFactoryFinder.getMathTransformFactory(null);
        final ParameterValueGroup    param = factory.getDefaultParameters(classification);
        final Ellipsoid    sourceEllipsoid = DefaultEllipsoid.INTERNATIONAL_1924;
        final Ellipsoid    targetEllipsoid = DefaultEllipsoid.WGS84;

        param.parameter("dx") .setValue( 0.000);
        param.parameter("dy") .setValue( 0.000);
        param.parameter("dz") .setValue( 4.5);
        param.parameter("ex") .setValue( 0.000);
        param.parameter("ey") .setValue( 0.000);
        param.parameter("ez") .setValue(-0.554);
        param.parameter("ppm").setValue( 0.219);

        param.parameter("src_dim").setValue(3);
        param.parameter("tgt_dim").setValue(3);
        param.parameter("src_semi_major").setValue(sourceEllipsoid.getSemiMajorAxis());
        param.parameter("src_semi_minor").setValue(sourceEllipsoid.getSemiMinorAxis());
        param.parameter("tgt_semi_major").setValue(targetEllipsoid.getSemiMajorAxis());
        param.parameter("tgt_semi_minor").setValue(targetEllipsoid.getSemiMinorAxis());

        final MathTransform test = factory.createParameterizedTransform(param);
        final GeneralDirectPosition position = new GeneralDirectPosition(3);
        position.setOrdinate(0,    4.00); // Longitude
        position.setOrdinate(1,   55.00); // Latitude
        position.setOrdinate(2, -191.61); // Height

        assertSame(position, test.transform(position, position));
        assertEquals( 4.00, position.getOrdinate(0), 1E-2);
        assertEquals(55.00, position.getOrdinate(1), 1E-2);
        assertEquals( 3.23, position.getOrdinate(2), 1E-2);
    }
}
