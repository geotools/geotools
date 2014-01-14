/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

import java.awt.Color;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.BandMergeDescriptor;
import javax.media.jai.operator.ConstantDescriptor;

import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.Viewer;
import org.geotools.coverage.processing.GridProcessingTestBase;
import org.geotools.referencing.CRS;
import org.geotools.resources.image.ComponentColorModelJAI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.media.imageioimpl.common.PackageUtil;


/**
 * Tests the {@link ImageWorker} implementation.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 * @author Martin Desruisseaux (Geomatys)
 */
public final class ImageWorkerTest extends GridProcessingTestBase {
    
    private final static String GOOGLE_MERCATOR_WKT="PROJCS[\"WGS 84 / Pseudo-Mercator\","+
            "GEOGCS[\"Popular Visualisation CRS\","+
                "DATUM[\"Popular_Visualisation_Datum\","+
                    "SPHEROID[\"Popular Visualisation Sphere\",6378137,0,"+
                        "AUTHORITY[\"EPSG\",\"7059\"]],"+
                    "TOWGS84[0,0,0,0,0,0,0],"+
                    "AUTHORITY[\"EPSG\",\"6055\"]],"+
                "PRIMEM[\"Greenwich\",0,"+
                    "AUTHORITY[\"EPSG\",\"8901\"]],"+
                "UNIT[\"degree\",0.01745329251994328,"+
                    "AUTHORITY[\"EPSG\",\"9122\"]],"+
                "AUTHORITY[\"EPSG\",\"4055\"]],"+
            "UNIT[\"metre\",1,"+
                "AUTHORITY[\"EPSG\",\"9001\"]],"+
            "PROJECTION[\"Mercator_1SP\"],"+
            "PARAMETER[\"central_meridian\",0],"+
            "PARAMETER[\"scale_factor\",1],"+
            "PARAMETER[\"false_easting\",0],"+
            "PARAMETER[\"false_northing\",0],"+
            "AUTHORITY[\"EPSG\",\"3785\"],"+
            "AXIS[\"X\",EAST],"+
            "AXIS[\"Y\",NORTH]]";
            
    /**
     * Image to use for testing purpose.
     */
    private static RenderedImage sstImage, worldImage, chlImage, bathy, smallWorld, gray, grayAlpha;

    /**
     * {@code true} if the image should be visualized.
     */
    private static final boolean SHOW = TestData.isInteractiveTest();

	private static BufferedImage worldDEMImage = null;
    
