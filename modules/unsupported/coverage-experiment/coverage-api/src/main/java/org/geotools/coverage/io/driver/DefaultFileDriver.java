package org.geotools.coverage.io.driver;

import java.io.IOException;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.opengis.util.ProgressListener;

public class DefaultFileDriver extends BaseFileDriver {

	protected DefaultFileDriver(String name, String description,
			String title, Hints implementationHints, List<String> fileExtensions) {
		super(name, description, title, implementationHints, fileExtensions);
	}

	@Override
	protected boolean canConnect(java.net.URL url,
			Map<String, Serializable> params) {
		return false;
	}

	@Override
	protected boolean canCreate(java.net.URL url,
			Map<String, Serializable> params) {
		return false;
	}

	@Override
	protected boolean canDelete(java.net.URL url,
			Map<String, Serializable> params) {
		return false;
	}

	@Override
	protected CoverageAccess connect(java.net.URL url, Map<String, Serializable> params,
			Hints hints, ProgressListener listener) throws IOException {
		throw new UnsupportedOperationException("Operation not currently implemented");
	}

	@Override
	protected CoverageAccess create(java.net.URL url, Map<String, Serializable> params,
			Hints hints, ProgressListener listener) throws IOException {
		throw new UnsupportedOperationException("Operation not currently implemented");
	}

	@Override
	protected CoverageAccess delete(java.net.URL url, Map<String, Serializable> params,
			Hints hints, ProgressListener listener) throws IOException {
		throw new UnsupportedOperationException("Operation not currently implemented");
	}

	@Override
	protected Map<String, Parameter<?>> defineConnectParameterInfo() {
		final Map<String, Parameter<?>> params= new HashMap<String, Parameter<?>>();
		params.put(URL.key, URL);		
		return params;
	}

	@Override
	protected Map<String, Parameter<?>> defineCreateParameterInfo() {
		final Map<String, Parameter<?>> params= new HashMap<String, Parameter<?>>();
		params.put(URL.key, URL);		
		return params;
	}

	@Override
	protected Map<String, Parameter<?>> defineDeleteParameterInfo() {
		final Map<String, Parameter<?>> params= new HashMap<String, Parameter<?>>();
		params.put(URL.key, URL);		
		return params;
	}

	public EnumSet<DriverOperation> getDriverCapabilities() {
		return EnumSet.noneOf(DriverOperation.class);
	}

	public boolean isAvailable() {
		return false;
	}

}
