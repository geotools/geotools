package org.geotools.coverage.io.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageStore;
import org.geotools.coverage.io.driver.Driver;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.data.Parameter;
import org.geotools.data.ServiceInfo;
import org.geotools.factory.Hints;
import org.opengis.feature.type.Name;
import org.opengis.geometry.Envelope;
import org.opengis.util.ProgressListener;
/**
 * Default implementation of {@link CoverageAccess}.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/impl/DefaultCoverageAccess.java $
 */
public class DefaultCoverageAccess implements CoverageAccess {

	/**
     * Driver used to create this CoverageAccess.
     */
    private final Driver driver;

    public DefaultCoverageAccess(Driver driver) {
		this.driver=driver;
	}

	public CoverageSource access(Name name, Map<String, Serializable> params,
			AccessType accessType, Hints hints, ProgressListener listener)
			throws IOException {
		throw new UnsupportedOperationException("Operation not implemented");
	}

	public boolean canCreate(Name name, Map<String, Serializable> params,
			Hints hints, ProgressListener listener) throws IOException {
		return false;
	}

	public boolean canDelete(Name name, Map<String, Serializable> params,
			Hints hints) throws IOException {
		return false;
	}

	public CoverageStore create(Name name, Map<String, Serializable> params,
			Hints hints, ProgressListener listener) throws IOException {
		throw new UnsupportedOperationException("Operation not implemented");
	}

	public boolean delete(Name name, Map<String, Serializable> params,
			Hints hints) throws IOException {
		return false;
	}

	public Map<String, Parameter<?>> getAccessParameterInfo(
			AccessType accessType) {
		return null;
	}

	public Map<String, Serializable> getConnectParameters() {
		return null;
	}

	public int getCoveragesNumber(ProgressListener listener) {
		throw new UnsupportedOperationException("Operation not implemented");
	}

	public Envelope getExtent(Name coverageName, ProgressListener listener) {
		throw new UnsupportedOperationException("Operation not implemented");
	}

	public ServiceInfo getInfo(ProgressListener listener) {
		throw new UnsupportedOperationException("Operation not implemented");
	}

	public List<Name> getNames(ProgressListener listener) {
		return Collections.emptyList();
	}

	public MetadataNode getStorageMetadata(String metadataDomain) {
		throw new UnsupportedOperationException("Operation not implemented");
	}

	public Set<String> getStorageMetadataDomains() {
		return Collections.emptySet();
	}

	public Set<AccessType> getSupportedAccessTypes() {
		return Collections.emptySet();
	}

	public boolean isCreateSupported() {
		return false;
	}

	public boolean isDeleteSupported() {
		return false;
	}

    public Driver getDriver() {
    	return driver;
    }

    public void dispose() {
    	
    }

}
