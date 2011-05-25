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
package org.geotools.data.wms.xml;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.geotools.data.wms.xml.WMSComplexTypes.LatitudeType;
import org.geotools.data.wms.xml.WMSComplexTypes.LongitudeType;
import org.geotools.data.wms.xml.WMSComplexTypes.OperationType;
import org.geotools.data.wms.xml.WMSComplexTypes._AttributionType;
import org.geotools.data.wms.xml.WMSComplexTypes._AuthorityURLType;
import org.geotools.data.wms.xml.WMSComplexTypes._BLANKType;
import org.geotools.data.wms.xml.WMSComplexTypes._BMPType;
import org.geotools.data.wms.xml.WMSComplexTypes._BoundingBoxType;
import org.geotools.data.wms.xml.WMSComplexTypes._CW_WKBType;
import org.geotools.data.wms.xml.WMSComplexTypes._CapabilityType;
import org.geotools.data.wms.xml.WMSComplexTypes._ContactAddressType;
import org.geotools.data.wms.xml.WMSComplexTypes._ContactInformationType;
import org.geotools.data.wms.xml.WMSComplexTypes._ContactPersonPrimaryType;
import org.geotools.data.wms.xml.WMSComplexTypes._DCPTypeType;
import org.geotools.data.wms.xml.WMSComplexTypes._DataURLType;
import org.geotools.data.wms.xml.WMSComplexTypes._DimensionType;
import org.geotools.data.wms.xml.WMSComplexTypes._EX_GeographicBoundingBoxType;
import org.geotools.data.wms.xml.WMSComplexTypes._ExceptionType;
import org.geotools.data.wms.xml.WMSComplexTypes._ExtentType;
import org.geotools.data.wms.xml.WMSComplexTypes._FeatureListURLType;
import org.geotools.data.wms.xml.WMSComplexTypes._FormatType;
import org.geotools.data.wms.xml.WMSComplexTypes._GIFType;
import org.geotools.data.wms.xml.WMSComplexTypes._GML_1Type;
import org.geotools.data.wms.xml.WMSComplexTypes._GML_2Type;
import org.geotools.data.wms.xml.WMSComplexTypes._GML_3Type;
import org.geotools.data.wms.xml.WMSComplexTypes._GeoTIFFType;
import org.geotools.data.wms.xml.WMSComplexTypes._GetType;
import org.geotools.data.wms.xml.WMSComplexTypes._HTTPType;
import org.geotools.data.wms.xml.WMSComplexTypes._INIMAGEType;
import org.geotools.data.wms.xml.WMSComplexTypes._IdentifierType;
import org.geotools.data.wms.xml.WMSComplexTypes._JPEGType;
import org.geotools.data.wms.xml.WMSComplexTypes._KeywordListType;
import org.geotools.data.wms.xml.WMSComplexTypes._KeywordType;
import org.geotools.data.wms.xml.WMSComplexTypes._KeywordsType;
import org.geotools.data.wms.xml.WMSComplexTypes._LatLonBoundingBoxType;
import org.geotools.data.wms.xml.WMSComplexTypes._LayerType;
import org.geotools.data.wms.xml.WMSComplexTypes._LegendURLType;
import org.geotools.data.wms.xml.WMSComplexTypes._LogoURLType;
import org.geotools.data.wms.xml.WMSComplexTypes._MIMEType;
import org.geotools.data.wms.xml.WMSComplexTypes._MetadataURLType;
import org.geotools.data.wms.xml.WMSComplexTypes._OnlineResourceType;
import org.geotools.data.wms.xml.WMSComplexTypes._PNGType;
import org.geotools.data.wms.xml.WMSComplexTypes._PPMType;
import org.geotools.data.wms.xml.WMSComplexTypes._PostType;
import org.geotools.data.wms.xml.WMSComplexTypes._RequestType;
import org.geotools.data.wms.xml.WMSComplexTypes._SVGType;
import org.geotools.data.wms.xml.WMSComplexTypes._ScaleHintType;
import org.geotools.data.wms.xml.WMSComplexTypes._ServiceException;
import org.geotools.data.wms.xml.WMSComplexTypes._ServiceExceptionReport;
import org.geotools.data.wms.xml.WMSComplexTypes._ServiceType;
import org.geotools.data.wms.xml.WMSComplexTypes._StyleSheetURLType;
import org.geotools.data.wms.xml.WMSComplexTypes._StyleType;
import org.geotools.data.wms.xml.WMSComplexTypes._StyleURLType;
import org.geotools.data.wms.xml.WMSComplexTypes._TIFFType;
import org.geotools.data.wms.xml.WMSComplexTypes._UserDefinedSymbolizationType;
import org.geotools.data.wms.xml.WMSComplexTypes._VendorSpecificCapabilitiesType;
import org.geotools.data.wms.xml.WMSComplexTypes._WBMPType;
import org.geotools.data.wms.xml.WMSComplexTypes._WMS_CapabilitiesType;
import org.geotools.data.wms.xml.WMSComplexTypes._WMS_XMLType;
import org.geotools.data.wms.xml.WMSComplexTypes._WMTException;
import org.geotools.data.wms.xml.WMSComplexTypes._WMT_MS_CapabilitiesType;
import org.geotools.data.wms.xml.WMSComplexTypes._WebCGMType;
import org.geotools.data.wms.xml.WMSComplexTypes.__ExtendedCapabilitiesType;
import org.geotools.data.wms.xml.WMSDescribeLayerTypes.WMS_DescribeLayerResponse;
import org.geotools.data.wms.xml.WMSDescribeLayerTypes._LayerDescription;
import org.geotools.data.wms.xml.WMSDescribeLayerTypes._Query;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.handlers.IgnoreHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.AttributeValue;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.AttributeGT;
import org.geotools.xml.schema.impl.AttributeValueGT;
import org.geotools.xml.schema.impl.FacetGT;
import org.geotools.xml.schema.impl.SimpleTypeGT;
import org.geotools.xml.xLink.XLinkSchema;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Richard Gould
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 * @source $URL$
 */
