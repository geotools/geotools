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
package org.geotools.referencing;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.Factory;
import org.opengis.referencing.AuthorityFactory;

import org.geotools.factory.FactoryRegistry;
import org.geotools.io.TableWriter;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * Prints a list of factory. This is used for {@link ReferencingFactoryFinder#listProviders}
 * implementation only.
 *
 * @source $URL$
 * @version $Id$
 * @author Desruisseaux
 */
final class FactoryPrinter implements Comparator<Class<?>> {
    /**
     * Constructs a default instance of this printer.
     */
    public FactoryPrinter() {
    }

    /**
     * Compares two factories for order. This is used for sorting out the factories
     * before to display them.
     */
    public int compare(final Class<?> factory1, final Class<?> factory2) {
        if (false) {
            // Sort authority factory last
            final boolean isAuthority1 = AuthorityFactory.class.isAssignableFrom(factory1);
            final boolean isAuthority2 = AuthorityFactory.class.isAssignableFrom(factory2);
            if (isAuthority1 && !isAuthority2) return +1;
            if (isAuthority2 && !isAuthority1) return -1;
            return 0;
        } else {
            // Or sort by name
            return Classes.getShortName(factory1).compareToIgnoreCase(
                   Classes.getShortName(factory2));
        }
    }

    /**
     * Lists all available factory implementations in a tabular format. For each factory interface,
     * the first implementation listed is the default one. This method provides a way to check the
     * state of a system, usually for debugging purpose.
     *
     * @param  FactoryRegistry Where the factories are registered.
     * @param  out The output stream where to format the list.
     * @param  locale The locale for the list, or {@code null}.
     * @throws IOException if an error occurs while writing to {@code out}.
     */
    public void list(final FactoryRegistry registry, final Writer out, final Locale locale)
            throws IOException
    {
        /*
         * Gets the categories in some sorted order.
         */
        final List<Class<?>> categories = new ArrayList<Class<?>>();
        for (final Iterator<Class<?>> it=registry.getCategories(); it.hasNext();) {
            categories.add(it.next());
        }
        Collections.sort(categories, this);
        /*
         * Prints the table header.
         */
        final Vocabulary resources = Vocabulary.getResources(locale);
        final TableWriter table  = new TableWriter(out, TableWriter.SINGLE_VERTICAL_LINE);
        table.setMultiLinesCells(true);
        table.writeHorizontalSeparator();
        table.write(resources.getString(VocabularyKeys.FACTORY));
        table.nextColumn();
        table.write(resources.getString(VocabularyKeys.AUTHORITY));
        table.nextColumn();
        table.write(resources.getString(VocabularyKeys.VENDOR));
        table.nextColumn();
        table.write(resources.getString(VocabularyKeys.IMPLEMENTATIONS));
        table.nextLine();
        table.nextLine(TableWriter.DOUBLE_HORIZONTAL_LINE);
        final StringBuilder vendors         = new StringBuilder();
        final StringBuilder implementations = new StringBuilder();
        for (final Iterator<Class<?>> it=categories.iterator(); it.hasNext();) {
            /*
             * Writes the category name (CRSFactory, DatumFactory, etc.)
             */
            final Class<?> category = it.next();
            table.write(Classes.getShortName(category));
            table.nextColumn();
            /*
             * Writes the authorities in a single cell. Same for vendors and implementations.
             */
            final Iterator<?> providers = registry.getServiceProviders(category, null, null);
            while (providers.hasNext()) {
                if (implementations.length() != 0) {
                    table          .write ('\n');
                    vendors        .append('\n');
                    implementations.append('\n');
                }
                final Factory provider = (Factory) providers.next();
                final Citation vendor = provider.getVendor();
                vendors.append(vendor.getTitle().toString(locale));
                implementations.append(Classes.getShortClassName(provider));
                if (provider instanceof AuthorityFactory) {
                    final Citation authority = ((AuthorityFactory) provider).getAuthority();
                    final Iterator<? extends Identifier> identifiers =
                            authority.getIdentifiers().iterator();
                    final String identifier = identifiers.hasNext()
                            ? identifiers.next().getCode().toString()
                            : authority.getTitle().toString(locale);
                    table.write(identifier);
                }
            }
            /*
             * Writes the vendors.
             */
            table.nextColumn();
            table.write(vendors.toString());
            vendors.setLength(0);
            /*
             * Writes the implementation class name.
             */
            table.nextColumn();
            table.write(implementations.toString());
            implementations.setLength(0);
            table.writeHorizontalSeparator();
        }
        table.flush();
    }
}
