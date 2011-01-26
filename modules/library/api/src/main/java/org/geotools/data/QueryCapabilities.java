/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import org.geotools.factory.Hints;
import org.opengis.feature.Feature;
import org.opengis.filter.sort.SortBy;

/**
 * Describes the query capabilities for a specific FeatureType, so client code can request which
 * features are natively supported by a FeatureSource.
 * <p>
 * This is the minimal Query capabilities we could come up in order to reliably support paging. Yet,
 * the need for a more complete set of capabilities is well known and a new proposal should be done
 * in order to define the complete set of capabilities a FeatureSource should advertise.
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/library/api/src/main/java/org/geotools/data/QueryCapabilities.java $
 */
public class QueryCapabilities {

    /**
     * Returns the list of filters natively supported by the underlaying storage. Every other filter
     * will be emulated in memory (and thus will not enjoy any acceleration).
     */
    // GR: commenting out by now as its being an unexpected scope increase,
    // gonna add it back once I have the paging use case handled
    // FilterCapabilities getFilterCapabilities();
    /**
     * True if this feature source supports reprojection
     */
    // public boolean isReprojectionSupported();
    /**
     *
     */
    // public boolean isCRSForcingSupported();
    /**
     * Is offset supported. A value of true implies ability to have a consistent sort order. At
     * least {@link SortBy#NATURAL_ORDER} shall be supported, and be the default order if a Query
     * with offset but no SortBy is issued.
     */
    public boolean isOffsetSupported() {
        return false;
    }

    /**
     * Returns whether a list of properties can be used as SortBy keys.
     * <p>
     * May include current feature type properties as well as <code>"@id"</code> for sorting on
     * the Feature ID. Note, however, that ability to sort by the fature id does not necessarily
     * implies the same ordering than SortBy.NATURAL_ORDER, though its probable they match for
     * datastores where the feature id is built up from a primary key.
     * </p>
     * <p>
     * Returns true if passed a null or empty array, otherwise the actual attributes are checked.
     * When the array is not null and not empty, by default returns false.
     * FeatureSource implementations should override as needed.
     * </p>
     * 
     * @return whether the FeatureType this query capabilities refers to can be natively sorted by
     *         the provided list of attribtue/order pairs
     */
    public boolean supportsSorting(SortBy[] sortAttributes) {
        return (sortAttributes == null) || (sortAttributes.length == 0);
    }
    
    /**
     * Returns whether the feature source is capable of producing "reliable" fids.
     * <p>
     * In this content the term "reliable" refers to the ability to read the same feature
     * twice (with no transactions against the feature source in the interim) and get the 
     * same feature id back both times.
     * </p>
     * @return True to indicate reliable fids are supported, otherwise false.
     */
    public boolean isReliableFIDSupported() {
        return true;
    }
    
    /**
     * If true the datastore supports using the provided feature id in the data insertion
     * workflow as opposed to generating a new id. In that case it will look into the user data 
     * map ({@link Feature#getUserData()}) for a {@link Hints#USE_PROVIDED_FID} key associated to a
     * {@link Boolean#TRUE} value, if the key/value pair is there an attempt to use the provided 
     * id will be made, and the operation will fail of the key cannot be parsed into a valid 
     * storage identifier.
     * @return
     */
    public boolean isUseProvidedFIDSupported() {
        return false;
    }
}
