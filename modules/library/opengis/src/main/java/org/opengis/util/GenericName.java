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

import java.util.List;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A sequence of identifiers rooted within the context of a {@linkplain NameSpace namespace}.
 * This interface is similar in purpose to {@link javax.naming.Name} from the <cite>Java Naming
 * and Directory Interface</cite>. All generic names:
 * <p>
 * <ul>
 *   <li>carry an association with their {@linkplain #scope scope} in which they are
 *       considered local;</li>
 *   <li>have the ability to provide a {@linkplain #getParsedNames parsed} version of
 *       themselves.</li>
 * </ul>
 * <p>
 * Names are <em>immutables</em>. They may be {@linkplain #toFullyQualifiedName fully qualified}
 * like {@code "org.opengis.util.Record"}, or they may be relative to a {@linkplain #scope scope}
 * like {@code "util.Record"} in the {@code "org.opengis"} scope. The illustration below shows all
 * possible constructions for {@code "org.opengis.util.Record"}.
 *
 * <blockquote><table border="1" cellpadding="15"><tr><td><table border="0" cellspacing="0">
 *   <tr>
 *     <th align="right">org</th>
 *     <th>.</th><th>opengis</th>
 *     <th>.</th><th>util</th>
 *     <th>.</th><th>Record</th>
 *     <th width="50"></th>
 *     <th>{@link #scope}</th>
 *     <th>{@link #getParsedNames}</th>
 *   </tr>
 *
 *   <tr align="center">
 *     <td bgcolor="palegoldenrod" colspan="1"><font size="-1">{@linkplain #head head}</font></td><td></td>
 *     <td bgcolor="palegoldenrod" colspan="5"><font size="-1">{@linkplain ScopedName#tail tail}</font></td>
 *     <td rowspan="3"></td>
 *     <td rowspan="3" bgcolor="beige" align="left">{@linkplain NameSpace#isGlobal global}</td>
 *     <td rowspan="3" bgcolor="beige" align="right">{@literal {"org", "opengis", "util", "Record"}}</td>
 *   </tr>
 *   <tr align="center">
 *     <td bgcolor="wheat" colspan="5"><font size="-1">{@linkplain ScopedName#path path}</font></td><td></td>
 *     <td bgcolor="wheat" colspan="1"><font size="-1">{@linkplain #tip tip}</font></td>
 *   </tr>
 *   <tr align="center">
 *     <td bgcolor="burlywood" colspan="7">{@linkplain ScopedName}</td>
 *   </tr>
 *
 *   <tr><td colspan="7" height="3"></td></tr>
 *   <tr align="center">
 *     <td bgcolor="palegoldenrod" colspan="1" rowspan="3"><font size="-1">{@linkplain #scope scope}</font></td><td rowspan="3"></td>
 *     <td bgcolor="palegoldenrod" colspan="1"><font size="-1">head</font></td><td></td>
 *     <td bgcolor="palegoldenrod" colspan="3"><font size="-1">tail</font></td>
 *     <td rowspan="3"></td>
 *     <td rowspan="3" bgcolor="beige" align="left">{@literal "org"}</td>
 *     <td rowspan="3" bgcolor="beige" align="right">{@literal {"opengis", "util", "Record"}}</td>
 *   </tr>
 *   <tr align="center">
 *     <td bgcolor="wheat" colspan="3"><font size="-1">path</font></td><td></td>
 *     <td bgcolor="wheat" colspan="1"><font size="-1">tip</font></td>
 *   </tr>
 *   <tr align="center">
 *     <td bgcolor="burlywood" colspan="5">ScopedName</td>
 *   </tr>
 *
 *   <tr><td colspan="7" height="3"></td></tr>
 *   <tr align="center">
 *     <td bgcolor="palegoldenrod" colspan="3" rowspan="3"><font size="-1">scope</font></td><td rowspan="3"></td>
 *     <td bgcolor="palegoldenrod" colspan="1"><font size="-1">head</font></td><td></td>
 *     <td bgcolor="palegoldenrod" colspan="1"><font size="-1">tail</font></td>
 *     <td rowspan="3"></td>
 *     <td rowspan="3" bgcolor="beige" align="left">{@literal "org.opengis"}</td>
 *     <td rowspan="3" bgcolor="beige" align="right">{@literal {"util", "Record"}}</td>
 *   </tr>
 *   <tr align="center">
 *     <td bgcolor="wheat" colspan="1"><font size="-1">path</font></td><td></td>
 *     <td bgcolor="wheat" colspan="1"><font size="-1">tip</font></td>
 *   </tr>
 *   <tr align="center">
 *     <td bgcolor="burlywood" colspan="3">ScopedName</td>
 *   </tr>
 *
 *   <tr><td colspan="7" height="3"></td></tr>
 *   <tr align="center">
 *     <td bgcolor="palegoldenrod" colspan="5" rowspan="3"><font size="-1">scope</font></td><td rowspan="3"></td>
 *     <td bgcolor="palegoldenrod" colspan="1"><font size="-1">head</font></td>
 *     <td rowspan="3"></td>
 *     <td rowspan="3" bgcolor="beige" align="left">{@literal "org.opengis.util"}</td>
 *     <td rowspan="3" bgcolor="beige" align="right">{@literal {"Record"}}</td>
 *   </tr>
 *   <tr align="center">
 *     <td bgcolor="wheat" colspan="1"><font size="-1">tip</font></td>
 *   </tr>
 *   <tr align="center">
 *     <td bgcolor="burlywood" colspan="1">{@linkplain LocalName}</td>
 *   </tr>
 * </table></td></tr></table></blockquote>
 * <p>
 * The {@linkplain Comparable natural ordering} for generic names is implementation dependent.
 * A recommended practice is to {@linkplain String#compareTo compare lexicographically} each
 * element in the {@linkplain #getParsedNames list of parsed names}. Specific attributes of
 * the name, such as how it treats case, may affect the ordering. In general, two names of
 * different classes may not be compared.
 *
 * @author Martin Desruisseaux (IRD)
 * @author Bryce Nordgren (USDA)
 * @since GeoAPI 1.0
 *
 * @see javax.naming.Name
 */
@UML(identifier="GenericName", specification=ISO_19103)
public interface GenericName extends Comparable<GenericName> {
    /**
     * Returns the scope (name space) in which this name is local. The scope is set on creation
     * and is not modifiable. The scope of a name determines where a name starts.
     * <p>
     * <b>Example</b>:
     * For a {@linkplain #toFullyQualifiedName fully qualified name} (a name having a
     * {@linkplain NameSpace#isGlobal global namespace}) {@code "org.opengis.util.Record"},
     * if this instance is the name {@code "util.Record"}, then the scope of this instance
     * has the {@linkplain NameSpace#name name} {@code "org.opengis"}.
     *
     * @return The scope of this name.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="scope", obligation=MANDATORY, specification=ISO_19103)
    NameSpace scope();

    /**
     * Indicates the number of levels specified by this name. The depth is the {@linkplain List#size size}
     * of the list returned by the {@link #getParsedNames} method. As such it is a derived parameter. For
     * any {@link LocalName}, it is always one. For a {@link ScopedName} it is some number greater than or
     * equal to 2.
     * <p>
     * This method is similar in purpose to {@link javax.naming.Name#size()}
     * from the <cite>Java Naming and Directory Interface</cite>.
     * <p>
     * <b>Example</b>:
     * If {@code this} name is {@code "org.opengis.util.Record"}, then this method shall returns
     * {@code 4}. If this name is {@code "util.Record"} in scope {@code "org.opengis"}, then this
     * method shall returns {@code 2}.
     *
     * @return The depth of this name.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="depth", obligation=MANDATORY, specification=ISO_19103)
    int depth();

    /**
     * Returns the sequence of {@linkplain LocalName local names} making this generic name.
     * The length of this sequence is the {@linkplain #depth depth}. It does not include the
     * {@linkplain #scope scope}.
     * <p>
     * This method is similar in purpose to {@link javax.naming.Name#getAll()}
     * from the <cite>Java Naming and Directory Interface</cite>.
     * <p>
     * <b>Example</b>:
     * If {@code this} name is {@code "org.opengis.util.Record"}, then this method shall returns a
     * list containing {@code {"org", "opengis", "util", "Record"}} elements in that iteration order.
     * If this name is {@code "util.Record"} in scope {@code "org.opengis"}, then this method shall
     * returns a list containing only {@code {"util", "Record"}} elements.
     *
     * @return The local names making this generic name, without the {@linkplain #scope scope}.
     *         Shall never be {@code null} neither {@linkplain List#isEmpty empty}.
     */
    @UML(identifier="parsedName", obligation=MANDATORY, specification=ISO_19103)
    List<? extends LocalName> getParsedNames();

    /**
     * Returns the first element in the sequence of {@linkplain #getParsedNames parsed names}.
     * For any {@link LocalName}, this is always {@code this}.
     * <p>
     * This method is similar in purpose to <code>{@linkplain javax.naming.Name#get(int)
     * Name.get}(0)</code> from the <cite>Java Naming and Directory Interface</cite>.
     * <p>
     * <b>Example</b>:
     * If {@code this} name is {@code "org.opengis.util.Record"} (no matter its
     * {@linkplain #scope scope}), then this method shall returns {@code "org"}.
     *
     * @return The first element in the list of {@linkplain #getParsedNames parsed names}.
     *
     * @since GeoAPI 2.2
     */
    @UML(identifier="ScopedName.head", obligation=MANDATORY, specification=ISO_19103)
    LocalName head();

    /**
     * Returns the last element in the sequence of {@linkplain #getParsedNames parsed names}.
     * For any {@link LocalName}, this is always {@code this}.
     * <p>
     * This method is similar in purpose to <code>{@linkplain javax.naming.Name#get(int)
     * Name.get}(size-1)</code> from the <cite>Java Naming and Directory Interface</cite>.
     * <p>
     * <b>Example</b>:
     * If {@code this} name is {@code "org.opengis.util.Record"} (no matter its
     * {@linkplain #scope scope}), then this method shall returns {@code "Record"}.
     *
     * @return The last element in the list of {@linkplain #getParsedNames parsed names}.
     *
     * @since GeoAPI 2.1
     */
    @Extension
    LocalName tip();

    /**
     * @deprecated Renamed as {@link #tip}.
     *
     * @return The last element in the list of {@linkplain #getParsedNames parsed names}.
     */
    @Extension
    @Deprecated
    LocalName name();

    /**
     * Returns a view of this name as a fully-qualified name. The {@linkplain #scope scope}
     * of a fully qualified name must be {@linkplain NameSpace#isGlobal global}. If the scope
     * of this name is already global, then this method shall returns {@code this}.
     * <p>
     * <b>Example</b>:
     * If {@code this} name is {@code "util.Record"} ({@linkplain #depth depth} of two) and its
     * {@linkplain #scope scope} has the {@linkplain NameSpace#name name} {@code "org.opengis"},
     * then the fully qualified name shall be {@code "org.opengis.util.Record"}.
     *
     * @return The fully-qualified name (never {@code null}).
     *
     * @since GeoAPI 2.1
     */
    @Extension
    GenericName toFullyQualifiedName();

    /**
     * Returns this name expanded with the specified scope. One may represent this operation
     * as a concatenation of the specified {@code scope} with {@code this}. In pseudo-code,
     * the following relationships must hold (the last one is specific to {@link ScopedName}):
     * <p>
     * <ul>
     *   <li><code>push(</code><var>foo</var><code> : LocalName).{@linkplain #head}</code>
     *       {@linkplain #equals equals} <var>foo</var></li>
     *
     *   <li><code>push(</code><var>foo</var><code> : LocalName).{@linkplain ScopedName#tail tail()}</code>
     *       {@linkplain #equals equals} <var>this</var></li>
     *
     *   <li><code>push(</code><var>foo</var><code> : GenericName).{@linkplain #scope}</code>
     *       {@linkplain #equals equals} <var>foo</var>.{@link #scope()}</li>
     *
     *   <li><code>push(</code><var>foo</var><code> : GenericName).{@linkplain #getParsedNames}</code>
     *       {@linkplain List#equals equals} <var>foo</var>.<code>getParsedNames().{@linkplain
     *       List#addAll addAll}(</code><var>this</var>.<code>getParsedNames())</code></li>
     * </ul>
     * <p>
     * This method is similar in purpose to <code>{@linkplain javax.naming.Name#addAll(int,javax.naming.Name)
     * Name.addAll}(0,name)</code> from the <cite>Java Naming and Directory Interface</cite>.
     * <p>
     * <b>Example</b>:
     * If {@code this} name is {@code "util.Record"} and the given {@code scope} argument is
     * {@code "org.opengis"}, then {@code this.push(scope)} shall returns
     * {@code "org.opengis.util.Record"}.
     *
     * @param scope The name to use as prefix.
     * @return A concatenation of the given name with this name.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="push", obligation=MANDATORY, specification=ISO_19103)
    ScopedName push(GenericName scope);

    /**
     * Returns a string representation of this generic name. This string representation is
     * local-independant. It contains all elements listed by {@link #getParsedNames} separated
     * by a namespace-dependant character (usually {@code :} or {@code /}). This rule implies
     * that the result may or may not be fully qualified. Special cases:
     * <p>
     * <ul>
     *   <li><code>{@linkplain #toFullyQualifiedName}.toString()</code> is garanteed to
     *       contains the {@linkplain #scope scope} (if any).</li>
     *   <li><code>{@linkplain #name}.toString()</code> is garanteed to <strong>not</strong>
     *       contains any scope.</li>
     * </ul>
     *
     * @return A local-independant string representation of this name.
     */
/// @Override
    @Extension
    String toString();

    /**
     * Returns a local-dependent string representation of this generic name. This string
     * is similar to the one returned by {@link #toString} except that each element has
     * been localized in the {@linkplain InternationalString#toString(java.util.Locale)
     * specified locale}. If no international string is available, then this method shall
     * returns an implementation mapping to {@link #toString} for all locales.
     * <p>
     * <b>Example</b>:
     * An implementation may want to localize the {@code "My Documents"} directory name
     * into {@code "Mes Documents"} on French installation of Windows operating system.
     *
     * @return A localizable string representation of this name.
     */
    @Extension
    InternationalString toInternationalString();
}
