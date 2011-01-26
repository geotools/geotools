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
package org.geotools.data.ogr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.DefaultFeature;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SimpleFeature;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Basic test for OGR data store capabilites against file data sources
 * 
 * @author aaime
 *
 * @source $URL$
 */
public class OGRDataStoreTest extends TestCaseSupport {

	public OGRDataStoreTest(String testName) throws IOException {
		super(testName);
	}

	public void testGetTypeNames() throws FileNotFoundException, IOException {
		OGRDataStore store = new OGRDataStore(getAbsolutePath(STATE_POP), null, null);
		assertEquals(1, store.getTypeNames().length);
		assertEquals("statepop", store.getTypeNames()[0]);
		store = new OGRDataStore(getAbsolutePath(MIXED), null, null);
		assertEquals(1, store.getTypeNames().length);
		assertEquals("mixed", store.getTypeNames()[0]);
	}

	public void testSchemaPop() throws Exception {
		OGRDataStore s = new OGRDataStore(getAbsolutePath(STATE_POP), null, null);
		FeatureType schema = s.getSchema(s.getTypeNames()[0]);
		AttributeType[] types = schema.getAttributeTypes();
		assertEquals("Number of Attributes", 253, types.length);
		assertTrue(CRS.equalsIgnoreMetadata(CRS.decode("EPSG:4269", true), schema
				.getDefaultGeometry().getCoordinateSystem()));
	}

	public void testSchemaMix() throws Exception {
		OGRDataStore s = new OGRDataStore(getAbsolutePath(MIXED), null, null);
		FeatureType schema = s.getSchema(s.getTypeNames()[0]);
		AttributeType[] types = schema.getAttributeTypes();
		assertEquals("Number of Attributes", 11, types.length);
		// mixed geometry types, only way is to use Geometry as geom type
		assertEquals(Geometry.class, schema.getDefaultGeometry().getType());
		// ah, can't compare the WKT against the EPSG database becuase it's
		// apparently broken, it's EPSG:3003 but with (very) different TOWGS84
		// parameters, crazy...
		// assertTrue(CRS.equalsIgnoreMetadata(CRS.decode("EPSG:3003", true),
		// schema
		// .getDefaultGeometry().getCoordinateSystem()));
	}

	/**
	 * Test envelope versus old DataSource
	 */
	public void testOptimizedEnvelope() throws Exception {
		FeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
		OGRDataStore s = new OGRDataStore(getAbsolutePath(STATE_POP), null, null);
		String typeName = s.getTypeNames()[0];

		assertEquals(features.getBounds(), s.getFeatureSource(typeName).getBounds());
		assertNotNull(s.getFeatureSource(typeName).getBounds());
	}

	public void testLoadAndVerify() throws Exception {
		FeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
		int count = features.size();

		assertTrue("Have features", count > 0);
		assertEquals("Number of Features loaded", 49, features.size());
		assertEquals(49, countFeatures(features));

		FeatureType schema = firstFeature(features).getFeatureType();
		assertNotNull(schema.getDefaultGeometry());
		assertEquals("Number of Attributes", 253, schema.getAttributeTypes().length);
		assertEquals("Value of statename is wrong", firstFeature(features).getAttribute(
				"STATE_NAME"), "Illinois");
		assertEquals("Value of land area is wrong", ((Double) firstFeature(features).getAttribute(
				"LAND_KM")).doubleValue(), 143986.61, 0.001);
	}

	public void testLoadAndCheckParentTypeIsPolygon() throws Exception {
		FeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
		FeatureType schema = firstFeature(features).getFeatureType();
		assertTrue(schema.isDescendedFrom(BasicFeatureTypes.POLYGON));
		assertTrue(schema.isDescendedFrom(FeatureTypes.DEFAULT_NAMESPACE, "polygonFeature"));
	}

	public void testShapefileComparison() throws Exception {
		URL url = TestData.url(STATE_POP);
		ShapefileDataStore sds = new ShapefileDataStore(url);
		OGRDataStore ods = new OGRDataStore(getAbsolutePath(STATE_POP), null, null);

		assertEquals(sds.getSchema(), ods.getSchema(sds.getSchema().getTypeName()));

		DefaultQuery query = new DefaultQuery(sds.getSchema().getTypeName());
		FeatureReader sfr = sds.getFeatureReader(query, Transaction.AUTO_COMMIT);
		FeatureReader ofr = ods.getFeatureReader(query, Transaction.AUTO_COMMIT);
		Feature sf = null;
		Feature of = null;
		while (true) {
			if (!sfr.hasNext()) {
				assertTrue(!ofr.hasNext());
				break;
			}
			sf = sfr.next();
			of = ofr.next();
			for (int i = 0; i < sds.getSchema().getAttributeCount(); i++) {
				Object shapeAtt = sf.getAttribute(i);
				Object ogrAtt = of.getAttribute(i);
				// don't know exactly why geometries have to be compared
				// separately
				// but issuing an assertEqual
				if (shapeAtt instanceof Geometry)
					assertTrue(((Geometry) (shapeAtt)).equals((Geometry) ogrAtt));
				else
					assertEquals(shapeAtt, ogrAtt);
			}
		}
		sfr.close();
		ofr.close();
	}

