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
package org.geotools.geometry.jts;

// J2SE dependencies
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import java.util.Random;

import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.DefaultCoordinateSequenceFactory;



/**
 * Tests the {@link DefaultCoordinateSequenceTransformer} implementation.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class CoordinateSequenceTransformerTest {
    /**
     * The coordinate sequence factory to use.
     */
    private final CoordinateSequenceFactory csFactory = DefaultCoordinateSequenceFactory.instance();

    /**
     * Compares the current implementation with a simplier one.
     */
    @Test
    public void testTransform() throws FactoryException, TransformException {
        final MathTransform2D t;
        final CoordinateReferenceSystem crs;
        crs = ReferencingFactoryFinder.getCRSFactory(null).createFromWKT(JTSTest.UTM_ZONE_10N);
        t = (MathTransform2D) ReferencingFactoryFinder.getCoordinateOperationFactory(null).createOperation(
                                            DefaultGeographicCRS.WGS84, crs).getMathTransform();
        final Random random = new Random(546757437746704345L);

        // Tries with different coordinate sequence length.
        final int[] size = {12, 1000};
        for (int j=0; j<size.length; j++) {
            final Coordinate[] source = new Coordinate[size[j]];
            for (int i=0; i<source.length; i++) {
                source[i] = new Coordinate(-121 -  4*random.nextDouble(),
                                            -45 + 90*random.nextDouble(),
                                                 500*random.nextDouble());
            }
            final CoordinateSequence sourceCS = csFactory.create(source);
            final CoordinateSequence targetCS = transform(sourceCS, t);
            assertNotSame(sourceCS, targetCS);
            assertEquals(sourceCS.size(), targetCS.size());
            for (int i=sourceCS.size(); --i>=0;) {
                assertFalse(sourceCS.getCoordinate(i).equals(targetCS.getCoordinate(i)));
            }

            final CoordinateSequenceTransformer transformer = new DefaultCoordinateSequenceTransformer();
            final CoordinateSequence testCS = transformer.transform(sourceCS, t);
            assertNotSame(sourceCS, testCS);
            assertNotSame(targetCS, testCS);
            assertEquals(sourceCS.size(), testCS.size());
            for (int i=targetCS.size(); --i>=0;) {
                assertEquals(targetCS.getCoordinate(i), testCS.getCoordinate(i));
            }
        }
    }
    
    @Test
    public void testTransformExtraMZ() throws Exception {
        LiteCoordinateSequence cs = new LiteCoordinateSequence(1, 4);
        cs.setArray(new double[] {1000000, 4000000, 25, 48});
        CoordinateReferenceSystem sourceCrs = CRS.parseWKT(JTSTest.UTM_ZONE_10N);
        CoordinateReferenceSystem destCrs = DefaultGeographicCRS.WGS84;
        
        DefaultCoordinateSequenceTransformer cst;
        cst = new DefaultCoordinateSequenceTransformer(new LiteCoordinateSequenceFactory());
        MathTransform tx = CRS.findMathTransform(sourceCrs, destCrs, true);
        LiteCoordinateSequence transformed = (LiteCoordinateSequence) cst.transform(cs, tx);
        
        assertEquals(25.0, transformed.getOrdinate(0, 2), 0.0);
        assertEquals(48.0, transformed.getOrdinate(0, 3), 0.0);
    }
    
    @Test
    public void testLiteToStandard() throws Exception {
        LiteCoordinateSequence cs = new LiteCoordinateSequence(1, 2);
        cs.setArray(new double[] {1000000, 4000000});
        CoordinateReferenceSystem sourceCrs = CRS.parseWKT(JTSTest.UTM_ZONE_10N);
        CoordinateReferenceSystem destCrs = DefaultGeographicCRS.WGS84;
        
        DefaultCoordinateSequenceTransformer cst;
        cst = new DefaultCoordinateSequenceTransformer(/* standard cs factory */);
        MathTransform tx = CRS.findMathTransform(sourceCrs, destCrs, true);
        CoordinateSequence transformed = cst.transform(cs, tx);
        CoordinateSequence reference = transform(cs, tx);
        
        
        assertEquals(reference.getOrdinate(0, 0), transformed.getOrdinate(0, 0), 0.0);
        assertEquals(reference.getOrdinate(0, 1), transformed.getOrdinate(0, 1), 0.0);
        assertEquals(Double.NaN, transformed.getOrdinate(0, 2), 0.0);
    }

    /**
     * The following is basically a copy-and-paste of a previous implementation by Andrea Aime.
     */
    private CoordinateSequence transform(final CoordinateSequence cs,
                                         final MathTransform transform)
            throws TransformException
    {
        double[] coords = new double[100];
        final Coordinate[] scs = cs.toCoordinateArray();
        final Coordinate[] tcs = new Coordinate[scs.length];
        if (coords.length < (scs.length * 2)) {
            coords = new double[scs.length * 2];
        }
        for (int i=0; i<scs.length; i++) {
            coords[i * 2    ] = scs[i].x; 
            coords[i * 2 + 1] = scs[i].y;
        }
        transform.transform(coords, 0, coords, 0, scs.length);
        for (int i = 0; i < tcs.length; i++) {
            tcs[i] = new Coordinate(coords[i * 2], coords[i * 2 + 1]);
        }        
        return csFactory.create(tcs);
    }
}
