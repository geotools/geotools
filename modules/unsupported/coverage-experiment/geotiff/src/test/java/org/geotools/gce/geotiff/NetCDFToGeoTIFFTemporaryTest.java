///*
// *    GeoTools - The Open Source Java GIS Toolkit
// *    http://geotools.org
// *
// *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
// *
// *    This library is free software; you can redistribute it and/or
// *    modify it under the terms of the GNU Lesser General Public
// *    License as published by the Free Software Foundation;
// *    version 2.1 of the License.
// *
// *    This library is distributed in the hope that it will be useful,
// *    but WITHOUT ANY WARRANTY; without even the implied warranty of
// *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// *    Lesser General Public License for more details.
// */
//package org.geotools.gce.geotiff;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.text.ParseException;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeSet;
//import java.util.logging.Logger;
//
//import javax.media.jai.JAI;
//import javax.media.jai.TileCache;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import org.geotools.coverage.grid.GridCoverage2D;
//import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;
//import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
//import org.geotools.coverage.io.CoverageAccess;
//import org.geotools.coverage.io.CoverageReadRequest;
//import org.geotools.coverage.io.CoverageResponse;
//import org.geotools.coverage.io.CoverageSource;
//import org.geotools.coverage.io.CoverageAccess.AccessType;
//import org.geotools.coverage.io.CoverageResponse.Status;
//import org.geotools.coverage.io.geotiff.GeoTiffDriver;
//import org.geotools.coverage.io.geotiff.GeoTiffReader;
//import org.geotools.coverage.io.geotiff.GeoTiffWriter;
//import org.geotools.coverage.io.impl.BaseFileDriver;
//import org.geotools.coverage.io.impl.DefaultCoverageReadRequest;
//import org.geotools.coverage.io.impl.range.DefaultRangeType;
//import org.geotools.coverage.io.netcdf.NetCDFDriver;
//import org.geotools.coverage.io.range.FieldType;
//import org.geotools.coverage.io.range.RangeType;
//import org.geotools.test.TestData;
//import org.opengis.coverage.Coverage;
//import org.opengis.feature.type.Name;
//import org.opengis.geometry.BoundingBox;
//import org.opengis.geometry.Envelope;
//import org.opengis.referencing.FactoryException;
//import org.opengis.referencing.crs.CoordinateReferenceSystem;
//import org.opengis.referencing.operation.TransformException;
//import org.opengis.temporal.TemporalGeometricPrimitive;
//
///**
// * @author Simone Giannecchini
// * 
// * @source $URL:
// *         http://svn.geotools.org/geotools/trunk/gt/plugin/geotiff/test/org/geotools/gce/geotiff/GeoTiffVisualizationTest.java $
// */
//public class NetCDFToGeoTIFFTemporaryTest extends TestCase {
//
////    private final static String filePath = "C:/Work/data/rixen/lsvc08/";
////
////    private final static String subPath = "NRL/NCOM/20080929/";
////    
//    private final static String filePath = "E:/Work/data/prove/testWriter";
//
//    private final static String subPath = "/";
//
//    private final static String name = "converted_temp_m08_nest0_20080929.nc";
//    
////    private final static String filePath = "C:/Work/data/netcdf";
////
////    private final static String subPath = "/";
////
////    private final static String name = "2008091700-0500.nc";
//
//    private final static String fileName = filePath + subPath + name;
//    
//    private String writtenFile="";
//
//    private static final Logger LOGGER = org.geotools.util.logging.Logging
//            .getLogger(NetCDFToGeoTIFFTemporaryTest.class.toString());
//
//    private static GeoTiffDriver factory;
//    
//    /**
//     * 
//     */
//    public NetCDFToGeoTIFFTemporaryTest() {
//        this("Writer Test!");
//    }
//
//    public NetCDFToGeoTIFFTemporaryTest(String string) {
//        super(string);
//    }
//
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//
//        // Test read exploiting common JAI operations (Crop-Translate-Rotate)
//        suite.addTest(new NetCDFToGeoTIFFTemporaryTest("testWrite"));
//        
////        suite.addTest(new NetCDFToGeoTIFFTemporaryTest("test2"));
//        
//
//        return suite;
//    }
//
//    public static void main(java.lang.String[] args) {
//        junit.textui.TestRunner.run(suite());
//    }
//
//    /*
//     * @see TestCase#setUp()
//     */
//    protected void setUp() throws Exception {
//        super.setUp();
//        final JAI jaiDef = JAI.getDefaultInstance();
//
//        // using a big tile cache
//        final TileCache cache = jaiDef.getTileCache();
//        cache.setMemoryCapacity(64 * 1024 * 1024);
//        cache.setMemoryThreshold(0.75f);
//
//        factory = new GeoTiffDriver();
//    }
//
//
//    /**
//     * Testing {@link GeoTiffWriter} capabilities to write a cropped coverage.
//     * 
//     * @throws IllegalArgumentException
//     * @throws IOException
//     * @throws UnsupportedOperationException
//     * @throws ParseException
//     * @throws FactoryException
//     * @throws TransformException
//     */
//    public void testWrite() throws IllegalArgumentException, IOException,
//            UnsupportedOperationException, ParseException, FactoryException,
//            TransformException {
//        final File dir = new File("E:/work/data/prove");
//        final StringBuffer buffer = new StringBuffer();
//        writtenFile = new StringBuffer(dir.getAbsolutePath())
//        .append("/testWriter/").toString();
//        final File writedir = new File(writtenFile);
//        writedir.mkdir();
//
//        final BaseFileDriver driver = new NetCDFDriver();
//        File file = new File(fileName);
//        final URL source = file.toURI().toURL();
//        if (driver.canConnect(source)) {
//
//            // getting access to the file
//            CoverageAccess access = driver.connect(source, null, null,
//                    null);
//
//            if (access == null)
//                throw new IOException("Unable to connect");
//
//            // get the names
//            List<Name> names = access.getNames(null);
//            for (Name name : names) {
//                // get a source
//                CoverageSource gridSource = access.access(name, null,
//                        AccessType.READ_ONLY, null, null);
//                if (gridSource == null)
//                    throw new IOException("Unable to access");
//
//                Set<TemporalGeometricPrimitive> temporalDomain = gridSource
//                        .getTemporalDomain(null);
//                List<BoundingBox> horizontalDomain = gridSource
//                        .getHorizontalDomain(false, null);
//                Set<Envelope> verticalDomain = gridSource.getVerticalDomain(
//                        false, null);
//                CoordinateReferenceSystem crs = gridSource
//                        .getCoordinateReferenceSystem(null);
//                
//                RangeType range = gridSource.getRangeType(null);
//
//                CoverageReadRequest readRequest = new DefaultCoverageReadRequest();
//                // //
//                //
//                // Setting up a limited range for the request.
//                //
//                // //
//                final int numFieldTypes = range.getNumFieldTypes();
//
//                Iterator<FieldType> ftIterator = range.getFieldTypes()
//                        .iterator();
//                FieldType ft = null;
//                while (ftIterator.hasNext()){
//                    ft = ftIterator.next();
//                    if (ft!=null){
//                        if (ft.getName().toString().contains("temp"))
//                            break;
//                    }
//                }
//                    
//                if (ft != null) {
//                    TreeSet<FieldType> fieldSet = new TreeSet<FieldType>();
//                    fieldSet.add(ft);
//                    RangeType rangeSubset = new DefaultRangeType(range
//                            .getName(), range.getDescription(), fieldSet);
//                    readRequest.setRangeSubset(rangeSubset);
//                }
//
//                LinkedHashSet<Envelope> requestedVerticalSubset = new LinkedHashSet<Envelope>();
//                final Iterator<Envelope> iterator = verticalDomain.iterator();
//                requestedVerticalSubset.add(iterator.next());
//                final Iterator<TemporalGeometricPrimitive> timeIterator = temporalDomain.iterator();
//                
//                readRequest.setVerticalSubset(requestedVerticalSubset);
//                TreeSet<TemporalGeometricPrimitive> requestedTemporalSubset = new TreeSet<TemporalGeometricPrimitive> ();
//                requestedTemporalSubset.add(timeIterator.next());
//                readRequest.setTemporalSubset(requestedTemporalSubset);
//                
//                CoverageResponse response = gridSource.read(readRequest, null);
//                if (response == null || response.getStatus() != Status.SUCCESS
//                        || !response.getExceptions().isEmpty())
//                    throw new IOException("Unable to read");
//
//                Collection<? extends Coverage> results = response
//                        .getResults(null);
//                for (Coverage c : results) {
//                    GridCoverage2D coverage = (GridCoverage2D) c;
//
//                    // Crs and envelope
//                    if (TestData.isInteractiveTest()) {
//                        buffer.append("CRS: ").append(
//                                coverage.getCoordinateReferenceSystem2D()
//                                        .toWKT()).append("\n");
//                        buffer.append("GG: ").append(
//                                coverage.getGridGeometry().toString()).append(
//                                "\n");
//                    }
//                    access.dispose();
//
//                    final File writeFile = new File(writedir, coverage
//                            .getName().toString()
//                            + ".tiff");
//                    GeoTiffWriter writer = new GeoTiffWriter(writeFile);
//                    writer.write(coverage, null);
//                    writer.dispose();
//                    
//                    GeoTiffReader reader = new GeoTiffReader(writeFile.toURI().toURL());
//                    GeoTiffIIOMetadataDecoder readMetadata = reader.getMetadata();
//                    if (readMetadata.hasNoData())
//                        System.out.println("NODATA IS: " + readMetadata.getNoData());
//                }
//
//            }
//
//        } else
//            buffer.append("NOT ACCEPTED").append("\n");
//        if (TestData.isInteractiveTest())
//            LOGGER.info(buffer.toString());
//
//    }
//    
//    public void test2() throws IOException{
//        writtenFile = "C:/work/data/prove/testWriter/gdal.tiff";
//        File writeFile = new File(writtenFile);
//        StringBuffer buffer = new StringBuffer("");
//        GeoTiffReader reader = new GeoTiffReader(writeFile.toURI().toURL());
//        reader.read(null);
//             IIOMetadataDumper iIOMetadataDumper = new
//             IIOMetadataDumper(
//             ((GeoTiffReader) reader).getMetadata()
//             .getRootNode());
//             buffer.append("TIFF metadata: ").append(
//            iIOMetadataDumper.getMetadata()).append("\n");
//             LOGGER.info(buffer.toString());
//    }
//}
