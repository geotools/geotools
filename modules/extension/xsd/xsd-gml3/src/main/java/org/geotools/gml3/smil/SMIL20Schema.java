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
package org.geotools.gml3.smil;

import java.util.Collections;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.xs.XSSchema;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;


public class SMIL20Schema extends SchemaImpl {
    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="restartDefaultType"&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="never"/&gt;
     *          &lt;enumeration value="always"/&gt;
     *          &lt;enumeration value="whenNotActive"/&gt;
     *          &lt;enumeration value="inherit"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType RESTARTDEFAULTTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "restartDefaultType"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, XSSchema.STRING_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="fillTimingAttrsType"&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="remove"/&gt;
     *          &lt;enumeration value="freeze"/&gt;
     *          &lt;enumeration value="hold"/&gt;
     *          &lt;enumeration value="auto"/&gt;
     *          &lt;enumeration value="default"/&gt;
     *          &lt;enumeration value="transition"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType FILLTIMINGATTRSTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "fillTimingAttrsType"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, XSSchema.STRING_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="animateMotionPrototype"&gt;
     *      &lt;attributeGroup ref="smil20:animAddAccumAttrs"/&gt;
     *      &lt;attributeGroup ref="smil20:animValuesAttrs"/&gt;
     *      &lt;attribute name="origin" type="string" use="optional"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ANIMATEMOTIONPROTOTYPE_TYPE = new ComplexTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "animateMotionPrototype"),
            Collections.EMPTY_LIST, false, false, Collections.EMPTY_LIST, XSSchema.ANYTYPE_TYPE,
            null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="animateColorPrototype"&gt;
     *      &lt;attributeGroup ref="smil20:animNamedTargetAttrs"/&gt;
     *      &lt;attributeGroup ref="smil20:animAddAccumAttrs"/&gt;
     *      &lt;attributeGroup ref="smil20:animValuesAttrs"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ANIMATECOLORPROTOTYPE_TYPE = new ComplexTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "animateColorPrototype"), Collections.EMPTY_LIST,
            false, false, Collections.EMPTY_LIST, XSSchema.ANYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="nonNegativeDecimalType"&gt;
     *      &lt;restriction base="decimal"&gt;
     *          &lt;minInclusive value="0.0"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NONNEGATIVEDECIMALTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "nonNegativeDecimalType"),
            java.lang.Object.class, false, false, Collections.EMPTY_LIST, XSSchema.DECIMAL_TYPE,
            null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="fillDefaultType"&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="remove"/&gt;
     *          &lt;enumeration value="freeze"/&gt;
     *          &lt;enumeration value="hold"/&gt;
     *          &lt;enumeration value="auto"/&gt;
     *          &lt;enumeration value="inherit"/&gt;
     *          &lt;enumeration value="transition"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType FILLDEFAULTTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "fillDefaultType"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, XSSchema.STRING_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="animatePrototype"&gt;
     *      &lt;attributeGroup ref="smil20:animNamedTargetAttrs"/&gt;
     *      &lt;attributeGroup ref="smil20:animAddAccumAttrs"/&gt;
     *      &lt;attributeGroup ref="smil20:animValuesAttrs"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ANIMATEPROTOTYPE_TYPE = new ComplexTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "animatePrototype"), Collections.EMPTY_LIST,
            false, false, Collections.EMPTY_LIST, XSSchema.ANYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="restartTimingType"&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="never"/&gt;
     *          &lt;enumeration value="always"/&gt;
     *          &lt;enumeration value="whenNotActive"/&gt;
     *          &lt;enumeration value="default"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType RESTARTTIMINGTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "restartTimingType"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, XSSchema.STRING_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="syncBehaviorDefaultType"&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="canSlip"/&gt;
     *          &lt;enumeration value="locked"/&gt;
     *          &lt;enumeration value="independent"/&gt;
     *          &lt;enumeration value="inherit"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType SYNCBEHAVIORDEFAULTTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "syncBehaviorDefaultType"),
            java.lang.Object.class, false, false, Collections.EMPTY_LIST, XSSchema.STRING_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="syncBehaviorType"&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="canSlip"/&gt;
     *          &lt;enumeration value="locked"/&gt;
     *          &lt;enumeration value="independent"/&gt;
     *          &lt;enumeration value="default"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType SYNCBEHAVIORTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "syncBehaviorType"), java.lang.Object.class,
            false, false, Collections.EMPTY_LIST, XSSchema.STRING_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="setPrototype"&gt;
     *      &lt;attributeGroup ref="smil20:animNamedTargetAttrs"/&gt;
     *      &lt;attributeGroup ref="smil20:animSetValuesAttrs"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SETPROTOTYPE_TYPE = new ComplexTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/", "setPrototype"), Collections.EMPTY_LIST, false,
            false, Collections.EMPTY_LIST, XSSchema.ANYTYPE_TYPE, null);

    public SMIL20Schema() {
        super("http://www.w3.org/2001/SMIL20/");

        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "restartDefaultType"),
            RESTARTDEFAULTTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "fillTimingAttrsType"),
            FILLTIMINGATTRSTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "animateMotionPrototype"),
            ANIMATEMOTIONPROTOTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "animateColorPrototype"),
            ANIMATECOLORPROTOTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "nonNegativeDecimalType"),
            NONNEGATIVEDECIMALTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "fillDefaultType"), FILLDEFAULTTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "animatePrototype"), ANIMATEPROTOTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "restartTimingType"), RESTARTTIMINGTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "syncBehaviorDefaultType"),
            SYNCBEHAVIORDEFAULTTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "syncBehaviorType"), SYNCBEHAVIORTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/", "setPrototype"), SETPROTOTYPE_TYPE);
    }
}
