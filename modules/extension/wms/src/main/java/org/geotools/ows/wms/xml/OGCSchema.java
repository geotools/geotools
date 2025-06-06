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
package org.geotools.ows.wms.xml;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import org.geotools.xml.gml.GMLSchema;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;

public class OGCSchema implements Schema {

    private static OGCSchema instance = new OGCSchema();

    protected OGCSchema() {
        // do nothing
    }

    public static OGCSchema getInstance() {
        return instance;
    }

    public static final URI NAMESPACE = loadNS();

    private static URI loadNS() {
        try {
            return new URI("http://www.opengis.net/ows");
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override
    public int getBlockDefault() {
        return 0;
    }

    @Override
    public int getFinalDefault() {
        return 0;
    }

    @Override
    public String getId() {
        return "null";
    }

    private static volatile Schema[] imports = null;

    @Override
    public Schema[] getImports() {
        if (imports == null) {
            imports = new Schema[] {GMLSchema.getInstance()};
        }
        return imports;
    }

    @Override
    public String getPrefix() {
        return "ogc";
    }

    @Override
    public URI getTargetNamespace() {
        return NAMESPACE;
    }

    @Override
    public URI getURI() {
        return NAMESPACE;
    }

    @Override
    public String getVersion() {
        return "null";
    }

    @Override
    public boolean includesURI(URI uri) {
        // // TODO fill me in!
        return false; // // safer
    }

    @Override
    public boolean isAttributeFormDefault() {
        return false;
    }

    @Override
    public boolean isElementFormDefault() {
        return false;
    }

    @Override
    public AttributeGroup[] getAttributeGroups() {
        return null;
    }

    @Override
    public Attribute[] getAttributes() {
        return null;
    }
    /** TODO comment here */
    private static volatile ComplexType[] complexTypes = null;

    @Override
    public ComplexType[] getComplexTypes() {
        if (complexTypes == null) {
            complexTypes = new ComplexType[] {ogcComplexTypes.VendorType.getInstance()};
        }
        return complexTypes;
    }
    /** TODO comment here */
    private static volatile Element[] elements = null;

    public static final int GET_CAPABILITIES = 0;
    public static final int GET_MAP = 1;
    public static final int GET_FEATURE_INFO = 2;

    @Override
    public Element[] getElements() {
        if (elements == null) {
            synchronized (OGCSchema.class) {
                if (elements == null) {
                    Element[] array = new Element[3];
                    array[GET_CAPABILITIES] = new ogcElement(
                            "GetCapabilities", ogcComplexTypes._GetCapabilities.getInstance(), null, 1, 1);
                    array[GET_MAP] = new ogcElement("GetMap", ogcComplexTypes._GetMap.getInstance(), null, 1, 1);
                    array[GET_FEATURE_INFO] = new ogcElement(
                            "ogc:GetFeatureInfo", ogcComplexTypes._GetFeatureInfo.getInstance(), null, 1, 1);
                    elements = array;
                }
            }
        }
        return elements;
    }

    @Override
    public Group[] getGroups() {
        return null;
    }
    /** TODO comment here */
    private static volatile SimpleType[] simpleTypes = null;

    @Override
    public SimpleType[] getSimpleTypes() {
        if (simpleTypes == null) {
            simpleTypes = new SimpleType[] {
                ogcSimpleTypes.CapabilitiesSectionType.getInstance(),
                ogcSimpleTypes.FormatType.getInstance(),
                ogcSimpleTypes.OWSType.getInstance(),
                ogcSimpleTypes.ExceptionsType.getInstance()
            };
        }
        return simpleTypes;
    }

    /** Returns the implementation hints. The default implementation returns en empty map. */
    @Override
    public Map<java.awt.RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
