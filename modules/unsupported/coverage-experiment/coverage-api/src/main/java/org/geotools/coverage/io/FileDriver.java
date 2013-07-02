package org.geotools.coverage.io;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.geotools.factory.Hints;
import org.opengis.util.ProgressListener;


/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @source $URL$
 */
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

	public boolean canProcess(DriverCapabilities operation,URL url,Map<String, Serializable> params);

	public CoverageAccess process(DriverCapabilities operation,URL url, Map<String, Serializable> params,Hints hints, final ProgressListener listener)throws IOException;

}
