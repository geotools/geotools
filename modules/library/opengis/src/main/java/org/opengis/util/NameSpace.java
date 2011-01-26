/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.util;

import java.util.Set;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A domain in which {@linkplain GenericName names} given by character strings are defined.
 *
 * @author Bryce Nordgren (USDA)
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.1
 */
@UML(identifier="NameSpace", specification=ISO_19103)
public interface NameSpace {
    /**
     * Indicates whether this namespace is a "top level" namespace.  Global, or top-level
     * namespaces are not contained within another namespace.  There is no namespace called
     * "global" or "root" which contains all of the top-level namespaces.  Hence, this flag
     * indicates whether the namespace has a parent.
     *
     * @return {@code true} if this namespace has no parent.
     */
    @UML(identifier="isGlobal", obligation=MANDATORY, specification=ISO_19103)
    boolean isGlobal();

    /**
     * Represents the identifier of this namespace. If the {@linkplain #isGlobal global} attribute is
     * {@code true}, indicating that this is a top level {@code NameSpace}, then the name should be a
     * {@linkplain LocalName local name}. If {@code false}, name should be a fully-qualified name where
     * <code>name.{@linkplain GenericName#scope() scope()}.{@linkplain #isGlobal} == true</code>.
     *
     * @return The identifier of this namespace.
     */
    @UML(identifier="name", obligation=MANDATORY, specification=ISO_19103)
    GenericName name();

    /**
     * Returns the set of {@linkplain GenericName generic names} registered with this namespace.
     * Duplicate names are forbidden. The names may be either:
     * <p>
     * <ul>
     *   <li>A {@link LocalName}.</li>
     *   <li>A {@link ScopedName} with the following constraints:
     *     <ul>
     *       <li>All elements of the {@linkplain ScopedName#getParsedNames parsed names list} except
     *           for the {@linkplain ScopedName#tail tail} must refer to a {@code NameSpace}.</li>
     *       <li>Each element of the {@linkplain ScopedName#getParsedNames parsed names list} except
     *           for the {@linkplain ScopedName#head head} must be defined in the {@code NameSpace}
     *           referred to by the previous element.</li>
     *     </ul></li>
     * </ul>
     *
     * @return All generic names registered with this namespace.
     *
     * @todo This method will put a significant burden on implementations (they will need to manage
     *       a list of names, probably through weak references, etc.). Is the ISO 19103 association
     *       really naviguable that way?
     *
     * @deprecated Not implementable in a simple naming system. We may create a {@code Register}
     *             subclass (or something similar) later for this kind of job.
     */
    @Deprecated
    @UML(identifier="names", obligation=MANDATORY, specification=ISO_19103)
    Set<GenericName> getNames();
}
