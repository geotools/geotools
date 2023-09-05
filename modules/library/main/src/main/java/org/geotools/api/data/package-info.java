/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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
/**
 * Defines the DataStore API via which all data is imported or exported.
 * <p>
 * All DataStores (e.g. PostGIS, Shapefile(tm), GML...) must provide
 * implementations of the DataStore interface and the DataStoreFactorySpi
 * interface.  These interfaces allow new types of datastore to be plugged into
 * applications which use GeoTools without the need to modify any code.
 * <p>
 * Example:</p>
 * <pre><code>
 * FeatureStore postgis = new PostGisDataStore( ... );
 * Query all = Query.ALL;
 * FeatureType roadType = postgis.getFeatureType( "roads" );
 *
 * // reader 1 streams over all roads
 * FeatureReader reader1 = postgis.getFeatureReader( roadsType, all, Transaction.AUTO_COMMIT );
 *
 * // allRoads = featureResults is a prepaired query of all roads
 * FeatureSource roads = postgis.getFeatureSource( "roads" );
 * FeatureResults allRoads = roads.getFeatures( all );
 *
 * // reader 2 & 3 streams over all roads in the same manner as reader 1
 * FeatureReader reader2 = allRoads.reader();
 * FeatureReader reader3 = allRoads.reader();
 *
 * // bounds1 returns the bounding box of roads, may be null depending on expense
 * Envelope bounds1 = roads.getBounds( all );
 *
 * // bounds2 returns the bounding box of roads, may actually calculate by going over the entire dataset
 * Envelope bounds 2 = allRoads.getBounds();
 * </code></pre>
 */
package org.geotools.api.data;
