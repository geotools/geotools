/**
 * 
 */
package org.geotools.coverage.io.geotiff;

import static org.geotools.coverage.io.driver.BaseFileDriver.URL;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.media.jai.IHSColorSpace;

import org.apache.commons.collections.map.ListOrderedMap;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageStore;
import org.geotools.coverage.io.driver.BaseFileDriver;
import org.geotools.coverage.io.driver.Driver;
import org.geotools.coverage.io.impl.BaseCoverageAccess;
import org.geotools.coverage.io.impl.DefaultCoverageAccess;
import org.geotools.coverage.io.impl.range.DefaultFieldType;
import org.geotools.coverage.io.impl.range.DefaultRangeType;
import org.geotools.coverage.io.impl.range.DimensionlessAxis;
import org.geotools.coverage.io.impl.range.HSV;
import org.geotools.coverage.io.impl.range.WavelengthAxis;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.coverage.io.range.Axis;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.Parameter;
import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.factory.Hints;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.feature.type.Name;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * Access to a file (or URL) in the GeoTiff Format.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * @author Jody Garnett
 */
@SuppressWarnings("deprecation")
public class GeoTiffAccess extends DefaultCoverageAccess implements CoverageAccess {

	/**
	 * Recognise a DEM and produce a RangeType.
	 */
	static class DEMPolicy extends RangePolicy {
		@Override
		public RangeType describe(GridCoverage2D coverage) {
			final GridSampleDimension[] sampleDimensions = coverage.getSampleDimensions();
			HashSet<SampleDimension> samples = new HashSet<SampleDimension>(Arrays.asList(sampleDimensions));
			
			SampleDimension sample = sampleDimensions[0];
			
			Name name = new NameImpl("DEM");
			InternationalString description = sample.getDescription();
			Unit<?> unit = Unit.ONE;
			final List<Axis<?,?>> axes = new ArrayList<Axis<?,?>>();
			axes.add( HSV.INTENSITY_AXIS );
			FieldType field = new DefaultFieldType( name, description, unit, axes, samples);			
			
			Set<FieldType> fields = Collections.singleton( field );
			DefaultRangeType range = new DefaultRangeType( name, description, fields );
			
			return range;
		}

		@Override
		public boolean match(GridCoverage2D coverage) {
			final GridSampleDimension[] sampleDimensions = coverage.getSampleDimensions();
			if( sampleDimensions.length != 1){
				return false;
			}
			final Unit<?> UoM=sampleDimensions[0].getUnits();
			if( sampleDimensions.length == 1 && UoM!=null&& UoM.equals(SI.METER))
				return true;
			return false;
		}
	}

	static class GenericPhotometricPolicy extends RangePolicy {
		@Override
		public RangeType describe(GridCoverage2D coverage) {
			final GridSampleDimension[] sampleDimensions = coverage.getSampleDimensions();
			final HashSet<SampleDimension> samples = new HashSet<SampleDimension>(Arrays.asList(sampleDimensions));
			final DimensionlessAxis axis=DimensionlessAxis.createFromRenderedImage(coverage.getRenderedImage());
			final List<Axis<?,?>> axes= new ArrayList<Axis<?,?>>();
			axes.add(axis);
			final FieldType field = new DefaultFieldType( new NameImpl("photometric-FieldType"), new SimpleInternationalString("Photometric image field"), Dimensionless.UNIT,axes, samples);
			final DefaultRangeType range = new DefaultRangeType(  new NameImpl("photometric-RangeType"),  new SimpleInternationalString("Photometric range field"), Collections.singleton(field) );	
			return range;
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean match(GridCoverage2D coverage) {
			final GridSampleDimension[] sampleDimensions = coverage.getSampleDimensions();
			final RenderedImage raster=coverage.getRenderedImage();
			if(raster==null)
				return false;
			final ColorModel cm= raster.getColorModel();
			if(cm==null)
				return false;				
			//get the color interpretation for the three bands
			final ColorInterpretation firstBandCI = TypeMap.getColorInterpretation(cm, 0);
			
			//		CMY - CMYK
			if(firstBandCI==ColorInterpretation.CYAN_BAND)
				return true;
			
			// HSV
			if(firstBandCI==ColorInterpretation.HUE_BAND)
				return true;
			
			//RGBA
			if(firstBandCI==ColorInterpretation.RED_BAND)
				return sampleDimensions.length==4;//RGBA
			
			//PALETTE
			if(firstBandCI==ColorInterpretation.PALETTE_INDEX)
				return true;			
			
			// GRAY, GRAY+ALPHA
			if(firstBandCI==ColorInterpretation.GRAY_INDEX&&sampleDimensions.length<=2)
			{
				if(sampleDimensions.length==2&&TypeMap.getColorInterpretation(cm, 1)==ColorInterpretation.ALPHA_BAND)
					return true;
				//gray if we do not have a UoM
				final Unit<?> uom=sampleDimensions[0].getUnits();
				if(uom==null|| uom.equals(Unit.ONE))
					return true;
				return false;
					
			}
			
			
			final ColorSpace cs = cm.getColorSpace();
			//IHS
			if(cs instanceof IHSColorSpace)
				return true;
			//YCbCr, LUV, LAB, HLS, IEXYZ 
			switch(cs.getType()){
			case ColorSpace.TYPE_YCbCr:case ColorSpace.TYPE_Luv:case ColorSpace.TYPE_Lab:case ColorSpace.TYPE_HLS:case ColorSpace.CS_CIEXYZ:
				return true;
			default:
				return false;
			
				
			}

		}
	}
	
