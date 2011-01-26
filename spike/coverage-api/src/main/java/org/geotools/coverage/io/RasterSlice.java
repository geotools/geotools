package org.geotools.coverage.io;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.geotools.coverage.io.domain.RasterDatasetDomainManager;
import org.geotools.coverage.io.range.Band;
import org.geotools.coverage.io.request.CoverageReadRequest;
import org.geotools.coverage.io.request.CoverageRequest;
import org.geotools.coverage.io.request.CoverageResponse;
import org.geotools.coverage.io.request.CoverageRequest.RequestType;
import org.geotools.data.Parameter;
import org.opengis.util.ProgressListener;

public interface RasterSlice {

	/**
	 * Retrieves a {@link RangeManager} instance which can be used to describe the
	 * codomain for the underlying coverage.
	 * 
	 * @param listener
	 * @return a {@link RangeManager} instance which can be used to describe the
	 * 			codomain for the underlying coverage.
	 * @throws IOException in case something bad occurs
	 */
	public List<Band> getBands(final ProgressListener listener) throws IOException;
	
	public List<Band> getRangeSubset(final List<Band.BandKey>bands,final ProgressListener listener) throws IOException;

	public RasterDatasetDomainManager getDomainManager(final ProgressListener listener) throws IOException;

	public RasterDataset getRasterDataset();
	
	
	/**
	 * Obtain a {@link CoverageResponse} from this {@link RasterDataset} given a specified {@link DefaultCoverageRequest}.
	 * 
	 * @param request the input {@link DefaultCoverageRequest}.
	 * @param listener
	 * @return
	 * @throws IOException
	 */
	public <T extends CoverageRequest> CoverageResponse performRequest(final T request,final ProgressListener listener) throws IOException;

	/**
	 * Describes the required (and optional) parameters that
	 * can be passed to the {@link #update(CoverageReadRequest, ProgressListener)} method.
	 * <p>
	 * @return Param a {@link Map} describing the {@link Map} for {@link #update(CoverageReadRequest, ProgressListener)}.
	 */
	public Map<String, Parameter<?>> getDefaultParameterInfo(RequestType requestType);

	/**
	 * Closes this {@link RasterDataset} and releases any lock or cached information it holds.
	 * 
	 * <p>
	 * Once a {@link RasterStorage} has been disposed it can be seen as being in unspecified state, 
	 * hence calling a method on it may have unpredictable results.
	 */
	public void close();
}
