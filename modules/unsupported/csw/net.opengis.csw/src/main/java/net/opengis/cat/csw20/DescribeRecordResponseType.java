/**
 * <copyright>
 * (C) 2012, Open Source Geospatial Foundation (OSGeo)
 * (C) 2004,2010 Open Geospatial Consortium.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Describe Record Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The response contains a list of matching schema components
 *          in the requested schema language.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.DescribeRecordResponseType#getSchemaComponent <em>Schema Component</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getDescribeRecordResponseType()
 * @model extendedMetaData="name='DescribeRecordResponseType' kind='elementOnly'"
 * @generated
 */
public interface DescribeRecordResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Schema Component</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.SchemaComponentType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Schema Component</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Schema Component</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getDescribeRecordResponseType_SchemaComponent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='SchemaComponent' namespace='##targetNamespace'"
     * @generated
     */
    EList<SchemaComponentType> getSchemaComponent();

} // DescribeRecordResponseType
