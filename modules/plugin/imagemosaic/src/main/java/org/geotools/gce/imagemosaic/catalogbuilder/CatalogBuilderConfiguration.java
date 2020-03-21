/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2013, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;
import java.util.Objects;
import org.apache.commons.beanutils.BeanUtils;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType.Parameter;
import org.geotools.gce.imagemosaic.catalog.index.SchemaType;
import org.geotools.gce.imagemosaic.catalog.index.SchemasType;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;

/**
 * Simple bean that conveys the information needed by the CatalogBuilder to create a catalogue of
 * granules
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class CatalogBuilderConfiguration implements Cloneable {

    private Hints hints;

    private String timeAttribute;

    private String runtimeAttribute;

    private Indexer indexer;

    public CatalogBuilderConfiguration() {
        initDefaultsParam();
    }

    private void initDefaultsParam() {
        this.indexer = IndexerUtils.createDefaultIndexer();
    }

    public CatalogBuilderConfiguration(final CatalogBuilderConfiguration that) {
        Utilities.ensureNonNull("CatalogBuilderConfiguration", that);
        initDefaultsParam();
        try {
            BeanUtils.copyProperties(this, that);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /** @return the hints */
    public Hints getHints() {
        return hints;
    }

    /** @param hints the hints to set */
    public void setHints(Hints hints) {
        this.hints = hints;
    }

    /** Get the schema with the specified name */
    public String getSchema(String name) {
        // return schema;
        SchemasType schemas = indexer.getSchemas();
        if (schemas != null) {
            List<SchemaType> schemaList = schemas.getSchema();
            for (SchemaType schema : schemaList) {
                if (schema.getName().equalsIgnoreCase(name)) {
                    return schema.getAttributes();
                }
            }
        }
        return null;
    }

    /** Set the indexer parameter */
    public void setParameter(String parameterName, String parameterValue) {
        List<Parameter> params = indexer.getParameters().getParameter();
        parameterValue = IndexerUtils.refineParameterValue(parameterName, parameterValue);
        for (Parameter param : params) {
            if (param.getName().equalsIgnoreCase(parameterName)) {
                param.setValue(parameterValue);
                return;
            }
        }
        Parameter param = Utils.OBJECT_FACTORY.createParametersTypeParameter();
        param.setName(parameterName);
        param.setValue(parameterValue);
        params.add(param);
    }

    public String getParameter(String parameterName) {
        return IndexerUtils.getParameter(parameterName, indexer);
    }

    public String getTimeAttribute() {
        return timeAttribute;
    }

    public Indexer getIndexer() {
        return indexer;
    }

    public void setIndexer(Indexer indexer) {
        this.indexer = indexer;
    }

    public String getRuntimeAttribute() {
        return runtimeAttribute;
    }

    public void setRuntimeAttribute(String runtimeAttribute) {
        this.runtimeAttribute = runtimeAttribute;
    }

    @Override
    public CatalogBuilderConfiguration clone() throws CloneNotSupportedException {
        return new CatalogBuilderConfiguration(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CatalogBuilderConfiguration)) return false;
        final CatalogBuilderConfiguration that = (CatalogBuilderConfiguration) obj;
        if (!equalsParameter(this, that, Prop.ABSOLUTE_PATH)) return false;

        if (!equalsParameter(this, that, Prop.CACHING)) return false;
        if (!equalsParameter(this, that, Prop.RECURSIVE)) return false;
        if (!equalsParameter(this, that, Prop.FOOTPRINT_MANAGEMENT)) return false;
        if (!equalsParameter(this, that, Prop.INDEX_NAME)) return false;
        if (!equalsParameter(this, that, Prop.LOCATION_ATTRIBUTE)) return false;
        if (!equalsParameter(this, that, Prop.ROOT_MOSAIC_DIR)) return false;
        return true;
    }

    private static boolean equalsParameter(
            CatalogBuilderConfiguration thisConfig,
            CatalogBuilderConfiguration thatConfig,
            String parameterName) {
        String thisValue = thisConfig.getParameter(parameterName);
        String thatValue = thatConfig.getParameter(parameterName);
        if (!(thisValue == null && thatValue == null) && !Objects.equals(thisValue, thatValue)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int seed = 37;
        seed = Utilities.hash(Boolean.parseBoolean(getParameter(Prop.ABSOLUTE_PATH)), seed);
        seed = Utilities.hash(Boolean.parseBoolean(getParameter(Prop.RECURSIVE)), seed);
        seed = Utilities.hash(Boolean.parseBoolean(getParameter(Prop.CACHING)), seed);
        seed = Utilities.hash(Boolean.parseBoolean(getParameter(Prop.FOOTPRINT_MANAGEMENT)), seed);
        seed = Utilities.hash(getParameter(Prop.LOCATION_ATTRIBUTE), seed);
        seed = Utilities.hash(getParameter(Prop.INDEX_NAME), seed);
        seed = Utilities.hash(getParameter(Prop.WILDCARD), seed);
        seed = Utilities.hash(getParameter(Prop.ROOT_MOSAIC_DIR), seed);
        return seed;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("CatalogBuilderConfiguration").append("\n");
        builder.append("wildcardString:\t\t\t").append(getParameter(Prop.WILDCARD)).append("\n");
        builder.append("indexName:\t\t\t").append(getParameter(Prop.INDEX_NAME)).append("\n");
        builder.append("absolute:\t\t\t")
                .append(Boolean.parseBoolean(getParameter(Prop.ABSOLUTE_PATH)))
                .append("\n");
        builder.append("caching:\t\t\t")
                .append(Boolean.parseBoolean(getParameter(Prop.CACHING)))
                .append("\n");
        builder.append("recursive:\t\t\t")
                .append(Boolean.parseBoolean(getParameter(Prop.RECURSIVE)))
                .append("\n");
        builder.append("footprintManagement:\t\t\t")
                .append(Boolean.parseBoolean(getParameter(Prop.FOOTPRINT_MANAGEMENT)))
                .append("\n");
        builder.append("locationAttribute:\t\t\t")
                .append(getParameter(Prop.LOCATION_ATTRIBUTE))
                .append("\n");
        builder.append("rootMosaicDirectory:\t\t\t")
                .append(getParameter(Prop.ROOT_MOSAIC_DIR))
                .append("\n");
        return builder.toString();
    }

    public void check() {
        // check parameters

        // Check the indexing directories
        String indexingDirs = getParameter(Prop.INDEXING_DIRECTORIES);
        if (indexingDirs == null) {

            // check whether we are on harvesting so check the Harvest directory param.
            String customDirs = getParameter(Prop.HARVEST_DIRECTORY);
            indexingDirs = customDirs;
        }
        if (indexingDirs == null) {
            throw new IllegalStateException("Indexing directories are empty");
        } else {
            String[] indexingDirectoriesString = indexingDirs.split("\\s*,\\s*");
            if (indexingDirectoriesString == null || indexingDirectoriesString.length <= 0)
                throw new IllegalStateException("Indexing directories are empty");
            // final List<String> directories = new ArrayList<String>();
            // for (String dir : indexingDirectoriesString)
            // directories.add(Utils.checkDirectory(dir,false));
            // indexingDirectories = directories;
        }
        String indexName = getParameter(Prop.INDEX_NAME);
        if (indexName == null || indexName.length() == 0)
            throw new IllegalStateException("Index name cannot be empty");

        // Check the root mosaic directory
        String rootMosaicDirectory = getParameter(Prop.ROOT_MOSAIC_DIR);
        if (rootMosaicDirectory == null || rootMosaicDirectory.length() == 0)
            throw new IllegalStateException("RootMosaicDirectory name cannot be empty");

        rootMosaicDirectory = Utils.checkDirectory(rootMosaicDirectory, true);
        String wildcard = getParameter(Prop.WILDCARD);
        if (wildcard == null || wildcard.length() == 0)
            throw new IllegalStateException("WildcardString name cannot be empty");
    }
}
