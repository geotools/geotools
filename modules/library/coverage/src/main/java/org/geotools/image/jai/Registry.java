/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.jai;

import java.awt.image.renderable.ContextualRenderedImageFactory;
import java.awt.image.renderable.RenderedImageFactory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.eclipse.imagen.ImageN;
import org.eclipse.imagen.OperationDescriptor;
import org.eclipse.imagen.OperationRegistry;
import org.eclipse.imagen.ParameterBlockImageN;
import org.eclipse.imagen.registry.RIFRegistry;
import org.eclipse.imagen.registry.RenderedRegistryMode;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.AbstractGridCoverage;
import org.geotools.metadata.i18n.LoggingKeys;
import org.geotools.metadata.i18n.Loggings;
import org.geotools.util.logging.Logging;

/**
 * A set of static methods for managing JAI's {@linkplain OperationRegistry operation registry}.
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Andrea Aime - GeoSolutions
 */
public final class Registry {

    /** The JAITools product name (used to register operations in JAI) */
    public static final String JAI_TOOLS_PRODUCT = "org.jaitools.media.jai";

    /** The GeoTools product name (used to register operations in JAI) */
    public static final String GEOTOOLS_PRODUCT = "org.geotools";

    /** Do not allows instantiation of this class. */
    private Registry() {}

    /** Native acceleration is no longer supported. */
    @Deprecated
    public static synchronized void setNativeAccelerationAllowed(
            final String operation, final boolean allowed, final ImageN imagen) {
        final String product = "org.eclipse.imagen.media";
        final OperationRegistry registry = imagen.getOperationRegistry();

        // TODO: Check if we can remove SuppressWarnings with a future ImageN version.
        @SuppressWarnings("unchecked")
        final List<RenderedImageFactory> factories =
                registry.getOrderedFactoryList(RenderedRegistryMode.MODE_NAME, operation, product);
        if (factories != null) {
            RenderedImageFactory javaFactory = null;
            RenderedImageFactory nativeFactory = null;
            Boolean currentState = null;
            for (final RenderedImageFactory factory : factories) {
                final String pack = factory.getClass().getPackage().getName();
                if (pack.equals("org.eclipse.imagen.media.mlib")) {
                    nativeFactory = factory;
                    if (javaFactory != null) {
                        currentState = Boolean.FALSE;
                    }
                }
                if (pack.equals("org.eclipse.imagen.media.opimage")) {
                    javaFactory = factory;
                    if (nativeFactory != null) {
                        currentState = Boolean.TRUE;
                    }
                }
            }
            if (currentState != null && currentState.booleanValue() != allowed) {
                RIFRegistry.unsetPreference(
                        registry,
                        operation,
                        product,
                        allowed ? javaFactory : nativeFactory,
                        allowed ? nativeFactory : javaFactory);
                RIFRegistry.setPreference(
                        registry,
                        operation,
                        product,
                        allowed ? nativeFactory : javaFactory,
                        allowed ? javaFactory : nativeFactory);
                final LogRecord record = Loggings.format(
                        Level.CONFIG,
                        LoggingKeys.NATIVE_ACCELERATION_STATE_$2,
                        operation,
                        Integer.valueOf(allowed ? 1 : 0));
                log("setNativeAccelerationAllowed", record);
            }
        }
    }

    /** Native acceleration is no longer supported. */
    @Deprecated
    public static void setNativeAccelerationAllowed(final String operation, final boolean allowed) {
        setNativeAccelerationAllowed(operation, allowed, ImageN.getDefaultInstance());
    }

    /**
     * Register the "SampleTranscode" image operation to the operation registry of the specified ImageN instance. This
     * method is invoked by the static initializer of {@link GridSampleDimension}.
     *
     * @param imagen is he {@link JAI} instance in which we ant to register this operation.
     * @param descriptor is the {@link OperationDescriptor} for the ImageN operation to register.
     * @param name is the name of the operation to register.
     * @param crif is the rendered image facotry for this operation.
     * @return <code>true</code> if everything goes well, <code>false</code> otherwise.
     */
    public static boolean registerRIF(
            final ImageN imagen,
            final OperationDescriptor descriptor,
            final String name,
            final ContextualRenderedImageFactory crif) {
        final OperationRegistry registry = imagen.getOperationRegistry();
        try {
            registry.registerDescriptor(descriptor);
            registry.registerFactory(RenderedRegistryMode.MODE_NAME, name, GEOTOOLS_PRODUCT, crif);
            return true;
        } catch (IllegalArgumentException exception) {
            final LogRecord record = Loggings.format(Level.SEVERE, LoggingKeys.CANT_REGISTER_JAI_OPERATION_$1, name);
            // Note: GridSampleDimension is the public class that use this
            // transcoder.
            record.setSourceClassName(GridSampleDimension.class.getName());
            record.setSourceMethodName("<classinit>");
            record.setThrown(exception);
            record.setLoggerName(AbstractGridCoverage.LOGGER.getName());
            AbstractGridCoverage.LOGGER.log(record);
        }
        return false;
    }

    /**
     * Forcefully registers the specified rendered operation in the ImageN registry
     *
     * @return true if the registration succeded, false if the registration was not required as the operation was
     *     already available in the registry
     */
    public static boolean registerRIF(
            final ImageN imagen, OperationDescriptor descriptor, RenderedImageFactory rif, String productName) {
        final OperationRegistry registry = imagen.getOperationRegistry();
        try {
            // see if the operation is already registered, avoid registering it twice
            new ParameterBlockImageN(descriptor.getName());
            return false;
        } catch (Exception e) {
            registry.registerDescriptor(descriptor);
            final String descName = descriptor.getName();
            registry.registerFactory(RenderedRegistryMode.MODE_NAME, descName, productName, rif);
            return true;
        }
    }

    /** Logs the specified record. */
    private static void log(final String method, final LogRecord record) {
        record.setSourceClassName(Registry.class.getName());
        record.setSourceMethodName(method);
        final Logger logger = Logging.getLogger(Registry.class);
        record.setLoggerName(logger.getName());
        logger.log(record);
    }
}
