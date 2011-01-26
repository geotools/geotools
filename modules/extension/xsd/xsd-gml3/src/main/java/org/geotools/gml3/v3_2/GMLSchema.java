package org.geotools.gml3.v3_2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.geotools.feature.type.SchemaImpl;

import org.geotools.gml3.v3_2.gts.GTSSchema;
import org.geotools.gml3.v3_2.gco.GCOSchema;
import org.geotools.xs.XSSchema;
import org.geotools.gml3.v3_2.gsr.GSRSchema;
import org.geotools.gml3.v3_2.gmd.GMDSchema;
import org.geotools.xlink.XLINKSchema;

public class GMLSchema extends SchemaImpl {

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="doubleList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A type for a list of values of the respective simple type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="double"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DOUBLELIST_TYPE = build_DOUBLELIST_TYPE();
     
    private static AttributeType build_DOUBLELIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","doubleList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="NCNameList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A type for a list of values of the respective simple type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="NCName"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NCNAMELIST_TYPE = build_NCNAMELIST_TYPE();
     
    private static AttributeType build_NCNAMELIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","NCNameList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectPositionType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Direct position instances hold the coordinates for a position within some coordinate reference system (CRS). Since direct positions, as data types, will often be included in larger objects (such as geometry elements) that have references to CRS, the srsName attribute will in general be missing, if this particular direct position is included in a larger element with such a reference to a CRS. In this case, the CRS is implicitly assumed to take on the value of the containing object's CRS.
     *  if no srsName attribute is given, the CRS shall be specified as part of the larger context this geometry element is part of, typically a geometric object like a point, curve, etc.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:doubleList"&gt;
     *              &lt;attributeGroup ref="gml:SRSReferenceGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTPOSITIONTYPE_TYPE = build_DIRECTPOSITIONTYPE_TYPE();
    
    private static ComplexType build_DIRECTPOSITIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                NCNAMELIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","axisLabels"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","srsDimension"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","srsName"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NCNAMELIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","uomLabels"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectPositionType"), schema, false,
            false, Collections.<Filter>emptyList(), DOUBLELIST_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CoordinatesType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;This type is deprecated for tuples with ordinate values that are numbers.
     *  CoordinatesType is a text string, intended to be used to record an array of tuples or coordinates. 
     *  While it is not possible to enforce the internal structure of the string through schema validation, some optional attributes have been provided in previous versions of GML to support a description of the internal structure. These attributes are deprecated. The attributes were intended to be used as follows:
     *  Decimal	symbol used for a decimal point (default="." a stop or period)
     *  cs        	symbol used to separate components within a tuple or coordinate string (default="," a comma)
     *  ts        	symbol used to separate tuples or coordinate strings (default=" " a space)
     *  Since it is based on the XML Schema string type, CoordinatesType may be used in the construction of tables of tuples or arrays of tuples, including ones that contain mixed text and numeric values.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="string"&gt;
     *              &lt;attribute default="." name="decimal" type="string"/&gt;
     *              &lt;attribute default="," name="cs" type="string"/&gt;
     *              &lt;attribute default=" " name="ts" type="string"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COORDINATESTYPE_TYPE = build_COORDINATESTYPE_TYPE();
    
    private static ComplexType build_COORDINATESTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","decimal"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","cs"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ts"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CoordinatesType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EnvelopeType"&gt;
     *      &lt;choice&gt;
     *          &lt;sequence&gt;
     *              &lt;element name="lowerCorner" type="gml:DirectPositionType"/&gt;
     *              &lt;element name="upperCorner" type="gml:DirectPositionType"/&gt;
     *          &lt;/sequence&gt;
     *          &lt;element maxOccurs="2" minOccurs="2" ref="gml:pos"&gt;
     *              &lt;annotation&gt;
     *                  &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *              &lt;/annotation&gt;
     *          &lt;/element&gt;
     *          &lt;element ref="gml:coordinates"/&gt;
     *      &lt;/choice&gt;
     *      &lt;attributeGroup ref="gml:SRSReferenceGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ENVELOPETYPE_TYPE = build_ENVELOPETYPE_TYPE();
    
    private static ComplexType build_ENVELOPETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","lowerCorner"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","upperCorner"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 2, 2, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NCNAMELIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","axisLabels"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","srsDimension"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","srsName"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NCNAMELIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","uomLabels"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EnvelopeType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="NilReasonType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:NilReasonType defines a content model that allows recording of an explanation for a void value or other exception.
     *  gml:NilReasonType is a union of the following enumerated values:
     *  -	inapplicable there is no value
     *  -	missing the correct value is not readily available to the sender of this data. Furthermore, a correct value may not exist
     *  -	template the value will be available later
     *  -	unknown the correct value is not known to, and not computable by, the sender of this data. However, a correct value probably exists
     *  -	withheld the value is not divulged
     *  -	other:text other brief explanation, where text is a string of two or more characters with no included spaces
     *  and
     *  -	anyURI which should refer to a resource which describes the reason for the exception
     *  A particular community may choose to assign more detailed semantics to the standard values provided. Alternatively, the URI method enables a specific or more complete explanation for the absence of a value to be provided elsewhere and indicated by-reference in an instance document.
     *  gml:NilReasonType is used as a member of a union in a number of simple content types where it is necessary to permit a value from the NilReasonType union as an alternative to the primary type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;union memberTypes="gml:NilReasonEnumeration anyURI"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NILREASONTYPE_TYPE = build_NILREASONTYPE_TYPE();
     
    private static AttributeType build_NILREASONTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","NilReasonType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="BoundingShapeType"&gt;
     *      &lt;sequence&gt;
     *          &lt;choice&gt;
     *              &lt;element ref="gml:Envelope"/&gt;
     *              &lt;element ref="gml:Null"/&gt;
     *          &lt;/choice&gt;
     *      &lt;/sequence&gt;
     *      &lt;attribute name="nilReason" type="gml:NilReasonType"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType BOUNDINGSHAPETYPE_TYPE = build_BOUNDINGSHAPETYPE_TYPE();
    
    private static ComplexType build_BOUNDINGSHAPETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ENVELOPETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Envelope"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Null"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","BoundingShapeType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" mixed="true" name="AbstractMetaDataType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence/&gt;
     *      &lt;attribute ref="gml:id"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTMETADATATYPE_TYPE = build_ABSTRACTMETADATATYPE_TYPE();
    
