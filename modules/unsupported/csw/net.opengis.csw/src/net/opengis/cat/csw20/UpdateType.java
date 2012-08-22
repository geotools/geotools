/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Update Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             Update statements may replace an entire record or only update part
 *             of a record:
 *             1) To replace an existing record, include a new instance of the
 *                record;
 *             2) To update selected properties of an existing record, include
 *                a set of RecordProperty elements. The scope of the update
 *                statement  is determined by the Constraint element.
 *                The 'handle' is a local identifier for the action.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.UpdateType#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.UpdateType#getRecordProperty <em>Record Property</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.UpdateType#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.UpdateType#getHandle <em>Handle</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getUpdateType()
 * @model extendedMetaData="name='UpdateType' kind='elementOnly'"
 * @generated
 */
public interface UpdateType extends EObject {
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
     * @see net.opengis.cat.csw20.Csw20Package#getUpdateType_Any()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':0' processing='strict'"
     * @generated
     */
    FeatureMap getAny();

    /**
     * Returns the value of the '<em><b>Record Property</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.RecordPropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *             The RecordProperty element is used to specify the new
     *             value of a record property in an update statement.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Record Property</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getUpdateType_RecordProperty()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='RecordProperty' namespace='##targetNamespace'"
     * @generated
     */
    EList<RecordPropertyType> getRecordProperty();

    /**
     * Returns the value of the '<em><b>Constraint</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Constraint</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Constraint</em>' containment reference.
     * @see #setConstraint(QueryConstraintType)
     * @see net.opengis.cat.csw20.Csw20Package#getUpdateType_Constraint()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Constraint' namespace='##targetNamespace'"
     * @generated
     */
    QueryConstraintType getConstraint();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.UpdateType#getConstraint <em>Constraint</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Constraint</em>' containment reference.
     * @see #getConstraint()
     * @generated
     */
    void setConstraint(QueryConstraintType value);

    /**
     * Returns the value of the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Handle</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Handle</em>' attribute.
     * @see #setHandle(String)
     * @see net.opengis.cat.csw20.Csw20Package#getUpdateType_Handle()
     * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
     *        extendedMetaData="kind='attribute' name='handle'"
     * @generated
     */
    String getHandle();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.UpdateType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
    void setHandle(String value);

} // UpdateType
