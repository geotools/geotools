package org.geotools.coverage.io.range.impl;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageTypeSpecifier;
import javax.media.jai.IHSColorSpace;

import org.geotools.coverage.TypeMap;
import org.geotools.coverage.io.range.RangeAxis;
import org.geotools.coverage.io.range.RangeAxis.DimensionlessAxis;
import org.geotools.coverage.io.range.RangeAxisBin.StringAxisBin;
import org.geotools.coverage.io.range.Band.BandKey;
import org.geotools.feature.NameImpl;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.ColorInterpretation;

/**
 * Process Color is a subtractive model used when working with pigment. This
 * model is often used when printing.
 * <p>
 * This is a normal Java 5 enum capturing the closed set of CMYK names. It is
 * used as a basis for the definition of an RangeAxis built around these constants.
 * <p>
 * Please understand that this is not the only possible subtractive color model
 * - a commercial alternative is the Pantone (tm)) colors.
 * 
 */
@SuppressWarnings("deprecation")
public enum IMAGE_BAND_UTILITIES{

	X {
		@Override
		public StringAxisBin getAxisBin() {
			return X_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},		
	Y {
		@Override
		public StringAxisBin getAxisBin() {
			return Y_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},		
	Z {
		@Override
		public StringAxisBin getAxisBin() {
			return Z_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},	
	A {
		@Override
		public StringAxisBin getAxisBin() {
			return A_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},		
	B {
		@Override
		public StringAxisBin getAxisBin() {
			return B_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},		
	LIGHTNESS {
		@Override
		public StringAxisBin getAxisBin() {
			return LIGHTNESS_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},
	U {
		@Override
		public StringAxisBin getAxisBin() {
			return U_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},		
	V {
		@Override
		public StringAxisBin getAxisBin() {
			return V_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},	
	LUMA {
		@Override
		public StringAxisBin getAxisBin() {
			return LUMA_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},
	CHROMA_A {
		@Override
		public StringAxisBin getAxisBin() {
			return CHROMA_A_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},		
	CHROMA_B {
		@Override
		public StringAxisBin getAxisBin() {
			return CHROMA_B_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},
	INTENSITY {
		@Override
		public StringAxisBin getAxisBin() {
			return INTENSITY_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},
	ALPHA {
		@Override
		public StringAxisBin getAxisBin() {
			return ALPHA_BIN;
		}
		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},		
	GRAY {
		@Override
		public StringAxisBin getAxisBin() {
			return GRAY_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},	
	UNDEFINED {
		@Override
		public StringAxisBin getAxisBin() {
			return UNDEFINED_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},	
	PALETTE {
		@Override
		public StringAxisBin getAxisBin() {
			return PALETTE_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},		
	BLUE {
		@Override
		public StringAxisBin getAxisBin() {
			return BLUE_BIN;
		}
		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},	
	GREEN {
		@Override
		public StringAxisBin getAxisBin() {
			return GREEN_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},	
	RED {
		@Override
		public StringAxisBin getAxisBin() {
			return RED_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},	
	VALUE {
		@Override
		public StringAxisBin getAxisBin() {
			return SATURATION_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},
	SATURATION {
		@Override
		public StringAxisBin getAxisBin() {
			return SATURATION_BIN;
		}
		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},
	HUE {
		@Override
		public StringAxisBin getAxisBin() {
			return HUE_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	},
	CYAN {
		@Override
		public StringAxisBin getAxisBin() {
			return CYAN_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	}, MAGENTA {
		@Override
		public StringAxisBin getAxisBin() {
			return MAGENTA_BIN;
		}

		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	}, YELLOW {
		@Override
		public StringAxisBin getAxisBin() {
			return 						
				YELLOW_BIN;
		}
		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	}, KEY {
		@Override
		public StringAxisBin getAxisBin() {
			return 					
			KEY_BIN;
		}
		@Override
		public BandKey getBandKeys() {
			return new BandKey(Arrays.asList(getAxisBin()));
		}
	};

	/**
	 * Specific axis for controlling bins that looks up photographic bands, like RGB, CMYK, HSV, etc...
	 */
	public static final RangeAxis PHOTOGRAPHIC_BANDS_AXIS= new DimensionlessAxis("SyntheticColorAxis" );
	
	
	private static final StringAxisBin KEY_BIN = new StringAxisBin(
						new NameImpl("BLACK"),
						new SimpleInternationalString("BLACK bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"BLACK");
	private static final StringAxisBin YELLOW_BIN = new StringAxisBin(
						new NameImpl("YELLOW"),
						new SimpleInternationalString("YELLOW bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"YELLOW");
	private static final StringAxisBin MAGENTA_BIN = new StringAxisBin(
						new NameImpl("MAGENTA"),
						new SimpleInternationalString("Magenta bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"MAGENTA");
	private static final StringAxisBin CYAN_BIN = new StringAxisBin(
						new NameImpl("CYAN"),
						new SimpleInternationalString("CYAN bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"CYAN");
	private static final StringAxisBin HUE_BIN = new StringAxisBin(
						new NameImpl("HUE"),
						new SimpleInternationalString("HUE bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"HUE");
	private static final StringAxisBin SATURATION_BIN = new StringAxisBin(
						new NameImpl("SATURATION"),
						new SimpleInternationalString("SATURATION bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"SATURATION");
	private static final StringAxisBin GREEN_BIN = new StringAxisBin(
						new NameImpl("RED"),
						new SimpleInternationalString("RED bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"RED");
	private static final StringAxisBin BLUE_BIN = new StringAxisBin(
						new NameImpl("BLUE"),
						new SimpleInternationalString("BLUE bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"BLUE");
	private static final StringAxisBin RED_BIN = GREEN_BIN;
	private static final StringAxisBin PALETTE_BIN = new StringAxisBin(
						new NameImpl("VALUE"),
						new SimpleInternationalString("VALUE bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"VALUE");
	private static final StringAxisBin UNDEFINED_BIN = new StringAxisBin(
						new NameImpl("UNDEFINED"),
						new SimpleInternationalString("UNDEFINED bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"UNDEFINED");
	private static final StringAxisBin GRAY_BIN = new StringAxisBin(
						new NameImpl("GRAY"),
						new SimpleInternationalString("GRAY bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"GRAY");
	private static final StringAxisBin ALPHA_BIN = new StringAxisBin(
						new NameImpl("ALPHA"),
						new SimpleInternationalString("ALPHA bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"ALPHA");
	private static final StringAxisBin INTENSITY_BIN = new StringAxisBin(
						new NameImpl("INTENSITY"),
						new SimpleInternationalString("INTENSITY bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"INTENSITY");
	private static final StringAxisBin CHROMA_B_BIN = new StringAxisBin(
						new NameImpl("CHROMA-B"),
						new SimpleInternationalString("CHROMA-B bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"CHROMA-B");
	private static final StringAxisBin CHROMA_A_BIN = new StringAxisBin(
						new NameImpl("CHROMA-A"),
						new SimpleInternationalString("CHROMA-A bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"CHROMA-A");
	private static final StringAxisBin LUMA_BIN = new StringAxisBin(
						new NameImpl("LUMA"),
						new SimpleInternationalString("LUMA bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"LUMA");
	private static final StringAxisBin V_BIN = new StringAxisBin(
						new NameImpl("V"),
						new SimpleInternationalString("V bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"V");
	private static final StringAxisBin U_BIN = new StringAxisBin(
						new NameImpl("U"),
						new SimpleInternationalString("U bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"U");
	private static final StringAxisBin LIGHTNESS_BIN = new StringAxisBin(
						new NameImpl("LIGHTNESS"),
						new SimpleInternationalString("LIGHTNESS bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"LIGHTNESS");
	private static final StringAxisBin B_BIN = new StringAxisBin(
						new NameImpl("B"),
						new SimpleInternationalString("B bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"B");
	private static final StringAxisBin A_BIN = new StringAxisBin(
						new NameImpl("A"),
						new SimpleInternationalString("A bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"A");
	private static final StringAxisBin Z_BIN = new StringAxisBin(
						new NameImpl("Z"),
						new SimpleInternationalString("Z bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"Z");
	private static final StringAxisBin Y_BIN = new StringAxisBin(
						new NameImpl("Y"),
						new SimpleInternationalString("Y bin"),
						PHOTOGRAPHIC_BANDS_AXIS,
						"Y");

	public abstract StringAxisBin getAxisBin();
	
	public abstract BandKey getBandKeys();
	
	public static List<StringAxisBin> getBinsFromRenderedImage(final ColorModel cm, final SampleModel sm){
		if(cm==null)
			throw new IllegalArgumentException("Provided input image with null color model");	
		if(sm==null)
			throw new IllegalArgumentException("Provided input image with null SampleModel");			
		
		//get the color interpretation for the three bands
		final ColorInterpretation firstBandCI = TypeMap.getColorInterpretation(cm, 0);
				
		//		CMY - CMYK
		if(firstBandCI==ColorInterpretation.CYAN_BAND)
		{
			if(sm.getNumBands()==3)
			{
				return 
					Arrays.asList(
							IMAGE_BAND_UTILITIES.CYAN.getAxisBin(),
							IMAGE_BAND_UTILITIES.MAGENTA.getAxisBin(),						
							IMAGE_BAND_UTILITIES.YELLOW.getAxisBin()
						);
			}							
			else
			{	
				return 
					Arrays.asList(
							IMAGE_BAND_UTILITIES.CYAN.getAxisBin(),
							IMAGE_BAND_UTILITIES.MAGENTA.getAxisBin(),						
							IMAGE_BAND_UTILITIES.YELLOW.getAxisBin(),
							IMAGE_BAND_UTILITIES.KEY.getAxisBin()
					);		
			}
		}
		
		// HSV
		if(firstBandCI==ColorInterpretation.HUE_BAND)
		{
			return Collections.singletonList(IMAGE_BAND_UTILITIES.HUE.getAxisBin());
		}
		
		//RGBA
		if(firstBandCI==ColorInterpretation.RED_BAND)
		{
			if(sm.getNumBands()==3)
			{
				return 
					Arrays.asList(
						IMAGE_BAND_UTILITIES.RED.getAxisBin(),
						IMAGE_BAND_UTILITIES.GREEN.getAxisBin(),
						IMAGE_BAND_UTILITIES.BLUE.getAxisBin()
					);
			}							
			else
			{	
				return 
				Arrays.asList(
					IMAGE_BAND_UTILITIES.RED.getAxisBin(),
					IMAGE_BAND_UTILITIES.GREEN.getAxisBin(),
					IMAGE_BAND_UTILITIES.BLUE.getAxisBin(),
					IMAGE_BAND_UTILITIES.ALPHA.getAxisBin()
				);				
			}
		}
		
		//PALETTE
		if(firstBandCI==ColorInterpretation.PALETTE_INDEX)
			return Collections.singletonList(IMAGE_BAND_UTILITIES.PALETTE.getAxisBin());		
		
		// GRAY, GRAY+ALPHA
		if(firstBandCI==ColorInterpretation.GRAY_INDEX)
		{
			if(sm.getNumBands()==2)
				return Arrays.asList(
						IMAGE_BAND_UTILITIES.GRAY.getAxisBin(),
						IMAGE_BAND_UTILITIES.ALPHA.getAxisBin()
				);	
			else
				return Arrays.asList(
						IMAGE_BAND_UTILITIES.GRAY.getAxisBin()
				);	
				
		}
		
		
		final ColorSpace cs = cm.getColorSpace();
		//IHS
		if(cs instanceof IHSColorSpace)
			return Arrays.asList(
					IMAGE_BAND_UTILITIES.INTENSITY.getAxisBin(),
					IMAGE_BAND_UTILITIES.HUE.getAxisBin(),
					IMAGE_BAND_UTILITIES.SATURATION.getAxisBin()
			);	
	
		//YCbCr, LUV, LAB, HLS, IEXYZ 
		switch(cs.getType()){
		case ColorSpace.TYPE_YCbCr:
			return Arrays.asList(
					IMAGE_BAND_UTILITIES.LUMA.getAxisBin(),
					IMAGE_BAND_UTILITIES.CHROMA_A.getAxisBin(),
					IMAGE_BAND_UTILITIES.CHROMA_B.getAxisBin()
			);		
		case ColorSpace.TYPE_Luv:
			return Arrays.asList(
					IMAGE_BAND_UTILITIES.LIGHTNESS.getAxisBin(),
					IMAGE_BAND_UTILITIES.U.getAxisBin(),
					IMAGE_BAND_UTILITIES.V.getAxisBin()
			);					
		case ColorSpace.TYPE_Lab:
			return Arrays.asList(
					IMAGE_BAND_UTILITIES.LIGHTNESS.getAxisBin(),
					IMAGE_BAND_UTILITIES.A.getAxisBin(),
					IMAGE_BAND_UTILITIES.B.getAxisBin()
			);					
		case ColorSpace.TYPE_HLS:
			return Arrays.asList(
					IMAGE_BAND_UTILITIES.HUE.getAxisBin(),
					IMAGE_BAND_UTILITIES.LIGHTNESS.getAxisBin(),
					IMAGE_BAND_UTILITIES.SATURATION.getAxisBin()
			);				
		case ColorSpace.CS_CIEXYZ:
			return Arrays.asList(
					IMAGE_BAND_UTILITIES.X.getAxisBin(),
					IMAGE_BAND_UTILITIES.Y.getAxisBin(),
					IMAGE_BAND_UTILITIES.Z.getAxisBin()
			);				
			
		default:
			return null;
		
			
		}
	}
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
	public static List<StringAxisBin> getBinsFromRenderedImage(final RenderedImage raster){
		if(raster==null)
			throw new NullPointerException("Provided null input image");
		final ColorModel cm= raster.getColorModel();
		if(cm==null)
			throw new IllegalArgumentException("Provided input image with null color model");	
		final SampleModel sm= raster.getSampleModel();
		return getBinsFromRenderedImage(cm, sm);
		
	}
	
	public static List<StringAxisBin> getBinsFromRenderedImage(final ImageTypeSpecifier it){
		if(it==null)
			throw new NullPointerException("Provided null input ImageTypeSpecifier");
		final ColorModel cm= it.getColorModel();
		if(cm==null)
			throw new IllegalArgumentException("Provided input image with null color model");	
		final SampleModel sm= it.getSampleModel();
		return getBinsFromRenderedImage(cm, sm);
		
	}
	
//	
//	public static RangeDescriptor<String,Dimensionless> getFieldTypeFromRenderedImage(final RenderedImage raster){
//		if(raster==null)
//			throw new NullPointerException("Provided null input image");
//		
//		final ColorModel cm= raster.getColorModel();
//		if(cm==null)
//			throw new IllegalArgumentException("Provided input image with null color model");	
//		final SampleModel sm= raster.getSampleModel();
//		if(sm==null)
//			throw new IllegalArgumentException("Provided input image with null SampleModel");			
//		
//		//get the color interpretation for the three bands
//		final ColorInterpretation firstBandCI = TypeMap.getColorInterpretation(cm, 0);
//		
//		// get axis for this fieldtype and prepare the map for the band type
//		final HashMap<BandKey, BandDescriptor> bands= new HashMap<BandKey<String,Dimensionless>, BandDescriptor>();
//		
//		
//		//		CMY - CMYK
//		if(firstBandCI==ColorInterpretation.CYAN_BAND)
//		{
//			if(sm.getNumBands()==3)
//			{
//				bands.put(IMAGE_BAND_UTILITIES.CYAN.getBandKey(), IMAGE_BAND_UTILITIES.CYAN.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.MAGENTA.getBandKey(), IMAGE_BAND_UTILITIES.MAGENTA.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.YELLOW.getBandKey(), IMAGE_BAND_UTILITIES.YELLOW.getBandType());
//			}							
//			else
//			{	
//				bands.put(IMAGE_BAND_UTILITIES.CYAN.getBandKey(), IMAGE_BAND_UTILITIES.CYAN.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.MAGENTA.getBandKey(), IMAGE_BAND_UTILITIES.MAGENTA.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.YELLOW.getBandKey(), IMAGE_BAND_UTILITIES.YELLOW.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.KEY.getBandKey(), IMAGE_BAND_UTILITIES.KEY.getBandType());
//	
//			}
//		}
//		
//		// HSV
//		if(firstBandCI==ColorInterpretation.HUE_BAND)
//		{
//			bands.put(IMAGE_BAND_UTILITIES.HUE.getBandKey(), IMAGE_BAND_UTILITIES.HUE.getBandType());	
//		}
//		
//		//RGB(A)
//		if(firstBandCI==ColorInterpretation.RED_BAND)
//		{
//			if(sm.getNumBands()==3)
//			{
//				bands.put(IMAGE_BAND_UTILITIES.RED.getBandKey(), IMAGE_BAND_UTILITIES.RED.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.GREEN.getBandKey(), IMAGE_BAND_UTILITIES.GREEN.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.BLUE.getBandKey(), IMAGE_BAND_UTILITIES.BLUE.getBandType());
//			}							
//			else
//			{	
//				bands.put(IMAGE_BAND_UTILITIES.RED.getBandKey(), IMAGE_BAND_UTILITIES.RED.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.GREEN.getBandKey(), IMAGE_BAND_UTILITIES.GREEN.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.BLUE.getBandKey(), IMAGE_BAND_UTILITIES.BLUE.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.ALPHA.getBandKey(), IMAGE_BAND_UTILITIES.ALPHA.getBandType());
//			
//			}	
//		}
//		
//		//PALETTE
//		if(firstBandCI==ColorInterpretation.PALETTE_INDEX)
//		{
//			bands.put(IMAGE_BAND_UTILITIES.PALETTE.getBandKey(), IMAGE_BAND_UTILITIES.PALETTE.getBandType());	
//		}
//		
//		// GRAY, GRAY+ALPHA
//		if(firstBandCI==ColorInterpretation.GRAY_INDEX)
//		{
//			if(sm.getNumBands()==2)
//			{
//
//				bands.put(IMAGE_BAND_UTILITIES.GRAY.getBandKey(), IMAGE_BAND_UTILITIES.GRAY.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.ALPHA.getBandKey(), IMAGE_BAND_UTILITIES.ALPHA.getBandType());
//			}
//			else
//				bands.put(IMAGE_BAND_UTILITIES.GRAY.getBandKey(), IMAGE_BAND_UTILITIES.GRAY.getBandType());	
//				
//		}
//		
//		
//		final ColorSpace cs = cm.getColorSpace();
//		//IHS
//		if(cs instanceof IHSColorSpace){
//			bands.put(IMAGE_BAND_UTILITIES.INTENSITY.getBandKey(), IMAGE_BAND_UTILITIES.INTENSITY.getBandType());
//			bands.put(IMAGE_BAND_UTILITIES.HUE.getBandKey(), IMAGE_BAND_UTILITIES.HUE.getBandType());
//			bands.put(IMAGE_BAND_UTILITIES.SATURATION.getBandKey(), IMAGE_BAND_UTILITIES.SATURATION.getBandType());
//		}
//	
//			if(bands.isEmpty()){
//			//YCbCr, LUV, LAB, HLS, IEXYZ 
//			switch(cs.getType()){
//			case ColorSpace.TYPE_YCbCr:
//				bands.put(IMAGE_BAND_UTILITIES.LUMA.getBandKey(), IMAGE_BAND_UTILITIES.LUMA.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.CHROMA_A.getBandKey(), IMAGE_BAND_UTILITIES.CHROMA_A.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.CHROMA_B.getBandKey(), IMAGE_BAND_UTILITIES.CHROMA_B.getBandType());
//				break;
//		
//			case ColorSpace.TYPE_Luv:
//				bands.put(IMAGE_BAND_UTILITIES.LIGHTNESS.getBandKey(), IMAGE_BAND_UTILITIES.LIGHTNESS.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.U.getBandKey(), IMAGE_BAND_UTILITIES.U.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.V.getBandKey(), IMAGE_BAND_UTILITIES.V.getBandType());
//				break;			
//					
//			case ColorSpace.TYPE_Lab:
//				bands.put(IMAGE_BAND_UTILITIES.LIGHTNESS.getBandKey(), IMAGE_BAND_UTILITIES.LIGHTNESS.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.A.getBandKey(), IMAGE_BAND_UTILITIES.A.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.B.getBandKey(), IMAGE_BAND_UTILITIES.B.getBandType());
//				break;			
//					
//			case ColorSpace.TYPE_HLS:
//				bands.put(IMAGE_BAND_UTILITIES.HUE.getBandKey(), IMAGE_BAND_UTILITIES.HUE.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.LIGHTNESS.getBandKey(), IMAGE_BAND_UTILITIES.LIGHTNESS.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.SATURATION.getBandKey(), IMAGE_BAND_UTILITIES.SATURATION.getBandType());
//				break;				
//			case ColorSpace.CS_CIEXYZ:
//				bands.put(IMAGE_BAND_UTILITIES.X.getBandKey(), IMAGE_BAND_UTILITIES.X.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.Y.getBandKey(), IMAGE_BAND_UTILITIES.Y.getBandType());
//				bands.put(IMAGE_BAND_UTILITIES.Z.getBandKey(), IMAGE_BAND_UTILITIES.Z.getBandType());
//				break;			
//				
//			default:
//				throw new IllegalArgumentException("Unable to create RangeDescriptor for this rendered image");
//			
//			}
//		}
//		//build the field type
//		return new RangeDescriptor<String, Dimensionless>(
//				new NameImpl("RenderedImageFieldType"),
//				new SimpleInternationalString("RangeDescriptor for rendered image"),
//				Unit.ONE,
//				band);
//	}
	
	private final static StringAxisBin X_BIN=new StringAxisBin(
			new NameImpl("X"),
			new SimpleInternationalString("X bin"),
			PHOTOGRAPHIC_BANDS_AXIS,
			"X");
}