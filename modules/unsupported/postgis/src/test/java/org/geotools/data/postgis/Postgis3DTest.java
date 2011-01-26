package org.geotools.data.postgis;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class Postgis3DTest extends AbstractPostgisDataTestCase {
    
    protected static final String LINE3D = "line3d";

    protected static final String POLY3D = "poly3d";

    protected static final String POINT3D = "point3d";

    protected static final String ID = "id";

    protected static final String GEOM = "geom";

    protected static final String NAME = "name";

    protected static final FilterFactory FF = CommonFactoryFinder.getFilterFactory(null);

    protected SimpleFeatureType poly3DType;

    protected SimpleFeatureType line3DType;
    
    protected CoordinateReferenceSystem epsg4326;

    public Postgis3DTest(String name) {
        super(name);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        JDBCDataStore data = (JDBCDataStore) this.data;
        
        line3DType = DataUtilities.createType(data.getNameSpace().toString() + "." + LINE3D,
                ID + ":0," + GEOM + ":LineString:srid=4326," + NAME  + ":String");
        line3DType.getGeometryDescriptor().getUserData().put(Hints.COORDINATE_DIMENSION, 3);
        poly3DType = DataUtilities.createType(data.getNameSpace().toString() + "." + POLY3D,
                ID + ":0," + GEOM + ":Polygon:srid=4326," + NAME + ":String");
        poly3DType.getGeometryDescriptor().getUserData().put(Hints.COORDINATE_DIMENSION, 3);
        
        epsg4326 = CRS.decode("EPSG:4326");
    }
    
    @Override
    protected void setupDbTables() throws Exception {
        setUpLine3DTable();
        setUpPoint3DTable();
        dropPoly3DTable();
    }
    
    protected void setUpLine3DTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema
                + "','line3d','geom')");
        } catch (Exception ignore) {}

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".line3d CASCADE");
        } catch (Exception ignore) {}

        try {
            Statement s = conn.createStatement();

            s.execute("CREATE TABLE " + f.schema + ".line3d (id int primary key)");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                + "', 'line3d', 'geom', 4326, 'LINESTRING', 3);");
            s.execute("ALTER TABLE " + f.schema + ".line3d add name varchar;");

            s.execute("INSERT INTO " + f.schema + ".line3d (id,geom,name) VALUES (0,"
            + "GeometryFromText('LINESTRING(1 1 0, 2 2 0, 4 2 1, 5 1 1)', 4326), 'l1')");
            s.execute("INSERT INTO " + f.schema + ".line3d (id,geom,name) VALUES (1,"
                    + "GeometryFromText('LINESTRING(3 0 1, 3 2 2, 3 3 3, 3 4 5)', 4326), 'l2')");
            
            s.execute( "VACUUM ANALYZE " + f.schema + ".line3d" );
        } finally {
            conn.close();
        }
    }
    
    protected void setUpPoint3DTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema
                + "','point3d','geom')");
        } catch (Exception ignore) {}

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".point3d CASCADE");
        } catch (Exception ignore) {}

        try {
            Statement s = conn.createStatement();

            s.execute("CREATE TABLE " + f.schema + ".point3d (id int primary key)");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                + "', 'point3d', 'geom', 4326, 'POINT', 3);");
            s.execute("ALTER TABLE " + f.schema + ".point3d add name varchar;");

            s.execute("INSERT INTO " + f.schema + ".point3d (id,geom,name) VALUES (0,"
            + "GeometryFromText('POINT(1 1 1)', 4326), 'p1')");
            s.execute("INSERT INTO " + f.schema + ".point3d (id,geom,name) VALUES (1,"
                    + "GeometryFromText('POINT(3 0 1)', 4326), 'p2')");
            
            s.execute( "VACUUM ANALYZE " + f.schema + ".point3d" );
        } finally {
            conn.close();
        }
    }
    
    protected void dropPoly3DTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema
                + "','poly3d','geom')");
        } catch (Exception ignore) {}

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".poly3d CASCADE");
        } catch (Exception ignore) {}
    }
    
   

    public void testSchema() throws Exception {
        SimpleFeatureType schema = data.getSchema(LINE3D);
        CoordinateReferenceSystem crs = schema.getGeometryDescriptor()
                .getCoordinateReferenceSystem();
        assertEquals(new Integer(4326), CRS.lookupEpsgCode(crs, false));
        assertEquals(3, schema.getGeometryDescriptor().getUserData().get(Hints.COORDINATE_DIMENSION));
    }

    public void testReadPoint() throws Exception {
    	SimpleFeatureCollection fc = data.getFeatureSource(POINT3D).getFeatures();
        SimpleFeatureIterator fr = fc.features();
        assertTrue(fr.hasNext());
        SimpleFeature feature = fr.next();
        assertEquals(3, feature.getType().getGeometryDescriptor().getUserData().get(Hints.COORDINATE_DIMENSION));
        Point p = (Point) feature.getDefaultGeometry();
        assertTrue(new Coordinate(1, 1, 1).equals3D(p.getCoordinate()));
        fr.close();
    }
    
    public void testReadPoint2D() throws Exception {
        DefaultQuery q = new DefaultQuery(POINT3D);
        q.setHints(new Hints(Hints.FEATURE_2D, true));
        SimpleFeatureCollection fc = data.getFeatureSource(POINT3D).getFeatures(q);
        SimpleFeatureIterator fr = fc.features();
        assertTrue(fr.hasNext());
        SimpleFeature feature = fr.next();
        assertEquals(2, feature.getType().getGeometryDescriptor().getUserData().get(Hints.COORDINATE_DIMENSION));
        Point p = (Point) feature.getDefaultGeometry();
        assertTrue(new Coordinate(1, 1, Double.NaN).equals3D(p.getCoordinate()));
        fr.close();
    }

    public void testReadLine() throws Exception {
    	SimpleFeatureCollection fc = data.getFeatureSource(LINE3D).getFeatures();
        SimpleFeatureIterator fr = fc.features();
        assertTrue(fr.hasNext());
        LineString ls = (LineString) fr.next().getDefaultGeometry();
        // 1 1 0, 2 2 0, 4 2 1, 5 1 1
        assertEquals(4, ls.getCoordinates().length);
        assertTrue(new Coordinate(1, 1, 0).equals3D(ls.getCoordinateN(0)));
        assertTrue(new Coordinate(2, 2, 0).equals3D(ls.getCoordinateN(1)));
        assertTrue(new Coordinate(4, 2, 1).equals3D(ls.getCoordinateN(2)));
        assertTrue(new Coordinate(5, 1, 1).equals3D(ls.getCoordinateN(3)));
        fr.close();
    }

    public void testWriteLine() throws Exception {
        // build a 3d line
        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(new Coordinate[] { new Coordinate(0, 0, 0),
                new Coordinate(1, 1, 1) });

        // build a feature around it
        SimpleFeature newFeature = SimpleFeatureBuilder.build(line3DType, new Object[] { 2, ls,
                "l3" }, null);

        // insert it
        SimpleFeatureStore fs = (SimpleFeatureStore) data
                .getFeatureSource(LINE3D);
        List<FeatureId> fids = fs.addFeatures(DataUtilities.collection(newFeature));

        // retrieve it back
        SimpleFeatureIterator fi = fs.getFeatures(FF.id(new HashSet<FeatureId>(fids)))
                .features();
        assertTrue(fi.hasNext());
        SimpleFeature f = fi.next();
        assertTrue(ls.equals((Geometry) f.getDefaultGeometry()));
        fi.close();
    }

    /**
     * Creates the polygon schema and then inserts a 3D geometry into the
     * datastore and retrieves it back to make sure 3d data is really handled as
     * such
     * 
     * @throws Exception
     */
    public void testCreateSchemaAndInsert() throws Exception {
        data.createSchema(poly3DType);
        SimpleFeatureType actualSchema = data.getSchema(POLY3D);
        assertFeatureTypesEqual(poly3DType, actualSchema);

        // build a 3d polygon (ordinates in ccw order)
        GeometryFactory gf = new GeometryFactory();
        LinearRing shell = gf.createLinearRing(new Coordinate[] { new Coordinate(0, 0, 0),
                new Coordinate(1, 1, 1), new Coordinate(1, 0, 1), new Coordinate(0, 0, 0) });
        Polygon poly = gf.createPolygon(shell, null);

        // insert it
        FeatureWriter<SimpleFeatureType, SimpleFeature> fw = data.getFeatureWriterAppend(
                POLY3D, Transaction.AUTO_COMMIT);
        SimpleFeature f = fw.next();
        f.setAttribute(ID, 0);
        f.setAttribute(GEOM, poly);
        f.setAttribute(NAME, "3dpolygon!");
        fw.write();
        fw.close();

        // read id back and compare
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = data.getFeatureReader(
                new DefaultQuery(POLY3D), Transaction.AUTO_COMMIT);
        assertTrue(fr.hasNext());
        f = fr.next();
        assertTrue(poly.equals((Geometry) f.getDefaultGeometry()));
        fr.close();
    }

    /**
     * Make sure we can properly retrieve the bounds of 3d layers
     * 
     * @throws Exception
     */
    public void testBounds() throws Exception {
        ReferencedEnvelope env = data.getFeatureSource(LINE3D).getBounds();

        // check we got the right 2d component
        Envelope expected = new Envelope(1, 5, 0, 4);
        assertEquals(expected, env);

        // check the srs the expected one
        assertEquals(epsg4326, env.getCoordinateReferenceSystem());
    }

    public void testRendererBehaviour() throws Exception {
        // make sure the hints are supported
        FeatureSource fs = data.getFeatureSource(LINE3D);
        assertTrue(fs.getSupportedHints().contains(Hints.JTS_COORDINATE_SEQUENCE_FACTORY));

        // setup a query that mimicks the streaming renderer behaviour
        DefaultQuery q = new DefaultQuery(LINE3D);
        Hints hints = new Hints(Hints.JTS_COORDINATE_SEQUENCE_FACTORY,
                new LiteCoordinateSequenceFactory());
        q.setHints(hints);

        // check the srs you get is the expected one 
         FeatureCollection fc = fs.getFeatures(q);
         FeatureType fcSchema = fc.getSchema();
        assertEquals(epsg4326, fcSchema.getCoordinateReferenceSystem());
         assertEquals(epsg4326, fcSchema.getGeometryDescriptor().getCoordinateReferenceSystem());

        // build up the reference 2d line, the 3d one is (1 1 0, 2 2 0, 4 2 1, 5
        // 1 1)
        LineString expected = new GeometryFactory().createLineString(new Coordinate[] {
                new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(4, 2),
                new Coordinate(5, 1) });

        // check feature reader and the schema
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = data.getFeatureReader(q, Transaction.AUTO_COMMIT);
        assertEquals(epsg4326, fr.getFeatureType().getCoordinateReferenceSystem());
        assertEquals(epsg4326, fr.getFeatureType().getGeometryDescriptor()
                .getCoordinateReferenceSystem());
        assertTrue(fr.hasNext());
        SimpleFeature f = fr.next();
        assertTrue(expected.equals((Geometry) f.getDefaultGeometry()));
        fr.close();
    }
    
    /**
     * Checkes the two feature types are equal, taking into consideration the eventual modification
     * the datastore had to perform in order to actually manage the type (change in names case, for example)
     */
    protected void assertFeatureTypesEqual(SimpleFeatureType expected, SimpleFeatureType actual) {
        for (int i = 0; i < expected.getAttributeCount(); i++) {
            AttributeDescriptor expectedAttribute = expected.getDescriptor(i);
            AttributeDescriptor actualAttribute = actual.getDescriptor(i);

            assertAttributesEqual(expectedAttribute,actualAttribute);
        }

        // make sure the geometry is nillable and has minOccurrs to 0
        if(expected.getGeometryDescriptor() != null) {
            AttributeDescriptor dg = actual.getGeometryDescriptor();
            assertTrue(dg.isNillable());
            assertEquals(0, dg.getMinOccurs());
        }
    }

    /**
     * Checkes the two feature types are equal, taking into consideration the eventual modification
     * the datastore had to perform in order to actually manage the type (change in names case, for example)
     */
    protected void assertAttributesEqual(AttributeDescriptor expected, AttributeDescriptor actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getMinOccurs(), actual.getMinOccurs());
        assertEquals(expected.getMaxOccurs(), actual.getMaxOccurs());
        assertEquals(expected.isNillable(), actual.isNillable());
        assertEquals(expected.getDefaultValue(), actual.getDefaultValue());

        AttributeType texpected = expected.getType();
        AttributeType tactual = actual.getType();

        if ( Number.class.isAssignableFrom( texpected.getBinding() ) ) {
            assertTrue( Number.class.isAssignableFrom( tactual.getBinding() ) );
        }
        else if ( Geometry.class.isAssignableFrom( texpected.getBinding())) {
            assertTrue( Geometry.class.isAssignableFrom( tactual.getBinding()));
        }
        else {
            assertTrue(texpected.getBinding().isAssignableFrom(tactual.getBinding()));    
        }
        
    }
    
    protected boolean areCRSEqual(CoordinateReferenceSystem crs1, CoordinateReferenceSystem crs2) {
        
        if (crs1==null && crs2==null)
            return true;
        
        if (crs1==null ) return false;
            
        return crs1.equals(crs2); 
    }

}
