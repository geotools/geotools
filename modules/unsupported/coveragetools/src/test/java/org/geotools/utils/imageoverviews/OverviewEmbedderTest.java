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
//    @Ignore
    public void nearestExternal() throws Exception{
        
        final OverviewsEmbedder oe= new OverviewsEmbedder();
        oe.setDownsampleStep(2);
        oe.setNumSteps(5);
        oe.setScaleAlgorithm(OverviewsEmbedder.SubsampleAlgorithm.Nearest.toString());
        // use default 
        oe.setTileCache(JAI.getDefaultInstance().getTileCache());
        oe.setTileWidth(256);
        oe.setTileHeight(256);
        oe.setExternalOverviews(true);
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
        assertTrue(reader.getNumImages(true)==1);
        reader.reset();
        

        assertTrue(org.geotools.test.TestData.file(this, "DEM_.tif.ovr").exists());
        reader.setInput(ImageIO.createImageInputStream(org.geotools.test.TestData.file(this, "DEM_.tif.ovr")));
        assertTrue(reader.getNumImages(true)==5);
        assertTrue(reader.isImageTiled(0));
        assertEquals(256,reader.getTileHeight(0));
        assertEquals(256,reader.getTileWidth(0));
        reader.dispose();
        
    }
    
    @Test
    public void nearest() throws Exception{
        
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
            
            private double lastProgress=-1;

            @Override
            public void getNotification(ProcessingEvent event) {
                assertTrue(lastProgress<=event.getPercentage()); 
                lastProgress=event.getPercentage();
                assertTrue(lastProgress<=100); 
                assertTrue(lastProgress>=0); 
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
        reader.dispose();
    }
    
    @Test
//    @Ignore
    public void nearestMultiple() throws Exception{
        
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
            
            private double lastProgress=-1;

            @Override
            public void getNotification(ProcessingEvent event) {
                assertTrue(lastProgress<=event.getPercentage()); 
                lastProgress=event.getPercentage();
                assertTrue(lastProgress<=100); 
                assertTrue(lastProgress>=0); 
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
        reader.dispose();
    }

    @Test
//    @Ignore
        public void average() throws Exception{
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
            reader.dispose();
        }

    @Test
//    @Ignore
        public void bicubic() throws Exception{
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
            reader.dispose();
        }

    @Test
//    @Ignore
        public void bilinear() throws Exception{
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
            reader.dispose();
        }

    @Test
    @Ignore
        public void filtered() throws Exception{
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
                exceptions.get(0).printStackTrace();
                fail("Failed with " + exceptions.size() + " exceptions during overview embedding: ");
            }
            
            // now red it back and check that things are coherent
            final ImageReader reader= new TIFFImageReaderSpi().createReaderInstance();
            reader.setInput(ImageIO.createImageInputStream(org.geotools.test.TestData.file(this, "DEM_.tiff")));
            assertEquals(6, reader.getNumImages(true));
            reader.dispose();
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
