/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author ian
 *
 */
public class CSVWriteOptionsTest {
  File tmp;
  File statesfile;
  private FileDataStore store;
  CSVDataStoreFactory factory = new CSVDataStoreFactory();
  @Before 
  public void setup() throws IOException {
    URL states = TestData.url("shapes/statepop.shp");
     store = FileDataStoreFinder.getDataStore(states);
    assertNotNull("couldn't create input store", store);
  }
  @Before
  public void createTemporaryLocations() throws IOException {
      tmp = File.createTempFile("example", "");
      boolean exists = tmp.exists();
      if (exists) {
          // System.err.println("Removing tempfile " + tmp);
          tmp.delete();
      }
      boolean created = tmp.mkdirs();
      if (!created) {
          // System.err.println("Could not create " + tmp);
          System.exit(1);
      }
      statesfile = new File(tmp, "locations.csv");

      URL resource = TestData.getResource(CSVWriteTest.class, "locations.csv");
      Files.copy(resource.openStream(), statesfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  private String getFileContents(File modified) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Files.copy(modified.toPath(), baos);
      String contents = new String(baos.toByteArray(), StandardCharsets.UTF_8);
      return contents;
  }

  @After
  public void removeTemporaryLocations() throws IOException {
      File list[] = tmp.listFiles();
      for (int i = 0; i < list.length; i++) {
          list[i].delete();
      }
      tmp.delete();
  }

  // Make sure any temp files were cleaned up.
  public boolean cleanedup() {
      File list[] = tmp.listFiles((dir, name) -> name.endsWith(".csv"));
      for (int i = 0; i < list.length; i++) {
          if (list[i].getName().equalsIgnoreCase("locations.csv")) {
              continue;
          }
          return false;
      }
      return true;
  }
  @Test
  public void testWithOutQuotes() throws IOException{
    
    
    File file2 = File.createTempFile("CSVTest", ".csv");
    Map<String, Serializable> params2 = new HashMap<String, Serializable>();
    params2.put("file", file2);
    
    params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
    params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");

    
    
    String contents = createOutputFile(file2, params2);
    BufferedReader lineReader = new BufferedReader(new CharArrayReader(contents.toCharArray()));
    String line = lineReader.readLine(); // header
    assertNotNull(line);
    assertTrue("Geom is not included", line.toLowerCase().contains("the_geom_wkt"));
    line = lineReader.readLine();
    assertTrue("Multipolygon is not quoted", line.toLowerCase().contains("\"multipolygon"));
    file2.delete();
  }
  @Test
  public void testWithQuotes() throws IOException {
    File file2 = File.createTempFile("CSVTest", ".csv");
    Map<String, Serializable> params2 = new HashMap<String, Serializable>();
    params2.put("file", file2);
    
    params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
    params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");
    params2.put(CSVDataStoreFactory.QUOTEALL.key, true);
    String contents = createOutputFile(file2, params2);
     BufferedReader lineReader = new BufferedReader(new CharArrayReader(contents.toCharArray()));
     String line = lineReader.readLine(); // header
    assertNotNull(line);
    
    assertTrue("Geom is not included", line.toLowerCase().contains("\"the_geom_wkt\""));
    line = lineReader.readLine();
     
    assertTrue("Multipolygon is not quoted", line.toLowerCase().contains("\"multipolygon"));
    assertTrue("Missing quotes", line.contains("\"0.0\""));
    file2.delete();
  }
  @Test
  public void testWithSingleQuotes() throws IOException {
    File file2 = File.createTempFile("CSVTest", ".csv");
    Map<String, Serializable> params2 = new HashMap<String, Serializable>();
    params2.put("file", file2);
    
    params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
    params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");
    params2.put(CSVDataStoreFactory.QUOTEALL.key, true);
    params2.put(CSVDataStoreFactory.QUOTECHAR.key, '\'');
    String contents = createOutputFile(file2, params2);
     BufferedReader lineReader = new BufferedReader(new CharArrayReader(contents.toCharArray()));
     String line = lineReader.readLine(); // header
    assertNotNull(line);
    
    assertTrue("Geom is not included", line.toLowerCase().contains("the_geom_wkt"));
    line = lineReader.readLine();
     
    assertTrue("Multipolygon is not quoted", line.toLowerCase().contains("'multipolygon"));
    assertTrue("Missing quotes", line.contains("'0.0'"));
    file2.delete();
  }
  @Test
  public void testWithtabs() throws IOException {
    File file2 = File.createTempFile("CSVTest", ".csv");
    Map<String, Serializable> params2 = new HashMap<String, Serializable>();
    params2.put("file", file2);
    
    params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
    params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");
    params2.put(CSVDataStoreFactory.SEPERATORCHAR.key, '\t');
    String contents = createOutputFile(file2, params2);
     BufferedReader lineReader = new BufferedReader(new CharArrayReader(contents.toCharArray()));
     String line = lineReader.readLine(); // header
    assertNotNull(line);
    System.out.println(line ); 
    assertTrue("Geom is not included", line.toLowerCase().contains("the_geom_wkt"));
    line = lineReader.readLine();
    System.out.println(line ); 
    assertTrue("Wrong seperator", line.contains("\t"));
    assertTrue("Missing quotes", line.toLowerCase().startsWith("multipolygon"));
    file2.delete();
  }
  private String createOutputFile(File file2, Map<String, Serializable> params2)
      throws IOException {
    DataStore datastore = factory.createNewDataStore(params2);
    
    SimpleFeatureType featureType = store.getSchema();

    datastore.createSchema(featureType);
    String typeName = datastore.getTypeNames()[0];
    SimpleFeatureSource source = datastore.getFeatureSource(typeName);
    if (source instanceof SimpleFeatureStore) {
        SimpleFeatureStore outStore = (SimpleFeatureStore) source;
        outStore.addFeatures(store.getFeatureSource().getFeatures());
    } else {
        fail("Can't write to CSVDatastore");
    }

    datastore.dispose();
    datastore = (CSVDataStore) factory.createNewDataStore(params2);
    source = datastore.getFeatureSource(typeName);
    SimpleFeatureCollection features = source.getFeatures();
    assertEquals(
            "wrong number of records",
            store.getFeatureSource().getFeatures().size(),
            features.size()); 
    String contents = getFileContents(file2);
    return contents;
  }

}
