/**
 */
package net.opengis.cat.csw20;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DCMI Record Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             This type encapsulates all of the standard DCMI metadata terms,
 *             including the Dublin Core refinements; these terms may be mapped
 *             to the profile-specific information model.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.DCMIRecordType#getDCElement <em>DC Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getDCMIRecordType()
 * @model extendedMetaData="name='DCMIRecordType' kind='elementOnly'"
 * @generated
 */
public interface DCMIRecordType extends AbstractRecordType {
    

    /**
     * Returns the value of the '<em><b>DC Element</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.SimpleLiteral}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>DC Element</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>DC Element</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getDCMIRecordType_DCElement()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DC-element' namespace='http://purl.org/dc/elements/1.1/' group='http://purl.org/dc/elements/1.1/#DC-element:group'"
     * @generated
     */
    EList<SimpleLiteral> getDCElement();

} // DCMIRecordType
