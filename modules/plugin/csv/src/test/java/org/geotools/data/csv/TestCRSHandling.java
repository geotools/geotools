package org.geotools.data.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class TestCRSHandling {

  private static File tmp;
  private static File statesfile;
  private static DataStore stateStore;

  /**
   * Check that CRS are handled correctly
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  @BeforeClass
  public static void createTemporaryLocations() throws IOException {
    tmp = File.createTempFile("example", "");
    boolean exists = tmp.exists();
    if (exists) {
      tmp.delete();
    }
    boolean created = tmp.mkdirs();
    if (!created) {
      System.exit(1);
    }
    statesfile = new File(tmp, "locations.csv");

    URL resource = TestData.getResource(CSVWriteTest.class, "locations.csv");
    Files.copy(resource.openStream(), statesfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    Map<String, Object> params = new HashMap<>();
    params.put(CSVDataStoreFactory.FILE_PARAM.key, statesfile.toString());
    stateStore = DataStoreFinder.getDataStore(params);

  }
  @Test
  public void testPrjFileRead()
      throws FileNotFoundException, IOException, NoSuchAuthorityCodeException, FactoryException {
    File f = TestData.file(this, "coastal2.csv");
    Map<String, Object> params = new HashMap<>();
    params.put(CSVDataStoreFactory.FILE_PARAM.key, f.toString());
    params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
    params.put(CSVDataStoreFactory.WKTP.key, "WKT");
    
    DataStore store = DataStoreFinder.getDataStore(params);
    String name = store.getTypeNames()[0];
    CoordinateReferenceSystem crs = store.getFeatureSource(name).getSchema().getCoordinateReferenceSystem();
    CoordinateReferenceSystem expected = CRS.decode("EPSG:27700");
    assertEquals(expected.getIdentifiers().iterator().next().getCode(),
        crs.getIdentifiers().iterator().next().getCode());
  }

  @Test
  public void testWritePrj() throws IOException {
    File f = new File(tmp, "testcrs.csv");
    Map<String, Object> params = new HashMap<>();
    params.put(CSVDataStoreFactory.FILE_PARAM.key, f.toString());
    params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
    params.put(CSVDataStoreFactory.WKTP.key, "WKT");
    params.put(CSVDataStoreFactory.WRITEPRJ.key, "true");
    DataStore store = DataStoreFinder.getDataStore(params);
    store.createSchema(stateStore.getSchema(stateStore.getTypeNames()[0]));
    SimpleFeatureSource source = store.getFeatureSource(store.getTypeNames()[0]);
    if (!(source instanceof SimpleFeatureStore)) {
      fail("can't create output file");
    }
    SimpleFeatureStore outstore = (SimpleFeatureStore) source;
    outstore.addFeatures(stateStore.getFeatureSource(stateStore.getTypeNames()[0]).getFeatures());
    store.dispose();
    String prjName = FilenameUtils.getBaseName(f.getName()) + ".prj";
    File prj = new File(f.getParent(), prjName);
    assertTrue(prj.exists());
  }
}
