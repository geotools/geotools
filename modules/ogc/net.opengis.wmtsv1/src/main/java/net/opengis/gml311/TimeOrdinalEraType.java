/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Ordinal Era Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Ordinal temporal reference systems are often hierarchically structured 
 *       such that an ordinal era at a given level of the hierarchy includes a 
 *       sequence of shorter, coterminous ordinal eras. This captured using the member/group properties.  
 *       
 *       Note that in this schema, TIme Ordinal Era is patterned on TimeEdge, which is a variation from ISO 19108.  
 *       This is in order to fulfill the requirements of ordinal reference systems based on eras delimited by 
 *       named points or nodes, which are common in geology, archeology, etc.  
 *       
 *       This change is subject of a change proposal to ISO
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TimeOrdinalEraType#getRelatedTime <em>Related Time</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeOrdinalEraType#getStart <em>Start</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeOrdinalEraType#getEnd <em>End</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeOrdinalEraType#getExtent <em>Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeOrdinalEraType#getMember <em>Member</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeOrdinalEraType#getGroup <em>Group</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTimeOrdinalEraType()
 * @model extendedMetaData="name='TimeOrdinalEraType' kind='elementOnly'"
 * @generated
 */
public interface TimeOrdinalEraType extends DefinitionType {
    /**
     * Returns the value of the '<em><b>Related Time</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.RelatedTimeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Related Time</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Related Time</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTimeOrdinalEraType_RelatedTime()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='relatedTime' namespace='##targetNamespace'"
     * @generated
     */
    EList<RelatedTimeType> getRelatedTime();

    /**
     * Returns the value of the '<em><b>Start</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Start</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Start</em>' containment reference.
     * @see #setStart(TimeNodePropertyType)
     * @see net.opengis.gml311.Gml311Package#getTimeOrdinalEraType_Start()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='start' namespace='##targetNamespace'"
     * @generated
     */
    TimeNodePropertyType getStart();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeOrdinalEraType#getStart <em>Start</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start</em>' containment reference.
     * @see #getStart()
     * @generated
     */
    void setStart(TimeNodePropertyType value);

    /**
     * Returns the value of the '<em><b>End</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>End</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>End</em>' containment reference.
     * @see #setEnd(TimeNodePropertyType)
     * @see net.opengis.gml311.Gml311Package#getTimeOrdinalEraType_End()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='end' namespace='##targetNamespace'"
     * @generated
     */
    TimeNodePropertyType getEnd();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeOrdinalEraType#getEnd <em>End</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End</em>' containment reference.
     * @see #getEnd()
     * @generated
     */
    void setEnd(TimeNodePropertyType value);

    /**
     * Returns the value of the '<em><b>Extent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Extent</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Extent</em>' containment reference.
     * @see #setExtent(TimePeriodPropertyType)
     * @see net.opengis.gml311.Gml311Package#getTimeOrdinalEraType_Extent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='extent' namespace='##targetNamespace'"
     * @generated
     */
    TimePeriodPropertyType getExtent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeOrdinalEraType#getExtent <em>Extent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extent</em>' containment reference.
     * @see #getExtent()
     * @generated
     */
    void setExtent(TimePeriodPropertyType value);

    /**
     * Returns the value of the '<em><b>Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.TimeOrdinalEraPropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An Era may be composed of several member Eras. The "member" element implements the association to the Era at the next level down the hierarchy.  "member" follows the standard GML property pattern whereby its (complex) value may be either described fully inline, or may be the target of a link carried on the member element and described fully elsewhere, either in the same document or from another service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Member</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTimeOrdinalEraType_Member()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='member' namespace='##targetNamespace'"
     * @generated
     */
    EList<TimeOrdinalEraPropertyType> getMember();

    /**
     * Returns the value of the '<em><b>Group</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * In a particular Time System, an Era may be a member of a group.  The "group" element implements the back-pointer to the Era at the next level up in the hierarchy. 
     * 
     * If the hierarchy is represented by describing the nested components fully in the their nested position inside "member" elements, then the parent can be easily inferred, so the group property is unnecessary.  
     * 
     * However, if the hierarchy is represented by links carried on the "member" property elements, pointing to Eras described fully elsewhere, then it may be useful for a child (member) era to carry an explicit pointer back to its parent (group) Era.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Group</em>' containment reference.
     * @see #setGroup(ReferenceType)
     * @see net.opengis.gml311.Gml311Package#getTimeOrdinalEraType_Group()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='group' namespace='##targetNamespace'"
     * @generated
     */
    ReferenceType getGroup();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeOrdinalEraType#getGroup <em>Group</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Group</em>' containment reference.
     * @see #getGroup()
     * @generated
     */
    void setGroup(ReferenceType value);

} // TimeOrdinalEraType
