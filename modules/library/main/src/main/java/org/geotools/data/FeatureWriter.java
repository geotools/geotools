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

import java.io.Closeable;
import java.io.IOException;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * Provides the ability to write Features information.
 *
 * <p>Capabilities:
 *
 * <ul>
 *   <li>Similar API to FeatureReader
 *   <li>After aquiring a feature using next() you may call remove() or after modification write().
 *       If you do not call one of these two methods before calling hasNext(), or next() for that
 *       matter, the feature will be left unmodified.
 *   <li>This API allows modification, and Filter based modification to be written. Please see
 *       ContentDataStore for examples of implementing common opperations using this API.
 *   <li>In order to add new Features, FeatureWriters capable of accepting new content allow next()
 *       to be called when hasNext() is <code>false</code> to allow new feature creation. These
 *       changes
 * </ul>
 *
 * <p>One thing that is really nice about the approach to adding content is that the generation of
 * FID is not left in the users control.
 *
 * @author Ian Schneider
 * @author Jody Garnett, Refractions Research
 * @version $Id$
 */
public interface FeatureWriter<T extends FeatureType, F extends Feature> extends Closeable {
    /**
     * FeatureType this reader has been configured to create.
     *
     * @return FeatureType this writer has been configured to create.
     */
    T getFeatureType();

    /**
     * Reads a Feature from the underlying AttributeReader.
     *
     * <p>This method may return a Feature even though hasNext() returns <code>false</code>, this
     * allows FeatureWriters to provide an ability to append content.
     *
     * @return Feature from Query, or newly appended Feature
     * @throws IOException if the writer has been closed or an I/O error occurs reading the next
     *     <code>Feature</code>.
     */
    F next() throws IOException;

    /**
     * Removes current Feature, must be called before hasNext.
     *
     * <p>FeatureWriters will need to allow all FeatureSources of the same typeName to issue a
     * FeatureEvent event of type <code>FeatureEvent.FEATURES_REMOVED</code> when this method is
     * called.
     *
     * <p>If this FeatureWriter is opperating against a Transaction FEATURES_REMOVED events should
     * only be sent to FeatureSources operating on the same Transaction. When Transaction commit()
     * is called other FeatureSources will be informed of the modifications.
     *
     * <p>When the current Feature has been provided as new content, this method "cancels" the add
     * opperation (and notification needed).
     */
    void remove() throws IOException;

    /**
     * Writes the current Feature, must be called before hasNext.
     *
     * <p>FeautreWriters will need to allow FeatureSources of the same typeName to issue a
     * FeatureEvent:
     *
     * <ul>
     *   <li>FeatureEvent.FEATURES_ADDED: when next() has been called with hasNext() equal to <code>
     *       false</code>.
     *   <li>FeatureEvent.FEATURES_MODIFIED: when next has been called with hasNext() equal to
     *       <code>true</code> and the resulting Feature has indeed been modified.
     * </ul>
     *
     * <p>If this FeatureWriter is opperating against a Transaction the FEATURES_MODIFIED or
     * FEATURES_ADDED events should only be sent to FeatureSources opperating on the same
     * Transaction. When Transaction commit() is called other FeatureSources will be informed of the
     * modifications.
     *
     * <p>If you have not called write() when you call hasNext() or next(), no modification will
     * occur().
     */
    void write() throws IOException;

    /**
     * Query whether this FeatureWriter has another Feature.
     *
     * <p>Please note: it is more efficient to construct your FeatureWriter with a Filer (to skip
     * entries you do not want), than to force the creation of entire Features only to skip over
     * them.
     *
     * <p>FeatureWriters that support append operations will allow calls to next, even when
     * hasNext() returns <code>false</code>.
     *
     * @return <code>true</code> if an additional <code>Feature</code> is available, <code>false
     *     </code> if not.
     * @throws IOException if an I/O error occurs.
     */
    boolean hasNext() throws IOException;

    /**
     * Release the underlying resources.
     *
     * @throws IOException if there there are problems releasing underlying resources, or possibly
     *     if close has been called (up to the implementation).
     * @throws IOException if an I/O error occurs
     */
    void close() throws IOException;
}
