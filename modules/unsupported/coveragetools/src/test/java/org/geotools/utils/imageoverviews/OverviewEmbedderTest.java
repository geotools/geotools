/**
 * 
 */
package org.geotools.utils.imageoverviews;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.media.jai.JAI;

import org.geotools.util.logging.Logging;
import org.geotools.utils.progress.ExceptionEvent;
import org.geotools.utils.progress.ProcessingEvent;
import org.geotools.utils.progress.ProcessingEventListener;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Testing {@link OverviewsEmbedder}.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class OverviewEmbedderTest {
    
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
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }
    

    @Test
    public void firstTest() throws FileNotFoundException, IOException{
        final OverviewsEmbedder oe= new OverviewsEmbedder();
        oe.setDownsampleStep(2);
        oe.setNumSteps(5);
        oe.setScaleAlgorithm(OverviewsEmbedder.SubsampleAlgorithm.Average.toString());
        oe.setTileCache(JAI.getDefaultInstance().getTileCache());
        oe.setTileWidth(256);
        oe.setTileHeight(256);
        oe.setSourcePath(org.geotools.test.TestData.file(this, "DEM.tiff").getAbsolutePath());
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
        
        
    }

}
