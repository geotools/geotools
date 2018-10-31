package org.geotools.data.shapefile;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import org.geotools.TestData;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.URLs;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

public class DuplicateColumnNameReaderTest extends TestCaseSupport {

    public final String SHPFILE = "dup-column/dup_column.shp";

    // TODO The name "TestColumn1" for the duplicate column "TestColumn" is
    // based on the current implementation of ShapefileDataStore.
    public final String testColumn = "TestColumn1";

    public final Integer expectedValue = new Integer(20);

    @Test
    public void testAttributeReader() throws IOException {
        URL u = TestData.url(TestCaseSupport.class, SHPFILE);
        File shpFile = URLs.urlToFile(u);

        // open the test shapefile
        ShapefileDataStore store = new ShapefileDataStore(shpFile.toURI().toURL());
        SimpleFeatureSource source = store.getFeatureSource();

        // read the first feature
        SimpleFeatureIterator iter = source.getFeatures().features();
        SimpleFeature feature = iter.next();
        iter.close();

        // get the value of the duplicate column & compare it against expectation
        assertEquals(expectedValue, feature.getAttribute(testColumn));

        // cleanup
        store.dispose();
    }

    @Test
    public void testAttributeReaderIndexed() throws IOException {
        URL u = TestData.url(TestCaseSupport.class, SHPFILE);
        File shpFile = URLs.urlToFile(u);

        // open the test shapefile
        // creates both indexed and regular shapefile data store
        ShapefileDataStore indexedstore = new ShapefileDataStore(shpFile.toURI().toURL());

        // get a random feature id from one of the stores
        SimpleFeatureIterator it = indexedstore.getFeatureSource().getFeatures().features();
        FeatureId fid = it.next().getIdentifier();
        it.close();

        // query the datastore
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Filter idFilter = ff.id(Collections.singleton(fid));
        final Query query =
                new Query(
                        indexedstore.getSchema().getName().getLocalPart(),
                        idFilter,
                        new String[] {testColumn});
        final SimpleFeatureCollection indexedfeatures =
                indexedstore.getFeatureSource().getFeatures(query);

        // compare the results
        SimpleFeatureIterator indexIterator = indexedfeatures.features();
        SimpleFeature indexedFeature = indexIterator.next();
        indexIterator.close();

        // get the value of the duplicate column & compare it against expectation
        assertEquals(expectedValue, indexedFeature.getAttribute(testColumn));

        // cleanup
        indexedstore.dispose();
    }
}