public class WMSSchema implements Schema {

    private static Schema instance = new WMSSchema();
    public static URI NAMESPACE = makeURI("http://www.opengis.net/wms");
    
    static final Element[] elements = new Element[] {
        new WMSElement("WMS_Capabilities", _WMS_CapabilitiesType.getInstance()),
        new WMSElement("WMT_MS_Capabilities", _WMT_MS_CapabilitiesType.getInstance()),
        
        new WMSElement("Name", XSISimpleTypes.String.getInstance()),
        new WMSElement("Title", XSISimpleTypes.String.getInstance()),
        new WMSElement("Abstract", XSISimpleTypes.String.getInstance()),
        new WMSElement("KeywordList", _KeywordListType.getInstance()), 
        new WMSElement("Keyword", _KeywordType.getInstance()),
        new WMSElement("Keywords", _KeywordsType.getInstance()),
        new WMSElement("OnlineResource", _OnlineResourceType.getInstance()),
        new WMSElement("Format", _FormatType.getInstance()),
        
        new WMSElement("Service", _ServiceType.getInstance()),
        new WMSElement("ContactInformation", _ContactInformationType.getInstance()),
        new WMSElement("ContactPersonPrimary", _ContactPersonPrimaryType.getInstance()),
        new WMSElement("ContactPerson", XSISimpleTypes.String.getInstance()),
        new WMSElement("ContactOrganization", XSISimpleTypes.String.getInstance()),
        new WMSElement("ContactPosition", XSISimpleTypes.String.getInstance()),
        new WMSElement("ContactAddress", _ContactAddressType.getInstance()),
        new WMSElement("AddressType", XSISimpleTypes.String.getInstance()),
        new WMSElement("Address", XSISimpleTypes.String.getInstance()),
        new WMSElement("City", XSISimpleTypes.String.getInstance()),
        new WMSElement("StateOrProvince", XSISimpleTypes.String.getInstance()),
        new WMSElement("PostCode", XSISimpleTypes.String.getInstance()),
        new WMSElement("Country", XSISimpleTypes.String.getInstance()),
        new WMSElement("ContactVoiceTelephone", XSISimpleTypes.String.getInstance()),
        new WMSElement("ContactFacsimileTelephone", XSISimpleTypes.String.getInstance()),
        new WMSElement("ContactElectronicMailAddress", XSISimpleTypes.String.getInstance()),
        
        new WMSElement("Fees", XSISimpleTypes.String.getInstance()),
        new WMSElement("AccessConstraints", XSISimpleTypes.String.getInstance()),
        new WMSElement("LayerLimit", XSISimpleTypes.PositiveInteger.getInstance()),
        new WMSElement("MaxWidth", XSISimpleTypes.PositiveInteger.getInstance()),
        new WMSElement("MaxHeight", XSISimpleTypes.PositiveInteger.getInstance()),
        
        new WMSElement("Capability", _CapabilityType.getInstance()),
        new WMSElement("VendorSpecificCapabilities", _VendorSpecificCapabilitiesType.getInstance()),
        new WMSElement("UserDefinedSymbolization", _UserDefinedSymbolizationType.getInstance()),
        new WMSElement("SupportedSLDVersion", XSISimpleTypes.String.getInstance()),
        new WMSElement("Request", _RequestType.getInstance()),
        
        new WMSElement("Capabilities", OperationType.getInstance()),
        new WMSElement("GetCapabilities", OperationType.getInstance()),
        
        new WMSElement("Map", OperationType.getInstance()),
        new WMSElement("GetMap", OperationType.getInstance()),
        
        new WMSElement("FeatureInfo", OperationType.getInstance()),
        new WMSElement("GetFeatureInfo", OperationType.getInstance()),
        
        new WMSElement("DescribeLayer", OperationType.getInstance()),
        new WMSElement("GetLegendGraphic", OperationType.getInstance()),
        new WMSElement("GetStyles", OperationType.getInstance()),
        new WMSElement("PutStyles", OperationType.getInstance()),
        new WMSElement("_ExtendedOperation", OperationType.getInstance()), //is abstract
        
        new WMSElement("DCPType", _DCPTypeType.getInstance()),
        new WMSElement("HTTP", _HTTPType.getInstance()),
        new WMSElement("Get", _GetType.getInstance()),
        new WMSElement("Post", _PostType.getInstance()),
                
        new WMSElement("Exception", _ExceptionType.getInstance()),
        new WMSElement("_ExtendedCapabilities", __ExtendedCapabilitiesType.getInstance()),
        
        new WMSElement("Layer", _LayerType.getInstance()),
        new WMSElement("CRS", XSISimpleTypes.String.getInstance()),
        new WMSElement("EX_GeographicBoundingBox", _EX_GeographicBoundingBoxType.getInstance()),
        new WMSElement("westBoundLongitude", LongitudeType.getInstance()),
        new WMSElement("eastBoundLongitude", LongitudeType.getInstance()),
        new WMSElement("southBoundLatitude", LatitudeType.getInstance()),
        new WMSElement("northBoundLatitude", LatitudeType.getInstance()),
        new WMSElement("LatLonBoundingBox", _LatLonBoundingBoxType.getInstance()),
        new WMSElement("BoundingBox", _BoundingBoxType.getInstance()),
        new WMSElement("Dimension", _DimensionType.getInstance()),
        new WMSElement("Extent", _ExtentType.getInstance()),
        new WMSElement("Attribution", _AttributionType.getInstance()),
        new WMSElement("LogoURL", _LogoURLType.getInstance()),
        new WMSElement("MetadataURL", _MetadataURLType.getInstance()),
        new WMSElement("AuthorityURL", _AuthorityURLType.getInstance()),
        new WMSElement("Identifier", _IdentifierType.getInstance()),
        new WMSElement("DataURL", _DataURLType.getInstance()),
        new WMSElement("FeatureListURL", _FeatureListURLType.getInstance()),
        new WMSElement("Style", _StyleType.getInstance()),
        new WMSElement("LegendURL", _LegendURLType.getInstance()),
        new WMSElement("StyleSheetURL", _StyleSheetURLType.getInstance()),
        new WMSElement("StyleURL", _StyleURLType.getInstance()),
        new WMSElement("MinScaleDenominator", XSISimpleTypes.Double.getInstance()),
        new WMSElement("MaxScaleDenominator", XSISimpleTypes.Double.getInstance()),
        new WMSElement("ScaleHint", _ScaleHintType.getInstance()),
        new WMSElement("SRS", XSISimpleTypes.String.getInstance()),
        
        //1.0.0 format elements
        new WMSElement("GIF", _GIFType.getInstance()),
        new WMSElement("JPEG", _JPEGType.getInstance()),
        new WMSElement("PNG", _PNGType.getInstance()),
        new WMSElement("PPM", _PPMType.getInstance()),
        new WMSElement("TIFF", _TIFFType.getInstance()),
        new WMSElement("GeoTIFF", _GeoTIFFType.getInstance()),
        new WMSElement("WebCGM", _WebCGMType.getInstance()),
        new WMSElement("SVG", _SVGType.getInstance()),
        new WMSElement("WMS_XML", _WMS_XMLType.getInstance()),
        new WMSElement("GML.1", _GML_1Type.getInstance()),
        new WMSElement("GML.2", _GML_2Type.getInstance()),
        new WMSElement("GML.3", _GML_3Type.getInstance()),
        new WMSElement("BMP", _BMPType.getInstance()),
        new WMSElement("WBMP", _WBMPType.getInstance()),
        new WMSElement("MIME", _MIMEType.getInstance()),
        new WMSElement("INIMAGE", _INIMAGEType.getInstance()),
        new WMSElement("BLANK", _BLANKType.getInstance()),
        new WMSElement("CW_WKB", _CW_WKBType.getInstance()),
        
        new WMSElement("WMS_DescribeLayerResponse", WMS_DescribeLayerResponse.getInstance()),
        new WMSElement("LayerDescription", _LayerDescription.getInstance()),
        new WMSElement("Query", _Query.getInstance()),
        
        //Service Exception stuff
        new WMSElement("WMTException", _WMTException.getInstance()),
        new WMSElement("ServiceExceptionReport", _ServiceExceptionReport.getInstance()),
        new WMSElement("ServiceException", _ServiceException.getInstance()),
        
        new WMSElement(IgnoreHandler.NAME, new WMSSchema.WMSIgnoreType(), 0, Integer.MAX_VALUE)
    };
    
