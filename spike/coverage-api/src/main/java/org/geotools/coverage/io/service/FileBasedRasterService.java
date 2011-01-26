package org.geotools.coverage.io.service;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.geotools.coverage.io.RasterDatasetReader;
import org.geotools.factory.Hints;
import org.opengis.util.ProgressListener;

public interface FileBasedRasterService extends RasterService {

	/**
	 * The list of filename extensions handled by this driver.
	 * <p>
	 * This List may be empty if the RasterService is not file based.
	 * <p>
	 * 
	 * @return List of file extensions which can be read by this dataStore.
	 */
	public List<String> getFileExtensions();
	
    public RasterDatasetReader createReader(final URL url,final Map<String, Serializable>  parameters, Hints hints,ProgressListener listener)throws IOException;
    
    public RasterDatasetReader createWriter(final URL url,final Map<String, Serializable>  parameters, Hints hints,ProgressListener listener)throws IOException;
    
    /**
     * TODO Improve me
     * 
     * @param parameters
     * @param hints
     * @return
     * @throws IOException
     */
    public boolean canCreateReader(final URL url,final Map<String, Serializable>  parameters, Hints hints)throws IOException;

    public boolean canCreateWriter(final URL url,final Map<String, Serializable>  parameters, Hints hints)throws IOException;
    

}
