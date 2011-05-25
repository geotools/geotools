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
package org.geotools.wfs.bindings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wfs.QueryType;
import net.opengis.wfs.WfsFactory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wfs:QueryType.
 * 
 * <p>
 * 
 * <pre>
 *         &lt;code&gt;
 *  &lt;xsd:complexType name=&quot;QueryType&quot;&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              The Query element is of type QueryType.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:choice maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;&gt;
 *              &lt;xsd:element ref=&quot;wfs:PropertyName&quot;&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                     The Property element is used to specify one or more
 *                     properties of a feature whose values are to be retrieved
 *                     by a Web Feature Service.
 *                     While a Web Feature Service should endeavour to satisfy
 *                     the exact request specified, in some instance this may
 *                     not be possible.  Specifically, a Web Feature Service
 *                     must generate a valid GML3 response to a Query operation.
 *                     The schema used to generate the output may include
 *                     properties that are mandatory.  In order that the output
 *                     validates, these mandatory properties must be specified
 *                     in the request.  If they are not, a Web Feature Service
 *                     may add them automatically to the Query before processing
 *                     it.  Thus a client application should, in general, be
 *                     prepared to receive more properties than it requested.
 *                     Of course, using the DescribeFeatureType request, a client
 *                     application can determine which properties are mandatory
 *                     and request them in the first place.
 *                  &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:element&gt;
 *              &lt;xsd:element ref=&quot;wfs:XlinkPropertyName&quot;/&gt;
 *              &lt;xsd:element ref=&quot;ogc:Function&quot;&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                     A function may be used as a select item in a query.
 *                     However, if a function is used, care must be taken
 *                     to ensure that the result type matches the type in the
 *                  &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:element&gt;
 *          &lt;/xsd:choice&gt;
 *          &lt;xsd:element maxOccurs=&quot;1&quot; minOccurs=&quot;0&quot; ref=&quot;ogc:Filter&quot;&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                  The Filter element is used to define spatial and/or non-spatial
 *                  constraints on query.  Spatial constrains use GML3 to specify
 *                  the constraining geometry.  A full description of the Filter
 *                  element can be found in the Filter Encoding Implementation
 *                  Specification.
 *               &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *          &lt;xsd:element maxOccurs=&quot;1&quot; minOccurs=&quot;0&quot; ref=&quot;ogc:SortBy&quot;&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                  The SortBy element is used specify property names whose
 *                  values should be used to order (upon presentation) the
 *                  set of feature instances that satisfy the query.
 *               &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name=&quot;handle&quot; type=&quot;xsd:string&quot; use=&quot;optional&quot;&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                 The handle attribute allows a client application
 *                 to assign a client-generated identifier for the
 *                 Query.  The handle is included to facilitate error
 *                 reporting.  If one Query in a GetFeature request
 *                 causes an exception, a WFS may report the handle
 *                 to indicate which query element failed.  If the a
 *                 handle is not present, the WFS may use other means
 *                 to localize the error (e.g. line numbers).
 *              &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *      &lt;xsd:attribute name=&quot;typeName&quot; type=&quot;wfs:TypeNameListType&quot; use=&quot;required&quot;&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                The typeName attribute is a list of one or more
 *                feature type names that indicate which types
 *                of feature instances should be included in the
 *                reponse set.  Specifying more than one typename
 *                indicates that a join operation is being performed.
 *                All the names in the typeName list must be valid
 *                types that belong to this query's feature content
 *                as defined by the GML Application Schema.
 *             &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *      &lt;xsd:attribute name=&quot;featureVersion&quot; type=&quot;xsd:string&quot; use=&quot;optional&quot;&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                For systems that implement versioning, the featureVersion
 *                attribute is used to specify which version of a particular
 *                feature instance is to be retrieved.  A value of ALL means
 *                that all versions should be retrieved.  An integer value
 *                'i', means that the ith version should be retrieve if it
 *                exists or the most recent version otherwise.
 *             &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *      &lt;xsd:attribute name=&quot;srsName&quot; type=&quot;xsd:anyURI&quot; use=&quot;optional&quot;&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                This attribute is used to specify a specific WFS-supported SRS
 *                that should be used for returned feature geometries.  The value
 *                may be the WFS StorageSRS value, DefaultRetrievalSRS value, or
 *                one of AdditionalSRS values.  If no srsName value is supplied,
 *                then the features will be returned using either the
 *                DefaultRetrievalSRS, if specified, and StorageSRS otherwise.
 *                For feature types with no spatial properties, this attribute
 *                must not be specified or ignored if it is specified.
 *             &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *  &lt;/xsd:complexType&gt;
 * &lt;/code&gt;
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL$
 */
@SuppressWarnings( { "nls", "unchecked" })
public class QueryTypeBinding extends AbstractComplexEMFBinding {
    public QueryTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.QueryType;
    }

    /**
     * Overrides to return the value of the "typeName" attribute as a single String instead of a
     * List. Otherwise typeName gets encoded as the {@link QueryType#getTypeName()} toString's value
     * which depends on the toString implementation of the internal java.util.List.
     * <p>
     * Also, if the requested property is "SortBy" and the QueryType has an empty sortby list,
     * returns null to avoid encoding an empty sortBy list
     * </p>
     */
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ("typeName".equals(name.getLocalPart())) {
            QueryType query = (QueryType) object;
            List typeName = query.getTypeName();
            StringBuilder typeNameList = new StringBuilder();
            if (typeName != null) {
                for (Iterator it = typeName.iterator(); it.hasNext();) {
                    Object o = it.next();
                    if (o instanceof QName) {
                        QName qName = (QName) o;
                        o = qName.getPrefix() + ":" + qName.getLocalPart(); 
                    }
                    
                    typeNameList.append(o);
                    if (it.hasNext()) {
                        typeNameList.append(",");
                    }
                }
            }
            return typeNameList.toString();
        } else if ("SortBy".equals(name.getLocalPart())) {
            QueryType query = (QueryType) object;
            List sortBy = query.getSortBy();
            if (sortBy != null && sortBy.size() == 0) {
                return null;
            }
        }

        return super.getProperty(object, name);
    }
    
    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        if ("typeName".equals(property)) {
            QueryType query = (QueryType) eObject;
            if (query.getTypeName() == null) {
                query.setTypeName(new ArrayList());
            }
        }
        
        super.setProperty(eObject, property, value, lax);
    }
}
