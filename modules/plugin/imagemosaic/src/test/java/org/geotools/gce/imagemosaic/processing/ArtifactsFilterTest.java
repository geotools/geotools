package org.geotools.gce.imagemosaic.processing;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderedImageFactory;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptor;
import javax.media.jai.OperationRegistry;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.HistogramDescriptor;
import javax.media.jai.registry.RenderedRegistryMode;

import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArtifactsFilterTest extends Assert {

	@Before
	public void setup() throws IOException {
		OperationRegistry registry = JAI.getDefaultInstance().getOperationRegistry();
		try {
			final OperationDescriptor op = new ArtifactsFilterDescriptor();
			registry.registerDescriptor(op);
			final String descName = op.getName();
			final RenderedImageFactory rif = new ArtifactsFilterRIF();
			registry.registerFactory(RenderedRegistryMode.MODE_NAME, descName, "org.geotools.gce.imagemosaic.processing", rif);
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testArtifact() throws IOException{
		TIFFImageReaderSpi spi = new TIFFImageReaderSpi();
		File file = TestData.file(this, "filter.tif");
		ImageReader reader = null;
		FileImageInputStream fis = null;
		try {
			fis = new FileImageInputStream(file);
			reader = spi.createReaderInstance();
			reader.setInput(fis);
			RenderedImage image = reader.read(0);
			RenderedOp histogramOp = HistogramDescriptor.create(image, null, Integer.valueOf(1), Integer.valueOf(1), new int[]{256}, null, null, null);
			Histogram histogram = (Histogram) histogramOp.getProperty("histogram");
			int[][] bins = histogram.getBins();
		
			assertEquals(bins[0][0],4261);
			assertEquals(bins[1][0],4261);
			assertEquals(bins[2][0],4832);
			assertEquals(bins[0][20],127); // This bin will disappear in the Histogram of the filtered image
			assertEquals(bins[1][20],127); // This bin will disappear in the Histogram of the filtered image
			assertEquals(bins[2][20],127); // This bin will disappear in the Histogram of the filtered image
			assertEquals(bins[0][180],571);
			assertEquals(bins[0][200],5041);
			assertEquals(bins[2][200],5041);
			assertEquals(bins[1][255],5612);
			
			assertEquals(bins[0][0]+bins[1][0]+bins[2][0]+bins[0][20]+bins[1][20]+bins[2][20]+bins[0][180]+bins[0][200]+bins[2][200]+bins[1][255], 100*100*3);
			
			// Image filtering
			ROI roi = new ROIShape(new Rectangle(14, 11, 75, 75));
			double [] backgroundValues = new double[]{0.0d, 0.0d, 0.0d};
			RenderedImage filtered = ArtifactsFilterDescriptor.create(image, roi, backgroundValues, 30, 3, null);
			histogramOp = HistogramDescriptor.create(filtered, null, Integer.valueOf(1), Integer.valueOf(1), new int[]{256}, null, null, null);
			histogram = (Histogram) histogramOp.getProperty("histogram");
			
			bins = histogram.getBins();
			
			assertEquals(bins[0][0],4375);
			assertEquals(bins[1][0],4375);
			assertEquals(bins[2][0],4959);
			assertEquals(bins[0][180],584);
			assertEquals(bins[0][200],5041);
			assertEquals(bins[2][200],5041);
			assertEquals(bins[1][255],5625);
			
			assertEquals(bins[0][0]+bins[1][0]+bins[2][0]+bins[0][180]+bins[0][200]+bins[2][200]+bins[1][255], 100*100*3);
		} finally {
			if (fis != null){
				try {
					fis.close();
				} catch (Throwable t){
					
				}
			}
			if (reader != null){
				try {
					reader.dispose();
				} catch (Throwable t){
					
				}
			}
		}
	}
}
