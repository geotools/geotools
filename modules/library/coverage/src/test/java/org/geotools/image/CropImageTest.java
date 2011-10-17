package org.geotools.image;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.media.jai.operator.ExtremaDescriptor;
import javax.media.jai.operator.SubtractDescriptor;

import org.geotools.image.crop.GTCropDescriptor;
import org.junit.Assert;
import org.junit.Test;

import com.sun.media.jai.util.SunTileCache;

public class CropImageTest {

    @Test
    public void testCropImagePB() {
        BufferedImage source = buildSource();
        
        ParameterBlock pb = buildParameterBlock(source);
        
        RenderedOp cropped = JAI.create("crop", pb);
        RenderedOp gtCropped = JAI.create("GTCrop", pb);
        assertImageEquals(cropped, gtCropped);
    }

    @Test
    public void testTileCache() {
        TileCache tc = new SunTileCache();
        RenderingHints hints = new RenderingHints(JAI.KEY_TILE_CACHE, tc);
        
        BufferedImage source = buildSource();
        ParameterBlock pb = buildParameterBlock(source);
        
        RenderedOp gtCropped = JAI.create("GTCrop", pb, hints);
        gtCropped.getColorModel(); // force to compute the image
        assertSame(tc,  gtCropped.getRenderingHint(JAI.KEY_TILE_CACHE));
    }
    
    @Test
    public void testNullTileCache() {
        RenderingHints hints = new RenderingHints(JAI.KEY_TILE_CACHE, null);
        
        BufferedImage source = buildSource();
        ParameterBlock pb = buildParameterBlock(source);
        
        RenderedOp gtCropped = JAI.create("GTCrop", pb, hints);
        gtCropped.getColorModel(); // force to compute the image
        assertNull(gtCropped.getRenderingHint(JAI.KEY_TILE_CACHE));
    }
    
    @Test
    public void testNullTileCacheDescriptor() {
        RenderingHints hints = new RenderingHints(JAI.KEY_TILE_CACHE, null);
        
        BufferedImage source = buildSource();
        
        RenderedOp gtCropped = GTCropDescriptor.create(source, 10f, 10f, 20f, 20f, hints);
        gtCropped.getColorModel(); // force to compute the image
        assertNull(gtCropped.getRenderingHint(JAI.KEY_TILE_CACHE));
    }
    
    private BufferedImage buildSource() {
        BufferedImage source = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) source.getGraphics();
        g.setPaint(new GradientPaint(new Point(0, 0), Color.WHITE, new Point(100, 100), Color.BLACK));
        g.dispose();
        return source;
    }

    private void assertImageEquals(RenderedOp first, RenderedOp second) {
        RenderedOp difference = SubtractDescriptor.create(first, second, null);
        RenderedOp stats = ExtremaDescriptor.create(difference, null, 1, 1, false, 1, null);
        
        double[] minimum = (double[]) stats.getProperty("minimum");
        double[] maximum = (double[]) stats.getProperty("maximum");
        assertEquals(minimum[0], maximum[0], 0.0);
        assertEquals(minimum[1], maximum[1], 0.0);
        assertEquals(minimum[2], maximum[2], 0.0);
    }

    private ParameterBlock buildParameterBlock(BufferedImage source) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(source);
        pb.add((float) 10);
        pb.add((float) 50);
        pb.add((float) 20);
        pb.add((float) 20);
        return pb;
    }
}
