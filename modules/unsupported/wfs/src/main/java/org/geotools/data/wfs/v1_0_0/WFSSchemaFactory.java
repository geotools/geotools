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
package org.geotools.data.wfs.v1_0_0;

import java.net.URI;

import org.geotools.ows.ServiceException;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.XSISAXHandler;
import org.geotools.xml.filter.FilterSchema;
import org.geotools.xml.handlers.xsi.RootHandler;
import org.geotools.xml.schema.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 *
 * @source $URL$
 */
public class WFSSchemaFactory extends SchemaFactory {
    // HACK for setting up the static field to use.
    protected WFSSchemaFactory() {
        is = this;
    }

    protected XSISAXHandler getSAXHandler(URI uri) {
        return new WFSXSISAXHandler(uri);
    }

    protected static class WFSXSISAXHandler extends XSISAXHandler {
        /**
         * 
         * @param uri
         */
        public WFSXSISAXHandler(URI uri) {
            super(uri);
            rootHandler = new WFSRootHandler(uri);
        }
    }

    protected static class WFSRootHandler extends RootHandler {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = "org.geotools.data.wfs.WFSSchemaFactory.WFSRootHandler".hashCode();
        private ServiceExceptionReportHandler se = null;

        /**
         * 
         * @param uri
         */
        public WFSRootHandler(URI uri) {
            super(uri);
        }

        /**
         * 
         * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String)
         */
        public XSIElementHandler getHandler(String namespaceURI,
            String localName){
            XSIElementHandler r = null;
            r = super.getHandler(namespaceURI, localName);

            if (r != null) {
                return r;
            }

            if ("ServiceExceptionReport".equalsIgnoreCase(localName)
                    && FilterSchema.NAMESPACE.toString().equalsIgnoreCase(namespaceURI)) {
                //                FilterSchema.getInstance().getElements()[37]
                //                ServiceException
                if (se == null) {
                    se = new ServiceExceptionReportHandler();
                }

                return se;
            }

            return null;
        }

        /**
         * 
         * @see org.geotools.xml.handlers.xsi.RootHandler#getSchema()
         */
        public Schema getSchema() throws SAXException {
            if (se != null) {
                if (se.getException() != null) {
                    throw se.getException();
                }
            }

            return super.getSchema();
        }
    }

    private static class ServiceExceptionReportHandler extends XSIElementHandler {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = "org.geotools.data.wfs.WFSSchemaFactory.ServiceExceptionReportHandler".hashCode();
        private ServiceException exception;
        private boolean inside = false;

        /**
         * @see org.geotools.xml.XSIElementHandler#getHandlerType()
         */
        public int getHandlerType() {
            return DEFAULT;
        }

        protected ServiceException getException() {
            return exception;
        }

        /**
         * @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String,
         *      java.lang.String)
         */
        public void endElement(String namespaceURI, String localName){
            inside = false;
        }

        /**
         * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String,
         *      java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String namespaceURI, String localName,
            Attributes attr){
            if ("ServiceException".equalsIgnoreCase(localName)) {
                inside = true;

                if (attr != null) {
                    String locator = attr.getValue("", "locator");

                    if (locator == null) {
                        locator = attr.getValue(namespaceURI, "locator");
                    }

                    String code = attr.getValue("", "code");

                    if (code == null) {
                        code = attr.getValue(namespaceURI, "code");
                    }

                    exception = new ServiceException("", code, locator);
                }
            }
        }

        /**
         * @see org.geotools.xml.XSIElementHandler#characters(java.lang.String)
         */
        public void characters(String text){
            if (inside) {
                exception = new ServiceException(text, exception.getCode(),
                        exception.getLocator());
            }
        }

        /**
         * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String,
         *      java.lang.String)
         */
        public XSIElementHandler getHandler(String namespaceURI,
            String localName){
            if ("ServiceException".equalsIgnoreCase(localName)
                    && FilterSchema.NAMESPACE.toString().equalsIgnoreCase(namespaceURI)) {
                return this;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.XSIElementHandler#getLocalName()
         */
        public String getLocalName() {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * @see org.geotools.xml.XSIElementHandler#hashCode()
         */
        public int hashCode() {
            // TODO Auto-generated method stub
            return 0;
        }
    }
}
