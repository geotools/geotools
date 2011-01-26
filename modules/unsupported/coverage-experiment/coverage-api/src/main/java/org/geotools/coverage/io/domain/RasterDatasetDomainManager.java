package org.geotools.coverage.io.domain;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.geotools.referencing.CRS;
import org.geotools.util.NumberRange;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.TransfiniteSet;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.opengis.util.ProgressListener;

public abstract class RasterDatasetDomainManager {
	
	/**
	 * Envelope??? range????
	 * @return
	 */
	public abstract Extent getExtent();
	
	public abstract CoordinateReferenceSystem getCoordinateReferenceSystem();
	
	
	public abstract class HorizontalDomain{

		/**
		 * The first {@link BoundingBox} of this {@link List} should contain the
		 * overall bounding for the underlying coverage in its native coordinate
		 * reference system. However, by setting the <code>global</code> param to
		 * true we can request additional bounding boxes in case the area covered by
		 * the mentioned coverage is poorly approximated by a single coverage, like
		 * it could happen for a mosaic which has some holes.
		 * 
		 * @param overall
		 * @param listener
		 * @return
		 * @throws IOException
		 * @see {@link BoundingBox}
		 * 
		 */
		public abstract Set<? extends BoundingBox> getSpatialElements(final boolean overall,final ProgressListener listener) throws IOException;
		
		public abstract CoordinateReferenceSystem getCoordinateReferenceSystem2D();

		/**
		 * Transformation between the 2D raster space and the 2D model space. In
		 * case the underlying coverage is unrectified this transformation maybe a
		 * georeferencing transformation of simply the identity in case we do not
		 * have means to georeference the mentioned coverage.
		 * 
		 * @param brief
		 * @param listener
		 * @return
		 * @throws IOException
		 */
		public abstract MathTransform2D getGridToWorldTransform(final ProgressListener listener) throws IOException;

		/**
		 * The first {@link Rectangle} should describe the overall bidimensional
		 * raster range for the underlying coverage. However, by setting the <code>
		 * overall</code> param to true we can request additional raster ranges in
		 * case the area covered by the mentioned coverage is poorly approximated by
		 * a single {@link Rectangle}, like it could happen for a mosaic which has
		 * some holes.
		 * 
		 * @param overall
		 * @param listener
		 * @return
		 * @throws IOException
		 * 
		 * @todo should we consider {@link GridEnvelope}?? or RasterLayout which also
		 *       contains tiling information??? This has also an impact on the
		 *       {@link #getOptimalDataBlockSizes()} method, which may become
		 *       useless
		 */
		public abstract Set<? extends RasterLayout> getRasterElements(final boolean overall,final ProgressListener listener) throws IOException;
		
	}
	
	public abstract class VerticalDomain{

		/**
		 * A {@link Set} of {@link Envelope} element for the underlying coverage.
		 * Note that the {@link CRS} for such envelope can be <code>null</code> in case the overall spatial {@link CRS} 
		 * is a non-separable 3D {@link CRS} like WGS84-3D. Otherwise, all the envelopes should share the same
		 * {@link VerticalCRS}. Finally, note that the envelope should be 1-dimensional. In case of single vertical value, 
		 * the lower coordinate should match the upper coordinate while lower and upper coordinates may be different to define
		 * vertical intervals.
		 * 
		 * @param overall
		 * @param listener
		 * @return
		 * @throws IOException
		 * @todo consider {@link TransfiniteSet} as an alternative to {@link SortedSet}
		 * @todo allow using an interval as well as a direct position
		 * @todo allow transfinite sets!
		 */
		public abstract SortedSet<? extends NumberRange<Double>> getVerticalElements(final boolean overall,final ProgressListener listener) throws IOException;

		public abstract CoordinateReferenceSystem getCoordinateReferenceSystem();
		
	}
	
	public abstract class TemporalDomain{

		/**
		 * Describes the temporal domain for the underlying {@link RasterDataset}
		 * by returning a {@link Set} of {@link TemporalGeometricPrimitive}s elements for
		 * it.
		 * Note that the {@link TemporalCRS} for the listed {@link TemporalGeometricPrimitive}s
		 * can be obtained from the overall {@link CRS} for the underlying coverage.
		 * 
		 * @todo should we change to something different than
		 *       {@link TemporalGeometricPrimitive}? Should we consider GML TimeSequence
		 * @param listener
		 * @return a {@link Set} of {@link TemporalGeometricPrimitive}s elements.
		 * @todo allow transfinite sets!
		 * @throws IOException
		 */
		public abstract SortedSet<? extends TemporalGeometricPrimitive> getTemporalElements(final ProgressListener listener) throws IOException;

		public abstract CoordinateReferenceSystem getCoordinateReferenceSystem();
		
	}
	
	
	public abstract TemporalDomain getTemporalDomain() throws IOException;

	public abstract VerticalDomain getVerticalDomain() throws IOException;
	
	public abstract HorizontalDomain getHorizontalDomain() throws IOException;
	
	

}
