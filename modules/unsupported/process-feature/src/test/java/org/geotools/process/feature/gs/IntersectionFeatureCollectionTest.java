package org.geotools.process.feature.gs;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.feature.gs.IntersectionFeatureCollection.IntersectionMode;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * 
 *
 * @source $URL$
 */
public class IntersectionFeatureCollectionTest {
    private static final Logger logger = Logger.getLogger("org.geotools.process.feature.gs.VectoralZonalStatisticalProcessTest");
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    private DataStore data;
    private SimpleFeatureCollection zonesCollection;
    private SimpleFeatureCollection featuresCollection;
    private SimpleFeatureCollection polylineCollection;
    private SimpleFeatureCollection multipointCollection;
    private IntersectionFeatureCollection process;
    private static final double WORLDAREA = 510072000.0d;
    private static final double COLORADOAREA = 269837.0d;

    @Before
    public void setup() throws IOException {
        File file = TestData.file(this, null);
        data = new PropertyDataStore(file);
        zonesCollection = data.getFeatureSource("zones").getFeatures();
        featuresCollection = data.getFeatureSource("features").getFeatures();
        polylineCollection = data.getFeatureSource("polyline").getFeatures();
        multipointCollection = data.getFeatureSource("multipoint").getFeatures();
        process = new IntersectionFeatureCollection();
    }


