/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.impl.range;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;
import javax.media.jai.IHSColorSpace;

import org.geotools.coverage.TypeMap;
import org.geotools.coverage.io.range.Axis;
import org.geotools.feature.NameImpl;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.SampleDimension;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.util.InternationalString;

/**
 * Implementation of {@link Axis} for multibands images.
 * 
 * <p>
 * This implementation of Axis can be seen as a stub implementation since in
 * this case we do not really have an {@link Axis} for this kind of data, or
 * rather we have an axis that just represents an ordinal or a certain set of .
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @todo add convenience constructor based on {@link SampleDimension} and or
 *       {@link SampleModel}
 */
@SuppressWarnings("deprecation")
public class DimensionlessAxis implements Axis<String,Dimensionless> {

    /**
     * Textual representation for the various bands in this {@link Axis}.
     */
    private String[] bandsKeys = null;

    private Name name = null;

    private InternationalString description;
    
    
    /**
     * Helper classes for creating {@link DimensionlessAxis} for the most common color models' bands.
     * 
     * <p>
     * Suypported colorspaces incluse RGBA, GRAY, GRAYA, HSV,HLS, LAB, LUV, IHS, CI_XYZ, CMY(K).
     * Notice that RGB is not handled here but through a wavelength axis.
     * 
     * <p>
     * This method returns null if an unsupported {@link ColorModel} is provided.
     * 
     * @param raster a {@link RenderedImage} implementation from which to extract needed info, usually {@link ColorModel} and {@link SampleModel}.
     * @return a {@link DimensionlessAxis} or null if an unsupported {@link ColorModel} is provided.
     */
	public static DimensionlessAxis createFromRenderedImage(final RenderedImage raster){
		if(raster==null)
			throw new IllegalArgumentException("Provided null input image");
		
		final ColorModel cm= raster.getColorModel();
		if(cm==null)
			throw new IllegalArgumentException("Provided input image with null color model");	
		final SampleModel sm= raster.getSampleModel();
		if(sm==null)
			throw new IllegalArgumentException("Provided input image with null SampleModel");			
		
		//get the color interpretation for the three bands
		final ColorInterpretation firstBandCI = TypeMap.getColorInterpretation(cm, 0);
		
		//		CMY - CMYK
		if(firstBandCI==ColorInterpretation.CYAN_BAND)
		{
			if(sm.getNumBands()==3)
				return new DimensionlessAxis(
						Arrays.asList("CYAN","MAGENTA","YELLOW"),
						new NameImpl("CMY-AXIS"),
						new SimpleInternationalString("Axis for CMY bands"));
			else
				return new DimensionlessAxis(
						Arrays.asList("CYAN","MAGENTA","YELLOW","BLACK"),
						new NameImpl("CMYK-AXIS"),
						new SimpleInternationalString("Axis for CMYK bands"));				
		}
		
		// HSV
		if(firstBandCI==ColorInterpretation.HUE_BAND)
		{
			return new DimensionlessAxis(
					Arrays.asList("HUE","SATURATION","VALUE"),
					new NameImpl("HSV-AXIS"),
					new SimpleInternationalString("Axis for HSV bands"));
		}
		
		//RGBA
		if(firstBandCI==ColorInterpretation.RED_BAND)
		{
			return new DimensionlessAxis(
					Arrays.asList("RED","GREEN","BLUE","ALPHA"),
					new NameImpl("RGBA-AXIS"),
					new SimpleInternationalString("Axis for RGBA bands"));
		}
		
		//PALETTE
		if(firstBandCI==ColorInterpretation.PALETTE_INDEX)
			return new DimensionlessAxis(
					Arrays.asList("PALETTE_INDEX"),
					new NameImpl("PALETTE_INDEX-AXIS"),
					new SimpleInternationalString("Axis for PALETTE INDEX bands"));			
		
		// GRAY, GRAY+ALPHA
		if(firstBandCI==ColorInterpretation.GRAY_INDEX)
		{
			if(sm.getNumBands()==2)
				return new DimensionlessAxis(
						Arrays.asList("GRAY","ALPHA"),
						new NameImpl("GA-AXIS"),
						new SimpleInternationalString("Axis for GRAY-ALPHA bands"));
			else
				return new DimensionlessAxis(
						Arrays.asList("GRAY"),
						new NameImpl("GRAY-AXIS"),
						new SimpleInternationalString("Axis for GRAY bands"));
				
		}
		
		
		final ColorSpace cs = cm.getColorSpace();
		//IHS
		if(cs instanceof IHSColorSpace)
			return new DimensionlessAxis(
					Arrays.asList("INTENSITY","HUE","SATURATION"),
					new NameImpl("IHS-AXIS"),
					new SimpleInternationalString("Axis for IHS bands"));

		//YCbCr, LUV, LAB, HLS, IEXYZ 
		switch(cs.getType()){
		case ColorSpace.TYPE_YCbCr:
			return new DimensionlessAxis(
					Arrays.asList("LUMA","CHROMA-A","CHROMA-B"),
					new NameImpl("YCbCr-AXIS"),
					new SimpleInternationalString("Axis for YCbCr bands"));		
		case ColorSpace.TYPE_Luv:
			return new DimensionlessAxis(
					Arrays.asList("LIGHTNESS","U","V"),
					new NameImpl("LUV-AXIS"),
					new SimpleInternationalString("Axis for LUV bands"));				
		case ColorSpace.TYPE_Lab:
			return new DimensionlessAxis(
					Arrays.asList("LIGHTNESS","A","B"),
					new NameImpl("LAB-AXIS"),
					new SimpleInternationalString("Axis for LAB bands"));				
		case ColorSpace.TYPE_HLS:
			return new DimensionlessAxis(
					Arrays.asList("HUE","LIGHTNESS","SATURATION"),
					new NameImpl("HLS-AXIS"),
					new SimpleInternationalString("Axis for HLS bands"));			
		case ColorSpace.CS_CIEXYZ:
			return new DimensionlessAxis(
					Arrays.asList("X","Y","Z"),
					new NameImpl("XYZ-AXIS"),
					new SimpleInternationalString("Axis for XYZ bands"));				
			
		default:
			return null;
		
			
		}
    }
    

