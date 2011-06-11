package org.geotools.referencing.factory.epsg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.epsg.ThreadedHsqlEpsgFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Dumps the contents of the EPSG database out to a java property file for use by the wkt plugin.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/epsg-hsql/src/test/java/org/geotools/referencing/factory/epsg/PropertyDumper.java $
 */
public class PropertyDumper {
    public static void main(String[] args) throws Exception {
        String filename = "/tmp/wkt.properties";
        Properties diff = new Properties();
        String compare = null;
        if( args.length > 0 ){
            filename = args[0];
        }
        
        if( args.length > 1 ){
            File file = new File( args[1] );
            if( file.exists() ){
                InputStream in = new FileInputStream( file );
                diff.load( in );
            }
        }
        
        FileOutputStream out = new FileOutputStream( filename );
        Writer writer = new BufferedWriter(new OutputStreamWriter(out, "8859_1"));
        writer.append("Generate from EPSG database version "+ThreadedHsqlEpsgFactory.VERSION );
        
        Properties props = new Properties();
        for (String code : CRS.getSupportedCodes("EPSG")) {
            try {
                CoordinateReferenceSystem crs = CRS.decode("EPSG:" + code, true);
                // use toString, it's more lenient that toWKT
                String wkt = crs.toString().replaceAll("\n", "").replaceAll("  ", "");
                // make sure we can parse back what we generated
                CRS.parseWKT(wkt);
                
                props.put(code, wkt);
                
                diff.remove(code);
                
            } catch (Exception e) {
                // we cannot actually decode all codes, but let's list what we can't
                System.out.println("#"+code + " -> " + e.getMessage());
            }
        }
        props.store(out,"Generated from EPSG database version " + ThreadedHsqlEpsgFactory.VERSION);
        if(!diff.isEmpty() ){
            diff.store(out, "Extra Definitions Supplied from Community");
        }
        out.close();
    }
}
