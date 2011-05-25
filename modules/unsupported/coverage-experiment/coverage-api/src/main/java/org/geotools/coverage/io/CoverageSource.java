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
package org.geotools.coverage.io;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.geotools.coverage.io.domain.RasterDatasetDomainManager;
import org.geotools.coverage.io.impl.CoverageReadRequest;
import org.geotools.coverage.io.impl.CoverageResponse;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.data.Parameter;
import org.geotools.data.ResourceInfo;
import org.opengis.feature.type.Name;
import org.opengis.util.ProgressListener;

/**
 * Allows read-only access to a Coverage.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Jody Garnett
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/CoverageSource.java $
 */
public interface CoverageSource {

	/**
	 * Name of the Coverage (ie data product) provided by this CoverageSource.
	 * 
	 * @since 2.5
	 * @return Name of the Coverage (ie data product) provided.
	 */
	Name getName(final ProgressListener listener);

	/**
	 * Information describing the contents of this resource.
	 * <p>
	 * Please note that for FeatureContent:
	 * <ul>
	 * <li>name - unique with in the context of a Service
	 * <li>schema - used to identify the type of resource; usually the format
	 * or data product being represented
	 * <ul>
	 * 
	 * @todo do we need this??
	 */
	ResourceInfo getInfo(final ProgressListener listener);
//
//	/**
//	 * The first {@link Rectangle} should describe the overall bidimensional
//	 * raster range for the underlying coverage. However, by setting the
//	 * <code>overall</code> param to true we can request additional raster
//	 * ranges in case the area covered by the mentioned coverage is poorly
//	 * approximated by a single {@link Rectangle}, like it could happen for a
//	 * mosaic which has some holes.
//	 * 
//	 * @param overall
//	 * @param listener
//	 * @return
//	 * @throws IOException
//	 * 
//	 * @todo should we consider {@link GridEnvelope}?? or {@link ImageLayout} which also contains tiling information???
//	 */
//	public List<Rectangle> getRasterDomain(final boolean overall,
//			final ProgressListener listener) throws IOException;
	/**
	 * Describes the required (and optional) parameters that
	 * can be passed to the {@link #read(CoverageReadRequest, ProgressListener)} method.
	 * <p>
	 * @return Param a {@link Map} describing the {@link Map} for {@link #read(CoverageReadRequest, ProgressListener)}.
	 */
	public Map<String, Parameter<?>> getReadParameterInfo();		
	
	/**
	 * Obtain a {@link CoverageResponse} from this {@link CoverageSource} given a specified {@link DefaultCoverageRequest}.
	 * 
	 * @param request the input {@link DefaultCoverageRequest}.
	 * @param listener
	 * @return
	 * @throws IOException
	 */
	public CoverageResponse read(final CoverageReadRequest request,
	        final ProgressListener listener) throws IOException;

	/**
	 * Retrieves a {@link RangeType} instance which can be used to describe the
	 * codomain for the underlying coverage.
	 * 
	 * @param listener
	 * @return a {@link RangeType} instance which can be used to describe the
	 * 			codomain for the underlying coverage.
	 * @throws IOException in case something bad occurs
	 */
	public RangeType getRangeType(final ProgressListener listener) throws IOException;

	/**
	 * Closes this {@link CoverageSource} and releases any lock or cached information it holds.
	 * 
	 * <p>
	 * Once a {@link CoverageAccess} has been disposed it can be seen as being in unspecified state, 
	 * hence calling a method on it may have unpredictable results.
	 */
	public void dispose();
	
	/**
	 * Set of supported {@link CoverageCapabilities} which can be used to discover
	 * capabilities of a certain {@link CoverageSource}.
	 * <p>
	 * You can use set membership to quickly test abilities:<code><pre>
	 * if( getCapabilities().contains( CoverageCapabilities.READ_SUBSAMPLING ) ){
	 *     ...
	 * }
	 * </code></pre>
	 * @return a {@link EnumSet} of CoverageCapabilities which can be used to discover
	 * capabilities of this {@link CoverageSource}.
	 */
	public EnumSet<CoverageCapabilities> getCapabilities();

	public MetadataNode getMetadata(String metadataDomain,final ProgressListener listener);

	public Set<Name> getMetadataDomains();

	public RasterDatasetDomainManager getDomainManager(final ProgressListener listener) throws IOException;
	
//	/**
//	 * @todo TBD, I am not even sure this should leave at the general interface level!
//	 * 
//	 * @return
//	 * @throws IOException
//	 */
//	public Object getGCPManager(final ProgressListener listener)throws IOException;
//	
//	/**
//	 * @todo TBD, I am not even sure this should leave at the general interface level!
//	 * 
//	 * @return
//	 * @throws IOException
//	 */
//	public Object getStatisticsManager(final ProgressListener listener)throws IOException;
//	
//	
//	
//	/**
//	 * @todo TBD, I am not even sure this should leave at the general interface level!
//	 * 
//	 * @return
//	 * @throws IOException
//	 */
//	public Object getOverviewsManager(final ProgressListener listener)throws IOException;
	

}
