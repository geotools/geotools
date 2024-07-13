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

package org.geotools.data.graticule;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import org.geotools.api.data.DataStore;
import org.geotools.api.feature.type.Name;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;

public class GraticuleDataStore extends ContentDataStore implements DataStore {
    static final Logger log = Logger.getLogger("GraticuleDataStore");
    final ReferencedEnvelope bounds;
    ArrayList<Double> steps;

    public GraticuleDataStore(ReferencedEnvelope env, List<Double> steps) {
        this.steps = new ArrayList<>(steps);
        Collections.sort(this.steps);
        this.bounds = env;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry contentEntry)
            throws IOException {
        return new GraticuleFeatureSource(contentEntry, steps, bounds);
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        NameImpl name = new NameImpl(this.namespaceURI, buildTypeName());
        return Collections.singletonList(name);
    }

    /** Graticule_step or Graticule_step1_stepN withouth unecessary decimals */
    private String buildTypeName() {
        StringBuilder n = new StringBuilder("Graticule_");
        DecimalFormat df = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.US));
        n.append(df.format(steps.get(0)));
        if (steps.size() > 1) {
            n.append("_").append(df.format(steps.get(steps.size() - 1)));
        }
        return n.toString();
    }
}
