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


/**
 * Indicates that it is legal to make a field-for-field copy of instances of implementing classes.
 * A cloneable class implements the J2SE's {@link java.lang.Cloneable} standard interface and
 * additionnaly overrides the {@link Object#clone()} method with public access.
 * <p>
 * Because the {@link Object#clone()} method has protected access, containers wanting to clone
 * theirs elements need to 1) use Java reflection (which is less efficient than standard method
 * calls), or 2) cast every elements to a specific type like {@link java.util.Date} (which may
 * require a large amount of "{@code if (x instanceof y)}" checks if arbitrary classes are
 * allowed). This {@code Cloneable} interface had a third alternative: checks only for this
 * interface instead of a list of particular cases.
 * <p>
 * Implementors of cloneable classes may consider implementing this interface, but this is not
 * mandatory. A large amount of independant classes like {@link java.util.Date} will continue to
 * ignore this interface, so no rule can be enforced anyway. However this interface may help the
 * work of containers in some case. For example a container may checks for this interface first,
 * and uses Java reflection as a fallback.
 *
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see java.lang.Cloneable
 * @see <A HREF="http://developer.java.sun.com/developer/bugParade/bugs/4098033.html">&quot;<cite>Cloneable
 *      doesn't define <code>clone()</code></cite>&quot; on Sun's bug parade</A>
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/util/Cloneable.java $
 */
public interface Cloneable extends java.lang.Cloneable {
    /**
     * Creates and returns a copy of this object.
     * The precise meaning of "copy" may depend on the class of the object.
     *
     * @return A copy of this object.
     *
     * @see Object#clone
     */
    Object clone();
}
