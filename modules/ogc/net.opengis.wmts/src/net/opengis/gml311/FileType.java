/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>File Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.FileType#getRangeParameters <em>Range Parameters</em>}</li>
 *   <li>{@link net.opengis.gml311.FileType#getFileName <em>File Name</em>}</li>
 *   <li>{@link net.opengis.gml311.FileType#getFileStructure <em>File Structure</em>}</li>
 *   <li>{@link net.opengis.gml311.FileType#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.gml311.FileType#getCompression <em>Compression</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getFileType()
 * @model extendedMetaData="name='FileType' kind='elementOnly'"
 * @generated
 */
public interface FileType extends EObject {
    /**
     * Returns the value of the '<em><b>Range Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range Parameters</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Range Parameters</em>' containment reference.
     * @see #setRangeParameters(RangeParametersType)
     * @see net.opengis.gml311.Gml311Package#getFileType_RangeParameters()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='rangeParameters' namespace='##targetNamespace'"
     * @generated
     */
    RangeParametersType getRangeParameters();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FileType#getRangeParameters <em>Range Parameters</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Parameters</em>' containment reference.
     * @see #getRangeParameters()
     * @generated
     */
    void setRangeParameters(RangeParametersType value);

    /**
     * Returns the value of the '<em><b>File Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>File Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>File Name</em>' attribute.
     * @see #setFileName(String)
     * @see net.opengis.gml311.Gml311Package#getFileType_FileName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='element' name='fileName' namespace='##targetNamespace'"
     * @generated
     */
    String getFileName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FileType#getFileName <em>File Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File Name</em>' attribute.
     * @see #getFileName()
     * @generated
     */
    void setFileName(String value);

    /**
     * Returns the value of the '<em><b>File Structure</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.gml311.FileValueModelType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>File Structure</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>File Structure</em>' attribute.
     * @see net.opengis.gml311.FileValueModelType
     * @see #isSetFileStructure()
     * @see #unsetFileStructure()
     * @see #setFileStructure(FileValueModelType)
     * @see net.opengis.gml311.Gml311Package#getFileType_FileStructure()
     * @model unsettable="true" required="true"
     *        extendedMetaData="kind='element' name='fileStructure' namespace='##targetNamespace'"
     * @generated
     */
    FileValueModelType getFileStructure();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FileType#getFileStructure <em>File Structure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File Structure</em>' attribute.
     * @see net.opengis.gml311.FileValueModelType
     * @see #isSetFileStructure()
     * @see #unsetFileStructure()
     * @see #getFileStructure()
     * @generated
     */
    void setFileStructure(FileValueModelType value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.FileType#getFileStructure <em>File Structure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetFileStructure()
     * @see #getFileStructure()
     * @see #setFileStructure(FileValueModelType)
     * @generated
     */
    void unsetFileStructure();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.FileType#getFileStructure <em>File Structure</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>File Structure</em>' attribute is set.
     * @see #unsetFileStructure()
     * @see #getFileStructure()
     * @see #setFileStructure(FileValueModelType)
     * @generated
     */
    boolean isSetFileStructure();

    /**
     * Returns the value of the '<em><b>Mime Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mime Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mime Type</em>' attribute.
     * @see #setMimeType(String)
     * @see net.opengis.gml311.Gml311Package#getFileType_MimeType()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='mimeType' namespace='##targetNamespace'"
     * @generated
     */
    String getMimeType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FileType#getMimeType <em>Mime Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Mime Type</em>' attribute.
     * @see #getMimeType()
     * @generated
     */
    void setMimeType(String value);

    /**
     * Returns the value of the '<em><b>Compression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Compression</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Compression</em>' attribute.
     * @see #setCompression(String)
     * @see net.opengis.gml311.Gml311Package#getFileType_Compression()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='compression' namespace='##targetNamespace'"
     * @generated
     */
    String getCompression();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FileType#getCompression <em>Compression</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Compression</em>' attribute.
     * @see #getCompression()
     * @generated
     */
    void setCompression(String value);

} // FileType
