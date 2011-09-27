/**
 * 
 */
package org.geotools.utils.imageoverviews;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.media.jai.JAI;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.geotools.utils.progress.ExceptionEvent;
import org.geotools.utils.progress.ProcessingEvent;
import org.geotools.utils.progress.ProcessingEventListener;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Testing {@link OverviewsEmbedder}.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 * @source $URL$
 */
public class OverviewEmbedderTest extends Assert{
    
    private final static Logger LOGGER = Logging.getLogger(OverviewEmbedderTest.class);

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        File inputFile=TestData.file(this, "DEM.tiff");
        FileUtils.copyFile(inputFile, new File(inputFile.getParent(), "DEM_.tiff"));
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        FileUtils.deleteQuietly(TestData.temp(this, "DEM_.tiff"));
    }
    
    @Test
   
    public void nearest() throws FileNotFoundException, IOException{
//        JAI.getDefaultInstance().getTileCache().setMemoryCapacity(512*1024*1024);
//        JAI.getDefaultInstance().getTileScheduler().setParallelism(10);
//        JAI.getDefaultInstance().getTileScheduler().setPrefetchParallelism(10);
        
        final OverviewsEmbedder oe= new OverviewsEmbedder();
        oe.setDownsampleStep(2);
        oe.setNumSteps(5);
        oe.setScaleAlgorithm(OverviewsEmbedder.SubsampleAlgorithm.Nearest.toString());
        // use default 
        oe.setTileCache(JAI.getDefaultInstance().getTileCache());
        oe.setTileWidth(256);
        oe.setTileHeight(256);
        oe.setSourcePath(TestData.file(this, "DEM_.tiff").getAbsolutePath());
        oe.addProcessingEventListener(new ProcessingEventListener() {
            
            @Override
            public void getNotification(ProcessingEvent event) {
                LOGGER.info(event.toString());
                
            }
            
            @Override
            public void exceptionOccurred(ExceptionEvent event) {
                LOGGER.warning(event.toString());                
            }
            
        });
        oe.run();
        
        // now red it back and check that things are coherent
        final ImageReader reader= new TIFFImageReaderSpi().createReaderInstance();
        reader.setInput(ImageIO.createImageInputStream(org.geotools.test.TestData.file(this, "DEM_.tiff")));
        assertTrue(reader.getNumImages(true)==6);
        
    }

    @Test
   
        public void average() throws FileNotFoundException, IOException{
    //        JAI.getDefaultInstance().getTileCache().setMemoryCapacity(512*1024*1024);
    //        JAI.getDefaultInstance().getTileScheduler().setParallelism(10);
    //        JAI.getDefaultInstance().getTileScheduler().setPrefetchParallelism(10);
            
            final OverviewsEmbedder oe= new OverviewsEmbedder();
            oe.setDownsampleStep(2);
            oe.setNumSteps(5);
            oe.setScaleAlgorithm(OverviewsEmbedder.SubsampleAlgorithm.Average.toString());
            // use default 
            oe.setTileCache(JAI.getDefaultInstance().getTileCache());
            oe.setTileWidth(256);
            oe.setTileHeight(256);
            oe.setSourcePath(TestData.file(this, "DEM_.tiff").getAbsolutePath());
            oe.addProcessingEventListener(new ProcessingEventListener() {
                
                @Override
                public void getNotification(ProcessingEvent event) {
                    LOGGER.info(event.toString());
                    
                }
                
                @Override
                public void exceptionOccurred(ExceptionEvent event) {
                    LOGGER.warning(event.toString());                
                }
                
            });
            oe.run();
            
            // now red it back and check that things are coherent
            final ImageReader reader= new TIFFImageReaderSpi().createReaderInstance();
            reader.setInput(ImageIO.createImageInputStream(org.geotools.test.TestData.file(this, "DEM_.tiff")));
            assertTrue(reader.getNumImages(true)==6);
            
        }

    @Test
   
        public void bicubic() throws FileNotFoundException, IOException{
    //        JAI.getDefaultInstance().getTileCache().setMemoryCapacity(512*1024*1024);
    //        JAI.getDefaultInstance().getTileScheduler().setParallelism(10);
    //        JAI.getDefaultInstance().getTileScheduler().setPrefetchParallelism(10);
            
            final OverviewsEmbedder oe= new OverviewsEmbedder();
            oe.setDownsampleStep(2);
            oe.setNumSteps(5);
            oe.setScaleAlgorithm(OverviewsEmbedder.SubsampleAlgorithm.Bicubic.toString());
            // use default 
            oe.setTileCache(JAI.getDefaultInstance().getTileCache());
            oe.setTileWidth(256);
            oe.setTileHeight(256);
            oe.setSourcePath(TestData.file(this, "DEM_.tiff").getAbsolutePath());
            oe.addProcessingEventListener(new ProcessingEventListener() {
                
                @Override
                public void getNotification(ProcessingEvent event) {
                    LOGGER.info(event.toString());
                    
                }
                
                @Override
                public void exceptionOccurred(ExceptionEvent event) {
                    LOGGER.warning(event.toString());                
                }
                
            });
            oe.run();
            
            // now red it back and check that things are coherent
            final ImageReader reader= new TIFFImageReaderSpi().createReaderInstance();
            reader.setInput(ImageIO.createImageInputStream(org.geotools.test.TestData.file(this, "DEM_.tiff")));
            assertTrue(reader.getNumImages(true)==6);
            
        }

    @Test
   
        public void bilinear() throws FileNotFoundException, IOException{
    //        JAI.getDefaultInstance().getTileCache().setMemoryCapacity(512*1024*1024);
    //        JAI.getDefaultInstance().getTileScheduler().setParallelism(10);
    //        JAI.getDefaultInstance().getTileScheduler().setPrefetchParallelism(10);
            
            final OverviewsEmbedder oe= new OverviewsEmbedder();
            oe.setDownsampleStep(2);
            oe.setNumSteps(5);
            oe.setScaleAlgorithm(OverviewsEmbedder.SubsampleAlgorithm.Bilinear.toString());
            // use default 
            oe.setTileCache(JAI.getDefaultInstance().getTileCache());
            oe.setTileWidth(256);
            oe.setTileHeight(256);
            oe.setSourcePath(TestData.file(this, "DEM_.tiff").getAbsolutePath());
            oe.addProcessingEventListener(new ProcessingEventListener() {
                
                @Override
                public void getNotification(ProcessingEvent event) {
                    LOGGER.info(event.toString());
                    
                }
                
                @Override
                public void exceptionOccurred(ExceptionEvent event) {
                    LOGGER.warning(event.toString());                
                }
                
            });
            oe.run();
            
            // now red it back and check that things are coherent
            final ImageReader reader= new TIFFImageReaderSpi().createReaderInstance();
            reader.setInput(ImageIO.createImageInputStream(org.geotools.test.TestData.file(this, "DEM_.tiff")));
            assertTrue(reader.getNumImages(true)==6);
            
        }

    @Test
    @Ignore
        public void filtered() throws FileNotFoundException, IOException{
    //        JAI.getDefaultInstance().getTileCache().setMemoryCapacity(512*1024*1024);
    //        JAI.getDefaultInstance().getTileScheduler().setParallelism(10);
    //        JAI.getDefaultInstance().getTileScheduler().setPrefetchParallelism(10);
            
            final OverviewsEmbedder oe= new OverviewsEmbedder();
            oe.setDownsampleStep(2);
            oe.setNumSteps(5);
            oe.setScaleAlgorithm(OverviewsEmbedder.SubsampleAlgorithm.Filtered.toString());
            // use default 
            oe.setTileCache(JAI.getDefaultInstance().getTileCache());
            oe.setTileWidth(256);
            oe.setTileHeight(256);
            oe.setSourcePath(TestData.file(this, "DEM_.tiff").getAbsolutePath());
            final List<Throwable> exceptions = new ArrayList<Throwable>();
            oe.addProcessingEventListener(new ProcessingEventListener() {
                
                @Override
                public void getNotification(ProcessingEvent event) {
                    LOGGER.info(event.toString());
                    
                }
                
                @Override
                public void exceptionOccurred(ExceptionEvent event) {
                    LOGGER.warning(event.toString());
                    exceptions.add(event.getException());
                    event.getException().printStackTrace();
                }
                
            });
            oe.run();
            
            // fail if any exception was reported
            if(exceptions.size() > 0) {
                fail("Failed with " + exceptions.size() + " exceptions during overview embedding");
            }
            
            // now red it back and check that things are coherent
            final ImageReader reader= new TIFFImageReaderSpi().createReaderInstance();
            reader.setInput(ImageIO.createImageInputStream(org.geotools.test.TestData.file(this, "DEM_.tiff")));
            assertEquals(6, reader.getNumImages(true));
            
        }