	public void testShapeWriteCapabilities() throws Exception {
		OGRDataStore ods = new OGRDataStore(getAbsolutePath(STATE_POP), null, null);
		assertTrue(ods.supportsInPlaceWrite(ods.getTypeNames()[0]));
	}

	public void testMIFWriteCapabilities() throws Exception {
		OGRDataStore ods = new OGRDataStore(getAbsolutePath(MIXED), null, null);
		assertTrue(ods.supportsWriteNewLayer(ods.getTypeNames()[0]));
	}

	/**
	 * Create a test file, then continue removing the first entry until there
	 * are no features left.
	 */
	public void testRemoveFromFrontAndClose() throws Throwable {
		OGRDataStore sds = createDataStore();

		String typeName = sds.getTypeNames()[0];
		int idx = loadFeatures(sds, typeName).size();

		while (idx > 0) {
			FeatureWriter writer = null;

			try {
				writer = sds.getFeatureWriter(typeName, Filter.INCLUDE, Transaction.AUTO_COMMIT);
				writer.next();
				writer.remove();
			} finally {
				if (writer != null) {
					writer.close();
					writer = null;
				}
			}
			idx--;
			assertEquals(idx, loadFeatures(sds, typeName).size());
		}
	}

	/**
	 * Create a test file, then continue removing the last entry until there are
	 * no features left.
	 */
	public void testRemoveFromBackAndClose() throws Throwable {
		OGRDataStore sds = createDataStore();
		String typeName = sds.getTypeNames()[0];

		int idx = loadFeatures(sds, typeName).size();

		while (idx > 0) {
			FeatureWriter writer = null;
			try {
				writer = sds.getFeatureWriter(sds.getTypeNames()[0], Filter.INCLUDE,
						Transaction.AUTO_COMMIT);
				while (writer.hasNext()) {
					writer.next();
				}
				writer.remove();
			} finally {
				if (writer != null) {
					writer.close();
					writer = null;
				}
			}
			assertEquals(--idx, loadFeatures(sds, typeName).size());
		}

	}

	public void testCreateSchema() throws Exception {
		String[] fileNames = shapeFileNames("test");
		cleanFiles(fileNames);
		String absolutePath = new File(fileNames[0]).getAbsolutePath();
		OGRDataStore ds = new OGRDataStore(absolutePath, "ESRI shapefile", null);
		FeatureType schema = DataUtilities.createType("test",
				"geom:MultiPolygon,count:int,level1:double,level2:float,chars:string");
		ds.createSchema(schema);

		// now do some testing
		assertEquals(1, ds.getTypeNames().length);
		assertEquals("test", ds.getTypeNames()[0]);
		FeatureType ogrSchema = ds.getSchema(ds.getTypeNames()[0]);
		assertEquals(schema.getDefaultGeometry().getType(), ogrSchema.getDefaultGeometry()
				.getType());
		for (int i = 0; i < schema.getAttributeCount(); i++) {
			AttributeType at = schema.getAttributeType(i);
			if (at == schema.getDefaultGeometry())
				continue;

			assertEquals(at.getName(), ogrSchema.getAttributeType(i).getName());
			assertEquals("Wrong type for attribute " + at.getName(), at.getType(), ogrSchema
					.getAttributeType(i).getType());
		}
	}

	public void testCreateWriteRead() throws Exception {
		String typeName = "testw";
		String[] files = shapeFileNames(typeName);
		cleanFiles(files);

		File file = new File(typeName + ".shp");
		OGRDataStore ds = new OGRDataStore(file.getAbsolutePath(), "ESRI shapefile", null);
		FeatureType schema = DataUtilities.createType(typeName, "geom:Point,cat:int,name:string");
		ds.createSchema(schema);

		GeometryFactory gf = new GeometryFactory();
		// creating 20 geometries because with only a couple a finalization
		// related error that did blew up the VM would not appear
		Feature[] features = new Feature[20];
		for (int i = 0; i < features.length; i++) {
			features[i] = schema.create(new Object[] { gf.createPoint(new Coordinate(i, i)),
					new Integer(i), "" + i });
		}

		FeatureWriter writer = ds.getFeatureWriterAppend("testw", Transaction.AUTO_COMMIT);
		for (int i = 0; i < features.length; i++) {
			assertFalse(writer.hasNext());
			DefaultFeature f = (DefaultFeature) writer.next();
			f.setAttributes(features[i].getAttributes(null));
			writer.write();
			assertEquals(typeName + "." + i, f.getID());
		}
		writer.close();

		FeatureReader reader = ds.getFeatureReader("testw");
		for (int i = 0; i < features.length; i++) {
			assertTrue(reader.hasNext());
			Feature f = reader.next();
			for (int j = 0; j < schema.getAttributeCount(); j++) {
				if (f.getAttribute(j) instanceof Geometry) {
					// this is necessary because geometry equality is
					// implemented as Geometry.equals(Geometry)
					Geometry a = (Geometry) f.getAttribute(j);
					Geometry b = (Geometry) features[i].getAttribute(j);
					assertTrue(a.equals(b));
				} else {
					assertEquals(f.getAttribute(j), features[i].getAttribute(j));
				}
			}
		}
		assertFalse(reader.hasNext());
		reader.close();
	}

