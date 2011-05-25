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
package org.geotools.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.ProfileImpl;
import org.geotools.feature.type.SchemaImpl;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;


/**
 * Schema for XML simple types.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class XSSchema extends SchemaImpl {
    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="anyType" name="anyType"&gt;
     *      &lt;xs:restriction base="xs:anyType"/&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ANYTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "anyType"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, null, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="anySimpleType" name="anySimpleType"&gt;
     *      &lt;xs:restriction base="xs:anyType"/&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ANYSIMPLETYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "anySimpleType"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, ANYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="NOTATION" name="NOTATION"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="length"/&gt;
     *              &lt;hfp:hasFacet name="minLength"/&gt;
     *              &lt;hfp:hasFacet name="maxLength"/&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="false"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#NOTATION"/&gt;
     *          &lt;xs:documentation&gt;
     *          NOTATION cannot be used directly in a schema; rather a type
     *          must be derived from it by specifying at least one enumeration
     *          facet whose value is the name of a NOTATION declared in the
     *          schema.
     *        &lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="NOTATION.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NOTATION_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "NOTATION"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="decimal" name="decimal"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="totalDigits"/&gt;
     *              &lt;hfp:hasFacet name="fractionDigits"/&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="total"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="true"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#decimal"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="decimal.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DECIMAL_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "decimal"), BigDecimal.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="integer" name="integer"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#integer"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:decimal"&gt;
     *          &lt;xs:fractionDigits fixed="true" id="integer.fractionDigits" value="0"/&gt;
     *          &lt;xs:pattern value="[\-+]?[0-9]+"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType INTEGER_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "integer"), BigInteger.class, false, false,
            Collections.EMPTY_LIST, DECIMAL_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="nonNegativeInteger" name="nonNegativeInteger"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#nonNegativeInteger"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:integer"&gt;
     *          &lt;xs:minInclusive id="nonNegativeInteger.minInclusive" value="0"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NONNEGATIVEINTEGER_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "nonNegativeInteger"), BigInteger.class, false,
            false, Collections.EMPTY_LIST, INTEGER_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="unsignedLong" name="unsignedLong"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasProperty name="bounded" value="true"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="finite"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#unsignedLong"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:nonNegativeInteger"&gt;
     *          &lt;xs:maxInclusive id="unsignedLong.maxInclusive" value="18446744073709551615"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType UNSIGNEDLONG_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "unsignedLong"), BigInteger.class, false,
            false, Collections.EMPTY_LIST, NONNEGATIVEINTEGER_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="unsignedInt" name="unsignedInt"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#unsignedInt"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:unsignedLong"&gt;
     *          &lt;xs:maxInclusive id="unsignedInt.maxInclusive" value="4294967295"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType UNSIGNEDINT_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "unsignedInt"), Long.class, false, false,
            Collections.EMPTY_LIST, UNSIGNEDLONG_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="unsignedShort" name="unsignedShort"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#unsignedShort"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:unsignedInt"&gt;
     *          &lt;xs:maxInclusive id="unsignedShort.maxInclusive" value="65535"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType UNSIGNEDSHORT_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "unsignedShort"), Integer.class, false, false,
            Collections.EMPTY_LIST, UNSIGNEDINT_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="unsignedByte" name="unsignedByte"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#unsignedByte"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:unsignedShort"&gt;
     *          &lt;xs:maxInclusive id="unsignedByte.maxInclusive" value="255"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType UNSIGNEDBYTE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "unsignedByte"), Short.class, false, false,
            Collections.EMPTY_LIST, UNSIGNEDSHORT_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="QName" name="QName"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="length"/&gt;
     *              &lt;hfp:hasFacet name="minLength"/&gt;
     *              &lt;hfp:hasFacet name="maxLength"/&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="false"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#QName"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="QName.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType QNAME_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "QName"), QName.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="string" name="string"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="length"/&gt;
     *              &lt;hfp:hasFacet name="minLength"/&gt;
     *              &lt;hfp:hasFacet name="maxLength"/&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="false"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#string"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace id="string.preserve" value="preserve"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType STRING_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="normalizedString" name="normalizedString"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#normalizedString"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:string"&gt;
     *          &lt;xs:whiteSpace id="normalizedString.whiteSpace" value="replace"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NORMALIZEDSTRING_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "normalizedString"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, STRING_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="token" name="token"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#token"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:normalizedString"&gt;
     *          &lt;xs:whiteSpace id="token.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType TOKEN_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "token"), java.lang.Object.class, false, false,
            Collections.EMPTY_LIST, NORMALIZEDSTRING_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="Name" name="Name"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#Name"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:token"&gt;
     *          &lt;xs:pattern id="Name.pattern" value="\i\c*"&gt;
     *              &lt;xs:annotation&gt;
     *                  &lt;xs:documentation source="http://www.w3.org/TR/REC-xml#NT-Name"&gt;
     *              pattern matches production 5 from the XML spec
     *            &lt;/xs:documentation&gt;
     *              &lt;/xs:annotation&gt;
     *          &lt;/xs:pattern&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NAME_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "Name"), java.lang.Object.class, false, false,
            Collections.EMPTY_LIST, TOKEN_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="NCName" name="NCName"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#NCName"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:Name"&gt;
     *          &lt;xs:pattern id="NCName.pattern" value="[\i-[:]][\c-[:]]*"&gt;
     *              &lt;xs:annotation&gt;
     *                  &lt;xs:documentation source="http://www.w3.org/TR/REC-xml-names/#NT-NCName"&gt;
     *              pattern matches production 4 from the Namespaces in XML spec
     *            &lt;/xs:documentation&gt;
     *              &lt;/xs:annotation&gt;
     *          &lt;/xs:pattern&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NCNAME_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "NCName"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, NAME_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="ENTITY" name="ENTITY"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#ENTITY"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:NCName"/&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ENTITY_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "ENTITY"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, NCNAME_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="simpleDerivationSet"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;
     *     #all or (possibly empty) subset of {restriction, union, list}
     *     &lt;/xs:documentation&gt;
     *          &lt;xs:documentation&gt;
     *     A utility type, not for public use&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:union&gt;
     *          &lt;xs:simpleType&gt;
     *              &lt;xs:restriction base="xs:token"&gt;
     *                  &lt;xs:enumeration value="#all"/&gt;
     *              &lt;/xs:restriction&gt;
     *          &lt;/xs:simpleType&gt;
     *          &lt;xs:simpleType&gt;
     *              &lt;xs:restriction base="xs:derivationControl"&gt;
     *                  &lt;xs:enumeration value="list"/&gt;
     *                  &lt;xs:enumeration value="union"/&gt;
     *                  &lt;xs:enumeration value="restriction"/&gt;
     *              &lt;/xs:restriction&gt;
     *          &lt;/xs:simpleType&gt;
     *      &lt;/xs:union&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType SIMPLEDERIVATIONSET_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "simpleDerivationSet"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="public"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;
     *     A utility type, not for public use&lt;/xs:documentation&gt;
     *          &lt;xs:documentation&gt;
     *     A public identifier, per ISO 8879&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:token"/&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType PUBLIC_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "public"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, TOKEN_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="language" name="language"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#language"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:token"&gt;
     *          &lt;xs:pattern id="language.pattern" value="[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*"&gt;
     *              &lt;xs:annotation&gt;
     *                  &lt;xs:documentation source="http://www.w3.org/TR/REC-xml#NT-LanguageID"&gt;
     *              pattern specifies the content of section 2.12 of XML 1.0e2
     *              and RFC 3066 (Revised version of RFC 1766).
     *            &lt;/xs:documentation&gt;
     *              &lt;/xs:annotation&gt;
     *          &lt;/xs:pattern&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType LANGUAGE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "language"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, TOKEN_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="duration" name="duration"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="partial"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#duration"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="duration.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DURATION_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "duration"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="blockSet"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;
     *      A utility type, not for public use&lt;/xs:documentation&gt;
     *          &lt;xs:documentation&gt;
     *      #all or (possibly empty) subset of {substitution, extension,
     *      restriction}&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:union&gt;
     *          &lt;xs:simpleType&gt;
     *              &lt;xs:restriction base="xs:token"&gt;
     *                  &lt;xs:enumeration value="#all"/&gt;
     *              &lt;/xs:restriction&gt;
     *          &lt;/xs:simpleType&gt;
     *          &lt;xs:simpleType&gt;
     *              &lt;xs:list&gt;
     *                  &lt;xs:simpleType&gt;
     *                      &lt;xs:restriction base="xs:derivationControl"&gt;
     *                          &lt;xs:enumeration value="extension"/&gt;
     *                          &lt;xs:enumeration value="restriction"/&gt;
     *                          &lt;xs:enumeration value="substitution"/&gt;
     *                      &lt;/xs:restriction&gt;
     *                  &lt;/xs:simpleType&gt;
     *              &lt;/xs:list&gt;
     *          &lt;/xs:simpleType&gt;
     *      &lt;/xs:union&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType BLOCKSET_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "blockSet"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="float" name="float"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="total"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="true"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="finite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="true"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#float"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="float.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType FLOAT_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "float"), Float.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="dateTime" name="dateTime"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="partial"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#dateTime"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="dateTime.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DATETIME_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "dateTime"), Timestamp.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="gYearMonth" name="gYearMonth"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="partial"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#gYearMonth"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="gYearMonth.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType GYEARMONTH_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "gYearMonth"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="ID" name="ID"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#ID"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:NCName"/&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ID_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "ID"), java.lang.Object.class, false, false,
            Collections.EMPTY_LIST, NCNAME_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="NMTOKEN" name="NMTOKEN"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#NMTOKEN"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:token"&gt;
     *          &lt;xs:pattern id="NMTOKEN.pattern" value="\c+"&gt;
     *              &lt;xs:annotation&gt;
     *                  &lt;xs:documentation source="http://www.w3.org/TR/REC-xml#NT-Nmtoken"&gt;
     *              pattern matches production 7 from the XML spec
     *            &lt;/xs:documentation&gt;
     *              &lt;/xs:annotation&gt;
     *          &lt;/xs:pattern&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NMTOKEN_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "NMTOKEN"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, TOKEN_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="derivationControl"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;
     *     A utility type, not for public use&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:NMTOKEN"&gt;
     *          &lt;xs:enumeration value="substitution"/&gt;
     *          &lt;xs:enumeration value="extension"/&gt;
     *          &lt;xs:enumeration value="restriction"/&gt;
     *          &lt;xs:enumeration value="list"/&gt;
     *          &lt;xs:enumeration value="union"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DERIVATIONCONTROL_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "derivationControl"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, NMTOKEN_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="reducedDerivationControl"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;
     *     A utility type, not for public use&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:derivationControl"&gt;
     *          &lt;xs:enumeration value="extension"/&gt;
     *          &lt;xs:enumeration value="restriction"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType REDUCEDDERIVATIONCONTROL_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "reducedDerivationControl"),
            java.lang.Object.class, false, false, Collections.EMPTY_LIST, DERIVATIONCONTROL_TYPE,
            null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="nonPositiveInteger" name="nonPositiveInteger"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#nonPositiveInteger"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:integer"&gt;
     *          &lt;xs:maxInclusive id="nonPositiveInteger.maxInclusive" value="0"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NONPOSITIVEINTEGER_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "nonPositiveInteger"), BigInteger.class, false,
            false, Collections.EMPTY_LIST, INTEGER_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="negativeInteger" name="negativeInteger"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#negativeInteger"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:nonPositiveInteger"&gt;
     *          &lt;xs:maxInclusive id="negativeInteger.maxInclusive" value="-1"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NEGATIVEINTEGER_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "negativeInteger"), BigInteger.class, false,
            false, Collections.EMPTY_LIST, NONPOSITIVEINTEGER_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="gMonth" name="gMonth"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="partial"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#gMonth"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="gMonth.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType GMONTH_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "gMonth"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="formChoice"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;
     *     A utility type, not for public use&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:NMTOKEN"&gt;
     *          &lt;xs:enumeration value="qualified"/&gt;
     *          &lt;xs:enumeration value="unqualified"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType FORMCHOICE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "formChoice"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, NMTOKEN_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="positiveInteger" name="positiveInteger"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#positiveInteger"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:nonNegativeInteger"&gt;
     *          &lt;xs:minInclusive id="positiveInteger.minInclusive" value="1"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType POSITIVEINTEGER_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "positiveInteger"), BigInteger.class, false,
            false, Collections.EMPTY_LIST, NONNEGATIVEINTEGER_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="base64Binary" name="base64Binary"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="length"/&gt;
     *              &lt;hfp:hasFacet name="minLength"/&gt;
     *              &lt;hfp:hasFacet name="maxLength"/&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="false"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#base64Binary"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="base64Binary.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType BASE64BINARY_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "base64Binary"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="double" name="double"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="total"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="true"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="finite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="true"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#double"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="double.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DOUBLE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "double"), Double.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="namespaceList"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;
     *     A utility type, not for public use&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:union&gt;
     *          &lt;xs:simpleType&gt;
     *              &lt;xs:restriction base="xs:token"&gt;
     *                  &lt;xs:enumeration value="##any"/&gt;
     *                  &lt;xs:enumeration value="##other"/&gt;
     *              &lt;/xs:restriction&gt;
     *          &lt;/xs:simpleType&gt;
     *          &lt;xs:simpleType&gt;
     *              &lt;xs:list&gt;
     *                  &lt;xs:simpleType&gt;
     *                      &lt;xs:union memberTypes="xs:anyURI"&gt;
     *                          &lt;xs:simpleType&gt;
     *                              &lt;xs:restriction base="xs:token"&gt;
     *                                  &lt;xs:enumeration value="##targetNamespace"/&gt;
     *                                  &lt;xs:enumeration value="##local"/&gt;
     *                              &lt;/xs:restriction&gt;
     *                          &lt;/xs:simpleType&gt;
     *                      &lt;/xs:union&gt;
     *                  &lt;/xs:simpleType&gt;
     *              &lt;/xs:list&gt;
     *          &lt;/xs:simpleType&gt;
     *      &lt;/xs:union&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NAMESPACELIST_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "namespaceList"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="gYear" name="gYear"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="partial"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#gYear"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="gYear.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType GYEAR_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "gYear"), java.lang.Object.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="derivationSet"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;
     *     A utility type, not for public use&lt;/xs:documentation&gt;
     *          &lt;xs:documentation&gt;
     *     #all or (possibly empty) subset of {extension, restriction}&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:union&gt;
     *          &lt;xs:simpleType&gt;
     *              &lt;xs:restriction base="xs:token"&gt;
     *                  &lt;xs:enumeration value="#all"/&gt;
     *              &lt;/xs:restriction&gt;
     *          &lt;/xs:simpleType&gt;
     *          &lt;xs:simpleType&gt;
     *              &lt;xs:list itemType="xs:reducedDerivationControl"/&gt;
     *          &lt;/xs:simpleType&gt;
     *      &lt;/xs:union&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DERIVATIONSET_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "derivationSet"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="gMonthDay" name="gMonthDay"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="partial"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#gMonthDay"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="gMonthDay.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType GMONTHDAY_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "gMonthDay"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="long" name="long"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasProperty name="bounded" value="true"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="finite"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#long"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:integer"&gt;
     *          &lt;xs:minInclusive id="long.minInclusive" value="-9223372036854775808"/&gt;
     *          &lt;xs:maxInclusive id="long.maxInclusive" value="9223372036854775807"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType LONG_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "long"), Long.class, false, false,
            Collections.EMPTY_LIST, INTEGER_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="int" name="int"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#int"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:long"&gt;
     *          &lt;xs:minInclusive id="int.minInclusive" value="-2147483648"/&gt;
     *          &lt;xs:maxInclusive id="int.maxInclusive" value="2147483647"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType INT_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "int"), Integer.class, false, false,
            Collections.EMPTY_LIST, LONG_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="short" name="short"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#short"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:int"&gt;
     *          &lt;xs:minInclusive id="short.minInclusive" value="-32768"/&gt;
     *          &lt;xs:maxInclusive id="short.maxInclusive" value="32767"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType SHORT_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "short"), Short.class, false, false,
            Collections.EMPTY_LIST, INT_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="byte" name="byte"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#byte"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:short"&gt;
     *          &lt;xs:minInclusive id="byte.minInclusive" value="-128"/&gt;
     *          &lt;xs:maxInclusive id="byte.maxInclusive" value="127"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType BYTE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "byte"), Byte.class, false, false,
            Collections.EMPTY_LIST, SHORT_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="boolean" name="boolean"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="false"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="finite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#boolean"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="boolean.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType BOOLEAN_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "boolean"), Boolean.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="anyURI" name="anyURI"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="length"/&gt;
     *              &lt;hfp:hasFacet name="minLength"/&gt;
     *              &lt;hfp:hasFacet name="maxLength"/&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="false"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#anyURI"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="anyURI.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ANYURI_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "anyURI"), URI.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="gDay" name="gDay"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="partial"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#gDay"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="gDay.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType GDAY_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "gDay"), java.lang.Object.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="time" name="time"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="partial"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#time"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="time.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType TIME_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "time"), Time.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="hexBinary" name="hexBinary"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="length"/&gt;
     *              &lt;hfp:hasFacet name="minLength"/&gt;
     *              &lt;hfp:hasFacet name="maxLength"/&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="false"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#binary"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="hexBinary.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType HEXBINARY_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "hexBinary"), byte[].class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="date" name="date"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:appinfo&gt;
     *              &lt;hfp:hasFacet name="pattern"/&gt;
     *              &lt;hfp:hasFacet name="enumeration"/&gt;
     *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
     *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
     *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
     *              &lt;hfp:hasFacet name="minInclusive"/&gt;
     *              &lt;hfp:hasFacet name="minExclusive"/&gt;
     *              &lt;hfp:hasProperty name="ordered" value="partial"/&gt;
     *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
     *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
     *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
     *          &lt;/xs:appinfo&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#date"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:anySimpleType"&gt;
     *          &lt;xs:whiteSpace fixed="true" id="date.whiteSpace" value="collapse"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DATE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "date"), java.sql.Date.class, false, false,
            Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="allNNI"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;
     *     for maxOccurs&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:union memberTypes="xs:nonNegativeInteger"&gt;
     *          &lt;xs:simpleType&gt;
     *              &lt;xs:restriction base="xs:NMTOKEN"&gt;
     *                  &lt;xs:enumeration value="unbounded"/&gt;
     *              &lt;/xs:restriction&gt;
     *          &lt;/xs:simpleType&gt;
     *      &lt;/xs:union&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ALLNNI_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "allNNI"), java.lang.Object.class, false,
            false, Collections.EMPTY_LIST, ANYSIMPLETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType id="IDREF" name="IDREF"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#IDREF"/&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:NCName"/&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType IDREF_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/XMLSchema", "IDREF"), java.lang.Object.class, false, false,
            Collections.EMPTY_LIST, NCNAME_TYPE, null);

    public XSSchema() {
        super("http://www.w3.org/2001/XMLSchema");

        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "anyType"), ANYTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "anySimpleType"), ANYSIMPLETYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "NOTATION"), NOTATION_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "decimal"), DECIMAL_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "integer"), INTEGER_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "nonNegativeInteger"),
            NONNEGATIVEINTEGER_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "unsignedLong"), UNSIGNEDLONG_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "unsignedInt"), UNSIGNEDINT_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "unsignedShort"), UNSIGNEDSHORT_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "unsignedByte"), UNSIGNEDBYTE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "QName"), QNAME_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "string"), STRING_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "normalizedString"), NORMALIZEDSTRING_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "token"), TOKEN_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "Name"), NAME_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "NCName"), NCNAME_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "ENTITY"), ENTITY_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "simpleDerivationSet"),
            SIMPLEDERIVATIONSET_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "public"), PUBLIC_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "language"), LANGUAGE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "duration"), DURATION_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "blockSet"), BLOCKSET_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "float"), FLOAT_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "dateTime"), DATETIME_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "gYearMonth"), GYEARMONTH_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "ID"), ID_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "NMTOKEN"), NMTOKEN_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "derivationControl"),
            DERIVATIONCONTROL_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "reducedDerivationControl"),
            REDUCEDDERIVATIONCONTROL_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "nonPositiveInteger"),
            NONPOSITIVEINTEGER_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "negativeInteger"), NEGATIVEINTEGER_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "gMonth"), GMONTH_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "formChoice"), FORMCHOICE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "positiveInteger"), POSITIVEINTEGER_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "base64Binary"), BASE64BINARY_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "double"), DOUBLE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "namespaceList"), NAMESPACELIST_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "gYear"), GYEAR_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "derivationSet"), DERIVATIONSET_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "gMonthDay"), GMONTHDAY_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "long"), LONG_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "int"), INT_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "short"), SHORT_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "byte"), BYTE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "boolean"), BOOLEAN_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "anyURI"), ANYURI_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "gDay"), GDAY_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "time"), TIME_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "hexBinary"), HEXBINARY_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "date"), DATE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "allNNI"), ALLNNI_TYPE);
        put(new NameImpl("http://www.w3.org/2001/XMLSchema", "IDREF"), IDREF_TYPE);
    }
    
    /**
     * Profile of XSSchema used to provide a unique mapping for Java classes.
     * <p>
     * The entries selected are:
     * <ul>
     * <li>XS.BYTE: Byte</li>
     * <li>XS.HEXBINARY)): byte</li>
     * <li>XS.SHORT: Short</li>
     * <li>XS.INT: Integer</li>
     * <li>XS.FLOAT: Float</li>
     * <li>XS.LONG: Long</li>
     * <li>XS.QNAME: Qname</li>
     * <li>XS.DATE: java.sql.Date</li>
     * <li>XS.DATETIME: java.sql.Timestamp</li>
     * <li>XS.TIME: java.sql.Time</li>
     * <li>XS.BOOLEAN: Boolean</li>
     * <li>XS.DOUBLE: Double</li>
     * <li>XS.STRING: String</li>
     * <li>XS.INTEGER: BigInteger</li>
     * <li>XS.DECIMAL: BigDecimal</li>
     * <li>XS.ANYURI: URI</li>
     * </ul>
     * This is just the profile used by GeoTools internally; you are free to make and use your own
     * profile when encoding/decoding.
     * <p>
     * 
     * @return Subset of XSSchema capturing a unique mapping for Java classes
     */
    public ProfileImpl profile() {
        Set<Name> profile = new LinkedHashSet<Name>();
        profile.add(new NameImpl(XS.BYTE)); // Byte.class
        profile.add(new NameImpl(XS.HEXBINARY)); // byte[].class
        profile.add(new NameImpl(XS.SHORT)); // Short.class
        profile.add(new NameImpl(XS.INT)); // Integer.class
        profile.add(new NameImpl(XS.FLOAT)); // Float.class
        profile.add(new NameImpl(XS.LONG)); // Long.class
        profile.add(new NameImpl(XS.QNAME)); // Qname.class
        profile.add(new NameImpl(XS.DATE)); // java.sql.Date.class
        profile.add(new NameImpl(XS.DATETIME)); // java.sql.Timestamp.class
        profile.add(new NameImpl(XS.TIME)); // java.sql.Time.class
        profile.add(new NameImpl(XS.BOOLEAN)); // Boolean.class
        profile.add(new NameImpl(XS.DOUBLE)); // Double.class
        profile.add(new NameImpl(XS.STRING)); // String.class
        profile.add(new NameImpl(XS.INTEGER)); // BigInteger.class
        profile.add(new NameImpl(XS.DECIMAL)); // BigDecimal.class
        profile.add(new NameImpl(XS.ANYURI)); // URI.class
        return new ProfileImpl(this, profile);
    }
    
}
