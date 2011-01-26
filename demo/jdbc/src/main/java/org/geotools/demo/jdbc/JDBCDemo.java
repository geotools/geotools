package org.geotools.demo.jdbc;

import java.util.HashMap;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.h2.tools.DeleteDbFiles;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.BBOX;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Demo application illustrating the use of JDBC datastores.
 * <p>
 * This demo runs with an embedded H2 database, but could easily be 
 * adapted to PostGIS,Oracle,etc...
 * </p>
 * @author Justin Deoliveira
 *
 *
 * @source $URL$
 */
public class JDBCDemo {

    public static void main(String[] args) throws Exception {
        //clean up any old files
        DeleteDbFiles.execute(System.getProperty("user.dir"),"acme",true);
        
        //get a new datastore
        HashMap params = new HashMap();
        params.put( "dbtype", "h2");
        params.put( "database", "acme");
        
        //To use postgis:
        // 1. Uncomment the following, and comment out the above 
        // 2. Delete the acme table if it does exist
        // 3. Change the parameters as necessary
//        HashMap params = new HashMap();
//        params.put( "dbtype", "postgis");
//        params.put( "host", "localhost");
//        params.put( "port", "5432");
//        params.put( "database", "acme");
//        params.put( "user", "geotools");
//        params.put( "password", "");
        
        DataStore ds = (DataStore) DataStoreFinder.getDataStore( params );
         
        //create a new schema
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setNamespaceURI( "http://acme.com");
        tb.setName( "widgets");
        tb.srs("EPSG:4326");
        tb.add( "geom", Point.class );
        tb.add( "name", String.class );
        tb.add( "type", Integer.class );
        tb.add( "price", Double.class );
        SimpleFeatureType type = tb.buildFeatureType();
        ds.createSchema( type );
    
        //write some features
        GeometryFactory gf = new GeometryFactory();
        FeatureWriter fw = ds.getFeatureWriter( "widgets", Transaction.AUTO_COMMIT );
        fw.hasNext();
        
        SimpleFeature f = (SimpleFeature) fw.next();
        f.setAttribute( "geom", gf.createPoint(new Coordinate(1,1)) );
        f.setAttribute( "name", "foo" );
        f.setAttribute( "type", 1 );
        f.setAttribute( "price", 1.10 );
        fw.write();
        
        fw.hasNext();
        f = (SimpleFeature) fw.next();
        
        f.setAttribute( "geom", gf.createPoint(new Coordinate(10,10)) );
        f.setAttribute( "name", "bar" );
        f.setAttribute( "type", 10 );
        f.setAttribute( "price", 10.10 );
        fw.write();
        
        fw.hasNext();
        f = (SimpleFeature) fw.next();
        f.setAttribute( "geom", gf.createPoint(new Coordinate(20,20)) );
        f.setAttribute( "name", "foobar" );
        f.setAttribute( "type", 20 );
        f.setAttribute( "price", 20.20 );
        fw.write();
        
        fw.close();
        
        //grab all features
        FeatureSource fs = ds.getFeatureSource( "widgets");
        System.out.println( "Number of features = " + fs.getCount(Query.ALL) );
        
        FeatureCollection features = fs.getFeatures();
        FeatureIterator fi = features.features();
        try {
            while( fi.hasNext() ) {
                f = (SimpleFeature) fi.next();
                System.out.println( f.getID() );
            }
        }
        finally {
            features.close( fi );
        }
        
        //query some features
        FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
        BBOX bbox = ff.bbox( "geom", 5, 5, 15, 15, "EPSG:4326");
        
        DefaultQuery q = new DefaultQuery("widgets", bbox );
        features = ds.getFeatureSource("widgets").getFeatures( q );
        
        System.out.println("Number of features = " + features.size() );
        fi = features.features();
        try {
            while( fi.hasNext() ) {
                f = (SimpleFeature) fi.next();
                System.out.println( f.getID() );
            }
        }
        finally {
            features.close( fi );
        }
    }
}