    private static ComplexType build_ABSTRACTMETADATATYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ID_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","id"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractMetaDataType"), schema, false,
            true, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MetaDataPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractMetaData"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attribute name="about" type="anyURI"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType METADATAPROPERTYTYPE_TYPE = build_METADATAPROPERTYTYPE_TYPE();
    
    private static ComplexType build_METADATAPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTMETADATATYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractMetaData"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","about"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MetaDataPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ReferenceType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:ReferenceType is intended to be used in application schemas directly, if a property element shall use a "by-reference only" encoding.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType REFERENCETYPE_TYPE = build_REFERENCETYPE_TYPE();
    
    private static ComplexType build_REFERENCETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ReferenceType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CodeType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CodeType is a generalized type to be used for a term, keyword or name.
     *  It adds a XML attribute codeSpace to a term, where the value of the codeSpace attribute (if present) shall indicate a dictionary, thesaurus, classification scheme, authority, or pattern for the term.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="string"&gt;
     *              &lt;attribute name="codeSpace" type="anyURI"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODETYPE_TYPE = build_CODETYPE_TYPE();
    
    private static ComplexType build_CODETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","codeSpace"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CodeType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CodeWithAuthorityType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CodeWithAuthorityType requires that the codeSpace attribute is provided in an instance.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;restriction base="gml:CodeType"&gt;
     *              &lt;attribute name="codeSpace" type="anyURI" use="required"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODEWITHAUTHORITYTYPE_TYPE = build_CODEWITHAUTHORITYTYPE_TYPE();
    
    private static ComplexType build_CODEWITHAUTHORITYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","codeSpace"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CodeWithAuthorityType"), schema, false,
            false, Collections.<Filter>emptyList(), CODETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="StringOrRefType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="string"&gt;
     *              &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType STRINGORREFTYPE_TYPE = build_STRINGORREFTYPE_TYPE();
    
    private static ComplexType build_STRINGORREFTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","StringOrRefType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGMLType"&gt;
     *      &lt;sequence&gt;
     *          &lt;group ref="gml:StandardObjectProperties"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attribute ref="gml:id" use="required"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGMLTYPE_TYPE = build_ABSTRACTGMLTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGMLTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                METADATAPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","metaDataProperty"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","description"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","descriptionReference"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODEWITHAUTHORITYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","identifier"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","name"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ID_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","id"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGMLType"), schema, false,
            true, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGeometryType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;All geometry elements are derived directly or indirectly from this abstract supertype. A geometry element may have an identifying attribute (gml:id), one or more names (elements identifier and name) and a description (elements description and descriptionReference) . It may be associated with a spatial reference system (attribute group gml:SRSReferenceGroup).
     *  The following rules shall be adhered to:
     *  -	Every geometry type shall derive from this abstract type.
     *  -	Every geometry element (i.e. an element of a geometry type) shall be directly or indirectly in the substitution group of AbstractGeometry.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGMLType"&gt;
     *              &lt;attributeGroup ref="gml:SRSReferenceGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ABSTRACTGEOMETRYTYPE_TYPE = build_ABSTRACTGEOMETRYTYPE_TYPE();
     
    private static AttributeType build_ABSTRACTGEOMETRYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometryType"), com.vividsolutions.jts.geom.Geometry.class, false,
            true, Collections.<Filter>emptyList(), ABSTRACTGMLTYPE_TYPE, null
        );
        return builtType;
    }

    
    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LocationPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;choice&gt;
     *              &lt;element ref="gml:AbstractGeometry"/&gt;
     *              &lt;element ref="gml:LocationKeyWord"/&gt;
     *              &lt;element ref="gml:LocationString"/&gt;
     *              &lt;element ref="gml:Null"/&gt;
     *          &lt;/choice&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType LOCATIONPROPERTYTYPE_TYPE = build_LOCATIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_LOCATIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGEOMETRYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometry"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","LocationKeyWord"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","LocationString"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Null"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","LocationPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractFeatureType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;The basic feature model is given by the gml:AbstractFeatureType.
     *  The content model for gml:AbstractFeatureType adds two specific properties suitable for geographic features to the content model defined in gml:AbstractGMLType. 
     *  The value of the gml:boundedBy property describes an envelope that encloses the entire feature instance, and is primarily useful for supporting rapid searching for features that occur in a particular location. 
     *  The value of the gml:location property describes the extent, position or relative location of the feature.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGMLType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:boundedBy"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:location"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTFEATURETYPE_TYPE = build_ABSTRACTFEATURETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTFEATURETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                BOUNDINGSHAPETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","boundedBy"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                LOCATIONPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","location"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractFeatureType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTGMLTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="BoundedFeatureType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:AbstractFeatureType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;group ref="gml:StandardObjectProperties"/&gt;
     *                  &lt;element ref="gml:boundedBy"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:location"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType BOUNDEDFEATURETYPE_TYPE = build_BOUNDEDFEATURETYPE_TYPE();
    
    private static ComplexType build_BOUNDEDFEATURETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                METADATAPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","metaDataProperty"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","description"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","descriptionReference"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODEWITHAUTHORITYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","identifier"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","name"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                BOUNDINGSHAPETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","boundedBy"), 1, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                LOCATIONPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","location"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","BoundedFeatureType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTFEATURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="_coordinateOperationAccuracy"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gmd:AbstractDQ_PositionalAccuracy"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType _COORDINATEOPERATIONACCURACY_TYPE = build__COORDINATEOPERATIONACCURACY_TYPE();
    
    private static ComplexType build__COORDINATEOPERATIONACCURACY_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GMDSchema.ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE, new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_PositionalAccuracy"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","_coordinateOperationAccuracy"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DefinitionBaseType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:AbstractGMLType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:metaDataProperty"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:description"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:descriptionReference"/&gt;
     *                  &lt;element ref="gml:identifier"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:name"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute ref="gml:id" use="required"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DEFINITIONBASETYPE_TYPE = build_DEFINITIONBASETYPE_TYPE();
    
    private static ComplexType build_DEFINITIONBASETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                METADATAPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","metaDataProperty"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","description"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","descriptionReference"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODEWITHAUTHORITYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","identifier"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","name"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ID_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","id"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DefinitionBaseType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGMLTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DefinitionType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:DefinitionBaseType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:remarks"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DEFINITIONTYPE_TYPE = build_DEFINITIONTYPE_TYPE();
    
    private static ComplexType build_DEFINITIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remarks"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DefinitionType"), schema, false,
            false, Collections.<Filter>emptyList(), DEFINITIONBASETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="IdentifiedObjectType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:IdentifiedObjectType provides identification properties of a CRS-related object. In gml:DefinitionType, the gml:identifier element shall be the primary name by which this object is identified, encoding the "name" attribute in the UML model.
     *  Zero or more of the gml:name elements can be an unordered set of "identifiers", encoding the "identifier" attribute in the UML model. Each of these gml:name elements can reference elsewhere the object's defining information or be an identifier by which this object can be referenced.
     *  Zero or more other gml:name elements can be an unordered set of "alias" alternative names by which this CRS related object is identified, encoding the "alias" attributes in the UML model. An object may have several aliases, typically used in different contexts. The context for an alias is indicated by the value of its (optional) codeSpace attribute.
     *  Any needed version information shall be included in the codeSpace attribute of a gml:identifier and gml:name elements. In this use, the gml:remarks element in the gml:DefinitionType shall contain comments on or information about this object, including data source information.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:DefinitionType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType IDENTIFIEDOBJECTTYPE_TYPE = build_IDENTIFIEDOBJECTTYPE_TYPE();
    
    private static ComplexType build_IDENTIFIEDOBJECTTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","IdentifiedObjectType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), DEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="_domainOfValidity"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gmd:EX_Extent"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType _DOMAINOFVALIDITY_TYPE = build__DOMAINOFVALIDITY_TYPE();
    
    private static ComplexType build__DOMAINOFVALIDITY_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GMDSchema.EX_EXTENT_TYPE_TYPE, new NameImpl("http://www.isotc211.org/2005/gmd","EX_Extent"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","_domainOfValidity"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractCRSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:domainOfValidity"/&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:scope"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTCRSTYPE_TYPE = build_ABSTRACTCRSTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                _DOMAINOFVALIDITY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","domainOfValidity"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","scope"), 1, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractCRSType"), schema, false,
            true, Collections.<Filter>emptyList(), IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CRSPropertyType is a property type for association roles to a CRS abstract coordinate reference system, either referencing or containing the definition of that CRS.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CRSPROPERTYTYPE_TYPE = build_CRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_CRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractCoordinateOperationType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:domainOfValidity"/&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:scope"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:operationVersion"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:coordinateOperationAccuracy"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:sourceCRS"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:targetCRS"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTCOORDINATEOPERATIONTYPE_TYPE = build_ABSTRACTCOORDINATEOPERATIONTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTCOORDINATEOPERATIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                _DOMAINOFVALIDITY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","domainOfValidity"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","scope"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","operationVersion"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                _COORDINATEOPERATIONACCURACY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinateOperationAccuracy"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CRSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","sourceCRS"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CRSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","targetCRS"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractCoordinateOperationType"), schema, false,
            true, Collections.<Filter>emptyList(), IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CoordinateOperationPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CoordinateOperationPropertyType is a property type for association roles to a coordinate operation, either referencing or containing the definition of that coordinate operation.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractCoordinateOperation"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COORDINATEOPERATIONPROPERTYTYPE_TYPE = build_COORDINATEOPERATIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_COORDINATEOPERATIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTCOORDINATEOPERATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractCoordinateOperation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CoordinateOperationPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType final="#all" name="AggregationType"&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="set"/&gt;
     *          &lt;enumeration value="bag"/&gt;
     *          &lt;enumeration value="sequence"/&gt;
     *          &lt;enumeration value="array"/&gt;
     *          &lt;enumeration value="record"/&gt;
     *          &lt;enumeration value="table"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType AGGREGATIONTYPE_TYPE = build_AGGREGATIONTYPE_TYPE();
     
    private static AttributeType build_AGGREGATIONTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AggregationType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PassThroughOperationType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateOperationType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:modifiedCoordinate"/&gt;
     *                  &lt;element ref="gml:coordOperation"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PASSTHROUGHOPERATIONTYPE_TYPE = build_PASSTHROUGHOPERATIONTYPE_TYPE();
    
    private static ComplexType build_PASSTHROUGHOPERATIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","modifiedCoordinate"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATEOPERATIONPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordOperation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PassThroughOperationType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATEOPERATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PassThroughOperationPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:PassThroughOperationPropertyType is a property type for association roles to a pass through operation, either referencing or containing the definition of that pass through operation.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:PassThroughOperation"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PASSTHROUGHOPERATIONPROPERTYTYPE_TYPE = build_PASSTHROUGHOPERATIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_PASSTHROUGHOPERATIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                PASSTHROUGHOPERATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","PassThroughOperation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PassThroughOperationPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractRingType"&gt;
     *      &lt;sequence/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTRINGTYPE_TYPE = build_ABSTRACTRINGTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTRINGTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractRingType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGeometricPrimitiveType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:AbstractGeometricPrimitiveType is the abstract root type of the geometric primitives. A geometric primitive is a geometric object that is not decomposed further into other primitives in the system. All primitives are oriented in the direction implied by the sequence of their coordinate tuples.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometryType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGEOMETRICPRIMITIVETYPE_TYPE = build_ABSTRACTGEOMETRICPRIMITIVETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGEOMETRICPRIMITIVETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometricPrimitiveType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTGEOMETRYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PointType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometricPrimitiveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:pos"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType POINTTYPE_TYPE = build_POINTTYPE_TYPE();
     
    private static AttributeType build_POINTTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PointType"), com.vividsolutions.jts.geom.Point.class, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGEOMETRICPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PointPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a point as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Point"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType POINTPROPERTYTYPE_TYPE = build_POINTPROPERTYTYPE_TYPE();
     
    private static AttributeType build_POINTPROPERTYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PointPropertyType"), com.vividsolutions.jts.geom.Point.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectPositionListType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;posList instances (and other instances with the content model specified by DirectPositionListType) hold the coordinates for a sequence of direct positions within the same coordinate reference system (CRS).
     *  if no srsName attribute is given, the CRS shall be specified as part of the larger context this geometry element is part of, typically a geometric object like a point, curve, etc. 
     *  The optional attribute count specifies the number of direct positions in the list. If the attribute count is present then the attribute srsDimension shall be present, too.
     *  The number of entries in the list is equal to the product of the dimensionality of the coordinate reference system (i.e. it is a derived value of the coordinate reference system definition) and the number of direct positions.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:doubleList"&gt;
     *              &lt;attributeGroup ref="gml:SRSReferenceGroup"/&gt;
     *              &lt;attribute name="count" type="positiveInteger"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTPOSITIONLISTTYPE_TYPE = build_DIRECTPOSITIONLISTTYPE_TYPE();
    
    private static ComplexType build_DIRECTPOSITIONLISTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                NCNAMELIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","axisLabels"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","srsDimension"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","srsName"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NCNAMELIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","uomLabels"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","count"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectPositionListType"), schema, false,
            false, Collections.<Filter>emptyList(), DOUBLELIST_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LinearRingType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractRingType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice maxOccurs="unbounded" minOccurs="4"&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType LINEARRINGTYPE_TYPE = build_LINEARRINGTYPE_TYPE();
     
    private static AttributeType build_LINEARRINGTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","LinearRingType"), com.vividsolutions.jts.geom.LinearRing.class, false,
            false, Collections.<Filter>emptyList(), ABSTRACTRINGTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LinearRingPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property with the content model of gml:LinearRingPropertyType encapsulates a linear ring to represent a component of a surface boundary.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:LinearRing"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType LINEARRINGPROPERTYTYPE_TYPE = build_LINEARRINGPROPERTYTYPE_TYPE();
     
    private static AttributeType build_LINEARRINGPROPERTYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","LinearRingPropertyType"), com.vividsolutions.jts.geom.LinearRing.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractTopologyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;This abstract type supplies the root or base type for all topological elements including primitives and complexes. It inherits AbstractGMLType and hence can be identified using the gml:id attribute.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGMLType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTTOPOLOGYTYPE_TYPE = build_ABSTRACTTOPOLOGYTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTTOPOLOGYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractTopologyType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTGMLTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractTopoPrimitiveType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTopologyType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTTOPOPRIMITIVETYPE_TYPE = build_ABSTRACTTOPOPRIMITIVETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTTOPOPRIMITIVETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractTopoPrimitiveType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTTOPOLOGYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractSurfaceType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:AbstractSurfaceType is an abstraction of a surface to support the different levels of complexity. A surface is always a continuous region of a plane.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometricPrimitiveType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTSURFACETYPE_TYPE = build_ABSTRACTSURFACETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTSURFACETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractSurfaceType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTGEOMETRICPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SurfacePropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a surface as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractSurface"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType SURFACEPROPERTYTYPE_TYPE = build_SURFACEPROPERTYTYPE_TYPE();
     
    private static AttributeType build_SURFACEPROPERTYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SurfacePropertyType"), com.vividsolutions.jts.geom.Polygon.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="FaceType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTopoPrimitiveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;!--element name="isolated" type="gml:NodePropertyType" minOccurs="0" maxOccurs="unbounded"/&gt;
     *            &lt;element ref="gml:directedEdge" maxOccurs="unbounded"/&gt;
     *            &lt;element ref="gml:directedTopoSolid" minOccurs="0" maxOccurs="2"/--&gt;
     *                  &lt;element minOccurs="0" ref="gml:surfaceProperty"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *              &lt;attribute default="false" name="universal" type="boolean" use="optional"&gt;
     *                  &lt;annotation&gt;
     *                      &lt;documentation&gt;If the topological representation exists an unbounded manifold (e.g. Euclidean plane), a gml:Face must indicate whether it is a universal face or not, to ensure a lossless topology representation as defined by Kuijpers, et. al. (see OGC 05-102 Topology IPR). The optional universal attribute of type boolean is used to indicate this. NOTE The universal face is normally not part of any feature, and is used to represent the unbounded portion of the data set. Its interior boundary (it has no exterior boundary) would normally be considered the exterior boundary of the map represented by the data set. &lt;/documentation&gt;
     *                  &lt;/annotation&gt;
     *              &lt;/attribute&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType FACETYPE_TYPE = build_FACETYPE_TYPE();
    
    private static ComplexType build_FACETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SURFACEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","surfaceProperty"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","universal"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","FaceType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTOPOPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="SignType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:SignType is a convenience type with values "+" (plus) and "-" (minus).&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="-"/&gt;
     *          &lt;enumeration value="+"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType SIGNTYPE_TYPE = build_SIGNTYPE_TYPE();
     
    private static AttributeType build_SIGNTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SignType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectedFacePropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Face"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attribute default="+" name="orientation" type="gml:SignType"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTEDFACEPROPERTYTYPE_TYPE = build_DIRECTEDFACEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_DIRECTEDFACEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                FACETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Face"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SIGNTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","orientation"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectedFacePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AbstractSolidType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:AbstractSolidType is an abstraction of a solid to support the different levels of complexity. The solid may always be viewed as a geometric primitive, i.e. is contiguous.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometricPrimitiveType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTSOLIDTYPE_TYPE = build_ABSTRACTSOLIDTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTSOLIDTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractSolidType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTGEOMETRICPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SolidPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a solid as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractSolid"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SOLIDPROPERTYTYPE_TYPE = build_SOLIDPROPERTYTYPE_TYPE();
    
    private static ComplexType build_SOLIDPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTSOLIDTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractSolid"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SolidPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoSolidType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTopoPrimitiveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;!--element name="isolated" type="gml:NodeOrEdgePropertyType" minOccurs="0" maxOccurs="unbounded"/--&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:directedFace"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:solidProperty"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *              &lt;attribute default="false" name="universal" type="boolean" use="optional"&gt;
     *                  &lt;annotation&gt;
     *                      &lt;documentation&gt;A gml:TopoSolid must indicate whether it is a universal topo-solid or not, to ensure a lossless topology representation as defined by Kuijpers, et. al. (see OGC 05-102 Topology IPR). The optional universal attribute of type boolean is used to indicate this and the default is fault. NOTE The universal topo-solid is normally not part of any feature, and is used to represent the unbounded portion of the data set. Its interior boundary (it has no exterior boundary) would normally be considered the exterior boundary of the data set.&lt;/documentation&gt;
     *                  &lt;/annotation&gt;
     *              &lt;/attribute&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOSOLIDTYPE_TYPE = build_TOPOSOLIDTYPE_TYPE();
    
    private static ComplexType build_TOPOSOLIDTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTEDFACEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","directedFace"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SOLIDPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","solidProperty"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","universal"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoSolidType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTOPOPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectedTopoSolidPropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TopoSolid"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attribute default="+" name="orientation" type="gml:SignType"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTEDTOPOSOLIDPROPERTYTYPE_TYPE = build_DIRECTEDTOPOSOLIDPROPERTYTYPE_TYPE();
    
    private static ComplexType build_DIRECTEDTOPOSOLIDPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TOPOSOLIDTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TopoSolid"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SIGNTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","orientation"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectedTopoSolidPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoVolumeType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTopologyType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:directedTopoSolid"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOVOLUMETYPE_TYPE = build_TOPOVOLUMETYPE_TYPE();
    
    private static ComplexType build_TOPOVOLUMETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTEDTOPOSOLIDPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","directedTopoSolid"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoVolumeType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTOPOLOGYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoVolumePropertyType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:TopoVolume"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOVOLUMEPROPERTYTYPE_TYPE = build_TOPOVOLUMEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TOPOVOLUMEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TOPOVOLUMETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TopoVolume"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoVolumePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGeometricAggregateType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometryType"&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGEOMETRICAGGREGATETYPE_TYPE = build_ABSTRACTGEOMETRICAGGREGATETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGEOMETRICAGGREGATETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometricAggregateType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTGEOMETRYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SurfaceArrayPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:SurfaceArrayPropertyType is a container for an array of surfaces. The elements are always contained in the array property, referencing geometry elements or arrays of geometry elements via XLinks is not supported.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractSurface"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SURFACEARRAYPROPERTYTYPE_TYPE = build_SURFACEARRAYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_SURFACEARRAYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTSURFACETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractSurface"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SurfaceArrayPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiSurfaceType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometricAggregateType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:surfaceMember"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:surfaceMembers"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTISURFACETYPE_TYPE = build_MULTISURFACETYPE_TYPE();
     
    private static AttributeType build_MULTISURFACETYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MultiSurfaceType"), com.vividsolutions.jts.geom.MultiPolygon.class, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGEOMETRICAGGREGATETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiSurfacePropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a collection of surfaces as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:MultiSurface"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTISURFACEPROPERTYTYPE_TYPE = build_MULTISURFACEPROPERTYTYPE_TYPE();
     
    private static AttributeType build_MULTISURFACEPROPERTYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MultiSurfacePropertyType"), com.vividsolutions.jts.geom.MultiPolygon.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGeneralConversionType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:AbstractCoordinateOperationType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:metaDataProperty"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:description"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:descriptionReference"/&gt;
     *                  &lt;element ref="gml:identifier"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:name"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:remarks"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:domainOfValidity"/&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:scope"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:coordinateOperationAccuracy"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute ref="gml:id" use="required"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGENERALCONVERSIONTYPE_TYPE = build_ABSTRACTGENERALCONVERSIONTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGENERALCONVERSIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                METADATAPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","metaDataProperty"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","description"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","descriptionReference"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODEWITHAUTHORITYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","identifier"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","name"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remarks"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                _DOMAINOFVALIDITY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","domainOfValidity"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","scope"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                _COORDINATEOPERATIONACCURACY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinateOperationAccuracy"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ID_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","id"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralConversionType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTCOORDINATEOPERATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="_formulaCitation"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gmd:CI_Citation"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType _FORMULACITATION_TYPE = build__FORMULACITATION_TYPE();
    
    private static ComplexType build__FORMULACITATION_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GMDSchema.CI_CITATION_TYPE_TYPE, new NameImpl("http://www.isotc211.org/2005/gmd","CI_Citation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","_formulaCitation"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGeneralOperationParameterType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:minimumOccurs"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGENERALOPERATIONPARAMETERTYPE_TYPE = build_ABSTRACTGENERALOPERATIONPARAMETERTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGENERALOPERATIONPARAMETERTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.NONNEGATIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","minimumOccurs"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralOperationParameterType"), schema, false,
            true, Collections.<Filter>emptyList(), IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AbstractGeneralOperationParameterPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:AbstractGeneralOperationParameterPropertyType is a property type for association roles to an operation parameter or group, either referencing or containing the definition of that parameter or group.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractGeneralOperationParameter"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGENERALOPERATIONPARAMETERPROPERTYTYPE_TYPE = build_ABSTRACTGENERALOPERATIONPARAMETERPROPERTYTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGENERALOPERATIONPARAMETERPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGENERALOPERATIONPARAMETERTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralOperationParameter"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralOperationParameterPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="OperationMethodType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:formulaCitation"/&gt;
     *                      &lt;element ref="gml:formula"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element minOccurs="0" ref="gml:sourceDimensions"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:targetDimensions"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:parameter"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONMETHODTYPE_TYPE = build_OPERATIONMETHODTYPE_TYPE();
    
    private static ComplexType build_OPERATIONMETHODTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                _FORMULACITATION_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","formulaCitation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","formula"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","sourceDimensions"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","targetDimensions"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGENERALOPERATIONPARAMETERPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","parameter"), 0, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","OperationMethodType"), schema, false,
            false, Collections.<Filter>emptyList(), IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="OperationMethodPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:OperationMethodPropertyType is a property type for association roles to a concrete general-purpose operation method, either referencing or containing the definition of that method.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:OperationMethod"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONMETHODPROPERTYTYPE_TYPE = build_OPERATIONMETHODPROPERTYTYPE_TYPE();
    
    private static ComplexType build_OPERATIONMETHODPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                OPERATIONMETHODTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","OperationMethod"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","OperationMethodPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGeneralParameterValueType"&gt;
     *      &lt;sequence/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGENERALPARAMETERVALUETYPE_TYPE = build_ABSTRACTGENERALPARAMETERVALUETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGENERALPARAMETERVALUETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralParameterValueType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AbstractGeneralParameterValuePropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:AbstractGeneralParameterValuePropertyType is a  property type for inline association roles to a parameter value or group of parameter values, always containing the values.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:AbstractGeneralParameterValue"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGENERALPARAMETERVALUEPROPERTYTYPE_TYPE = build_ABSTRACTGENERALPARAMETERVALUEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGENERALPARAMETERVALUEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGENERALPARAMETERVALUETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralParameterValue"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralParameterValuePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ConversionType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeneralConversionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:method"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:parameterValue"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CONVERSIONTYPE_TYPE = build_CONVERSIONTYPE_TYPE();
    
    private static ComplexType build_CONVERSIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                OPERATIONMETHODPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","method"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGENERALPARAMETERVALUEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","parameterValue"), 0, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ConversionType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGENERALCONVERSIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ConversionPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:ConversionPropertyType is a property type for association roles to a concrete general-purpose conversion, either referencing or containing the definition of that conversion.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Conversion"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CONVERSIONPROPERTYTYPE_TYPE = build_CONVERSIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_CONVERSIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CONVERSIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Conversion"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ConversionPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="integerOrNilReasonList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A type for a list of values of the respective simple type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="gml:integerOrNilReason"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType INTEGERORNILREASONLIST_TYPE = build_INTEGERORNILREASONLIST_TYPE();
     
    private static AttributeType build_INTEGERORNILREASONLIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","integerOrNilReasonList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="CountExtentType"&gt;
     *      &lt;restriction base="gml:integerOrNilReasonList"&gt;
     *          &lt;length value="2"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType COUNTEXTENTTYPE_TYPE = build_COUNTEXTENTTYPE_TYPE();
     
    private static AttributeType build_COUNTEXTENTTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CountExtentType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), INTEGERORNILREASONLIST_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="FaceOrTopoSolidPropertyType"&gt;
     *      &lt;choice minOccurs="0"&gt;
     *          &lt;element ref="gml:Face"/&gt;
     *          &lt;element ref="gml:TopoSolid"/&gt;
     *      &lt;/choice&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType FACEORTOPOSOLIDPROPERTYTYPE_TYPE = build_FACEORTOPOSOLIDPROPERTYTYPE_TYPE();
    
    private static ComplexType build_FACEORTOPOSOLIDPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                FACETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Face"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TOPOSOLIDTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TopoSolid"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","FaceOrTopoSolidPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoSolidPropertyType"&gt;
     *      &lt;choice minOccurs="0"&gt;
     *          &lt;element ref="gml:TopoSolid"/&gt;
     *      &lt;/choice&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOSOLIDPROPERTYTYPE_TYPE = build_TOPOSOLIDPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TOPOSOLIDPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TOPOSOLIDTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TopoSolid"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoSolidPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractCurveType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:AbstractCurveType is an abstraction of a curve to support the different levels of complexity. The curve may always be viewed as a geometric primitive, i.e. is continuous.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometricPrimitiveType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTCURVETYPE_TYPE = build_ABSTRACTCURVETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTCURVETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractCurveType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTGEOMETRICPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CurvePropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a curve as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractCurve"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType CURVEPROPERTYTYPE_TYPE = build_CURVEPROPERTYTYPE_TYPE();
     
    private static AttributeType build_CURVEPROPERTYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CurvePropertyType"), com.vividsolutions.jts.geom.MultiLineString.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EdgeType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTopoPrimitiveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" name="container" type="gml:TopoSolidPropertyType"/&gt;
     *                  &lt;!--element ref="gml:directedNode" minOccurs="2" maxOccurs="2"/--&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:directedFace"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:curveProperty"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType EDGETYPE_TYPE = build_EDGETYPE_TYPE();
    
    private static ComplexType build_EDGETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TOPOSOLIDPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","container"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTEDFACEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","directedFace"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","curveProperty"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EdgeType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTOPOPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectedEdgePropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Edge"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attribute default="+" name="orientation" type="gml:SignType"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTEDEDGEPROPERTYTYPE_TYPE = build_DIRECTEDEDGEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_DIRECTEDEDGEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                EDGETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Edge"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SIGNTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","orientation"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectedEdgePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="NodeType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTopoPrimitiveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" name="container" type="gml:FaceOrTopoSolidPropertyType"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:directedEdge"&gt;
     *                      &lt;annotation&gt;
     *                          &lt;documentation&gt;In the case of planar topology, a gml:Node must have a clockwise sequence of gml:directedEdge properties, to ensure a lossless topology representation as defined by Kuijpers, et. al. (see OGC 05-102 Topology IPR).&lt;/documentation&gt;
     *                      &lt;/annotation&gt;
     *                  &lt;/element&gt;
     *                  &lt;element minOccurs="0" ref="gml:pointProperty"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType NODETYPE_TYPE = build_NODETYPE_TYPE();
    
    private static ComplexType build_NODETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                FACEORTOPOSOLIDPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","container"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTEDEDGEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","directedEdge"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","NodeType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTOPOPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="NodePropertyType"&gt;
     *      &lt;choice minOccurs="0"&gt;
     *          &lt;element ref="gml:Node"/&gt;
     *      &lt;/choice&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType NODEPROPERTYTYPE_TYPE = build_NODEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_NODEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                NODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Node"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","NodePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="UomIdentifier"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;The simple type gml:UomIdentifer defines the syntax and value space of the unit of measure identifier.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;union memberTypes="gml:UomSymbol gml:UomURI"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType UOMIDENTIFIER_TYPE = build_UOMIDENTIFIER_TYPE();
     
    private static AttributeType build_UOMIDENTIFIER_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","UomIdentifier"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CoordinateSystemAxisType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:axisAbbrev"/&gt;
     *                  &lt;element ref="gml:axisDirection"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:minimumValue"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:maximumValue"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:rangeMeaning"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute name="uom" type="gml:UomIdentifier" use="required"&gt;
     *                  &lt;annotation&gt;
     *                      &lt;documentation&gt;The uom attribute provides an identifier of the unit of measure used for this coordinate system axis. The value of this coordinate in a coordinate tuple shall be recorded using this unit of measure, whenever those coordinates use a coordinate reference system that uses a coordinate system that uses this axis.&lt;/documentation&gt;
     *                  &lt;/annotation&gt;
     *              &lt;/attribute&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COORDINATESYSTEMAXISTYPE_TYPE = build_COORDINATESYSTEMAXISTYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMAXISTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","axisAbbrev"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODEWITHAUTHORITYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","axisDirection"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","minimumValue"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","maximumValue"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODEWITHAUTHORITYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","rangeMeaning"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                UOMIDENTIFIER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","uom"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CoordinateSystemAxisType"), schema, false,
            false, Collections.<Filter>emptyList(), IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CoordinateSystemAxisPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CoordinateSystemAxisPropertyType is a property type for association roles to a coordinate system axis, either referencing or containing the definition of that axis.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:CoordinateSystemAxis"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COORDINATESYSTEMAXISPROPERTYTYPE_TYPE = build_COORDINATESYSTEMAXISPROPERTYTYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMAXISPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESYSTEMAXISTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","CoordinateSystemAxis"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CoordinateSystemAxisPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractCoordinateSystemType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:axis"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTCOORDINATESYSTEMTYPE_TYPE = build_ABSTRACTCOORDINATESYSTEMTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTCOORDINATESYSTEMTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESYSTEMAXISPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","axis"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractCoordinateSystemType"), schema, false,
            true, Collections.<Filter>emptyList(), IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AffineCSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType AFFINECSTYPE_TYPE = build_AFFINECSTYPE_TYPE();
    
    private static ComplexType build_AFFINECSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AffineCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AffineCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:AffineCSPropertyType is a property type for association roles to an affine coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AffineCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType AFFINECSPROPERTYTYPE_TYPE = build_AFFINECSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_AFFINECSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                AFFINECSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AffineCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AffineCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CartesianCSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CARTESIANCSTYPE_TYPE = build_CARTESIANCSTYPE_TYPE();
    
    private static ComplexType build_CARTESIANCSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CartesianCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CartesianCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CartesianCSPropertyType is a property type for association roles to a Cartesian coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:CartesianCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CARTESIANCSPROPERTYTYPE_TYPE = build_CARTESIANCSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_CARTESIANCSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CARTESIANCSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","CartesianCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CartesianCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CylindricalCSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CYLINDRICALCSTYPE_TYPE = build_CYLINDRICALCSTYPE_TYPE();
    
    private static ComplexType build_CYLINDRICALCSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CylindricalCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CylindricalCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CylindricalCSPropertyType is a property type for association roles to a cylindrical coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:CylindricalCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CYLINDRICALCSPROPERTYTYPE_TYPE = build_CYLINDRICALCSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_CYLINDRICALCSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CYLINDRICALCSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","CylindricalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CylindricalCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LinearCSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType LINEARCSTYPE_TYPE = build_LINEARCSTYPE_TYPE();
    
    private static ComplexType build_LINEARCSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","LinearCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LinearCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:LinearCSPropertyType is a property type for association roles to a linear coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:LinearCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType LINEARCSPROPERTYTYPE_TYPE = build_LINEARCSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_LINEARCSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                LINEARCSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","LinearCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","LinearCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PolarCSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType POLARCSTYPE_TYPE = build_POLARCSTYPE_TYPE();
    
    private static ComplexType build_POLARCSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PolarCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PolarCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:PolarCSPropertyType is a property type for association roles to a polar coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:PolarCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType POLARCSPROPERTYTYPE_TYPE = build_POLARCSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_POLARCSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                POLARCSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","PolarCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PolarCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SphericalCSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SPHERICALCSTYPE_TYPE = build_SPHERICALCSTYPE_TYPE();
    
    private static ComplexType build_SPHERICALCSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SphericalCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SphericalCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:SphericalCSPropertyType is property type for association roles to a spherical coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:SphericalCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SPHERICALCSPROPERTYTYPE_TYPE = build_SPHERICALCSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_SPHERICALCSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SPHERICALCSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","SphericalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SphericalCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="UserDefinedCSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType USERDEFINEDCSTYPE_TYPE = build_USERDEFINEDCSTYPE_TYPE();
    
    private static ComplexType build_USERDEFINEDCSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","UserDefinedCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="UserDefinedCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:UserDefinedCSPropertyType is a property type for association roles to a user-defined coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:UserDefinedCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType USERDEFINEDCSPROPERTYTYPE_TYPE = build_USERDEFINEDCSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_USERDEFINEDCSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                USERDEFINEDCSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","UserDefinedCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","UserDefinedCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CoordinateSystemPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CoordinateSystemPropertyType is a property type for association roles to a coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractCoordinateSystem"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COORDINATESYSTEMPROPERTYTYPE_TYPE = build_COORDINATESYSTEMPROPERTYTYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTCOORDINATESYSTEMTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractCoordinateSystem"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CoordinateSystemPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractDatumType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:domainOfValidity"/&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:scope"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:anchorDefinition"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:realizationEpoch"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTDATUMTYPE_TYPE = build_ABSTRACTDATUMTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTDATUMTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                _DOMAINOFVALIDITY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","domainOfValidity"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","scope"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","anchorDefinition"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DATE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","realizationEpoch"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractDatumType"), schema, false,
            true, Collections.<Filter>emptyList(), IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EngineeringDatumType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractDatumType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ENGINEERINGDATUMTYPE_TYPE = build_ENGINEERINGDATUMTYPE_TYPE();
    
    private static ComplexType build_ENGINEERINGDATUMTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EngineeringDatumType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EngineeringDatumPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:EngineeringDatumPropertyType is a property type for association roles to an engineering datum, either referencing or containing the definition of that datum.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:EngineeringDatum"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ENGINEERINGDATUMPROPERTYTYPE_TYPE = build_ENGINEERINGDATUMPROPERTYTYPE_TYPE();
    
    private static ComplexType build_ENGINEERINGDATUMPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ENGINEERINGDATUMTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","EngineeringDatum"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EngineeringDatumPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EngineeringCRSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:affineCS"/&gt;
     *                      &lt;element ref="gml:cartesianCS"/&gt;
     *                      &lt;element ref="gml:cylindricalCS"/&gt;
     *                      &lt;element ref="gml:linearCS"/&gt;
     *                      &lt;element ref="gml:polarCS"/&gt;
     *                      &lt;element ref="gml:sphericalCS"/&gt;
     *                      &lt;element ref="gml:userDefinedCS"/&gt;
     *                      &lt;element ref="gml:coordinateSystem"&gt;
     *                          &lt;annotation&gt;
     *                              &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *                          &lt;/annotation&gt;
     *                      &lt;/element&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element ref="gml:engineeringDatum"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ENGINEERINGCRSTYPE_TYPE = build_ENGINEERINGCRSTYPE_TYPE();
    
    private static ComplexType build_ENGINEERINGCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                AFFINECSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","affineCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CARTESIANCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","cartesianCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CYLINDRICALCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","cylindricalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                LINEARCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","linearCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POLARCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","polarCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SPHERICALCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","sphericalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                USERDEFINEDCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","userDefinedCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESYSTEMPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinateSystem"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ENGINEERINGDATUMPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","engineeringDatum"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EngineeringCRSType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EngineeringCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:EngineeringCRSPropertyType is a property type for association roles to an engineering coordinate reference system, either referencing or containing the definition of that reference system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:EngineeringCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ENGINEERINGCRSPROPERTYTYPE_TYPE = build_ENGINEERINGCRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_ENGINEERINGCRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ENGINEERINGCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","EngineeringCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EngineeringCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MeasureType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:MeasureType supports recording an amount encoded as a value of XML Schema double, together with a units of measure indicated by an attribute uom, short for "units Of measure". The value of the uom attribute identifies a reference system for the amount, usually a ratio or interval scale.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="double"&gt;
     *              &lt;attribute name="uom" type="gml:UomIdentifier" use="required"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MEASURETYPE_TYPE = build_MEASURETYPE_TYPE();
    
    private static ComplexType build_MEASURETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                UOMIDENTIFIER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","uom"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MeasureType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.DOUBLE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GridLengthType"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:MeasureType"/&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GRIDLENGTHTYPE_TYPE = build_GRIDLENGTHTYPE_TYPE();
    
    private static ComplexType build_GRIDLENGTHTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GridLengthType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="doubleOrNilReason"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Extension to the respective XML Schema built-in simple type to allow a choice of either a value of the built-in simple type or a reason for a nil value.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;union memberTypes="gml:NilReasonEnumeration double anyURI"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DOUBLEORNILREASON_TYPE = build_DOUBLEORNILREASON_TYPE();
     
    private static AttributeType build_DOUBLEORNILREASON_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","doubleOrNilReason"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="VerticalCSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VERTICALCSTYPE_TYPE = build_VERTICALCSTYPE_TYPE();
    
    private static ComplexType build_VERTICALCSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","VerticalCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="VerticalCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:VerticalCSPropertyType is a property type for association roles to a vertical coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:VerticalCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VERTICALCSPROPERTYTYPE_TYPE = build_VERTICALCSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_VERTICALCSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                VERTICALCSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","VerticalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","VerticalCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="VerticalDatumType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractDatumType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VERTICALDATUMTYPE_TYPE = build_VERTICALDATUMTYPE_TYPE();
    
    private static ComplexType build_VERTICALDATUMTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","VerticalDatumType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="VerticalDatumPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:VerticalDatumPropertyType is property type for association roles to a vertical datum, either referencing or containing the definition of that datum.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:VerticalDatum"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VERTICALDATUMPROPERTYTYPE_TYPE = build_VERTICALDATUMPROPERTYTYPE_TYPE();
    
    private static ComplexType build_VERTICALDATUMPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                VERTICALDATUMTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","VerticalDatum"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","VerticalDatumPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="VerticalCRSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:verticalCS"/&gt;
     *                  &lt;element ref="gml:verticalDatum"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VERTICALCRSTYPE_TYPE = build_VERTICALCRSTYPE_TYPE();
    
    private static ComplexType build_VERTICALCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                VERTICALCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","verticalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                VERTICALDATUMPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","verticalDatum"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","VerticalCRSType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="VerticalCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:VerticalCRSPropertyType is a property type for association roles to a vertical coordinate reference system, either referencing or containing the definition of that reference system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:VerticalCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VERTICALCRSPROPERTYTYPE_TYPE = build_VERTICALCRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_VERTICALCRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                VERTICALCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","VerticalCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","VerticalCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="_Category"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:CodeType"&gt;
     *              &lt;attribute name="nilReason" type="gml:NilReasonType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType _CATEGORY_TYPE = build__CATEGORY_TYPE();
    
    private static ComplexType build__CATEGORY_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","_Category"), schema, false,
            false, Collections.<Filter>emptyList(), CODETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CategoryPropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Category"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CATEGORYPROPERTYTYPE_TYPE = build_CATEGORYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_CATEGORYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                _CATEGORY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Category"), 1, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CategoryPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DatumPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:DatumPropertyType is a property type for association roles to a datum, either referencing or containing the definition of that datum.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractDatum"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DATUMPROPERTYTYPE_TYPE = build_DATUMPROPERTYTYPE_TYPE();
    
    private static ComplexType build_DATUMPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTDATUMTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractDatum"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DatumPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeReferenceSystemType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:DefinitionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element name="domainOfValidity" type="string"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEREFERENCESYSTEMTYPE_TYPE = build_TIMEREFERENCESYSTEMTYPE_TYPE();
    
    private static ComplexType build_TIMEREFERENCESYSTEMTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","domainOfValidity"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeReferenceSystemType"), schema, false,
            false, Collections.<Filter>emptyList(), DEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractTimeObjectType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGMLType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTTIMEOBJECTTYPE_TYPE = build_ABSTRACTTIMEOBJECTTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTTIMEOBJECTTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeObjectType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTGMLTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractTimePrimitiveType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTimeObjectType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;!--element name="relatedTime" type="gml:RelatedTimeType" minOccurs="0" maxOccurs="unbounded"/--&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTTIMEPRIMITIVETYPE_TYPE = build_ABSTRACTTIMEPRIMITIVETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTTIMEPRIMITIVETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimePrimitiveType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTTIMEOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimePrimitivePropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimePrimitivePropertyType provides a standard content model for associations between an arbitrary member of the substitution group whose head is gml:AbstractTimePrimitive and another object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractTimePrimitive"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEPRIMITIVEPROPERTYTYPE_TYPE = build_TIMEPRIMITIVEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMEPRIMITIVEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTTIMEPRIMITIVETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimePrimitive"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimePrimitivePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="RelatedTimeType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:RelatedTimeType provides a content model for indicating the relative position of an arbitrary member of the substitution group whose head is gml:AbstractTimePrimitive. It extends the generic gml:TimePrimitivePropertyType with an XML attribute relativePosition, whose value is selected from the set of 13 temporal relationships identified by Allen (1983)&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:TimePrimitivePropertyType"&gt;
     *              &lt;attribute name="relativePosition"&gt;
     *                  &lt;simpleType&gt;
     *                      &lt;restriction base="string"&gt;
     *                          &lt;enumeration value="Before"/&gt;
     *                          &lt;enumeration value="After"/&gt;
     *                          &lt;enumeration value="Begins"/&gt;
     *                          &lt;enumeration value="Ends"/&gt;
     *                          &lt;enumeration value="During"/&gt;
     *                          &lt;enumeration value="Equals"/&gt;
     *                          &lt;enumeration value="Contains"/&gt;
     *                          &lt;enumeration value="Overlaps"/&gt;
     *                          &lt;enumeration value="Meets"/&gt;
     *                          &lt;enumeration value="OverlappedBy"/&gt;
     *                          &lt;enumeration value="MetBy"/&gt;
     *                          &lt;enumeration value="BegunBy"/&gt;
     *                          &lt;enumeration value="EndedBy"/&gt;
     *                      &lt;/restriction&gt;
     *                  &lt;/simpleType&gt;
     *              &lt;/attribute&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType RELATEDTIMETYPE_TYPE = build_RELATEDTIMETYPE_TYPE();
    
    private static ComplexType build_RELATEDTIMETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","RelatedTimeType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), TIMEPRIMITIVEPROPERTYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractTimeTopologyPrimitiveType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTimePrimitiveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" name="complex" type="gml:ReferenceType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTTIMETOPOLOGYPRIMITIVETYPE_TYPE = build_ABSTRACTTIMETOPOLOGYPRIMITIVETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTTIMETOPOLOGYPRIMITIVETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","complex"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeTopologyPrimitiveType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTTIMEPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractTimeGeometricPrimitiveType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTimePrimitiveType"&gt;
     *              &lt;attribute default="#ISO-8601" name="frame" type="anyURI"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTTIMEGEOMETRICPRIMITIVETYPE_TYPE = build_ABSTRACTTIMEGEOMETRICPRIMITIVETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTTIMEGEOMETRICPRIMITIVETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","frame"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeGeometricPrimitiveType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTTIMEPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="TimePositionUnion"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;The simple type gml:TimePositionUnion is a union of XML Schema simple types which instantiate the subtypes for temporal position described in ISO 19108.
     *   An ordinal era may be referenced via URI.  A decimal value may be used to indicate the distance from the scale origin .  time is used for a position that recurs daily (see ISO 19108:2002 5.4.4.2).
     *   Finally, calendar and clock forms that support the representation of time in systems based on years, months, days, hours, minutes and seconds, in a notation following ISO 8601, are assembled by gml:CalDate&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;union memberTypes="gml:CalDate time dateTime anyURI decimal"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType TIMEPOSITIONUNION_TYPE = build_TIMEPOSITIONUNION_TYPE();
     
    private static AttributeType build_TIMEPOSITIONUNION_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimePositionUnion"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="TimeIndeterminateValueType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;These values are interpreted as follows: 
     *  -	"unknown" indicates that no specific value for temporal position is provided.
     *  -	"now" indicates that the specified value shall be replaced with the current temporal position whenever the value is accessed.
     *  -	"before" indicates that the actual temporal position is unknown, but it is known to be before the specified value.
     *  -	"after" indicates that the actual temporal position is unknown, but it is known to be after the specified value.
     *  A value for indeterminatePosition may 
     *  -	be used either alone, or 
     *  -	qualify a specific value for temporal position.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="after"/&gt;
     *          &lt;enumeration value="before"/&gt;
     *          &lt;enumeration value="now"/&gt;
     *          &lt;enumeration value="unknown"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType TIMEINDETERMINATEVALUETYPE_TYPE = build_TIMEINDETERMINATEVALUETYPE_TYPE();
     
    private static AttributeType build_TIMEINDETERMINATEVALUETYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeIndeterminateValueType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType final="#all" name="TimePositionType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;The method for identifying a temporal position is specific to each temporal reference system.  gml:TimePositionType supports the description of temporal position according to the subtypes described in ISO 19108.
     *  Values based on calendars and clocks use lexical formats that are based on ISO 8601, as described in XML Schema Part 2:2001. A decimal value may be used with coordinate systems such as GPS time or UNIX time. A URI may be used to provide a reference to some era in an ordinal reference system . 
     *  In common with many of the components modelled as data types in the ISO 19100 series of International Standards, the corresponding GML component has simple content. However, the content model gml:TimePositionType is defined in several steps.
     *  Three XML attributes appear on gml:TimePositionType:
     *  A time value shall be associated with a temporal reference system through the frame attribute that provides a URI reference that identifies a description of the reference system. Following ISO 19108, the Gregorian calendar with UTC is the default reference system, but others may also be used. Components for describing temporal reference systems are described in 14.4, but it is not required that the reference system be described in this, as the reference may refer to anything that may be indentified with a URI.  
     *  For time values using a calendar containing more than one era, the (optional) calendarEraName attribute provides the name of the calendar era.  
     *  Inexact temporal positions may be expressed using the optional indeterminatePosition attribute.  This takes a value from an enumeration.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:TimePositionUnion"&gt;
     *              &lt;attribute default="#ISO-8601" name="frame" type="anyURI"/&gt;
     *              &lt;attribute name="calendarEraName" type="string"/&gt;
     *              &lt;attribute name="indeterminatePosition" type="gml:TimeIndeterminateValueType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEPOSITIONTYPE_TYPE = build_TIMEPOSITIONTYPE_TYPE();
    
    private static ComplexType build_TIMEPOSITIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","frame"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","calendarEraName"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEINDETERMINATEVALUETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","indeterminatePosition"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimePositionType"), schema, false,
            false, Collections.<Filter>emptyList(), TIMEPOSITIONUNION_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType final="#all" name="TimeInstantType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTimeGeometricPrimitiveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:timePosition"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEINSTANTTYPE_TYPE = build_TIMEINSTANTTYPE_TYPE();
    
    private static ComplexType build_TIMEINSTANTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","timePosition"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeInstantType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTIMEGEOMETRICPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeInstantPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimeInstantPropertyType provides for associating a gml:TimeInstant with an object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TimeInstant"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEINSTANTPROPERTYTYPE_TYPE = build_TIMEINSTANTPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMEINSTANTPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEINSTANTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TimeInstant"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeInstantPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="TimeUnitType"&gt;
     *      &lt;union&gt;
     *          &lt;simpleType&gt;
     *              &lt;restriction base="string"&gt;
     *                  &lt;enumeration value="year"/&gt;
     *                  &lt;enumeration value="month"/&gt;
     *                  &lt;enumeration value="day"/&gt;
     *                  &lt;enumeration value="hour"/&gt;
     *                  &lt;enumeration value="minute"/&gt;
     *                  &lt;enumeration value="second"/&gt;
     *              &lt;/restriction&gt;
     *          &lt;/simpleType&gt;
     *          &lt;simpleType&gt;
     *              &lt;restriction base="string"&gt;
     *                  &lt;pattern value="other:\w{2,}"/&gt;
     *              &lt;/restriction&gt;
     *          &lt;/simpleType&gt;
     *      &lt;/union&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType TIMEUNITTYPE_TYPE = build_TIMEUNITTYPE_TYPE();
     
    private static AttributeType build_TIMEUNITTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeUnitType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType final="#all" name="TimeIntervalLengthType"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="decimal"&gt;
     *              &lt;attribute name="unit" type="gml:TimeUnitType" use="required"/&gt;
     *              &lt;attribute name="radix" type="positiveInteger"/&gt;
     *              &lt;attribute name="factor" type="integer"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEINTERVALLENGTHTYPE_TYPE = build_TIMEINTERVALLENGTHTYPE_TYPE();
    
    private static ComplexType build_TIMEINTERVALLENGTHTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEUNITTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","unit"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","radix"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","factor"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeIntervalLengthType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.DECIMAL_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimePeriodType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTimeGeometricPrimitiveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element name="beginPosition" type="gml:TimePositionType"/&gt;
     *                      &lt;element name="begin" type="gml:TimeInstantPropertyType"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;choice&gt;
     *                      &lt;element name="endPosition" type="gml:TimePositionType"/&gt;
     *                      &lt;element name="end" type="gml:TimeInstantPropertyType"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;group minOccurs="0" ref="gml:timeLength"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEPERIODTYPE_TYPE = build_TIMEPERIODTYPE_TYPE();
    
    private static ComplexType build_TIMEPERIODTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","beginPosition"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEINSTANTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","begin"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","endPosition"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEINSTANTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","end"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DURATION_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","duration"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEINTERVALLENGTHTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","timeInterval"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimePeriodType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTIMEGEOMETRICPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimePeriodPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimePeriodPropertyType provides for associating a gml:TimePeriod with an object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TimePeriod"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEPERIODPROPERTYTYPE_TYPE = build_TIMEPERIODPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMEPERIODPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPERIODTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TimePeriod"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimePeriodPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeEdgeType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTimeTopologyPrimitiveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;!--element name="start" type="gml:TimeNodePropertyType"/&gt;
     *  					&lt;element name="end" type="gml:TimeNodePropertyType"/--&gt;
     *                  &lt;element minOccurs="0" name="extent" type="gml:TimePeriodPropertyType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEEDGETYPE_TYPE = build_TIMEEDGETYPE_TYPE();
    
    private static ComplexType build_TIMEEDGETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPERIODPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","extent"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeEdgeType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTIMETOPOLOGYPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeEdgePropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimeEdgePropertyType provides for associating a gml:TimeEdge with an object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TimeEdge"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEEDGEPROPERTYTYPE_TYPE = build_TIMEEDGEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMEEDGEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEEDGETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TimeEdge"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeEdgePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeNodeType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTimeTopologyPrimitiveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0"
     *                      name="previousEdge" type="gml:TimeEdgePropertyType"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0"
     *                      name="nextEdge" type="gml:TimeEdgePropertyType"/&gt;
     *                  &lt;element minOccurs="0" name="position" type="gml:TimeInstantPropertyType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMENODETYPE_TYPE = build_TIMENODETYPE_TYPE();
    
    private static ComplexType build_TIMENODETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEEDGEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","previousEdge"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEEDGEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nextEdge"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEINSTANTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","position"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeNodeType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTIMETOPOLOGYPRIMITIVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeNodePropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimeNodePropertyType provides for associating a gml:TimeNode with an object&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TimeNode"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMENODEPROPERTYTYPE_TYPE = build_TIMENODEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMENODEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMENODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TimeNode"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeNodePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeOrdinalEraType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:DefinitionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0"
     *                      name="relatedTime" type="gml:RelatedTimeType"/&gt;
     *                  &lt;element minOccurs="0" name="start" type="gml:TimeNodePropertyType"/&gt;
     *                  &lt;element minOccurs="0" name="end" type="gml:TimeNodePropertyType"/&gt;
     *                  &lt;element minOccurs="0" name="extent" type="gml:TimePeriodPropertyType"/&gt;
     *                  &lt;!--element name="member" type="gml:TimeOrdinalEraPropertyType" minOccurs="0" maxOccurs="unbounded"/--&gt;
     *                  &lt;element minOccurs="0" name="group" type="gml:ReferenceType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEORDINALERATYPE_TYPE = build_TIMEORDINALERATYPE_TYPE();
    
    private static ComplexType build_TIMEORDINALERATYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                RELATEDTIMETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","relatedTime"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMENODEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","start"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMENODEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","end"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPERIODPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","extent"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","group"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeOrdinalEraType"), schema, false,
            false, Collections.<Filter>emptyList(), DEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeOrdinalEraPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimeOrdinalEraPropertyType provides for associating a gml:TimeOrdinalEra with an object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TimeOrdinalEra"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEORDINALERAPROPERTYTYPE_TYPE = build_TIMEORDINALERAPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMEORDINALERAPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEORDINALERATYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TimeOrdinalEra"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeOrdinalEraPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeOrdinalReferenceSystemType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:TimeReferenceSystemType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" name="component" type="gml:TimeOrdinalEraPropertyType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMEORDINALREFERENCESYSTEMTYPE_TYPE = build_TIMEORDINALREFERENCESYSTEMTYPE_TYPE();
    
    private static ComplexType build_TIMEORDINALREFERENCESYSTEMTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEORDINALERAPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","component"), 1, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeOrdinalReferenceSystemType"), schema, false,
            false, Collections.<Filter>emptyList(), TIMEREFERENCESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractCurveSegmentType"&gt;
     *      &lt;attribute default="0" name="numDerivativesAtStart" type="integer"/&gt;
     *      &lt;attribute default="0" name="numDerivativesAtEnd" type="integer"/&gt;
     *      &lt;attribute default="0" name="numDerivativeInterior" type="integer"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTCURVESEGMENTTYPE_TYPE = build_ABSTRACTCURVESEGMENTTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTCURVESEGMENTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","numDerivativesAtStart"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","numDerivativesAtEnd"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","numDerivativeInterior"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractCurveSegmentType"), schema, false,
            true, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="VectorType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;For some applications the components of the position may be adjusted to yield a unit vector.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;restriction base="gml:DirectPositionType"/&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VECTORTYPE_TYPE = build_VECTORTYPE_TYPE();
    
    private static ComplexType build_VECTORTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","VectorType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), DIRECTPOSITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="CurveInterpolationType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CurveInterpolationType is a list of codes that may be used to identify the interpolation mechanisms specified by an application schema.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="linear"/&gt;
     *          &lt;enumeration value="geodesic"/&gt;
     *          &lt;enumeration value="circularArc3Points"/&gt;
     *          &lt;enumeration value="circularArc2PointWithBulge"/&gt;
     *          &lt;enumeration value="circularArcCenterPointWithRadius"/&gt;
     *          &lt;enumeration value="elliptical"/&gt;
     *          &lt;enumeration value="clothoid"/&gt;
     *          &lt;enumeration value="conic"/&gt;
     *          &lt;enumeration value="polynomialSpline"/&gt;
     *          &lt;enumeration value="cubicSpline"/&gt;
     *          &lt;enumeration value="rationalSpline"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType CURVEINTERPOLATIONTYPE_TYPE = build_CURVEINTERPOLATIONTYPE_TYPE();
     
    private static AttributeType build_CURVEINTERPOLATIONTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CurveInterpolationType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ArcStringByBulgeType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveSegmentType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice maxOccurs="unbounded" minOccurs="2"&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element maxOccurs="unbounded" name="bulge" type="double"/&gt;
     *                  &lt;element maxOccurs="unbounded" name="normal" type="gml:VectorType"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="circularArc2PointWithBulge"
     *                  name="interpolation" type="gml:CurveInterpolationType"/&gt;
     *              &lt;attribute name="numArc" type="integer"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ARCSTRINGBYBULGETYPE_TYPE = build_ARCSTRINGBYBULGETYPE_TYPE();
    
    private static ComplexType build_ARCSTRINGBYBULGETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointRep"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","bulge"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                VECTORTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","normal"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","numArc"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ArcStringByBulgeType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVESEGMENTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ArcByBulgeType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:ArcStringByBulgeType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice maxOccurs="2" minOccurs="2"&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element name="bulge" type="double"/&gt;
     *                  &lt;element name="normal" type="gml:VectorType"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="1" name="numArc" type="integer"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ARCBYBULGETYPE_TYPE = build_ARCBYBULGETYPE_TYPE();
    
    private static ComplexType build_ARCBYBULGETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointRep"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","bulge"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                VECTORTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","normal"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","numArc"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ArcByBulgeType"), schema, false,
            false, Collections.<Filter>emptyList(), ARCSTRINGBYBULGETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="NameList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A type for a list of values of the respective simple type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="Name"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NAMELIST_TYPE = build_NAMELIST_TYPE();
     
    private static AttributeType build_NAMELIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","NameList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CodeListType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CodeListType provides for lists of terms. The values in an instance element shall all be valid according to the rules of the dictionary, classification scheme, or authority identified by the value of its codeSpace attribute.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:NameList"&gt;
     *              &lt;attribute name="codeSpace" type="anyURI"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODELISTTYPE_TYPE = build_CODELISTTYPE_TYPE();
    
    private static ComplexType build_CODELISTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","codeSpace"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CodeListType"), schema, false,
            false, Collections.<Filter>emptyList(), NAMELIST_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="FeaturePropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractFeature"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType FEATUREPROPERTYTYPE_TYPE = build_FEATUREPROPERTYTYPE_TYPE();
    
    private static ComplexType build_FEATUREPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTFEATURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractFeature"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","FeaturePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="FeatureArrayPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractFeature"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType FEATUREARRAYPROPERTYTYPE_TYPE = build_FEATUREARRAYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_FEATUREARRAYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTFEATURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractFeature"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","FeatureArrayPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractFeatureCollectionType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractFeatureType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:featureMember"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:featureMembers"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTFEATURECOLLECTIONTYPE_TYPE = build_ABSTRACTFEATURECOLLECTIONTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTFEATURECOLLECTIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                FEATUREPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","featureMember"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                FEATUREARRAYPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","featureMembers"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractFeatureCollectionType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTFEATURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="FeatureCollectionType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractFeatureCollectionType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType FEATURECOLLECTIONTYPE_TYPE = build_FEATURECOLLECTIONTYPE_TYPE();
    
    private static ComplexType build_FEATURECOLLECTIONTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","FeatureCollectionType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTFEATURECOLLECTIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CubicSplineType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveSegmentType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice maxOccurs="unbounded" minOccurs="2"&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element name="vectorAtStart" type="gml:VectorType"/&gt;
     *                  &lt;element name="vectorAtEnd" type="gml:VectorType"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="cubicSpline" name="interpolation" type="gml:CurveInterpolationType"/&gt;
     *              &lt;attribute fixed="3" name="degree" type="integer"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CUBICSPLINETYPE_TYPE = build_CUBICSPLINETYPE_TYPE();
    
    private static ComplexType build_CUBICSPLINETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointRep"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                VECTORTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","vectorAtStart"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                VECTORTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","vectorAtEnd"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","degree"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CubicSplineType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVESEGMENTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeometricPrimitivePropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a geometric primitive as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractGeometricPrimitive"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEOMETRICPRIMITIVEPROPERTYTYPE_TYPE = build_GEOMETRICPRIMITIVEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_GEOMETRICPRIMITIVEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGEOMETRICPRIMITIVETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometricPrimitive"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeometricPrimitivePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeometricComplexType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometryType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" name="element" type="gml:GeometricPrimitivePropertyType"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEOMETRICCOMPLEXTYPE_TYPE = build_GEOMETRICCOMPLEXTYPE_TYPE();
    
    private static ComplexType build_GEOMETRICCOMPLEXTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GEOMETRICPRIMITIVEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","element"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeometricComplexType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGEOMETRYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CompositeCurveType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:curveMember"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COMPOSITECURVETYPE_TYPE = build_COMPOSITECURVETYPE_TYPE();
    
    private static ComplexType build_COMPOSITECURVETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CURVEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","curveMember"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CompositeCurveType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CompositeSurfaceType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractSurfaceType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:surfaceMember"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COMPOSITESURFACETYPE_TYPE = build_COMPOSITESURFACETYPE_TYPE();
    
    private static ComplexType build_COMPOSITESURFACETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SURFACEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","surfaceMember"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CompositeSurfaceType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTSURFACETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CompositeSolidType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractSolidType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:solidMember"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COMPOSITESOLIDTYPE_TYPE = build_COMPOSITESOLIDTYPE_TYPE();
    
    private static ComplexType build_COMPOSITESOLIDTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SOLIDPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","solidMember"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CompositeSolidType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTSOLIDTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeometricComplexPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a geometric complex as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;choice&gt;
     *              &lt;element ref="gml:GeometricComplex"/&gt;
     *              &lt;element ref="gml:CompositeCurve"/&gt;
     *              &lt;element ref="gml:CompositeSurface"/&gt;
     *              &lt;element ref="gml:CompositeSolid"/&gt;
     *          &lt;/choice&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEOMETRICCOMPLEXPROPERTYTYPE_TYPE = build_GEOMETRICCOMPLEXPROPERTYTYPE_TYPE();
    
    private static ComplexType build_GEOMETRICCOMPLEXPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GEOMETRICCOMPLEXTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","GeometricComplex"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COMPOSITECURVETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","CompositeCurve"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COMPOSITESURFACETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","CompositeSurface"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COMPOSITESOLIDTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","CompositeSolid"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeometricComplexPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="_Count"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="integer"&gt;
     *              &lt;attribute name="nilReason" type="gml:NilReasonType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType _COUNT_TYPE = build__COUNT_TYPE();
    
    private static ComplexType build__COUNT_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","_Count"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.INTEGER_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CountPropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Count"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COUNTPROPERTYTYPE_TYPE = build_COUNTPROPERTYTYPE_TYPE();
    
    private static ComplexType build_COUNTPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                _COUNT_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Count"), 1, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CountPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LineStringType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice maxOccurs="unbounded" minOccurs="2"&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType LINESTRINGTYPE_TYPE = build_LINESTRINGTYPE_TYPE();
     
    private static AttributeType build_LINESTRINGTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","LineStringType"), com.vividsolutions.jts.geom.LineString.class, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractFeatureMemberType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;To create a collection of GML features, a property type shall be derived by extension from gml:AbstractFeatureMemberType.
     *  By default, this abstract property type does not imply any ownership of the features in the collection. The owns attribute of gml:OwnershipAttributeGroup may be used on a property element instance to assert ownership of a feature in the collection. A collection shall not own a feature already owned by another object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTFEATUREMEMBERTYPE_TYPE = build_ABSTRACTFEATUREMEMBERTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTFEATUREMEMBERTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractFeatureMemberType"), schema, false,
            true, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractTimeSliceType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGMLType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:validTime"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:dataSource"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTTIMESLICETYPE_TYPE = build_ABSTRACTTIMESLICETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTTIMESLICETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPRIMITIVEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","validTime"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","dataSource"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeSliceType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTGMLTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="HistoryPropertyType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element maxOccurs="unbounded" ref="gml:AbstractTimeSlice"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType HISTORYPROPERTYTYPE_TYPE = build_HISTORYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_HISTORYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTTIMESLICETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeSlice"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","HistoryPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DynamicFeatureType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractFeatureType"&gt;
     *              &lt;group ref="gml:dynamicProperties"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DYNAMICFEATURETYPE_TYPE = build_DYNAMICFEATURETYPE_TYPE();
    
    private static ComplexType build_DYNAMICFEATURETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPRIMITIVEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","validTime"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                HISTORYPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","history"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","dataSource"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","dataSourceReference"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DynamicFeatureType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTFEATURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DynamicFeatureMemberType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractFeatureMemberType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:DynamicFeature"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DYNAMICFEATUREMEMBERTYPE_TYPE = build_DYNAMICFEATUREMEMBERTYPE_TYPE();
    
    private static ComplexType build_DYNAMICFEATUREMEMBERTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DYNAMICFEATURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","DynamicFeature"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DynamicFeatureMemberType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTFEATUREMEMBERTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DynamicFeatureCollectionType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:DynamicFeatureType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:dynamicMembers"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DYNAMICFEATURECOLLECTIONTYPE_TYPE = build_DYNAMICFEATURECOLLECTIONTYPE_TYPE();
    
    private static ComplexType build_DYNAMICFEATURECOLLECTIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DYNAMICFEATUREMEMBERTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","dynamicMembers"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DynamicFeatureCollectionType"), schema, false,
            false, Collections.<Filter>emptyList(), DYNAMICFEATURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PointArrayPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:PointArrayPropertyType is a container for an array of points. The elements are always contained inline in the array property, referencing geometry elements or arrays of geometry elements via XLinks is not supported.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
     *          &lt;element ref="gml:Point"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType POINTARRAYPROPERTYTYPE_TYPE = build_POINTARRAYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_POINTARRAYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                POINTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Point"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PointArrayPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiPointType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometricAggregateType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:pointMember"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:pointMembers"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTIPOINTTYPE_TYPE = build_MULTIPOINTTYPE_TYPE();
     
    private static AttributeType build_MULTIPOINTTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MultiPointType"), com.vividsolutions.jts.geom.MultiPoint.class, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGEOMETRICAGGREGATETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiPointPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a collection of points as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:MultiPoint"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTIPOINTPROPERTYTYPE_TYPE = build_MULTIPOINTPROPERTYTYPE_TYPE();
     
    private static AttributeType build_MULTIPOINTPROPERTYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MultiPointPropertyType"), com.vividsolutions.jts.geom.MultiPoint.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="doubleOrNilReasonList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A type for a list of values of the respective simple type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="gml:doubleOrNilReason"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DOUBLEORNILREASONLIST_TYPE = build_DOUBLEORNILREASONLIST_TYPE();
     
    private static AttributeType build_DOUBLEORNILREASONLIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","doubleOrNilReasonList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MeasureOrNilReasonListType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:MeasureOrNilReasonListType provides for a list of quantities. An instance element may also include embedded values from NilReasonType. It is intended to be used in situations where a value is expected, but the value may be absent for some reason.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:doubleOrNilReasonList"&gt;
     *              &lt;attribute name="uom" type="gml:UomIdentifier" use="required"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MEASUREORNILREASONLISTTYPE_TYPE = build_MEASUREORNILREASONLISTTYPE_TYPE();
    
    private static ComplexType build_MEASUREORNILREASONLISTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                UOMIDENTIFIER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","uom"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MeasureOrNilReasonListType"), schema, false,
            false, Collections.<Filter>emptyList(), DOUBLEORNILREASONLIST_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="QuantityExtentType"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;restriction base="gml:MeasureOrNilReasonListType"&gt;
     *              &lt;length value="2"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType QUANTITYEXTENTTYPE_TYPE = build_QUANTITYEXTENTTYPE_TYPE();
    
    private static ComplexType build_QUANTITYEXTENTTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","QuantityExtentType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MEASUREORNILREASONLISTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DomainSetType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;choice&gt;
     *              &lt;element ref="gml:AbstractGeometry"/&gt;
     *              &lt;element ref="gml:AbstractTimeObject"/&gt;
     *          &lt;/choice&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DOMAINSETTYPE_TYPE = build_DOMAINSETTYPE_TYPE();
    
    private static ComplexType build_DOMAINSETTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGEOMETRYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometry"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTTIMEOBJECTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeObject"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DomainSetType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CompositeValueType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGMLType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;!--element ref="gml:valueComponent" minOccurs="0" maxOccurs="unbounded"/&gt;
     *  					&lt;element ref="gml:valueComponents" minOccurs="0"/--&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COMPOSITEVALUETYPE_TYPE = build_COMPOSITEVALUETYPE_TYPE();
    
    private static ComplexType build_COMPOSITEVALUETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CompositeValueType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGMLTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ValueArrayType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:CompositeValueType"&gt;
     *              &lt;attributeGroup ref="gml:referenceSystem"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VALUEARRAYTYPE_TYPE = build_VALUEARRAYTYPE_TYPE();
    
    private static ComplexType build_VALUEARRAYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","codeSpace"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                UOMIDENTIFIER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","uom"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ValueArrayType"), schema, false,
            false, Collections.<Filter>emptyList(), COMPOSITEVALUETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AssociationRoleType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;any namespace="##any"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ASSOCIATIONROLETYPE_TYPE = build_ASSOCIATIONROLETYPE_TYPE();
    
    private static ComplexType build_ASSOCIATIONROLETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AssociationRoleType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DataBlockType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:rangeParameters"/&gt;
     *          &lt;choice&gt;
     *              &lt;element ref="gml:tupleList"/&gt;
     *              &lt;element ref="gml:doubleOrNilReasonTupleList"/&gt;
     *          &lt;/choice&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DATABLOCKTYPE_TYPE = build_DATABLOCKTYPE_TYPE();
    
    private static ComplexType build_DATABLOCKTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ASSOCIATIONROLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","rangeParameters"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","tupleList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DOUBLEORNILREASONLIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","doubleOrNilReasonTupleList"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DataBlockType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="FileType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:rangeParameters"/&gt;
     *          &lt;choice&gt;
     *              &lt;element name="fileName" type="anyURI"&gt;
     *                  &lt;annotation&gt;
     *                      &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *                  &lt;/annotation&gt;
     *              &lt;/element&gt;
     *              &lt;element name="fileReference" type="anyURI"/&gt;
     *          &lt;/choice&gt;
     *          &lt;element name="fileStructure" type="gml:CodeType"/&gt;
     *          &lt;element minOccurs="0" name="mimeType" type="anyURI"/&gt;
     *          &lt;element minOccurs="0" name="compression" type="anyURI"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType FILETYPE_TYPE = build_FILETYPE_TYPE();
    
    private static ComplexType build_FILETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ASSOCIATIONROLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","rangeParameters"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","fileName"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","fileReference"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","fileStructure"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","mimeType"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","compression"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","FileType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="RangeSetType"&gt;
     *      &lt;choice&gt;
     *          &lt;element maxOccurs="unbounded" ref="gml:ValueArray"/&gt;
     *          &lt;element maxOccurs="unbounded" ref="gml:AbstractScalarValueList"/&gt;
     *          &lt;element ref="gml:DataBlock"/&gt;
     *          &lt;element ref="gml:File"/&gt;
     *      &lt;/choice&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType RANGESETTYPE_TYPE = build_RANGESETTYPE_TYPE();
    
    private static ComplexType build_RANGESETTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                VALUEARRAYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ValueArray"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractScalarValueList"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DATABLOCKTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","DataBlock"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                FILETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","File"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","RangeSetType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractCoverageType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;The base type for coverages is gml:AbstractCoverageType. The basic elements of a coverage can be seen in this content model: the coverage contains gml:domainSet and gml:rangeSet properties. The gml:domainSet property describes the domain of the coverage and the gml:rangeSet property describes the range of the coverage.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractFeatureType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:domainSet"/&gt;
     *                  &lt;element ref="gml:rangeSet"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTCOVERAGETYPE_TYPE = build_ABSTRACTCOVERAGETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTCOVERAGETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DOMAINSETTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","domainSet"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                RANGESETTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","rangeSet"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractCoverageType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTFEATURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType final="#all" name="MappingRuleType"&gt;
     *      &lt;choice&gt;
     *          &lt;element name="ruleDefinition" type="string"/&gt;
     *          &lt;element name="ruleReference" type="gml:ReferenceType"/&gt;
     *      &lt;/choice&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MAPPINGRULETYPE_TYPE = build_MAPPINGRULETYPE_TYPE();
    
    private static ComplexType build_MAPPINGRULETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ruleDefinition"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ruleReference"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MappingRuleType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="SequenceRuleEnumeration"&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="Linear"/&gt;
     *          &lt;enumeration value="Boustrophedonic"/&gt;
     *          &lt;enumeration value="Cantor-diagonal"/&gt;
     *          &lt;enumeration value="Spiral"/&gt;
     *          &lt;enumeration value="Morton"/&gt;
     *          &lt;enumeration value="Hilbert"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType SEQUENCERULEENUMERATION_TYPE = build_SEQUENCERULEENUMERATION_TYPE();
     
    private static AttributeType build_SEQUENCERULEENUMERATION_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SequenceRuleEnumeration"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="IncrementOrder"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="+x+y"/&gt;
     *          &lt;enumeration value="+y+x"/&gt;
     *          &lt;enumeration value="+x-y"/&gt;
     *          &lt;enumeration value="-x-y"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType INCREMENTORDER_TYPE = build_INCREMENTORDER_TYPE();
     
    private static AttributeType build_INCREMENTORDER_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","IncrementOrder"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="AxisDirectionList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;The different values in a gml:AxisDirectionList indicate the incrementation order to be used on all axes of the grid. Each axis shall be mentioned once and only once.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="gml:AxisDirection"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType AXISDIRECTIONLIST_TYPE = build_AXISDIRECTIONLIST_TYPE();
     
    private static AttributeType build_AXISDIRECTIONLIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AxisDirectionList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SequenceRuleType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;The gml:SequenceRuleType is derived from the gml:SequenceRuleEnumeration through the addition of an axisOrder attribute.  The gml:SequenceRuleEnumeration is an enumerated type. The rule names are defined in ISO 19123. If no rule name is specified the default is "Linear".&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:SequenceRuleEnumeration"&gt;
     *              &lt;attribute name="order" type="gml:IncrementOrder"&gt;
     *                  &lt;annotation&gt;
     *                      &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *                  &lt;/annotation&gt;
     *              &lt;/attribute&gt;
     *              &lt;attribute name="axisOrder" type="gml:AxisDirectionList"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SEQUENCERULETYPE_TYPE = build_SEQUENCERULETYPE_TYPE();
    
    private static ComplexType build_SEQUENCERULETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                INCREMENTORDER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","order"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AXISDIRECTIONLIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","axisOrder"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SequenceRuleType"), schema, false,
            false, Collections.<Filter>emptyList(), SEQUENCERULEENUMERATION_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="integerList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A type for a list of values of the respective simple type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="integer"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType INTEGERLIST_TYPE = build_INTEGERLIST_TYPE();
     
    private static AttributeType build_INTEGERLIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","integerList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GridFunctionType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element minOccurs="0" name="sequenceRule" type="gml:SequenceRuleType"/&gt;
     *          &lt;element minOccurs="0" name="startPoint" type="gml:integerList"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GRIDFUNCTIONTYPE_TYPE = build_GRIDFUNCTIONTYPE_TYPE();
    
    private static ComplexType build_GRIDFUNCTIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SEQUENCERULETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","sequenceRule"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                INTEGERLIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","startPoint"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GridFunctionType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CoverageFunctionType"&gt;
     *      &lt;choice&gt;
     *          &lt;element ref="gml:MappingRule"/&gt;
     *          &lt;element ref="gml:CoverageMappingRule"/&gt;
     *          &lt;element ref="gml:GridFunction"/&gt;
     *      &lt;/choice&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COVERAGEFUNCTIONTYPE_TYPE = build_COVERAGEFUNCTIONTYPE_TYPE();
    
    private static ComplexType build_COVERAGEFUNCTIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","MappingRule"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                MAPPINGRULETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","CoverageMappingRule"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                GRIDFUNCTIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","GridFunction"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CoverageFunctionType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DiscreteCoverageType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoverageType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:coverageFunction"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DISCRETECOVERAGETYPE_TYPE = build_DISCRETECOVERAGETYPE_TYPE();
    
    private static ComplexType build_DISCRETECOVERAGETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                COVERAGEFUNCTIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coverageFunction"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DiscreteCoverageType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOVERAGETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LengthType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;This is a prototypical definition for a specific measure type defined as a vacuous extension (i.e. aliases) of gml:MeasureType. In this case, the content model supports the description of a length (or distance) quantity, with its units. The unit of measure referenced by uom shall be suitable for a length, such as metres or feet.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:MeasureType"/&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType LENGTHTYPE_TYPE = build_LENGTHTYPE_TYPE();
    
    private static ComplexType build_LENGTHTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","LengthType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AngleType"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:MeasureType"/&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ANGLETYPE_TYPE = build_ANGLETYPE_TYPE();
    
    private static ComplexType build_ANGLETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AngleType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ArcByCenterPointType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveSegmentType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element name="radius" type="gml:LengthType"/&gt;
     *                  &lt;element minOccurs="0" name="startAngle" type="gml:AngleType"/&gt;
     *                  &lt;element minOccurs="0" name="endAngle" type="gml:AngleType"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="circularArcCenterPointWithRadius"
     *                  name="interpolation" type="gml:CurveInterpolationType"/&gt;
     *              &lt;attribute fixed="1" name="numArc" type="integer" use="required"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ARCBYCENTERPOINTTYPE_TYPE = build_ARCBYCENTERPOINTTYPE_TYPE();
    
    private static ComplexType build_ARCBYCENTERPOINTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointRep"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                LENGTHTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","radius"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ANGLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","startAngle"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ANGLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","endAngle"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","numArc"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ArcByCenterPointType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVESEGMENTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CircleByCenterPointType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:ArcByCenterPointType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element name="radius" type="gml:LengthType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CIRCLEBYCENTERPOINTTYPE_TYPE = build_CIRCLEBYCENTERPOINTTYPE_TYPE();
    
    private static ComplexType build_CIRCLEBYCENTERPOINTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointRep"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                LENGTHTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","radius"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CircleByCenterPointType"), schema, false,
            false, Collections.<Filter>emptyList(), ARCBYCENTERPOINTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PrimeMeridianType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:greenwichLongitude"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PRIMEMERIDIANTYPE_TYPE = build_PRIMEMERIDIANTYPE_TYPE();
    
    private static ComplexType build_PRIMEMERIDIANTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ANGLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","greenwichLongitude"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PrimeMeridianType"), schema, false,
            false, Collections.<Filter>emptyList(), IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PrimeMeridianPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:PrimeMeridianPropertyType is a property type for association roles to a prime meridian, either referencing or containing the definition of that meridian.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:PrimeMeridian"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PRIMEMERIDIANPROPERTYTYPE_TYPE = build_PRIMEMERIDIANPROPERTYTYPE_TYPE();
    
    private static ComplexType build_PRIMEMERIDIANPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                PRIMEMERIDIANTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","PrimeMeridian"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PrimeMeridianPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="_SecondDefiningParameter"&gt;
     *      &lt;choice&gt;
     *          &lt;element name="inverseFlattening" type="gml:MeasureType"/&gt;
     *          &lt;element name="semiMinorAxis" type="gml:LengthType"/&gt;
     *          &lt;element default="true" name="isSphere" type="boolean"/&gt;
     *      &lt;/choice&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType _SECONDDEFININGPARAMETER_TYPE = build__SECONDDEFININGPARAMETER_TYPE();
    
    private static ComplexType build__SECONDDEFININGPARAMETER_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                MEASURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","inverseFlattening"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                LENGTHTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","semiMinorAxis"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","isSphere"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","_SecondDefiningParameter"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="_secondDefiningParameter"&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:SecondDefiningParameter"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType _sECONDDEFININGPARAMETER_TYPE = build__sECONDDEFININGPARAMETER_TYPE();
    
    private static ComplexType build__sECONDDEFININGPARAMETER_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                _SECONDDEFININGPARAMETER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","SecondDefiningParameter"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","_secondDefiningParameter"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EllipsoidType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:semiMajorAxis"/&gt;
     *                  &lt;element ref="gml:secondDefiningParameter"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ELLIPSOIDTYPE_TYPE = build_ELLIPSOIDTYPE_TYPE();
    
    private static ComplexType build_ELLIPSOIDTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                MEASURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","semiMajorAxis"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                _SECONDDEFININGPARAMETER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","secondDefiningParameter"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EllipsoidType"), schema, false,
            false, Collections.<Filter>emptyList(), IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EllipsoidPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:EllipsoidPropertyType is a property type for association roles to an ellipsoid, either referencing or containing the definition of that ellipsoid.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Ellipsoid"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ELLIPSOIDPROPERTYTYPE_TYPE = build_ELLIPSOIDPROPERTYTYPE_TYPE();
    
    private static ComplexType build_ELLIPSOIDPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ELLIPSOIDTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Ellipsoid"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EllipsoidPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeodeticDatumType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractDatumType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:primeMeridian"/&gt;
     *                  &lt;element ref="gml:ellipsoid"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEODETICDATUMTYPE_TYPE = build_GEODETICDATUMTYPE_TYPE();
    
    private static ComplexType build_GEODETICDATUMTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                PRIMEMERIDIANPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","primeMeridian"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ELLIPSOIDPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ellipsoid"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeodeticDatumType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeodeticDatumPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:GeodeticDatumPropertyType is a property type for association roles to a geodetic datum, either referencing or containing the definition of that datum.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:GeodeticDatum"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEODETICDATUMPROPERTYTYPE_TYPE = build_GEODETICDATUMPROPERTYTYPE_TYPE();
    
    private static ComplexType build_GEODETICDATUMPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GEODETICDATUMTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","GeodeticDatum"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeodeticDatumPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeocentricCRSType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:usesCartesianCS"/&gt;
     *                      &lt;element ref="gml:usesSphericalCS"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element ref="gml:usesGeodeticDatum"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEOCENTRICCRSTYPE_TYPE = build_GEOCENTRICCRSTYPE_TYPE();
    
    private static ComplexType build_GEOCENTRICCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CARTESIANCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","usesCartesianCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SPHERICALCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","usesSphericalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                GEODETICDATUMPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","usesGeodeticDatum"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeocentricCRSType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeocentricCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:GeocentricCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEOCENTRICCRSPROPERTYTYPE_TYPE = build_GEOCENTRICCRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_GEOCENTRICCRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GEOCENTRICCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","GeocentricCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeocentricCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GridEnvelopeType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element name="low" type="gml:integerList"/&gt;
     *          &lt;element name="high" type="gml:integerList"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GRIDENVELOPETYPE_TYPE = build_GRIDENVELOPETYPE_TYPE();
    
    private static ComplexType build_GRIDENVELOPETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                INTEGERLIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","low"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                INTEGERLIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","high"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GridEnvelopeType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GridLimitsType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element name="GridEnvelope" type="gml:GridEnvelopeType"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GRIDLIMITSTYPE_TYPE = build_GRIDLIMITSTYPE_TYPE();
    
    private static ComplexType build_GRIDLIMITSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GRIDENVELOPETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","GridEnvelope"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GridLimitsType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GridType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometryType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element name="limits" type="gml:GridLimitsType"/&gt;
     *                  &lt;choice&gt;
     *                      &lt;element name="axisLabels" type="gml:NCNameList"/&gt;
     *                      &lt;element maxOccurs="unbounded" name="axisName" type="string"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute name="dimension" type="positiveInteger" use="required"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GRIDTYPE_TYPE = build_GRIDTYPE_TYPE();
    
    private static ComplexType build_GRIDTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GRIDLIMITSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","limits"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NCNAMELIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","axisLabels"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","axisName"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","dimension"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GridType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGEOMETRYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="RectifiedGridType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:GridType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element name="origin" type="gml:PointPropertyType"/&gt;
     *                  &lt;element maxOccurs="unbounded" name="offsetVector" type="gml:VectorType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType RECTIFIEDGRIDTYPE_TYPE = build_RECTIFIEDGRIDTYPE_TYPE();
    
    private static ComplexType build_RECTIFIEDGRIDTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","origin"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                VECTORTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","offsetVector"), 1, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","RectifiedGridType"), schema, false,
            false, Collections.<Filter>emptyList(), GRIDTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoSurfaceType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTopologyType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:directedFace"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOSURFACETYPE_TYPE = build_TOPOSURFACETYPE_TYPE();
    
    private static ComplexType build_TOPOSURFACETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTEDFACEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","directedFace"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoSurfaceType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTOPOLOGYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoSurfacePropertyType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:TopoSurface"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOSURFACEPROPERTYTYPE_TYPE = build_TOPOSURFACEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TOPOSURFACEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TOPOSURFACETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TopoSurface"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoSurfacePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="SuccessionType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="substitution"/&gt;
     *          &lt;enumeration value="division"/&gt;
     *          &lt;enumeration value="fusion"/&gt;
     *          &lt;enumeration value="initiation"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType SUCCESSIONTYPE_TYPE = build_SUCCESSIONTYPE_TYPE();
     
    private static AttributeType build_SUCCESSIONTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SuccessionType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="booleanOrNilReason"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Extension to the respective XML Schema built-in simple type to allow a choice of either a value of the built-in simple type or a reason for a nil value.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;union memberTypes="gml:NilReasonEnumeration boolean anyURI"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType BOOLEANORNILREASON_TYPE = build_BOOLEANORNILREASON_TYPE();
     
    private static AttributeType build_BOOLEANORNILREASON_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","booleanOrNilReason"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeometryPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A geometric property may either be any geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same or another document). Note that either the reference or the contained element shall be given, but not both or none.
     *  If a feature has a property that takes a geometry element as its value, this is called a geometry property. A generic type for such a geometry property is GeometryPropertyType.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractGeometry"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType GEOMETRYPROPERTYTYPE_TYPE = build_GEOMETRYPROPERTYTYPE_TYPE();
     
    private static AttributeType build_GEOMETRYPROPERTYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeometryPropertyType"), com.vividsolutions.jts.geom.Geometry.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectionVectorType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Direction vectors are specified by providing components of a vector.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;choice&gt;
     *          &lt;element ref="gml:vector"/&gt;
     *          &lt;sequence&gt;
     *              &lt;annotation&gt;
     *                  &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *              &lt;/annotation&gt;
     *              &lt;element name="horizontalAngle" type="gml:AngleType"/&gt;
     *              &lt;element name="verticalAngle" type="gml:AngleType"/&gt;
     *          &lt;/sequence&gt;
     *      &lt;/choice&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTIONVECTORTYPE_TYPE = build_DIRECTIONVECTORTYPE_TYPE();
    
    private static ComplexType build_DIRECTIONVECTORTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                VECTORTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","vector"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ANGLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","horizontalAngle"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ANGLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","verticalAngle"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectionVectorType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="CompassPointEnumeration"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;These directions are necessarily approximate, giving direction with a precision of 22.5�. It is thus generally unnecessary to specify the reference frame, though this may be detailed in the definition of a GML application language.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="N"/&gt;
     *          &lt;enumeration value="NNE"/&gt;
     *          &lt;enumeration value="NE"/&gt;
     *          &lt;enumeration value="ENE"/&gt;
     *          &lt;enumeration value="E"/&gt;
     *          &lt;enumeration value="ESE"/&gt;
     *          &lt;enumeration value="SE"/&gt;
     *          &lt;enumeration value="SSE"/&gt;
     *          &lt;enumeration value="S"/&gt;
     *          &lt;enumeration value="SSW"/&gt;
     *          &lt;enumeration value="SW"/&gt;
     *          &lt;enumeration value="WSW"/&gt;
     *          &lt;enumeration value="W"/&gt;
     *          &lt;enumeration value="WNW"/&gt;
     *          &lt;enumeration value="NW"/&gt;
     *          &lt;enumeration value="NNW"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType COMPASSPOINTENUMERATION_TYPE = build_COMPASSPOINTENUMERATION_TYPE();
     
    private static AttributeType build_COMPASSPOINTENUMERATION_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CompassPointEnumeration"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectionDescriptionType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;direction descriptions are specified by a compass point code, a keyword, a textual description or a reference to a description.
     *  A gml:compassPoint is specified by a simple enumeration.  	
     *  In addition, thre elements to contain text-based descriptions of direction are provided.  
     *  If the direction is specified using a term from a list, gml:keyword should be used, and the list indicated using the value of the codeSpace attribute. 
     *  if the direction is decribed in prose, gml:direction or gml:reference should be used, allowing the value to be included inline or by reference.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;choice&gt;
     *          &lt;element name="compassPoint" type="gml:CompassPointEnumeration"/&gt;
     *          &lt;element name="keyword" type="gml:CodeType"/&gt;
     *          &lt;element name="description" type="string"/&gt;
     *          &lt;element name="reference" type="gml:ReferenceType"/&gt;
     *      &lt;/choice&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTIONDESCRIPTIONTYPE_TYPE = build_DIRECTIONDESCRIPTIONTYPE_TYPE();
    
    private static ComplexType build_DIRECTIONDESCRIPTIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                COMPASSPOINTENUMERATION_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","compassPoint"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","keyword"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","description"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","reference"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectionDescriptionType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectionPropertyType"&gt;
     *      &lt;choice minOccurs="0"&gt;
     *          &lt;element name="DirectionVector" type="gml:DirectionVectorType"/&gt;
     *          &lt;element name="DirectionDescription" type="gml:DirectionDescriptionType"/&gt;
     *          &lt;element name="CompassPoint" type="gml:CompassPointEnumeration"/&gt;
     *          &lt;element name="DirectionKeyword" type="gml:CodeType"/&gt;
     *          &lt;element name="DirectionString" type="gml:StringOrRefType"/&gt;
     *      &lt;/choice&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTIONPROPERTYTYPE_TYPE = build_DIRECTIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_DIRECTIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTIONVECTORTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","DirectionVector"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTIONDESCRIPTIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","DirectionDescription"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COMPASSPOINTENUMERATION_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","CompassPoint"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","DirectionKeyword"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","DirectionString"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectionPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MovingObjectStatusType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTimeSliceType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element name="position" type="gml:GeometryPropertyType"/&gt;
     *                      &lt;element ref="gml:pos"/&gt;
     *                      &lt;element ref="gml:locationName"/&gt;
     *                      &lt;element ref="gml:locationReference"/&gt;
     *                      &lt;element ref="gml:location"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element minOccurs="0" name="speed" type="gml:MeasureType"/&gt;
     *                  &lt;element minOccurs="0" name="bearing" type="gml:DirectionPropertyType"/&gt;
     *                  &lt;element minOccurs="0" name="acceleration" type="gml:MeasureType"/&gt;
     *                  &lt;element minOccurs="0" name="elevation" type="gml:MeasureType"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:status"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:statusReference"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MOVINGOBJECTSTATUSTYPE_TYPE = build_MOVINGOBJECTSTATUSTYPE_TYPE();
    
    private static ComplexType build_MOVINGOBJECTSTATUSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GEOMETRYPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","position"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","locationName"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","locationReference"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                LOCATIONPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","location"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                MEASURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","speed"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTIONPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","bearing"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                MEASURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","acceleration"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                MEASURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","elevation"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","status"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","statusReference"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MovingObjectStatusType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTIMESLICETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractMemberType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;To create a collection of GML Objects that are not all features, a property type shall be derived by extension from gml:AbstractMemberType.
     *  This abstract property type is intended to be used only in object types where software shall be able to identify that an instance of such an object type is to be interpreted as a collection of objects.
     *  By default, this abstract property type does not imply any ownership of the objects in the collection. The owns attribute of gml:OwnershipAttributeGroup may be used on a property element instance to assert ownership of an object in the collection. A collection shall not own an object already owned by another object.
     *  &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTMEMBERTYPE_TYPE = build_ABSTRACTMEMBERTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTMEMBERTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractMemberType"), schema, false,
            true, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DictionaryEntryType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractMemberType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:Definition"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DICTIONARYENTRYTYPE_TYPE = build_DICTIONARYENTRYTYPE_TYPE();
    
    private static ComplexType build_DICTIONARYENTRYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DEFINITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Definition"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DictionaryEntryType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTMEMBERTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DefinitionProxyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:DefinitionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:definitionRef"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DEFINITIONPROXYTYPE_TYPE = build_DEFINITIONPROXYTYPE_TYPE();
    
    private static ComplexType build_DEFINITIONPROXYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","definitionRef"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DefinitionProxyType"), schema, false,
            false, Collections.<Filter>emptyList(), DEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="IndirectEntryType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:DefinitionProxy"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType INDIRECTENTRYTYPE_TYPE = build_INDIRECTENTRYTYPE_TYPE();
    
    private static ComplexType build_INDIRECTENTRYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DEFINITIONPROXYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","DefinitionProxy"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","IndirectEntryType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DictionaryType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:DefinitionType"&gt;
     *              &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
     *                  &lt;element ref="gml:dictionaryEntry"/&gt;
     *                  &lt;element ref="gml:indirectEntry"/&gt;
     *              &lt;/choice&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DICTIONARYTYPE_TYPE = build_DICTIONARYTYPE_TYPE();
    
    private static ComplexType build_DICTIONARYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DICTIONARYENTRYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","dictionaryEntry"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                INDIRECTENTRYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","indirectEntry"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DictionaryType"), schema, false,
            false, Collections.<Filter>emptyList(), DEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractSurfacePatchType"/&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTSURFACEPATCHTYPE_TYPE = build_ABSTRACTSURFACEPATCHTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTSURFACEPATCHTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractSurfacePatchType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AbstractRingPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property with the content model of gml:AbstractRingPropertyType encapsulates a ring to represent the surface boundary property of a surface.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:AbstractRing"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTRINGPROPERTYTYPE_TYPE = build_ABSTRACTRINGPROPERTYTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTRINGPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTRINGTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractRing"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractRingPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="SurfaceInterpolationType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:SurfaceInterpolationType is a list of codes that may be used to identify the interpolation mechanisms specified by an application schema.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="none"/&gt;
     *          &lt;enumeration value="planar"/&gt;
     *          &lt;enumeration value="spherical"/&gt;
     *          &lt;enumeration value="elliptical"/&gt;
     *          &lt;enumeration value="conic"/&gt;
     *          &lt;enumeration value="tin"/&gt;
     *          &lt;enumeration value="parametricCurve"/&gt;
     *          &lt;enumeration value="polynomialSpline"/&gt;
     *          &lt;enumeration value="rationalSpline"/&gt;
     *          &lt;enumeration value="triangulatedSpline"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType SURFACEINTERPOLATIONTYPE_TYPE = build_SURFACEINTERPOLATIONTYPE_TYPE();
     
    private static AttributeType build_SURFACEINTERPOLATIONTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SurfaceInterpolationType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="RectangleType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractSurfacePatchType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:exterior"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="planar" name="interpolation" type="gml:SurfaceInterpolationType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType RECTANGLETYPE_TYPE = build_RECTANGLETYPE_TYPE();
    
    private static ComplexType build_RECTANGLETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTRINGPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","exterior"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SURFACEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","RectangleType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTSURFACEPATCHTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="NodeOrEdgePropertyType"&gt;
     *      &lt;choice minOccurs="0"&gt;
     *          &lt;element ref="gml:Node"/&gt;
     *          &lt;element ref="gml:Edge"/&gt;
     *      &lt;/choice&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType NODEOREDGEPROPERTYTYPE_TYPE = build_NODEOREDGEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_NODEOREDGEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                NODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Node"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                EDGETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Edge"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","NodeOrEdgePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ObliqueCartesianCSType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OBLIQUECARTESIANCSTYPE_TYPE = build_OBLIQUECARTESIANCSTYPE_TYPE();
    
    private static ComplexType build_OBLIQUECARTESIANCSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ObliqueCartesianCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ObliqueCartesianCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:ObliqueCartesianCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OBLIQUECARTESIANCSPROPERTYTYPE_TYPE = build_OBLIQUECARTESIANCSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_OBLIQUECARTESIANCSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                OBLIQUECARTESIANCSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ObliqueCartesianCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ObliqueCartesianCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ImageDatumType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractDatumType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:pixelInCell"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType IMAGEDATUMTYPE_TYPE = build_IMAGEDATUMTYPE_TYPE();
    
    private static ComplexType build_IMAGEDATUMTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CODEWITHAUTHORITYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pixelInCell"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ImageDatumType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ImageDatumPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:ImageDatumPropertyType is a property type for association roles to an image datum, either referencing or containing the definition of that datum.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:ImageDatum"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType IMAGEDATUMPROPERTYTYPE_TYPE = build_IMAGEDATUMPROPERTYTYPE_TYPE();
    
    private static ComplexType build_IMAGEDATUMPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                IMAGEDATUMTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ImageDatum"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ImageDatumPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ImageCRSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:cartesianCS"/&gt;
     *                      &lt;element ref="gml:affineCS"/&gt;
     *                      &lt;element ref="gml:usesObliqueCartesianCS"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element ref="gml:imageDatum"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType IMAGECRSTYPE_TYPE = build_IMAGECRSTYPE_TYPE();
    
    private static ComplexType build_IMAGECRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CARTESIANCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","cartesianCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AFFINECSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","affineCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                OBLIQUECARTESIANCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","usesObliqueCartesianCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                IMAGEDATUMPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","imageDatum"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ImageCRSType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ImageCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:ImageCRSPropertyType is a property type for association roles to an image coordinate reference system, either referencing or containing the definition of that reference system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:ImageCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType IMAGECRSPROPERTYTYPE_TYPE = build_IMAGECRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_IMAGECRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                IMAGECRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ImageCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ImageCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="OrientableSurfaceType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractSurfaceType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:baseSurface"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute default="+" name="orientation" type="gml:SignType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ORIENTABLESURFACETYPE_TYPE = build_ORIENTABLESURFACETYPE_TYPE();
    
    private static ComplexType build_ORIENTABLESURFACETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SURFACEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","baseSurface"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SIGNTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","orientation"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","OrientableSurfaceType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTSURFACETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoPrimitiveMemberType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractTopoPrimitive"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOPRIMITIVEMEMBERTYPE_TYPE = build_TOPOPRIMITIVEMEMBERTYPE_TYPE();
    
    private static ComplexType build_TOPOPRIMITIVEMEMBERTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTTOPOPRIMITIVETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractTopoPrimitive"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoPrimitiveMemberType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoPrimitiveArrayAssociationType"&gt;
     *      &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractTopoPrimitive"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOPRIMITIVEARRAYASSOCIATIONTYPE_TYPE = build_TOPOPRIMITIVEARRAYASSOCIATIONTYPE_TYPE();
    
    private static ComplexType build_TOPOPRIMITIVEARRAYASSOCIATIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTTOPOPRIMITIVETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractTopoPrimitive"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoPrimitiveArrayAssociationType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoComplexType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTopologyType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;!--element ref="gml:maximalComplex"/&gt;
     *            &lt;element ref="gml:superComplex" minOccurs="0" maxOccurs="unbounded"/&gt;
     *            &lt;element ref="gml:subComplex" minOccurs="0" maxOccurs="unbounded"/--&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:topoPrimitiveMember"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:topoPrimitiveMembers"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute default="false" name="isMaximal" type="boolean"/&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOCOMPLEXTYPE_TYPE = build_TOPOCOMPLEXTYPE_TYPE();
    
    private static ComplexType build_TOPOCOMPLEXTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TOPOPRIMITIVEMEMBERTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","topoPrimitiveMember"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TOPOPRIMITIVEARRAYASSOCIATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","topoPrimitiveMembers"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","isMaximal"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoComplexType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTOPOLOGYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoComplexPropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TopoComplex"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOCOMPLEXPROPERTYTYPE_TYPE = build_TOPOCOMPLEXPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TOPOCOMPLEXPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TOPOCOMPLEXTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TopoComplex"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoComplexPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="QNameList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A type for a list of values of the respective simple type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="QName"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType QNAMELIST_TYPE = build_QNAMELIST_TYPE();
     
    private static AttributeType build_QNAMELIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","QNameList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ArrayAssociationType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:AbstractObject"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ARRAYASSOCIATIONTYPE_TYPE = build_ARRAYASSOCIATIONTYPE_TYPE();
    
    private static ComplexType build_ARRAYASSOCIATIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractObject"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ArrayAssociationType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="BagType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGMLType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:member"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:members"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType BAGTYPE_TYPE = build_BAGTYPE_TYPE();
    
    private static ComplexType build_BAGTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ASSOCIATIONROLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","member"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ARRAYASSOCIATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","members"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","BagType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGMLTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="NameOrNilReasonList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A type for a list of values of the respective simple type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="gml:NameOrNilReason"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NAMEORNILREASONLIST_TYPE = build_NAMEORNILREASONLIST_TYPE();
     
    private static AttributeType build_NAMEORNILREASONLIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","NameOrNilReasonList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CodeOrNilReasonListType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CodeOrNilReasonListType provides for lists of terms. The values in an instance element shall all be valid according to the rules of the dictionary, classification scheme, or authority identified by the value of its codeSpace attribute. An instance element may also include embedded values from NilReasonType. It is intended to be used in situations where a term or classification is expected, but the value may be absent for some reason.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:NameOrNilReasonList"&gt;
     *              &lt;attribute name="codeSpace" type="anyURI"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODEORNILREASONLISTTYPE_TYPE = build_CODEORNILREASONLISTTYPE_TYPE();
    
    private static ComplexType build_CODEORNILREASONLISTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","codeSpace"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CodeOrNilReasonListType"), schema, false,
            false, Collections.<Filter>emptyList(), NAMEORNILREASONLIST_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CategoryExtentType"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;restriction base="gml:CodeOrNilReasonListType"&gt;
     *              &lt;length value="2"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CATEGORYEXTENTTYPE_TYPE = build_CATEGORYEXTENTTYPE_TYPE();
    
    private static ComplexType build_CATEGORYEXTENTTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CategoryExtentType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), CODEORNILREASONLISTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="DegreeValueType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="nonNegativeInteger"&gt;
     *          &lt;maxInclusive value="359"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DEGREEVALUETYPE_TYPE = build_DEGREEVALUETYPE_TYPE();
     
    private static AttributeType build_DEGREEVALUETYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DegreeValueType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.NONNEGATIVEINTEGER_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DegreesType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:DegreeValueType"&gt;
     *              &lt;attribute name="direction"&gt;
     *                  &lt;simpleType&gt;
     *                      &lt;restriction base="string"&gt;
     *                          &lt;enumeration value="N"/&gt;
     *                          &lt;enumeration value="E"/&gt;
     *                          &lt;enumeration value="S"/&gt;
     *                          &lt;enumeration value="W"/&gt;
     *                          &lt;enumeration value="+"/&gt;
     *                          &lt;enumeration value="-"/&gt;
     *                      &lt;/restriction&gt;
     *                  &lt;/simpleType&gt;
     *              &lt;/attribute&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DEGREESTYPE_TYPE = build_DEGREESTYPE_TYPE();
    
    private static ComplexType build_DEGREESTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DegreesType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), DEGREEVALUETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="DecimalMinutesType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="decimal"&gt;
     *          &lt;minInclusive value="0.00"/&gt;
     *          &lt;maxExclusive value="60.00"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DECIMALMINUTESTYPE_TYPE = build_DECIMALMINUTESTYPE_TYPE();
     
    private static AttributeType build_DECIMALMINUTESTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DecimalMinutesType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.DECIMAL_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="ArcMinutesType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="nonNegativeInteger"&gt;
     *          &lt;maxInclusive value="59"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ARCMINUTESTYPE_TYPE = build_ARCMINUTESTYPE_TYPE();
     
    private static AttributeType build_ARCMINUTESTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ArcMinutesType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.NONNEGATIVEINTEGER_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="ArcSecondsType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="decimal"&gt;
     *          &lt;minInclusive value="0.00"/&gt;
     *          &lt;maxExclusive value="60.00"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ARCSECONDSTYPE_TYPE = build_ARCSECONDSTYPE_TYPE();
     
    private static AttributeType build_ARCSECONDSTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ArcSecondsType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.DECIMAL_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DMSAngleType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:degrees"/&gt;
     *          &lt;choice minOccurs="0"&gt;
     *              &lt;element ref="gml:decimalMinutes"/&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:minutes"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:seconds"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/choice&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DMSANGLETYPE_TYPE = build_DMSANGLETYPE_TYPE();
    
    private static ComplexType build_DMSANGLETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DEGREESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","degrees"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DECIMALMINUTESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","decimalMinutes"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ARCMINUTESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","minutes"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ARCSECONDSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","seconds"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DMSAngleType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AngleChoiceType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;choice&gt;
     *          &lt;element ref="gml:angle"/&gt;
     *          &lt;element ref="gml:dmsAngle"/&gt;
     *      &lt;/choice&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ANGLECHOICETYPE_TYPE = build_ANGLECHOICETYPE_TYPE();
    
    private static ComplexType build_ANGLECHOICETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ANGLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","angle"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DMSANGLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","dmsAngle"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AngleChoiceType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AffinePlacementType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element name="location" type="gml:DirectPositionType"/&gt;
     *          &lt;element maxOccurs="unbounded" name="refDirection" type="gml:VectorType"/&gt;
     *          &lt;element name="inDimension" type="positiveInteger"/&gt;
     *          &lt;element name="outDimension" type="positiveInteger"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType AFFINEPLACEMENTTYPE_TYPE = build_AFFINEPLACEMENTTYPE_TYPE();
    
    private static ComplexType build_AFFINEPLACEMENTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","location"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                VECTORTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","refDirection"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","inDimension"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","outDimension"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AffinePlacementType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ClothoidType_refLocation"&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:AffinePlacement"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CLOTHOIDTYPE_REFLOCATION_TYPE = build_CLOTHOIDTYPE_REFLOCATION_TYPE();
    
    private static ComplexType build_CLOTHOIDTYPE_REFLOCATION_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                AFFINEPLACEMENTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AffinePlacement"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ClothoidType_refLocation"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ClothoidType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveSegmentType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element name="refLocation"&gt;
     *                      &lt;complexType name="ClothoidType_refLocation"&gt;
     *                          &lt;sequence&gt;
     *                              &lt;element ref="gml:AffinePlacement"/&gt;
     *                          &lt;/sequence&gt;
     *                      &lt;/complexType&gt;
     *                  &lt;/element&gt;
     *                  &lt;element name="scaleFactor" type="decimal"/&gt;
     *                  &lt;element name="startParameter" type="double"/&gt;
     *                  &lt;element name="endParameter" type="double"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="clothoid" name="interpolation" type="gml:CurveInterpolationType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CLOTHOIDTYPE_TYPE = build_CLOTHOIDTYPE_TYPE();
    
    private static ComplexType build_CLOTHOIDTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CLOTHOIDTYPE_REFLOCATION_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","refLocation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DECIMAL_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","scaleFactor"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","startParameter"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","endParameter"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ClothoidType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVESEGMENTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="OrientableCurveType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:baseCurve"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute default="+" name="orientation" type="gml:SignType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ORIENTABLECURVETYPE_TYPE = build_ORIENTABLECURVETYPE_TYPE();
    
    private static ComplexType build_ORIENTABLECURVETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CURVEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","baseCurve"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SIGNTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","orientation"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","OrientableCurveType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractParametricCurveSurfaceType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractSurfacePatchType"&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTPARAMETRICCURVESURFACETYPE_TYPE = build_ABSTRACTPARAMETRICCURVESURFACETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTPARAMETRICCURVESURFACETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractParametricCurveSurfaceType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTSURFACEPATCHTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PointGrid_rows_RowType"&gt;
     *      &lt;group ref="gml:geometricPositionListGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType POINTGRID_ROWS_ROWTYPE_TYPE = build_POINTGRID_ROWS_ROWTYPE_TYPE();
    
    private static ComplexType build_POINTGRID_ROWS_ROWTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PointGrid_rows_RowType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AbstractGriddedSurfaceType_rows"&gt;
     *      &lt;sequence&gt;
     *          &lt;element maxOccurs="unbounded" name="Row"&gt;
     *              &lt;complexType name="PointGrid_rows_RowType"&gt;
     *                  &lt;group ref="gml:geometricPositionListGroup"/&gt;
     *              &lt;/complexType&gt;
     *          &lt;/element&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGRIDDEDSURFACETYPE_ROWS_TYPE = build_ABSTRACTGRIDDEDSURFACETYPE_ROWS_TYPE();
    
    private static ComplexType build_ABSTRACTGRIDDEDSURFACETYPE_ROWS_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                POINTGRID_ROWS_ROWTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Row"), 1, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGriddedSurfaceType_rows"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGriddedSurfaceType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractParametricCurveSurfaceType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;group ref="gml:PointGrid"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute name="rows" type="integer"/&gt;
     *              &lt;attribute name="columns" type="integer"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGRIDDEDSURFACETYPE_TYPE = build_ABSTRACTGRIDDEDSURFACETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGRIDDEDSURFACETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGRIDDEDSURFACETYPE_ROWS_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","rows"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","rows"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","columns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGriddedSurfaceType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTPARAMETRICCURVESURFACETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CylinderType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGriddedSurfaceType"&gt;
     *              &lt;attribute fixed="circularArc3Points"
     *                  name="horizontalCurveType" type="gml:CurveInterpolationType"/&gt;
     *              &lt;attribute fixed="linear" name="verticalCurveType" type="gml:CurveInterpolationType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CYLINDERTYPE_TYPE = build_CYLINDERTYPE_TYPE();
    
    private static ComplexType build_CYLINDERTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","horizontalCurveType"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","verticalCurveType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CylinderType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGRIDDEDSURFACETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType mixed="true" name="GenericMetaDataType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent mixed="true"&gt;
     *          &lt;extension base="gml:AbstractMetaDataType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;any maxOccurs="unbounded" minOccurs="0" processContents="lax"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GENERICMETADATATYPE_TYPE = build_GENERICMETADATATYPE_TYPE();
    
    private static ComplexType build_GENERICMETADATATYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GenericMetaDataType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTMETADATATYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="InlinePropertyType"&gt;
     *      &lt;sequence&gt;
     *          &lt;any namespace="##any"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType INLINEPROPERTYTYPE_TYPE = build_INLINEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_INLINEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","InlinePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="CalDate"&gt;
     *      &lt;union memberTypes="date gYearMonth gYear"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType CALDATE_TYPE = build_CALDATE_TYPE();
     
    private static AttributeType build_CALDATE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CalDate"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeCalendarEraType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:DefinitionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element name="referenceEvent" type="gml:StringOrRefType"/&gt;
     *                  &lt;element name="referenceDate" type="gml:CalDate"/&gt;
     *                  &lt;element name="julianReference" type="decimal"/&gt;
     *                  &lt;element name="epochOfUse" type="gml:TimePeriodPropertyType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMECALENDARERATYPE_TYPE = build_TIMECALENDARERATYPE_TYPE();
    
    private static ComplexType build_TIMECALENDARERATYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","referenceEvent"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CALDATE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","referenceDate"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DECIMAL_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","julianReference"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPERIODPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","epochOfUse"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeCalendarEraType"), schema, false,
            false, Collections.<Filter>emptyList(), DEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeCalendarEraPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimeCalendarEraPropertyType provides for associating a gml:TimeCalendarEra with an object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TimeCalendarEra"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMECALENDARERAPROPERTYTYPE_TYPE = build_TIMECALENDARERAPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMECALENDARERAPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMECALENDARERATYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TimeCalendarEra"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeCalendarEraPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeCalendarType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:TimeReferenceSystemType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" name="referenceFrame" type="gml:TimeCalendarEraPropertyType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMECALENDARTYPE_TYPE = build_TIMECALENDARTYPE_TYPE();
    
    private static ComplexType build_TIMECALENDARTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMECALENDARERAPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","referenceFrame"), 1, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeCalendarType"), schema, false,
            false, Collections.<Filter>emptyList(), TIMEREFERENCESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeCalendarPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimeCalendarPropertyType provides for associating a gml:TimeCalendar with an object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TimeCalendar"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMECALENDARPROPERTYTYPE_TYPE = build_TIMECALENDARPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMECALENDARPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMECALENDARTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TimeCalendar"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeCalendarPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType final="#all" name="TimeClockType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:TimeReferenceSystemType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element name="referenceEvent" type="gml:StringOrRefType"/&gt;
     *                  &lt;element name="referenceTime" type="time"/&gt;
     *                  &lt;element name="utcReference" type="time"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0"
     *                      name="dateBasis" type="gml:TimeCalendarPropertyType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMECLOCKTYPE_TYPE = build_TIMECLOCKTYPE_TYPE();
    
    private static ComplexType build_TIMECLOCKTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","referenceEvent"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.TIME_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","referenceTime"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.TIME_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","utcReference"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMECALENDARPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","dateBasis"), 0, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeClockType"), schema, false,
            false, Collections.<Filter>emptyList(), TIMEREFERENCESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeClockPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimeClockPropertyType provides for associating a gml:TimeClock with an object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TimeClock"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMECLOCKPROPERTYTYPE_TYPE = build_TIMECLOCKPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMECLOCKPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMECLOCKTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TimeClock"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeClockPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PolygonPatchType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractSurfacePatchType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:exterior"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:interior"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="planar" name="interpolation" type="gml:SurfaceInterpolationType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType POLYGONPATCHTYPE_TYPE = build_POLYGONPATCHTYPE_TYPE();
    
    private static ComplexType build_POLYGONPATCHTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTRINGPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","exterior"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTRINGPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interior"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SURFACEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PolygonPatchType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTSURFACEPATCHTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PolygonType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractSurfaceType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:exterior"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:interior"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType POLYGONTYPE_TYPE = build_POLYGONTYPE_TYPE();
     
    private static AttributeType build_POLYGONTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PolygonType"), com.vividsolutions.jts.geom.Polygon.class, false,
            false, Collections.<Filter>emptyList(), ABSTRACTSURFACETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ValuePropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;group ref="gml:Value"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VALUEPROPERTYTYPE_TYPE = build_VALUEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_VALUEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractValue"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGEOMETRYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometry"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTTIMEOBJECTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeObject"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Null"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ValuePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="UomSymbol"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;This type specifies a character string of length at least one, and restricted such that it must not contain any of the following characters: ":" (colon), " " (space), (newline), (carriage return), (tab). This allows values corresponding to familiar abbreviations, such as "kg", "m/s", etc. 
     *  It is recommended that the symbol be an identifier for a unit of measure as specified in the "Unified Code of Units of Measure" (UCUM) (http://aurora.regenstrief.org/UCUM). This provides a set of symbols and a grammar for constructing identifiers for units of measure that are unique, and may be easily entered with a keyboard supporting the limited character set known as 7-bit ASCII. ISO 2955 formerly provided a specification with this scope, but was withdrawn in 2001. UCUM largely follows ISO 2955 with modifications to remove ambiguities and other problems.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;pattern value="[^: \n\r\t]+"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType UOMSYMBOL_TYPE = build_UOMSYMBOL_TYPE();
     
    private static AttributeType build_UOMSYMBOL_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","UomSymbol"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="UnitDefinitionType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:DefinitionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:quantityType"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:quantityTypeReference"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:catalogSymbol"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType UNITDEFINITIONTYPE_TYPE = build_UNITDEFINITIONTYPE_TYPE();
    
    private static ComplexType build_UNITDEFINITIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","quantityType"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","quantityTypeReference"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","catalogSymbol"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinitionType"), schema, false,
            false, Collections.<Filter>emptyList(), DEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="BaseUnitType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:UnitDefinitionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element name="unitsSystem" type="gml:ReferenceType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType BASEUNITTYPE_TYPE = build_BASEUNITTYPE_TYPE();
    
    private static ComplexType build_BASEUNITTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","unitsSystem"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","BaseUnitType"), schema, false,
            false, Collections.<Filter>emptyList(), UNITDEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiGeometryPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a geometric aggregate as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractGeometricAggregate"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTIGEOMETRYPROPERTYTYPE_TYPE = build_MULTIGEOMETRYPROPERTYTYPE_TYPE();
     
    private static AttributeType build_MULTIGEOMETRYPROPERTYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MultiGeometryPropertyType"), com.vividsolutions.jts.geom.GeometryCollection.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SurfacePatchArrayPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:SurfacePatchArrayPropertyType is a container for a sequence of surface patches.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractSurfacePatch"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SURFACEPATCHARRAYPROPERTYTYPE_TYPE = build_SURFACEPATCHARRAYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_SURFACEPATCHARRAYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTSURFACEPATCHTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractSurfacePatch"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SurfacePatchArrayPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SurfaceType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractSurfaceType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:patches"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType SURFACETYPE_TYPE = build_SURFACETYPE_TYPE();
     
    private static AttributeType build_SURFACETYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SurfaceType"), com.vividsolutions.jts.geom.Polygon.class, false,
            false, Collections.<Filter>emptyList(), ABSTRACTSURFACETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LineStringSegmentType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveSegmentType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice maxOccurs="unbounded" minOccurs="2"&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="linear" name="interpolation" type="gml:CurveInterpolationType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType LINESTRINGSEGMENTTYPE_TYPE = build_LINESTRINGSEGMENTTYPE_TYPE();
    
    private static ComplexType build_LINESTRINGSEGMENTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointRep"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","LineStringSegmentType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVESEGMENTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LineStringSegmentArrayPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:LineStringSegmentArrayPropertyType provides a container for line strings.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
     *          &lt;element ref="gml:LineStringSegment"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType LINESTRINGSEGMENTARRAYPROPERTYTYPE_TYPE = build_LINESTRINGSEGMENTARRAYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_LINESTRINGSEGMENTARRAYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                LINESTRINGSEGMENTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","LineStringSegment"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","LineStringSegmentArrayPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TinType_controlPoint"&gt;
     *      &lt;choice&gt;
     *          &lt;element ref="gml:posList"/&gt;
     *          &lt;group maxOccurs="unbounded" minOccurs="3" ref="gml:geometricPositionGroup"/&gt;
     *      &lt;/choice&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TINTYPE_CONTROLPOINT_TYPE = build_TINTYPE_CONTROLPOINT_TYPE();
    
    private static ComplexType build_TINTYPE_CONTROLPOINT_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TinType_controlPoint"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TinType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:SurfaceType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0"
     *                      name="stopLines" type="gml:LineStringSegmentArrayPropertyType"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0"
     *                      name="breakLines" type="gml:LineStringSegmentArrayPropertyType"/&gt;
     *                  &lt;element name="maxLength" type="gml:LengthType"/&gt;
     *                  &lt;element name="controlPoint"&gt;
     *                      &lt;complexType name="TinType_controlPoint"&gt;
     *                          &lt;choice&gt;
     *                              &lt;element ref="gml:posList"/&gt;
     *                              &lt;group maxOccurs="unbounded" minOccurs="3" ref="gml:geometricPositionGroup"/&gt;
     *                          &lt;/choice&gt;
     *                      &lt;/complexType&gt;
     *                  &lt;/element&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TINTYPE_TYPE = build_TINTYPE_TYPE();
    
    private static ComplexType build_TINTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                LINESTRINGSEGMENTARRAYPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","stopLines"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                LINESTRINGSEGMENTARRAYPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","breakLines"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                LENGTHTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","maxLength"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TINTYPE_CONTROLPOINT_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","controlPoint"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TinType"), schema, false,
            false, Collections.<Filter>emptyList(), SURFACETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeodesicStringType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveSegmentType"&gt;
     *              &lt;choice&gt;
     *                  &lt;element ref="gml:posList"/&gt;
     *                  &lt;group maxOccurs="unbounded" minOccurs="2" ref="gml:geometricPositionGroup"/&gt;
     *              &lt;/choice&gt;
     *              &lt;attribute fixed="geodesic" name="interpolation" type="gml:CurveInterpolationType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEODESICSTRINGTYPE_TYPE = build_GEODESICSTRINGTYPE_TYPE();
    
    private static ComplexType build_GEODESICSTRINGTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeodesicStringType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVESEGMENTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeodesicType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:GeodesicStringType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEODESICTYPE_TYPE = build_GEODESICTYPE_TYPE();
    
    private static ComplexType build_GEODESICTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeodesicType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GEODESICSTRINGTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="UnitOfMeasureType"&gt;
     *      &lt;sequence/&gt;
     *      &lt;attribute name="uom" type="gml:UomIdentifier" use="required"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType UNITOFMEASURETYPE_TYPE = build_UNITOFMEASURETYPE_TYPE();
    
    private static ComplexType build_UNITOFMEASURETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                UOMIDENTIFIER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","uom"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","UnitOfMeasureType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="FormulaType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element minOccurs="0" name="a" type="double"/&gt;
     *          &lt;element name="b" type="double"/&gt;
     *          &lt;element name="c" type="double"/&gt;
     *          &lt;element minOccurs="0" name="d" type="double"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType FORMULATYPE_TYPE = build_FORMULATYPE_TYPE();
    
    private static ComplexType build_FORMULATYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","a"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","b"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","c"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","d"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","FormulaType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ConversionToPreferredUnitType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;The inherited attribute uom references the preferred unit that this conversion applies to. The conversion of a unit to the preferred unit for this physical quantity type is specified by an arithmetic conversion (scaling and/or offset). The content model extends gml:UnitOfMeasureType, which has a mandatory attribute uom which identifies the preferred unit for the physical quantity type that this conversion applies to. The conversion is specified by a choice of 
     *  -	gml:factor, which defines the scale factor, or
     *  -	gml:formula, which defines a formula 
     *  by which a value using the conventional unit of measure can be converted to obtain the corresponding value using the preferred unit of measure.  
     *  The formula defines the parameters of a simple formula by which a value using the conventional unit of measure can be converted to the corresponding value using the preferred unit of measure. The formula element contains elements a, b, c and d, whose values use the XML Schema type double. These values are used in the formula y = (a + bx) / (c + dx), where x is a value using this unit, and y is the corresponding value using the base unit. The elements a and d are optional, and if values are not provided, those parameters are considered to be zero. If values are not provided for both a and d, the formula is equivalent to a fraction with numerator and denominator parameters.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:UnitOfMeasureType"&gt;
     *              &lt;choice&gt;
     *                  &lt;element name="factor" type="double"/&gt;
     *                  &lt;element name="formula" type="gml:FormulaType"/&gt;
     *              &lt;/choice&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CONVERSIONTOPREFERREDUNITTYPE_TYPE = build_CONVERSIONTOPREFERREDUNITTYPE_TYPE();
    
    private static ComplexType build_CONVERSIONTOPREFERREDUNITTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","factor"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                FORMULATYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","formula"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ConversionToPreferredUnitType"), schema, false,
            false, Collections.<Filter>emptyList(), UNITOFMEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DerivationUnitTermType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:UnitOfMeasureType"&gt;
     *              &lt;attribute name="exponent" type="integer"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DERIVATIONUNITTERMTYPE_TYPE = build_DERIVATIONUNITTERMTYPE_TYPE();
    
    private static ComplexType build_DERIVATIONUNITTERMTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","exponent"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DerivationUnitTermType"), schema, false,
            false, Collections.<Filter>emptyList(), UNITOFMEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ConventionalUnitType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:UnitDefinitionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:conversionToPreferredUnit"/&gt;
     *                      &lt;element ref="gml:roughConversionToPreferredUnit"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:derivationUnitTerm"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CONVENTIONALUNITTYPE_TYPE = build_CONVENTIONALUNITTYPE_TYPE();
    
    private static ComplexType build_CONVENTIONALUNITTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CONVERSIONTOPREFERREDUNITTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","conversionToPreferredUnit"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CONVERSIONTOPREFERREDUNITTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","roughConversionToPreferredUnit"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DERIVATIONUNITTERMTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","derivationUnitTerm"), 0, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ConventionalUnitType"), schema, false,
            false, Collections.<Filter>emptyList(), UNITDEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="booleanList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A type for a list of values of the respective simple type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="boolean"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType BOOLEANLIST_TYPE = build_BOOLEANLIST_TYPE();
     
    private static AttributeType build_BOOLEANLIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","booleanList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="UomURI"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;This type specifies a URI, restricted such that it must start with one of the following sequences: "#", "./", "../", or a string of characters followed by a ":". These patterns ensure that the most common URI forms are supported, including absolute and relative URIs and URIs that are simple fragment identifiers, but prohibits certain forms of relative URI that could be mistaken for unit of measure symbol . 
     *  NOTE	It is possible to re-write such a relative URI to conform to the restriction (e.g. "./m/s").
     *  In an instance document, on elements of type gml:MeasureType the mandatory uom attribute shall carry a value corresponding to either 
     *  -	a conventional unit of measure symbol,
     *  -	a link to a definition of a unit of measure that does not have a conventional symbol, or when it is desired to indicate a precise or variant definition.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="anyURI"&gt;
     *          &lt;pattern value="([a-zA-Z][a-zA-Z0-9\-\+\.]*:|\.\./|\./|#).*"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType UOMURI_TYPE = build_UOMURI_TYPE();
     
    private static AttributeType build_UOMURI_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","UomURI"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYURI_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="NameOrNilReason"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Extension to the respective XML Schema built-in simple type to allow a choice of either a value of the built-in simple type or a reason for a nil value.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;union memberTypes="gml:NilReasonEnumeration Name anyURI"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NAMEORNILREASON_TYPE = build_NAMEORNILREASON_TYPE();
     
    private static AttributeType build_NAMEORNILREASON_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","NameOrNilReason"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeCSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMECSTYPE_TYPE = build_TIMECSTYPE_TYPE();
    
    private static ComplexType build_TIMECSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimeCSPropertyType is a property type for association roles to a time coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TimeCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMECSPROPERTYTYPE_TYPE = build_TIMECSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMECSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMECSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TimeCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TemporalCSType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TEMPORALCSTYPE_TYPE = build_TEMPORALCSTYPE_TYPE();
    
    private static ComplexType build_TEMPORALCSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TemporalCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TemporalCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TemporalCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TEMPORALCSPROPERTYTYPE_TYPE = build_TEMPORALCSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TEMPORALCSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TEMPORALCSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TemporalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TemporalCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="TemporalDatumBaseType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;The TemporalDatumBaseType partially defines the origin of a temporal coordinate reference system. This type restricts the AbstractDatumType to remove the "anchorDefinition" and "realizationEpoch" elements.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:AbstractDatumType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:metaDataProperty"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:description"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:descriptionReference"/&gt;
     *                  &lt;element ref="gml:identifier"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:name"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:remarks"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:domainOfValidity"/&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:scope"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute ref="gml:id" use="required"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TEMPORALDATUMBASETYPE_TYPE = build_TEMPORALDATUMBASETYPE_TYPE();
    
    private static ComplexType build_TEMPORALDATUMBASETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                METADATAPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","metaDataProperty"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","description"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","descriptionReference"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODEWITHAUTHORITYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","identifier"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","name"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remarks"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                _DOMAINOFVALIDITY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","domainOfValidity"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","scope"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ID_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","id"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TemporalDatumBaseType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TemporalDatumType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:TemporalDatumBaseType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:origin"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TEMPORALDATUMTYPE_TYPE = build_TEMPORALDATUMTYPE_TYPE();
    
    private static ComplexType build_TEMPORALDATUMTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DATETIME_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","origin"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TemporalDatumType"), schema, false,
            false, Collections.<Filter>emptyList(), TEMPORALDATUMBASETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TemporalDatumPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TemporalDatumPropertyType is a property type for association roles to a temporal datum, either referencing or containing the definition of that datum.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TemporalDatum"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TEMPORALDATUMPROPERTYTYPE_TYPE = build_TEMPORALDATUMPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TEMPORALDATUMPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TEMPORALDATUMTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TemporalDatum"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TemporalDatumPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TemporalCRSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:timeCS"/&gt;
     *                      &lt;element ref="gml:usesTemporalCS"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element ref="gml:temporalDatum"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TEMPORALCRSTYPE_TYPE = build_TEMPORALCRSTYPE_TYPE();
    
    private static ComplexType build_TEMPORALCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMECSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","timeCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TEMPORALCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","usesTemporalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TEMPORALDATUMPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","temporalDatum"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TemporalCRSType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TemporalCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TemporalCRSPropertyType is a property type for association roles to a temporal coordinate reference system, either referencing or containing the definition of that reference system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TemporalCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TEMPORALCRSPROPERTYTYPE_TYPE = build_TEMPORALCRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TEMPORALCRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TEMPORALCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TemporalCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TemporalCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SpeedType"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:MeasureType"/&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SPEEDTYPE_TYPE = build_SPEEDTYPE_TYPE();
    
    private static ComplexType build_SPEEDTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SpeedType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ArrayType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGMLType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:members"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ARRAYTYPE_TYPE = build_ARRAYTYPE_TYPE();
    
    private static ComplexType build_ARRAYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ARRAYASSOCIATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","members"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ArrayType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGMLTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MeasureListType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:MeasureListType provides for a list of quantities.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:doubleList"&gt;
     *              &lt;attribute name="uom" type="gml:UomIdentifier" use="required"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MEASURELISTTYPE_TYPE = build_MEASURELISTTYPE_TYPE();
    
    private static ComplexType build_MEASURELISTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                UOMIDENTIFIER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","uom"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MeasureListType"), schema, false,
            false, Collections.<Filter>emptyList(), DOUBLELIST_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="OperationParameterType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeneralOperationParameterType"&gt;
     *              &lt;sequence/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONPARAMETERTYPE_TYPE = build_OPERATIONPARAMETERTYPE_TYPE();
    
    private static ComplexType build_OPERATIONPARAMETERTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","OperationParameterType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTGENERALOPERATIONPARAMETERTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="OperationParameterPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:OperationParameterPropertyType is a property type for association roles to an operation parameter, either referencing or containing the definition of that parameter.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:OperationParameter"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONPARAMETERPROPERTYTYPE_TYPE = build_OPERATIONPARAMETERPROPERTYTYPE_TYPE();
    
    private static ComplexType build_OPERATIONPARAMETERPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                OPERATIONPARAMETERTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","OperationParameter"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","OperationParameterPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ParameterValueType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeneralParameterValueType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:value"/&gt;
     *                      &lt;element ref="gml:dmsAngleValue"/&gt;
     *                      &lt;element ref="gml:stringValue"/&gt;
     *                      &lt;element ref="gml:integerValue"/&gt;
     *                      &lt;element ref="gml:booleanValue"/&gt;
     *                      &lt;element ref="gml:valueList"/&gt;
     *                      &lt;element ref="gml:integerValueList"/&gt;
     *                      &lt;element ref="gml:valueFile"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element ref="gml:operationParameter"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PARAMETERVALUETYPE_TYPE = build_PARAMETERVALUETYPE_TYPE();
    
    private static ComplexType build_PARAMETERVALUETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                MEASURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","value"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DMSANGLETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","dmsAngleValue"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","stringValue"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","integerValue"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","booleanValue"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                MEASURELISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","valueList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                INTEGERLIST_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","integerValueList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","valueFile"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                OPERATIONPARAMETERPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","operationParameter"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ParameterValueType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGENERALPARAMETERVALUETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="KnotType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element name="value" type="double"/&gt;
     *          &lt;element name="multiplicity" type="nonNegativeInteger"/&gt;
     *          &lt;element name="weight" type="double"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType KNOTTYPE_TYPE = build_KNOTTYPE_TYPE();
    
    private static ComplexType build_KNOTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","value"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.NONNEGATIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","multiplicity"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.DOUBLE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","weight"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","KnotType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="KnotPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:KnotPropertyType encapsulates a knot to use it in a geometric type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element name="Knot" type="gml:KnotType"&gt;
     *              &lt;annotation&gt;
     *                  &lt;documentation&gt;A knot is a breakpoint on a piecewise spline curve.
     *  value is the value of the parameter at the knot of the spline (see ISO 19107:2003, 6.4.24.2).
     *  multiplicity is the multiplicity of this knot used in the definition of the spline (with the same weight).
     *  weight is the value of the averaging weight used for this knot of the spline.&lt;/documentation&gt;
     *              &lt;/annotation&gt;
     *          &lt;/element&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType KNOTPROPERTYTYPE_TYPE = build_KNOTPROPERTYTYPE_TYPE();
    
    private static ComplexType build_KNOTPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                KNOTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Knot"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","KnotPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="KnotTypesType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;This enumeration type specifies values for the knots' type (see ISO 19107:2003, 6.4.25).&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="uniform"/&gt;
     *          &lt;enumeration value="quasiUniform"/&gt;
     *          &lt;enumeration value="piecewiseBezier"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType KNOTTYPESTYPE_TYPE = build_KNOTTYPESTYPE_TYPE();
     
    private static AttributeType build_KNOTTYPESTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","KnotTypesType"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="BSplineType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveSegmentType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element name="degree" type="nonNegativeInteger"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="2" name="knot" type="gml:KnotPropertyType"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute default="polynomialSpline" name="interpolation" type="gml:CurveInterpolationType"/&gt;
     *              &lt;attribute name="isPolynomial" type="boolean"/&gt;
     *              &lt;attribute name="knotType" type="gml:KnotTypesType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType BSPLINETYPE_TYPE = build_BSPLINETYPE_TYPE();
    
    private static ComplexType build_BSPLINETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointRep"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.NONNEGATIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","degree"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                KNOTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","knot"), 2, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","isPolynomial"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                KNOTTYPESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","knotType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","BSplineType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVESEGMENTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="BezierType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:BSplineType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element name="degree" type="nonNegativeInteger"/&gt;
     *                  &lt;element maxOccurs="2" minOccurs="2" name="knot" type="gml:KnotPropertyType"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="polynomialSpline" name="interpolation" type="gml:CurveInterpolationType"/&gt;
     *              &lt;attribute fixed="true" name="isPolynomial" type="boolean"/&gt;
     *              &lt;attribute name="knotType" type="gml:KnotTypesType" use="prohibited"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType BEZIERTYPE_TYPE = build_BEZIERTYPE_TYPE();
    
    private static ComplexType build_BEZIERTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointRep"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.NONNEGATIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","degree"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                KNOTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","knot"), 2, 2, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","isPolynomial"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                KNOTTYPESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","knotType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","BezierType"), schema, false,
            false, Collections.<Filter>emptyList(), BSPLINETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="integerOrNilReason"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Extension to the respective XML Schema built-in simple type to allow a choice of either a value of the built-in simple type or a reason for a nil value.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;union memberTypes="gml:NilReasonEnumeration integer anyURI"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType INTEGERORNILREASON_TYPE = build_INTEGERORNILREASON_TYPE();
     
    private static AttributeType build_INTEGERORNILREASON_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","integerOrNilReason"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeCoordinateSystemType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:TimeReferenceSystemType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element name="originPosition" type="gml:TimePositionType"/&gt;
     *                      &lt;element name="origin" type="gml:TimeInstantPropertyType"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element name="interval" type="gml:TimeIntervalLengthType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMECOORDINATESYSTEMTYPE_TYPE = build_TIMECOORDINATESYSTEMTYPE_TYPE();
    
    private static ComplexType build_TIMECOORDINATESYSTEMTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","originPosition"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEINSTANTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","origin"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEINTERVALLENGTHTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interval"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeCoordinateSystemType"), schema, false,
            false, Collections.<Filter>emptyList(), TIMEREFERENCESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoCurveType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTopologyType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:directedEdge"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOCURVETYPE_TYPE = build_TOPOCURVETYPE_TYPE();
    
    private static ComplexType build_TOPOCURVETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTEDEDGEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","directedEdge"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoCurveType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTOPOLOGYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoCurvePropertyType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:TopoCurve"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOCURVEPROPERTYTYPE_TYPE = build_TOPOCURVEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TOPOCURVEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TOPOCURVETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TopoCurve"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoCurvePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PriorityLocationPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:LocationPropertyType"&gt;
     *              &lt;attribute name="priority" type="string"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PRIORITYLOCATIONPROPERTYTYPE_TYPE = build_PRIORITYLOCATIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_PRIORITYLOCATIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","priority"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","PriorityLocationPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), LOCATIONPROPERTYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ShellType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element maxOccurs="unbounded" ref="gml:surfaceMember"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SHELLTYPE_TYPE = build_SHELLTYPE_TYPE();
    
    private static ComplexType build_SHELLTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SURFACEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","surfaceMember"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ShellType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ShellPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property with the content model of gml:ShellPropertyType encapsulates a shell to represent a component of a solid boundary.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:Shell"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SHELLPROPERTYTYPE_TYPE = build_SHELLPROPERTYTYPE_TYPE();
    
    private static ComplexType build_SHELLPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SHELLTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Shell"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ShellPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SolidType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractSolidType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" name="exterior" type="gml:ShellPropertyType"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0"
     *                      name="interior" type="gml:ShellPropertyType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SOLIDTYPE_TYPE = build_SOLIDTYPE_TYPE();
    
    private static ComplexType build_SOLIDTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SHELLPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","exterior"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SHELLPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interior"), 0, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SolidType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTSOLIDTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGeneralTransformationType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:AbstractCoordinateOperationType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:metaDataProperty"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:description"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:descriptionReference"/&gt;
     *                  &lt;element ref="gml:identifier"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:name"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:remarks"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:domainOfValidity"/&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:scope"/&gt;
     *                  &lt;element ref="gml:operationVersion"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:coordinateOperationAccuracy"/&gt;
     *                  &lt;element ref="gml:sourceCRS"/&gt;
     *                  &lt;element ref="gml:targetCRS"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute ref="gml:id" use="required"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGENERALTRANSFORMATIONTYPE_TYPE = build_ABSTRACTGENERALTRANSFORMATIONTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGENERALTRANSFORMATIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                METADATAPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","metaDataProperty"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                STRINGORREFTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","description"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                REFERENCETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","descriptionReference"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODEWITHAUTHORITYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","identifier"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","name"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remarks"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                _DOMAINOFVALIDITY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","domainOfValidity"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","scope"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","operationVersion"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                _COORDINATEOPERATIONACCURACY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinateOperationAccuracy"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CRSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","sourceCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CRSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","targetCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ID_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","id"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralTransformationType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTCOORDINATEOPERATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TransformationType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeneralTransformationType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:method"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:parameterValue"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TRANSFORMATIONTYPE_TYPE = build_TRANSFORMATIONTYPE_TYPE();
    
    private static ComplexType build_TRANSFORMATIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                OPERATIONMETHODPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","method"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGENERALPARAMETERVALUEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","parameterValue"), 0, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TransformationType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGENERALTRANSFORMATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TransformationPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TransformationPropertyType is a property type for association roles to a transformation, either referencing or containing the definition of that transformation.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Transformation"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TRANSFORMATIONPROPERTYTYPE_TYPE = build_TRANSFORMATIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TRANSFORMATIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TRANSFORMATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Transformation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TransformationPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SphereType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGriddedSurfaceType"&gt;
     *              &lt;attribute fixed="circularArc3Points"
     *                  name="horizontalCurveType" type="gml:CurveInterpolationType"/&gt;
     *              &lt;attribute fixed="circularArc3Points"
     *                  name="verticalCurveType" type="gml:CurveInterpolationType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SPHERETYPE_TYPE = build_SPHERETYPE_TYPE();
    
    private static ComplexType build_SPHERETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","horizontalCurveType"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","verticalCurveType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SphereType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGRIDDEDSURFACETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CurveArrayPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A container for an array of curves. The elements are always contained in the array property, referencing geometry elements or arrays of geometry elements via XLinks is not supported.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractCurve"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CURVEARRAYPROPERTYTYPE_TYPE = build_CURVEARRAYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_CURVEARRAYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTCURVETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractCurve"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CurveArrayPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiCurveType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometricAggregateType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:curveMember"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:curveMembers"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTICURVETYPE_TYPE = build_MULTICURVETYPE_TYPE();
     
    private static AttributeType build_MULTICURVETYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MultiCurveType"), com.vividsolutions.jts.geom.GeometryCollection.class, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGEOMETRICAGGREGATETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiCurvePropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a collection of curves as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:MultiCurve"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTICURVEPROPERTYTYPE_TYPE = build_MULTICURVEPROPERTYTYPE_TYPE();
     
    private static AttributeType build_MULTICURVEPROPERTYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MultiCurvePropertyType"), com.vividsolutions.jts.geom.GeometryCollection.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="AreaType"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:MeasureType"/&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType AREATYPE_TYPE = build_AREATYPE_TYPE();
    
    private static ComplexType build_AREATYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AreaType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DerivedUnitType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:UnitDefinitionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:derivationUnitTerm"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DERIVEDUNITTYPE_TYPE = build_DERIVEDUNITTYPE_TYPE();
    
    private static ComplexType build_DERIVEDUNITTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DERIVATIONUNITTERMTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","derivationUnitTerm"), 1, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DerivedUnitType"), schema, false,
            false, Collections.<Filter>emptyList(), UNITDEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ProcedurePropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractFeature"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PROCEDUREPROPERTYTYPE_TYPE = build_PROCEDUREPROPERTYTYPE_TYPE();
    
    private static ComplexType build_PROCEDUREPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTFEATURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractFeature"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ProcedurePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TargetPropertyType"&gt;
     *      &lt;choice minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractFeature"/&gt;
     *          &lt;element ref="gml:AbstractGeometry"/&gt;
     *      &lt;/choice&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TARGETPROPERTYTYPE_TYPE = build_TARGETPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TARGETPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTFEATURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractFeature"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGEOMETRYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometry"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TargetPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ResultType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;any namespace="##any"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType RESULTTYPE_TYPE = build_RESULTTYPE_TYPE();
    
    private static ComplexType build_RESULTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ResultType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ObservationType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractFeatureType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:validTime"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:using"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:target"/&gt;
     *                  &lt;element ref="gml:resultOf"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OBSERVATIONTYPE_TYPE = build_OBSERVATIONTYPE_TYPE();
    
    private static ComplexType build_OBSERVATIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPRIMITIVEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","validTime"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                PROCEDUREPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","using"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TARGETPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","target"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                RESULTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","resultOf"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ObservationType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTFEATURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectedObservationType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:ObservationType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:direction"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTEDOBSERVATIONTYPE_TYPE = build_DIRECTEDOBSERVATIONTYPE_TYPE();
    
    private static ComplexType build_DIRECTEDOBSERVATIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTIONPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","direction"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectedObservationType"), schema, false,
            false, Collections.<Filter>emptyList(), OBSERVATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectedObservationAtDistanceType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:DirectedObservationType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element name="distance" type="gml:MeasureType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTEDOBSERVATIONATDISTANCETYPE_TYPE = build_DIRECTEDOBSERVATIONATDISTANCETYPE_TYPE();
    
    private static ComplexType build_DIRECTEDOBSERVATIONATDISTANCETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                MEASURETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","distance"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectedObservationAtDistanceType"), schema, false,
            false, Collections.<Filter>emptyList(), DIRECTEDOBSERVATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="_Boolean"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="boolean"&gt;
     *              &lt;attribute name="nilReason" type="gml:NilReasonType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType _BOOLEAN_TYPE = build__BOOLEAN_TYPE();
    
    private static ComplexType build__BOOLEAN_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","_Boolean"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.BOOLEAN_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="BooleanPropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Boolean"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType BOOLEANPROPERTYTYPE_TYPE = build_BOOLEANPROPERTYTYPE_TYPE();
    
    private static ComplexType build_BOOLEANPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                _BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Boolean"), 1, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","BooleanPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="NilReasonEnumeration"&gt;
     *      &lt;union&gt;
     *          &lt;simpleType&gt;
     *              &lt;restriction base="string"&gt;
     *                  &lt;enumeration value="inapplicable"/&gt;
     *                  &lt;enumeration value="missing"/&gt;
     *                  &lt;enumeration value="template"/&gt;
     *                  &lt;enumeration value="unknown"/&gt;
     *                  &lt;enumeration value="withheld"/&gt;
     *              &lt;/restriction&gt;
     *          &lt;/simpleType&gt;
     *          &lt;simpleType&gt;
     *              &lt;restriction base="string"&gt;
     *                  &lt;pattern value="other:\w{2,}"/&gt;
     *              &lt;/restriction&gt;
     *          &lt;/simpleType&gt;
     *      &lt;/union&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NILREASONENUMERATION_TYPE = build_NILREASONENUMERATION_TYPE();
     
    private static AttributeType build_NILREASONENUMERATION_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","NilReasonEnumeration"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TriangleType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractSurfacePatchType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:exterior"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="planar" name="interpolation" type="gml:SurfaceInterpolationType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TRIANGLETYPE_TYPE = build_TRIANGLETYPE_TYPE();
    
    private static ComplexType build_TRIANGLETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTRINGPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","exterior"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SURFACEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TriangleType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTSURFACEPATCHTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ArcStringType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveSegmentType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice maxOccurs="unbounded" minOccurs="3"&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="circularArc3Points" name="interpolation" type="gml:CurveInterpolationType"/&gt;
     *              &lt;attribute name="numArc" type="integer"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ARCSTRINGTYPE_TYPE = build_ARCSTRINGTYPE_TYPE();
    
    private static ComplexType build_ARCSTRINGTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointRep"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","interpolation"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","numArc"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ArcStringType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVESEGMENTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ArcType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:ArcStringType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;choice maxOccurs="3" minOccurs="3"&gt;
     *                          &lt;element ref="gml:pos"/&gt;
     *                          &lt;element ref="gml:pointProperty"/&gt;
     *                          &lt;element ref="gml:pointRep"/&gt;
     *                      &lt;/choice&gt;
     *                      &lt;element ref="gml:posList"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute fixed="1" name="numArc" type="integer"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ARCTYPE_TYPE = build_ARCTYPE_TYPE();
    
    private static ComplexType build_ARCTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pos"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointProperty"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                POINTPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","pointRep"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTPOSITIONLISTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","posList"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinates"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.INTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","numArc"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ArcType"), schema, false,
            false, Collections.<Filter>emptyList(), ARCSTRINGTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CircleType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:ArcType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CIRCLETYPE_TYPE = build_CIRCLETYPE_TYPE();
    
    private static ComplexType build_CIRCLETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CircleType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ARCTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="_Quantity"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:MeasureType"&gt;
     *              &lt;attribute name="nilReason" type="gml:NilReasonType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType _QUANTITY_TYPE = build__QUANTITY_TYPE();
    
    private static ComplexType build__QUANTITY_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","_Quantity"), schema, false,
            false, Collections.<Filter>emptyList(), MEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="QuantityPropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Quantity"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType QUANTITYPROPERTYTYPE_TYPE = build_QUANTITYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_QUANTITYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                _QUANTITY_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Quantity"), 1, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","QuantityPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="RingType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractRingType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:curveMember"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType RINGTYPE_TYPE = build_RINGTYPE_TYPE();
    
    private static ComplexType build_RINGTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CURVEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","curveMember"), 1, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","RingType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTRINGTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="RingPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property with the content model of gml:RingPropertyType encapsulates a ring to represent a component of a surface boundary.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:Ring"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType RINGPROPERTYTYPE_TYPE = build_RINGPROPERTYTYPE_TYPE();
    
    private static ComplexType build_RINGPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                RINGTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Ring"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","RingPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeneralConversionPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:GeneralConversionPropertyType is a property type for association roles to a general conversion, either referencing or containing the definition of that conversion.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractGeneralConversion"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GENERALCONVERSIONPROPERTYTYPE_TYPE = build_GENERALCONVERSIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_GENERALCONVERSIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGENERALCONVERSIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralConversion"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeneralConversionPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGeneralDerivedCRSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:conversion"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTGENERALDERIVEDCRSTYPE_TYPE = build_ABSTRACTGENERALDERIVEDCRSTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTGENERALDERIVEDCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GENERALCONVERSIONPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","conversion"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralDerivedCRSType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SingleCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:SingleCRSPropertyType is a property type for association roles to a single coordinate reference system, either referencing or containing the definition of that coordinate reference system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractSingleCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SINGLECRSPROPERTYTYPE_TYPE = build_SINGLECRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_SINGLECRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractSingleCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SingleCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DerivedCRSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeneralDerivedCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:baseCRS"/&gt;
     *                  &lt;element ref="gml:derivedCRSType"/&gt;
     *                  &lt;element ref="gml:coordinateSystem"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DERIVEDCRSTYPE_TYPE = build_DERIVEDCRSTYPE_TYPE();
    
    private static ComplexType build_DERIVEDCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SINGLECRSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","baseCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CODEWITHAUTHORITYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","derivedCRSType"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATESYSTEMPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordinateSystem"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DerivedCRSType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGENERALDERIVEDCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DerivedCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:DerivedCRSPropertyType is a property type for association roles to a non-projected derived coordinate reference system, either referencing or containing the definition of that reference system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:DerivedCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DERIVEDCRSPROPERTYTYPE_TYPE = build_DERIVEDCRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_DERIVEDCRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DERIVEDCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","DerivedCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DerivedCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="OperationParameterGroupType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeneralOperationParameterType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:maximumOccurs"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="2" ref="gml:parameter"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONPARAMETERGROUPTYPE_TYPE = build_OPERATIONPARAMETERGROUPTYPE_TYPE();
    
    private static ComplexType build_OPERATIONPARAMETERGROUPTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.POSITIVEINTEGER_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","maximumOccurs"), 0, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGENERALOPERATIONPARAMETERPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","parameter"), 2, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","OperationParameterGroupType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGENERALOPERATIONPARAMETERTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="OperationParameterGroupPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:OperationParameterPropertyType is a property type for association roles to an operation parameter group, either referencing or containing the definition of that parameter group.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:OperationParameterGroup"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONPARAMETERGROUPPROPERTYTYPE_TYPE = build_OPERATIONPARAMETERGROUPPROPERTYTYPE_TYPE();
    
    private static ComplexType build_OPERATIONPARAMETERGROUPPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                OPERATIONPARAMETERGROUPTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","OperationParameterGroup"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","OperationParameterGroupPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ParameterValueGroupType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeneralParameterValueType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="2" ref="gml:parameterValue"/&gt;
     *                  &lt;element ref="gml:group"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PARAMETERVALUEGROUPTYPE_TYPE = build_PARAMETERVALUEGROUPTYPE_TYPE();
    
    private static ComplexType build_PARAMETERVALUEGROUPTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGENERALPARAMETERVALUEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","parameterValue"), 2, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                OPERATIONPARAMETERGROUPPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","group"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ParameterValueGroupType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGENERALPARAMETERVALUETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ScaleType"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:MeasureType"/&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SCALETYPE_TYPE = build_SCALETYPE_TYPE();
    
    private static ComplexType build_SCALETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ScaleType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeometryArrayPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;If a feature has a property which takes an array of geometry elements as its value, this is called a geometry array property. A generic type for such a geometry property is GeometryArrayPropertyType. 
     *  The elements are always contained inline in the array property, referencing geometry elements or arrays of geometry elements via XLinks is not supported.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractGeometry"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEOMETRYARRAYPROPERTYTYPE_TYPE = build_GEOMETRYARRAYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_GEOMETRYARRAYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGEOMETRYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometry"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeometryArrayPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiGeometryType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometricAggregateType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:geometryMember"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:geometryMembers"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTIGEOMETRYTYPE_TYPE = build_MULTIGEOMETRYTYPE_TYPE();
     
    private static AttributeType build_MULTIGEOMETRYTYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MultiGeometryType"), com.vividsolutions.jts.geom.GeometryCollection.class, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGEOMETRICAGGREGATETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ConeType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGriddedSurfaceType"&gt;
     *              &lt;attribute fixed="circularArc3Points"
     *                  name="horizontalCurveType" type="gml:CurveInterpolationType"/&gt;
     *              &lt;attribute fixed="linear" name="verticalCurveType" type="gml:CurveInterpolationType"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CONETYPE_TYPE = build_CONETYPE_TYPE();
    
    private static ComplexType build_CONETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","horizontalCurveType"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CURVEINTERPOLATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","verticalCurveType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ConeType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGRIDDEDSURFACETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ValueArrayPropertyType"&gt;
     *      &lt;sequence maxOccurs="unbounded"&gt;
     *          &lt;group ref="gml:Value"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VALUEARRAYPROPERTYTYPE_TYPE = build_VALUEARRAYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_VALUEARRAYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractValue"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGEOMETRYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometry"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTTIMEOBJECTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeObject"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Null"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ValueArrayPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CurveSegmentArrayPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CurveSegmentArrayPropertyType is a container for an array of curve segments.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractCurveSegment"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CURVESEGMENTARRAYPROPERTYTYPE_TYPE = build_CURVESEGMENTARRAYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_CURVESEGMENTARRAYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTCURVESEGMENTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractCurveSegment"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CurveSegmentArrayPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CurveType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:segments"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType CURVETYPE_TYPE = build_CURVETYPE_TYPE();
     
    private static AttributeType build_CURVETYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CurveType"), com.vividsolutions.jts.geom.MultiLineString.class, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="DirectedNodePropertyType"&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:Node"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attribute default="+" name="orientation" type="gml:SignType"/&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DIRECTEDNODEPROPERTYTYPE_TYPE = build_DIRECTEDNODEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_DIRECTEDNODEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                NODETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","Node"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SIGNTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","orientation"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","DirectedNodePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoPointType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTopologyType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:directedNode"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOPOINTTYPE_TYPE = build_TOPOPOINTTYPE_TYPE();
    
    private static ComplexType build_TOPOPOINTTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                DIRECTEDNODEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","directedNode"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoPointType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTTOPOLOGYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TopoPointPropertyType"&gt;
     *      &lt;sequence&gt;
     *          &lt;element ref="gml:TopoPoint"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TOPOPOINTPROPERTYTYPE_TYPE = build_TOPOPOINTPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TOPOPOINTPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TOPOPOINTTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TopoPoint"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TopoPointPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="AxisDirection"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;The value of a gml:AxisDirection indicates the incrementation order to be used on an axis of the grid.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;pattern value="[\+\-][1-9][0-9]*"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType AXISDIRECTION_TYPE = build_AXISDIRECTION_TYPE();
     
    private static AttributeType build_AXISDIRECTION_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AxisDirection"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="OperationPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractOperation"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONPROPERTYTYPE_TYPE = build_OPERATIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_OPERATIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTCOORDINATEOPERATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractOperation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","OperationPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="booleanOrNilReasonList"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A type for a list of values of the respective simple type.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;list itemType="gml:booleanOrNilReason"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType BOOLEANORNILREASONLIST_TYPE = build_BOOLEANORNILREASONLIST_TYPE();
     
    private static AttributeType build_BOOLEANORNILREASONLIST_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","booleanOrNilReasonList"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="VolumeType"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:MeasureType"/&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType VOLUMETYPE_TYPE = build_VOLUMETYPE_TYPE();
    
    private static ComplexType build_VOLUMETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","VolumeType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="stringOrNilReason"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Extension to the respective XML Schema built-in simple type to allow a choice of either a value of the built-in simple type or a reason for a nil value.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;union memberTypes="gml:NilReasonEnumeration string anyURI"/&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType STRINGORNILREASON_TYPE = build_STRINGORNILREASON_TYPE();
     
    private static AttributeType build_STRINGORNILREASON_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","stringOrNilReason"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractTimeComplexType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTimeObjectType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTTIMECOMPLEXTYPE_TYPE = build_ABSTRACTTIMECOMPLEXTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTTIMECOMPLEXTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeComplexType"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTTIMEOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeTopologyPrimitivePropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimeTopologyPrimitivePropertyType provides for associating a gml:AbstractTimeTopologyPrimitive with an object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractTimeTopologyPrimitive"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMETOPOLOGYPRIMITIVEPROPERTYTYPE_TYPE = build_TIMETOPOLOGYPRIMITIVEPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMETOPOLOGYPRIMITIVEPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTTIMETOPOLOGYPRIMITIVETYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeTopologyPrimitive"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeTopologyPrimitivePropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="TimeTopologyComplexType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractTimeComplexType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" name="primitive" type="gml:TimeTopologyPrimitivePropertyType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMETOPOLOGYCOMPLEXTYPE_TYPE = build_TIMETOPOLOGYCOMPLEXTYPE_TYPE();
    
    private static ComplexType build_TIMETOPOLOGYCOMPLEXTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMETOPOLOGYPRIMITIVEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","primitive"), 1, 2147483647, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeTopologyComplexType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTTIMECOMPLEXTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeTopologyComplexPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:TimeTopologyComplexPropertyType provides for associating a gml:TimeTopologyComplex with an object.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:TimeTopologyComplex"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMETOPOLOGYCOMPLEXPROPERTYTYPE_TYPE = build_TIMETOPOLOGYCOMPLEXPROPERTYTYPE_TYPE();
    
    private static ComplexType build_TIMETOPOLOGYCOMPLEXPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMETOPOLOGYCOMPLEXTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","TimeTopologyComplex"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeTopologyComplexPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="TimeType"&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="gml:MeasureType"/&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TIMETYPE_TYPE = build_TIMETYPE_TYPE();
    
    private static ComplexType build_TIMETYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","TimeType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MEASURETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EllipsoidalCSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateSystemType"/&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ELLIPSOIDALCSTYPE_TYPE = build_ELLIPSOIDALCSTYPE_TYPE();
    
    private static ComplexType build_ELLIPSOIDALCSTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EllipsoidalCSType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATESYSTEMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EllipsoidalCSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:EllipsoidalCSPropertyType is a property type for association roles to an ellipsoidal coordinate system, either referencing or containing the definition of that coordinate system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:EllipsoidalCS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ELLIPSOIDALCSPROPERTYTYPE_TYPE = build_ELLIPSOIDALCSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_ELLIPSOIDALCSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ELLIPSOIDALCSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","EllipsoidalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EllipsoidalCSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeodeticCRSType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:GeodeticCRS is a coordinate reference system based on a geodetic datum.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:ellipsoidalCS"/&gt;
     *                      &lt;element ref="gml:cartesianCS"/&gt;
     *                      &lt;element ref="gml:sphericalCS"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element ref="gml:geodeticDatum"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEODETICCRSTYPE_TYPE = build_GEODETICCRSTYPE_TYPE();
    
    private static ComplexType build_GEODETICCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ELLIPSOIDALCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ellipsoidalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CARTESIANCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","cartesianCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SPHERICALCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","sphericalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                GEODETICDATUMPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","geodeticDatum"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeodeticCRSType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeodeticCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:GeodeticCRSPropertyType is a property type for association roles to a geodetic coordinate reference system, either referencing or containing the definition of that reference system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:GeodeticCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEODETICCRSPROPERTYTYPE_TYPE = build_GEODETICCRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_GEODETICCRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GEODETICCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","GeodeticCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeodeticCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeographicCRSType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:usesEllipsoidalCS"/&gt;
     *                  &lt;element ref="gml:usesGeodeticDatum"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEOGRAPHICCRSTYPE_TYPE = build_GEOGRAPHICCRSTYPE_TYPE();
    
    private static ComplexType build_GEOGRAPHICCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ELLIPSOIDALCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","usesEllipsoidalCS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                GEODETICDATUMPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","usesGeodeticDatum"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeographicCRSType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeographicCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:GeographicCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GEOGRAPHICCRSPROPERTYTYPE_TYPE = build_GEOGRAPHICCRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_GEOGRAPHICCRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GEOGRAPHICCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","GeographicCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeographicCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ProjectedCRSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeneralDerivedCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:baseGeodeticCRS"/&gt;
     *                      &lt;element ref="gml:baseGeographicCRS"/&gt;
     *                  &lt;/choice&gt;
     *                  &lt;element ref="gml:cartesianCS"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PROJECTEDCRSTYPE_TYPE = build_PROJECTEDCRSTYPE_TYPE();
    
    private static ComplexType build_PROJECTEDCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                GEODETICCRSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","baseGeodeticCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                GEOGRAPHICCRSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","baseGeographicCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                CARTESIANCSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","cartesianCS"), 1, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ProjectedCRSType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGENERALDERIVEDCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ProjectedCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:ProjectedCRSPropertyType is a property type for association roles to a projected coordinate reference system, either referencing or containing the definition of that reference system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:ProjectedCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PROJECTEDCRSPROPERTYTYPE_TYPE = build_PROJECTEDCRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_PROJECTEDCRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                PROJECTEDCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ProjectedCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ProjectedCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="EnvelopeWithTimePeriodType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:EnvelopeType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element name="beginPosition" type="gml:TimePositionType"/&gt;
     *                  &lt;element name="endPosition" type="gml:TimePositionType"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute default="#ISO-8601" name="frame" type="anyURI"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ENVELOPEWITHTIMEPERIODTYPE_TYPE = build_ENVELOPEWITHTIMEPERIODTYPE_TYPE();
    
    private static ComplexType build_ENVELOPEWITHTIMEPERIODTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","beginPosition"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                TIMEPOSITIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","endPosition"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","frame"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","EnvelopeWithTimePeriodType"), schema, false,
            false, Collections.<Filter>emptyList(), ENVELOPETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CompoundCRSType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCRSType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="2" ref="gml:componentReferenceSystem"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COMPOUNDCRSTYPE_TYPE = build_COMPOUNDCRSTYPE_TYPE();
    
    private static ComplexType build_COMPOUNDCRSTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SINGLECRSPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","componentReferenceSystem"), 2, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CompoundCRSType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CompoundCRSPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:CompoundCRSPropertyType is a property type for association roles to a compound coordinate reference system, either referencing or containing the definition of that reference system.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:CompoundCRS"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COMPOUNDCRSPROPERTYTYPE_TYPE = build_COMPOUNDCRSPROPERTYTYPE_TYPE();
    
    private static ComplexType build_COMPOUNDCRSPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                COMPOUNDCRSTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","CompoundCRS"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","CompoundCRSPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SingleOperationPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:SingleOperationPropertyType is a property type for association roles to a single operation, either referencing or containing the definition of that single operation.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractSingleOperation"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SINGLEOPERATIONPROPERTYTYPE_TYPE = build_SINGLEOPERATIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_SINGLEOPERATIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTCOORDINATEOPERATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractSingleOperation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SingleOperationPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeneralTransformationPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:GeneralTransformationPropertyType is a property type for association roles to a general transformation, either referencing or containing the definition of that transformation.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractGeneralTransformation"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType GENERALTRANSFORMATIONPROPERTYTYPE_TYPE = build_GENERALTRANSFORMATIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_GENERALTRANSFORMATIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTGENERALTRANSFORMATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralTransformation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","GeneralTransformationPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractMetadataPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;To associate metadata described by any XML Schema with a GML object, a property element shall be defined whose content model is derived by extension from gml:AbstractMetadataPropertyType. 
     *  The value of such a property shall be metadata. The content model of such a property type, i.e. the metadata application schema shall be specified by the GML Application Schema.
     *  By default, this abstract property type does not imply any ownership of the metadata. The owns attribute of gml:OwnershipAttributeGroup may be used on a metadata property element instance to assert ownership of the metadata. 
     *  If metadata following the conceptual model of ISO 19115 is to be encoded in a GML document, the corresponding Implementation Specification specified in ISO/TS 19139 shall be used to encode the metadata information.
     *  &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTMETADATAPROPERTYTYPE_TYPE = build_ABSTRACTMETADATAPROPERTYTYPE_TYPE();
    
    private static ComplexType build_ABSTRACTMETADATAPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractMetadataPropertyType"), schema, false,
            true, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ConcatenatedOperationType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:ConcatenatedOperation is an ordered sequence of two or more coordinate operations. This sequence of operations is constrained by the requirement that the source coordinate reference system of step (n+1) must be the same as the target coordinate reference system of step (n). The source coordinate reference system of the first step and the target coordinate reference system of the last step are the source and target coordinate reference system associated with the concatenated operation. Instead of a forward operation, an inverse operation may be used for one or more of the operation steps mentioned above, if the inverse operation is uniquely defined by the forward operation.
     *  The gml:coordOperation property elements are an ordered sequence of associations to the two or more operations used by this concatenated operation. The AggregationAttributeGroup should be used to specify that the coordOperation associations are ordered.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoordinateOperationType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="2" ref="gml:coordOperation"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CONCATENATEDOPERATIONTYPE_TYPE = build_CONCATENATEDOPERATIONTYPE_TYPE();
    
    private static ComplexType build_CONCATENATEDOPERATIONTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                COORDINATEOPERATIONPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coordOperation"), 2, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                AGGREGATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","aggregationType"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ConcatenatedOperationType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCOORDINATEOPERATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="ConcatenatedOperationPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:ConcatenatedOperationPropertyType is a property type for association roles to a concatenated operation, either referencing or containing the definition of that concatenated operation.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:ConcatenatedOperation"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CONCATENATEDOPERATIONPROPERTYTYPE_TYPE = build_CONCATENATEDOPERATIONPROPERTYTYPE_TYPE();
    
    private static ComplexType build_CONCATENATEDOPERATIONPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CONCATENATEDOPERATIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","ConcatenatedOperation"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","ConcatenatedOperationPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="OffsetCurveType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCurveSegmentType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element name="offsetBase" type="gml:CurvePropertyType"/&gt;
     *                  &lt;element name="distance" type="gml:LengthType"/&gt;
     *                  &lt;element minOccurs="0" name="refDirection" type="gml:VectorType"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OFFSETCURVETYPE_TYPE = build_OFFSETCURVETYPE_TYPE();
    
    private static ComplexType build_OFFSETCURVETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                CURVEPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","offsetBase"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                LENGTHTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","distance"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                VECTORTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","refDirection"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","OffsetCurveType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTCURVESEGMENTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="SolidArrayPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;gml:SolidArrayPropertyType is a container for an array of solids. The elements are always contained in the array property, referencing geometry elements or arrays of geometry elements is not supported.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
     *          &lt;element ref="gml:AbstractSolid"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SOLIDARRAYPROPERTYTYPE_TYPE = build_SOLIDARRAYPROPERTYTYPE_TYPE();
    
    private static ComplexType build_SOLIDARRAYPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                ABSTRACTSOLIDTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","AbstractSolid"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","SolidArrayPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiSolidType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometricAggregateType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:solidMember"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:solidMembers"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MULTISOLIDTYPE_TYPE = build_MULTISOLIDTYPE_TYPE();
    
    private static ComplexType build_MULTISOLIDTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                SOLIDPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","solidMember"), 0, 2147483647, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                SOLIDARRAYPROPERTYTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","solidMembers"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MultiSolidType"), schema, false,
            false, Collections.<Filter>emptyList(), ABSTRACTGEOMETRICAGGREGATETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiSolidPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;A property that has a collection of solids as its value domain may either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element shall be given, but neither both nor none.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:MultiSolid"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *      &lt;attributeGroup ref="gml:OwnershipAttributeGroup"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MULTISOLIDPROPERTYTYPE_TYPE = build_MULTISOLIDPROPERTYTYPE_TYPE();
    
    private static ComplexType build_MULTISOLIDPROPERTYTYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                MULTISOLIDTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","MultiSolid"), 1, 1, false, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._ACTUATE_TYPE, new NameImpl("http://www.w3.org/1999/xlink","actuate"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","arcrole"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","href"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                NILREASONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","nilReason"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","remoteSchema"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.ANYURI_TYPE, new NameImpl("http://www.w3.org/1999/xlink","role"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XLINKSchema._SHOW_TYPE, new NameImpl("http://www.w3.org/1999/xlink","show"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","title"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.w3.org/1999/xlink","type"), 0, 1, true, null
            )
        );
        schema.add(
            new AttributeDescriptorImpl(
                XSSchema.BOOLEAN_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","owns"), 0, 1, true, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","MultiSolidPropertyType"), schema, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractContinuousCoverageType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractCoverageType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:coverageFunction"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTCONTINUOUSCOVERAGETYPE_TYPE = build_ABSTRACTCONTINUOUSCOVERAGETYPE_TYPE();
    
    private static ComplexType build_ABSTRACTCONTINUOUSCOVERAGETYPE_TYPE() {
        ComplexType builtType;
        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();
        schema.add(
            new AttributeDescriptorImpl(
                COVERAGEFUNCTIONTYPE_TYPE, new NameImpl("http://www.opengis.net/gml/3.2","coverageFunction"), 0, 1, false, null
            )
        );
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.opengis.net/gml/3.2","AbstractContinuousCoverageType"), schema, false,
            true, Collections.<Filter>emptyList(), ABSTRACTCOVERAGETYPE_TYPE, null
        );
        return builtType;
    }


    public GMLSchema() {
        super("http://www.opengis.net/gml/3.2");

        put(new NameImpl("http://www.opengis.net/gml/3.2","doubleList"),DOUBLELIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","NCNameList"),NCNAMELIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectPositionType"),DIRECTPOSITIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CoordinatesType"),COORDINATESTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EnvelopeType"),ENVELOPETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","NilReasonType"),NILREASONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","BoundingShapeType"),BOUNDINGSHAPETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractMetaDataType"),ABSTRACTMETADATATYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MetaDataPropertyType"),METADATAPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","StringOrRefType"),STRINGORREFTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ReferenceType"),REFERENCETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CodeType"),CODETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CodeWithAuthorityType"),CODEWITHAUTHORITYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGMLType"),ABSTRACTGMLTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometryType"),ABSTRACTGEOMETRYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","StringOrRefType"),STRINGORREFTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","LocationPropertyType"),LOCATIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractFeatureType"),ABSTRACTFEATURETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","BoundedFeatureType"),BOUNDEDFEATURETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","_coordinateOperationAccuracy"),_COORDINATEOPERATIONACCURACY_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DefinitionBaseType"),DEFINITIONBASETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DefinitionType"),DEFINITIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","IdentifiedObjectType"),IDENTIFIEDOBJECTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","_domainOfValidity"),_DOMAINOFVALIDITY_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractCRSType"),ABSTRACTCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CRSPropertyType"),CRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractCoordinateOperationType"),ABSTRACTCOORDINATEOPERATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CoordinateOperationPropertyType"),COORDINATEOPERATIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AggregationType"),AGGREGATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PassThroughOperationType"),PASSTHROUGHOPERATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PassThroughOperationPropertyType"),PASSTHROUGHOPERATIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractRingType"),ABSTRACTRINGTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometricPrimitiveType"),ABSTRACTGEOMETRICPRIMITIVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PointType"),POINTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PointPropertyType"),POINTPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectPositionListType"),DIRECTPOSITIONLISTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","LinearRingType"),LINEARRINGTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","LinearRingPropertyType"),LINEARRINGPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractTopologyType"),ABSTRACTTOPOLOGYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractTopoPrimitiveType"),ABSTRACTTOPOPRIMITIVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractSurfaceType"),ABSTRACTSURFACETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SurfacePropertyType"),SURFACEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","FaceType"),FACETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SignType"),SIGNTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectedFacePropertyType"),DIRECTEDFACEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractSolidType"),ABSTRACTSOLIDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SolidPropertyType"),SOLIDPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoSolidType"),TOPOSOLIDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectedTopoSolidPropertyType"),DIRECTEDTOPOSOLIDPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoVolumeType"),TOPOVOLUMETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoVolumePropertyType"),TOPOVOLUMEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeometricAggregateType"),ABSTRACTGEOMETRICAGGREGATETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SurfaceArrayPropertyType"),SURFACEARRAYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MultiSurfaceType"),MULTISURFACETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MultiSurfacePropertyType"),MULTISURFACEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralConversionType"),ABSTRACTGENERALCONVERSIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","_formulaCitation"),_FORMULACITATION_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralOperationParameterType"),ABSTRACTGENERALOPERATIONPARAMETERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralOperationParameterPropertyType"),ABSTRACTGENERALOPERATIONPARAMETERPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","OperationMethodType"),OPERATIONMETHODTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","OperationMethodPropertyType"),OPERATIONMETHODPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralParameterValueType"),ABSTRACTGENERALPARAMETERVALUETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralParameterValuePropertyType"),ABSTRACTGENERALPARAMETERVALUEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ConversionType"),CONVERSIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ConversionPropertyType"),CONVERSIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","integerOrNilReasonList"),INTEGERORNILREASONLIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CountExtentType"),COUNTEXTENTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","FaceOrTopoSolidPropertyType"),FACEORTOPOSOLIDPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoSolidPropertyType"),TOPOSOLIDPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractCurveType"),ABSTRACTCURVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CurvePropertyType"),CURVEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EdgeType"),EDGETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectedEdgePropertyType"),DIRECTEDEDGEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","NodeType"),NODETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","NodePropertyType"),NODEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","UomIdentifier"),UOMIDENTIFIER_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CoordinateSystemAxisType"),COORDINATESYSTEMAXISTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CoordinateSystemAxisPropertyType"),COORDINATESYSTEMAXISPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractCoordinateSystemType"),ABSTRACTCOORDINATESYSTEMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AffineCSType"),AFFINECSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AffineCSPropertyType"),AFFINECSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CartesianCSType"),CARTESIANCSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CartesianCSPropertyType"),CARTESIANCSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CylindricalCSType"),CYLINDRICALCSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CylindricalCSPropertyType"),CYLINDRICALCSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","LinearCSType"),LINEARCSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","LinearCSPropertyType"),LINEARCSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PolarCSType"),POLARCSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PolarCSPropertyType"),POLARCSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SphericalCSType"),SPHERICALCSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SphericalCSPropertyType"),SPHERICALCSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","UserDefinedCSType"),USERDEFINEDCSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","UserDefinedCSPropertyType"),USERDEFINEDCSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CoordinateSystemPropertyType"),COORDINATESYSTEMPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractDatumType"),ABSTRACTDATUMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EngineeringDatumType"),ENGINEERINGDATUMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EngineeringDatumPropertyType"),ENGINEERINGDATUMPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EngineeringCRSType"),ENGINEERINGCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EngineeringCRSPropertyType"),ENGINEERINGCRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MeasureType"),MEASURETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GridLengthType"),GRIDLENGTHTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","doubleOrNilReason"),DOUBLEORNILREASON_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","VerticalCSType"),VERTICALCSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","VerticalCSPropertyType"),VERTICALCSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","VerticalDatumType"),VERTICALDATUMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","VerticalDatumPropertyType"),VERTICALDATUMPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","VerticalCRSType"),VERTICALCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","VerticalCRSPropertyType"),VERTICALCRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","_Category"),_CATEGORY_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CategoryPropertyType"),CATEGORYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DatumPropertyType"),DATUMPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeReferenceSystemType"),TIMEREFERENCESYSTEMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeObjectType"),ABSTRACTTIMEOBJECTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimePrimitiveType"),ABSTRACTTIMEPRIMITIVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimePrimitivePropertyType"),TIMEPRIMITIVEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","RelatedTimeType"),RELATEDTIMETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeTopologyPrimitiveType"),ABSTRACTTIMETOPOLOGYPRIMITIVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeGeometricPrimitiveType"),ABSTRACTTIMEGEOMETRICPRIMITIVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimePositionUnion"),TIMEPOSITIONUNION_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeIndeterminateValueType"),TIMEINDETERMINATEVALUETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimePositionType"),TIMEPOSITIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeInstantType"),TIMEINSTANTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeInstantPropertyType"),TIMEINSTANTPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeUnitType"),TIMEUNITTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeIntervalLengthType"),TIMEINTERVALLENGTHTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimePeriodType"),TIMEPERIODTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimePeriodPropertyType"),TIMEPERIODPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeEdgeType"),TIMEEDGETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeEdgePropertyType"),TIMEEDGEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeNodeType"),TIMENODETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeNodePropertyType"),TIMENODEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeOrdinalEraType"),TIMEORDINALERATYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeOrdinalEraPropertyType"),TIMEORDINALERAPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeOrdinalReferenceSystemType"),TIMEORDINALREFERENCESYSTEMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractCurveSegmentType"),ABSTRACTCURVESEGMENTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","VectorType"),VECTORTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CurveInterpolationType"),CURVEINTERPOLATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ArcStringByBulgeType"),ARCSTRINGBYBULGETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ArcByBulgeType"),ARCBYBULGETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","NameList"),NAMELIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CodeListType"),CODELISTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","FeaturePropertyType"),FEATUREPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","FeatureArrayPropertyType"),FEATUREARRAYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractFeatureCollectionType"),ABSTRACTFEATURECOLLECTIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","FeatureCollectionType"),FEATURECOLLECTIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CubicSplineType"),CUBICSPLINETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeometricPrimitivePropertyType"),GEOMETRICPRIMITIVEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeometricComplexType"),GEOMETRICCOMPLEXTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CompositeCurveType"),COMPOSITECURVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CompositeSurfaceType"),COMPOSITESURFACETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CompositeSolidType"),COMPOSITESOLIDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeometricComplexPropertyType"),GEOMETRICCOMPLEXPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","_Count"),_COUNT_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CountPropertyType"),COUNTPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","LineStringType"),LINESTRINGTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractFeatureMemberType"),ABSTRACTFEATUREMEMBERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeSliceType"),ABSTRACTTIMESLICETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","HistoryPropertyType"),HISTORYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DynamicFeatureType"),DYNAMICFEATURETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DynamicFeatureMemberType"),DYNAMICFEATUREMEMBERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DynamicFeatureCollectionType"),DYNAMICFEATURECOLLECTIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PointArrayPropertyType"),POINTARRAYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MultiPointType"),MULTIPOINTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MultiPointPropertyType"),MULTIPOINTPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","doubleOrNilReasonList"),DOUBLEORNILREASONLIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MeasureOrNilReasonListType"),MEASUREORNILREASONLISTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","QuantityExtentType"),QUANTITYEXTENTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DomainSetType"),DOMAINSETTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CompositeValueType"),COMPOSITEVALUETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ValueArrayType"),VALUEARRAYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AssociationRoleType"),ASSOCIATIONROLETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DataBlockType"),DATABLOCKTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","FileType"),FILETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","RangeSetType"),RANGESETTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractCoverageType"),ABSTRACTCOVERAGETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MappingRuleType"),MAPPINGRULETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SequenceRuleEnumeration"),SEQUENCERULEENUMERATION_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","IncrementOrder"),INCREMENTORDER_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AxisDirectionList"),AXISDIRECTIONLIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SequenceRuleType"),SEQUENCERULETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","integerList"),INTEGERLIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GridFunctionType"),GRIDFUNCTIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CoverageFunctionType"),COVERAGEFUNCTIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DiscreteCoverageType"),DISCRETECOVERAGETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","LengthType"),LENGTHTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AngleType"),ANGLETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ArcByCenterPointType"),ARCBYCENTERPOINTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CircleByCenterPointType"),CIRCLEBYCENTERPOINTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PrimeMeridianType"),PRIMEMERIDIANTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PrimeMeridianPropertyType"),PRIMEMERIDIANPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","_SecondDefiningParameter"),_SECONDDEFININGPARAMETER_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","_secondDefiningParameter"),_SECONDDEFININGPARAMETER_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EllipsoidType"),ELLIPSOIDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EllipsoidPropertyType"),ELLIPSOIDPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeodeticDatumType"),GEODETICDATUMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeodeticDatumPropertyType"),GEODETICDATUMPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeocentricCRSType"),GEOCENTRICCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeocentricCRSPropertyType"),GEOCENTRICCRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GridEnvelopeType"),GRIDENVELOPETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GridLimitsType"),GRIDLIMITSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GridType"),GRIDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","RectifiedGridType"),RECTIFIEDGRIDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoSurfaceType"),TOPOSURFACETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoSurfacePropertyType"),TOPOSURFACEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SuccessionType"),SUCCESSIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","booleanOrNilReason"),BOOLEANORNILREASON_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeometryPropertyType"),GEOMETRYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectionVectorType"),DIRECTIONVECTORTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CompassPointEnumeration"),COMPASSPOINTENUMERATION_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectionDescriptionType"),DIRECTIONDESCRIPTIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectionPropertyType"),DIRECTIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MovingObjectStatusType"),MOVINGOBJECTSTATUSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractMemberType"),ABSTRACTMEMBERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DictionaryEntryType"),DICTIONARYENTRYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DefinitionProxyType"),DEFINITIONPROXYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","IndirectEntryType"),INDIRECTENTRYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DictionaryType"),DICTIONARYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractSurfacePatchType"),ABSTRACTSURFACEPATCHTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractRingPropertyType"),ABSTRACTRINGPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SurfaceInterpolationType"),SURFACEINTERPOLATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","RectangleType"),RECTANGLETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","NodeOrEdgePropertyType"),NODEOREDGEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ObliqueCartesianCSType"),OBLIQUECARTESIANCSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ObliqueCartesianCSPropertyType"),OBLIQUECARTESIANCSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ImageDatumType"),IMAGEDATUMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ImageDatumPropertyType"),IMAGEDATUMPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ImageCRSType"),IMAGECRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ImageCRSPropertyType"),IMAGECRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","OrientableSurfaceType"),ORIENTABLESURFACETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoPrimitiveMemberType"),TOPOPRIMITIVEMEMBERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoPrimitiveArrayAssociationType"),TOPOPRIMITIVEARRAYASSOCIATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoComplexType"),TOPOCOMPLEXTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoComplexPropertyType"),TOPOCOMPLEXPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","QNameList"),QNAMELIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ArrayAssociationType"),ARRAYASSOCIATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","BagType"),BAGTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","NameOrNilReasonList"),NAMEORNILREASONLIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CodeOrNilReasonListType"),CODEORNILREASONLISTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CategoryExtentType"),CATEGORYEXTENTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DegreeValueType"),DEGREEVALUETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DegreesType"),DEGREESTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DecimalMinutesType"),DECIMALMINUTESTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ArcMinutesType"),ARCMINUTESTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ArcSecondsType"),ARCSECONDSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DMSAngleType"),DMSANGLETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AngleChoiceType"),ANGLECHOICETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AffinePlacementType"),AFFINEPLACEMENTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ClothoidType_refLocation"),CLOTHOIDTYPE_REFLOCATION_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ClothoidType"),CLOTHOIDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","OrientableCurveType"),ORIENTABLECURVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractParametricCurveSurfaceType"),ABSTRACTPARAMETRICCURVESURFACETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PointGrid_rows_RowType"),POINTGRID_ROWS_ROWTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGriddedSurfaceType_rows"),ABSTRACTGRIDDEDSURFACETYPE_ROWS_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGriddedSurfaceType"),ABSTRACTGRIDDEDSURFACETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CylinderType"),CYLINDERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GenericMetaDataType"),GENERICMETADATATYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","InlinePropertyType"),INLINEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CalDate"),CALDATE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeCalendarEraType"),TIMECALENDARERATYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeCalendarEraPropertyType"),TIMECALENDARERAPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeCalendarType"),TIMECALENDARTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeCalendarPropertyType"),TIMECALENDARPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeClockType"),TIMECLOCKTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeClockPropertyType"),TIMECLOCKPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PolygonPatchType"),POLYGONPATCHTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PolygonType"),POLYGONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ValuePropertyType"),VALUEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","UomSymbol"),UOMSYMBOL_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinitionType"),UNITDEFINITIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","BaseUnitType"),BASEUNITTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MultiGeometryPropertyType"),MULTIGEOMETRYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SurfacePatchArrayPropertyType"),SURFACEPATCHARRAYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SurfaceType"),SURFACETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","LineStringSegmentType"),LINESTRINGSEGMENTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","LineStringSegmentArrayPropertyType"),LINESTRINGSEGMENTARRAYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TinType_controlPoint"),TINTYPE_CONTROLPOINT_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TinType"),TINTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeodesicStringType"),GEODESICSTRINGTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeodesicType"),GEODESICTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","UnitOfMeasureType"),UNITOFMEASURETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","FormulaType"),FORMULATYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ConversionToPreferredUnitType"),CONVERSIONTOPREFERREDUNITTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DerivationUnitTermType"),DERIVATIONUNITTERMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ConventionalUnitType"),CONVENTIONALUNITTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","booleanList"),BOOLEANLIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","UomURI"),UOMURI_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","NameOrNilReason"),NAMEORNILREASON_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeCSType"),TIMECSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeCSPropertyType"),TIMECSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TemporalCSType"),TEMPORALCSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TemporalCSPropertyType"),TEMPORALCSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TemporalDatumBaseType"),TEMPORALDATUMBASETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TemporalDatumType"),TEMPORALDATUMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TemporalDatumPropertyType"),TEMPORALDATUMPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TemporalCRSType"),TEMPORALCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TemporalCRSPropertyType"),TEMPORALCRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SpeedType"),SPEEDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ArrayType"),ARRAYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MeasureListType"),MEASURELISTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","OperationParameterType"),OPERATIONPARAMETERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","OperationParameterPropertyType"),OPERATIONPARAMETERPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ParameterValueType"),PARAMETERVALUETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","KnotType"),KNOTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","KnotPropertyType"),KNOTPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","KnotTypesType"),KNOTTYPESTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","BSplineType"),BSPLINETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","BezierType"),BEZIERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","integerOrNilReason"),INTEGERORNILREASON_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeCoordinateSystemType"),TIMECOORDINATESYSTEMTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoCurveType"),TOPOCURVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoCurvePropertyType"),TOPOCURVEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","PriorityLocationPropertyType"),PRIORITYLOCATIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ShellType"),SHELLTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ShellPropertyType"),SHELLPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SolidType"),SOLIDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralTransformationType"),ABSTRACTGENERALTRANSFORMATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TransformationType"),TRANSFORMATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TransformationPropertyType"),TRANSFORMATIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SphereType"),SPHERETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CurveArrayPropertyType"),CURVEARRAYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MultiCurveType"),MULTICURVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MultiCurvePropertyType"),MULTICURVEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AreaType"),AREATYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DerivedUnitType"),DERIVEDUNITTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ProcedurePropertyType"),PROCEDUREPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TargetPropertyType"),TARGETPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ResultType"),RESULTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ObservationType"),OBSERVATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectedObservationType"),DIRECTEDOBSERVATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectedObservationAtDistanceType"),DIRECTEDOBSERVATIONATDISTANCETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","_Boolean"),_BOOLEAN_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","BooleanPropertyType"),BOOLEANPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","NilReasonEnumeration"),NILREASONENUMERATION_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TriangleType"),TRIANGLETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ArcStringType"),ARCSTRINGTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ArcType"),ARCTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CircleType"),CIRCLETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","_Quantity"),_QUANTITY_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","QuantityPropertyType"),QUANTITYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","RingType"),RINGTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","RingPropertyType"),RINGPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeneralConversionPropertyType"),GENERALCONVERSIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralDerivedCRSType"),ABSTRACTGENERALDERIVEDCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SingleCRSPropertyType"),SINGLECRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DerivedCRSType"),DERIVEDCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DerivedCRSPropertyType"),DERIVEDCRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","OperationParameterGroupType"),OPERATIONPARAMETERGROUPTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","OperationParameterGroupPropertyType"),OPERATIONPARAMETERGROUPPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ParameterValueGroupType"),PARAMETERVALUEGROUPTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ScaleType"),SCALETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeometryArrayPropertyType"),GEOMETRYARRAYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MultiGeometryType"),MULTIGEOMETRYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ConeType"),CONETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ValueArrayPropertyType"),VALUEARRAYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CurveSegmentArrayPropertyType"),CURVESEGMENTARRAYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CurveType"),CURVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","DirectedNodePropertyType"),DIRECTEDNODEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoPointType"),TOPOPOINTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TopoPointPropertyType"),TOPOPOINTPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AxisDirection"),AXISDIRECTION_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","OperationPropertyType"),OPERATIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","booleanOrNilReasonList"),BOOLEANORNILREASONLIST_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","VolumeType"),VOLUMETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","stringOrNilReason"),STRINGORNILREASON_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractTimeComplexType"),ABSTRACTTIMECOMPLEXTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeTopologyPrimitivePropertyType"),TIMETOPOLOGYPRIMITIVEPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeTopologyComplexType"),TIMETOPOLOGYCOMPLEXTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeTopologyComplexPropertyType"),TIMETOPOLOGYCOMPLEXPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","TimeType"),TIMETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EllipsoidalCSType"),ELLIPSOIDALCSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EllipsoidalCSPropertyType"),ELLIPSOIDALCSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeodeticCRSType"),GEODETICCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeodeticCRSPropertyType"),GEODETICCRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeographicCRSType"),GEOGRAPHICCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeographicCRSPropertyType"),GEOGRAPHICCRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ProjectedCRSType"),PROJECTEDCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ProjectedCRSPropertyType"),PROJECTEDCRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","EnvelopeWithTimePeriodType"),ENVELOPEWITHTIMEPERIODTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CompoundCRSType"),COMPOUNDCRSTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","CompoundCRSPropertyType"),COMPOUNDCRSPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SingleOperationPropertyType"),SINGLEOPERATIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","GeneralTransformationPropertyType"),GENERALTRANSFORMATIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractMetadataPropertyType"),ABSTRACTMETADATAPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ConcatenatedOperationType"),CONCATENATEDOPERATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","ConcatenatedOperationPropertyType"),CONCATENATEDOPERATIONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","OffsetCurveType"),OFFSETCURVETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","SolidArrayPropertyType"),SOLIDARRAYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MultiSolidType"),MULTISOLIDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","MultiSolidPropertyType"),MULTISOLIDPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml/3.2","AbstractContinuousCoverageType"),ABSTRACTCONTINUOUSCOVERAGETYPE_TYPE);
    }
    
}