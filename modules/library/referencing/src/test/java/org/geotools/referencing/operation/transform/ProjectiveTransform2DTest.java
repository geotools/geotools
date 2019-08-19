/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.operation.transform;

import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;

/**
 * Tests the {@link ProjectiveTransform2D} classes.
 *
 * @version $Id$
 * @author Alexey Valikov
 */
public final class ProjectiveTransform2DTest {
    // Tests that ProjectiveTransform2D is invertible
    @Test
    public void testInvertible() throws NoninvertibleTransformException {
        final GeneralMatrix matrix = new GeneralMatrix(3);
        matrix.setElement(0, 1, -1);
        matrix.setElement(1, 0, 1);
        final MathTransform transform = new ProjectiveTransform2D(matrix);
        Assert.assertNotNull(transform.inverse());
    }
}
