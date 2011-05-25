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
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.coverage.io.driver.Driver;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.data.Parameter;
import org.geotools.data.ServiceInfo;
import org.geotools.factory.Hints;
import org.opengis.feature.type.Name;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.extent.Extent;
import org.opengis.util.ProgressListener;

/**
 * Represents a Physical storage of coverage data (that we have a connection to).
 * <p>
 * Please note that this service may be remote (or otherwise slow). You are doing 
 * IO here and should treat this class with respect - please do not access these 
 * methods from a display thread.
 * </p>
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Jody Garnett
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/CoverageAccess.java $
 */
public interface CoverageAccess {
	
    /**
     * Level of access supported.
     */
    public enum AccessType {
        /**
         * Read access to coverage data.
         * <p>
         * This level of access implys {@link CoverageAccess#access(Name, Map, AccessType, Hints, ProgressListener)} is able to
         * return a {@link CoverageSource}
         */
        READ_ONLY,
        /**
         * Read-write access to coverage data
         * <p>
         * This level of access implys {@link CoverageAccess#access(Name, Map, AccessType, Hints, ProgressListener)} is able to
         * return a {@link CoverageStore}
         */
        READ_WRITE;
    }
    
    /**
     * Returns the {@link Driver} which has been used to connect to this
     * CoverageAccess.
     * 
     * @return {@link Driver} used to connect
     */
    public Driver getDriver();
    
    /**
     * Description of the CoverageAccess we are connected to here.
     * <p>
     * @todo TODO think about the equivalence with StreamMetadata once we define
     *       them
     * 
     * @return Description of the CoverageAccess we are connected to here.
     */
    public ServiceInfo getInfo(final ProgressListener listener);

    /**
     * Retrieves the {@link Set} of supported {@link AccessType}s for this
     * {@link CoverageAccess} instance.
     * 
     * @return the {@link Set} of supported {@link AccessType}s for this
     *         {@link CoverageAccess} instance.
     */
    public Set<CoverageAccess.AccessType> getSupportedAccessTypes();

    // 
    // Describe Contents
    //
    /**
     * Names of the available Coverages.
     * <p>
     * Each Coverage named here represents a unique data product that
     * may be accessed via the CoverageSource
     * 
     * @return Names of the available contents.
     * @throws IOException
     */
    public List<Name> getNames(final ProgressListener listener);

    
    /**
     * The number of Coverages made available.
     * 
     * @param listener
     * @return getNames( listener ).size()
     */
    public int getCoveragesNumber(final ProgressListener listener);
    
    //
    // it is tempting to make Name support children here (the ISO GenericName
    // does)
    // which would allow us to present a tree of data, rather than a flat list.
    // (not sure if this is a good idea - just getting it out there)
    //
    
	/**
	 * This method can be used to acquire a rough description of a coverage
	 * spatiotemporal domain.
	 * 
	 * @param coverageName
	 * @param listener
	 * @return {@link Envelope}
	 * 
	 * TODO @todo consider geoapi {@link Extent} which can in turn wrap the Envelope
	 *       we are providing here In order to do so we need to resolve the
	 *       ambiguity of the 3D inseparable CRSs between vertical and horizontal domain.
	 */
    public Envelope getExtent(Name coverageName, final ProgressListener listener);
    
    //
    // Data Access
    //
    /**
     * Describes the required (and optional) parameters that can be used to open
     * a {@link CoverageSource}.
     * <p>
     * 
     * @return Param a {@link Map} describing the {@link Map} for
     *         {@link #connect(Map)}.
     */
    public Map<String, Parameter<?>> getAccessParameterInfo(CoverageAccess.AccessType accessType);

    /**
     * Retrieve a {@link CoverageSource} to access a Named Coverage.
     * <p>
     * @param Name Indicate the coverage to access
     * @param params Additional parameters as needed to indicate what part of the data set to access
     * @param accessType Requested level of access 
     * @param Hints Implementation specific hints; please review the javadocs for your Driver for details
     * @param listener used to report progress while obtianing access
     */
    public CoverageSource access(Name name, Map<String, Serializable> params,
            AccessType accessType, Hints hints, ProgressListener listener)
            throws IOException;

