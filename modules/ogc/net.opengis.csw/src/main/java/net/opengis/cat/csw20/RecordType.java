/**
 */
package net.opengis.cat.csw20;

import java.lang.String;
import net.opengis.ows10.BoundingBoxType;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Record Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             This type extends DCMIRecordType to add ows:BoundingBox;
 *             it may be used to specify a spatial envelope for the
 *             catalogued resource.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.RecordType#getAnyText <em>Any Text</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.RecordType#getBoundingBox <em>Bounding Box</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getRecordType()
 * @model extendedMetaData="name='RecordType' kind='elementOnly'"
 * @generated
 */
public interface RecordType extends DCMIRecordType {
    /**
     * Returns the value of the '<em><b>Any Text</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw._2._0.EmptyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any Text</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any Text</em>' containment reference list.
     * @see net.opengis.cat.csw._2._0._0Package#getRecordType_AnyText()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AnyText' namespace='##targetNamespace'"
     */
    EList<String> getAnyText();

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows10.BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounding Box</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getRecordType_BoundingBox()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='http://www.opengis.net/ows' group='http://www.opengis.net/ows#BoundingBox:group'"
     * @generated
     */
    EList<BoundingBoxType> getBoundingBox();

} // RecordType