	/**
	 * We are keeping one Info class for each coverage; this
	 * way we have an object to hold onto things we care about:
	 * <ul>
	 * <li>GridGeometry2D
	 * <li>GeneralEnvelope
	 * <li>assorted metadata stuff mentioned by ResourceInfo
	 * </ul> 
	 * @author Jody
	 */
	public class Info implements ResourceInfo {
		private GridGeometry2D geometry;
		private GeneralEnvelope extent;
		private final Name name;
		private String title;
		private URI dataProduct;
		
		public Info( Name name ){
			this.name = name;
		}
		/**
		 * We may need to improve ReferencedEnvelope to handle more
		 * cases.
		 */
		public ReferencedEnvelope getBounds() {
			return new ReferencedEnvelope(extent);
		}		
		public CoordinateReferenceSystem getCRS() {
			return extent.getCoordinateReferenceSystem();
		}
		public URI getDataProduct() {
			return dataProduct;
		}
		public String getDescription() {
			return null;
		}
		public GeneralEnvelope getExtent() {
			return extent;
		}
		public GridGeometry2D getGeometry() {
			return geometry;
		}
		public Set<String> getKeywords() {
			return null;
		}
		public String getName() {
			return name.getLocalPart();
		}		
		public Name getKey(){
			return name;
		}
		/**
		 * This should be some indication of the data product being provided.
		 */
		public URI getSchema() {
			return dataProduct;
		}
		public String getTitle() {
			return title;
		}
		public void setDataProduct(URI dataProduct) {
			this.dataProduct = dataProduct;
		}
		public void setExtent(GeneralEnvelope extent) {
			this.extent = extent;
		}
		public void setGeometry(GridGeometry2D geometry) {
			this.geometry = geometry;
		}
		public void setTitle(String title) {
			this.title = title;
		}
	}
	static abstract class RangePolicy {
		/**
		 * Describe the provided GridCoverage.
		 * 
		 * @param coverage
		 * @return RangeType describing available data as a series of FieldType
		 */
		public abstract RangeType describe(GridCoverage2D coverage);

		/**
		 * Right now the init method asks the reader to produce a GridCoverage;
		 * this method should switch to a ligther weight solution in the future
		 * when metadata entries are available to check.
		 * <p>
		 * For now we usually check the SampleDimensions (as the best
		 * description available).
		 * 
		 * @param coverage
		 * @return
		 */
		public abstract boolean match(GridCoverage2D coverage);
	}

	/** Closure called with read access */
	public static abstract class Read<T> {
		public abstract T run(GeoTiffReader reader, GeoTiffAccess access)
				throws IOException;
	}

