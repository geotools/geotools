/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// The following are just data structure, not dependencies to the whole Swing framework.
import javax.swing.tree.TreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultMutableTreeNode;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.Factory;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.cs.CSAuthorityFactory;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.datum.DatumAuthorityFactory;
import org.opengis.referencing.operation.CoordinateOperationAuthorityFactory;
import org.opengis.referencing.operation.CoordinateOperationFactory;

import org.geotools.factory.BufferedFactory;
import org.geotools.factory.OptionalFactory;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.resources.OptionalDependencies;
import org.geotools.resources.Classes;
import org.geotools.resources.X364;


/**
 * Build a tree of factory dependencies.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class FactoryDependencies {
    /**
     * A list of interfaces that may be implemented by this class. Used for
     * the properties printed between parenthesis by {@link #createTree()}.
     */
    private static final Class[] TYPES = {
        CRSFactory                          .class,
        CRSAuthorityFactory                 .class,
        CSFactory                           .class,
        CSAuthorityFactory                  .class,
        DatumFactory                        .class,
        DatumAuthorityFactory               .class,
        CoordinateOperationFactory          .class,
        CoordinateOperationAuthorityFactory .class,
        BufferedFactory                     .class,
        OptionalFactory                     .class,
        Factory                             .class  // Processed in a special way.
    };

    /**
     * Labels for {@link #TYPES}.
     */
    private static final String[] TYPE_LABELS = {
        "crs", "crs", "cs", "cs", "datum", "datum", "operation", "operation",
        "buffered", "optional", "registered"
    };

    /**
     * The number of elements in {@link #TYPES} which are referencing factories.
     * They are printed in a different color than the last elements.
     */
    private static final int FACTORY_COUNT = TYPES.length - 3;

    /**
     * The factory to format.
     */
    private final Factory factory;

    /**
     * {@code true} for applying colors on a ANSI X3.64 (aka ECMA-48 and ISO/IEC 6429)
     * compliant output device.
     */
    private boolean colorEnabled;

    /**
     * {@code true} for printing attributes {@link #TYPE_LABELS} between parenthesis
     * after the factory name.
     */
    private boolean attributes;

    /**
     * Creates a new dependency tree for the specified factory.
     */
    public FactoryDependencies(final Factory factory) {
        this.factory = factory;
    }

    /**
     * Returns {@code true} if syntax coloring is enabled.
     * Syntax coloring is disabled by default.
     */
    public boolean isColorEnabled() {
        return colorEnabled;
    }

    /**
     * Enables or disables syntax coloring on ANSI X3.64 (aka ECMA-48 and ISO/IEC 6429)
     * compatible terminal. By default, syntax coloring is disabled.
     */
    public void setColorEnabled(final boolean enabled) {
        colorEnabled = enabled;
    }

    /**
     * Returns {@code true} if attributes are to be printed.
     * By default, attributes are not printed.
     */
    public boolean isAttributeEnabled() {
        return attributes;
    }

    /**
     * Enables or disables the addition of attributes after factory names. Attributes
     * are labels like "{@code crs}", "{@code datum}", <cite>etc.</cite> put between
     * parenthesis. They give indications on the services implemented by the factory
     * (e.g. {@link CRSAuthorityFactory}, {@link DatumAuthorityFactory}, <cite>etc.</cite>).
     */
    public void setAttributeEnabled(final boolean enabled) {
        attributes = enabled;
    }

    /**
     * Prints the dependencies as a tree to the specified printer.
     */
    public void print(final PrintWriter out) {
        out.print(OptionalDependencies.toString(asTree()));
    }

    /**
     * Prints the dependencies as a tree to the specified writer.
     *
     * @param  out The writer where to print the tree.
     * @throws IOException if an error occured while writting to the stream.
     */
    public void print(final Writer out) throws IOException {
        out.write(OptionalDependencies.toString(asTree()));
    }

    /**
     * Returns the dependencies as a tree.
     */
    public TreeNode asTree() {
        return createTree(factory, new HashSet<Factory>());
    }

    /**
     * Returns the dependencies for the specified factory.
     */
    private MutableTreeNode createTree(final Factory factory, final Set<Factory> done) {
        final DefaultMutableTreeNode root = createNode(factory);
        if (factory instanceof ReferencingFactory) {
            final Collection<?> dep = ((ReferencingFactory) factory).dependencies();
            if (dep != null) {
                for (final Object element : dep) {
                    final MutableTreeNode child;
                    if (element instanceof Factory) {
                        final Factory candidate = (Factory) element;
                        if (!done.add(candidate)) {
                            continue;
                        }
                        child = createTree(candidate, done);
                        if (!done.remove(candidate)) {
                            throw new AssertionError(); // Should never happen.
                        }
                    } else {
                        child = OptionalDependencies.createTreeNode(String.valueOf(element), element, false);
                    }
                    root.add(child);
                }
            }
        }
        return root;
    }

    /**
     * Creates a single node for the specified factory.
     */
    private DefaultMutableTreeNode createNode(final Factory factory) {
        final StringBuilder buffer =
                new StringBuilder(Classes.getShortClassName(factory)).append('[');
        if (factory instanceof AuthorityFactory) {
            final Citation authority = ((AuthorityFactory) factory).getAuthority();
            if (authority != null) {
                final Collection<? extends Identifier> identifiers = authority.getIdentifiers();
                if (identifiers != null && !identifiers.isEmpty()) {
                    boolean next = false;
                    for (final Identifier id : identifiers) {
                        if (next) {
                            buffer.append(", ");
                        }
                        appendIdentifier(buffer, id.getCode());
                        next = true;
                    }
                } else {
                    appendIdentifier(buffer, authority.getTitle());
                }
            }
        } else {
            if (colorEnabled) buffer.append(X364.RED);
            buffer.append("direct");
            if (colorEnabled) buffer.append(X364.DEFAULT);
        }
        buffer.append(']');
        if (attributes) {
            boolean hasFound = false;
            for (int i=0; i<TYPES.length; i++) {
                final Class type = TYPES[i];
                if (!type.isInstance(factory)) {
                    continue;
                }
                if (type.equals(Factory.class)) { // Special case.
                    if (!ReferencingFactoryFinder.isRegistered(factory)) {
                        continue;
                    }
                }
                buffer.append(hasFound ? ", " : " (");
                if (colorEnabled) {
                    buffer.append(i < FACTORY_COUNT ? X364.GREEN : X364.CYAN);
                }
                buffer.append(TYPE_LABELS[i]);
                if (colorEnabled) {
                    buffer.append(X364.DEFAULT);
                }
                hasFound = true;
            }
            if (hasFound) {
                buffer.append(')');
            }
        }
        return OptionalDependencies.createTreeNode(buffer.toString(), factory, true);
    }

    /**
     * Appends an identifier to the specified buffer.
     */
    private void appendIdentifier(final StringBuilder buffer, final CharSequence identifier) {
        if (colorEnabled) buffer.append(X364.MAGENTA);
        buffer.append('"').append(identifier).append('"');
        if (colorEnabled) buffer.append(X364.DEFAULT);
    }
}