    static final ComplexType[] complexTypes = new ComplexType[] {
        OperationType.getInstance()
    };

    static final SimpleType[] simpleTypes = new SimpleType[] {
        new SimpleTypeGT(null, "longitudeType", NAMESPACE, SimpleType.RESTRICTION,
                new SimpleType[] { XSISimpleTypes.String.getInstance() },
                	new Facet[] { 
                		new FacetGT(Facet.MININCLUSIVE, "-180"),
                		new FacetGT(Facet.MAXINCLUSIVE, "180") },
                	SimpleType.NONE
                ),
                
        new SimpleTypeGT(null, "latitudeType", NAMESPACE, SimpleType.RESTRICTION,
                new SimpleType[] { XSISimpleTypes.String.getInstance() },
                	new Facet[] { 
                		new FacetGT(Facet.MININCLUSIVE, "-90"),
                		new FacetGT(Facet.MAXINCLUSIVE, "90") },
                SimpleType.NONE
        ),

    };

    public AttributeGroup[] getAttributeGroups() {
        return new AttributeGroup[0];
    }

    public Attribute[] getAttributes() {
        return new Attribute[0];
    }

    public int getBlockDefault() {
        return NONE;
    }

    public ComplexType[] getComplexTypes() {
        return complexTypes;
    }

