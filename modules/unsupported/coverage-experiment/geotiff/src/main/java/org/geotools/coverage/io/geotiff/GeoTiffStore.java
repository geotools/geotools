/**
 * 
 */
package org.geotools.coverage.io.geotiff;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffException;
import org.geotools.coverage.io.CoverageCapabilities;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageStore;
import org.geotools.coverage.io.CoverageUpdateRequest;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.geotiff.GeoTiffAccess.Info;
import org.geotools.coverage.io.impl.DefaultCoverageResponseImpl;
import org.geotools.data.Parameter;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.feature.type.Name;
import org.opengis.util.ProgressListener;

/**
 * @author simone
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/geotiff/src/main/java/org/geotools/coverage/io/geotiff/GeoTiffStore.java $
 */
public class GeoTiffStore extends GeoTiffSource implements CoverageStore {

	GeoTiffStore(final GeoTiffAccess geotiff, final Name name) {
		super( geotiff, name );
		capabilities.addAll( EnumSet.of(
				CoverageCapabilities.WRITE_HORIZONTAL_DOMAIN_SUBSAMBLING,
				CoverageCapabilities.WRITE_RANGE_SUBSETTING,
				CoverageCapabilities.WRITE_SUBSAMPLING
		));		
	}
	public Map<String, Parameter<?>> getUpdateParameterInfo() {
		return Collections.emptyMap();
	}
	
	public CoverageResponse update(CoverageUpdateRequest writeRequest,
			ProgressListener progress) {
		ensureNotDisposed();
		access.globalLock.writeLock().lock();

		final DefaultCoverageResponseImpl response = new DefaultCoverageResponseImpl();
		response.setRequest(writeRequest);
		try {
			// reader
			final GeoTiffWriter writer = new GeoTiffWriter(this.access.input);

			// get the data
			final GridCoverage2D coverage = (GridCoverage2D) writeRequest.getData().iterator().next();
			writer.write(coverage, null);
			writer.dispose();
			response.addResult(coverage);
			response.setStatus(Status.SUCCESS);

			// update the access
			Info info = getInfo(null);
			info.setExtent( (GeneralEnvelope) coverage.getGridGeometry().getEnvelope() );
			info.setGeometry( coverage.getGridGeometry() );
		} catch (Throwable e) {
			response.addException(new GeoTiffException(null, "IO error", e));
		} finally {
			this.access.globalLock.writeLock().unlock();
		}

		return response;
	}

}
