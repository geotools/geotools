/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.wfs.v2_0.bindings;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.ows11.KeywordsType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.OutputFormatListType;
import net.opengis.wfs20.Wfs20Factory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;

public class FeatureTypeTypeBinding extends AbstractComplexEMFBinding {
    private Wfs20Factory factory;

    public FeatureTypeTypeBinding(Wfs20Factory factory) {
        super(factory);
        this.factory = factory;
    }

    @Override
    public QName getTarget() {
        return WFS.FeatureTypeType;
    }

    @Override
    public Class getType() {
        return FeatureTypeType.class;
    }

    @SuppressWarnings({"unchecked", "nls"})
    @Override
    protected void setProperty(EObject object, String property, Object value, boolean lax) {
        if ("OtherCRS".equals(property)) {
            String stringValue = null;
            if (value instanceof String) {
                stringValue = (String) value;
            } else if (value instanceof URI) {
                stringValue = ((URI) value).toString();
            }

            if (stringValue != null) {
                ((FeatureTypeType) object).getOtherCRS().add(stringValue);
                return;
            }
        } else if ("OtherSRS".equals(property)) {
            if (value instanceof Collection) {
                Collection<URI> formatListAsUris = (Collection<URI>) value;
                List<String> formatListAsString = new ArrayList<String>();
                for (URI uri : formatListAsUris) {
                    formatListAsString.add(uri.toString());
                }
                value = formatListAsString;
            } else {
                if (value instanceof URI) {
                    value = ((URI) value).toString();
                }
            }
        } else if ("Keywords".equals(property)) {
            if (value instanceof String) {
                String[] split = ((String) value).split(",");
                KeywordsType kwd = Ows11Factory.eINSTANCE.createKeywordsType();
                for (int i = 0; i < split.length; i++) {
                    String kw = split[i].trim();
                    kwd.getKeyword().add(kw);
                }
                ((FeatureTypeType) object).getKeywords().add(kwd);
                return;
            }
        } else if ("OutputFormats".equals(property)) {
            if (value != null) {
                OutputFormatListType oflt = ((FeatureTypeType) object).getOutputFormats();

                if (oflt == null) {
                    oflt = ((Wfs20Factory) factory).createOutputFormatListType();
                }

                if (value instanceof Map<?, ?>) {
                    oflt.getFormat().addAll(((Map<String, ArrayList<String>>) value).get("Format"));
                } else {
                    oflt.getFormat().add(value.toString());
                }

                ((FeatureTypeType) object).setOutputFormats(oflt);
                return;
            }
        }

        super.setProperty(object, property, value, lax);
    }
}