	/**
	 * Recognise an RGB raster and produce a RangeType.
	 */
	static class RGBPolicy extends RangePolicy {
		@Override
		public RangeType describe(GridCoverage2D coverage) {
			final GridSampleDimension[] sampleDimensions = coverage.getSampleDimensions();
			final HashSet<SampleDimension> samples = new HashSet<SampleDimension>(Arrays.asList(sampleDimensions));
			final WavelengthAxis<Double> axis=WavelengthAxis.RGB;
			final List<Axis<?,?>> axes= new ArrayList<Axis<?,?>>();
			axes.add(axis);
			final FieldType field = new DefaultFieldType( new NameImpl("RGB-FiledType"), new SimpleInternationalString("RGB image field"), Dimensionless.UNIT,axes, samples);
			final DefaultRangeType range = new DefaultRangeType(  new NameImpl("RGB-RangeType"),  new SimpleInternationalString("RGB range field"), Collections.singleton(field) );	
			return range;
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean match(GridCoverage2D coverage) {
			final GridSampleDimension[] sampleDimensions = coverage.getSampleDimensions();
			if(sampleDimensions.length == 3)
			{
				final RenderedImage raster=coverage.getRenderedImage();
				if(raster==null)
					return false;
				final ColorModel cm= raster.getColorModel();
				if(cm==null)
					return false;				
				//get the color interpretation for the three bands
				if(TypeMap.getColorInterpretation(cm, 0)!=ColorInterpretation.RED_BAND)
					return false;
				if(TypeMap.getColorInterpretation(cm, 1)!=ColorInterpretation.GREEN_BAND)
					return false;
				if(TypeMap.getColorInterpretation(cm,2)!=ColorInterpretation.BLUE_BAND)
					return false;
				return true;
			}
			return false;
		}
	}

	/** Closure called with read access */
	public static abstract class Write<T> {
		public abstract T run(GeoTiffWriter reader, GeoTiffAccess access)
				throws IOException;
	}

	/**
	 * Utility method to quickly report a failure to the progress listener and
	 * make it available to rethrow.
	 * 
	 * @param <T>
	 * @param listener
	 * @param problem
	 * @return problem
	 */
	private static <T extends Throwable> T fail(ProgressListener listener,
			T problem) {
		listener.exceptionOccurred(problem);
		return problem;
	}

	/**
	 * Lock used to control access to file set.
	 */
	final ReadWriteLock globalLock = new ReentrantReadWriteLock();
	
	/**
	 * Set access capabilities provided.
	 */
	final Set<AccessType> allowedAccessTypes = EnumSet.noneOf(AccessType.class);
	/**
	 * Class used for input.
	 * <p>
	 * Two possibilities exist in the codebase right now:
	 * <ul>
	 * <li>File
	 * <li>InputStream
	 * </ul>
	 */
	Class<?> inputClass;
	/**
	 * Indicate that a new GeoTiff must be created at the indicated url.
	 */
	private boolean mustCreate;
 
	/**
	 * Number of coverages contained in the GeoTiff.
	 */
	private int numberOfCoverages = 0;
	
	/**
	 * URL indicating the resource being accessed.
	 */
	URL input;

	/**
	 * Info about each coverage by Name.
	 */	
	@SuppressWarnings("unchecked")
	Map<Name, Info> coverageInfo = new ListOrderedMap();
	
	/**
	 * GridGeometry2D lookup by Name.
	 */
	//HashMap<Name, GridGeometry2D> coverageGeometries = new HashMap<Name, GridGeometry2D>();

	/**
	 * Names for the available coverages.
	 */
	//ArrayList<Name> coverageNames = new ArrayList<Name>();

	/**
	 * Bounds for the available coverages.
	 */
	//HashMap<Name, GeneralEnvelope> coverageExtents = new HashMap<Name, GeneralEnvelope>();

	/**
	 * RangeType describing our coverages.
	 */
	RangeType rangeType;

	/**
	 * Name of this coverage data product.
	 */
	private NameImpl name;

	private Map<String, Serializable> connectionParameters;
	/**
	 * An internal class defining the policy to interact with the GridCoveage.
	 * <p>
	 * A policy is selected from the list below during the init() method; giving
	 * us a single place to examin and decide how interaction occurs.
	 */
	static List<RangePolicy> rangeDefinitions = new ArrayList<RangePolicy>();
	static {
		rangeDefinitions.add(new DEMPolicy());
		rangeDefinitions.add(new RGBPolicy());
		
	}
	
