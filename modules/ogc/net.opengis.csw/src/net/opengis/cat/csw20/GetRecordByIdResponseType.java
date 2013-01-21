/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;
import java.math.BigInteger;
import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>GeRecordsById results</b></em>'.
 * <!-- end-user-doc -->

 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.GetRecordByIdResponseType#getAbstractRecordGroup <em>Abstract Record Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordByIdResponseType#getAbstractRecord <em>Abstract Record</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordByIdResponseType#getAny <em>Any</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getGetRecordByIdResponseType()
 * @model extendedMetaData="name='GetRecordsByIdType' kind='elementOnly'"
 * @generated
 */
public interface GetRecordByIdResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Abstract Record Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Record Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Record Group</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordByIdResponseType_AbstractRecordGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='AbstractRecord:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getAbstractRecordGroup();

    /**
     * Returns the value of the '<em><b>Abstract Record</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.AbstractRecordType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Record</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Record</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordByIdResponseType_AbstractRecord()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractRecord' namespace='##targetNamespace' group='AbstractRecord:group'"
     * @generated
     */
    EList<AbstractRecordType> getAbstractRecord();

    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordByIdResponseType_Any()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':2' processing='strict'"
     * @generated
     */
    FeatureMap getAny();

} // SearchResultsType
