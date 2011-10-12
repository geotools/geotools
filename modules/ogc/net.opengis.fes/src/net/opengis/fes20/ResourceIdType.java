/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Id Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.ResourceIdType#getEndDate <em>End Date</em>}</li>
 *   <li>{@link net.opengis.fes20.ResourceIdType#getPreviousRid <em>Previous Rid</em>}</li>
 *   <li>{@link net.opengis.fes20.ResourceIdType#getRid <em>Rid</em>}</li>
 *   <li>{@link net.opengis.fes20.ResourceIdType#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link net.opengis.fes20.ResourceIdType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getResourceIdType()
 * @model extendedMetaData="name='ResourceIdType' kind='empty'"
 * @generated
 */
public interface ResourceIdType extends AbstractIdType {
    /**
     * Returns the value of the '<em><b>End Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>End Date</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>End Date</em>' attribute.
     * @see #setEndDate(XMLGregorianCalendar)
     * @see net.opengis.fes20.Fes20Package#getResourceIdType_EndDate()
     * @model dataType="org.eclipse.emf.ecore.xml.type.DateTime"
     *        extendedMetaData="kind='attribute' name='endDate'"
     * @generated
     */
    XMLGregorianCalendar getEndDate();

    /**
     * Sets the value of the '{@link net.opengis.fes20.ResourceIdType#getEndDate <em>End Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End Date</em>' attribute.
     * @see #getEndDate()
     * @generated
     */
    void setEndDate(XMLGregorianCalendar value);

    /**
     * Returns the value of the '<em><b>Previous Rid</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Previous Rid</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Previous Rid</em>' attribute.
     * @see #setPreviousRid(String)
     * @see net.opengis.fes20.Fes20Package#getResourceIdType_PreviousRid()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='previousRid'"
     * @generated
     */
    String getPreviousRid();

    /**
     * Sets the value of the '{@link net.opengis.fes20.ResourceIdType#getPreviousRid <em>Previous Rid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Previous Rid</em>' attribute.
     * @see #getPreviousRid()
     * @generated
     */
    void setPreviousRid(String value);

    /**
     * Returns the value of the '<em><b>Rid</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Rid</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Rid</em>' attribute.
     * @see #setRid(String)
     * @see net.opengis.fes20.Fes20Package#getResourceIdType_Rid()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='rid'"
     * @generated
     */
    String getRid();

    /**
     * Sets the value of the '{@link net.opengis.fes20.ResourceIdType#getRid <em>Rid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rid</em>' attribute.
     * @see #getRid()
     * @generated
     */
    void setRid(String value);

    /**
     * Returns the value of the '<em><b>Start Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Start Date</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Start Date</em>' attribute.
     * @see #setStartDate(XMLGregorianCalendar)
     * @see net.opengis.fes20.Fes20Package#getResourceIdType_StartDate()
     * @model dataType="org.eclipse.emf.ecore.xml.type.DateTime"
     *        extendedMetaData="kind='attribute' name='startDate'"
     * @generated
     */
    XMLGregorianCalendar getStartDate();

    /**
     * Sets the value of the '{@link net.opengis.fes20.ResourceIdType#getStartDate <em>Start Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Date</em>' attribute.
     * @see #getStartDate()
     * @generated
     */
    void setStartDate(XMLGregorianCalendar value);

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(Object)
     * @see net.opengis.fes20.Fes20Package#getResourceIdType_Version()
     * @model dataType="net.opengis.fes20.VersionType"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    Object getVersion();

    /**
     * Sets the value of the '{@link net.opengis.fes20.ResourceIdType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(Object value);

} // ResourceIdType
