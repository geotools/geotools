package org.geotools.coverage.io;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.geotools.coverage.io.metadata.MetadataNode;
import org.opengis.feature.type.Name;
import org.opengis.util.ProgressListener;

public interface RasterDatasetWriter extends RasterDatasetReader{

	public void setStoreMetadata(String metadataDomain, MetadataNode root);
	
	public boolean remove(Name rasterDatasetName, Map<String, Serializable> params)throws IOException;
	public boolean canRemove(Name rasterDatasetName, Map<String, Serializable> params)throws IOException;
	
	public boolean remove(int index, Map<String, Serializable> params)throws IOException;	
	public boolean canRemove(int index, Map<String, Serializable> params)throws IOException;
	/**
	 * Delete entirely the underlying storage
	 * 
	 * @param params
	 * @param progress
	 * @return
	 * @throws IOException
	 */
	public boolean delete( Map<String, Serializable> params, ProgressListener progress)throws IOException;
	public boolean canDelete( Map<String, Serializable> params, ProgressListener progress)throws IOException;
	
	public boolean insert(int index, Map<String, Serializable> params, RasterDataset rasterDataset,ProgressListener progress)throws IOException;
	public boolean canInsert(int index, Map<String, Serializable> params, RasterDataset rasterDataset,ProgressListener progress)throws IOException;
	/**
	 * Perform an append to the underlying storage.
	 * 
	 * @param params
	 * @param rasterDataset
	 * @param progress
	 * @return
	 * @throws IOException
	 */
	public boolean insert( Map<String, Serializable> params, RasterDataset rasterDataset,ProgressListener progress)throws IOException;
	public boolean canInsert( Map<String, Serializable> params, RasterDataset rasterDataset,ProgressListener progress)throws IOException;
	
	public boolean updateMetadata(Name rasterDatasetName, Map<String, Serializable> params, Map<String,MetadataNode> metadataDomains,ProgressListener progress)throws IOException;	
	public boolean canUpdateMetadata(Name rasterDatasetName, Map<String, Serializable> params, Map<String,MetadataNode> metadataDomains,ProgressListener progress)throws IOException;
	
	public boolean updateData(Name rasterDatasetName, Map<String, Serializable> params, RasterDataset rasterDataset,ProgressListener progress)throws IOException;
	public boolean canUpdateData(Name rasterDatasetName, Map<String, Serializable> params, RasterDataset rasterDataset,ProgressListener progress)throws IOException;
	
	public boolean update(Name rasterDatasetName, Map<String, Serializable> params, RasterDataset rasterDataset,ProgressListener progress)throws IOException;
	public boolean canUpdate(Name rasterDatasetName, Map<String, Serializable> params, RasterDataset rasterDataset,ProgressListener progress)throws IOException;

}
