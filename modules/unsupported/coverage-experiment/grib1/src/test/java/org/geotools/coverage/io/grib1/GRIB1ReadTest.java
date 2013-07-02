///*
// *    JImageIO-extension - OpenSource Java Image translation Library
// *    http://www.geo-solutions.it/
// *    (C) 2007, GeoSolutions
// *
// *    This library is free software; you can redistribute it and/or
// *    modify it under the terms of the GNU Lesser General Public
// *    License as published by the Free Software Foundation;
// *    either version 3 of the License, or (at your option) any later version.
// *
// *    This library is distributed in the hope that it will be useful,
// *    but WITHOUT ANY WARRANTY; without even the implied warranty of
// *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// *    Lesser General Public License for more details.
// */
//package org.geotools.coverage.io.grib1;
//
//import it.geosolutions.imageio.plugins.netcdf.UcarImageMetadata;
//import it.geosolutions.imageio.utilities.ImageIOUtilities;
//import it.geosolutions.resources.TestData;
//
//import java.awt.image.RenderedImage;
//import java.io.File;
//import java.io.FileFilter;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.imageio.ImageReadParam;
//import javax.imageio.ImageReader;
//
//import org.junit.Assert;
///**
// * 
// * @author Simone Giannecchini, GeoSolutions
// *
// */
//public class GRIB1ReadTest {
//
//    private static final Logger LOGGER = Logger.getLogger(GRIB1ReadTest.class.toString());
//
//    /**
//     * Simple read from a Grib File containing a lot of records
//     * 
//     * @throws IOException
//     */
//    @org.junit.Test
//    public void testReadSingleFile() throws IOException {
//        File dir = TestData.file(this, ".");
//        // File dir = new File(dataPathPrefix);
//        File[] files = dir.listFiles(new FileFilter() {
//            public boolean accept(File pathname) {
//                final String path = pathname.getAbsolutePath();
//                if (path.endsWith(".grib") || path.endsWith(".grb"))
//                    return true;
//                return false;
//            }
//        });
//        for (File inputFile : files) {
//        	if(LOGGER.isLoggable(Level.INFO))
//        		LOGGER.info("Testing file "+ inputFile.getAbsolutePath());
//
//            ImageReader reader = new GRIB1ImageReaderSpi().createReaderInstance();
//            try{
//	            reader.setInput(inputFile);
//	            final int nImages = reader.getNumImages(false);
//	            for (int index = 0; index < nImages; index++) {
//	                final ImageReadParam param = new ImageReadParam();
//	                param.setSourceSubsampling(2, 2, 0, 0);
//	
//	                RenderedImage ri = reader.read(index, param);
//	                if (TestData.isInteractiveTest())
//	                    ImageIOUtilities.visualize(ri);
//	                else
//	                    Assert.assertNotNull(ri.getData());
//	                ImageIOUtilities.displayImageIOMetadata(reader.getImageMetadata(index).getAsTree(UcarImageMetadata.nativeMetadataFormatName));
//	                ImageIOUtilities.displayImageIOMetadata(reader.getImageMetadata(index).getAsTree(GRIB1ImageMetadata.nativeMetadataFormatName));
//	            }
//            }
//            finally{
//            	try{
//            		reader.dispose();
//            	}
//            	catch (Throwable e) {
//					if(LOGGER.isLoggable(Level.FINE))
//						LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
//				}
//            }
//            
//        }
//    }
//}
