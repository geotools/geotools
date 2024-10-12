/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.api.geometry.Position;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransformFactory;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.GeneralPosition;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link EarthGravitationalModel} class.
 *
 * @since 2.3
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class EarthGravitationalModelTest {
    /** Tests the {@link EarthGravitationalModel#heightOffset} method for WGS 84. */
    @Test
    public void testHeightOffsetWGS84() throws Exception {
        final EarthGravitationalModel gh = new EarthGravitationalModel();
        gh.load("EGM180.nor");
        Assert.assertEquals(1.505, gh.heightOffset(45, 45, 0), 0.001);
        Assert.assertEquals(1.515, gh.heightOffset(45, 45, 1000), 0.001);
        Assert.assertEquals(46.908, gh.heightOffset(0, 45, 0), 0.001);
    }

    /** Tests the {@link EarthGravitationalModel#heightOffset} method for WGS 72. */
    @Test
    public void testHeightOffsetWGS72() throws Exception {
        final EarthGravitationalModel gh = new EarthGravitationalModel(180, false);
        gh.load("EGM180.nor");
        Assert.assertEquals(1.475, gh.heightOffset(45, 45, 0), 0.001);
        Assert.assertEquals(46.879, gh.heightOffset(0, 45, 0), 0.001);
        Assert.assertEquals(23.324, gh.heightOffset(3, 10, 10), 0.001);
        Assert.assertEquals(0.380, gh.heightOffset(75, -30, 0), 0.001);
    }

    /** Tests the creation of the math transform from the factory. */
    @Test
    public void testMathTransform() throws FactoryException, TransformException {
        final MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);
        final ParameterValueGroup p = mtFactory.getDefaultParameters("Earth gravitational model");
        final MathTransform mt = mtFactory.createParameterizedTransform(p);
        Position pos = new GeneralPosition(new double[] {45, 45, 1000});
        pos = mt.transform(pos, pos);
        Assert.assertEquals(45.000, pos.getOrdinate(0), 0.001);
        Assert.assertEquals(45.000, pos.getOrdinate(1), 0.001);
        Assert.assertEquals(1001.515, pos.getOrdinate(2), 0.001);
    }
}