    public Element[] getElements() {
        return elements;
    }

    public int getFinalDefault() {
        return NONE;
    }

    public Group[] getGroups() {
        return new Group[0];
    }

    public String getId() {
        return null;
    }

    private static Schema[] imports = new Schema[]{
            XLinkSchema.getInstance()
    };
    public Schema[] getImports() {
        return imports;
    }

    public String getPrefix() {
        return "wms";
    }

    public SimpleType[] getSimpleTypes() {
        return simpleTypes;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.Schema#getTargetNamespace()
     */
    public URI getTargetNamespace() {
        return NAMESPACE;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.Schema#getURI()
     */
    public URI getURI() {
        return NAMESPACE;
    }

    public String getVersion() {
        return "1.3.0";
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.Schema#includesURI(java.net.URI)
     */
    public boolean includesURI( URI uri ) {
        //We don't need to read the definition at all
        //--this is a specification, it shouldn't change.
        return true;
    }

    public boolean isAttributeFormDefault() {
        return true;
    }

    public boolean isElementFormDefault() {
        return true;
    }
    
    public static Schema getInstance() {
        return instance;
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }

    // convinience method to deal with the URISyntaxException
    private static URI makeURI(String s) {
        try {
            return new URI(s);
        } catch (URISyntaxException e) {
            // do nothing
            return null;
        }
    }
    
    static class WMSElement implements Element {
        
        private int max;
        private int min;
        private String name;
        private Type type;

        /**
         * @param name
         * @param type
         */
        public WMSElement( String name, Type type ) {
            super();
            this.name = name;
            this.type = type;
            this.min = 1;
            this.max = 1;
        }
        /**
         * @param max
         * @param min
         * @param name
         * @param type
         */
        public WMSElement( String name, Type type, int min, int max ) {
            super();
            this.max = max;
            this.min = min;
            this.name = name;
            this.type = type;
        }
        private WMSElement() {
            
        }

        public boolean isAbstract() {
            return false;
        }

        public int getBlock() {
            return NONE;
        }

        public String getDefault() {
            //TODO terminate
            return null;
        }

        public int getFinal() {
            return NONE;
        }

        public String getFixed() {
            // TODO Terminate
            return null;
        }

        public boolean isForm() {
            // TODO Terminate
            return false;
        }

        public String getId() {
            return null;
        }

        public int getMaxOccurs() {
            // TODO Terminate
            return max;
        }

        public int getMinOccurs() {
            // TODO Terminate
            return min;
        }

        public String getName() {
            // TODO Terminate
            return name;
        }

        public URI getNamespace() {
            return NAMESPACE;
        }

        public boolean isNillable() {
            // TODO Terminate
            return false;
        }

        public Element getSubstitutionGroup() {
            // TODO Terminate
            return null;
        }

        public Type getType() {
            // TODO Terminate
            return type;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ElementGrouping#getGrouping()
         */
        public int getGrouping() {
            // TODO Auto-generated method stub
            return ELEMENT;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ElementGrouping#findChildElement(java.lang.String)
         */
        public Element findChildElement( String name ) {
            return (this.name!=null && this.name.equals(name))?this:null;
        }
		public Element findChildElement(String localName, URI namespaceURI) {
            return (this.name!=null 
            		&& this.name.equals(localName)
            		&& getNamespace().equals(namespaceURI))?this:null;
		}
    }
    
    static abstract class WMSComplexType implements ComplexType {

        public Type getParent() {
            return null;
        }

        public boolean isAbstract() {
            return false;
        }

        public String getAnyAttributeNameSpace() {
            return null;
        }

        public int getBlock() {
            return NONE;
        }

        public int getFinal() {
            return NONE;
        }
        
        public String getId() {
            return null;
        }

        public boolean isMixed() {
            return false;
        }

        public boolean isDerived() {
            return false;
        }

        public boolean cache( Element element, Map hints ) {
            return true;
        }

        public URI getNamespace() {
            return NAMESPACE;
        }

        public Element findChildElement( String name ) {
            return (getChild() == null) ? null : getChild().findChildElement(name);
        }

        protected boolean sameName( Element element, ElementValue value ) {
            return element.getName().equals(value.getElement().getName());
        }
    }
    
    static abstract class WMSSimpleType implements SimpleType {

        public int getFinal() {
            return NONE;
        }

        public String getId() {
            return null;
        }

        public boolean canCreateAttributes( Attribute attribute, Object value, Map hints ) {
            return false;
        }

        public URI getNamespace() {
            return NAMESPACE;
        }

        public Element findChildElement( String name ) {
            return null;
        }
        
        public AttributeValue toAttribute(Attribute attribute, Object value,
                Map hints) throws OperationNotSupportedException {
            return new AttributeValueGT(attribute, value.toString());
        }
    }
    
    static class WMSIgnoreType implements Type {
       
          /* (non-Javadoc)
       * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
       */
      public boolean canEncode(Element element, Object value, Map hints) {
         // TODO Auto-generated method stub
         return false;
      }
      /* (non-Javadoc)
       * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
       */
      public void encode(Element element, Object value, PrintHandler output, Map hints)
            throws IOException, OperationNotSupportedException {
         // TODO Auto-generated method stub

      }
      /* (non-Javadoc)
       * @see org.geotools.xml.schema.Type#findChildElement(java.lang.String)
       */
      public Element findChildElement(String name) {
         // TODO Auto-generated method stub
         return null;
      }
      /* (non-Javadoc)
       * @see org.geotools.xml.schema.Type#getInstanceType()
       */
      public Class getInstanceType() {
         // TODO Auto-generated method stub
         return null;
      }
      /* (non-Javadoc)
       * @see org.geotools.xml.schema.Type#getName()
       */
      public String getName() {
         // TODO Auto-generated method stub
         return "Ignore";
      }
      /* (non-Javadoc)
       * @see org.geotools.xml.schema.Type#getNamespace()
       */
      public URI getNamespace() {
         return NAMESPACE;
      }
      /* (non-Javadoc)
       * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
       */
      public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
            throws SAXException, OperationNotSupportedException {
         // TODO Auto-generated method stub
         return null;
      }
}
    
    
    static class WMSAttribute extends AttributeGT {

        public WMSAttribute( String id, String name, URI namespace, SimpleType type, int use, String _default, String fixed, boolean form ) {
            super(id, name, namespace, type, use, _default, fixed, form);
        }
        
        public WMSAttribute(String name, SimpleType simpleType) {
            super(null, name, WMSSchema.NAMESPACE, simpleType, OPTIONAL, null,
                null, false);
        }
    }
}