	/**
	 * Access a GeoTiff from the provided source.
	 * <p>
	 * Please note this constructor has package visibility; you are expected to
	 * use the GeoTiffDriver for any and all access.
	 * 
	 * @param source
	 * @param params
	 * @param hints
	 * @param listener
	 * @param canCreate
	 * @throws IOException
	 */
	GeoTiffAccess(
			final Driver driver, 
			URL source, 
			final Map<String, Serializable> params,
			final Hints hints, 
			ProgressListener listener, 
			final boolean canCreate)
			throws IOException {
		super(driver);

		if (listener == null)
			listener = new NullProgressListener();

		// url lookup
		if (source == null) {
			if (params.containsKey(URL.key)) {
				source = (URL) params.get(URL.key);
			}
		}
		if (source == null)
			throw new IllegalArgumentException("Source 'url' is required");

		connectionParameters = new HashMap<String, Serializable>();
		if (params != null) {
			connectionParameters.putAll(params);
		}
		connectionParameters.put(URL.key, source);
		// get the protocol
		final String protocol = source.getProtocol();
		listener.setTask(new SimpleInternationalString("connect"));
		try {
			// file
			if (protocol.equalsIgnoreCase("file")) {
				// convert to file
				final File sourceFile = BaseFileDriver.urlToFile(source);

				// does it exists?
				if (!sourceFile.exists()) {
					// can we create?
					if (!canCreate) {
						throw new FileNotFoundException("GeoTIFF file '"+ sourceFile + "' does not exist.");
					}
					// leave a flag saying that we must create it
					this.mustCreate = true;

					// get the parent dir
					final File parentDir = sourceFile.getParentFile();
					// check that it is directory,exists and can be written
					if (!parentDir.exists() || !parentDir.isDirectory()|| !parentDir.canWrite()) {
						throw new IllegalArgumentException("Invalid input");
					}
					// set access type
					this.allowedAccessTypes.add(AccessType.READ_WRITE);
				} else {
					// check that it is a file,exists and can be at least read
					if (!sourceFile.exists() || !sourceFile.isFile()|| !sourceFile.canRead()) {
						throw fail(listener, new IllegalArgumentException("Read access required to file " + sourceFile));
					}
					// set access type
					if (sourceFile.canWrite()) {
						// set access type
						this.allowedAccessTypes.add(AccessType.READ_WRITE);
					}
					// set access type
					this.allowedAccessTypes.add(AccessType.READ_ONLY);
				}
				listener.progress(0.1f);
				// set the class type
				this.inputClass = File.class;
				this.input = source;

				// initialize
				this.init();

				return;
			}

			// input stream
			if (protocol.equalsIgnoreCase("http")
					|| protocol.equalsIgnoreCase("ftp")) {
				InputStream inStream = null;
				try {
					listener.progress(0.1f);
					// check that the url actually exists and can be read
					inStream = source.openStream();

					// try and read a few bytes
					final byte[] bytes = new byte[256];
					if (inStream.read(bytes) <= 0) {
						throw new IllegalArgumentException(
								"Input stream could not be opened");
					}
					// set the input class type
					this.inputClass = InputStream.class;
					this.input = source;

					// set access type
					this.allowedAccessTypes.add(AccessType.READ_ONLY);

					// initialize
					this.init();

					return;
				} catch (Throwable t) {
					throw fail(listener, new IllegalArgumentException(
							"Could not connect to input", t));
				} finally {
					if (inStream != null)
						try {

						} catch (Exception e) {
							inStream.close();
						}
				}

			}
			// nothing else for the moment
			throw new IllegalArgumentException("Invalid input");
		} finally {
			listener.complete();
		}
	}	

	public CoverageSource access(Name name, Map<String, Serializable> params,
			AccessType accessType, Hints hints, ProgressListener listener)
			throws IOException {
		if (listener == null)
			listener = new NullProgressListener();
		listener.started();
		try {
			if (!allowedAccessTypes.contains(accessType))
				throw new IllegalAccessError("Illegal access type requested");
			if (!this.coverageInfo.containsKey(name)) {
				String localPart = name.getLocalPart().lastIndexOf(":") > 0 ? name
						.getLocalPart().substring(
								name.getLocalPart().lastIndexOf(":") + 1)
						: name.getLocalPart();
				if (!this.coverageInfo.containsKey(new NameImpl(localPart))) {
					throw new IllegalArgumentException("Name not found "
							+ name.toString());
				} else
					return new GeoTiffStore(this, new NameImpl(localPart));
			} else
				return new GeoTiffStore(this, name);
		} finally {
			listener.complete();
		}
	}

