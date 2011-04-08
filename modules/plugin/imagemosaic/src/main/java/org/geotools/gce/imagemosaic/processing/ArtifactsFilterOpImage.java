/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.processing;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.Map;

import javax.media.jai.BorderExtender;
import javax.media.jai.BorderExtenderConstant;
import javax.media.jai.ImageLayout;
import javax.media.jai.PlanarImage;
import javax.media.jai.PointOpImage;
import javax.media.jai.ROI;
import javax.media.jai.RasterAccessor;
import javax.media.jai.RasterFormatTag;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.media.jai.operator.BandCombineDescriptor;

import com.sun.media.jai.util.ImageUtil;

/**
 * An Artifacts Filter operation.
 * 
 * Given an input image and a ROI, transform the pixels along the inner border of the ROI, if less than a
 * specified Luminance threshold value, to a mean of all sourrounding pixels within ROI, having 
 * Luminance greater than threshold. 
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
@SuppressWarnings("unchecked")
public final class ArtifactsFilterOpImage extends PointOpImage {

    class RoiAccessor{
        RandomIter iterator;
        ROI roi;
        PlanarImage image;
        int minX;
        int minY;
        int w;
        int h;
        /**
         * @param iterator
         * @param roiAccessor
         * @param image
         * @param minX
         * @param minY
         * @param w
         * @param h
         */
        public RoiAccessor(RandomIter iterator, ROI roi, PlanarImage image,
                int minX, int minY, int w, int h) {
            super();
            this.iterator = iterator;
            this.roi = roi;
            this.image = image;
            this.minX = minX;
            this.minY = minY;
            this.w = w;
            this.h = h;
        }
        
        public void dispose(){
            image.dispose();
        }
      
    }
    
    private final static double RGB_TO_GRAY_MATRIX [][]= {{ 0.114, 0.587, 0.299, 0 }};

    private final static byte FILL_VALUE = 1; 
    
    private RoiAccessor roiAccessor;
    
    private RoiAccessor thresholdRoiAccessor;
    
    private RoiAccessor zeroRoiAccessor;
    
    private RandomIter iter;
    
    private final double[] backgroundValues;

    private final int numBands;

    private final BorderExtender sourceExtender;

    private int filterSize;
    
    /**
     * Base constructor for a {@link PixelRestorationOpImage}
     * @param source the input {@link RenderedImage}
     * @param layout the optional {@link ImageLayout}
     * @param config 
     * @param sourceROI a {@link ROI} representing pixels to be restored.
     * @param backgroundValues the value of the background pixel values.
     */
    public ArtifactsFilterOpImage(
    		final RenderedImage source, 
    		final ImageLayout layout,
            final Map<?, ?> config, 
            final ROI sourceROI, 
            double[] backgroundValues,
            final int threshold,
            final int filterSize) {
        super(source, layout, config, true);
        
        RenderedImage inputRI = source;
        iter = RandomIterFactory.create(inputRI, null);
        final int tr= inputRI.getColorModel().getTransparency();
        // Set the band count.
        this.numBands = sampleModel.getNumBands();
        
        this.filterSize = filterSize;
        
        // Save the ROI array.
        ROI thresholdRoi = null;
        ROI zeroRoi = null;
        if (sourceROI != null) {
            RenderedImage image = inputRI;
            if (threshold != Integer.MAX_VALUE){
                if (numBands == 3) {
                    image = BandCombineDescriptor.create(image, RGB_TO_GRAY_MATRIX, null);
                } else {
                        //do we have transparency
                        //combination matrix
                        
                        final double fillValue = tr == Transparency.OPAQUE ? 1.0/numBands : 1.0/(numBands-1);
                        
                        final double[][] matrix = new double[1][numBands + 1];
                        for (int i=0; i < numBands; i++) {
                                matrix[0][i] = fillValue;
                        }
    
                        image = BandCombineDescriptor.create(image, matrix, null);
                }
                    thresholdRoi = new ROI(image, threshold);
                    thresholdRoi = thresholdRoi.intersect(sourceROI);
                    zeroRoi = new ROI(image, 1);
            }
        } 
        
        // Copy the background values per the specification.
        this.backgroundValues = new double[numBands];
        if (backgroundValues == null) {
            backgroundValues = new double[] { 0.0 };
        }

        if (backgroundValues.length < numBands) {
            Arrays.fill(this.backgroundValues, backgroundValues[0]);
        } else {
            System.arraycopy(backgroundValues, 0, this.backgroundValues, 0, numBands);
        }

        final int dataType = sampleModel.getDataType();

        // Determine constant value for source border extension.
        double sourceExtensionConstant;
        switch (dataType) {
        case DataBuffer.TYPE_BYTE:
            sourceExtensionConstant = 0.0;
            break;
        case DataBuffer.TYPE_USHORT:
            sourceExtensionConstant = 0.0;
            break;
        case DataBuffer.TYPE_SHORT:
            sourceExtensionConstant = Short.MIN_VALUE;
            break;
        case DataBuffer.TYPE_INT:
            sourceExtensionConstant = Integer.MIN_VALUE;
            break;
        case DataBuffer.TYPE_FLOAT:
            sourceExtensionConstant = -Float.MAX_VALUE;
            break;
        case DataBuffer.TYPE_DOUBLE:
        default:
            sourceExtensionConstant = -Double.MAX_VALUE;
        }
        this.sourceExtender = sourceExtensionConstant == 0.0 ? BorderExtender
                .createInstance(BorderExtender.BORDER_ZERO)
                : new BorderExtenderConstant(new double[] { sourceExtensionConstant });

        if (sourceROI != null) {
            final PlanarImage roiImage = sourceROI.getAsImage();
            final RandomIter roiIter = RandomIterFactory.create(roiImage, null);
            final int minRoiX = roiImage.getMinX();
            final int minRoiY = roiImage.getMinY();
            final int roiW = roiImage.getWidth();
            final int roiH = roiImage.getHeight();
            roiAccessor = new RoiAccessor(roiIter, sourceROI, roiImage, minRoiX, minRoiY, roiW, roiH);
        }

        if (thresholdRoi != null){
            final PlanarImage thresholdRoiImage = thresholdRoi.getAsImage();
            final RandomIter thresholdRoiIter = RandomIterFactory.create(thresholdRoiImage, null);
            final int minRoiThX = thresholdRoiImage.getMinX();
            final int minRoiThY= thresholdRoiImage.getMinY();
            final int roiThW = thresholdRoiImage.getWidth();
            final int roiThH = thresholdRoiImage.getHeight();
            thresholdRoiAccessor = new RoiAccessor(thresholdRoiIter, thresholdRoi, thresholdRoiImage, minRoiThX, minRoiThY, roiThW, roiThH);
        }
        
        if (zeroRoi != null){
            final PlanarImage zeroRoiImage = zeroRoi.getAsImage();
            final RandomIter zeroRoiIter = RandomIterFactory.create(zeroRoiImage, null);
            final int minX = zeroRoiImage.getMinX();
            final int minY= zeroRoiImage.getMinY();
            final int w = zeroRoiImage.getWidth();
            final int h = zeroRoiImage.getHeight();
            zeroRoiAccessor = new RoiAccessor(zeroRoiIter, zeroRoi, zeroRoiImage, minX, minY, w, h);            
        }
    }

    @Override
    public Raster computeTile(final int tileX, final int tileY) {
        // Create a new Raster.
        final WritableRaster dest = createWritableRaster(sampleModel, new Point(
                tileXToX(tileX), tileYToY(tileY)));

        // Determine the active area; tile intersects with image's bounds.
        final Rectangle destRect = getTileRect(tileX, tileY);

        final int numSources = getNumSources();

        Raster rasterSources = null;

        // Cobble areas
        for (int i = 0; i < numSources; i++) {
            final PlanarImage source = getSourceImage(i);
            final Rectangle srcRect = mapDestRect(destRect, i);

            // If srcRect is empty, set the Raster for this source to
            // null; otherwise pass srcRect to getData(). If srcRect
            // is null, getData() will return a Raster containing the
            // data of the entire source image.
            rasterSources = srcRect != null && srcRect.isEmpty() ? null
                    : source.getExtendedData(destRect, sourceExtender);

        }

        computeRect(rasterSources, dest, destRect);

        final Raster sourceData = rasterSources;
        if (sourceData != null) {
            final PlanarImage source = getSourceImage(0);

            // Recycle the source tile
            if (source.overlapsMultipleTiles(sourceData.getBounds())) {
                recycleTile(sourceData);
            }
        }

        return dest;
    }


    private void computeRect(final Raster sources, final WritableRaster destinationRaster, final Rectangle destRect) {
        // Clear the background and return if no sources.
        if (sources == null) {
            ImageUtil.fillBackground(destinationRaster, destRect, backgroundValues);
            return;
        }

        // Determine the format tag id.
        final SampleModel[] sourceSM = new SampleModel[] { sources.getSampleModel() };
        final int formatTagID = RasterAccessor.findCompatibleTag(sourceSM, destinationRaster.getSampleModel());

        // Create dest accessor.
        final RasterAccessor rasterAccessor = new RasterAccessor(destinationRaster, destRect,
                new RasterFormatTag(destinationRaster.getSampleModel(), formatTagID), null);

        final int dataType = rasterAccessor.getDataType();
        // Branch to data type-specific method.
        switch (dataType) {
        case DataBuffer.TYPE_BYTE:
            computeRect(rasterAccessor);
            break;
        default:
            throw new UnsupportedOperationException("The following datatype isn't actually supported " + dataType);
        }

        rasterAccessor.copyDataToRaster();
    }

    /**
     * Compute operation for the provided dest.
     * @param dest
     */
    private void computeRect(RasterAccessor dest) {
        int dwidth = dest.getWidth();
        int dheight = dest.getHeight();
        int dnumBands = dest.getNumBands();

        byte dstDataArrays[][] = dest.getByteDataArrays();
        int dstBandOffsets[] = dest.getBandOffsets();
        int dstPixelStride = dest.getPixelStride();
        int dstScanlineStride = dest.getScanlineStride();

        final int x = dest.getX();
        final int y = dest.getY();

        int valuess[][] = new int[filterSize*filterSize][dnumBands];
        int min = -(filterSize/2);
        int max = filterSize/2;
        int dstPixelOffset[] = new int[dnumBands];
        int dstScanlineOffset[] = new int[dnumBands];
        int val[]= new int[dnumBands];
        int valueCount = 0;
        for (int k = 0; k < dnumBands; k++) {
            dstScanlineOffset[k] = dstBandOffsets[k];
        }
        for (int j = 0; j < dheight; j++) {
            for (int k = 0; k < dnumBands; k++) {
                dstPixelOffset[k] = dstScanlineOffset[k];
            }

            for (int i = 0; i < dwidth; i++) {
                valueCount = 0;
                for (int k = 0;k<dnumBands;k++){
                    val[k] = Integer.MIN_VALUE;
                }
                
                // //
                //
                // Pixels outside the ROI will be forced to background color
                //
                // //
                boolean outsideRoi = !contains(roiAccessor, x+i, y+j);
                
                if (!outsideRoi){
                    
                    // // 
                    //
                    // Artifact filtering is applied only on ROI border 
                    //
                    // //
                    boolean isBorder = isBorder(roiAccessor, x+i, y+j);
                    if (isBorder) {
                        
                        // //
                        //
                        // If the actual pixel luminance is less then the threshold value,
                        // filter it
                        //
                        // //
                        if(!contains(thresholdRoiAccessor, x+i, y+j)){
                            
                            // //
                            //
                            // Checking sourrounding pixels
                            //
                            // //
                            for (int u = min; u <= max; u++) {
                                for (int v = min; v <= max; v++) {
                                    boolean set = false;
                                    
                                    // //
                                    // 
                                    // Neighbour pixel is in ROI and its luminance value is greater than
                                    // the threshold. Then use it for computation
                                    // 
                                    // //
                                    if (/*contains(roiAccessor, x+i+v, y+j+u) && */contains(thresholdRoiAccessor, x+i+v, y+j+u)){
                                        set = true;
                                    }
                                    if (set){
                                        iter.getPixel(x+i+v, y+j+u, valuess[valueCount++]);
                                    }
                                    
                                }
                            }
                            
                            if (valueCount == 0){
                                //Last attempt to get more valid pixels by looking at the 
                                for (int u = min - 1; u <= max + 1; u += (filterSize+1) ) {
                                    for (int v = min-1 ; v <= max +1; v += (filterSize+1)) {
                                        boolean set = false;
                                        if (/*contains(roiAccessor, x+i+v, y+j+u) && */contains(thresholdRoiAccessor, x+i+v, y+j+u)){
                                            set = true;
                                        }
                                        if (set){
                                            iter.getPixel(x+i+v, y+j+u, valuess[valueCount++]);
                                        }
                                    }
                                }
                            }
                            // //
                            //
                            // Compute filter value from sourrounding pixel values
                            // 
                            // //
                            if (valueCount > 0) {
                                computeValueAtOnce(valuess, valueCount, val);
                            }
                        }
                    } else {
                        // //
                        //
                        // Pixel within ROI is all zero. Force to almost black value
                        // 
                        // //
                        if (!contains(zeroRoiAccessor,x+i, y+j)){
                            for (int k = 0; k < dnumBands; k++){
                                val[k] = FILL_VALUE;
                            }
                        }
                    }
                    int zeros=0;
                    for (int k = 0; k < dnumBands; k++){
                        // //
                        //
                        // In case some pixels haven't been computed, provide them a proper value
                        //
                        // //
                        if (val[k] == Integer.MIN_VALUE) {
                            val[k] = (int) iter.getSample(x+i, y+j, k) & 0xff;
                        }
                        if (val[k] == 0){
                            zeros++;
                        }
                    }
                    if (zeros == dnumBands){
                        for (int k = 0; k < dnumBands; k++){
                            val[k] = FILL_VALUE;
                        }
                    }
                    for (int k = 0; k < dnumBands; k++){
                        dstDataArrays[k][dstPixelOffset[k]] = (byte) val[k];
                    }    
                }
                for (int k = 0; k < dnumBands; k++){
                    dstPixelOffset[k] += dstPixelStride;
                }    
            }
            for (int k = 0; k < dnumBands; k++){
                dstScanlineOffset[k] += dstScanlineStride;
            }
        }
    }
    
    private void computeValueAtOnce(int[][] values, int valueCount, int[] val) {
        for (int k = 0; k < this.numBands; k++){
            val[k] = computeValueBands(values, valueCount, k);
        }
    }
    
    private int computeValueBands(int[][] data, int valueCount, int band) {
        int left=0;
        int right=valueCount-1;
        int target = valueCount/2;
 
        while (true) {
            int oleft = left;
            int oright = right;
            int mid = data[(left+right)/2][band];
            do {
              while(data[left][band]<mid) {
                left++;
              }
              while (mid<data[right][band]) {
                right--;
              }
              if (left<=right) {
                int tmp=data[left][band];
                data[left][band]=data[right][band];
                data[right][band]=tmp;
                left++;
                right--;
              }
            } while (left<=right);
            if (oleft<right && right >= target) {
                left = oleft;
            } else if (left<oright && left <= target) {
                right = oright;
            } else {
                return data[target][band];
            }
        }
    }
    
