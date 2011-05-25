/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptor;
import javax.media.jai.OperationRegistry;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.registry.RIFRegistry;
import javax.media.jai.registry.RenderedRegistryMode;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.AbstractGridCoverage;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.util.logging.Logging;


/**
 * A set of static methods for managing JAI's {@linkplain OperationRegistry operation registry}.
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Andrea Aime - GeoSolutions
 */
public final class Registry {
    
    /**
     * The JAITools product name (used to register operations in JAI)
     */
    public static final String JAI_TOOLS_PRODUCT = "jaitools.media.jai";
    
    /**
     * The GeoTools product name (used to register operations in JAI)
     */
    public static final String GEOTOOLS_PRODUCT = "org.geotools";
    
    /**
     * Do not allows instantiation of this class.
     */
    private Registry() {
    }

  
    /**
     * Allows or disallow native acceleration for the specified operation on the given JAI instance.
     * By default, JAI uses hardware accelerated methods when available. For example, it make use of
     * MMX instructions on Intel processors. Unfortunatly, some native method crash the Java Virtual
     * Machine under some circonstances. For example on JAI 1.1.2, the {@code "Affine"} operation on
     * an image with float data type, bilinear interpolation and an {@link javax.media.jai.ImageLayout}
     * rendering hint cause an exception in medialib native code. Disabling the native acceleration
     * (i.e using the pure Java version) is a convenient workaround until Sun fix the bug.
     * <p>
     * <strong>Implementation note:</strong> the current implementation assumes that factories for
     * native implementations are declared in the {@code com.sun.media.jai.mlib} package, while
     * factories for pure java implementations are declared in the {@code com.sun.media.jai.opimage}
     * package. It work for Sun's 1.1.2 implementation, but may change in future versions. If this
     * method doesn't recognize the package, it does nothing.
     *
     * @param operation The operation name (e.g. {@code "Affine"}).
     * @param allowed {@code false} to disallow native acceleration.
     * @param jai The instance of {@link JAI} we are going to work on. This argument can be
     *        omitted for the {@linkplain JAI#getDefaultInstance default JAI instance}.
     *
     * @see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4906854">JAI bug report 4906854</a>
     *
     * @since 2.5
     */
    public synchronized static void setNativeAccelerationAllowed(final String operation,
                                                                 final boolean  allowed,
                                                                 final JAI jai)
    {
        final String product = "com.sun.media.jai";
        final OperationRegistry registry = jai.getOperationRegistry();

        // TODO: Check if we can remove SuppressWarnings with a future JAI version.
        @SuppressWarnings("unchecked")
        final List<RenderedImageFactory> factories = registry.getOrderedFactoryList(
                RenderedRegistryMode.MODE_NAME, operation, product);
        if (factories != null) {
            RenderedImageFactory   javaFactory = null;
            RenderedImageFactory nativeFactory = null;
            Boolean               currentState = null;
            for (final RenderedImageFactory factory : factories) {
                final String pack = factory.getClass().getPackage().getName();
                if (pack.equals("com.sun.media.jai.mlib")) {
                    nativeFactory = factory;
                    if (javaFactory != null) {
                        currentState = Boolean.FALSE;
                    }
                }
                if (pack.equals("com.sun.media.jai.opimage")) {
                    javaFactory = factory;
                    if (nativeFactory != null) {
                        currentState = Boolean.TRUE;
                    }
                }
            }
            if (currentState!=null && currentState.booleanValue()!=allowed) {
                RIFRegistry.unsetPreference(registry, operation, product,
                                            allowed ? javaFactory : nativeFactory,
                                            allowed ? nativeFactory : javaFactory);
                RIFRegistry.setPreference(registry, operation, product,
                                          allowed ? nativeFactory : javaFactory,
                                          allowed ? javaFactory : nativeFactory);
                final LogRecord record = Loggings.format(Level.CONFIG,
                                                 LoggingKeys.NATIVE_ACCELERATION_STATE_$2,
                                                 operation, Integer.valueOf(allowed ? 1 : 0));
                log("setNativeAccelerationAllowed", record);
            }
        }
    }

    /**
     * Allows or disallow native acceleration for the specified operation on the
     * {@linkplain JAI#getDefaultInstance default JAI instance}. This method is
     * a shortcut for <code>{@linkplain #setNativeAccelerationAllowed(String,boolean,JAI)
     * setNativeAccelerationAllowed}(operation, allowed, JAI.getDefaultInstance())</code>.
     *
     * @see #setNativeAccelerationAllowed(String, boolean, JAI)
     */
    public static void setNativeAccelerationAllowed(final String operation, final boolean allowed) {
    	setNativeAccelerationAllowed(operation, allowed, JAI.getDefaultInstance());
    }
    
	/**
	 * Register the "SampleTranscode" image operation to the operation registry
	 * of the specified JAI instance. This method is invoked by the static
	 * initializer of {@link GridSampleDimension}.
	 * @param jai is he {@link JAI} instance in which we ant to register this operation.
	 * @param descriptor is the {@link OperationDescriptor} for the JAI operation to register.
	 * @param name is the name of the operation to register.
	 * @param crif is the rendered image facotry for this operation.
	 * @return <code>true</code> if everything goes well, <code>false</code> otherwise.
	 */
	public static boolean registerRIF(
			final JAI jai,
			final OperationDescriptor descriptor,
			final String name,
			final ContextualRenderedImageFactory crif) {
		final OperationRegistry registry = jai.getOperationRegistry();
		try {
			registry.registerDescriptor(descriptor);
			registry.registerFactory(RenderedRegistryMode.MODE_NAME,
					name, GEOTOOLS_PRODUCT,crif);
			return true;
		} catch (IllegalArgumentException exception) {
			final LogRecord record = Loggings.format(Level.SEVERE,
					LoggingKeys.CANT_REGISTER_JAI_OPERATION_$1, name);
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
     * Forcefully registers the specified rendered operation in the JAI registry
     * 
     * @param descriptor
     * @param rif
     * @param productName
     * @return true if the registration succeded, false if the registration was not required 
     *         as the operation was already available in the registry
     */
    public static boolean registerRIF(final JAI jai, OperationDescriptor descriptor,
            RenderedImageFactory rif, String productName) {
        final OperationRegistry registry = jai.getOperationRegistry();
        try {
            // see if the operation is already registered, avoid registering it twice
            new ParameterBlockJAI(descriptor.getName());
            return false;
        } catch (Exception e) {
            registry.registerDescriptor(descriptor);
            final String descName = descriptor.getName();
            registry.registerFactory(RenderedRegistryMode.MODE_NAME, descName, productName,
                    rif);
            return true;
        }
    }

    /**
     * Logs the specified record.
     */
    private static void log(final String method, final LogRecord record) {
        record.setSourceClassName(Registry.class.getName());
        record.setSourceMethodName(method);
        final Logger logger = Logging.getLogger(Registry.class);
        record.setLoggerName(logger.getName());
        logger.log(record);
    }
}
