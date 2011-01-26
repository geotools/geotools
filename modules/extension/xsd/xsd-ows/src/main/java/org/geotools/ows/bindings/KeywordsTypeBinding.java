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
package org.geotools.ows.bindings;

import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.Ows10Factory;
import javax.xml.namespace.QName;
import org.geotools.ows.OWS;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ows:KeywordsType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="KeywordsType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Unordered list of one or more commonly used or formalised word(s) or phrase(s) used to describe the subject. When needed, the optional "type" can name the type of the associated list of keywords that shall all have the same type. Also when needed, the codeSpace attribute of that "type" can reference the type name authority and/or thesaurus. &lt;/documentation&gt;
 *          &lt;documentation&gt;For OWS use, the optional thesaurusName element was omitted as being complex information that could be referenced by the codeSpace attribute of the Type element. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" name="Keyword" type="string"/&gt;
 *          &lt;element minOccurs="0" name="Type" type="ows:CodeType"/&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class KeywordsTypeBinding extends AbstractComplexEMFBinding {
    public KeywordsTypeBinding(Ows10Factory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OWS.KeywordsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return super.getType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
