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
package org.geotools.parameter;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.opengis.metadata.Identifier;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.util.InternationalString;
import org.opengis.util.GenericName;

import org.geotools.io.TableWriter;
import org.geotools.measure.Angle;
import org.geotools.measure.AngleFormat;
import org.geotools.resources.Arguments;
import org.geotools.resources.Classes;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * Format {@linkplain ParameterDescriptorGroup parameter descriptors} or
 * {@linkplain ParameterValueGroup parameter values} in a tabular format.
 * This writer assumes a monospaced font and an encoding capable to provide
 * drawing box characters (e.g. unicode).
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class ParameterWriter extends FilterWriter {
    /**
     * The locale.
     */
    private Locale locale = Locale.getDefault();

    /**
     * The formatter to use for numbers. Will be created only when first needed.
     */
    private transient NumberFormat numberFormat;

    /**
     * The formatter to use for dates. Will be created only when first needed.
     */
    private transient DateFormat dateFormat;

    /**
     * The formatter to use for angles. Will be created only when first needed.
     */
    private transient AngleFormat angleFormat;

    /**
     * Creates a new formatter writting parameters to the
     * {@linkplain System#out default output stream}.
     */
    public ParameterWriter() {
        this(Arguments.getWriter(System.out));
    }

    /**
     * Creates a new formatter writting parameters to the specified output stream.
     */
    public ParameterWriter(final Writer out) {
        super(out);
    }

    /**
     * Prints the elements of an operation to the
     * {@linkplain System#out default output stream}.
     * This is a convenience method for <code>new
     * ParameterWriter().{@linkplain #format(OperationMethod) format}(operation)</code>.
     */
    public static void print(final OperationMethod operation) {
        final ParameterWriter writer = new ParameterWriter();
        try {
            writer.format(operation);
        } catch (IOException exception) {
            // Should never happen, since we are writting to System.out.
            throw new AssertionError(exception);
        }
    }

    /**
     * Prints the elements of a descriptor group to the
     * {@linkplain System#out default output stream}.
     * This is a convenience method for <code>new
     * ParameterWriter().{@linkplain #format(ParameterDescriptorGroup)
     * format}(descriptor)</code>.
     */
    public static void print(final ParameterDescriptorGroup descriptor) {
        final ParameterWriter writer = new ParameterWriter();
        try {
            writer.format(descriptor);
        } catch (IOException exception) {
            // Should never happen, since we are writting to System.out.
            throw new AssertionError(exception);
        }
    }

    /**
     * Prints the elements of a parameter group to the
     * {@linkplain System#out default output stream}.
     * This is a convenience method for <code>new
     * ParameterWriter().{@linkplain #format(ParameterValueGroup)
     * format}(values)</code>.
     */
    public static void print(final ParameterValueGroup values) {
        final ParameterWriter writer = new ParameterWriter();
        try {
            writer.format(values);
        } catch (IOException exception) {
            // Should never happen, since we are writting to System.out.
            throw new AssertionError(exception);
        }
    }

    /**
     * Prints the elements of an operation to the output stream.
     *
     * @param  operation The operation method to format.
     * @throws IOException if an error occured will writing to the stream.
     */
    public void format(final OperationMethod operation) throws IOException {
        synchronized (lock) {
            format(operation.getName().getCode(), operation.getParameters(), null);
        }
    }

    /**
     * Prints the elements of a descriptor group to the output stream.
     *
     * @param  descriptor The descriptor group to format.
     * @throws IOException if an error occured will writing to the stream.
     */
    public void format(final ParameterDescriptorGroup descriptor) throws IOException {
        synchronized (lock) {
            format(descriptor.getName().getCode(), descriptor, null);
        }
    }

    /**
     * Prints the elements of a parameter group to the output stream.
     *
     * @param  values The parameter group to format.
     * @throws IOException if an error occured will writing to the stream.
     */
    public void format(final ParameterValueGroup values) throws IOException {
        final ParameterDescriptorGroup descriptor = values.getDescriptor();
        synchronized (lock) {
            format(descriptor.getName().getCode(), descriptor, values);
        }
    }

    /**
     * Implementation of public {@code format} methods.
     *
     * @param  name The group name, usually {@code descriptor.getCode().getName()}.
     * @param  descriptor The parameter descriptor. Should be equals to
     *         {@code values.getDescriptor()} if {@code values} is non null.
     * @param  values The parameter values, or {@code null} if none.
     * @throws IOException if an error occured will writing to the stream.
     */
    private void format(final String                   name,
                        final ParameterDescriptorGroup group,
                        final ParameterValueGroup      values)
            throws IOException
    {
        /*
         * Write the operation name (including aliases) before the table.
         */
        final String lineSeparator = System.getProperty("line.separator", "\n");
        out.write(' ');
        out.write(name);
        out.write(lineSeparator);
        Collection<GenericName> alias = group.getAlias();
        if (alias != null) {
            boolean first = true;
            for (final GenericName a : alias) {
                out.write(first ? " alias " : "       ");
                out.write(a.toInternationalString().toString(locale));
                out.write(lineSeparator);
                first = false;
            }
        }
        /*
         * Format the table header (i.e. column names).
         */
        final Vocabulary resources = Vocabulary.getResources(locale);
        final TableWriter table = new TableWriter(out, TableWriter.SINGLE_VERTICAL_LINE);
        table.setMultiLinesCells(true);
        table.writeHorizontalSeparator();
        table.write(resources.getString(VocabularyKeys.NAME));
        table.nextColumn();
        table.write(resources.getString(VocabularyKeys.CLASS));
        table.nextColumn();
        table.write("Minimum");  // TODO localize
        table.nextColumn();
        table.write("Maximum");  // TODO localize
        table.nextColumn();
        table.write(resources.getString((values==null) ? VocabularyKeys.DEFAULT_VALUE
                                                       : VocabularyKeys.VALUE));
        table.nextColumn();
        table.write("Units");  // TODO localize
        table.nextLine();
        table.nextLine(TableWriter.DOUBLE_HORIZONTAL_LINE);
        /*
         * Format each element in the parameter group. If values were supplied, we will
         * iterate through the values instead of the descriptor. We do it that way because
         * the descriptor can't know which optional values are included and which one are
         * omitted.
         */
        List<Object> deferredGroups = null;
        final Object[] array1 = new Object[1];
        final Collection<?> elements = (values!=null) ? values.values() : group.descriptors();
        for (final Object element : elements) {
            final GeneralParameterValue      generalValue;
            final GeneralParameterDescriptor generalDescriptor;
            if (values != null) {
                generalValue = (GeneralParameterValue) element;
                generalDescriptor = generalValue.getDescriptor();
            } else {
                generalValue = null;
                generalDescriptor = (GeneralParameterDescriptor) element;
            }
            /*
             * If the current element is a group, we will format it later (after
             * all ordinary elements) in order avoid breaking the table layout.
             */
            if (generalDescriptor instanceof ParameterDescriptorGroup) {
                if (deferredGroups == null) {
                    deferredGroups = new ArrayList<Object>();
                }
                deferredGroups.add(element);
                continue;
            }
            /*
             * Format the element name, including all alias (if any).
             * Each alias will be formatted on its own line.
             */
            final Identifier identifier = generalDescriptor.getName();
            table.write(identifier.getCode());
            alias = generalDescriptor.getAlias();
            if (alias != null) {
                for (final GenericName a : alias) {
                    if (!identifier.equals(a)) {
                        table.write(lineSeparator);
                        table.write(a.tip().toInternationalString().toString(locale));
                    }
                }
            }
            table.nextColumn();
            /*
             * Format the current element as an ordinary descriptor. If we are iterating
             * over the descriptors rather than values, then the "value" column will be
             * filled with the default value specified in descriptors.
             */
            if (generalDescriptor instanceof ParameterDescriptor) {
                final ParameterDescriptor descriptor = (ParameterDescriptor) generalDescriptor;
                table.write(Classes.getShortName(descriptor.getValueClass()));
                table.nextColumn();
                table.setAlignment(TableWriter.ALIGN_RIGHT);
                Object value = descriptor.getMinimumValue();
                if (value != null) {
                    table.write(formatValue(value));
                }
                table.nextColumn();
                value = descriptor.getMaximumValue();
                if (value != null) {
                    table.write(formatValue(value));
                }
                table.nextColumn();
                if (generalValue != null) {
                    value = ((ParameterValue) generalValue).getValue();
                } else {
                    value = descriptor.getDefaultValue();
                }
                /*
                 * Wraps the value in an array. Because it may be an array of primitive
                 * type, we can't cast to Object[]. Then, each array's element will be
                 * formatted on its own line.
                 */
                final Object array;
                if (value!=null && value.getClass().isArray()) {
                    array = value;
                } else {
                    array = array1;
                    array1[0] = value;
                }
                final int length = Array.getLength(array);
                for (int i=0; i<length; i++) {
                    value = Array.get(array, i);
                    if (value != null) {
                        if (i != 0) {
                            table.write(lineSeparator);
                        }
                        table.write(formatValue(value));
                    }
                }
                table.nextColumn();
                table.setAlignment(TableWriter.ALIGN_LEFT);
                value = descriptor.getUnit();
                if (value != null) {
                    table.write(value.toString());
                }
            }
            table.writeHorizontalSeparator();
        }
        table.flush();
        /*
         * Now format all groups deferred to the end of this table.
         * Most of the time, there is no such group.
         */
        if (deferredGroups != null) {
            for (final Object element : deferredGroups) {
                final ParameterValueGroup value;
                final ParameterDescriptorGroup descriptor;
                if (element instanceof ParameterValueGroup) {
                    value = (ParameterValueGroup) element;
                    descriptor = value.getDescriptor();
                } else {
                    value = null;
                    descriptor = (ParameterDescriptorGroup) element;
                }
                out.write(lineSeparator);
                format(name + '/' + descriptor.getName().getCode(), descriptor, value);
            }
        }
    }

    /**
     * Formats a summary of a collection of {@linkplain IdentifiedObject identified objects}.
     * The summary contains the identifier name and alias aligned in a table.
     *
     * @param  parameters The collection of parameters to format.
     * @param  scopes     The set of scopes to include in the table, of {@code null} for all
     *                    of them. A restricted a set will produce a table with less columns.
     * @throws IOException if an error occured will writing to the stream.
     */
    public void summary(final Collection<? extends IdentifiedObject> parameters,
                        final Set<String> scopes) throws IOException
    {
        /*
         * Prepares the list of alias before any write to the output stream.
         * We need to prepare the list first, because not all identified objects
         * may have generic names with the same scopes in the same order.
         *
         *   titles    -  The column number for each column title.
         *   names     -  The names (including alias) for each line.
         */
        final Map<Object,Integer> titles = new LinkedHashMap<Object,Integer>();
        final List<String[]>      names  = new ArrayList<String[]>();
        final Locale              locale = this.locale; // Protect from changes.
        String[] descriptions = null;
        titles.put(null, 0); // Special value for the identifier column.
        for (final IdentifiedObject element : parameters) {
            final Collection<GenericName> aliases = element.getAlias();
            String[] elementNames = new String[titles.size()];
            elementNames[0] = element.getName().getCode();
            if (aliases != null) {
                /*
                 * The primary name has been fetch (before this block) for one element, and we
                 * determined that some alias may be available in addition. Add local alias
                 * (i.e. names without their scope) to the 'elementNames' row.
                 */
                int count = 0;
                for (final GenericName alias : aliases) {
                    final GenericName scope = alias.scope().name();
                    final GenericName name  = alias.tip();
                    final Object title;
                    if (scope != null) {
                        if (scopes!=null && !scopes.contains(scope.toString())) {
                            /*
                             * The user requested only a subset of alias (the 'scopes' argument),
                             * and the current alias is not a member of this subset. Continue the
                             * search to other alias.
                             */
                            continue;
                        }
                        title = scope.toInternationalString().toString(locale);
                    } else {
                        title = count++;
                    }
                    /*
                     * The alias scope is used as the column's title. If the alias has no scope,
                     * then a sequencial number is used instead. Now check if the column already
                     * exists. If it exists, fetch its position. If it doesn't exist, inserts the
                     * new column at the end of existing columns.
                     */
                    Integer position = titles.get(title);
                    if (position == null) {
                        position = titles.size();
                        titles.put(title, position);
                    }
                    /*
                     * Now stores the alias local name at the position we just determined above.
                     * Note that more than one value may exist for the same column. For example
                     * both "WGS 84" and "4326" may appear as EPSG alias (as EPSG name and EPSG
                     * identifier respectively), depending how the parameters given by the user
                     * were constructed.
                     */
                    final int index = position.intValue();
                    if (index >= elementNames.length) {
                        elementNames = XArray.resize(elementNames, index+1);
                    }
                    final String oldName = elementNames[index];
                    final String newName = name.toInternationalString().toString(locale);
                    if (oldName==null || oldName.length()>newName.length()) {
                        /*
                         * Keep the shortest string, since it is often a code used
                         * for identification (e.g. EPSG code). It also help to fit
                         * the table in the window's width.
                         */
                        elementNames[index] = newName;
                    }
                }
            }
            /*
             * Before to add the name and alias to the list, fetch the remarks (if any).
             * They are stored in a separated list and will appear as the very last column.
             */
            final InternationalString remarks = element.getRemarks();
            if (remarks != null) {
                if (descriptions == null) {
                    descriptions = new String[parameters.size()];
                }
                descriptions[names.size()] = remarks.toString(locale);
            }
            names.add(elementNames);
        }
        /*
         * Trim the columns that duplicates the identifier column (#0). This is
         * usually the case of the OGC column (usually #1), since we already use
         * OGC name as the main identifier in most cases.
         */
        final boolean[] hide = new boolean[titles.size()];
trim:   for (int column=hide.length; --column>=1;) {
            for (final String[] alias : names) {
                if (alias.length > column) {
                    final String name = alias[column];
                    if (name!=null && !name.equals(alias[0])) {
                        // No need to looks at the next lines.
                        // Move to previous column.
                        continue trim;
                    }
                }
            }
            // A column duplicating the identifier column has been found.
            hide[column] = true;
        }
        /*
         * Writes the table. The header will contains one column for each alias's
         * scope (or authority) declared in 'titles', in the same order. It will
         * also contains a "Description" column if there is some.
         */
        int column = 0;
        synchronized (lock) {
            final TableWriter table = new TableWriter(out, TableWriter.SINGLE_VERTICAL_LINE);
            table.setMultiLinesCells(true);
            table.writeHorizontalSeparator();
            /*
             * Writes all column headers.
             */
            for (final Object element : titles.keySet()) {
                if (hide[column++]) {
                    continue;
                }
                final String title;
                if (element == null) {
                    title = "Identifier"; // TODO: localize
                } else if (element instanceof String) {
                    title = (String) element;
                } else {
                    title = "Alias " + element; // TODO: localize
                }
                table.write(title);
                table.nextColumn();
            }
            if (descriptions != null) {
                table.write("Description"); // TODO: localize
            }
            table.writeHorizontalSeparator();
            /*
             * Writes all row.
             */
            int counter = 0;
            for (final String[] aliases : names) {
                for (column=0; column<hide.length; column++) {
                    if (hide[column]) {
                        continue;
                    }
                    if (column < aliases.length) {
                        final String alias = aliases[column];
                        if (alias != null) {
                            table.write(alias);
                        }
                    }
                    table.nextColumn();
                }
                if (descriptions != null) {
                    final String remarks = descriptions[counter++];
                    if (remarks != null) {
                        table.write(remarks);
                    }
                }
                table.nextLine();
            }
            table.writeHorizontalSeparator();
            table.flush();
        }
    }

    /**
     * Returns the current locale. Newly constructed {@code ParameterWriter}
     * use the {@linkplain Locale#getDefault system default}.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Set the locale to use for table formatting.
     */
    public void setLocale(final Locale locale) {
        synchronized (lock) {
            this.locale  = locale;
            numberFormat = null;
            dateFormat   = null;
            angleFormat  = null;
        }
    }

    /**
     * Format the specified value as a string. This method is automatically invoked
     * by {@code format(...)} methods. The default implementation format
     * {@link Number}, {@link Date} and {@link Angle} object according the
     * {@linkplain #getLocale current locale}. This method can been overridden if
     * more objects need to be formatted in a special way.
     *
     * @param  value the value to format.
     * @return The value formatted as a string.
     */
    protected String formatValue(final Object value) {
        if (value instanceof Number) {
            if (numberFormat == null) {
                numberFormat = NumberFormat.getNumberInstance(locale);
            }
            return numberFormat.format(value);
        }
        if (value instanceof Date) {
            if (dateFormat == null) {
                dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            }
            return dateFormat.format(value);
        }
        if (value instanceof Angle) {
            if (angleFormat == null) {
                angleFormat = AngleFormat.getInstance(locale);
            }
            return angleFormat.format(value);
        }
        return String.valueOf(value);
    }
}
