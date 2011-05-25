/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalogbuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.geotools.console.Option;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.util.Utilities;

/**
 * Simple bean that conveys the information needed by the CatalogBuilder to
 * create a catalogue of granules
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imagemosaic/src/main/java/org/geotools/gce/imagemosaic/catalogbuilder/CatalogBuilderConfiguration.java $
 */
public class CatalogBuilderConfiguration {

	public Hints hints;

	private String timeAttribute;

	private String elevationAttribute;

	private String runtimeAttribute;

	private boolean absolute = Utils.DEFAULT_PATH_BEHAVIOR;
	
	private boolean caching = Utils.DEFAULT_CONFIGURATION_CACHING;

	/**
	 * Index file name. Default is index.
	 */
	private String indexName = Utils.DEFAULT_INDEX_NAME;

	private String locationAttribute = Utils.DEFAULT_LOCATION_ATTRIBUTE;

	private boolean footprintManagement = Utils.DEFAULT_FOOTPRINT_MANAGEMENT;

	@Option(description = "Root directory where to place the index file", mandatory = true, name = "rootDirectory")
	private String rootMosaicDirectory;

	@Option(description = "Wildcard to use for building the index of this mosaic", mandatory = false, name = "wildcard")
	private String wildcard = Utils.DEFAULT_WILCARD;

	/**
	 * String to pass to the featuretypebuilder for building the schema for the
	 * index.
	 */
	private String schema;

	private String propertyCollectors;
	
	/**
	 * Imposed BBOX
	 */
	private String envelope2D;
	
	public String getResolutionLevels() {
		return resolutionLevels;
	}

	public void setResolutionLevels(String resolutionLevels) {
		this.resolutionLevels = resolutionLevels;
	}

	/**
	 * Imposed resolution levels
	 */
	private String resolutionLevels;

	public String getEnvelope2D() {
		return envelope2D;
	}

	public void setEnvelope2D(String bbox) {
		this.envelope2D = bbox;
	}

	public CatalogBuilderConfiguration() {
	}

	public CatalogBuilderConfiguration(final CatalogBuilderConfiguration that) {
		Utilities.ensureNonNull("CatalogBuilderConfiguration", that);
		try {
			BeanUtils.copyProperties(this, that);
		} catch (IllegalAccessException e) {
			final IllegalArgumentException iae = new IllegalArgumentException(e);
			throw iae;
		} catch (InvocationTargetException e) {
			final IllegalArgumentException iae = new IllegalArgumentException(e);
			throw iae;
		}

	}

	/**
	 * @return the hints
	 */
	public Hints getHints() {
		return hints;
	}

	/**
	 * @param hints
	 *            the hints to set
	 */
	public void setHints(Hints hints) {
		this.hints = hints;
	}

	public void setIndexingDirectories(List<String> indexingDirectories) {
		this.indexingDirectories = indexingDirectories;
	}

	private boolean recursive = Utils.DEFAULT_RECURSION_BEHAVIOR;

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public boolean isCaching() {
		return caching;
	}

	public void setCaching(boolean caching) {
		this.caching = caching;
	}

	public String getPropertyCollectors() {
		return propertyCollectors;
	}

