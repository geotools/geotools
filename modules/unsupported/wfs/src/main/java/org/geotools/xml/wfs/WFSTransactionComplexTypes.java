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
package org.geotools.xml.wfs;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import org.geotools.data.Query;
import org.geotools.data.wfs.v1_0_0.Action;
import org.geotools.data.wfs.v1_0_0.LockRequest;
import org.geotools.data.wfs.v1_0_0.LockResult;
import org.geotools.data.wfs.v1_0_0.TransactionResult;
import org.geotools.data.wfs.v1_0_0.WFSTransactionState;
import org.geotools.data.wfs.v1_0_0.Action.DeleteAction;
import org.geotools.data.wfs.v1_0_0.Action.InsertAction;
import org.geotools.data.wfs.v1_0_0.Action.UpdateAction;
import org.geotools.filter.FidFilter;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.filter.FilterSchema;
import org.geotools.xml.filter.FilterSchema.FilterElement;
import org.geotools.xml.gml.GMLSchema;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeValue;
import org.geotools.xml.schema.Choice;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.Sequence;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.ChoiceGT;
import org.geotools.xml.schema.impl.FacetGT;
import org.geotools.xml.schema.impl.SequenceGT;
import org.geotools.xml.wfs.WFSBasicComplexTypes.FeatureCollectionType;
import org.geotools.xml.wfs.WFSBasicComplexTypes.QueryType;
import org.geotools.xml.wfs.WFSSchema.WFSAttribute;
import org.geotools.xml.wfs.WFSSchema.WFSComplexType;
import org.geotools.xml.wfs.WFSSchema.WFSElement;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.AttributesImpl;

import com.vividsolutions.jts.geom.Geometry;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 * @source $URL$
 */