    /**
     * 
     */
    public DimensionlessAxis(final int bandsNumber, final Name name,
            final InternationalString description) {
        String[] bandsKeys = new String[bandsNumber];
        for (int i = 0; i < bandsNumber; i++)
            bandsKeys[i] = Integer.toString(i);
        init(bandsKeys, name, description);
    }

    /**
     * 
     */
    public DimensionlessAxis(final String[] bands, final Name name,
            final InternationalString description) {
        init(bands, name, description);
    }

    /**
     * 
     */
    public DimensionlessAxis(final List<String> bandsKeys, final Name name,
            final InternationalString description) {
        if (bandsKeys==null || bandsKeys.isEmpty())
            throw new IllegalArgumentException("Specified band keys list is invalid");
        init((String[])bandsKeys.toArray(new String[bandsKeys.size()]), name, description);
    }

    private void init(String[] bandsKeys, final Name name,
            final InternationalString description) {
        this.name = name;
        this.description = description;
        this.bandsKeys = bandsKeys;
    }
    
    /**
     * @see org.geotools.coverage.io.range.Axis#getCoordinateReferenceSystem()
     */
    public SingleCRS getCoordinateReferenceSystem() {
        return null;
    }

    /**
     * @see org.geotools.coverage.io.range.Axis#getDescription()
     */
    public InternationalString getDescription() {
        return this.description;
    }

    /**
     * @see org.geotools.coverage.io.range.Axis#getKey(int)
     */
    public BandIndexMeasure getKey(int keyIndex) {
        return new BandIndexMeasure(keyIndex, this.bandsKeys[keyIndex]);
    }

    /**
     * @see org.geotools.coverage.io.range.Axis#getKeys()
     */
    public List<BandIndexMeasure> getKeys() {
        List<BandIndexMeasure> list = new ArrayList<BandIndexMeasure>(
                this.bandsKeys.length);
        int i = 0;
        for (String band : this.bandsKeys)
            list.add(new BandIndexMeasure(i++, band));
        return list;
    }

    /**
     * @see org.geotools.coverage.io.range.Axis#getName()
     */
    public Name getName() {
        return this.name;
    }

    /**
     * @see org.geotools.coverage.io.range.Axis#getNumKeys()
     */
    public int getNumKeys() {
        return bandsKeys.length;
    }

    /**
     * @see org.geotools.coverage.io.range.Axis#getUnitOfMeasure()
     */
    public Unit<Dimensionless> getUnitOfMeasure() {
        return Unit.ONE;
    }

}
