/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.protocol.wfs;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.spi.ServiceRegistry;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;
import org.geotools.factory.FactoryNotFoundException;

/**
 * Utility class to look up for a parser that can deal with a given WFS response and process it.
 * <p>
 * This class uses the usual GeoTools SPI (Service Provider Interface) mechanism to find out a
 * {@link WFSResponseParserFactory} for a given {@link WFSResponse}. As such,
 * {@link WFSResponseParserFactory} implementation may live outside this plugin as long as they're
 * declared in it's own {code
 * /META-INF/services/org.geotools.data.wfs.protocol.wfs.WFSResponseParserFactory} text file.
 * </p>
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id: WFSExtensions.java 35131 2010-03-29 13:43:06Z groldan $
 * @since 2.6
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/data/wfs/protocol/wfs/WFSExtensions.java $
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/wfs/src/main/java/org/geotools
 *         /data/wfs/protocol/wfs/WFSExtensions.java $
 */
@SuppressWarnings("nls")
public class WFSExtensions {
    /**
     * The service registry for this manager. Will be initialized only when first needed.
     */
    private static Set<WFSResponseParserFactory> registry;

    /**
     * Processes the result of a WFS operation and returns the parsed object.
     * <p>
     * The result can either be:
     * <ul>
     * <li>a {@link WFSException} exception if the WFS response was an exception report
     * <li>a {@link GetFeatureParser} if the WFS returned a FeatureCollection
     * </p>
     * 
     * @param request
     *            the WFS request that originated the given response
     * @param response
     *            the handle to the WFS response contents
     * @return
     * @throws IOException
     */
    public static Object process(WFS_1_1_0_DataStore wfs, WFSResponse response) throws IOException {

        EObject originatingRequest = response.getOriginatingRequest();
        WFSResponseParserFactory pf = findParserFactory(originatingRequest);

        WFSResponseParser parser = pf.createParser(wfs, response);

        Object result = parser.parse(wfs, response);
        return result;
    }

    /**
     * @param requestType
     * @param outputFormat
     * @return
     * @throws FactoryNotFoundException
     */
    static WFSResponseParserFactory findParserFactory(EObject request) {
        Iterator<WFSResponseParserFactory> serviceProviders;
        serviceProviders = getServiceProviders();

        WFSResponseParserFactory factory;
        while (serviceProviders.hasNext()) {
            factory = serviceProviders.next();
            if (factory.isAvailable()) {
                if (factory.canProcess(request)) {
                    return factory;
                }
            }
        }
        throw new FactoryNotFoundException("Can't find a response parser factory for " + request);
    }

    private static Iterator<WFSResponseParserFactory> getServiceProviders() {
        if (registry == null) {
            synchronized (WFSExtensions.class) {
                if (registry == null) {
                    /*
                     * Set the current thread's class loader to the one that actually loaded the
                     * WDSDataStore and related classes for while the factory lookup is performed.
                     * This way the module is friendlier to crazy class loader hierarchies like
                     * OSGI/Eclipse
                     */
                    final ClassLoader current = Thread.currentThread().getContextClassLoader();
                    try {
                        final ClassLoader tempClassLoader = WFSDataStore.class.getClassLoader();
                        Thread.currentThread().setContextClassLoader(tempClassLoader);
                        /*
                         * Now that we're on the correct classloader lets perform the lookup
                         */
                        Iterator<WFSResponseParserFactory> providers;
                        providers = ServiceRegistry.lookupProviders(WFSResponseParserFactory.class);
                        registry = new HashSet<WFSResponseParserFactory>();
                        while (providers.hasNext()) {
                            WFSResponseParserFactory provider = providers.next();
                            registry.add(provider);
                        }
                    } finally {
                        /*
                         * And finally restore the original thread's class loader
                         */
                        Thread.currentThread().setContextClassLoader(current);
                    }
                }
            }
        }
        return registry.iterator();
    }

}
