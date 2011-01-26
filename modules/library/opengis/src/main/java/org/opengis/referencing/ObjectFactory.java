/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing;

import java.util.Map;
import java.util.Locale;
import java.util.Properties;
import java.util.Collections;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.util.InternationalString;
import org.opengis.util.GenericName;
import org.opengis.annotation.Extension;


/**
 * Base interface for all factories of {@linkplain IdentifiedObject identified objects}. Factories
 * build up complex objects from simpler objects or values. This factory allows applications to make
 * {@linkplain org.opengis.referencing.cs.CoordinateSystem coordinate systems},
 * {@linkplain org.opengis.referencing.datum.Datum datum} or
 * {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem coordinate reference systems}
 * that cannot be created by an {@linkplain AuthorityFactory authority factory}. This factory is
 * very flexible, whereas the authority factory is easier to use.
 * <p>
 * <b>Object properties</b><br>
 * Most factory methods expect a {@link Map} argument. The map can be a {@link Properties} instance.
 * The map shall contains at least a {@code "name"} property. In the common case where the name is
 * the only property, the map may be constructed with
 *
 * <code>Collections.{@linkplain Collections#singletonMap singletonMap}("name", <var>theName</var>)</code>
 *
 * where <var>theName</var> is an arbitrary name as free text.
 * <p>
 * Implementations are encouraged to recognize at least the properties listed in the following
 * table. Additional implementation-specific properties can be added. Unknown properties shall
 * be ignored.
 * <p>
 * <table border='1'>
 *   <tr bgcolor="#CCCCFF" class="TableHeadingColor">
 *     <th nowrap>Property name</th>
 *     <th nowrap>Value type</th>
 *     <th nowrap>Value given to</th>
 *   </tr>
 *   <tr>
 *     <td nowrap>&nbsp;{@value org.opengis.referencing.IdentifiedObject#NAME_KEY}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link org.opengis.referencing.ReferenceIdentifier} or {@link String}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link IdentifiedObject#getName}</td>
 *   </tr>
 *   <tr>
 *     <td nowrap>&nbsp;{@value org.opengis.referencing.IdentifiedObject#ALIAS_KEY}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link String}, <code>{@linkplain String}[]</code>,
 *     {@link GenericName} or <code>{@linkplain GenericName}[]</code>&nbsp;</td>
 *     <td nowrap>&nbsp;{@link IdentifiedObject#getAlias}</td>
 *   </tr>
 *   <tr>
 *     <td nowrap>&nbsp;{@value org.opengis.metadata.Identifier#AUTHORITY_KEY}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link String} or {@link Citation}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link Identifier#getAuthority} on the {@linkplain IdentifiedObject#getName name}</td>
 *   </tr>
 *   <tr>
 *     <td nowrap>&nbsp;{@value org.opengis.referencing.ReferenceIdentifier#CODESPACE_KEY}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link String}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link ReferenceIdentifier#getCodeSpace} on the {@linkplain IdentifiedObject#getName name}</td>
 *   </tr>
 *   <tr>
 *     <td nowrap>&nbsp;{@value org.opengis.referencing.ReferenceIdentifier#VERSION_KEY}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link String}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link ReferenceIdentifier#getVersion} on the {@linkplain IdentifiedObject#getName name}</td>
 *   </tr>
 *   <tr>
 *     <td nowrap>&nbsp;{@value org.opengis.referencing.IdentifiedObject#IDENTIFIERS_KEY}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link Identifier} or <code>{@linkplain Identifier}[]</code>&nbsp;</td>
 *     <td nowrap>&nbsp;{@link IdentifiedObject#getIdentifiers}</td>
 *   </tr>
 *   <tr>
 *     <td nowrap>&nbsp;{@value org.opengis.referencing.IdentifiedObject#REMARKS_KEY}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link String} or {@link InternationalString}&nbsp;</td>
 *     <td nowrap>&nbsp;{@link IdentifiedObject#getRemarks}</td>
 *   </tr>
 * </table>
 * <p>
 * The {@code "name"} property is mandatory. All others are optional. All localizable attributes
 * like {@code "remarks"} can have a language and country code suffix. For example the
 * {@code "remarks_fr"} property stands for remarks in {@linkplain Locale#FRENCH French} and the
 * {@code "remarks_fr_CA"} property stands for remarks in {@linkplain Locale#CANADA_FRENCH French Canadian}.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@Extension
public interface ObjectFactory extends Factory {
}