	/**
	 * Creates a simple 128x128 {@link RenderedImage} for testing purposes.
	 * 
	 * @param maximum
	 * @return
	 */
	private static RenderedImage getSynthetic(final double maximum) {
		final int width = 128;
		final int height = 128;
		final WritableRaster raster = RasterFactory.createBandedRaster(
				DataBuffer.TYPE_DOUBLE, width, height, 1, null);
		final Random random = new Random();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				raster.setSample(x, y, 0,Math.ceil(random.nextDouble()*maximum) );
			}
		}
		final ColorModel cm = new ComponentColorModelJAI(ColorSpace
				.getInstance(ColorSpace.CS_GRAY), false, false,
				Transparency.OPAQUE, DataBuffer.TYPE_DOUBLE);
		final BufferedImage image = new BufferedImage(cm, raster, false, null);
		return image;
	}    
	/**
	 * Creates a test image in RGB with either {@link ComponentColorModel} or {@link DirectColorModel}.
	 * 
	 * @param direct <code>true</code> when we request a {@link DirectColorModel}, <code>false</code> otherwise.
	 * @return 
	 */
	private static BufferedImage getSyntheticRGB(final boolean direct) {
		final int width = 128;
		final int height = 128;
		final BufferedImage image= 
			direct?new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR)
					:
				   new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		final WritableRaster raster =(WritableRaster) image.getData();
		final Random random = new Random();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				raster.setSample(x, y, 0,random.nextInt(256));
			}
		}
		return image;
	} 
	
	/**
	 * Creates a test paletted image with translucency.
	 * 
	 * @return 
	 */
	private static BufferedImage getSyntheticTranslucentIndexed() {
		final byte bb[]= new byte[256];
		for(int i =0;i<256;i++)
			bb[i]=(byte)i;
		final IndexColorModel icm = new IndexColorModel(8, 256, bb, bb, bb,bb);
		final WritableRaster raster=RasterFactory.createWritableRaster(icm.createCompatibleSampleModel(1024, 1024), null);
		for(int i= raster.getMinX();i<raster.getMinX()+raster.getWidth();i++)
			for(int j= raster.getMinY();j<raster.getMinY()+raster.getHeight();j++)
				raster.setSample(i,j, 0, (i+j)/32);
		return new BufferedImage(icm, raster, false,null);		
	}

    /**
     * Loads the image (if not already loaded) and creates the worker instance.
     *
     * @throws IOException If the image was not found.
     */
    @Before
    public void setUp() throws IOException {
        if (sstImage == null) {
            final InputStream input = TestData.openStream(GridCoverage2D.class, "QL95209.png");
            sstImage = ImageIO.read(input);
            input.close();
        }
        if (worldImage == null) {
            final InputStream input = TestData.openStream(GridCoverage2D.class, "world.png");
            worldImage = ImageIO.read(input);
            input.close();
        }
        if (worldDEMImage == null) {
            final InputStream input = TestData.openStream(GridCoverage2D.class, "world_dem.gif");
            worldDEMImage = ImageIO.read(input);
            input.close();
        }        
        if (chlImage == null) {
            final InputStream input = TestData.openStream(GridCoverage2D.class, "CHL01195.png");
            chlImage = ImageIO.read(input);
            input.close();
        }
        if (bathy == null) {
            final InputStream input = TestData.openStream(GridCoverage2D.class, "BATHY.png");
            bathy = ImageIO.read(input);
            input.close();
        }
        
        if (smallWorld == null) {
            final InputStream input = TestData.openStream(GridCoverage2D.class, "small_world.png");
            smallWorld = ImageIO.read(input);
            input.close();
        }        

        if (gray == null) {
            final InputStream input = TestData.openStream(GridCoverage2D.class, "gray.png");
            gray = ImageIO.read(input);
            input.close();
        }   
        
        if (grayAlpha == null) {
            final InputStream input = TestData.openStream(GridCoverage2D.class, "gray-alpha.png");
            grayAlpha = ImageIO.read(input);
            input.close();
        }          
    }


    @Test
    public void testBitmask(){
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(sstImage);
        
        worker.forceBitmaskIndexColorModel();
        assertEquals(  1, worker.getNumBands());
        assertEquals( -1, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertFalse (     worker.isBinary());
        assertTrue  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());
        assertFalse (     worker.isColorSpaceGRAYScale());
        assertFalse (     worker.isTranslucent());

        final BufferedImage directRGB= getSyntheticRGB(true);
        worker = new ImageWorker(directRGB);        
        worker.forceBitmaskIndexColorModel();
        assertEquals(  1, worker.getNumBands());
        assertEquals( -1, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertFalse (     worker.isBinary());
        assertTrue  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());
        assertFalse (     worker.isColorSpaceGRAYScale());
        assertFalse (     worker.isTranslucent());
        
        final BufferedImage componentRGB= getSyntheticRGB(false);
        worker = new ImageWorker(componentRGB);        
        worker.forceBitmaskIndexColorModel();
        assertEquals(  1, worker.getNumBands());
        assertEquals( -1, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertFalse (     worker.isBinary());
        assertTrue  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());
        assertFalse (     worker.isColorSpaceGRAYScale());
        assertFalse (     worker.isTranslucent());
        
        
        final BufferedImage translucentIndexed= getSyntheticTranslucentIndexed();
        worker=new ImageWorker(translucentIndexed);
        assertTrue  (     worker.isBytes());
        assertFalse (     worker.isBinary());
        assertTrue  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());       
        assertTrue (     worker.isTranslucent());    
        
        
        worker.forceIndexColorModelForGIF(true);
        assertEquals(  1, worker.getNumBands());
        assertEquals( 0, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertFalse (     worker.isBinary());
        assertTrue  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());
        assertFalse (     worker.isTranslucent());        
        
    }
    /**
     * Tests capability to write GIF image.
     *
     * @throws IOException If an error occured while writting the image.
     */
    @Test
    public void testGIFImageWrite() throws IOException {
    	assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        // Get the image of the world with transparency.
        ImageWorker worker = new ImageWorker(worldDEMImage);
        show(worker, "Input GIF");
        RenderedImage image = worker.getRenderedImage();
        ColorModel cm = image.getColorModel();
        assertTrue("wrong color model", cm instanceof IndexColorModel);
        assertEquals("wrong transparency model", Transparency.OPAQUE, cm.getTransparency());
        // Writes it out as GIF on a file using index color model with 
        final File outFile = TestData.temp(this, "temp.gif");
        worker.writeGIF(outFile, "LZW", 0.75f);

        // Read it back
        final ImageWorker readWorker = new ImageWorker(ImageIO.read(outFile));
        show(readWorker, "GIF to file");
        image = readWorker.getRenderedImage();
        cm = image.getColorModel();
        assertTrue("wrong color model", cm instanceof IndexColorModel);
        assertEquals("wrong transparency model", Transparency.OPAQUE, cm.getTransparency());

        // Write on an output streams.
        final OutputStream os = new FileOutputStream(outFile);
        worker = new ImageWorker(worldImage);
        worker.forceIndexColorModelForGIF(true);
        worker.writeGIF(os, "LZW", 0.75f);

        // Read it back.
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "GIF to output stream");
        image = readWorker.getRenderedImage();
        cm = image.getColorModel();
        assertTrue("wrong color model", cm instanceof IndexColorModel);
        assertEquals("wrong transparency model", Transparency.BITMASK, cm.getTransparency());
        assertEquals("wrong transparent color index", 255, ((IndexColorModel)cm).getTransparentPixel());
        outFile.delete();
    }

    /**
     * Testing JPEG capabilities.
     *
     * @throws IOException If an error occured while writting the image.
     */
    @Test
    public void testJPEGWrite() throws IOException {
    	assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        // get the image of the world with transparency
        final ImageWorker worker = new ImageWorker(getSyntheticRGB(true));
        show(worker, "Input JPEG");

        // /////////////////////////////////////////////////////////////////////
        // nativeJPEG  with compression JPEG-LS
        // ////////////////////////////////////////////////////////////////////
        final File outFile = TestData.temp(this, "temp.jpeg");
        ImageWorker readWorker;
        if(PackageUtil.isCodecLibAvailable()){
	        worker.writeJPEG(outFile, "JPEG-LS", 0.75f, true);
	        readWorker = new ImageWorker(ImageIO.read(outFile));
	        show(readWorker, "Native JPEG LS");
        } else {
            try{
                worker.writeJPEG(outFile, "JPEG-LS", 0.75f, true);
                assertFalse(true);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        // /////////////////////////////////////////////////////////////////////
        // native JPEG compression
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writeJPEG(outFile, "JPEG", 0.75f, true);
        readWorker = new ImageWorker(ImageIO.read(outFile));
        show(readWorker, "native JPEG");

        // /////////////////////////////////////////////////////////////////////
        // pure java JPEG compression
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writeJPEG(outFile, "JPEG", 0.75f, false);
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "Pure Java JPEG");
        outFile.delete();
    }

    /**
     * Testing PNG capabilities.
     *
     * @throws IOException If an error occured while writting the image.
     */
    @Test
    public void testPNGWrite() throws IOException {
    	assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        // Get the image of the world with transparency.
        final ImageWorker worker = new ImageWorker(worldImage);
        show(worker, "Input file");

        // /////////////////////////////////////////////////////////////////////
        // native png filtered compression 24 bits
        // /////////////////////////////////////////////////////////////////////
        final File outFile = TestData.temp(this, "temp.png");
        worker.writePNG(outFile, "FILTERED", 0.75f, true,false);
        final ImageWorker readWorker = new ImageWorker(ImageIO.read(outFile));
        show(readWorker, "Native PNG24");

        // /////////////////////////////////////////////////////////////////////
        // native png filtered compression 8 bits
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writePNG(outFile, "FILTERED", 0.75f, true,true);
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "native PNG8");

        // /////////////////////////////////////////////////////////////////////
        // pure java png 24
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writePNG(outFile, "FILTERED", 0.75f, false,false);
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "Pure  PNG24");

        // /////////////////////////////////////////////////////////////////////
        // pure java png 8
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writePNG(outFile, "FILTERED", 0.75f, false,true);
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "Pure  PNG8");
        outFile.delete();
        
        // Check we are not expanding to RGB a paletted image
        worker.setImage(sstImage);
        assertTrue(sstImage.getColorModel() instanceof IndexColorModel);
        worker.writePNG(outFile, "FILTERED", 0.75f, false, false);
        readWorker.setImage(ImageIO.read(outFile));
        assertTrue(readWorker.getRenderedImage().getColorModel() instanceof IndexColorModel);
    }
    
    @Test
    public void test16BitGIF() throws Exception {
        // the resource has been compressed since the palette is way larger than the image itself, 
        // and the palette does not get compressed
        InputStream gzippedStream = ImageWorkerTest.class.getResource("test-data/sf-sfdem.tif.gz").openStream();
        GZIPInputStream is = new GZIPInputStream(gzippedStream);
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(is);
            ImageReader reader = new TIFFImageReaderSpi().createReaderInstance(iis);
            reader.setInput(iis);
            BufferedImage bi = reader.read(0);
            if(TestData.isInteractiveTest()){
                ImageIOUtilities.visualize(bi,"before");
            }
            reader.dispose();
            iis.close();
            IndexColorModel icm = (IndexColorModel) bi.getColorModel();
            assertEquals(65536, icm.getMapSize());
            
            final File outFile = TestData.temp(this, "temp.gif");
            ImageWorker worker = new ImageWorker(bi);
            worker.writeGIF(outFile, "LZW", 0.75f);

            // Read it back.
            bi=ImageIO.read(outFile);
            if(TestData.isInteractiveTest()){
                ImageIOUtilities.visualize(bi,"after");
            }
            ColorModel cm = bi.getColorModel();
            assertTrue("wrong color model", cm instanceof IndexColorModel);
            assertEquals("wrong transparency model", Transparency.OPAQUE, cm.getTransparency());
            final IndexColorModel indexColorModel = (IndexColorModel)cm;
            assertEquals("wrong transparent color index", -1, indexColorModel.getTransparentPixel());
            assertEquals("wrong component size", 8, indexColorModel.getComponentSize(0));
            outFile.delete();
        } finally {
            is.close();
        }
    }
    
    @Test
    public void test16BitPNG() throws Exception {
        // the resource has been compressed since the palette is way larger than the image itself, 
        // and the palette does not get compressed
        InputStream gzippedStream = ImageWorkerTest.class.getResource("test-data/sf-sfdem.tif.gz").openStream();
        GZIPInputStream is = new GZIPInputStream(gzippedStream);
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(is);
            ImageReader reader = new TIFFImageReaderSpi().createReaderInstance(iis);
            reader.setInput(iis);
            BufferedImage bi = reader.read(0);
            reader.dispose();
            iis.close();
            IndexColorModel icm = (IndexColorModel) bi.getColorModel();
            assertEquals(65536, icm.getMapSize());
            
            final File outFile = TestData.temp(this, "temp.png");
            ImageWorker worker = new ImageWorker(bi);
            worker.writePNG(outFile, "FILTERED", 0.75f, true, false);
            worker.dispose();
            
            // make sure we can read it 
            BufferedImage back = ImageIO.read(outFile);
            
            // we expect a RGB one
            ComponentColorModel ccm = (ComponentColorModel) back.getColorModel();
            assertEquals(3, ccm.getNumColorComponents());
            
            
            // now ask to write paletted
            worker = new ImageWorker(bi);
            worker.writePNG(outFile, "FILTERED", 0.75f, true, true);
            worker.dispose();
            
            // make sure we can read it 
            back = ImageIO.read(outFile);
            
            // we expect a RGB one
            icm =  (IndexColorModel) back.getColorModel();
            assertEquals(3, icm.getNumColorComponents());
            assertTrue(icm.getMapSize() <= 256);  
        } finally {
            is.close();
        }
    }
    
    @Test
    public void test4BitPNG() throws Exception {

        // create test image
        IndexColorModel icm =new IndexColorModel(
                        4, 
                        16, 
                        new byte[]{(byte)255,0,        0,        0,16,32,64,(byte)128,1,2,3,4,5,6,7,8}, 
                        new byte[]{0,        (byte)255,0,        0,16,32,64,(byte)128,1,2,3,4,5,6,7,8}, 
                        new byte[]{0,        0,        (byte)255,0,16,32,64,(byte)128,1,2,3,4,5,6,7,8});
        assertEquals(16, icm.getMapSize());
        
        // create random data
        WritableRaster data = com.sun.media.jai.codecimpl.util.RasterFactory.createWritableRaster(
                        icm.createCompatibleSampleModel(32,32), 
                        new Point(0,0));
        for(int x=data.getMinX();x<data.getMinX()+data.getWidth();x++){
                for(int y=data.getMinY();y<data.getMinY()+data.getHeight();y++){
                        data.setSample(x, y, 0, (x+y)%8);
                }
        }
        

        final BufferedImage bi = new BufferedImage(
                        icm,
                        data,
                        false,
                        null);
        assertEquals(16, ((IndexColorModel)bi.getColorModel()).getMapSize());
        assertEquals(4, bi.getSampleModel().getSampleSize(0));
        bi.setData(data);
        if(TestData.isInteractiveTest()){
                ImageIOUtilities.visualize(bi,"before");
        }
        
        // encode as png
        ImageWorker worker = new ImageWorker(bi);
        final File outFile = TestData.temp(this, "temp4.png");
        worker.writePNG(outFile, "FILTERED", 0.75f, true, false);
        worker.dispose();
        
        // make sure we can read it 
        BufferedImage back = ImageIO.read(outFile);
        
        // we expect an IndexColorMolde one matching the old one
        IndexColorModel ccm =  (IndexColorModel) back.getColorModel();
        assertEquals(3, ccm.getNumColorComponents());
        assertEquals(16, ccm.getMapSize());
        assertEquals(4, ccm.getPixelSize());
        if(TestData.isInteractiveTest()){
                ImageIOUtilities.visualize(back,"after");
        }
    }
    
    /**
     * Testing TIFF capabilities.
     *
     * @throws IOException If an error occured while writting the image.
     */
    @Test
    public void testTIFFWrite() throws IOException {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        // Get the image of the world with transparency.
        final ImageWorker worker = new ImageWorker(worldImage);
        show(worker, "Input file");

        // /////////////////////////////////////////////////////////////////////
        // tiff deflated untiled
        // /////////////////////////////////////////////////////////////////////
        final File outFile = TestData.temp(this, "temp.tiff");
        worker.writeTIFF(outFile, "Deflate", 0.75f, -1, -1);
        final ImageWorker readWorker = new ImageWorker(ImageIO.read(outFile));
        show(readWorker, "Tiff untiled");

        // /////////////////////////////////////////////////////////////////////
        // tiff deflated compressed tiled
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writeTIFF(outFile, "Deflate", 0.75f, 32, 32);
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "Tiff jpeg compressed, tiled");
        
        outFile.delete();
    }

    /**
     * Tests the conversion between RGB and indexed color model.
     */
    @Test
    public void testRGB2Palette(){
    	assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        final ImageWorker worker = new ImageWorker(worldImage);
        show(worker, "Input file");
        worker.forceIndexColorModelForGIF(true);

        // Convert to to index color bitmask
        ColorModel cm = worker.getRenderedImage().getColorModel();
        assertTrue("wrong color model", cm instanceof IndexColorModel);
        assertEquals("wrong transparency model", Transparency.BITMASK, cm.getTransparency());
        assertEquals("wrong transparency index", 255, ((IndexColorModel) cm).getTransparentPixel());
        show(worker, "Paletted bitmask");

        // Go back to rgb.
        worker.forceComponentColorModel();
        cm = worker.getRenderedImage().getColorModel();
        assertTrue("wrong color model", cm instanceof ComponentColorModel);
        assertEquals("wrong bands number", 4, cm.getNumComponents());

        show(worker, "RGB translucent");
        assertEquals("wrong transparency model", Transparency.TRANSLUCENT, cm.getTransparency());
        show(worker, "RGB translucent");
    }
    
    /**
     * Tests the {@link #rescaleToBytes()} operation.
     */
    @Test
    public void rescaleToBytes(){

        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        
    	// set up synthetic images for testing
    	final RenderedImage test1= ConstantDescriptor.create(128.0f, 128.0f, new Double[]{20000.0}, null);
    	final RenderedImage test2= ConstantDescriptor.create(128.0f, 128.0f, new Double[]{255.0}, null);
    	final RenderedImage test3= getSynthetic(20000);
    	final RenderedImage test4= getSynthetic(255);
    	
    	// starting to check the results
    	
    	// single band value exceed the byte upper bound and is constant
    	final ImageWorker test1I=new ImageWorker(test1).rescaleToBytes();
    	Assert.assertEquals("Format",test1I.getRenderedOperation().getOperationName());
    	final double[] maximums1 = test1I.getMaximums();
    	Assert.assertTrue(maximums1.length==1);
    	Assert.assertEquals(255.0,maximums1[0],1E-10);
    	final double[] minimums1 = test1I.getMinimums();
    	Assert.assertTrue(minimums1.length==1);
    	Assert.assertEquals(255.0,minimums1[0],1E-10);
    	
    	
    	// single band value does not exceed the byte upper bound and is constant
    	final ImageWorker test2I=new ImageWorker(test2).rescaleToBytes();
    	Assert.assertEquals("Format",test2I.getRenderedOperation().getOperationName());
    	final double[] maximums2 = test1I.getMaximums();
    	Assert.assertTrue(maximums2.length==1);
    	Assert.assertEquals(255.0,maximums2[0],1E-10);    	
    	final double[] minimums2 = test1I.getMinimums();
    	Assert.assertTrue(minimums2.length==1);
    	Assert.assertEquals(255.0,minimums2[0],1E-10);    	
    	
    	// single band value exceed the byte upper bound
    	ImageWorker test3I=new ImageWorker(test3);
    	final double[] maximums3a = test3I.getMaximums();
    	final double[] minimums3a = test3I.getMinimums();
    	test3I.rescaleToBytes();
    	Assert.assertEquals("Rescale",test3I.getRenderedOperation().getOperationName());
    	final double[] maximums3b = test3I.getMaximums();
    	final double[] minimums3b = test3I.getMinimums();

    	if(maximums3a[0]>255)
    	{
    		Assert.assertTrue(Math.abs(maximums3a[0]-maximums3b[0])>1E-10); 
    		Assert.assertTrue(Math.abs(255.0-maximums3b[0])>=0);
    	}
    	
    	if(minimums3a[0]<0)
    	{
    		Assert.assertTrue(minimums3b[0]>=0);
    	}
    	
    	// single band value does not exceed the byte upper bound
    	ImageWorker test4I=new ImageWorker(test4);
    	final double[] maximums4a = test4I.getMaximums();
    	final double[] minimums4a = test4I.getMinimums();
    	test4I.rescaleToBytes();
    	Assert.assertEquals("Format",test4I.getRenderedOperation().getOperationName());
    	final double[] maximums4b = test4I.getMaximums();
    	final double[] minimums4b = test4I.getMinimums();
    	Assert.assertEquals(maximums4a[0],maximums4b[0],1E-10);
    	Assert.assertEquals(minimums4a[0],minimums4b[0],1E-10);
    	
    	// now test multibands case
    	final RenderedImage multiband=BandMergeDescriptor.create(test2, test3, null);
    	ImageWorker testmultibandI=new ImageWorker(multiband);
    	final double[] maximums5a = testmultibandI.getMaximums();
    	final double[] minimums5a = testmultibandI.getMinimums();    
    	testmultibandI.rescaleToBytes();
    	final double[] maximums5b = testmultibandI.getMaximums();
    	final double[] minimums5b = testmultibandI.getMinimums();
    	Assert.assertEquals(maximums5a[0],maximums5b[0],1E-10);
    	Assert.assertEquals(minimums5a[0],minimums5b[0],1E-10);    

    	Assert.assertTrue(Math.abs(maximums5a[1]-maximums5b[1])>1E-10);
    	Assert.assertTrue(Math.abs(minimums5a[1]-minimums5b[1])>1E-10);    
    	
    	
    }
    
    /**
     * Tests the {@link ImageWorker#makeColorTransparent} methods.
     * Some trivial tests are performed before.
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws IllegalStateException 
     */
    @Test
    public void testMakeColorTransparent() throws IllegalStateException, FileNotFoundException, IOException {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(sstImage);
        
        assertSame(sstImage, worker.getRenderedImage());
        assertEquals(  1, worker.getNumBands());
        assertEquals( -1, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertFalse (     worker.isBinary());
        assertTrue  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());
        assertFalse (     worker.isColorSpaceGRAYScale());
        assertFalse (     worker.isTranslucent());

        assertSame("Expected no operation.", sstImage, worker.forceIndexColorModel(false).getRenderedImage());
        assertSame("Expected no operation.", sstImage, worker.forceIndexColorModel(true ).getRenderedImage());
        assertSame("Expected no operation.", sstImage, worker.forceColorSpaceRGB()       .getRenderedImage());
        assertSame("Expected no operation.", sstImage, worker.retainFirstBand()          .getRenderedImage());
        assertSame("Expected no operation.", sstImage, worker.retainLastBand()           .getRenderedImage());

        // Following will change image, so we need to test after the above assertions.
        assertEquals(  0, worker.getMinimums()[0], 0);
        assertEquals(255, worker.getMaximums()[0], 0);
        assertNotSame(sstImage, worker.getRenderedImage());
        assertSame("Expected same databuffer, i.e. pixels should not be duplicated.",
                   sstImage.getTile(0,0).getDataBuffer(),
                   worker.getRenderedImage().getTile(0,0).getDataBuffer());

        assertSame(worker, worker.makeColorTransparent(Color.WHITE));
        assertEquals(255,  worker.getTransparentPixel());
        assertFalse (      worker.isTranslucent());
        assertSame("Expected same databuffer, i.e. pixels should not be duplicated.",
                   sstImage.getTile(0,0).getDataBuffer(),
                   worker.getRenderedImage().getTile(0,0).getDataBuffer());
        

        // INDEX TO INDEX-ALPHA
        worker=new ImageWorker(chlImage).makeColorTransparent(Color.black);
        show(worker,  "CHL01195.png");
        assertEquals(  1, worker.getNumBands());
        assertEquals( 0, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertTrue  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());
        assertFalse (     worker.isColorSpaceGRAYScale());
        assertFalse (     worker.isTranslucent());        
        RenderedImage image= worker.getRenderedImage();
        assertTrue  (     image.getColorModel() instanceof IndexColorModel);
        IndexColorModel iColorModel=(IndexColorModel) image.getColorModel();
        int transparentColor=iColorModel.getRGB(worker.getTransparentPixel())&0x00ffffff;
        assertTrue  (     transparentColor==0);
        

        
        // INDEX TO INDEX-ALPHA
        worker=new ImageWorker(bathy).makeColorTransparent(Color.WHITE);
        show(worker,  "BATHY.png");
        assertEquals(  1, worker.getNumBands());
        assertEquals( 206, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertTrue  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());
        assertFalse (     worker.isColorSpaceGRAYScale());
        assertFalse (     worker.isTranslucent());        
        image= worker.getRenderedImage();
        assertTrue  (     image.getColorModel() instanceof IndexColorModel);
        iColorModel=(IndexColorModel) image.getColorModel();
        transparentColor=iColorModel.getRGB(worker.getTransparentPixel())&0x00ffffff;
        assertTrue  (     transparentColor==(Color.WHITE.getRGB()&0x00ffffff));        
        
        // RGB TO RGBA
        worker=new ImageWorker(smallWorld).makeColorTransparent(new Color(11,10,50));
        show(worker,  "small_world.png");
        assertEquals(  4, worker.getNumBands());
        assertEquals( -1, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertFalse  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());
        assertFalse (     worker.isColorSpaceGRAYScale());
        assertTrue (     worker.isTranslucent());        
        image= worker.getRenderedImage();
        assertTrue  (     image.getColorModel() instanceof ComponentColorModel);  
                
        
        // RGBA to RGBA
        worker=new ImageWorker(worldImage).makeColorTransparent(Color.white);
        show(worker,  "world.png");
        assertEquals(  4, worker.getNumBands());
        assertEquals( -1, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertFalse  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());
        assertFalse (     worker.isColorSpaceGRAYScale());
        assertTrue (     worker.isTranslucent());        
        image= worker.getRenderedImage();
        assertTrue  (     image.getColorModel() instanceof ComponentColorModel);  
        
        
        // GRAY TO GRAY-ALPHA
        worker=new ImageWorker(gray).makeColorTransparent(Color.black);
        show(worker,  "gray.png");
        assertEquals(  2, worker.getNumBands());
        assertEquals( -1, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertFalse  (     worker.isIndexed());
        assertFalse  (     worker.isColorSpaceRGB());
        assertTrue (     worker.isColorSpaceGRAYScale());
        assertTrue (     worker.isTranslucent());        
        image= worker.getRenderedImage();
        assertTrue  (     image.getColorModel() instanceof ComponentColorModel);  
        
        // GRAY-ALPHA TO GRAY-ALPHA.
        worker=new ImageWorker(grayAlpha).makeColorTransparent(Color.black);
        show(worker,  "gray-alpha.png");  
        assertEquals(  2, worker.getNumBands());
        assertEquals( -1, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertFalse  (     worker.isIndexed());
        assertFalse  (     worker.isColorSpaceRGB());
        assertTrue (     worker.isColorSpaceGRAYScale());
        assertTrue (     worker.isTranslucent());        
        image= worker.getRenderedImage();
        assertTrue  (     image.getColorModel() instanceof ComponentColorModel);         
        
        
    }
    
    /**
     * Tests the {@link ImageWorker#tile()} methods.
     * Some trivial tests are performed before.
     */
    @Test
    public void testReTile()  {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(worldImage);
        
        assertSame(worldImage, worker.getRenderedImage());
        assertEquals(  4, worker.getNumBands());
        assertEquals( -1, worker.getTransparentPixel());
        assertTrue  (     worker.isBytes());
        assertFalse (     worker.isBinary());
        assertFalse  (     worker.isIndexed());
        assertTrue  (     worker.isColorSpaceRGB());
        assertFalse (     worker.isColorSpaceGRAYScale());
        assertTrue (     worker.isTranslucent());

        assertSame("Expected no operation.", worldImage, worker.rescaleToBytes()           .getRenderedImage());
        assertSame("Expected no operation.", worldImage, worker.forceComponentColorModel().getRenderedImage());
        assertSame("Expected no operation.", worldImage, worker.forceColorSpaceRGB()       .getRenderedImage());
        assertSame("Expected no operation.", worldImage, worker.retainBands(4)             .getRenderedImage());

        // Following will change image, so we need to test after the above assertions.
        worker.setRenderingHint(JAI.KEY_IMAGE_LAYOUT, new ImageLayout().setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(64).setTileWidth(64));
        worker.tile();    
        assertSame("Expected 64.", 64, worker.getRenderedImage().getTileWidth());
        assertSame("Expected 64.", 64, worker.getRenderedImage().getTileHeight());
        
        
    }
    /**
     * Visualize the content of given image if {@link #SHOW} is {@code true}.
     *
     * @param worker The worker for which to visualize the image.
     * @param title  The title to be given to the windows.
     */
    private static void show(final ImageWorker worker, final String title) {
        if (SHOW) {
            Viewer.show(worker.getRenderedImage(), title);
        } else {
            assertNotNull(worker.getRenderedImage().getTile(worker.getRenderedImage().getMinTileX(), worker.getRenderedImage().getMinTileY())); // Force computation.
        }
    }
    
    @Test
    public void testOpacityAlphaRGBComponent() {
        testAlphaRGB(false);
    }
    
    @Test
    public void testOpacityAlphaRGBDirect() {
        testAlphaRGB(true);
    }
    
    @Test
    public void testYCbCr() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        // check the presence of the PYCC.pf file that contains the profile for the YCbCr color space
        if(ImageWorker.CS_PYCC==null){
            System.out.println("testYCbCr disabled since we are unable to locate the YCbCr color profile");
            return;
        }
        // RGB component color model
        ImageWorker worker = new ImageWorker(getSyntheticRGB(false));
        
        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(!image.getColorModel().hasAlpha());
        int sample = image.getTile(0, 0).getSample(0, 0, 2);
        assertEquals(0, sample);
        
        assertFalse(worker.isColorSpaceYCbCr());
        worker.forceColorSpaceYCbCr();
        assertTrue(worker.isColorSpaceYCbCr());
        worker.forceColorSpaceRGB();
        assertFalse(worker.isColorSpaceYCbCr());
        assertTrue(worker.isColorSpaceRGB());
        
        // RGB Palette
        worker.forceBitmaskIndexColorModel();
        image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof IndexColorModel);
        assertTrue(!image.getColorModel().hasAlpha());
        
        assertFalse(worker.isColorSpaceYCbCr());
        worker.forceColorSpaceYCbCr();
        assertTrue(worker.isColorSpaceYCbCr());   
        worker.forceColorSpaceRGB();
        assertFalse(worker.isColorSpaceYCbCr());
        assertTrue(worker.isColorSpaceRGB());     
        
        // RGB DirectColorModel
        worker = new ImageWorker(getSyntheticRGB(true));        
        image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof DirectColorModel);
        assertTrue(!image.getColorModel().hasAlpha());
        sample = image.getTile(0, 0).getSample(0, 0, 2);
        assertEquals(0, sample);
        
        assertFalse(worker.isColorSpaceYCbCr());
        worker.forceColorSpaceYCbCr();
        assertTrue(worker.isColorSpaceYCbCr()); 
        worker.forceColorSpaceRGB();
        assertFalse(worker.isColorSpaceYCbCr());
        assertTrue(worker.isColorSpaceRGB());       
        
    }
    
    private void testAlphaRGB(boolean direct) {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(getSyntheticRGB(direct));
        worker.applyOpacity(0.5f);
        
        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        int sample = image.getTile(0, 0).getSample(0, 0, 3);
        assertEquals(128, sample);
    }
    
    @Test
    public void testOpacityRGBA() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        assertTrue(worldImage.getColorModel().hasAlpha());
        assertTrue(worldImage.getColorModel() instanceof ComponentColorModel);
        ImageWorker worker = new ImageWorker(worldImage);
        worker.applyOpacity(0.5f);
        
        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        Raster tile = worldImage.getTile(0, 0);
        Raster outputTile = image.getTile(0, 0);
        for(int i = 0; i < tile.getWidth(); i++) {
            for(int j = 0; j < tile.getHeight(); j++) {
                int original = tile.getSample(i, j, 3);
                int result = outputTile.getSample(i, j, 3);
                assertEquals(Math.round(original * 0.5), result);
            }
        }
    }
    
    @Test
    public void testOpacityGray() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(gray);
        worker.applyOpacity(0.5f);
        
        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        int sample = image.getTile(0, 0).getSample(0, 0, 1);
        assertEquals(128, sample);
    }
    
    @Test
    public void testOpacityGrayAlpha() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(gray);
        worker.applyOpacity(0.5f);
        
        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        Raster tile = gray.getTile(0, 0);
        Raster outputTile = image.getTile(0, 0);
        for(int i = 0; i < tile.getWidth(); i++) {
            for(int j = 0; j < tile.getHeight(); j++) {
                int original = tile.getSample(i, j, 1);
                int result = outputTile.getSample(i, j, 1);
                assertEquals(Math.round(original * 0.5), result);
            }
        }
    }
    
    @Test
    public void testOpacityIndexed() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        assertFalse(worldDEMImage.getColorModel().hasAlpha());
        ImageWorker worker = new ImageWorker(worldDEMImage);
        worker.applyOpacity(0.5f);
        
        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof IndexColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        
        // check the resulting palette
        IndexColorModel index = (IndexColorModel) image.getColorModel();
        for (int i = 0; i < index.getMapSize(); i++) {
            assertEquals(128, index.getAlpha(i));
        }
    }
    
    @Test
    public void testOpacityIndexedTranslucent() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        assertFalse(worldDEMImage.getColorModel().hasAlpha());
        final BufferedImage input = getSyntheticTranslucentIndexed();
        ImageWorker worker = new ImageWorker(input);
        worker.applyOpacity(0.5f);
        
        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof IndexColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        
        // check the resulting palette
        IndexColorModel outputCM = (IndexColorModel) image.getColorModel();
        IndexColorModel inputCM = (IndexColorModel) input.getColorModel();
        for (int i = 0; i < inputCM.getMapSize(); i++) {
            assertEquals(Math.round(inputCM.getAlpha(i) * 0.5), outputCM.getAlpha(i));
        }
    }
    
    @Test
    public void testOptimizeAffine() throws Exception {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        ImageWorker iw = new ImageWorker(bi);
        
        // apply straight translation
        AffineTransform at = AffineTransform.getTranslateInstance(100, 100);
        iw.affine(at, null, null);
        RenderedImage t1 = iw.getRenderedImage();
        assertEquals(100, t1.getMinX());
        assertEquals(100, t1.getMinY());
        
        // now go back
        AffineTransform atInverse = AffineTransform.getTranslateInstance(-100, -100);
        iw.affine(atInverse, null, null);
        RenderedImage t2 = iw.getRenderedImage();
        assertEquals(0, t2.getMinX());
        assertEquals(0, t2.getMinY());
        assertSame(bi, t2);
        
    }
    
    
    
    @Test
    public void testOptimizedWarp() throws Exception {
        // do it again, make sure the image does not turn black since 
        GridCoverage2D ushortCoverage = EXAMPLES.get(5);
       GridCoverage2D coverage = project(ushortCoverage,CRS.parseWKT(GOOGLE_MERCATOR_WKT), null,"nearest", null, true);
       RenderedImage ri = coverage.getRenderedImage();
       
       
       ImageWorker.WARP_REDUCTION_ENABLED = false;
       AffineTransform at = new AffineTransform(0.4, 0, 0, 0.5, -200, -200);
       RenderedOp fullChain = (RenderedOp) new ImageWorker(ri).affine(at, Interpolation.getInstance(Interpolation.INTERP_NEAREST), new double[] {0}).getRenderedImage();
       assertEquals("Scale", fullChain.getOperationName());
       fullChain.getTiles();
       
       ImageWorker.WARP_REDUCTION_ENABLED = true;
       RenderedOp reduced = (RenderedOp) new ImageWorker(ri).affine(at, Interpolation.getInstance(Interpolation.INTERP_NEAREST), new double[] {0}).getRenderedImage();
       // force computation, to make sure it does not throw exceptions
       reduced.getTiles();
       // check the chain has been reduced
       assertEquals("Warp", reduced.getOperationName());
       assertEquals(1, reduced.getSources().size());
       assertSame(ushortCoverage.getRenderedImage(), reduced.getSourceImage(0));
    
       // check the bounds of the output image has not changed
       assertEquals(fullChain.getBounds(), reduced.getBounds());
    }
    
}