	public void setPropertyCollectors(String propertyCollectors) {
		this.propertyCollectors = propertyCollectors;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTimeAttribute() {
		return timeAttribute;
	}

	public void setTimeAttribute(String timeAttribute) {
		this.timeAttribute = timeAttribute;
	}

	public boolean isFootprintManagement() {
		return footprintManagement;
	}

	public void setFootprintManagement(boolean footprintManagement) {
		this.footprintManagement = footprintManagement;
	}

	public String getElevationAttribute() {
		return elevationAttribute;
	}

	public void setElevationAttribute(String elevationAttribute) {
		this.elevationAttribute = elevationAttribute;
	}

	public String getRuntimeAttribute() {
		return runtimeAttribute;
	}

	public void setRuntimeAttribute(String runtimeAttribute) {
		this.runtimeAttribute = runtimeAttribute;
	}

	private List<String> indexingDirectories;

	public List<String> getIndexingDirectories() {
		return indexingDirectories;
	}

	public String getIndexName() {
		return indexName;
	}

	public String getLocationAttribute() {
		return locationAttribute;
	}

	public String getRootMosaicDirectory() {
		return rootMosaicDirectory;
	}

	public String getWildcard() {
		return wildcard;
	}

	public boolean isAbsolute() {
		return absolute;
	}

	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public void setLocationAttribute(String locationAttribute) {
		this.locationAttribute = locationAttribute;
	}

	public void setRootMosaicDirectory(final String rootMosaicDirectory) {
		Utilities.ensureNonNull("rootMosaicDirectory", rootMosaicDirectory);
		String testingDirectory = rootMosaicDirectory;
		Utils.checkDirectory(testingDirectory);
		this.rootMosaicDirectory = testingDirectory;

	}

	public void setWildcard(String wildcardString) {
		this.wildcard = wildcardString;
	}

	@Override
	public CatalogBuilderConfiguration clone()
			throws CloneNotSupportedException {
		return new CatalogBuilderConfiguration(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CatalogBuilderConfiguration))
			return false;
		final CatalogBuilderConfiguration that = (CatalogBuilderConfiguration) obj;

		if (this.absolute != that.absolute)
			return false;
		if (this.caching != that.caching)
			return false;
		if (this.recursive != that.recursive)
			return false;
		if (this.footprintManagement != that.footprintManagement)
			return false;
		if (!(this.indexName == null && that.indexName == null)
				&& !this.indexName.equals(that.indexName))
			return false;
		if (!(this.locationAttribute == null && that.locationAttribute == null)
				&& !this.locationAttribute.equals(that.locationAttribute))
			return false;
		if (!(this.rootMosaicDirectory == null && that.rootMosaicDirectory == null)
				&& !this.rootMosaicDirectory.equals(that.rootMosaicDirectory))
			return false;
		if (!Utilities.deepEquals(this.indexingDirectories,
				that.indexingDirectories))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int seed = 37;
		seed = Utilities.hash(absolute, seed);
		seed = Utilities.hash(recursive, seed);
		seed = Utilities.hash(caching, seed);
		seed = Utilities.hash(footprintManagement, seed);
		seed = Utilities.hash(locationAttribute, seed);
		seed = Utilities.hash(indexName, seed);
		seed = Utilities.hash(wildcard, seed);
		seed = Utilities.hash(rootMosaicDirectory, seed);
		seed = Utilities.hash(indexingDirectories, seed);
		return seed;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CatalogBuilderConfiguration").append("\n");
		builder.append("wildcardString:\t\t\t").append(wildcard).append("\n");
		builder.append("indexName:\t\t\t").append(indexName).append("\n");
		builder.append("absolute:\t\t\t").append(absolute).append("\n");
		builder.append("caching:\t\t\t").append(caching).append("\n");
		builder.append("recursive:\t\t\t").append(recursive).append("\n");
		builder.append("footprintManagement:\t\t\t")
				.append(footprintManagement).append("\n");
		builder.append("locationAttribute:\t\t\t").append(locationAttribute)
				.append("\n");
		builder.append("rootMosaicDirectory:\t\t\t")
				.append(rootMosaicDirectory).append("\n");
		builder.append("indexingDirectories:\t\t\t").append(
				Utilities.deepToString(indexingDirectories)).append("\n");
		return builder.toString();
	}

	public void check() throws IllegalStateException {
		// check parameters
		if (indexingDirectories == null || indexingDirectories.size() <= 0)
			throw new IllegalStateException("Indexing directories are empty");
		final List<String> directories = new ArrayList<String>();
		for (String dir : indexingDirectories)
			directories.add(Utils.checkDirectory(dir));
		indexingDirectories = directories;

		if (indexName == null || indexName.length() == 0)
			throw new IllegalStateException("Index name cannot be empty");

		if (rootMosaicDirectory == null || rootMosaicDirectory.length() == 0)
			throw new IllegalStateException(
					"RootMosaicDirectory name cannot be empty");

		rootMosaicDirectory = Utils.checkDirectory(rootMosaicDirectory);
		if (wildcard == null || wildcard.length() == 0)
			throw new IllegalStateException(
					"WildcardString name cannot be empty");

	}

}
