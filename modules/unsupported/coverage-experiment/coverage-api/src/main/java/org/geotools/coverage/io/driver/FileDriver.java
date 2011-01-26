package org.geotools.coverage.io.driver;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.factory.Hints;
import org.opengis.util.ProgressListener;


public interface FileDriver extends Driver {

	/**
	 * The list of filename extensions handled by this driver.
	 * <p>
	 * This List may be empty if the Driver is not file based.
	 * <p>
	 * 
	 * @return List of file extensions which can be read by this dataStore.
	 */
	public List<String> getFileExtensions();

	public boolean canProcess(DriverOperation operation,URL url,Map<String, Serializable> params);

	public CoverageAccess process(DriverOperation operation,URL url, Map<String, Serializable> params,Hints hints, final ProgressListener listener)throws IOException;

}
