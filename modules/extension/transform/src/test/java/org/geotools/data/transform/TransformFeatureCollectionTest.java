/* (c) 2016 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.data.transform;

import static org.junit.Assert.assertTrue;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.visitor.BoundsVisitor;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.junit.Test;

public class TransformFeatureCollectionTest extends AbstractTransformFeatureCollection {

    @Test
    public void testVisitorWithSelect() throws Exception {
        Query query = new Query(STORE_NAME);
        SimpleFeatureSource transformed = transformWithSelection();

        CountVisitor countVisitor = new CountVisitor();
        MinVisitor minVisitor = new MinVisitor("male");
        MaxVisitor maxVisitor = new MaxVisitor("male");
        UniqueVisitor uniqueVisitor = new UniqueVisitor("female");
        BoundsVisitor boundsVisitor = new BoundsVisitor();
        boolean passedDown = false;

        FeatureCalc[] visitors = {
            countVisitor, minVisitor, maxVisitor, uniqueVisitor, boundsVisitor
        };
        boolean[] expectedPass = {true, true, true, true, false};

        for (int i = 0; i < visitors.length; i++) {
            ((TransformFeatureStoreWrapper) transformed).setPassedDown(false);
            transformed.getFeatures(query).accepts(visitors[i], null);
            passedDown = ((TransformFeatureStoreWrapper) transformed).isPassedDown();
            assertTrue(expectedPass[i] == passedDown);
        }
    }

    @Test
    public void testVisitorWithRename() throws Exception {
        Query query = new Query(STORE_NAME);
        SimpleFeatureSource transformed = transformWithRename();

        CountVisitor countVisitor = new CountVisitor();
        MinVisitor minVisitor = new MinVisitor("num_of_male");
        MaxVisitor maxVisitor = new MaxVisitor("num_of_male");
        UniqueVisitor uniqueVisitor = new UniqueVisitor("num_of_female");
        BoundsVisitor boundsVisitor = new BoundsVisitor();
        boolean passedDown = false;

        FeatureCalc[] visitors = {
            countVisitor, minVisitor, maxVisitor, uniqueVisitor, boundsVisitor
        };
        boolean[] expectedPass = {true, true, true, true, false};

        for (int i = 0; i < visitors.length; i++) {
            ((TransformFeatureStoreWrapper) transformed).setPassedDown(false);
            transformed.getFeatures(query).accepts(visitors[i], null);
            passedDown = ((TransformFeatureStoreWrapper) transformed).isPassedDown();
            assertTrue(expectedPass[i] == passedDown);
        }
    }

    @Test
    public void testVisitorWithCalc() throws Exception {
        Query query = new Query(STORE_NAME);
        SimpleFeatureSource transformed = transformWithExpressions();

        CountVisitor countVisitor = new CountVisitor();
        MinVisitor minVisitor = new MinVisitor("total");
        MaxVisitor maxVisitor = new MaxVisitor("total");
        UniqueVisitor uniqueVisitor = new UniqueVisitor("geom");
        BoundsVisitor boundsVisitor = new BoundsVisitor();
        boolean passedDown = false;

        FeatureCalc[] visitors = {
            countVisitor, minVisitor, maxVisitor, uniqueVisitor, boundsVisitor
        };
        boolean[] expectedPass = {true, false, false, false, false};

        for (int i = 0; i < visitors.length; i++) {
            ((TransformFeatureStoreWrapper) transformed).setPassedDown(false);
            transformed.getFeatures(query).accepts(visitors[i], null);
            passedDown = ((TransformFeatureStoreWrapper) transformed).isPassedDown();
            assertTrue(expectedPass[i] == passedDown);
        }
    }
}