	public void testAttributesWriting() throws Exception {
		FeatureCollection features = createFeatureCollection();
		File tmpFile = getTempFile();
		tmpFile.delete();
		OGRDataStore s = new OGRDataStore(tmpFile.getAbsolutePath(), "ESRI shapefile", null);
		writeFeatures(s, features);
	}
	
	

	// ---------------------------------------------------------------------------------------
	// SUPPORT METHODS
	// ---------------------------------------------------------------------------------------

	private int countFeatures(FeatureCollection features) {
		int count = 0;
		for (Iterator it = features.iterator(); it.hasNext(); it.next()) {
			count++;
		}
		return count;
	}

	protected FeatureCollection loadFeatures(String resource, Query query) throws Exception {
		assertNotNull(query);

		OGRDataStore s = new OGRDataStore(getAbsolutePath(resource), null, null);
		FeatureSource fs = s.getFeatureSource(s.getTypeNames()[0]);
		return fs.getFeatures(query);
	}

	protected FeatureCollection loadFeatures(DataStore store, String typeName) throws Exception {
		FeatureSource fs = store.getFeatureSource(typeName);
		return fs.getFeatures();
	}

	protected FeatureCollection createFeatureCollection() throws Exception {
		FeatureTypeFactory factory = FeatureTypeFactory.newInstance("junk");
		factory.addType(AttributeTypeFactory.newAttributeType("a", Point.class));
		factory.addType(AttributeTypeFactory.newAttributeType("b", Byte.class));
		factory.addType(AttributeTypeFactory.newAttributeType("c", Short.class));
		factory.addType(AttributeTypeFactory.newAttributeType("d", Double.class));
		factory.addType(AttributeTypeFactory.newAttributeType("e", Float.class));
		factory.addType(AttributeTypeFactory.newAttributeType("f", String.class));
		factory.addType(AttributeTypeFactory.newAttributeType("g", Date.class));
		factory.addType(AttributeTypeFactory.newAttributeType("h", Boolean.class));
		factory.addType(AttributeTypeFactory.newAttributeType("i", Number.class));
		factory.addType(AttributeTypeFactory.newAttributeType("j", Long.class));
		factory.addType(AttributeTypeFactory.newAttributeType("k", BigDecimal.class));
		factory.addType(AttributeTypeFactory.newAttributeType("l", BigInteger.class));
		FeatureType type = factory.getFeatureType();
		FeatureCollection features = FeatureCollections.newCollection();
		for (int i = 0, ii = 20; i < ii; i++) {
			features.add(type.create(new Object[] {
					new GeometryFactory().createPoint(new Coordinate(1, -1)), new Byte((byte) i),
					new Short((short) i), new Double(i), new Float(i), new String(i + " "),
					new Date(i), new Boolean(true), new Integer(22),
					new Long(1234567890123456789L),
					new BigDecimal(new BigInteger("12345678901234567890123456789"), 2),
					new BigInteger("12345678901234567890123456789") }));
		}
		return features;
	}

	private void writeFeatures(DataStore s, FeatureCollection fc) throws Exception {
		s.createSchema(fc.features().next().getFeatureType());
		FeatureWriter fw = s.getFeatureWriter(s.getTypeNames()[0], Transaction.AUTO_COMMIT);
		FeatureIterator it = fc.features();
		while (it.hasNext()) {
			((SimpleFeature) fw.next()).setAttributes(it.next().getAttributes(null));
			fw.write();
		}
		fw.close();
	}

	private OGRDataStore createDataStore(File f) throws Exception {
		FeatureCollection fc = createFeatureCollection();
		f.delete();
		OGRDataStore sds = new OGRDataStore(f.getAbsolutePath(), "ESRI shapefile", null);
		writeFeatures(sds, fc);
		return sds;
	}

	private OGRDataStore createDataStore() throws Exception {
		return createDataStore(getTempFile());
	}
	
	private String[] shapeFileNames(String typeName) {
		return new String[] { typeName + ".shp", typeName + ".dbf", typeName + ".shp",
				typeName + ".shx", typeName + ".prj" };
	}

	private void cleanFiles(String[] files) {
		for (int i = 0; i < files.length; i++) {
			File f = new File(files[i]);
			if (f.exists())
				f.delete();
		}
	}
}
