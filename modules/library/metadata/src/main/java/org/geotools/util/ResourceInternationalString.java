/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.io.Serializable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * An international string backed by a {@linkplain ResourceBundle resource bundle}. A resource
 * bundle can be a Java class or a {@linkplain java.util.Properties properties} file, one for
 * each language. The constructor expects the fully qualified class name of the base resource
 * bundle (the one used when no resource was found in the client's language). The right resource
 * bundle is loaded at runtime for the client's language by looking for a class or a
 * {@linkplain java.util.Properties properties} file with the right suffix ("{@code _en}" for
 * English or "{@code _fr}" for French). This mechanism is explained in J2SE's javadoc for the
 * {@link ResourceBundle#getBundle(String,Locale,ClassLoader) getBundle} static method.
 * <p>
 * <b>Example:</b> If a file named "{@code MyResources.properties}" exists in the package
 * "{@code org.geotools.mypackage}" and contains a line like "{@code MyKey = some value}",
 * then an international string for "{@code some value}" can be created using the following
 * code:
 *
 * <blockquote><code>
 * InternationalString value = new ResourceInternationalString(
 * "org.geotools.mypackage.MyResources", "MyKey");
 * </code></blockquote>
 *
 * The "{@code some value}" string will be localized if the required properties files exist, for
 * example "{@code MyResources_fr.properties}" for French, "{@code MyResources_it.properties}"
 * for Italian, <cite>etc.</cite>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class ResourceInternationalString extends AbstractInternationalString implements Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 6339944890723487336L;

    /**
     * The name of the resource bundle from which to fetch the string.
     */
    private final String resources;

    /**
     * The key for the resource to fetch.
     */
    private final String key;

    /**
     * Creates a new international string from the specified resource bundle and key.
     *
     * @param resources The name of the resource bundle, as a fully qualified class name.
     * @param key The key for the resource to fetch.
     */
    public ResourceInternationalString(final String resources, final String key) {
        this.resources = resources;
        this.key       = key;
        ensureNonNull("resources", resources);
        ensureNonNull("key",       key);
    }

    /**
     * Returns a string in the specified locale. If there is no string for the specified
     * {@code locale}, then this method search for a string in an other locale as
     * specified in the {@link ResourceBundle} class description.
     *
     * @param  locale The locale to look for, or {@code null} for an unlocalized version.
     * @return The string in the specified locale, or in a default locale.
     * @throws MissingResourceException is the key given to the constructor is invalid.
     */
    public String toString(Locale locale) throws MissingResourceException {
        if (locale == null) {
            // The English locale (NOT the system default) is often used
            // as the real identifier in OGC IdentifiedObject naming. If
            // a user wants a string in the system default locale, he
            // should invokes the 'toString()' method instead.
            locale = Locale.ENGLISH;
        }
        return ResourceBundle.getBundle(resources, locale).getString(key);
    }

    /**
     * Compares this international string with the specified object for equality.
     *
     * @param object The object to compare with this international string.
     * @return {@code true} if the given object is equals to this string.
     */
    @Override
    public boolean equals(final Object object) {
        if (object!=null && object.getClass().equals(getClass())) {
            final ResourceInternationalString that = (ResourceInternationalString) object;
            return Utilities.equals(this.key,       that.key) &&
                   Utilities.equals(this.resources, that.resources);
        }
        return false;
    }

    /**
     * Returns a hash code value for this international text.
     */
    @Override
    public int hashCode() {
        return (int)serialVersionUID ^ key.hashCode() ^ resources.hashCode();
    }
}