    // 
    // Data Modification
    //
    // These methods only work for getSupportedAccessTypes() == READ_WRITE
    
    /**
     * Tells me whether or not this {@link CoverageAccess} supports creation of
     * a new coverage storage.
     * <p>
     * This method will only return true if getSupportedAccessTypes() == READ_WRITE.
     * 
     * @return <code>true</code> when removal of of a new coverage storage is
     *         supported, <code>false</code> otherwise.
     */
    public boolean isCreateSupported();


    /**
     * Test to see if this coverage access is suitable for creating a
     * {@link CoverageStore} referred by Name, with the specified set of
     * parameters.
     * <p>
     * This method will only return true if getSupportedAccessTypes() == READ_WRITE.
     * 
     * @param Name The name of the data set to create
     * @param params Indicate the content to be created
     * @param Hints Implementations specific Hints, please check the javadocs for your driver for details
     * 
     * @return true if a coverage can be created
     */
    public boolean canCreate(Name name, Map<String, Serializable> params,
            Hints hints, ProgressListener listener) throws IOException;

    /**
     * Create a {@link CoverageStore} with the specified name.
     * <p>
     * You can check isCreateSupported() prior to calling this method.
     * Implementing subclasses may throw an {@link UnsupportedOperationException} in case the
     * related Driver won't allow {@link CoverageStore} creation.
     * @param Name The name of the data set to create
     * @param params Indicate the content to be created
     * @param Hints Implementations specific Hints, please check the javadocs for your driver for details
     * 
     * @throws IllegalStateException if getSupportedAccessTypes() == READ_ONLY
     */
    public CoverageStore create(Name name, Map<String, Serializable> params,
            Hints hints, ProgressListener listener) throws IOException;

    /**
     * Tells me whether or not this {@link CoverageAccess} supports removal of
     * an existing coverage storage.
     * 
     * @return <code>true</code> when removal of an existing coverage storage
     *         is supported, <code>false</code> otherwise.
     */
    public boolean isDeleteSupported();

    /**
     * Test to see if this coverage access is suitable for deleting a
     * {@link CoverageSource} referred by Name, with the specified set of
     * parameters.
     * @param Name Name of data set to remove from
     * @param Params Used to indicate what what of the data set to remove; if <code>null</code> indicate the entire data set should be removed
     * @param Hints Implementation specific Hints; please consult the javadocs for the Driver you are working with.
     */
    public boolean canDelete(Name name, Map<String, Serializable> params,
            Hints hints) throws IOException;
    
    /**
     * Asks this {@link CoverageAccess} to entirely remove a certain Coverage
     * from the available {@link CoverageSource}s.
     * 
     * <p>
     * Many file based formats won't allow to perform such operation, but db
     * based source should be quite happy with it.
     * 
     * @param name
     * @param params
     * @param accessType
     * @param hints
     * 
     * @return {@code true} in case of success.
     * @throws IOException
     */
    public boolean delete(Name name, Map<String, Serializable> params,
            Hints hints) throws IOException;

    /**
     * Retrieves the parameters used to connect to this live instance of
     * {@link CoverageAccess}.
     * 
     * 
     * @return the parameters used to connect to this live instance of
     *         {@link CoverageAccess}.
     */
    public Map<String, Serializable> getConnectParameters();

    /**
     * This will free any cached info object or header information.
     * <p>
     * Often a {@link CoverageAccess} will keep a file channel open, this will
     * clean that sort of thing up.
     * 
     * <p>
     * Once a {@link CoverageAccess} has been disposed it can be seen as being
     * in unspecified state, hence calling a method on it may have unpredictable
     * results.
     * 
     */
    public void dispose();
    

	public MetadataNode getStorageMetadata(String metadataDomain);

	public Set<String> getStorageMetadataDomains();


}