//    private byte computeValue2(int[] sourceValue, int valueCount) {
//        if (valueCount  == 1){
//            if (sourceValue[0] >= threshold){
//                return (byte) (sourceValue[0]/2);
//            }
//            return (byte) threshold;
//        } else {
//            int values = 0;
//            int sum = 0;
//            for (int i=0;i<valueCount;i++){
//                if (sourceValue[i] >= threshold){
//                    sum += sourceValue[i];
//                    values++;
//                }
//            }
//            return (byte)(values > 0? (sum/values): 0);
//        }
//    }
//    private static boolean isBorder1(RoiAccessor roi, int dstX, int dstY) {
//        int leftX = dstX - 1;
//        int lowerY = dstY - 1;
//        int rightX = dstX + 1;
//        int upperY = dstY + 1;
//        
//        if (!contains(roi, leftX, lowerY))
//            return true;
//        if (!contains(roi, leftX, dstY))
//            return true;
//        if (!contains(roi, leftX, upperY))
//            return true;
//        if (!contains(roi, dstX, lowerY))
//            return true;
//        if (!contains(roi, dstX, upperY))
//            return true;
//        if (!contains(roi, rightX, lowerY))
//            return true;
//        if (!contains(roi, rightX, dstY))
//            return true;
//        if (!contains(roi, rightX, upperY))
//            return true;
//        return false;
//    }
    
    private static boolean isBorder(RoiAccessor roi, int dstX, int dstY) {
        int leftX = dstX - 1;
        int lowerY = dstY - 1;
        int rightX = dstX + 1;
        int upperY = dstY + 1;
        
        if (!contains(roi, leftX, lowerY))
            return true;
        if (!contains(roi, leftX, dstY))
            return true;
        if (!contains(roi, leftX, upperY))
            return true;
        if (!contains(roi, dstX, lowerY))
            return true;
        if (!contains(roi, dstX, upperY))
            return true;
        if (!contains(roi, rightX, lowerY))
            return true;
        if (!contains(roi, rightX, dstY))
            return true;
        if (!contains(roi, rightX, upperY))
            return true;
        
        int leftX2 = dstX - 2;
        int lowerY2 = dstY - 2;
        int rightX2 = dstX + 2;
        int upperY2 = dstY + 2;
        
        if (!contains(roi, rightX2, dstY))
            return true;
        if (!contains(roi, leftX2, dstY))
            return true;
        if (!contains(roi, dstX, upperY2))
            return true;
        if (!contains(roi, dstX, lowerY2))
            return true;
        
        if (!contains(roi, rightX, lowerY2))
            return true;
        if (!contains(roi, leftX, lowerY2))
            return true;
        if (!contains(roi, rightX, upperY2))
            return true;
        if (!contains(roi, leftX, upperY2))
            return true;
        
        if (!contains(roi, rightX2, upperY))
            return true;
        if (!contains(roi, leftX2, upperY))
            return true;
        
        if (!contains(roi, rightX2, lowerY))
            return true;
        if (!contains(roi, leftX2, lowerY))
            return true;
        
        return false;
    }

    /**
     * 
     * @param roiAccessor
     * @param x
     * @param y
     * @return
     */
    private final static boolean contains(RoiAccessor roiAccessor, int x, int y) {
        return (x >= roiAccessor.minX && x < roiAccessor.minX + roiAccessor.w)
                && (y >= roiAccessor.minY && y < roiAccessor.minY + roiAccessor.h)
                && (roiAccessor.iterator.getSample(x, y, 0) >= 1);
    }

    /**
     * 
     */
    public void dispose(){
        super.dispose();
        if (roiAccessor != null){
            roiAccessor.dispose();
        }
        
        if (thresholdRoiAccessor != null){
            thresholdRoiAccessor.dispose();
        }

        if (zeroRoiAccessor != null){
            zeroRoiAccessor.dispose();
        }

    }
}
