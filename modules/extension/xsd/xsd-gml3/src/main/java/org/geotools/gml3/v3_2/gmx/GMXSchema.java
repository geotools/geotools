package org.geotools.gml3.v3_2.gmx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AbstractLazyAttributeTypeImpl;
import org.geotools.feature.type.AbstractLazyComplexTypeImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.gml3.v3_2.GMLSchema;
import org.geotools.gml3.v3_2.gco.GCOSchema;
import org.geotools.gml3.v3_2.gmd.GMDSchema;
import org.geotools.xlink.XLINKSchema;
import org.geotools.xs.XSSchema;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.Schema;

/**
 * 
 *
 * @source $URL$
 */
public class GMXSchema extends SchemaImpl {

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractCT_Catalogue_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="name" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="scope" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="fieldOfApplication" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="versionNumber" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="versionDate" type="gco:Date_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="language" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="characterSet" type="gmd:MD_CharacterSetCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="subCatalogue" type="gmx:CT_Catalogue_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTCT_CATALOGUE_TYPE_TYPE = build_ABSTRACTCT_CATALOGUE_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTCT_CATALOGUE_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","AbstractCT_Catalogue_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","name"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","scope"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","fieldOfApplication"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","versionNumber"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","versionDate"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","language"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.MD_CHARACTERSETCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","characterSet"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_CATALOGUE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","subCatalogue"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractMX_File_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="fileName" type="gmx:FileName_PropertyType"/&gt;
     *                  &lt;xs:element name="fileDescription" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="fileType" type="gmx:MimeFileType_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTMX_FILE_TYPE_TYPE = build_ABSTRACTMX_FILE_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTMX_FILE_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","AbstractMX_File_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        FILENAME_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","fileName"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","fileDescription"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MIMEFILETYPE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","fileType"),
                        1, 1, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Anchor_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:Anchor"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ANCHOR_PROPERTYTYPE_TYPE = build_ANCHOR_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ANCHOR_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","Anchor_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ANCHOR_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","Anchor"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Anchor_Type"&gt;
     *      &lt;xs:simpleContent&gt;
     *          &lt;xs:extension base="xs:string"&gt;
     *              &lt;xs:attributeGroup ref="xlink:simpleLink"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:simpleContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ANCHOR_TYPE_TYPE = build_ANCHOR_TYPE_TYPE();
    
    private static ComplexType build_ANCHOR_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","Anchor_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.STRING_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="BaseUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:BaseUnit"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType BASEUNIT_PROPERTYTYPE_TYPE = build_BASEUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_BASEUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","BaseUnit_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.BASEUNITTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","BaseUnit"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractCRS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_CRS_PROPERTYTYPE_TYPE = build_CT_CRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_CRS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.ABSTRACTCRSTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","AbstractCRS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_Catalogue_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:AbstractCT_Catalogue"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_CATALOGUE_PROPERTYTYPE_TYPE = build_CT_CATALOGUE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CATALOGUE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_Catalogue_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ABSTRACTCT_CATALOGUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","AbstractCT_Catalogue"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CodelistCatalogue_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CT_CodelistCatalogue"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_CODELISTCATALOGUE_PROPERTYTYPE_TYPE = build_CT_CODELISTCATALOGUE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CODELISTCATALOGUE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_CodelistCatalogue_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_CODELISTCATALOGUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CT_CodelistCatalogue"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CodelistCatalogue_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:AbstractCT_Catalogue_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="codelistItem" type="gmx:CT_Codelist_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_CODELISTCATALOGUE_TYPE_TYPE = build_CT_CODELISTCATALOGUE_TYPE_TYPE();
    
    private static ComplexType build_CT_CODELISTCATALOGUE_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_CodelistCatalogue_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTCT_CATALOGUE_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_CODELIST_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","codelistItem"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CodelistValue_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CodeDefinition"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_CODELISTVALUE_PROPERTYTYPE_TYPE = build_CT_CODELISTVALUE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CODELISTVALUE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_CodelistValue_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CODEDEFINITION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CodeDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_Codelist_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CodeListDictionary"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_CODELIST_PROPERTYTYPE_TYPE = build_CT_CODELIST_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CODELIST_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_Codelist_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CODELISTDICTIONARY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CodeListDictionary"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CoordinateSystemAxis_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:CoordinateSystemAxis"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE = build_CT_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_CoordinateSystemAxis_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.COORDINATESYSTEMAXISTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","CoordinateSystemAxis"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CoordinateSystem_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractCoordinateSystem"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_COORDINATESYSTEM_PROPERTYTYPE_TYPE = build_CT_COORDINATESYSTEM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_COORDINATESYSTEM_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_CoordinateSystem_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.ABSTRACTCOORDINATESYSTEMTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","AbstractCoordinateSystem"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CrsCatalogue_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CT_CrsCatalogue"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_CRSCATALOGUE_PROPERTYTYPE_TYPE = build_CT_CRSCATALOGUE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CRSCATALOGUE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_CrsCatalogue_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_CRSCATALOGUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CT_CrsCatalogue"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CrsCatalogue_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:AbstractCT_Catalogue_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="crs" type="gmx:CT_CRS_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="coordinateSystem" type="gmx:CT_CoordinateSystem_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="axis" type="gmx:CT_CoordinateSystemAxis_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="datum" type="gmx:CT_Datum_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="ellipsoid" type="gmx:CT_Ellipsoid_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="primeMeridian" type="gmx:CT_PrimeMeridian_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="operation" type="gmx:CT_Operation_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="operationMethod" type="gmx:CT_OperationMethod_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="parameters" type="gmx:CT_OperationParameters_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_CRSCATALOGUE_TYPE_TYPE = build_CT_CRSCATALOGUE_TYPE_TYPE();
    
    private static ComplexType build_CT_CRSCATALOGUE_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_CrsCatalogue_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTCT_CATALOGUE_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_CRS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","crs"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_COORDINATESYSTEM_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","coordinateSystem"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","axis"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_DATUM_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","datum"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_ELLIPSOID_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ellipsoid"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_PRIMEMERIDIAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","primeMeridian"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_OPERATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","operation"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_OPERATIONMETHOD_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","operationMethod"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_OPERATIONPARAMETERS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","parameters"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_Datum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractDatum"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_DATUM_PROPERTYTYPE_TYPE = build_CT_DATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_DATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_Datum_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.ABSTRACTDATUMTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","AbstractDatum"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_Ellipsoid_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:Ellipsoid"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_ELLIPSOID_PROPERTYTYPE_TYPE = build_CT_ELLIPSOID_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_ELLIPSOID_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_Ellipsoid_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.ELLIPSOIDTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","Ellipsoid"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_OperationMethod_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:OperationMethod"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_OPERATIONMETHOD_PROPERTYTYPE_TYPE = build_CT_OPERATIONMETHOD_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_OPERATIONMETHOD_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_OperationMethod_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.OPERATIONMETHODTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","OperationMethod"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_OperationParameters_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractGeneralOperationParameter"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_OPERATIONPARAMETERS_PROPERTYTYPE_TYPE = build_CT_OPERATIONPARAMETERS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_OPERATIONPARAMETERS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_OperationParameters_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.ABSTRACTGENERALOPERATIONPARAMETERTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","AbstractGeneralOperationParameter"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_Operation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractCoordinateOperation"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_OPERATION_PROPERTYTYPE_TYPE = build_CT_OPERATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_OPERATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_Operation_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.ABSTRACTCOORDINATEOPERATIONTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","AbstractCoordinateOperation"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_PrimeMeridian_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:PrimeMeridian"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_PRIMEMERIDIAN_PROPERTYTYPE_TYPE = build_CT_PRIMEMERIDIAN_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_PRIMEMERIDIAN_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_PrimeMeridian_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.PRIMEMERIDIANTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","PrimeMeridian"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_UomCatalogue_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CT_UomCatalogue"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_UOMCATALOGUE_PROPERTYTYPE_TYPE = build_CT_UOMCATALOGUE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_UOMCATALOGUE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_UomCatalogue_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_UOMCATALOGUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CT_UomCatalogue"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_UomCatalogue_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:AbstractCT_Catalogue_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="uomItem" type="gmx:UnitDefinition_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CT_UOMCATALOGUE_TYPE_TYPE = build_CT_UOMCATALOGUE_TYPE_TYPE();
    
    private static ComplexType build_CT_UOMCATALOGUE_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CT_UomCatalogue_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTCT_CATALOGUE_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        UNITDEFINITION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","uomItem"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ClAlternativeExpression_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ClAlternativeExpression"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CLALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE = build_CLALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CLALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ClAlternativeExpression_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CLALTERNATIVEEXPRESSION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ClAlternativeExpression"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ClAlternativeExpression_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DefinitionType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CLALTERNATIVEEXPRESSION_TYPE_TYPE = build_CLALTERNATIVEEXPRESSION_TYPE_TYPE();
    
    private static ComplexType build_CLALTERNATIVEEXPRESSION_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ClAlternativeExpression_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.DEFINITIONTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeAlternativeExpression_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CodeAlternativeExpression"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODEALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE = build_CODEALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CODEALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CodeAlternativeExpression_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CODEALTERNATIVEEXPRESSION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CodeAlternativeExpression"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeAlternativeExpression_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DefinitionType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODEALTERNATIVEEXPRESSION_TYPE_TYPE = build_CODEALTERNATIVEEXPRESSION_TYPE_TYPE();
    
    private static ComplexType build_CODEALTERNATIVEEXPRESSION_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CodeAlternativeExpression_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.DEFINITIONTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeDefinition_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CodeDefinition"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODEDEFINITION_PROPERTYTYPE_TYPE = build_CODEDEFINITION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CODEDEFINITION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CodeDefinition_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CODEDEFINITION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CodeDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeDefinition_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DefinitionType"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODEDEFINITION_TYPE_TYPE = build_CODEDEFINITION_TYPE_TYPE();
    
    private static ComplexType build_CODEDEFINITION_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CodeDefinition_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.DEFINITIONTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeListDictionary_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CodeListDictionary"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODELISTDICTIONARY_PROPERTYTYPE_TYPE = build_CODELISTDICTIONARY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CODELISTDICTIONARY_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CodeListDictionary_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CODELISTDICTIONARY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CodeListDictionary"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeListDictionary_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Constraints: - 1) metadataProperty.card = 0 - 2) dictionaryEntry.card = 0&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DictionaryType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="codeEntry" type="gmx:CodeDefinition_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODELISTDICTIONARY_TYPE_TYPE = build_CODELISTDICTIONARY_TYPE_TYPE();
    
    private static ComplexType build_CODELISTDICTIONARY_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CodeListDictionary_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.DICTIONARYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CODEDEFINITION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","codeEntry"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ConventionalUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:ConventionalUnit"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CONVENTIONALUNIT_PROPERTYTYPE_TYPE = build_CONVENTIONALUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CONVENTIONALUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ConventionalUnit_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.CONVENTIONALUNITTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","ConventionalUnit"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CoordinateSystemAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CoordinateSystemAlt"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COORDINATESYSTEMALT_PROPERTYTYPE_TYPE = build_COORDINATESYSTEMALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAlt_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAlt"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CoordinateSystemAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COORDINATESYSTEMALT_TYPE_TYPE = build_COORDINATESYSTEMALT_TYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMALT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAlt_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.IDENTIFIEDOBJECTTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.AGGREGATIONTYPE_TYPE,
                        new NameImpl("aggregationType"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CoordinateSystemAxisAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CoordinateSystemAxisAlt"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COORDINATESYSTEMAXISALT_PROPERTYTYPE_TYPE = build_COORDINATESYSTEMAXISALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMAXISALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAxisAlt_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMAXISALT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAxisAlt"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CoordinateSystemAxisAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:CoordinateSystemAxisType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType COORDINATESYSTEMAXISALT_TYPE_TYPE = build_COORDINATESYSTEMAXISALT_TYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMAXISALT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAxisAlt_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.COORDINATESYSTEMAXISTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CrsAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CrsAlt"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CRSALT_PROPERTYTYPE_TYPE = build_CRSALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CRSALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CrsAlt_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CRSALT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","CrsAlt"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CrsAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:AbstractCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CRSALT_TYPE_TYPE = build_CRSALT_TYPE_TYPE();
    
    private static ComplexType build_CRSALT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","CrsAlt_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.ABSTRACTCRSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DatumAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:DatumAlt"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DATUMALT_PROPERTYTYPE_TYPE = build_DATUMALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DATUMALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","DatumAlt_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DATUMALT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","DatumAlt"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DatumAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:AbstractDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DATUMALT_TYPE_TYPE = build_DATUMALT_TYPE_TYPE();
    
    private static ComplexType build_DATUMALT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","DatumAlt_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.ABSTRACTDATUMTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DerivedUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:DerivedUnit"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DERIVEDUNIT_PROPERTYTYPE_TYPE = build_DERIVEDUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DERIVEDUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","DerivedUnit_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.DERIVEDUNITTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","DerivedUnit"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EllipsoidAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:EllipsoidAlt"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ELLIPSOIDALT_PROPERTYTYPE_TYPE = build_ELLIPSOIDALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ELLIPSOIDALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","EllipsoidAlt_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ELLIPSOIDALT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","EllipsoidAlt"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EllipsoidAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ELLIPSOIDALT_TYPE_TYPE = build_ELLIPSOIDALT_TYPE_TYPE();
    
    private static ComplexType build_ELLIPSOIDALT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","EllipsoidAlt_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.IDENTIFIEDOBJECTTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="FileName_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:FileName"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType FILENAME_PROPERTYTYPE_TYPE = build_FILENAME_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_FILENAME_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","FileName_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        FILENAME_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","FileName"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="FileName_Type"&gt;
     *      &lt;xs:simpleContent&gt;
     *          &lt;xs:extension base="xs:string"&gt;
     *              &lt;xs:attribute name="src" type="xs:anyURI"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:simpleContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType FILENAME_TYPE_TYPE = build_FILENAME_TYPE_TYPE();
    
    private static ComplexType build_FILENAME_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","FileName_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.STRING_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("src"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_AffineCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_AffineCS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_AFFINECS_PROPERTYTYPE_TYPE = build_ML_AFFINECS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_AFFINECS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_AffineCS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_AFFINECS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_AffineCS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_AffineCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:AffineCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_AFFINECS_TYPE_TYPE = build_ML_AFFINECS_TYPE_TYPE();
    
    private static ComplexType build_ML_AFFINECS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_AffineCS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.AFFINECSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_BaseUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_BaseUnit"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_BASEUNIT_PROPERTYTYPE_TYPE = build_ML_BASEUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_BASEUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_BaseUnit_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_BASEUNIT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_BaseUnit"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_BaseUnit_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:BaseUnitType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:UomAlternativeExpression_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_BASEUNIT_TYPE_TYPE = build_ML_BASEUNIT_TYPE_TYPE();
    
    private static ComplexType build_ML_BASEUNIT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_BaseUnit_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.BASEUNITTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CartesianCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CartesianCS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CARTESIANCS_PROPERTYTYPE_TYPE = build_ML_CARTESIANCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CARTESIANCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CartesianCS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_CARTESIANCS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_CartesianCS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CartesianCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:CartesianCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CARTESIANCS_TYPE_TYPE = build_ML_CARTESIANCS_TYPE_TYPE();
    
    private static ComplexType build_ML_CARTESIANCS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CartesianCS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.CARTESIANCSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CodeDefinition_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CodeDefinition"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CODEDEFINITION_PROPERTYTYPE_TYPE = build_ML_CODEDEFINITION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CODEDEFINITION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeDefinition_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_CODEDEFINITION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CodeDefinition_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:CodeDefinition_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CodeAlternativeExpression_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CODEDEFINITION_TYPE_TYPE = build_ML_CODEDEFINITION_TYPE_TYPE();
    
    private static ComplexType build_ML_CODEDEFINITION_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeDefinition_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return CODEDEFINITION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CODEALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CodeListDictionary_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CodeListDictionary"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CODELISTDICTIONARY_PROPERTYTYPE_TYPE = build_ML_CODELISTDICTIONARY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CODELISTDICTIONARY_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeListDictionary_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_CODELISTDICTIONARY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeListDictionary"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CodeListDictionary_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Constraint: codeEntry.type = ML_CodeListDefinition&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:CodeListDictionary_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:ClAlternativeExpression_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CODELISTDICTIONARY_TYPE_TYPE = build_ML_CODELISTDICTIONARY_TYPE_TYPE();
    
    private static ComplexType build_ML_CODELISTDICTIONARY_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeListDictionary_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return CODELISTDICTIONARY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CLALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CompoundCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CompoundCRS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_COMPOUNDCRS_PROPERTYTYPE_TYPE = build_ML_COMPOUNDCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_COMPOUNDCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CompoundCRS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_COMPOUNDCRS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_CompoundCRS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CompoundCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:CompoundCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_COMPOUNDCRS_TYPE_TYPE = build_ML_COMPOUNDCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_COMPOUNDCRS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CompoundCRS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.COMPOUNDCRSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CRSALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ConcatenatedOperation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_ConcatenatedOperation"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CONCATENATEDOPERATION_PROPERTYTYPE_TYPE = build_ML_CONCATENATEDOPERATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CONCATENATEDOPERATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConcatenatedOperation_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_CONCATENATEDOPERATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConcatenatedOperation"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ConcatenatedOperation_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ConcatenatedOperationType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CONCATENATEDOPERATION_TYPE_TYPE = build_ML_CONCATENATEDOPERATION_TYPE_TYPE();
    
    private static ComplexType build_ML_CONCATENATEDOPERATION_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConcatenatedOperation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.CONCATENATEDOPERATIONTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        OPERATIONALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ConventionalUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_ConventionalUnit"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CONVENTIONALUNIT_PROPERTYTYPE_TYPE = build_ML_CONVENTIONALUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CONVENTIONALUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConventionalUnit_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_CONVENTIONALUNIT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConventionalUnit"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ConventionalUnit_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ConventionalUnitType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:UomAlternativeExpression_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CONVENTIONALUNIT_TYPE_TYPE = build_ML_CONVENTIONALUNIT_TYPE_TYPE();
    
    private static ComplexType build_ML_CONVENTIONALUNIT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConventionalUnit_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.CONVENTIONALUNITTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Conversion_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_Conversion"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CONVERSION_PROPERTYTYPE_TYPE = build_ML_CONVERSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CONVERSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_Conversion_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_CONVERSION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_Conversion"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Conversion_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ConversionType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CONVERSION_TYPE_TYPE = build_ML_CONVERSION_TYPE_TYPE();
    
    private static ComplexType build_ML_CONVERSION_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_Conversion_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.CONVERSIONTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        OPERATIONALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CoordinateSystemAxis_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CoordinateSystemAxis"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE = build_ML_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CoordinateSystemAxis_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_COORDINATESYSTEMAXIS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_CoordinateSystemAxis"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CoordinateSystemAxis_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:CoordinateSystemAxisType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAxisAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_COORDINATESYSTEMAXIS_TYPE_TYPE = build_ML_COORDINATESYSTEMAXIS_TYPE_TYPE();
    
    private static ComplexType build_ML_COORDINATESYSTEMAXIS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CoordinateSystemAxis_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.COORDINATESYSTEMAXISTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMAXISALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CylindricalCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CylindricalCS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CYLINDRICALCS_PROPERTYTYPE_TYPE = build_ML_CYLINDRICALCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CYLINDRICALCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CylindricalCS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_CYLINDRICALCS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_CylindricalCS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CylindricalCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:CylindricalCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_CYLINDRICALCS_TYPE_TYPE = build_ML_CYLINDRICALCS_TYPE_TYPE();
    
    private static ComplexType build_ML_CYLINDRICALCS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_CylindricalCS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.CYLINDRICALCSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_DerivedCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_DerivedCRS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_DERIVEDCRS_PROPERTYTYPE_TYPE = build_ML_DERIVEDCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_DERIVEDCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedCRS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_DERIVEDCRS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedCRS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_DerivedCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DerivedCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_DERIVEDCRS_TYPE_TYPE = build_ML_DERIVEDCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_DERIVEDCRS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedCRS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.DERIVEDCRSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CRSALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_DerivedUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_DerivedUnit"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_DERIVEDUNIT_PROPERTYTYPE_TYPE = build_ML_DERIVEDUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_DERIVEDUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedUnit_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_DERIVEDUNIT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedUnit"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_DerivedUnit_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DerivedUnitType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:UomAlternativeExpression_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_DERIVEDUNIT_TYPE_TYPE = build_ML_DERIVEDUNIT_TYPE_TYPE();
    
    private static ComplexType build_ML_DERIVEDUNIT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedUnit_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.DERIVEDUNITTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Ellipsoid_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_Ellipsoid"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_ELLIPSOID_PROPERTYTYPE_TYPE = build_ML_ELLIPSOID_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_ELLIPSOID_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_Ellipsoid_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_ELLIPSOID_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_Ellipsoid"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Ellipsoid_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:EllipsoidType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:EllipsoidAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_ELLIPSOID_TYPE_TYPE = build_ML_ELLIPSOID_TYPE_TYPE();
    
    private static ComplexType build_ML_ELLIPSOID_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_Ellipsoid_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.ELLIPSOIDTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ELLIPSOIDALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EllipsoidalCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_EllipsoidalCS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_ELLIPSOIDALCS_PROPERTYTYPE_TYPE = build_ML_ELLIPSOIDALCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_ELLIPSOIDALCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_EllipsoidalCS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_ELLIPSOIDALCS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_EllipsoidalCS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EllipsoidalCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:EllipsoidalCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_ELLIPSOIDALCS_TYPE_TYPE = build_ML_ELLIPSOIDALCS_TYPE_TYPE();
    
    private static ComplexType build_ML_ELLIPSOIDALCS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_EllipsoidalCS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.ELLIPSOIDALCSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EngineeringCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_EngineeringCRS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_ENGINEERINGCRS_PROPERTYTYPE_TYPE = build_ML_ENGINEERINGCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_ENGINEERINGCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringCRS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_ENGINEERINGCRS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringCRS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EngineeringCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:EngineeringCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_ENGINEERINGCRS_TYPE_TYPE = build_ML_ENGINEERINGCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_ENGINEERINGCRS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringCRS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.ENGINEERINGCRSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CRSALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EngineeringDatum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_EngineeringDatum"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_ENGINEERINGDATUM_PROPERTYTYPE_TYPE = build_ML_ENGINEERINGDATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_ENGINEERINGDATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringDatum_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_ENGINEERINGDATUM_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringDatum"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EngineeringDatum_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:EngineeringDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:DatumAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_ENGINEERINGDATUM_TYPE_TYPE = build_ML_ENGINEERINGDATUM_TYPE_TYPE();
    
    private static ComplexType build_ML_ENGINEERINGDATUM_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringDatum_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.ENGINEERINGDATUMTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DATUMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_GeodeticCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_GeodeticCRS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_GEODETICCRS_PROPERTYTYPE_TYPE = build_ML_GEODETICCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_GEODETICCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticCRS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_GEODETICCRS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticCRS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_GeodeticCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:GeodeticCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_GEODETICCRS_TYPE_TYPE = build_ML_GEODETICCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_GEODETICCRS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticCRS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.GEODETICCRSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CRSALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_GeodeticDatum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_GeodeticDatum"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_GEODETICDATUM_PROPERTYTYPE_TYPE = build_ML_GEODETICDATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_GEODETICDATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticDatum_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_GEODETICDATUM_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticDatum"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_GeodeticDatum_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:GeodeticDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:DatumAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_GEODETICDATUM_TYPE_TYPE = build_ML_GEODETICDATUM_TYPE_TYPE();
    
    private static ComplexType build_ML_GEODETICDATUM_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticDatum_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.GEODETICDATUMTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DATUMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ImageCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_ImageCRS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_IMAGECRS_PROPERTYTYPE_TYPE = build_ML_IMAGECRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_IMAGECRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageCRS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_IMAGECRS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageCRS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ImageCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ImageCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_IMAGECRS_TYPE_TYPE = build_ML_IMAGECRS_TYPE_TYPE();
    
    private static ComplexType build_ML_IMAGECRS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageCRS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.IMAGECRSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CRSALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ImageDatum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_ImageDatum"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_IMAGEDATUM_PROPERTYTYPE_TYPE = build_ML_IMAGEDATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_IMAGEDATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageDatum_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_IMAGEDATUM_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageDatum"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ImageDatum_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ImageDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:DatumAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_IMAGEDATUM_TYPE_TYPE = build_ML_IMAGEDATUM_TYPE_TYPE();
    
    private static ComplexType build_ML_IMAGEDATUM_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageDatum_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.IMAGEDATUMTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DATUMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_LinearCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_LinearCS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_LINEARCS_PROPERTYTYPE_TYPE = build_ML_LINEARCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_LINEARCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_LinearCS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_LINEARCS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_LinearCS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_LinearCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:LinearCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_LINEARCS_TYPE_TYPE = build_ML_LINEARCS_TYPE_TYPE();
    
    private static ComplexType build_ML_LINEARCS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_LinearCS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.LINEARCSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationMethod_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_OperationMethod"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_OPERATIONMETHOD_PROPERTYTYPE_TYPE = build_ML_OPERATIONMETHOD_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONMETHOD_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationMethod_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_OPERATIONMETHOD_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationMethod"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationMethod_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:OperationMethodType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationMethodAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_OPERATIONMETHOD_TYPE_TYPE = build_ML_OPERATIONMETHOD_TYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONMETHOD_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationMethod_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.OPERATIONMETHODTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        OPERATIONMETHODALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationParameterGroup_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_OperationParameterGroup"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_OPERATIONPARAMETERGROUP_PROPERTYTYPE_TYPE = build_ML_OPERATIONPARAMETERGROUP_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONPARAMETERGROUP_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameterGroup_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_OPERATIONPARAMETERGROUP_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameterGroup"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationParameterGroup_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:OperationParameterGroupType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationParameterAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_OPERATIONPARAMETERGROUP_TYPE_TYPE = build_ML_OPERATIONPARAMETERGROUP_TYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONPARAMETERGROUP_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameterGroup_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.OPERATIONPARAMETERGROUPTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        OPERATIONPARAMETERALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationParameter_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_OperationParameter"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_OPERATIONPARAMETER_PROPERTYTYPE_TYPE = build_ML_OPERATIONPARAMETER_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONPARAMETER_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameter_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_OPERATIONPARAMETER_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameter"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationParameter_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:OperationParameterType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationParameterAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_OPERATIONPARAMETER_TYPE_TYPE = build_ML_OPERATIONPARAMETER_TYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONPARAMETER_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameter_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.OPERATIONPARAMETERTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        OPERATIONPARAMETERALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PassThroughOperation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_PassThroughOperation"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_PASSTHROUGHOPERATION_PROPERTYTYPE_TYPE = build_ML_PASSTHROUGHOPERATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_PASSTHROUGHOPERATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_PassThroughOperation_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_PASSTHROUGHOPERATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_PassThroughOperation"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PassThroughOperation_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:PassThroughOperationType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_PASSTHROUGHOPERATION_TYPE_TYPE = build_ML_PASSTHROUGHOPERATION_TYPE_TYPE();
    
    private static ComplexType build_ML_PASSTHROUGHOPERATION_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_PassThroughOperation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.PASSTHROUGHOPERATIONTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        OPERATIONALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PolarCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_PolarCS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_POLARCS_PROPERTYTYPE_TYPE = build_ML_POLARCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_POLARCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_PolarCS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_POLARCS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_PolarCS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PolarCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:PolarCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_POLARCS_TYPE_TYPE = build_ML_POLARCS_TYPE_TYPE();
    
    private static ComplexType build_ML_POLARCS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_PolarCS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.POLARCSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PrimeMeridian_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_PrimeMeridian"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_PRIMEMERIDIAN_PROPERTYTYPE_TYPE = build_ML_PRIMEMERIDIAN_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_PRIMEMERIDIAN_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_PrimeMeridian_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_PRIMEMERIDIAN_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_PrimeMeridian"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PrimeMeridian_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:PrimeMeridianType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:PrimeMeridianAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_PRIMEMERIDIAN_TYPE_TYPE = build_ML_PRIMEMERIDIAN_TYPE_TYPE();
    
    private static ComplexType build_ML_PRIMEMERIDIAN_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_PrimeMeridian_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.PRIMEMERIDIANTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        PRIMEMERIDIANALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ProjectedCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_ProjectedCRS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_PROJECTEDCRS_PROPERTYTYPE_TYPE = build_ML_PROJECTEDCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_PROJECTEDCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_ProjectedCRS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_PROJECTEDCRS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_ProjectedCRS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ProjectedCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ProjectedCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_PROJECTEDCRS_TYPE_TYPE = build_ML_PROJECTEDCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_PROJECTEDCRS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_ProjectedCRS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.PROJECTEDCRSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CRSALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_SphericalCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_SphericalCS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_SPHERICALCS_PROPERTYTYPE_TYPE = build_ML_SPHERICALCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_SPHERICALCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_SphericalCS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_SPHERICALCS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_SphericalCS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_SphericalCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:SphericalCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_SPHERICALCS_TYPE_TYPE = build_ML_SPHERICALCS_TYPE_TYPE();
    
    private static ComplexType build_ML_SPHERICALCS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_SphericalCS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.SPHERICALCSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TemporalCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_TemporalCRS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_TEMPORALCRS_PROPERTYTYPE_TYPE = build_ML_TEMPORALCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_TEMPORALCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalCRS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_TEMPORALCRS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalCRS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TemporalCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:TemporalCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_TEMPORALCRS_TYPE_TYPE = build_ML_TEMPORALCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_TEMPORALCRS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalCRS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.TEMPORALCRSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CRSALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TemporalDatum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_TemporalDatum"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_TEMPORALDATUM_PROPERTYTYPE_TYPE = build_ML_TEMPORALDATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_TEMPORALDATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalDatum_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_TEMPORALDATUM_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalDatum"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TemporalDatum_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:TemporalDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:DatumAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_TEMPORALDATUM_TYPE_TYPE = build_ML_TEMPORALDATUM_TYPE_TYPE();
    
    private static ComplexType build_ML_TEMPORALDATUM_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalDatum_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.TEMPORALDATUMTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DATUMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TimeCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_TimeCS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_TIMECS_PROPERTYTYPE_TYPE = build_ML_TIMECS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_TIMECS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_TimeCS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_TIMECS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_TimeCS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TimeCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:TimeCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_TIMECS_TYPE_TYPE = build_ML_TIMECS_TYPE_TYPE();
    
    private static ComplexType build_ML_TIMECS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_TimeCS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.TIMECSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Transformation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_Transformation"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_TRANSFORMATION_PROPERTYTYPE_TYPE = build_ML_TRANSFORMATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_TRANSFORMATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_Transformation_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_TRANSFORMATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_Transformation"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Transformation_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:TransformationType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_TRANSFORMATION_TYPE_TYPE = build_ML_TRANSFORMATION_TYPE_TYPE();
    
    private static ComplexType build_ML_TRANSFORMATION_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_Transformation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.TRANSFORMATIONTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        OPERATIONALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_UnitDefinition_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_UnitDefinition"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_UNITDEFINITION_PROPERTYTYPE_TYPE = build_ML_UNITDEFINITION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_UNITDEFINITION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_UnitDefinition_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_UNITDEFINITION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_UnitDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_UnitDefinition_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:UnitDefinitionType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:UomAlternativeExpression_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_UNITDEFINITION_TYPE_TYPE = build_ML_UNITDEFINITION_TYPE_TYPE();
    
    private static ComplexType build_ML_UNITDEFINITION_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_UnitDefinition_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.UNITDEFINITIONTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_UserDefinedCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_UserDefinedCS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_USERDEFINEDCS_PROPERTYTYPE_TYPE = build_ML_USERDEFINEDCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_USERDEFINEDCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_UserDefinedCS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_USERDEFINEDCS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_UserDefinedCS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_UserDefinedCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:UserDefinedCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_USERDEFINEDCS_TYPE_TYPE = build_ML_USERDEFINEDCS_TYPE_TYPE();
    
    private static ComplexType build_ML_USERDEFINEDCS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_UserDefinedCS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.USERDEFINEDCSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_VerticalCRS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_VERTICALCRS_PROPERTYTYPE_TYPE = build_ML_VERTICALCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCRS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_VERTICALCRS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCRS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:VerticalCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_VERTICALCRS_TYPE_TYPE = build_ML_VERTICALCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALCRS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCRS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.VERTICALCRSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CRSALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_VerticalCS"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_VERTICALCS_PROPERTYTYPE_TYPE = build_ML_VERTICALCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCS_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_VERTICALCS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCS"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:VerticalCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_VERTICALCS_TYPE_TYPE = build_ML_VERTICALCS_TYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALCS_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCS_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.VERTICALCSTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COORDINATESYSTEMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalDatum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_VerticalDatum"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_VERTICALDATUM_PROPERTYTYPE_TYPE = build_ML_VERTICALDATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALDATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalDatum_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ML_VERTICALDATUM_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalDatum"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalDatum_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:VerticalDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:DatumAlt_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ML_VERTICALDATUM_TYPE_TYPE = build_ML_VERTICALDATUM_TYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALDATUM_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalDatum_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.VERTICALDATUMTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DATUMALT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","alternativeExpression"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_Aggregate_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MX_Aggregate"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_AGGREGATE_PROPERTYTYPE_TYPE = build_MX_AGGREGATE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_AGGREGATE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MX_Aggregate_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MX_AGGREGATE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","MX_Aggregate"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_Aggregate_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDS_Aggregate_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="aggregateCatalogue" type="gmx:CT_Catalogue_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="aggregateFile" type="gmx:MX_SupportFile_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_AGGREGATE_TYPE_TYPE = build_MX_AGGREGATE_TYPE_TYPE();
    
    private static ComplexType build_MX_AGGREGATE_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MX_Aggregate_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMDSchema.ABSTRACTDS_AGGREGATE_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_CATALOGUE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","aggregateCatalogue"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MX_SUPPORTFILE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","aggregateFile"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_DataFile_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MX_DataFile"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_DATAFILE_PROPERTYTYPE_TYPE = build_MX_DATAFILE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_DATAFILE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataFile_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MX_DATAFILE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataFile"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_DataFile_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:AbstractMX_File_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="featureTypes" type="gco:GenericName_PropertyType"/&gt;
     *                  &lt;xs:element name="fileFormat" type="gmd:MD_Format_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_DATAFILE_TYPE_TYPE = build_MX_DATAFILE_TYPE_TYPE();
    
    private static ComplexType build_MX_DATAFILE_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataFile_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTMX_FILE_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.GENERICNAME_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","featureTypes"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.MD_FORMAT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","fileFormat"),
                        1, 1, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_DataSet_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MX_DataSet"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_DATASET_PROPERTYTYPE_TYPE = build_MX_DATASET_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_DATASET_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataSet_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MX_DATASET_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataSet"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_DataSet_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:DS_DataSet_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="dataFile" type="gmx:MX_DataFile_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="datasetCatalogue" type="gmx:CT_Catalogue_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="supportFile" type="gmx:MX_SupportFile_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_DATASET_TYPE_TYPE = build_MX_DATASET_TYPE_TYPE();
    
    private static ComplexType build_MX_DATASET_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataSet_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMDSchema.DS_DATASET_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MX_DATAFILE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","dataFile"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CT_CATALOGUE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","datasetCatalogue"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MX_SUPPORTFILE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","supportFile"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_File_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:AbstractMX_File"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_FILE_PROPERTYTYPE_TYPE = build_MX_FILE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_FILE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MX_File_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        ABSTRACTMX_FILE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","AbstractMX_File"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_ScopeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MX_ScopeCode"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_SCOPECODE_PROPERTYTYPE_TYPE = build_MX_SCOPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_SCOPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MX_ScopeCode_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","MX_ScopeCode"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_SupportFile_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MX_SupportFile"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_SUPPORTFILE_PROPERTYTYPE_TYPE = build_MX_SUPPORTFILE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_SUPPORTFILE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MX_SupportFile_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MX_SUPPORTFILE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","MX_SupportFile"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_SupportFile_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:AbstractMX_File_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_SUPPORTFILE_TYPE_TYPE = build_MX_SUPPORTFILE_TYPE_TYPE();
    
    private static ComplexType build_MX_SUPPORTFILE_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MX_SupportFile_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTMX_FILE_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MimeFileType_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MimeFileType"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MIMEFILETYPE_PROPERTYTYPE_TYPE = build_MIMEFILETYPE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MIMEFILETYPE_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MimeFileType_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MIMEFILETYPE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","MimeFileType"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MimeFileType_Type"&gt;
     *      &lt;xs:simpleContent&gt;
     *          &lt;xs:extension base="xs:string"&gt;
     *              &lt;xs:attribute name="type" type="xs:string" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:simpleContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MIMEFILETYPE_TYPE_TYPE = build_MIMEFILETYPE_TYPE_TYPE();
    
    private static ComplexType build_MIMEFILETYPE_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","MimeFileType_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.STRING_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("type"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:OperationAlt"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONALT_PROPERTYTYPE_TYPE = build_OPERATIONALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_OPERATIONALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","OperationAlt_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        OPERATIONALT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","OperationAlt"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:AbstractCoordinateOperationType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONALT_TYPE_TYPE = build_OPERATIONALT_TYPE_TYPE();
    
    private static ComplexType build_OPERATIONALT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","OperationAlt_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.ABSTRACTCOORDINATEOPERATIONTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationMethodAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:OperationMethodAlt"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONMETHODALT_PROPERTYTYPE_TYPE = build_OPERATIONMETHODALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_OPERATIONMETHODALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","OperationMethodAlt_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        OPERATIONMETHODALT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","OperationMethodAlt"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationMethodAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONMETHODALT_TYPE_TYPE = build_OPERATIONMETHODALT_TYPE_TYPE();
    
    private static ComplexType build_OPERATIONMETHODALT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","OperationMethodAlt_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.IDENTIFIEDOBJECTTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationParameterAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:OperationParameterAlt"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONPARAMETERALT_PROPERTYTYPE_TYPE = build_OPERATIONPARAMETERALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_OPERATIONPARAMETERALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","OperationParameterAlt_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        OPERATIONPARAMETERALT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","OperationParameterAlt"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationParameterAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:OperationParameterType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType OPERATIONPARAMETERALT_TYPE_TYPE = build_OPERATIONPARAMETERALT_TYPE_TYPE();
    
    private static ComplexType build_OPERATIONPARAMETERALT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","OperationParameterAlt_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.OPERATIONPARAMETERTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="PrimeMeridianAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:PrimeMeridianAlt"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PRIMEMERIDIANALT_PROPERTYTYPE_TYPE = build_PRIMEMERIDIANALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_PRIMEMERIDIANALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","PrimeMeridianAlt_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        PRIMEMERIDIANALT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","PrimeMeridianAlt"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="PrimeMeridianAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PRIMEMERIDIANALT_TYPE_TYPE = build_PRIMEMERIDIANALT_TYPE_TYPE();
    
    private static ComplexType build_PRIMEMERIDIANALT_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","PrimeMeridianAlt_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.IDENTIFIEDOBJECTTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UnitDefinition_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:UnitDefinition"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType UNITDEFINITION_PROPERTYTYPE_TYPE = build_UNITDEFINITION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UNITDEFINITION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","UnitDefinition_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.UNITDEFINITIONTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomAlternativeExpression_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:UomAlternativeExpression"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE = build_UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","UomAlternativeExpression_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        UOMALTERNATIVEEXPRESSION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","UomAlternativeExpression"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomAlternativeExpression_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:UnitDefinitionType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType UOMALTERNATIVEEXPRESSION_TYPE_TYPE = build_UOMALTERNATIVEEXPRESSION_TYPE_TYPE();
    
    private static ComplexType build_UOMALTERNATIVEEXPRESSION_TYPE_TYPE() {
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmx","UomAlternativeExpression_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GMLSchema.UNITDEFINITIONTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMDSchema.PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmx","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }


    public GMXSchema() {
        super("http://www.isotc211.org/2005/gmx");
        put(ABSTRACTCT_CATALOGUE_TYPE_TYPE);
        put(ABSTRACTMX_FILE_TYPE_TYPE);
        put(ANCHOR_PROPERTYTYPE_TYPE);
        put(ANCHOR_TYPE_TYPE);
        put(BASEUNIT_PROPERTYTYPE_TYPE);
        put(CT_CRS_PROPERTYTYPE_TYPE);
        put(CT_CATALOGUE_PROPERTYTYPE_TYPE);
        put(CT_CODELISTCATALOGUE_PROPERTYTYPE_TYPE);
        put(CT_CODELISTCATALOGUE_TYPE_TYPE);
        put(CT_CODELISTVALUE_PROPERTYTYPE_TYPE);
        put(CT_CODELIST_PROPERTYTYPE_TYPE);
        put(CT_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE);
        put(CT_COORDINATESYSTEM_PROPERTYTYPE_TYPE);
        put(CT_CRSCATALOGUE_PROPERTYTYPE_TYPE);
        put(CT_CRSCATALOGUE_TYPE_TYPE);
        put(CT_DATUM_PROPERTYTYPE_TYPE);
        put(CT_ELLIPSOID_PROPERTYTYPE_TYPE);
        put(CT_OPERATIONMETHOD_PROPERTYTYPE_TYPE);
        put(CT_OPERATIONPARAMETERS_PROPERTYTYPE_TYPE);
        put(CT_OPERATION_PROPERTYTYPE_TYPE);
        put(CT_PRIMEMERIDIAN_PROPERTYTYPE_TYPE);
        put(CT_UOMCATALOGUE_PROPERTYTYPE_TYPE);
        put(CT_UOMCATALOGUE_TYPE_TYPE);
        put(CLALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE);
        put(CLALTERNATIVEEXPRESSION_TYPE_TYPE);
        put(CODEALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE);
        put(CODEALTERNATIVEEXPRESSION_TYPE_TYPE);
        put(CODEDEFINITION_PROPERTYTYPE_TYPE);
        put(CODEDEFINITION_TYPE_TYPE);
        put(CODELISTDICTIONARY_PROPERTYTYPE_TYPE);
        put(CODELISTDICTIONARY_TYPE_TYPE);
        put(CONVENTIONALUNIT_PROPERTYTYPE_TYPE);
        put(COORDINATESYSTEMALT_PROPERTYTYPE_TYPE);
        put(COORDINATESYSTEMALT_TYPE_TYPE);
        put(COORDINATESYSTEMAXISALT_PROPERTYTYPE_TYPE);
        put(COORDINATESYSTEMAXISALT_TYPE_TYPE);
        put(CRSALT_PROPERTYTYPE_TYPE);
        put(CRSALT_TYPE_TYPE);
        put(DATUMALT_PROPERTYTYPE_TYPE);
        put(DATUMALT_TYPE_TYPE);
        put(DERIVEDUNIT_PROPERTYTYPE_TYPE);
        put(ELLIPSOIDALT_PROPERTYTYPE_TYPE);
        put(ELLIPSOIDALT_TYPE_TYPE);
        put(FILENAME_PROPERTYTYPE_TYPE);
        put(FILENAME_TYPE_TYPE);
        put(ML_AFFINECS_PROPERTYTYPE_TYPE);
        put(ML_AFFINECS_TYPE_TYPE);
        put(ML_BASEUNIT_PROPERTYTYPE_TYPE);
        put(ML_BASEUNIT_TYPE_TYPE);
        put(ML_CARTESIANCS_PROPERTYTYPE_TYPE);
        put(ML_CARTESIANCS_TYPE_TYPE);
        put(ML_CODEDEFINITION_PROPERTYTYPE_TYPE);
        put(ML_CODEDEFINITION_TYPE_TYPE);
        put(ML_CODELISTDICTIONARY_PROPERTYTYPE_TYPE);
        put(ML_CODELISTDICTIONARY_TYPE_TYPE);
        put(ML_COMPOUNDCRS_PROPERTYTYPE_TYPE);
        put(ML_COMPOUNDCRS_TYPE_TYPE);
        put(ML_CONCATENATEDOPERATION_PROPERTYTYPE_TYPE);
        put(ML_CONCATENATEDOPERATION_TYPE_TYPE);
        put(ML_CONVENTIONALUNIT_PROPERTYTYPE_TYPE);
        put(ML_CONVENTIONALUNIT_TYPE_TYPE);
        put(ML_CONVERSION_PROPERTYTYPE_TYPE);
        put(ML_CONVERSION_TYPE_TYPE);
        put(ML_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE);
        put(ML_COORDINATESYSTEMAXIS_TYPE_TYPE);
        put(ML_CYLINDRICALCS_PROPERTYTYPE_TYPE);
        put(ML_CYLINDRICALCS_TYPE_TYPE);
        put(ML_DERIVEDCRS_PROPERTYTYPE_TYPE);
        put(ML_DERIVEDCRS_TYPE_TYPE);
        put(ML_DERIVEDUNIT_PROPERTYTYPE_TYPE);
        put(ML_DERIVEDUNIT_TYPE_TYPE);
        put(ML_ELLIPSOID_PROPERTYTYPE_TYPE);
        put(ML_ELLIPSOID_TYPE_TYPE);
        put(ML_ELLIPSOIDALCS_PROPERTYTYPE_TYPE);
        put(ML_ELLIPSOIDALCS_TYPE_TYPE);
        put(ML_ENGINEERINGCRS_PROPERTYTYPE_TYPE);
        put(ML_ENGINEERINGCRS_TYPE_TYPE);
        put(ML_ENGINEERINGDATUM_PROPERTYTYPE_TYPE);
        put(ML_ENGINEERINGDATUM_TYPE_TYPE);
        put(ML_GEODETICCRS_PROPERTYTYPE_TYPE);
        put(ML_GEODETICCRS_TYPE_TYPE);
        put(ML_GEODETICDATUM_PROPERTYTYPE_TYPE);
        put(ML_GEODETICDATUM_TYPE_TYPE);
        put(ML_IMAGECRS_PROPERTYTYPE_TYPE);
        put(ML_IMAGECRS_TYPE_TYPE);
        put(ML_IMAGEDATUM_PROPERTYTYPE_TYPE);
        put(ML_IMAGEDATUM_TYPE_TYPE);
        put(ML_LINEARCS_PROPERTYTYPE_TYPE);
        put(ML_LINEARCS_TYPE_TYPE);
        put(ML_OPERATIONMETHOD_PROPERTYTYPE_TYPE);
        put(ML_OPERATIONMETHOD_TYPE_TYPE);
        put(ML_OPERATIONPARAMETERGROUP_PROPERTYTYPE_TYPE);
        put(ML_OPERATIONPARAMETERGROUP_TYPE_TYPE);
        put(ML_OPERATIONPARAMETER_PROPERTYTYPE_TYPE);
        put(ML_OPERATIONPARAMETER_TYPE_TYPE);
        put(ML_PASSTHROUGHOPERATION_PROPERTYTYPE_TYPE);
        put(ML_PASSTHROUGHOPERATION_TYPE_TYPE);
        put(ML_POLARCS_PROPERTYTYPE_TYPE);
        put(ML_POLARCS_TYPE_TYPE);
        put(ML_PRIMEMERIDIAN_PROPERTYTYPE_TYPE);
        put(ML_PRIMEMERIDIAN_TYPE_TYPE);
        put(ML_PROJECTEDCRS_PROPERTYTYPE_TYPE);
        put(ML_PROJECTEDCRS_TYPE_TYPE);
        put(ML_SPHERICALCS_PROPERTYTYPE_TYPE);
        put(ML_SPHERICALCS_TYPE_TYPE);
        put(ML_TEMPORALCRS_PROPERTYTYPE_TYPE);
        put(ML_TEMPORALCRS_TYPE_TYPE);
        put(ML_TEMPORALDATUM_PROPERTYTYPE_TYPE);
        put(ML_TEMPORALDATUM_TYPE_TYPE);
        put(ML_TIMECS_PROPERTYTYPE_TYPE);
        put(ML_TIMECS_TYPE_TYPE);
        put(ML_TRANSFORMATION_PROPERTYTYPE_TYPE);
        put(ML_TRANSFORMATION_TYPE_TYPE);
        put(ML_UNITDEFINITION_PROPERTYTYPE_TYPE);
        put(ML_UNITDEFINITION_TYPE_TYPE);
        put(ML_USERDEFINEDCS_PROPERTYTYPE_TYPE);
        put(ML_USERDEFINEDCS_TYPE_TYPE);
        put(ML_VERTICALCRS_PROPERTYTYPE_TYPE);
        put(ML_VERTICALCRS_TYPE_TYPE);
        put(ML_VERTICALCS_PROPERTYTYPE_TYPE);
        put(ML_VERTICALCS_TYPE_TYPE);
        put(ML_VERTICALDATUM_PROPERTYTYPE_TYPE);
        put(ML_VERTICALDATUM_TYPE_TYPE);
        put(MX_AGGREGATE_PROPERTYTYPE_TYPE);
        put(MX_AGGREGATE_TYPE_TYPE);
        put(MX_DATAFILE_PROPERTYTYPE_TYPE);
        put(MX_DATAFILE_TYPE_TYPE);
        put(MX_DATASET_PROPERTYTYPE_TYPE);
        put(MX_DATASET_TYPE_TYPE);
        put(MX_FILE_PROPERTYTYPE_TYPE);
        put(MX_SCOPECODE_PROPERTYTYPE_TYPE);
        put(MX_SUPPORTFILE_PROPERTYTYPE_TYPE);
        put(MX_SUPPORTFILE_TYPE_TYPE);
        put(MIMEFILETYPE_PROPERTYTYPE_TYPE);
        put(MIMEFILETYPE_TYPE_TYPE);
        put(OPERATIONALT_PROPERTYTYPE_TYPE);
        put(OPERATIONALT_TYPE_TYPE);
        put(OPERATIONMETHODALT_PROPERTYTYPE_TYPE);
        put(OPERATIONMETHODALT_TYPE_TYPE);
        put(OPERATIONPARAMETERALT_PROPERTYTYPE_TYPE);
        put(OPERATIONPARAMETERALT_TYPE_TYPE);
        put(PRIMEMERIDIANALT_PROPERTYTYPE_TYPE);
        put(PRIMEMERIDIANALT_TYPE_TYPE);
        put(UNITDEFINITION_PROPERTYTYPE_TYPE);
        put(UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE);
        put(UOMALTERNATIVEEXPRESSION_TYPE_TYPE);
    }

    /**
     * Complete the definition of a type and store it in the schema.
     * 
     * <p>
     * 
     * This method calls {@link AttributeType#getSuper()} (and {@link ComplexType#getDescriptors()}
     * where applicable) to ensure the construction of the type (a concrete
     * {@link AbstractLazyAttributeTypeImpl} or {@link AbstractLazyComplexTypeImpl} sublass) is
     * complete. This should be sufficient to avoid any nasty thread-safety surprises in code using
     * this schema.
     * 
     * @param type
     *            the type to complete and store
     */
    private void put(AttributeType type) {
        type.getSuper();
        if (type instanceof ComplexType) {
            ((ComplexType) type).getDescriptors();
        }
        put(type.getName(), type);
    }

    /**
     * Test that this class can be loaded.
     */
    public static void main(String[] args) {
        Schema schema = new GMXSchema();
        for (Entry<Name, AttributeType> entry : new TreeMap<Name, AttributeType>(schema).entrySet()) {
            System.out.println("Type: " + entry.getValue().getName());
            System.out.println("    Super type: " + entry.getValue().getSuper().getName());
            if (entry.getValue() instanceof ComplexType) {
                for (PropertyDescriptor descriptor : ((ComplexType) entry.getValue())
                        .getDescriptors()) {
                    System.out.println("    Property descriptor: " + descriptor.getName());
                    System.out.println("        Property type: " + descriptor.getType().getName());
                }
            }
        }
    }

}
