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
package org.geotools.factory;

import java.util.Map;
import java.awt.RenderingHints;


/**
 * Base interface for Geotools factories (i.e. service discovery).
 *
 * <p>This interfaces forms the core of the Geotools plug-in system, by which capabilities
 * can be added to the library at runtime. Each sub-interface defines a <cite>service</cite>.
 * Most services are set up with concrete implementation being registered for use in
 * a <cite>service registry</cite>, which acts as a container for service implementations.</p>
 *
 * <p>Service registries don't need to be a Geotools implementation. They can be (but are not
 * limited to) any {@link javax.imageio.spi.ServiceRegistry} subclass. If the standard
 * {@code ServiceRegistry} (or its Geotools extension {@link FactoryRegistry}) is selected
 * as a container for services, then factory implementations should be declared as below
 * (select only one way):</p>
 *
 * <ul>
 *   <li><strong>Register for automatic discovery</strong></li>
 *   <ul>
 *     <li>Provide a public no-arguments constructor.</li>
 *     <li>Add the fully qualified name of the <u>implementation</u> class in the
 *         {@code META-INF/services/}<var>classname</var> file where <var>classname</var>
 *         is the fully qualified name of the service <u>interface</u>.</li>
 *     <li>The factory implementations will be discovered when
 *         {@link FactoryRegistry#scanForPlugins} will be invoked.</li>
 *   </ul>
 *   <li><strong><u>Or</u> register explicitly by application code</strong></li>
 *   <ul>
 *     <li>Invoke {@link ServiceRegistry#registerServiceProvider} in application code.</li>
 *   </ul>
 * </ul>
 *
 * <p>In addition, it is recommended that implementations provide a constructor expecting
 * a single {@link Hints} argument. This optional argument gives to the user some control
 * of the factory's low-level details. The amount of control is factory specific. The geotools
 * library defines a global class called {@link Hints} that is ment as API (i.e. you can assume
 * these hints are supported). Factories may also provide information on their own custom hints
 * as part of their javadoc class description.</p>
 *
 * <strong>Examples:</strong>
 * <ul>
 *   <li><p>An application supplied a {@linkplain Hints#DATUM_FACTORY datum factory hint}, being
 *   passed to a {@linkplain org.opengis.referencing.datum.DatumAuthorityFactory datum authority
 *   factory} so that all datum created from an authority code will come from the supplied datum
 *   factory.</p></li>
 *
 *   <li><p>An application supplied a {@link org.geotools.feature.FeatureFactory} (ensuring all
 *   constructed features support the Eclipse's {@code IAdaptable} interface), being passed to a
 *   {@link org.geotools.feature.FeatureTypeFactory} so that all {@code FeatureTypes}
 *   constructed will produce features supporting the indicated interface.<p></li>
 * </ul>
 *
 * <p>As seen in those examples this concept of a hint becomes more interesting when
 * the operation being controlled is discovery of other services used by the Factory.
 * By supplying appropriate hints one can chain together several factories and retarget
 * them to an application specific task.</p>
 *
 * @author Ian Schneider
 * @author Martin Desruisseaux
 * @author Jody Garnett
 *
 * @source $URL$
 * @version $Id$
 *
 * @see Hints
 * @see FactoryRegistry
 */
public interface Factory {
    /**
     * Map of  hints (maybe {@linkplain java.util.Collections#unmodifiableMap unmodifiable})
     * used by this factory to customize its use. This map is <strong>not</strong> guaranteed
     * to contains all the hints supplied by the user; it may be only a subset. Consequently,
     * hints provided here are usually not suitable for creating new factories, unless the
     * implementation make some additional garantees
     * (e.g. {@link FactoryUsingVolatileDependencies}).
     * <p>
     * The primary purpose of this method is to determine if an <strong>existing</strong>
     * factory instance can be reused for a set of user-supplied hints. This method is invoked by
     * {@link FactoryRegistry} in order to compare this factory's hints against user's hints.
     * This is dependency introspection only; {@code FactoryRegistry} <strong>never</strong>
     * invokes this method for creating new factories.
     * <p>
     * Keys are usually static constants from the {@link Hints} class, while values are
     * instances of some key-dependent class. The {@linkplain Map#keySet key set} must contains
     * at least all hints impacting functionality. While the key set may contains all hints
     * supplied by the user, it is recommended to limit the set to only the hints used by this
     * particular factory instance. A minimal set will helps {@link FactoryRegistry} to compare
     * only hints that matter and avoid the creation of unnecessary instances of this factory.
     * <p>
     * The hint values may be different than the one supplied by the user. If a user supplied a
     * hint as a {@link Class} object, this method shall replace it by the actual instance used,
     * if possible.
     * <p>
     * Implementations of this method are usually quite simple. For example if a
     * {@linkplain org.opengis.referencing.datum.DatumAuthorityFactory datum authority factory}
     * uses an ordinary {@linkplain org.opengis.referencing.datum.DatumFactory datum factory},
     * its method could be implemented as below (note that we should not check if the datum
     * factory is null, since key with null value is the expected behaviour in this case).
     * Example:
     *
     * <pre><code>
     * Map hints = new HashMap();
     * hints.put({@linkplain Hints#DATUM_FACTORY}, datumFactory);
     * return hints;
     * </code></pre>
     *
     * @return The map of hints, or an {@linkplain java.util.Collections#EMPTY_MAP empty map}
     *         if none.
     */
    Map<RenderingHints.Key, ?> getImplementationHints();
}