    @Test(expected=IllegalArgumentException.class)
    public void testExceptionOpModeFstCollection() throws Exception {
        logger.info("Running testExceptionOpModeFstCollection ...");
        ArrayList<String> toRemoveFst = new ArrayList<String>();
        toRemoveFst.add("cat2");
        ArrayList<String> toRemoveSnd = new ArrayList<String>();
        toRemoveSnd.add("cat");
        SimpleFeatureCollection output = process.execute(polylineCollection, featuresCollection,toRemoveFst, toRemoveSnd, IntersectionMode.INTERSECTION, true, false);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testExceptionOpModeSndCollection() throws Exception {
        logger.info("Running testExceptionOpModeSndCollection ...");
        ArrayList<String> toRetainFst = new ArrayList<String>();
        toRetainFst.add("cat2");
        ArrayList<String> toRetainSnd = new ArrayList<String>();
        toRetainSnd.add("cat");
        SimpleFeatureCollection output = process.execute(zonesCollection, polylineCollection,toRetainFst,toRetainSnd, IntersectionMode.INTERSECTION, true, false);
    }

    private Polygon createRectangularPolygonByCoords(double xMin, double xMax, double yMin, double yMax, CoordinateReferenceSystem sourceCRS){
        GeometryFactory geomFactory = new GeometryFactory();

        // creates the  polygon
        Coordinate[] tempCoordinates = new Coordinate[5];
        tempCoordinates[0] = new Coordinate(xMin, yMin);
        tempCoordinates[1] = new Coordinate(xMax, yMin);
        tempCoordinates[2] = new Coordinate(xMax, yMax);
        tempCoordinates[3] = new Coordinate(xMin, yMax);
        tempCoordinates[4] = tempCoordinates[0];
        LinearRing linearRing = geomFactory.createLinearRing(tempCoordinates);
        Polygon polygon = geomFactory.createPolygon(linearRing, null);
        return polygon;
    }

    // this test verifies if the Illegal argument exception is thrown when a MultiPointCollection is given as first collection
    @Test(expected = IllegalArgumentException.class)
    public void testProcessArguments1() throws IllegalArgumentException{
                SimpleFeatureCollection output2 = process.execute(multipointCollection, featuresCollection,null,null, IntersectionMode.INTERSECTION, null, null);
    }

        // this test verifies if the Illegal argument exception is thrown when a MultiPointCollection is given as second collection and area attributes are required
    @Test(expected = IllegalArgumentException.class)
    public void testProcessArguments2() throws IllegalArgumentException{
                SimpleFeatureCollection output2 = process.execute(featuresCollection,  multipointCollection,null,null, IntersectionMode.INTERSECTION, true, false);
    }

    @Test
    public void testGetIntersectionAreaRate()  {

        logger.info("Running testGetIntersectionAreaRate ...");
        CoordinateReferenceSystem sourceCRS = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

        // creates the world state polygon
        Polygon worldPolygon = createRectangularPolygonByCoords(-180,180,-90, 90, sourceCRS);

        // creates the Colorado state polygon
        Polygon coloradoPolygon = createRectangularPolygonByCoords(-102,-109,37, 41, sourceCRS);

        // calculates the estimated value
        double calculatedRate = IntersectionFeatureCollection.getIntersectionArea(worldPolygon, sourceCRS,coloradoPolygon , sourceCRS, true);

        // calculates the expected value
        double expectedRate = COLORADOAREA / WORLDAREA;
        
        // 0.01% error off the expected value
        assertEquals(0, (expectedRate - calculatedRate) / expectedRate, 0.01);
    }

    @Test
    public void testReturnedAttributes() throws Exception {
        logger.info("Running testReturnedAttributes ...");
        ArrayList<String> toRetainFst = new ArrayList<String>();
        toRetainFst.add("str1");
        ArrayList<String> toRetainSnd = new ArrayList<String>();
        toRetainSnd.add("str2");
        SimpleFeatureCollection output2 = process.execute(zonesCollection, featuresCollection,toRetainFst,toRetainSnd, IntersectionMode.INTERSECTION, true, true);
        SimpleFeatureIterator sfTemp2 = output2.features();
        sfTemp2.hasNext();
        SimpleFeature sf = sfTemp2.next();

        // test with both area and percentage attributes
        assertNotNull(sf.getAttribute("the_geom"));
        assertNotNull(sf.getAttribute("zones_str1"));
        assertNotNull(sf.getAttribute("features_str2"));
        assertNotNull(sf.getAttribute("percentageA"));
        assertNotNull(sf.getAttribute("percentageB"));
        assertNotNull(sf.getAttribute("areaA"));
        assertNotNull(sf.getAttribute("areaB"));
        assertTrue(sf.getAttributeCount()==7);

        // test without area and percentageAttributes
        SimpleFeatureCollection output3 = process.execute(zonesCollection, featuresCollection,toRetainFst,toRetainSnd, IntersectionMode.INTERSECTION, false, false);
        SimpleFeatureIterator sfTemp3 = output3.features();
        sfTemp3.hasNext();
        SimpleFeature sf2 = sfTemp3.next();
        assertNotNull(sf2.getAttribute("the_geom"));
        assertNotNull(sf2.getAttribute("zones_str1"));
        assertNotNull(sf2.getAttribute("features_str2"));
        assertTrue(sf2.getAttributeCount()==3);

    }

    @Test
    public void testExecute() throws Exception {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("featureType");
        tb.add("geometry", Polygon.class);
        tb.add("integer", Integer.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        DefaultFeatureCollection features = new DefaultFeatureCollection(null, b.getFeatureType());
        DefaultFeatureCollection secondFeatures = new DefaultFeatureCollection(null, b
                .getFeatureType());
        Polygon[] firstArrayGeometry = new Polygon[1];
        Polygon[] secondArrayGeometry = new Polygon[1];
        for (int numFeatures = 0; numFeatures < 1; numFeatures++) {
            Coordinate array[] = new Coordinate[5];
            array[0] = new Coordinate(0, 0);
            array[1] = new Coordinate(1, 0);
            array[2] = new Coordinate(1, 1);
            array[3] = new Coordinate(0, 1);
            array[4] = new Coordinate(0, 0);
            LinearRing shell = new LinearRing(array, new PrecisionModel(), 0);
            b.add(gf.createPolygon(shell, null));
            b.add(0);
            firstArrayGeometry[0] = gf.createPolygon(shell, null);
            features.add(b.buildFeature(numFeatures + ""));
        }
        for (int numFeatures = 0; numFeatures < 1; numFeatures++) {
            Coordinate array[] = new Coordinate[5];
            Coordinate centre = ((Polygon) features.features().next().getDefaultGeometry())
                    .getCentroid().getCoordinate();
            array[0] = new Coordinate(centre.x, centre.y);
            array[1] = new Coordinate(centre.x + 1, centre.y);
            array[2] = new Coordinate(centre.x + 1, centre.y + 1);
            array[3] = new Coordinate(centre.x, centre.y + 1);
            array[4] = new Coordinate(centre.x, centre.y);
            LinearRing shell = new LinearRing(array, new PrecisionModel(), 0);
            b.add(gf.createPolygon(shell, null));
            b.add(0);
            secondArrayGeometry[0] = gf.createPolygon(shell, null);
            secondFeatures.add(b.buildFeature(numFeatures + ""));
        }

        SimpleFeatureCollection output3 = process.execute(features,secondFeatures, null, null, null, false, false);

        assertTrue(output3.size()==1);
        SimpleFeatureIterator iterator = output3.features();


        GeometryCollection firstCollection = null;
        GeometryCollection secondCollection = null;
        firstCollection = new GeometryCollection(firstArrayGeometry, new GeometryFactory());
        secondCollection = new GeometryCollection(secondArrayGeometry, new GeometryFactory());
        for (int i = 0; i < firstCollection.getNumGeometries() && iterator.hasNext(); i++) {
            Geometry expected = (Geometry) firstCollection.getGeometryN(i).intersection(
                    secondCollection.getGeometryN(i));
            SimpleFeature sf = iterator.next();
            assertTrue(expected.equals((Geometry) sf.getDefaultGeometry()));
        }

}
}