	public boolean canCreate(Name name, Map<String, Serializable> params,
			Hints hints, ProgressListener listener) throws IOException {
		return allowedAccessTypes.contains(AccessType.READ_WRITE);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geotools.coverage.io.CoverageAccess#create(org.opengis.feature.type
	 * .Name, java.util.Map, org.geotools.coverage.io.CoverageAccess.AccessType,
	 * org.geotools.factory.Hints, org.opengis.util.ProgressListener)
	 */
	public CoverageStore create(Name name, Map<String, Serializable> params,
			Hints hints, ProgressListener listener) throws IOException {
		if (!allowedAccessTypes.contains(AccessType.READ_WRITE)) {
			throw new IllegalAccessError("Illegal access type requested");
		}
		this.coverageInfo.put(name, new Info(name));
		return new GeoTiffStore(this, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.coverage.io.CoverageAccess#dispose()
	 */
	public void dispose() {
		input = null;
		inputClass = null;
	}


	public Map<String, Serializable> getConnectParameters() {
		return Collections.unmodifiableMap(connectionParameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geotools.coverage.io.CoverageAccess#getExtent(org.opengis.feature
	 * .type.Name, org.opengis.util.ProgressListener)
	 */
	public GeneralEnvelope getExtent(Name name,
			ProgressListener listener) {
		
		Info info = getInfo( name, listener);
		return info.getExtent();
	}

	/**
	 * ResourceInfo for the indicated coverage.
	 * @param name
	 * @param listener
	 * @return
	 */
	public Info getInfo(final Name name, ProgressListener listener) {
		/**
		 * I am going to lazily create these and hold onto them rather than having a bunch
		 * of little HashMaps for things like Extent; CoverageGeometries and so on...
		 */
		if (listener == null){
			listener = new NullProgressListener();
		}
		try {
			listener.started();
			if( coverageInfo.containsKey(name)){
				return coverageInfo.get(name);
			}
			/*
			// info not available let us look?
			return read(new Read<Info>() {
				public Info run(GeoTiffReader reader,
						GeoTiffAccess access) throws IOException {
					// check again incase another thread was fetching the answer
					if( coverageInfo.containsKey(name)){
						return coverageInfo.get(name);
					}
					// okay let us figure it out
					return new Info(name);
				}
			});
			*/
			listener.exceptionOccurred(new IOException("Unknown coverage "+name));
			return null;
		}
		finally {
			listener.complete();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.geotools.coverage.io.CoverageAccess#getInfo(org.opengis.util.
	 * ProgressListener)
	 */
	public ServiceInfo getInfo(ProgressListener listener) {
		if (listener == null)
			listener = new NullProgressListener();
		try {
			return read(new Read<ServiceInfo>() {
				public ServiceInfo run(GeoTiffReader reader,
						GeoTiffAccess access) throws IOException {
					DefaultServiceInfo info = new DefaultServiceInfo();
					info.setTitle(reader.getCoverageName());

					StringBuffer description = new StringBuffer();
					
					Driver driver = getDriver();
					description.append( "Name: ");
					description.append( reader.getCoverageName() );
					description.append( "\nDriver: ");
					description.append( driver.getName() );
					description.append( "/" );
					description.append( getDriver().getTitle() );
					description.append( "\nSize is ");
					GridEnvelope size = reader.getOriginalGridRange();
					description.append(size.getSpan(0));
					description.append(", ");
					description.append(size.getSpan(1));
					description.append("\nCoordinate System is:\n");
					CoordinateReferenceSystem crs = reader.getCrs();
					description.append( crs.toWKT() );
					
					GeneralEnvelope bbox = reader.getOriginalEnvelope();					
					description.append("\nOrigion = ( ");
					DirectPosition lower = bbox.getLowerCorner();
					
					for( int dimension = 0; dimension < crs.getCoordinateSystem().getDimension(); dimension++ ){						
						if( dimension != 0 ){
							description.append(", ");
						}
						description.append( lower.getOrdinate( dimension ) );						
					}
					description.append(" )");
					
					info.setDescription(description.toString());

					try {
						info.setSource(input.toURI());
					} catch (URISyntaxException e1) {
					}
					try {
						// This should be a representation of the data product
						info.setSchema(new URI(
										"http://www.remotesensing.org/geotiff/spec/geotiffhome.html"));
					} catch (URISyntaxException e) {
					}
					try {
						if (inputClass == File.class) {
							info.setPublisher(new URI(System
									.getProperty("user.name")));
						} else {
							info.setPublisher(new URI(input.getProtocol()
									+ input.getHost()));
						}
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					return info;
				}
			});
		} catch (IOException problem) {
			listener.exceptionOccurred(problem);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.geotools.coverage.io.CoverageAccess#getNames(org.opengis.util.
	 * ProgressListener)
	 */
	public List<Name> getNames(ProgressListener listener) {
		if (listener == null)
			listener = new NullProgressListener();
		listener.started();
		try {
			return Collections.unmodifiableList(
					new ArrayList<Name>( coverageInfo.keySet()));
		} finally {
			listener.complete();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geotools.coverage.io.CoverageAccess#getNumCoverages(org.opengis.util
	 * .ProgressListener)
	 */
	public int getCoveragesNumber(ProgressListener listener) {
		return this.numberOfCoverages;
	}
	public Set<AccessType> getSupportedAccessTypes() {
		return Collections.unmodifiableSet(allowedAccessTypes);
	}

	private void init() throws IOException {
		// check if we already have something or not
		if (this.mustCreate) {
			this.numberOfCoverages = 0;
			return;
		}
		try	{
			read(new Read<Object>() {
				@Override
				public Object run(GeoTiffReader reader, GeoTiffAccess access)
						throws IOException {
					// set the number of coverages
					numberOfCoverages = reader.getGridCoverageCount();
	
					final GeneralEnvelope envelope = reader.getOriginalEnvelope();
					final CoordinateReferenceSystem crs = reader.getCrs();
					final GridEnvelope range = reader.getOriginalGridRange();
					final MathTransform2D g2w = (MathTransform2D) reader
							.getOriginalGridToWorld(PixelInCell.CELL_CENTER);
	
					name = new NameImpl(reader.getCoverageName());
					Info info = new Info( name );
					info.setGeometry( new GridGeometry2D(range, g2w, crs) );
					info.setExtent(envelope);
					coverageInfo.put( name, info );

					// init RANGE
					final GridCoverage2D gc = (GridCoverage2D) reader.read(null);
					for (RangePolicy policy : rangeDefinitions) {
						try {
							if (policy.match(gc)) {
								rangeType = policy.describe(gc);
							}
						} catch (Throwable eek) {
	
						}
					}					
					return null; // nothing to return
				}
			});
		}
		catch (Throwable t ){
			// could not init out state ... something must be horribly wrong
			numberOfCoverages = 0;
			coverageInfo.clear();			
			if( t instanceof IOException ){
				throw (IOException) t;
			}
			else {
				throw new DataSourceException("Could not initialize FieldType information", t );
			}
		}
		// get the needed info from them to set the extent
	}

	public boolean isCreateSupported() {
		return allowedAccessTypes.contains(AccessType.READ_WRITE);
	}

	/** Method called to read the GeoTiff file or input stream */
	public <T> T read(Read<T> read) throws IOException {
		// open up a reader
		globalLock.readLock().lock();
		try {
			GeoTiffReader reader = new GeoTiffReader(this.input);
			try {
				return read.run(reader, this);
			} finally {
				if (reader != null) {
					reader.dispose();
				}
			}
		} finally {
			globalLock.readLock().unlock();
		}
	}

	//
	// utility methods
	//

	/** Method called to read the GeoTiff file or input stream */
	public <T> T write(Write<T> write) throws IOException {
		// open up a reader
		globalLock.writeLock().lock();
		try {
			GeoTiffWriter writer = new GeoTiffWriter(this.input);
			try {
				return write.run(writer, this);
			} finally {
				if (writer != null) {
					writer.dispose();
				}
			}
		} finally {
			globalLock.writeLock().unlock();
		}
	}
}
