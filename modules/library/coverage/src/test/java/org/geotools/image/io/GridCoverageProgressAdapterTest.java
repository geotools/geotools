/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;

import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.image.ImageWorkerTest;
import org.geotools.util.DefaultProgressListener;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;


/**
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class GridCoverageProgressAdapterTest extends Assert {
    
    /**
     * The logger to use for this class.
     */
    private final static Logger LOGGER = Logging.getLogger(GridCoverageProgressAdapterTest.class);
    @Test
    public void testInReadingCanceled() throws Exception{
        final DefaultProgressListener adaptee= new DefaultProgressListener();
        final ProgressListener myListener= new ProgressListener() {
           

            @Override
            public void warningOccurred(String source, String location, String warning) {
                adaptee.warningOccurred(source, location, warning);
                
            }
            
            @Override
            public void started() {
                adaptee.started();
                LOGGER.info("started");
            }
            
            @Override
            public void setTask(InternationalString task) {
                adaptee.setTask(task);
                
            }
            
            @Override
            public void setDescription(String description) {
                adaptee.setDescription(description);
                
            }
            
            @Override
            public void setCanceled(boolean cancel) {
                adaptee.setCanceled(cancel);
                LOGGER.info("canceled");
            }
            
            @Override
            public void progress(float percent) {
                synchronized (this) {
                    try {
                        this.wait(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                assertTrue(percent>=0);
                assertTrue(percent<=100);
                adaptee.progress(percent);
                LOGGER.info("progress:"+percent);
                
            }
            
            @Override
            public boolean isCanceled() {
                return adaptee.isCanceled();
            }
            
            @Override
            public InternationalString getTask() {
                return adaptee.getTask();
            }
            
            @Override
            public float getProgress() {
                return adaptee.getProgress();
            }
            
            @Override
            public String getDescription() {
                return adaptee.getDescription();
            }
            
            @Override
            public void exceptionOccurred(Throwable exception) {
                adaptee.exceptionOccurred(exception);
                
            }
            
            @Override
            public void dispose() {
                adaptee.dispose();
                
            }
            
            @Override
            public void complete() {
                adaptee.complete();
                LOGGER.info("completed");
            }
        };
        
        
        // listener
        final GridCoverageReaderProgressAdapter readerAdapter= new GridCoverageReaderProgressAdapter(myListener);
        final InputStream input = TestData.openStream(GridCoverage2D.class, "CHL01195.png");
        
        ImageReader reader = ImageIO.getImageReadersByFormatName("png").next();
        reader.setInput(ImageIOExt.createImageInputStream(input));
        reader.addIIOReadProgressListener(readerAdapter);
        reader.addIIOReadUpdateListener(readerAdapter);
        TimerTask task = new TimerTask() {
            
            @Override
            public void run() {
               readerAdapter.monitor.setCanceled(true);
                
            }
        };
        new Timer().schedule(task, 1500);
        BufferedImage image = reader.read(0);
        reader.dispose();
        image.flush();
        image=null;
        
        assertFalse(adaptee.isCompleted());
        assertTrue(adaptee.isStarted());
        assertTrue(adaptee.isCanceled());
    }
    
    @Test
    public void testInReading() throws Exception{
        final DefaultProgressListener adaptee= new DefaultProgressListener();
        final ProgressListener myListener= new ProgressListener() {
           

            @Override
            public void warningOccurred(String source, String location, String warning) {
                adaptee.warningOccurred(source, location, warning);
                
            }
            
            @Override
            public void started() {
                adaptee.started();
                LOGGER.info("started");
            }
            
            @Override
            public void setTask(InternationalString task) {
                adaptee.setTask(task);
                
            }
            
            @Override
            public void setDescription(String description) {
                adaptee.setDescription(description);
                
            }
            
            @Override
            public void setCanceled(boolean cancel) {
                adaptee.setCanceled(cancel);
                LOGGER.info("canceled");
            }
            
            @Override
            public void progress(float percent) {
                assertTrue(percent>=0);
                assertTrue(percent<=100);
                adaptee.progress(percent);
                LOGGER.info("progress:"+percent);
                
            }
            
            @Override
            public boolean isCanceled() {
                return adaptee.isCanceled();
            }
            
            @Override
            public InternationalString getTask() {
                return adaptee.getTask();
            }
            
            @Override
            public float getProgress() {
                return adaptee.getProgress();
            }
            
            @Override
            public String getDescription() {
                return adaptee.getDescription();
            }
            
            @Override
            public void exceptionOccurred(Throwable exception) {
                adaptee.exceptionOccurred(exception);
                
            }
            
            @Override
            public void dispose() {
                adaptee.dispose();
                
            }
            
            @Override
            public void complete() {
                adaptee.complete();
                LOGGER.info("completed");
            }
        };
        
        
        // listener
        final GridCoverageReaderProgressAdapter readerAdapter= new GridCoverageReaderProgressAdapter(myListener);
        final InputStream input = TestData.openStream(GridCoverage2D.class, "CHL01195.png");
        
        ImageReader reader = ImageIO.getImageReadersByFormatName("png").next();
        reader.setInput(ImageIOExt.createImageInputStream(input));
        reader.addIIOReadProgressListener(readerAdapter);
        reader.addIIOReadUpdateListener(readerAdapter);
        BufferedImage image = reader.read(0);
        reader.dispose();
        image.flush();
        image=null;
        
        assertTrue(adaptee.isCompleted());
        assertTrue(adaptee.isStarted());
        assertFalse(adaptee.isCanceled());
    }
    

    @Test
    public void testInWriting() throws Exception {
        final DefaultProgressListener adaptee= new DefaultProgressListener();
        final ProgressListener myListener= new ProgressListener() {
    
            @Override
            public void warningOccurred(String source, String location, String warning) {
                adaptee.warningOccurred(source, location, warning);
                
            }
            
            @Override
            public void started() {
                adaptee.started();               
            }
            
            @Override
            public void setTask(InternationalString task) {
                adaptee.setTask(task);
                
            }
            
            @Override
            public void setDescription(String description) {
                adaptee.setDescription(description);
                
            }
            
            @Override
            public void setCanceled(boolean cancel) {
                adaptee.setCanceled(cancel);
                
            }
            
            @Override
            public void progress(float percent) {
                assertTrue(percent>=0);
                assertTrue(percent<=100);
                adaptee.progress(percent);
                
            }
            
            @Override
            public boolean isCanceled() {
                return adaptee.isCanceled();
            }
            
            @Override
            public InternationalString getTask() {
                return adaptee.getTask();
            }
            
            @Override
            public float getProgress() {
                return adaptee.getProgress();
            }
            
            @Override
            public String getDescription() {
                return adaptee.getDescription();
            }
            
            @Override
            public void exceptionOccurred(Throwable exception) {
                adaptee.exceptionOccurred(exception);
                
            }
            
            @Override
            public void dispose() {
                adaptee.dispose();
                
            }
            
            @Override
            public void complete() {
                adaptee.complete();
                
            }
        };
        
        
        // listener
        final GridCoverageWriterProgressAdapter writerAdapter= new GridCoverageWriterProgressAdapter(myListener);
        final InputStream input = TestData.openStream(GridCoverage2D.class, "CHL01195.png");
        final BufferedImage image = ImageIO.read(input);
        
        ImageWriter writer = ImageIO.getImageWritersByFormatName("tiff").next();
        writer.setOutput(ImageIOExt.createImageOutputStream(image, TestData.temp(ImageWorkerTest.class, "CHL01195.tif")));
        writer.addIIOWriteProgressListener(writerAdapter);
        writer.addIIOWriteWarningListener(writerAdapter);
        writer.write(image);
        writer.dispose();
        
        assertTrue(adaptee.isCompleted());
        assertTrue(adaptee.isStarted());
        assertFalse(adaptee.isCanceled());
    }
    
    @Test
    public void testInWritingCanceled() throws Exception {
        final DefaultProgressListener adaptee= new DefaultProgressListener();
        final ProgressListener myListener= new ProgressListener() {
    
            @Override
            public void warningOccurred(String source, String location, String warning) {
                adaptee.warningOccurred(source, location, warning);
                
            }
            
            @Override
            public void started() {
                adaptee.started();  
                LOGGER.info("started");
            }
            
            @Override
            public void setTask(InternationalString task) {
                adaptee.setTask(task);
                
            }
            
            @Override
            public void setDescription(String description) {
                adaptee.setDescription(description);
                
            }
            
            @Override
            public void setCanceled(boolean cancel) {
                adaptee.setCanceled(cancel);
                LOGGER.info("requesting cancel");
                
            }
            
            @Override
            public void progress(float percent) {
                synchronized (this) {
                    try {
                        this.wait(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }                
                assertTrue(percent>=0);
                assertTrue(percent<=100);
                adaptee.progress(percent);
                LOGGER.info("progress:"+percent);                
                
            }
            
            @Override
            public boolean isCanceled() {
                return adaptee.isCanceled();
            }
            
            @Override
            public InternationalString getTask() {
                return adaptee.getTask();
            }
            
            @Override
            public float getProgress() {
                return adaptee.getProgress();
            }
            
            @Override
            public String getDescription() {
                return adaptee.getDescription();
            }
            
            @Override
            public void exceptionOccurred(Throwable exception) {
                adaptee.exceptionOccurred(exception);
                
            }
            
            @Override
            public void dispose() {
                adaptee.dispose();
                
            }
            
            @Override
            public void complete() {
                adaptee.complete();
                LOGGER.info("completed");
            }
        };
        
        
        // listener
        final GridCoverageWriterProgressAdapter writerAdapter= new GridCoverageWriterProgressAdapter(myListener);
        final InputStream input = TestData.openStream(GridCoverage2D.class, "CHL01195.png");
        final BufferedImage image = ImageIO.read(input);
        
        ImageWriter writer = ImageIO.getImageWritersByFormatName("tiff").next();
        writer.setOutput(ImageIOExt.createImageOutputStream(image, TestData.temp(ImageWorkerTest.class, "CHL01195.tif")));
        writer.addIIOWriteProgressListener(writerAdapter);
        writer.addIIOWriteWarningListener(writerAdapter);
        TimerTask task = new TimerTask() {
            
            @Override
            public void run() {
               writerAdapter.monitor.setCanceled(true);
                
            }
        };   
        new Timer().schedule(task, 1000);
        writer.write(image);
        writer.dispose();
        
        assertFalse(adaptee.isCompleted());
        assertTrue(adaptee.isStarted());
        assertTrue(adaptee.isCanceled());
    }
}

