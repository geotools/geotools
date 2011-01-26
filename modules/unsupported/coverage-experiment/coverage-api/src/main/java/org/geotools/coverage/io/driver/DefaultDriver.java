package org.geotools.coverage.io.driver;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.opengis.util.ProgressListener;

public class DefaultDriver extends BaseDriver {

	protected DefaultDriver(String name, String description, String title,
			Hints implementationHints) {
		super(name, description, title, implementationHints);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean canConnect(Map<String, Serializable> params) {
		return false;
	}

	@Override
	protected boolean canCreate(Map<String, Serializable> params) {
		return false;
	}

	@Override
	protected boolean canDelete(Map<String, Serializable> params) {
		return false;
	}

	@Override
	protected CoverageAccess connect(Map<String, Serializable> params, Hints hints,
			ProgressListener listener) throws IOException {

		throw new UnsupportedOperationException("Operation not currently implemented");
	}

	@Override
	protected CoverageAccess create(Map<String, Serializable> params, Hints hints,
			ProgressListener listener) throws IOException {
		throw new UnsupportedOperationException("Operation not currently implemented");
	}

	@Override
	protected Map<String, Parameter<?>> defineConnectParameterInfo() {
		return Collections.emptyMap();
	}

	@Override
	protected Map<String, Parameter<?>> defineCreateParameterInfo() {
		return Collections.emptyMap();
	}

	@Override
	protected Map<String, Parameter<?>> defineDeleteParameterInfo() {
		return Collections.emptyMap();
	}

	@Override
	protected CoverageAccess delete(Map<String, Serializable> params, Hints hints,
			ProgressListener listener) throws IOException {
		throw new UnsupportedOperationException("Operation not currently implemented");
	}

	public EnumSet<DriverOperation> getDriverCapabilities() {
		return EnumSet.noneOf(DriverOperation.class);
	}

	public boolean isAvailable() {
		return false;
	}

}
