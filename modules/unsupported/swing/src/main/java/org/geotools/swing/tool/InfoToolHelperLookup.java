/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import org.geotools.map.Layer;

/**
 * A lookup facility for {@code InfoToolHelper} classes.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $URL$
 */
class InfoToolHelperLookup {
    private static List<InfoToolHelper> cachedInstances;

    public static InfoToolHelper getHelper(Layer layer) {
        loadProviders();

        for (InfoToolHelper helper : cachedInstances) {
            try {
                if (helper.isSupportedLayer(layer)) {
                    return helper.getClass().getDeclaredConstructor().newInstance();
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return null;
    }

    /** Caches available classes which implement the InfoToolHelper SPI. */
    private static void loadProviders() {
        if (cachedInstances == null) {
            List<InfoToolHelper> instances = new ArrayList<InfoToolHelper>();

            ServiceLoader<InfoToolHelper> loader = ServiceLoader.load(InfoToolHelper.class);

            Iterator<InfoToolHelper> iter = loader.iterator();
            while (iter.hasNext()) {
                instances.add(iter.next());
            }
            cachedInstances = instances;
        }
    }
}
