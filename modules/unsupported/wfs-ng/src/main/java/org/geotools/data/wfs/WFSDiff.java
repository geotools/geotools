/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import static org.geotools.data.wfs.internal.Loggers.MODULE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.data.Diff;
import org.geotools.api.data.FeatureReader;
import org.geotools.data.store.ContentState;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

public class WFSDiff extends Diff {

    public static class BatchUpdate {

        final Name[] properties;

        final Object[] values;

        final Filter filter;

        public BatchUpdate(Name[] properties, Object[] values, Filter filter) {
            this.properties = properties;
            this.values = values;
            this.filter = filter;
        }
    }

    // record batch modified fids so a single update element is not built for them by
    // WFSRemoteTransactionState
    private Set<String> batchModified;

    private List<BatchUpdate> batchCommands;

    public WFSDiff() {
        super();
        batchModified = new HashSet<>();
        batchCommands = new ArrayList<>();
    }

    @Override
    public synchronized void clear() {
        batchModified.clear();
        batchCommands.clear();
        super.clear();
    }

    public List<BatchUpdate> getBatchUpdates() {
        return batchCommands;
    }

    public Set<String> getBatchModified() {
        return batchModified;
    }

    @Override
    public void modify(String fid, SimpleFeature f) {
        synchronized (batchModified) {
            boolean removed = batchModified.remove(fid);
            if (removed) {
                // will cause an extra update to be sent after the batch modifications
                MODULE.finer(
                        "Removing "
                                + fid
                                + " from list of batch modified features as it's being modified directly");
            }
        }
        super.modify(fid, f);
    }

    public ReferencedEnvelope batchModify(
            Name[] properties,
            Object[] values,
            Filter filter,
            FeatureReader<SimpleFeatureType, SimpleFeature> oldFeatures,
            ContentState contentState)
            throws IOException {

        ReferencedEnvelope bounds =
                new ReferencedEnvelope(
                        contentState.getFeatureType().getCoordinateReferenceSystem());

        synchronized (batchModified) {
            while (oldFeatures.hasNext()) {

                SimpleFeature old = oldFeatures.next();
                BoundingBox fbounds = old.getBounds();
                if (fbounds != null) {
                    bounds.expandToInclude(new ReferencedEnvelope(fbounds));
                }

                for (int i = 0; i < properties.length; i++) {
                    Name propName = properties[i];
                    Object newValue = values[i];
                    old.setAttribute(propName, newValue);
                    if (newValue instanceof Geometry) {
                        Envelope envelope = ((Geometry) newValue).getEnvelopeInternal();
                        bounds.expandToInclude(envelope);
                    }
                }

                String fid = old.getID();
                super.modify(fid, old);
                batchModified.add(fid);
            }

            BatchUpdate batch = new BatchUpdate(properties, values, filter);
            batchCommands.add(batch);
        }

        return bounds;
    }
}
