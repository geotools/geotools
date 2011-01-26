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

import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.FacetGT;

public class ogcSimpleTypes {

    protected static class CapabilitiesSectionType extends ogcSimpleType {
        private static SimpleType instance = new CapabilitiesSectionType();
        public static SimpleType getInstance() {
            return instance;
        }
        private static SimpleType[] parents = new SimpleType[]{
                org.geotools.xml.xsi.XSISimpleTypes.String.getInstance()/* simpleType name is string */
        };
        private static Facet[] facets = new Facet[]{
            new FacetGT(Facet.ENUMERATION, GetCapabilitiesRequest.SECTION_ALL),
            new FacetGT(Facet.ENUMERATION, GetCapabilitiesRequest.SECTION_SERVICE),
            new FacetGT(Facet.ENUMERATION, GetCapabilitiesRequest.SECTION_OPERATIONS),
            new FacetGT(Facet.ENUMERATION, GetCapabilitiesRequest.SECTION_CONTENT),
            new FacetGT(Facet.ENUMERATION, GetCapabilitiesRequest.SECTION_COMMON)
        };

        private CapabilitiesSectionType() {
            super("ogc:CapabilitiesSectionType", 4, parents, facets);
        }
        
        
    }

    protected static class FormatType extends ogcSimpleType {
        private static SimpleType instance = new FormatType();
        public static SimpleType getInstance() {
            return instance;
        }
        private static SimpleType[] parents = new SimpleType[]{org.geotools.xml.xsi.XSISimpleTypes.String
                .getInstance()/* simpleType name is string */
        };
        private static Facet[] facets = new Facet[]{
            new FacetGT(Facet.ENUMERATION, "image/gif"),
            new FacetGT(Facet.ENUMERATION, "image/jpg"),
            new FacetGT(Facet.ENUMERATION, "image/png")
        };

        private FormatType() {
            super("FormatType", 4, parents, facets);
        }
    }
    protected static class OWSType extends ogcSimpleType {
        private static SimpleType instance = new OWSType();
        public static SimpleType getInstance() {
            return instance;
        }
        private static SimpleType[] parents = new SimpleType[]{org.geotools.xml.xsi.XSISimpleTypes.String
                .getInstance()/* simpleType name is string */
        };
        private static Facet[] facets = new Facet[]{
            new FacetGT(Facet.ENUMERATION, "WMS")
        };

        private OWSType() {
            super("OWSType", 4, parents, facets);
        }
    }
    protected static class ExceptionsType extends ogcSimpleType {
        private static SimpleType instance = new ExceptionsType();
        public static SimpleType getInstance() {
            return instance;
        }
        private static SimpleType[] parents = new SimpleType[]{org.geotools.xml.xsi.XSISimpleTypes.String
                .getInstance()/* simpleType name is string */
        };
        private static Facet[] facets = new Facet[]{
            new FacetGT(Facet.ENUMERATION, "application/vnd.ogc.se+inimage"),
            new FacetGT(Facet.ENUMERATION, "application/vnd.ogc.se+xml")
        };

        private ExceptionsType() {
            super("ExceptionsType", 4, parents, facets);
        }
        
        
    }
}