//    @Test
//        public void wrong() throws FileNotFoundException, IOException{
//    //        JAI.getDefaultInstance().getTileCache().setMemoryCapacity(512*1024*1024);
//    //        JAI.getDefaultInstance().getTileScheduler().setParallelism(10);
//    //        JAI.getDefaultInstance().getTileScheduler().setPrefetchParallelism(10);
//            
//            final OverviewsEmbedder oe= new OverviewsEmbedder();
//            oe.setDownsampleStep(0);
//            oe.setNumSteps(-3);
//            oe.setScaleAlgorithm("geosolutionsrocks!");
//            // use default 
//            oe.setTileCache(JAI.getDefaultInstance().getTileCache());
//            oe.setTileWidth(0);
//            oe.setTileHeight(256);
//            oe.setSourcePath(TestData.file(this, "DEM_.tiff").getAbsolutePath());
//            oe.addProcessingEventListener(new ProcessingEventListener() {
//                
//                @Override
//                public void getNotification(ProcessingEvent event) {
//                    LOGGER.info(event.toString());
//                    
//                }
//                
//                @Override
//                public void exceptionOccurred(ExceptionEvent event) {
//                    LOGGER.warning(event.toString());                
//                }
//                
//            });
//            oe.run();
//            
//
//            
//        }
}