public class WFSTransactionComplexTypes {
    /**
     * <p>
     * This class represents an TransactionType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * TransactionType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class TransactionType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new TransactionType();

        //        <xsd:complexType name="TransactionType">
        //	      <xsd:annotation>
        //	         <xsd:documentation>
        //	            The TranactionType defines the Transaction operation.  A
        //	            Transaction element contains one or more Insert, Update
        //	            Delete and Native elements that allow a client application
        //	            to create, modify or remove feature instances from the 
        //	            feature repository that a Web Feature Service controls.
        //	         </xsd:documentation>
        //	      </xsd:annotation>
        //	      <xsd:sequence>
        //	         <xsd:element ref="wfs:LockId" minOccurs="0">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  In order for a client application to operate upon locked
        //	                  feature instances, the Transaction request must include
        //	                  the LockId element.  The content of this element must be
        //	                  the lock identifier the client application obtained from
        //	                  a previous GetFeatureWithLock or LockFeature operation.
        //
        //	                  If the correct lock identifier is specified the Web
        //	                  Feature Service knows that the client application may
        //	                  operate upon the locked feature instances.
        //
        //	                  No LockId element needs to be specified to operate upon
        //	                  unlocked features.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	         <xsd:choice minOccurs="0" maxOccurs="unbounded">
        //	            <xsd:element ref="wfs:Insert"/>
        //	            <xsd:element ref="wfs:Update"/>
        //	            <xsd:element ref="wfs:Delete"/>
        //	            <xsd:element ref="wfs:Native"/>
        //	         </xsd:choice>
        //	      </xsd:sequence>
        //	      <xsd:attribute name="version"
        //	                     type="xsd:string" use="required" fixed="1.0.0"/>
        //	      <xsd:attribute name="service"
        //	                     type="xsd:string" use="required" fixed="WFS"/>
        //	      <xsd:attribute name="handle"
        //	                     type="xsd:string" use="optional"/>
        //	      <xsd:attribute name="releaseAction"
        //	                     type="wfs:AllSomeType" use="optional">
        //	         <xsd:annotation>
        //	            <xsd:documentation>
        //	               The releaseAction attribute is used to control how a Web
        //	               Feature service releases locks on feature instances after
        //	               a Transaction request has been processed.
        //
        //	               Valid values are ALL or SOME.
        //
        //	               A value of ALL means that the Web Feature Service should
        //	               release the locks of all feature instances locked with the
        //	               specified lockId, regardless or whether or not the features
        //	               were actually modified.
        //
        //	               A value of SOME means that the Web Feature Service will 
        //	               only release the locks held on feature instances that 
        //	               were actually operated upon by the transaction.  The lockId
        //	               that the client application obtained shall remain valid and
        //	               the other, unmodified, feature instances shall remain locked.
        //	               If the expiry attribute was specified in the original operation 
        //	               that locked the feature instances, then the expiry counter
        //	               will be reset to give the client application that same amount
        //	               of time to post subsequent transactions against the locked
        //	               features.
        //	            </xsd:documentation>
        //	         </xsd:annotation>
        //	      </xsd:attribute>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement("LockId", XSISimpleTypes.String.getInstance(),
                    0, 1, false, null),
                new WFSElement("Insert", InsertElementType.getInstance()),
                new WFSElement("Update", UpdateElementType.getInstance()),
                new WFSElement("Delete", DeleteElementType.getInstance()),
                new WFSElement("Native", NativeType.getInstance())
            };
        private static Sequence child = new SequenceGT(new ElementGrouping[] {
                    elems[0],
                    new ChoiceGT(null, 0, Integer.MAX_VALUE,
                        new Element[] { elems[1], elems[2], elems[3], elems[4] })
                });
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("version",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "1.0.0";
                        }
                    }
                ,
                new WFSAttribute("service",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "WFS";
                        }
                    }
                ,
                new WFSAttribute("handle", XSISimpleTypes.String.getInstance(),
                    Attribute.OPTIONAL),
                new WFSAttribute("lockAction", AllSomeType.getInstance(),
                    Attribute.OPTIONAL)
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            throw new SAXNotSupportedException("");
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "TransactionType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return WFSTransactionState.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element != null) && (element.getType() != null)
            && getName().equals(element.getType().getName()) && (value != null)
            && value instanceof WFSTransactionState;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new IOException("Cannot encode");
            }

            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[0].getName(), null, "string", attrs[0].getFixed());
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[1].getName(), null, "string", attrs[1].getFixed());
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[3].getName(), null, "string", "ALL");

            WFSTransactionState transactionRequest = (WFSTransactionState) value;

            output.startElement(element.getNamespace(), element.getName(),
                attributes);

            if (transactionRequest.getLockId() != null) {
                elems[0].getType().encode(elems[0],
                    transactionRequest.getLockId(), output, hints);
            }

            Iterator actions = transactionRequest.getAllActions().iterator();

            while (actions.hasNext()) {
                Action a = (Action) actions.next();

                switch (a.getType()) {
                case Action.DELETE:
                    elems[3].getType().encode(elems[3], a, output, hints);

                    break;

                case Action.INSERT:
                    elems[1].getType().encode(elems[1], a, output, hints);

                    break;

                case Action.UPDATE:
                    elems[2].getType().encode(elems[2], a, output, hints);

                    break;

                default:
                    elems[4].getType().encode(elems[4], a, output, hints);
                }
            }

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    static class GetFeatureWithLockType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new FeatureCollectionType();

        //        <xsd:complexType name="GetFeatureWithLockType">
        //	      <xsd:annotation>
        //	         <xsd:documentation>
        //	            A GetFeatureWithLock request operates identically to a
        //	            GetFeature request expect that it attempts to lock the
        //	            feature instances in the result set and includes a lock
        //	            identifier in its response to a client.  A lock identifier
        //	            is an identifier generated by a Web Feature Service that 
        //	            a client application can use, in subsequent operations,
        //	            to reference the locked set of feature instances.
        //	         </xsd:documentation>
        //	      </xsd:annotation>
        //	      <xsd:sequence>
        //	         <xsd:element ref="wfs:Query" maxOccurs="unbounded"/>
        //	      </xsd:sequence>
        //	      <xsd:attribute name="version"
        //	                     type="xsd:string" use="required" fixed="1.0.0"/>
        //	      <xsd:attribute name="service"
        //	                     type="xsd:string" use="required" fixed="WFS"/>
        //	      <xsd:attribute name="handle"
        //	                     type="xsd:string" use="optional"/>
        //	      <xsd:attribute name="expiry"
        //	                     type="xsd:positiveInteger" use="optional"/>
        //	      <xsd:attribute name="outputFormat"
        //	                     type="xsd:string" use="optional" default="GML2"/>
        //	      <xsd:attribute name="maxFeatures"
        //	                     type="xsd:positiveInteger" use="optional"/>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement("Query", QueryType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null)
            };
        private static Sequence child = new SequenceGT(elems);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("version",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "1.0.0";
                        }
                    }
                ,
                new WFSAttribute("service",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "WFS";
                        }
                    }
                ,
                new WFSAttribute("handle", XSISimpleTypes.String.getInstance(),
                    Attribute.OPTIONAL),
                new WFSAttribute("outputFormat",
                    XSISimpleTypes.String.getInstance(), Attribute.OPTIONAL,
                    "GML2"),
                new WFSAttribute("maxFeatures",
                    XSISimpleTypes.PositiveInteger.getInstance(),
                    Attribute.OPTIONAL)
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            throw new SAXNotSupportedException("");
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "GetFeatureWithLockType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Query.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((element.getType() != null)
                    && getName().equals(element.getType().getName())) {
                return ((value == null) || value instanceof Query);
            }

            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (canEncode(element, value, hints)) {
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    attrs[0].getName(), null, "string", attrs[0].getFixed());
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    attrs[1].getName(), null, "string", attrs[1].getFixed());
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    attrs[2].getName(), null, "string", attrs[3].getDefault());

                Query query = (Query) value;

                if ((query != null)
                        && (query.getMaxFeatures() != Query.DEFAULT_MAX)) {
                    attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                        elems[3].getName(), null, "integer",
                        "" + query.getMaxFeatures());
                }

                if (hints != null) {
                    String lockId = (String) hints.get(WFSBasicComplexTypes.LOCK_KEY);

                    if (lockId != null) {
                        attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                            elems[2].getName(), null, "string", lockId);
                    }
                }

                output.startElement(element.getNamespace(), element.getName(),
                    attributes);
                elems[0].getType().encode(elems[0], value, output, hints);
                output.endElement(element.getNamespace(), element.getName());
            } else {
                throw new OperationNotSupportedException(
                    "not a valid value/element for a DescribeFeatureTypeType.");
            }
        }
    }

    static class LockFeatureType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new FeatureCollectionType();

        //        <xsd:complexType name="LockFeatureType">
        //           <xsd:annotation>
        //              <xsd:documentation>
        //                 This type defines the LockFeature operation.  The LockFeature
        //                 element contains one or more Lock elements that define
        //                 which features of a particular type should be locked.  A lock
        //                 identifier (lockId) is returned to the client application which
        //                 can be used by subsequent operations to reference the locked
        //                 features.
        //              </xsd:documentation>
        //           </xsd:annotation>
        //           <xsd:sequence>
        //              <xsd:element name="Lock" type="wfs:LockType" maxOccurs="unbounded">
        //                 <xsd:annotation>
        //                    <xsd:documentation>
        //                       The lock element is used to indicate which feature 
        //                       instances of particular type are to be locked.
        //                    </xsd:documentation>
        //                 </xsd:annotation>
        //              </xsd:element>
        //           </xsd:sequence>
        //           <xsd:attribute name="version"
        //                          type="xsd:string" use="required" fixed="1.0.0"/>
        //           <xsd:attribute name="service"
        //                          type="xsd:string" use="required" fixed="WFS"/>
        //           <xsd:attribute name="expiry"
        //                          type="xsd:positiveInteger" use="optional"/>
        //           <xsd:attribute name="lockAction"
        //                          type="wfs:AllSomeType" use="optional">
        //              <xsd:annotation>
        //                 <xsd:documentation>
        //                    The lockAction attribute is used to indicate what
        //                    a Web Feature Service should do when it encounters
        //                    a feature instance that has already been locked by
        //                    another client application.
        //
        //                    Valid values are ALL or SOME.
        //
        //                    ALL means that the Web Feature Service must acquire
        //                    locks on all the requested feature instances.  If it
        //                    cannot acquire those locks then the request should
        //                    fail.  In this instance, all locks acquired by the
        //                    operation should be released.
        //      
        //                    SOME means that the Web Feature Service should lock
        //                    as many of the requested features as it can.
        //                 </xsd:documentation>
        //              </xsd:annotation>
        //           </xsd:attribute>
        //        </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement("Lock", LockType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null),
            };
        private Sequence child = new SequenceGT(elems);
        private Attribute[] attrs = new Attribute[] {
                new WFSAttribute("version",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "1.0.0";
                        }
                    }
                ,
                new WFSAttribute("service",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "WFS";
                        }
                    }
                ,
                new WFSAttribute("expiry",
                    XSISimpleTypes.PositiveInteger.getInstance(),
                    Attribute.OPTIONAL),
                new WFSAttribute("lockAction", AllSomeType.getInstance(),
                    Attribute.OPTIONAL)
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            throw new SAXNotSupportedException("");
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "LockFeatureType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LockRequest.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element != null) && (element.getType() != null)
            && getName().equals(element.getType().getName()) && (value != null)
            && value instanceof LockRequest;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new IOException("Cannot encode");
            }

            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[0].getName(), null, "string", attrs[0].getFixed());
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[1].getName(), null, "string", attrs[1].getFixed());
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[3].getName(), null, "string", "ALL");

            LockRequest lockRequest = (LockRequest) value;

            if ((lockRequest != null) && (lockRequest.getDuration() > 0)) {
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    elems[2].getName(), null, "integer",
                    "" + lockRequest.getDuration());
            }

            output.startElement(element.getNamespace(), element.getName(),
                attributes);

            Object[] t = new Object[2];

            for (int i = 0; i < lockRequest.getTypeNames().length; i++) {
                t[0] = lockRequest.getTypeNames()[i];
                t[1] = lockRequest.getFilters()[i];
                elems[0].getType().encode(elems[0], t, output, hints);
            }

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    static class LockType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new LockType();

        //        <xsd:complexType name="LockType">
        //           <xsd:annotation>
        //              <xsd:documentation>
        //                 This type defines the Lock element.  The Lock element
        //                 defines a locking operation on feature instances of 
        //                 a single type. An OGC Filter is used to constrain the
        //                 scope of the operation.  Features to be locked can be
        //                 identified individually by using their feature identifier
        //                 or they can be locked by satisfying the spatial and 
        //                 non-spatial constraints defined in the filter.
        //              </xsd:documentation>
        //           </xsd:annotation>
        //           <xsd:sequence>
        //              <xsd:element ref="ogc:Filter" minOccurs="0" maxOccurs="1"/>
        //           </xsd:sequence>
        //           <xsd:attribute name="handle" 
        //                          type="xsd:string" use="optional"/>
        //           <xsd:attribute name="typeName" 
        //                          type="xsd:QName" use="required"/>
        //        </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement(FilterSchema.getInstance().getElements()[2]
                    .getName(),
                    FilterSchema.getInstance().getElements()[2].getType(), 0,
                    1, false,
                    FilterSchema.getInstance().getElements()[2]
                    .getSubstitutionGroup()) {
                        public URI getNamespace() {
                            return FilterSchema.NAMESPACE;
                        }
                    }
                ,
            };
        private static Sequence child = new SequenceGT(elems);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("handle", XSISimpleTypes.String.getInstance(),
                    Attribute.OPTIONAL),
                new WFSAttribute("typeName",
                    XSISimpleTypes.QName.getInstance(), Attribute.REQUIRED)
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            throw new SAXNotSupportedException("");
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "LockType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Object[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element != null) && (element.getType() != null)
            && getName().equals(element.getType().getName()) && (value != null)
            && value instanceof Object[] && (((Object[]) value).length == 2);
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new IOException("Cannot encode");
            }

            Object[] t = (Object[]) value;
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[1].getName(), null, "string", (String) t[0]);

            LockRequest lockRequest = (LockRequest) value;

            if ((lockRequest != null) && (lockRequest.getDuration() > 0)) {
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    elems[2].getName(), null, "integer",
                    "" + lockRequest.getDuration());
            }

            output.startElement(element.getNamespace(), element.getName(),
                attributes);
            elems[0].getType().encode(elems[0], t[1], output, hints);
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    static class InsertElementType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new InsertElementType();

        //        <xsd:complexType name="InsertElementType">
        //           <xsd:sequence>
        //              <xsd:element ref="gml:_Feature" maxOccurs="unbounded"/>
        //           </xsd:sequence>
        //           <xsd:attribute name="handle" type="xsd:string" use="optional"/>
        //        </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement(GMLSchema.getInstance().getElements()[0].getName(),
                    GMLSchema.getInstance().getElements()[0].getType(), 1,
                    Integer.MAX_VALUE,
                    GMLSchema.getInstance().getElements()[0].isAbstract(),
                    GMLSchema.getInstance().getElements()[0]
                    .getSubstitutionGroup()) {
                        public URI getNamespace() {
                            return GMLSchema.NAMESPACE;
                        }
                    }
            };
        private static Sequence child = new SequenceGT(elems);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("handler",
                    XSISimpleTypes.String.getInstance(), Attribute.OPTIONAL),
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            throw new SAXNotSupportedException("");
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "InsertElementType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return InsertAction.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element != null) && (element.getType() != null)
            && getName().equals(element.getType().getName()) && (value != null)
            && value instanceof InsertAction;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            output.startElement(element.getNamespace(), element.getName(), null);

            InsertAction a = (InsertAction) value;

            // find element definition
            // should exist when original from a WFS ...
            SimpleFeature f = a.getFeature();
            SimpleFeatureType featureType = f.getFeatureType();
            Name name = featureType.getName();            
            Schema schema = SchemaFactory.getInstance( name.getNamespaceURI() );
            Element[] els = schema.getElements();
            Element e = null;

            if (els != null) {
                for (int i = 0; i < els.length; i++){
	                String typeName = featureType.getTypeName();
	                if (typeName.indexOf(':')>=0) {
	                   	typeName = typeName.substring(typeName.indexOf(':')+1);
	                }
	                if (typeName.equals(els[i].getName())) {
	                     e = els[i];
	                     i = els.length;
	                }
                }
            }

            // write it
            elems[0].getType().encode(e, f, output, hints);
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    static class UpdateElementType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new UpdateElementType();

        //        <xsd:complexType name="UpdateElementType">
        //	      <xsd:sequence>
        //	         <xsd:element ref="wfs:Property" maxOccurs="unbounded" />
        //	         <xsd:element ref="ogc:Filter" minOccurs="0" maxOccurs="1">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  The Filter element is used to constrain the scope
        //	                  of the update operation to those features identified
        //	                  by the filter.  Feature instances can be specified
        //	                  explicitly and individually using the identifier of
        //	                  each feature instance OR a set of features to be
        //	                  operated on can be identified by specifying spatial
        //	                  and non-spatial constraints in the filter.
        //	                  If no filter is specified, then the update operation 
        //	                  applies to all feature instances.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	      </xsd:sequence>
        //	      <xsd:attribute name="handle" type="xsd:string" use="optional"/>
        //	      <xsd:attribute name="typeName" type="xsd:QName" use="required"/>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement("Property", PropertyType.getInstance(), 0,
                    Integer.MAX_VALUE, true, null),
                new WFSElement(FilterSchema.getInstance().getElements()[2]
                    .getName(),
                    FilterSchema.getInstance().getElements()[2].getType(), 0,
                    1, false,
                    FilterSchema.getInstance().getElements()[2]
                    .getSubstitutionGroup()) {
                        public URI getNamespace() {
                            return FilterSchema.NAMESPACE;
                        }
                    }
            };
        private static Sequence child = new SequenceGT(elems);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("handler",
                    XSISimpleTypes.String.getInstance(), Attribute.OPTIONAL),
                new WFSAttribute("typeName",
                    XSISimpleTypes.QName.getInstance(), Attribute.REQUIRED),
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            throw new SAXNotSupportedException("");
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "UpdateElementType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return UpdateAction.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element != null) && (element.getType() != null)
            && getName().equals(element.getType().getName()) && (value != null)
            && value instanceof UpdateAction;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            UpdateAction a = (UpdateAction) value;

            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[1].getName(), null, "string", a.getTypeName());

            output.startElement(element.getNamespace(), element.getName(),
                attributes);

            Object[] prop = new Object[2];
            String[] names = a.getPropertyNames();

            for (int i = 0; i < names.length; i++) {
                prop[0] = names[i];
                prop[1] = a.getProperty(names[i]);
                elems[0].getType().encode(elems[0], prop, output, hints);
            }

            elems[1].getType().encode(elems[1], a.getFilter(), output, hints);

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    static class DeleteElementType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new DeleteElementType();

        //        <xsd:complexType name="DeleteElementType">
        //	      <xsd:sequence>
        //	         <xsd:element ref="ogc:Filter" minOccurs="1" maxOccurs="1">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  The Filter element is used to constrain the scope
        //	                  of the delete operation to those features identified
        //	                  by the filter.  Feature instances can be specified
        //	                  explicitly and individually using the identifier of
        //	                  each feature instance OR a set of features to be
        //	                  operated on can be identified by specifying spatial
        //	                  and non-spatial constraints in the filter.
        //	                  If no filter is specified then an exception should
        //	                  be raised since it is unlikely that a client application
        //	                  intends to delete all feature instances.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	      </xsd:sequence>
        //	      <xsd:attribute name="handle" type="xsd:string" use="optional"/>
        //	      <xsd:attribute name="typeName" type="xsd:QName" use="required"/>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement(FilterSchema.getInstance().getElements()[2]
                    .getName(),
                    FilterSchema.getInstance().getElements()[2].getType(), 0,
                    1, false,
                    FilterSchema.getInstance().getElements()[2]
                    .getSubstitutionGroup()) {
                        public URI getNamespace() {
                            return FilterSchema.NAMESPACE;
                        }
                    }
            };
        private static Sequence child = new SequenceGT(elems);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("handler",
                    XSISimpleTypes.String.getInstance(), Attribute.OPTIONAL),
                new WFSAttribute("typeName",
                    XSISimpleTypes.QName.getInstance(), Attribute.REQUIRED),
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            throw new SAXNotSupportedException("");
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "DeleteElementType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return DeleteAction.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element != null) && (element.getType() != null)
            && getName().equals(element.getType().getName()) && (value != null)
            && value instanceof DeleteAction;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            DeleteAction a = (DeleteAction) value;

            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[1].getName(), null, "string", a.getTypeName());

            output.startElement(element.getNamespace(), element.getName(),
                attributes);

            elems[0].getType().encode(elems[0], a.getFilter(), output, hints);

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    static class NativeType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new NativeType();

        //        <xsd:complexType name="NativeType">
        //	      <xsd:attribute name="vendorId" type="xsd:string" use="required">
        //	         <xsd:annotation>
        //	            <xsd:documentation>
        //	               The vendorId attribute is used to specify the name of
        //	               vendor who's vendor specific command the client
        //	               application wishes to execute.
        //	            </xsd:documentation>
        //	         </xsd:annotation>
        //	      </xsd:attribute>
        //	      <xsd:attribute name="safeToIgnore" type="xsd:boolean" use="required">
        //	         <xsd:annotation>
        //	            <xsd:documentation>
        //	               In the event that a Web Feature Service does not recognize
        //	               the vendorId or does not recognize the vendor specific command,
        //	               the safeToIgnore attribute is used to indicate whether the 
        //	               exception can be safely ignored.  A value of TRUE means that
        //	               the Web Feature Service may ignore the command.  A value of
        //	               FALSE means that a Web Feature Service cannot ignore the
        //	               command and an exception should be raised if a problem is 
        //	               encountered.
        //	            </xsd:documentation>
        //	         </xsd:annotation>
        //	      </xsd:attribute>
        //	   </xsd:complexType>
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("vendorId",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED),
                new WFSAttribute("safeToIgnore",
                    XSISimpleTypes.Boolean.getInstance(), Attribute.REQUIRED),
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            throw new SAXNotSupportedException("");
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "NativeType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Action.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element != null) && (element.getType() != null)
            && getName().equals(element.getType().getName()) && (value != null)
            && value instanceof Action && (((Action) value).getType() == 0);
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[0].getName(), null, "string", "www.refractions.net");

            // TODO? force failures on unknown actions? allowing ignores here
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[1].getName(), null, "string", "true");

            output.element(element.getNamespace(), element.getName(), attributes);
        }
    }

    static class PropertyType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new PropertyType();

        //        <xsd:complexType name="PropertyType">
        //	      <xsd:sequence>
        //	         <xsd:element name="Name" type="xsd:string">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  The Name element contains the name of a feature property
        //	                  to be updated.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	         <xsd:element name="Value" minOccurs="0">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  The Value element contains the replacement value for the
        //	                  named property.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	      </xsd:sequence>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement("Name", XSISimpleTypes.String.getInstance()),
				// TODO correctly represent the value element
                new WFSElement("Value", WFSEmptyType.getInstance(), 0, 1, true,
                    null) {
                        public boolean isMixed() {
                            return true;
                        }
                    }
                ,
            };
        private static Sequence child = new SequenceGT(elems);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            throw new SAXNotSupportedException("");
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "PropertyType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Object[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element != null) && (element.getType() != null)
            && getName().equals(element.getType().getName()) && (value != null)
            && value instanceof Object[] && (((Object[]) value).length == 2);
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode "
                    + element + " in PropertyType");
            }

            Object[] t = (Object[]) value;
            output.startElement(element.getNamespace(), element.getName(), null);

            elems[0].getType().encode(elems[0], t[0], output, hints);

            if (t[1] != null) {
//                elems[1].getType().encode(elems[1], t[1], output, hints);
            	
            	// can only be a primative, geometry or feature for version 2.0
            	// in the future use output.findElement(t[1]) ... posibly with a newer search order
            	output.startElement(elems[1].getNamespace(),elems[1].getName(),null);
            	if(t[1] instanceof SimpleFeature){
            		// Feature
            		GMLSchema.getInstance().getElements()[0].getType().encode(GMLSchema.getInstance().getElements()[0],t[1],output,hints);
            	}else{
            	if(t[1] instanceof Geometry){
            		// Geometry
            		GMLSchema.getInstance().getElements()[29].getType().encode(GMLSchema.getInstance().getElements()[0],t[1],output,hints);
            	}else{
            		// primative
            		output.characters(t[1].toString());
            	}}
            	output.endElement(elems[1].getNamespace(),elems[1].getName());
            }

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    static class WFS_LockFeatureResponseType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new WFS_LockFeatureResponseType();

        //        <xsd:complexType name="WFS_LockFeatureResponseType">
        //	      <xsd:annotation>
        //	         <xsd:documentation>
        //	            The WFS_LockFeatureResponseType is used to define an
        //	            element to contains the response to a LockFeature
        //	            operation.
        //	         </xsd:documentation>
        //	      </xsd:annotation>
        //	      <xsd:sequence>
        //	         <xsd:element ref="wfs:LockId">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  The WFS_LockFeatureResponse includes a LockId element
        //	                  that contains a lock identifier.  The lock identifier
        //	                  can be used by a client, in subsequent operations, to
        //	                  operate upon the locked feature instances.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	         <xsd:element name="FeaturesLocked"
        //	                      type="wfs:FeaturesLockedType" minOccurs="0">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  The LockFeature or GetFeatureWithLock operations
        //	                  identify and attempt to lock a set of feature 
        //	                  instances that satisfy the constraints specified 
        //	                  in the request.  In the event that the lockAction
        //	                  attribute (on the LockFeature or GetFeatureWithLock
        //	                  elements) is set to SOME, a Web Feature Service will
        //	                  attempt to lock as many of the feature instances from
        //	                  the result set as possible.
        //
        //	                  The FeaturesLocked element contains list of ogc:FeatureId
        //	                  elements enumerating the feature instances that a WFS
        //	                  actually managed to lock.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	         <xsd:element name="FeaturesNotLocked"
        //	                      type="wfs:FeaturesNotLockedType" minOccurs="0">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  In contrast to the FeaturesLocked element, the
        //	                  FeaturesNotLocked element contains a list of 
        //	                  ogc:Filter elements identifying feature instances
        //	                  that a WFS did not manage to lock because they were
        //	                  already locked by another process.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	      </xsd:sequence>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement("LockId", XSISimpleTypes.String.getInstance()),
                new WFSElement("FeaturesLocked",
                    FeaturesLockedType.getInstance(), 0, 1, true, null),
                new WFSElement("FeaturesNotLocked",
                    FeaturesNotLockedType.getInstance(), 0, 1, true, null)
            };
        private static Sequence child = new SequenceGT(elems);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)
                    || (element.getType() == null)) {
                throw new SAXException("Invalid parameters : null found");
            }

            if ((value.length < 1) || (value.length > 3)) {
                throw new SAXException("Invalid children: too few or too many");
            }

            if (!getName().equals(element.getType().getName())) {
                throw new SAXException("Invalid type name for element provided");
            }

            String lockId = (String) value[0].getValue();
            FidFilter in = null;
            FidFilter out = null;
            int i = 1;

            if ((i < value.length)
                    && elems[1].getType().getName().equals(value[i].getElement()
                                                                       .getType()
                                                                       .getName())) {
                in = (FidFilter) value[i++];
            }

            if ((i < value.length)
                    && elems[2].getType().getName().equals(value[i].getElement()
                                                                       .getType()
                                                                       .getName())) {
                out = (FidFilter) value[i++];
            }

            return new LockResult(lockId, in, out);
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "WFS_LockFeatureResponseType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LockResult.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    static class FeaturesLockedType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new FeaturesLockedType();

        //     <xsd:complexType name="FeaturesLockedType">
        //	     <xsd:sequence maxOccurs="unbounded">
        //	       <xsd:element ref="ogc:FeatureId"/>
        //	     </xsd:sequence>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                FilterSchema.getInstance().getElements()[1],
            };
        private static Sequence child = new SequenceGT(null, elems, 1,
                Integer.MAX_VALUE);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)
                    || (element.getType() == null)) {
                throw new SAXException("Invalid parameters : null found");
            }

            if (value.length < 1) {
                throw new SAXException("Invalid children: too few");
            }

            if (!getName().equals(element.getType().getName())) {
                throw new SAXException("Invalid type name for element provided");
            }

            Set fidSet = new HashSet();

            for (int i = 0; i < value.length; i++)
                fidSet.addAll(Arrays.asList(
                        ((FidFilter) value[i].getValue()).getFids()));

            FidFilter r = FilterFactoryFinder.createFilterFactory().createFidFilter();
            r.addAllFids(fidSet);

            return r;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "FeaturesLockedType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return FidFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    static class FeaturesNotLockedType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new FeaturesNotLockedType();

        //        <xsd:complexType name="FeaturesNotLockedType">
        //	     <xsd:sequence maxOccurs="unbounded">
        //	       <xsd:element ref="ogc:FeatureId"/>
        //	     </xsd:sequence>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                FilterSchema.getInstance().getElements()[1],
            };
        private static Sequence child = new SequenceGT(null, elems, 1,
                Integer.MAX_VALUE);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)
                    || (element.getType() == null)) {
                throw new SAXException("Invalid parameters : null found");
            }

            if (value.length < 1) {
                throw new SAXException("Invalid children: too few");
            }

            if (!getName().equals(element.getType().getName())) {
                throw new SAXException("Invalid type name for element provided");
            }

            Set fidSet = new HashSet();

            for (int i = 0; i < value.length; i++)
                fidSet.addAll(Arrays.asList(
                        ((FidFilter) value[i].getValue()).getFids()));

            FidFilter r = FilterFactoryFinder.createFilterFactory().createFidFilter();
            r.addAllFids(fidSet);

            return r;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "FeaturesNotLockedType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return FidFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    static class WFS_TransactionResponseType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new WFS_TransactionResponseType();

        //        <xsd:complexType name="WFS_TransactionResponseType">
        //	      <xsd:annotation>
        //	         <xsd:documentation>
        //	            The WFS_TransactionResponseType defines the format of
        //	            the XML document that a Web Feature Service generates 
        //	            in response to a Transaction request.  The response 
        //	            includes the completion status of the transaction 
        //	            and the feature identifiers of any newly created
        //	            feature instances.
        //	         </xsd:documentation>
        //	      </xsd:annotation>
        //	      <xsd:sequence>
        //	         <xsd:element name="InsertResult"
        //	                      type="wfs:InsertResultType"
        //	                      minOccurs="0" maxOccurs="unbounded">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  The InsertResult element contains a list of ogc:FeatureId
        //	                  elements that identify any newly created feature instances.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	         <xsd:element name="TransactionResult"
        //	                      type="wfs:TransactionResultType">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  The TransactionResult element contains a Status element
        //	                  indicating the completion status of a transaction.  In
        //	                  the event that the transaction fails, additional element
        //	                  may be included to help locate which part of the transaction
        //	                  failed and why.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	      </xsd:sequence>
        //	      <xsd:attribute name="version"
        //	                     type="xsd:string" use="required" fixed="1.0.0"/>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement("InsertResult", InsertResultType.getInstance(),
                    0, Integer.MAX_VALUE, true, null),
                new WFSElement("TransactionResult",
                    TransactionResultType.getInstance()),
            };
        private static Sequence child = new SequenceGT(elems);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("version",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "1.0.0";
                        }
                    }
                ,
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)
                    || (element.getType() == null)) {
                throw new SAXException("Invalid parameters : null found");
            }

            if (value.length < 1) {
                throw new SAXException("Invalid children: too few");
            }

            if (!getName().equals(element.getType().getName())) {
                throw new SAXException("Invalid type name for element provided");
            }

            List fidSet = new ArrayList();

            for (int i = 0; i < (value.length - 1); i++)
                fidSet.addAll( (Collection) value[i].getValue());

            Object[] t = (Object[]) value[value.length - 1].getValue();
            int status = ((Integer) t[0]).intValue();
            SAXException error = (SAXException) ((t.length < 2) ? null : t[1]);

            return new TransactionResult(status, fidSet, error);
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "WFS_TransactionResponseType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return TransactionResult.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    static class TransactionResultType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new TransactionResultType();

        //        <xsd:complexType name="TransactionResultType">
        //	      <xsd:sequence>
        //	         <xsd:element name="Status" type="wfs:StatusType">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  The Status element contains an element indicating the
        //	                  completion status of a transaction.  The SUCCESS element
        //	                  is used to indicate successful completion.  The FAILED
        //	                  element is used to indicate that an exception was 
        //	                  encountered.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	         <xsd:element name="Locator" type="xsd:string" minOccurs="0">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  In the event that an exception was encountered while 
        //	                  processing a transaction, a Web Feature Service may
        //	                  use the Locator element to try and identify the part
        //	                  of the transaction that failed.  If the element(s)
        //	                  contained in a Transaction element included a handle
        //	                  attribute, then a Web Feature Service may report the
        //	                  handle to identify the offending element.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	         <xsd:element name="Message" type="xsd:string" minOccurs="0">
        //	            <xsd:annotation>
        //	               <xsd:documentation>
        //	                  The Message element may contain an exception report
        //	                  generated by a Web Feature Service when an exception
        //	                  is encountered.
        //	               </xsd:documentation>
        //	            </xsd:annotation>
        //	         </xsd:element>
        //	      </xsd:sequence>
        //	      <xsd:attribute name="handle" type="xsd:string" use="optional"/>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement("Status", StatusType.getInstance()),
                new WFSElement("Locator", XSISimpleTypes.String.getInstance(),
                    0, 1, true, null),
                new WFSElement("Message", XSISimpleTypes.String.getInstance(),
                    0, 1, true, null)
            };
        private static Sequence child = new SequenceGT(elems);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("handle", XSISimpleTypes.String.getInstance(),
                    Attribute.OPTIONAL),
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)
                    || (element.getType() == null)) {
                throw new SAXException("Invalid parameters : null found");
            }

            if (value.length < 1) {
                throw new SAXException("Invalid children: too few");
            }

            if (!getName().equals(element.getType().getName())) {
                throw new SAXException("Invalid type name for element provided");
            }

            Object[] t = new Object[2];

            t[0] = value[0].getValue();

            String locator = null;
            String message = null;

            if (value.length > 1) {
                if ((value[1].getElement() != null)
                        && elems[1].getName().equals(value[1].getElement()
                                                                 .getName())) {
                    locator = (String) value[1].getValue();

                    if ((value[2].getElement() != null)
                            && elems[2].getName().equals(value[2].getElement()
                                                                     .getName())) {
                        message = (String) value[2].getValue();
                    }
                } else {
                    if ((value[1].getElement() != null)
                            && elems[2].getName().equals(value[1].getElement()
                                                                     .getName())) {
                        message = (String) value[1].getValue();
                    }
                }
            }

            t[1] = (message == null)
                ? ((locator == null) ? null : new SAXException(locator))
                : ((locator == null) ? new SAXException(message)
                                     : new SAXException(message + ":" + locator));

            return t;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "TransactionResultType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            // [int][SAXException]
            return Object[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    static class InsertResultType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new InsertResultType();

        //        <xsd:complexType name="InsertResultType">
        //	      <xsd:sequence>
        //	         <xsd:element ref="ogc:FeatureId" maxOccurs="unbounded"/>
        //	      </xsd:sequence>
        //	      <xsd:attribute name="handle" type="xsd:string" use="optional"/>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                new FilterElement(FilterSchema.getInstance().getElements()[1].getName(), FilterSchema.getInstance().getElements()[1].getType(), FilterSchema.getInstance().getElements()[1].getSubstitutionGroup()){
                	public int getMaxOccurs(){
                		return Integer.MAX_VALUE;
                	}
                },
            };
        private static Sequence child = new SequenceGT(null, elems, 1,
                Integer.MAX_VALUE);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("handle", XSISimpleTypes.String.getInstance(),
                    Attribute.OPTIONAL),
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)
                    || (element.getType() == null)) {
                throw new SAXException("Invalid parameters : null found");
            }

            if (value.length < 1) {
                throw new SAXException("Invalid children: too few");
            }

            if (!getName().equals(element.getType().getName())) {
                throw new SAXException("Invalid type name for element provided");
            }

            List fidList = new ArrayList();

            for (int i = 0; i < value.length; i++)
                fidList.addAll(Arrays.asList(
                        ((FidFilter) value[i].getValue()).getFids()));

            return fidList;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "InsertResultType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return FidFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    static class StatusType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new StatusType();

        //        <xsd:complexType name="StatusType">
        //	      <xsd:choice>
        //	         <xsd:element ref="wfs:SUCCESS"/>
        //	         <xsd:element ref="wfs:FAILED"/>
        //	         <xsd:element ref="wfs:PARTIAL"/>
        //	      </xsd:choice>
        //	   </xsd:complexType>
        private static Element[] elems = new Element[] {
                new WFSElement("SUCCESS", WFSEmptyType.getInstance()),
                new WFSElement("FAILED", WFSEmptyType.getInstance()),
                new WFSElement("FAILED", WFSEmptyType.getInstance()),
            };
        private static Choice child = new ChoiceGT(elems);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)
                    || (element.getType() == null)) {
                throw new SAXException("Invalid parameters : null found");
            }

            if ((value.length < 1) || (value.length > 1)) {
                throw new SAXException("Invalid children: too few or too many");
            }

            if (!getName().equals(element.getType().getName())) {
                throw new SAXException("Invalid type name for element provided");
            }

            if ((value[0].getElement() == null)
                    || (value[0].getElement().getName() == null)) {
                throw new SAXException(
                    "Invalid child element: no name was provided");
            }

            return new Integer(TransactionResult.parseStatus(
                    value[0].getElement().getName()));
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "StatusType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Integer.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    private static class WFSEmptyType extends WFSComplexType {
        private static WFSComplexType instance = new WFSEmptyType();

        public static WFSComplexType getInstance() {
            return instance;
        }

        // 	   <xsd:complexType name="EmptyType"/>

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "EmptyType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return element != null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException{
            output.element(element.getNamespace(), element.getName(), null);
        }
    }

    private static class AllSomeType implements SimpleType {
        private static SimpleType instance = new AllSomeType();
        private static Facet[] facets = new Facet[] {
                new FacetGT(Facet.ENUMERATION, "ALL"),
                new FacetGT(Facet.ENUMERATION, "SOME")
            };

        public static SimpleType getInstance() {
            return instance;
        }

        //    	   <xsd:simpleType name="AllSomeType">
        //    	      <xsd:restriction base="xsd:string">
        //    	         <xsd:enumeration value="ALL"/>
        //    	         <xsd:enumeration value="SOME"/>
        //    	      </xsd:restriction>
        //    	   </xsd:simpleType>

        /**
         * @see org.geotools.xml.schema.SimpleType#getFinal()
         */
        public int getFinal() {
            return 0;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getId()
         */
        public String getId() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#toAttribute(org.geotools.xml.schema.Attribute, java.lang.Object, java.util.Map)
         */
        public AttributeValue toAttribute(Attribute attribute, Object value,
            Map hints) {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#canCreateAttributes(org.geotools.xml.schema.Attribute, java.lang.Object, java.util.Map)
         */
        public boolean canCreateAttributes(Attribute attribute, Object value,
            Map hints) {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getChildType()
         */
        public int getChildType() {
            return RESTRICTION;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getParents()
         */
        public SimpleType[] getParents() {
            return new SimpleType[] { XSISimpleTypes.String.getInstance(), };
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getFacets()
         */
        public Facet[] getFacets() {
            return facets;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((value == null) || (value.length != 1) || (element == null)
                    || (element.getType() == null)) {
                throw new SAXNotSupportedException("invalid inputs");
            }

            if (value[0].getValue() instanceof String) {
                String t = (String) value[0].getValue();

                if ("ALL".equals(t) || "SOME".equals(t)) {
                    return t;
                }

                throw new SAXException("Invalid value: not ALL or NONE");
            }

            throw new SAXNotSupportedException("Invalid child value type.");
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "AllSomeType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getNamespace()
         */
        public URI getNamespace() {
            return WFSSchema.NAMESPACE;
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element != null) && (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof String
            && ("ALL".equals(value) || "SOME".equals(value));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException{
            if (canEncode(element, value, hints)) {
                output.startElement(element.getNamespace(), element.getName(),
                    null);
            }

            output.characters((String) value);
            output.endElement(element.getNamespace(), element.getName());
        }

        /**
         * @see org.geotools.xml.schema.Type#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name) {
            return null;
        }
    }
}
