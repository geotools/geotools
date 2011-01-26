/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.svg;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * DOCUMENT ME!
 *
 * @author James
 * @source $URL$
 */
public class SVGTest extends TestCase {    
    public SVGTest(java.lang.String testName) throws Exception{
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SVGTest.class);

        return suite;
    }

    public void testGenerateSVG() {
        String stylefile = "simple.sld";
        String gmlfile = "simple.gml";
        createSVG("simple.sld", "simple.gml", "simple.svg");
    }

    public void testBlueLake() {
        String stylefile = "bluelake.sld";
        String gmlfile = "bluelake.gml";
        //createSVG(stylefile, gmlfile, "bluelake.svg");
    }

    public void testNameFilterSVG() {
        createSVG("nameFilter.sld", "simple.gml", "nameFilter.svg");
    }

    /**
     * DOCUMENT ME!
     *
     * @param stylefile
     * @param gmlfile
     * @param outfile DOCUMENT ME!
     */
    private void createSVG(final String stylefile, final String gmlfile, final String outfile) {
        /* TODO: restore GML reading
        try {
            GenerateSVG gen = new GenerateSVG();
            File testFile = TestData.file( this, gmlfile );
            // DataSource ds = new GMLDataSource(url);            
            // SimpleFeatureCollection fc = ds.getFeatures(Query.ALL);
            
            URI uri = testFile.toURI();            
            Map hints = new HashMap();
            Object obj = DocumentFactory.getInstance( uri, hints );
            System.out.println( "what is this:"+obj );

            SimpleFeatureCollection fc = null;
            File f = TestData.file( this, stylefile );

            MapContext mapContext = new DefaultMapContext();
            StyleFactory sFac = StyleFactory.createStyleFactory();
            SLDStyle reader = new SLDStyle(sFac, f);
            Style[] style = reader.readXML();
            mapContext.addLayer(fc, style[0]);

            File file = TestData.temp( this, outfile );
            FileOutputStream out = new FileOutputStream( file ); 

            gen.setCanvasSize(new Dimension(500, 500));
            gen.go(mapContext, fc.getBounds(), out);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            fail("failed because of exception " + e.toString());
        }
        */
    }
}
