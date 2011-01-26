/**
 * 
 */
package org.geotools.coverage.io.geotiff;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffException;
import org.geotools.coverage.io.CoverageCapabilities;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.geotiff.GeoTiffAccess.Info;
import org.geotools.coverage.io.impl.DefaultCoverageResponseImpl;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.data.Parameter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.omg.CORBA.DomainManager;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.opengis.util.ProgressListener;

/**
 * Source of GeoTiff coverage data; used for read-only access to a GeoTiff file
 * or stream.
 * 
 * @author simone
 */
public class GeoTiffSource implements CoverageSource {

	/**
	 * CoverageAccess we are working from.
	 * <p>
	 * We will refer to this CoverageAccess from time to time (in particular to
	 * access a "global lock" used to prevent write operations tripping up over
	 * each other).
	 */
	protected GeoTiffAccess access;

	/** Name of the Coverage we are providing */
	protected Name name;

	protected RangeType rangeType;

	protected EnumSet<CoverageCapabilities> capabilities;

	/**
	 * 
	 */
	GeoTiffSource(final GeoTiffAccess geotiffAccess, final Name name) {
		access = geotiffAccess;
		this.name = name;
		capabilities = EnumSet.of(
				CoverageCapabilities.READ_HORIZONTAL_DOMAIN_SUBSAMBLING,
				CoverageCapabilities.READ_RANGE_SUBSETTING,
				CoverageCapabilities.READ_REPROJECTION,
				CoverageCapabilities.READ_SUBSAMPLING);
	}

	/** Used to quickly check if we are disposed. */
	protected boolean isDisposed() {
		return name == null;
	}

	protected void ensureNotDisposed() {
		if (isDisposed()) {
			throw new IllegalStateException("Disposed");
		}
	}

	public EnumSet<CoverageCapabilities> getCapabilities() {
		return EnumSet.copyOf(capabilities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.coverage.io.CoverageSource#dispose()
	 */
	public synchronized void dispose() {
		this.access = null;
		this.name = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geotools.coverage.io.CoverageSource#getCoordinateReferenceSystem(
	 * org.opengis.util.ProgressListener)
	 */
	public CoordinateReferenceSystem getCoordinateReferenceSystem(
			ProgressListener listener) throws IOException {
		ensureNotDisposed();
		return getInfo(listener).getCRS();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geotools.coverage.io.CoverageSource#getGridToWorldTransform(boolean,
	 * org.opengis.util.ProgressListener)
	 */
	public MathTransform2D getGridToWorldTransform(boolean brief,
			ProgressListener listener) throws IOException {
		ensureNotDisposed();
		return getInfo(listener).getGeometry().getGridToCRS2D(
				PixelOrientation.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.coverage.io.CoverageSource#getHorizontalDomain(boolean,
	 * org.opengis.util.ProgressListener)
	 */
	public List<BoundingBox> getHorizontalDomain(boolean global,
			ProgressListener listener) throws IOException {
		ensureNotDisposed();
		Info info = getInfo( listener );
		BoundingBox bbox = info.getBounds();
		return Collections.singletonList( bbox);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.coverage.io.CoverageSource#getInfo(org.opengis.util.
	 * ProgressListener)
	 */
	public Info getInfo(ProgressListener listener) {
		ensureNotDisposed();		
		return access.getInfo( name, listener);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.geotools.coverage.io.CoverageSource#getName(org.opengis.util.
	 * ProgressListener)
	 */
	public Name getName(ProgressListener listener) {
		ensureNotDisposed();
		return this.name;
	}

	public RangeType getRangeType(ProgressListener listener) throws IOException {
		ensureNotDisposed();
		return access.rangeType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.coverage.io.CoverageSource#getRasterDomain(boolean,
	 * org.opengis.util.ProgressListener)
	 */
	public List<Rectangle> getRasterDomain(boolean overall,
			ProgressListener listener) throws IOException {
		ensureNotDisposed();
		Info info = getInfo( listener );		
		return Collections.singletonList(new Rectangle(info.getGeometry().getGridRange2D()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geotools.coverage.io.CoverageSource#getTemporalDomain(org.opengis
	 * .util.ProgressListener)
	 */
	public Set<TemporalGeometricPrimitive> getTemporalDomain(
			ProgressListener listener) throws IOException {
		ensureNotDisposed();
		return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.coverage.io.CoverageSource#getVerticalDomain(boolean,
	 * org.opengis.util.ProgressListener)
	 */
	public Set<Envelope> getVerticalDomain(boolean global,
			ProgressListener listener) throws IOException {
		ensureNotDisposed();
		return Collections.emptySet();
	}

	public Map<String, Parameter<?>> getReadParameterInfo() {
		ensureNotDisposed();
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geotools.coverage.io.CoverageSource#read(org.geotools.coverage.io
	 * .CoverageRequest, org.opengis.util.ProgressListener)
	 */
	public CoverageResponse read(final CoverageReadRequest request,
			ProgressListener listener) throws IOException {
		ensureNotDisposed();

		final DefaultCoverageResponseImpl response = new DefaultCoverageResponseImpl();
		response.setRequest(request);

		if (listener == null)
			listener = new NullProgressListener();
		listener.started();
		try {
			access.read(new GeoTiffAccess.Read<CoverageResponse>() {
				public CoverageResponse run(GeoTiffReader reader,
						GeoTiffAccess access) throws IOException {

					// get the request elements
					final BoundingBox bbox = request.getGeographicArea();
					final Rectangle rasterArea = request.getRasterArea();
					final MathTransform2D g2w = request
							.getGridToWorldTransform();

					final GridCoverage2D coverage;
					if (g2w != null || rasterArea != null || g2w != null) {
						final ParameterValue<GridGeometry2D> readParameter = AbstractGridFormat.READ_GRIDGEOMETRY2D
								.createValue();
						if (g2w == null) {
							if (rasterArea == null) {
								Info info = getInfo(null );
								readParameter.setValue(new GridGeometry2D(
										info.getGeometry().getGridRange2D(),
										new ReferencedEnvelope(bbox)));
							} else {
								readParameter.setValue(new GridGeometry2D(
										new GridEnvelope2D(rasterArea),
										new ReferencedEnvelope(bbox)));
							}
						} else {
							readParameter.setValue(new GridGeometry2D(
									new GridEnvelope2D(rasterArea),
									PixelInCell.CELL_CENTER, g2w, bbox
											.getCoordinateReferenceSystem(),
									null));
						}
						coverage = (GridCoverage2D) reader
								.read(new GeneralParameterValue[] { readParameter });
					} else {
						coverage = (GridCoverage2D) reader.read(null);
					}
					response.addResult(coverage);
					response.setStatus(Status.SUCCESS);

					return response;
				}
			});
		} catch (Throwable e) {
			response.addException(new GeoTiffException(null, "IO error", e));
		} finally {
			listener.complete();
		}
		return response;
	}

	public MetadataNode getMetadata(String metadataDomain,
			ProgressListener listener) {
		ensureNotDisposed();
		throw new UnsupportedOperationException();
	}

	public Set<Name> getMetadataDomains() {
		ensureNotDisposed();
		throw new UnsupportedOperationException();
	}

	public DomainManager getDomainManager(ProgressListener listener)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
