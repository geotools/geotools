/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import net.opengis.wfs.BaseRequestType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Diff Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *         A GetDiff element contains one or more DifferenceQuery elements that describe a difference
 *         query operation on one feature type. In response to a GetDiff request, a Versioning Web
 *         Feature Service must be able to generate a Transaction command that can be used to alter
 *         features at fromFeatureVersion and alter them into features at toFeatureVersion
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfsv.GetDiffType#getDifferenceQuery <em>Difference Query</em>}</li>
 *   <li>{@link net.opengis.wfsv.GetDiffType#getOutputFormat <em>Output Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfsv.WfsvPackage#getGetDiffType()
 * @model extendedMetaData="name='GetDiffType' kind='elementOnly'"
 * @generated
 */
public interface GetDiffType extends BaseRequestType {
    /**
     * Returns the value of the '<em><b>Difference Query</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfsv.DifferenceQueryType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *         The DifferenceFilter element is used to gather differences in features matched by a standard
     *         OGC filter at starting and ending featureVersion, and a filter used to match
     *       
     * <!-- end-model-doc -->
     * @return the value of the '<em>Difference Query</em>' containment reference list.
     * @see net.opengis.wfsv.WfsvPackage#getGetDiffType_DifferenceQuery()
     * @model type="net.opengis.wfsv.DifferenceQueryType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='DifferenceQuery' namespace='##targetNamespace'"
     * @generated
     */
    EList getDifferenceQuery();

    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute.
     * The default value is <code>"text/xml; subtype=wfs-transaction/1.1.0"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *               The outputFormat attribute is used to specify the output format that the Versioning
     *               Web Feature Service should generate in response to a GetDiff element. The default
     *               value of 'application/xml; subtype=wfsv-transaction/1.1.0' indicates that the output
     *               is an XML document that conforms to the WFS 1.1.0 Transaction definition. For the
     *               purposes of experimentation, vendor extension, or even extensions that serve a
     *               specific community of interest, other acceptable output format values may be used to
     *               specify other formats as long as those values are advertised in the capabilities
     *               document.
     *             
     * <!-- end-model-doc -->
     * @return the value of the '<em>Output Format</em>' attribute.
     * @see #isSetOutputFormat()
     * @see #unsetOutputFormat()
     * @see #setOutputFormat(String)
     * @see net.opengis.wfsv.WfsvPackage#getGetDiffType_OutputFormat()
     * @model default="text/xml; subtype=wfs-transaction/1.1.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='outputFormat'"
     * @generated
     */
    String getOutputFormat();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.GetDiffType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Format</em>' attribute.
     * @see #isSetOutputFormat()
     * @see #unsetOutputFormat()
     * @see #getOutputFormat()
     * @generated
     */
    void setOutputFormat(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfsv.GetDiffType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
    void unsetOutputFormat();

    /**
     * Returns whether the value of the '{@link net.opengis.wfsv.GetDiffType#getOutputFormat <em>Output Format</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Output Format</em>' attribute is set.
     * @see #unsetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
    boolean isSetOutputFormat();

} // GetDiffType
