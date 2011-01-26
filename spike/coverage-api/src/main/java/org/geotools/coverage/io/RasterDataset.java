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
import java.util.Set;

import org.geotools.coverage.io.domain.RasterDatasetDomainManager;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.coverage.io.range.BandDescriptor;
import org.geotools.data.ResourceInfo;
import org.geotools.util.Range;
import org.opengis.feature.type.Name;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.opengis.util.ProgressListener;

/**
 * Allows read-only access to a Coverage.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Jody Garnett
 */
public interface RasterDataset {

	public MetadataNode getMetadata(String metadataDomain,final ProgressListener listener);
	
	public Set<Name> getMetadataDomains();
	
	/**
	 * Name of the Coverage (ie data product) provided by this RasterDataset.
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

	/**
	 * Retrieves a {@link RangeManager} instance which can be used to describe the
	 * codomain for the underlying coverage.
	 * 
	 * @param listener
	 * @return a {@link RangeManager} instance which can be used to describe the
	 * 			codomain for the underlying coverage.
	 * @throws IOException in case something bad occurs
	 */
	public BandDescriptor getBandDescriptor(final ProgressListener listener) throws IOException;
	
	public RasterDatasetDomainManager getDomainManager(final ProgressListener listener) throws IOException;
	
	/**
	 * Closes this {@link RasterDataset} and releases any lock or cached information it holds.
	 * 
	 * <p>
	 * Once a {@link RasterStorage} has been disposed it can be seen as being in unspecified state, 
	 * hence calling a method on it may have unpredictable results.
	 */
	public void close();
	
	/**
	 * In case there is no temporal domain and/or vertical domain the relative bins are ignored.
	 * 	
	 * @param temporalBin
	 * @param verticalBin
	 * @return
	 */
	public RasterSlice getRasterSlice(TemporalGeometricPrimitive temporalBin, Range<Double>  verticalBin,ProgressListener progress )throws IOException;
	
	public RasterSlice getRasterSlice(int index,ProgressListener progress )throws IOException;
	
	public int getRasterSlicesNumber(ProgressListener progress )throws IOException;
	
	public RasterDatasetDomainManager getRasterSliceDomain(int index,ProgressListener progress )throws IOException;
	
	public RasterDatasetDomainManager getRasterSliceDomain(TemporalGeometricPrimitive temporalBin, Range<Double>  verticalBin,ProgressListener progress )throws IOException;
}