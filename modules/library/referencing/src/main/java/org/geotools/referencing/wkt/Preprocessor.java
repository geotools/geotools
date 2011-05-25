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
package org.geotools.referencing.wkt;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import org.geotools.io.TableWriter;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * A parser that performs string replacements before to delegate the work to an other parser.
 * String replacements are specified through calls to the {@link #addDefinition addDefinition}
 * method. In the example below, the {@code WGS84} string in the {@linkplain #parseObject
 * parseObject} call is expanded into the full <code>GEOGCS["WGS84", ...</code> string before
 * to be parsed.
 *
 * <blockquote><code>
 * {@linkplain #addDefinition addDefinition}("WGS84", "GEOGCS[\"WGS84\", DATUM[</code> ...<i>etc</i>... <code>]]<BR>
 * {@linkplain #parseObject parseObject}("PROJCS[\"Mercator_1SP\", <strong>WGS84</strong>, PROJECTION[</code> ...<i>etc</i>... <code>]]")</code>
 * </blockquote>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class Preprocessor extends Format {
    /**
     * The WKT parser, usually a {@link Parser} object.
     */
    protected final Format parser;

    /**
     * The set of objects defined by calls to {@link #addDefinition}.
     */
    private final Map definitions/*<String,Definition>*/ = new TreeMap();

    /**
     * The unmodifiable set of keys in the {@link #definitions} map. Will be constructed
     * only when first needed.
     */
    private transient Set names;

    /**
     * A linked list of informations about the replacements performed by {@link #substitutes}.
     * Those informations are used by {@link #parseObject(String,Class)} in order to adjust
     * {@linkplain ParseException#getErrorOffset error offset} in case of failure.
     */
    private transient Replacement replacements;

    /**
     * The initial offset of the line in process of being parsed. This is a helper field
     * for use by {@link AbstractConsole} only, in order to produce more accurate information in
     * case of {@link ParseException}. This field has no impact on the object returned as a result
     * of successful parsing.
     */
    transient int offset = 0;

    /**
     * Creates a new preprocessor that delegates the work to the specified parser.
     *
     * @param parser The WKT parser, usually a {@link Parser} object.
     */
    public Preprocessor(final Format parser) {
        this.parser = parser;
    }

    /**
     * Formats the specified object. This method delegates the work to the
     * {@linkplain #parser parser} given at construction time.
     *
     * @param object     The object to format.
     * @param toAppendTo Where the text is to be appended.
     * @param position   Identification of a field in the formatted text.
     * @return The string buffer passed in as {@code toAppendTo},
     *         with formatted text appended
     */
    public StringBuffer format(final Object        object,
                               final StringBuffer  toAppendTo,
                               final FieldPosition position)
    {
        return parser.format(object, toAppendTo, position);
    }

    /**
     * Parses the specified Well Know Text starting at the specified position.
     * The default implementation delegates the work to
     * <code>{@link #parseObject(String) parseObject}(wkt.substring(position.getIndex()))</code>.
     *
     * @param  wkt The text to parse.
     * @param  position The index of the first character to parse.
     * @return The parsed object, or {@code null} in case of failure.
     */
    public Object parseObject(final String wkt, final ParsePosition position) {
        /*
         * NOTE:  the other way around (parseObject(String) invoking
         * parseObject(String,ParsePosition) like the default Format
         * implementation) is not pratical. Among other problems, it
         * doesn't provide any accurate error message.
         */
        final int start = position.getIndex();
        try {
            return parseObject(wkt.substring(start));
        } catch (ParseException exception) {
            position.setIndex(start);
            position.setErrorIndex(exception.getErrorOffset() + start);
            return null;
        }
    }

    /**
     * Parses the specified Well Know Text without restriction on the expected type.
     * The default implementation delegates the work to
     * <code>{@link #parseObject(String,Class) parseObject}(wkt, Object.class)</code>.
     *
     * @param  wkt The text to parse.
     * @return The parsed object.
     * @throws ParseException if the text can't be parsed.
     */
    @Override
    public Object parseObject(final String wkt) throws ParseException {
        try {
            return parseObject(wkt, Object.class);
        } catch (FactoryException cause) {
            final ParseException e = new ParseException(cause.getLocalizedMessage(), 0);
            e.initCause(cause);
            throw e;
        }
    }

    /**
     * Parses the specified text and ensure that the resulting object is of the specified type.
     * The text can be any of the following:
     * <BR>
     * <UL>
     *   <LI>A name declared in some previous call to
     *       <code>{@linkplain #addDefinition addDefinition}(name, ...)</code>.</LI>
     *   <LI>A Well Know Text, which may contains itself shortcuts declared in
     *       previous call to {@code addDefinition}. This text is given to
     *       the underlying {@link #parser}.</LI>
     *   <LI>Any services provided by subclasses. For example a subclass way recognize
     *       some authority code like {@code EPSG:6326}.</LI>
     * </UL>
     *
     * @param  text The text, as a name, a WKT to parse, or an authority code.
     * @param  type The expected type for the object to be parsed (usually a
     *         <code>{@linkplain CoordinateReferenceSystem}.class</code> or
     *         <code>{@linkplain MathTransform}.class</code>).
     * @return The object.
     * @throws ParseException if parsing the specified WKT failed.
     * @throws FactoryException if the object is not of the expected type.
     */
    public Object parseObject(String text, final Class type)
            throws ParseException, FactoryException
    {
        Object value;
        final Definition def = (Definition) definitions.get(text);
        if (def != null) {
            value = def.asObject;
            if (type.isAssignableFrom(value.getClass())) {
                return value;
            }
        } else if (!isIdentifier(text)) {
            /*
             * The specified string was not found in the definitions map. Try to parse it as a
             * WKT, but only if it contains more than a single word. This later condition exists
             * only in order to produces a more accurate error message (WKT parsing of a single
             * word is garantee to fail). In any case, the definitions map is not updated since
             * this method is not invoked from the SET instruction.
             */
            text  = substitute  (text);
            value = forwardParse(text);
            final Class actualType = value.getClass();
            if (type.isAssignableFrom(actualType)) {
                return value;
            }
            throw new FactoryException(Errors.format(
                    ErrorKeys.ILLEGAL_CLASS_$2, actualType, type));
        }
        throw new NoSuchIdentifierException(Errors.format(
                ErrorKeys.NO_SUCH_AUTHORITY_CODE_$2, type, text), text);
    }

    /**
     * Parses a WKT. This method delegates the work to the {@link #parser}, but
     * catch the exception in case of failure. The exception is rethrown with the
     * {@linkplain ParseException#getErrorIndex error index} adjusted in order to
     * point to the character in the original text (before substitutions).
     *
     * @param  text The WKT to parse.
     * @return The object.
     * @throws ParseException if the parsing failed.
     */
    private Object forwardParse(final String text) throws ParseException {
        try {
            return parser.parseObject(text);
        } catch (ParseException exception) {
            int shift = 0;
            int errorOffset = exception.getErrorOffset();
            for (Replacement r=replacements; r!=null; r=r.next) {
                if (errorOffset < r.lower) {
                    break;
                }
                if (errorOffset < r.upper) {
                    errorOffset = r.lower;
                    break;
                }
                shift += r.shift;
            }
            final ParseException adjusted = new ParseException(exception.getLocalizedMessage(),
                                                               errorOffset - shift);
            adjusted.setStackTrace(exception.getStackTrace());
            adjusted.initCause(exception.getCause());
            throw adjusted;
        }
    }

    /**
     * For every definition key found in the given string, substitute
     * the key by its value. The replacement will not be performed if
     * the key was found between two quotation marks.
     *
     * @param  text The string to process.
     * @return The string with all keys replaced by their values.
     */
    private String substitute(final String text) {
        Replacement last;
        replacements = last = new Replacement(0, 0, offset);
        StringBuilder buffer = null;
        for (final Iterator it=definitions.entrySet().iterator(); it.hasNext();) {
            final Map.Entry entry = (Map.Entry)  it.next();
            final String     name = (String)     entry.getKey();
            final Definition def  = (Definition) entry.getValue();
            int index = (buffer!=null) ? buffer.indexOf(name) : text.indexOf(name);
            while (index >= 0) {
                /*
                 * An occurence of the text to substitute was found. First, make sure
                 * that the occurence found is a full word  (e.g. if the occurence to
                 * search is "WGS84", do not accept "TOWGS84").
                 */
                final int upper = index + name.length();
                final CharSequence cs = (buffer!=null) ? (CharSequence)buffer : (CharSequence)text;
                if ((index==0           || !Character.isJavaIdentifierPart(cs.charAt(index-1))) &&
                    (upper==cs.length() || !Character.isJavaIdentifierPart(cs.charAt(upper))))
                {
                    /*
                     * Count the number of quotes before the text to substitute. If this
                     * number is odd, then the text is between quotes and should not be
                     * substituted.
                     */
                    int count = 0;
                    for (int scan=index; --scan>=0;) {
                        scan = (buffer!=null) ? buffer.lastIndexOf("\"", scan)
                                              :   text.lastIndexOf( '"', scan);
                        if (scan < 0) {
                            break;
                        }
                        count++;
                    }
                    if ((count & 1) == 0) {
                        /*
                         * An even number of quotes was found before the text to substitute.
                         * Performs the substitution and keep trace of this replacement in a
                         * chained list of 'Replacement' objects.
                         */
                        if (buffer == null) {
                            buffer = new StringBuilder(text);
                            assert buffer.indexOf(name, index) == index;
                        }
                        final String value = def.asString;
                        buffer.replace(index, upper, value);
                        final int change = value.length() - name.length();
                        last = last.next = new Replacement(index, index+value.length(), change);
                        index = buffer.indexOf(name, index + change);
                        // Note: it is okay to skip the text we just replaced, since the
                        //       'definitions' map do not contains nested definitions.
                        continue;
                    }
                }
                /*
                 * The substitution was not performed because the text found was not a word,
                 * or was between quotes. Search the next occurence.
                 */
                index += name.length();
                index = (buffer!=null) ? buffer.indexOf(name, index)
                                       : text  .indexOf(name, index);
            }
        }
        return (buffer!=null) ? buffer.toString() : text;
    }

    /**
     * Adds a predefined Well Know Text (WKT). The {@code value} argument given to this method
     * can contains itself other definitions specified in some previous calls to this method.
     *
     * @param  name The name for the definition to be added.
     * @param  value The Well Know Text (WKT) represented by the name.
     * @throws IllegalArgumentException if the name is invalid.
     * @throws ParseException if the WKT can't be parsed.
     */
    public void addDefinition(final String name, String value) throws ParseException {
        if (value==null || value.trim().length()==0) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.MISSING_WKT_DEFINITION));
        }
        if (!isIdentifier(name)) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_IDENTIFIER_$1, name));
        }
        value = substitute(value);
        final Definition newDef = new Definition(value, forwardParse(value));
        final Definition oldDef = (Definition) definitions.put(name, newDef);
    }

    /**
     * Removes a definition set in some previous call to
     * <code>{@linkplain #addDefinition addDefinition}(name, ...)</code>.
     *
     * @param name The name of the definition to remove.
     */
    public void removeDefinition(final String name) {
        definitions.remove(name);
    }

    /**
     * Returns an unmodifiable set which contains all definition's names given to the
     * <code>{@linkplain #addDefinition addDefinition}(name, ...)</code> method. The
     * elements in this set are sorted in alphabetical order.
     */
    public Set getDefinitionNames() {
        if (names == null) {
            names = Collections.unmodifiableSet(definitions.keySet());
        }
        return names;
    }

    /**
     * Prints to the specified stream a table of all definitions.
     * The content of this table is inferred from the values given to the
     * {@link #addDefinition} method.
     *
     * @param  out writer The output stream where to write the table.
     * @throws IOException if an error occured while writting to the output stream.
     */
    public void printDefinitions(final Writer out) throws IOException {
        final Locale locale = null;
        final Vocabulary resources = Vocabulary.getResources(locale);
        final TableWriter table = new TableWriter(out, TableWriter.SINGLE_VERTICAL_LINE);
        table.setMultiLinesCells(true);
        table.writeHorizontalSeparator();
        table.write(resources.getString(VocabularyKeys.NAME));
        table.nextColumn();
        table.write(resources.getString(VocabularyKeys.TYPE));
        table.nextColumn();
        table.write(resources.getString(VocabularyKeys.DESCRIPTION));
        table.nextLine();
        table.writeHorizontalSeparator();
        for (final Iterator it=definitions.entrySet().iterator(); it.hasNext();) {
            final Map.Entry entry = (Map.Entry) it.next();
            final Object   object = ((Definition) entry.getValue()).asObject;
            table.write(String.valueOf(entry.getKey()));
            table.nextColumn();
            table.write(Classes.getShortClassName(object));
            table.nextColumn();
            if (object instanceof IdentifiedObject) {
                table.write(((IdentifiedObject) object).getName().getCode());
            }
            table.nextLine();
        }
        table.writeHorizontalSeparator();
        table.flush();
    }

    /**
     * Returns {@code true} if the specified text is a valid identifier.
     */
    private static boolean isIdentifier(final String text) {
        for (int i=text.length(); --i>=0;) {
            final char c = text.charAt(i);
            if (!Character.isJavaIdentifierPart(c) && c!=':') {
                return false;
            }
        }
        return true;
    }

    /**
     * An entry for the {@link Console#definitions} map. This entry contains a definition
     * as a well know text (WKT), and the parsed value for this WKT (usually a
     * {@linkplain CoordinateReferenceSystem} or a {@linkplain MathTransform} object).
     */
    private static final class Definition implements Serializable {
        /**
         * The definition as a string. This string should not contains anymore
         * shortcut to substitute by an other WKT (i.e. compound definitions
         * must be resolved before to construct a {@code Definition} object).
         */
        public final String asString;

        /**
         * The definition as an object (usually a {@linkplain CoordinateReferenceSystem}
         * or a {@linkplain MathTransform} object).
         */
        public final Object asObject;

        /**
         * Constructs a new definition.
         */
        public Definition(final String asString, final Object asObject) {
            this.asString = asString;
            this.asObject = asObject;
        }
    }

    /**
     * Contains informations about the index changes induced by a replacement in a string.
     * All index refer to the string <strong>after</strong> the replacement. The substring
     * at index between {@link #lower} inclusive and {@link #upper} exclusive is the replacement
     * string. The {@link #shift} is the difference between the replacement substring length and
     * the replaced substring length.
     */
    private static final class Replacement {
        /** The lower index in the target string, inclusive. */ public final int  lower;
        /** The upper index in the target string, exclusive. */ public final int  upper;
        /** The shift from source string to target string.   */ public final int  shift;
        /** The next element in the linked list.             */ public Replacement next;

        /** Constructs a new index shift initialized with the given values. */
        public Replacement(final int lower, final int upper, final int shift) {
            this.lower = lower;
            this.upper = upper;
            this.shift = shift;
        }

        /**
         * Returns a string representation for debugging purpose.
         */
        @Override
        public String toString() {
            final StringBuilder buffer = new StringBuilder();
            for (Replacement r=this; r!=null; r=r.next) {
                if (r != this) {
                    buffer.append(", ");
                }
                buffer.append('[')
                      .append(r.lower)
                      .append("..")
                      .append(r.upper)
                      .append("] \u2192 ")
                      .append(r.shift);
            }
            return buffer.toString();
        }
    }
}
