/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.FileType;
import net.opengis.gml311.FileValueModelType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.RangeParametersType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>File Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.FileTypeImpl#getRangeParameters <em>Range Parameters</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FileTypeImpl#getFileName <em>File Name</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FileTypeImpl#getFileStructure <em>File Structure</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FileTypeImpl#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FileTypeImpl#getCompression <em>Compression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FileTypeImpl extends MinimalEObjectImpl.Container implements FileType {
    /**
     * The cached value of the '{@link #getRangeParameters() <em>Range Parameters</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeParameters()
     * @generated
     * @ordered
     */
    protected RangeParametersType rangeParameters;

    /**
     * The default value of the '{@link #getFileName() <em>File Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileName()
     * @generated
     * @ordered
     */
    protected static final String FILE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFileName() <em>File Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileName()
     * @generated
     * @ordered
     */
    protected String fileName = FILE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getFileStructure() <em>File Structure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileStructure()
     * @generated
     * @ordered
     */
    protected static final FileValueModelType FILE_STRUCTURE_EDEFAULT = FileValueModelType.RECORD_INTERLEAVED;

    /**
     * The cached value of the '{@link #getFileStructure() <em>File Structure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileStructure()
     * @generated
     * @ordered
     */
    protected FileValueModelType fileStructure = FILE_STRUCTURE_EDEFAULT;

    /**
     * This is true if the File Structure attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean fileStructureESet;

    /**
     * The default value of the '{@link #getMimeType() <em>Mime Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMimeType()
     * @generated
     * @ordered
     */
    protected static final String MIME_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMimeType() <em>Mime Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMimeType()
     * @generated
     * @ordered
     */
    protected String mimeType = MIME_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getCompression() <em>Compression</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCompression()
     * @generated
     * @ordered
     */
    protected static final String COMPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCompression() <em>Compression</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCompression()
     * @generated
     * @ordered
     */
    protected String compression = COMPRESSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FileTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getFileType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeParametersType getRangeParameters() {
        return rangeParameters;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRangeParameters(RangeParametersType newRangeParameters, NotificationChain msgs) {
        RangeParametersType oldRangeParameters = rangeParameters;
        rangeParameters = newRangeParameters;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.FILE_TYPE__RANGE_PARAMETERS, oldRangeParameters, newRangeParameters);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRangeParameters(RangeParametersType newRangeParameters) {
        if (newRangeParameters != rangeParameters) {
            NotificationChain msgs = null;
            if (rangeParameters != null)
                msgs = ((InternalEObject)rangeParameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.FILE_TYPE__RANGE_PARAMETERS, null, msgs);
            if (newRangeParameters != null)
                msgs = ((InternalEObject)newRangeParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.FILE_TYPE__RANGE_PARAMETERS, null, msgs);
            msgs = basicSetRangeParameters(newRangeParameters, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FILE_TYPE__RANGE_PARAMETERS, newRangeParameters, newRangeParameters));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFileName(String newFileName) {
        String oldFileName = fileName;
        fileName = newFileName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FILE_TYPE__FILE_NAME, oldFileName, fileName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FileValueModelType getFileStructure() {
        return fileStructure;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFileStructure(FileValueModelType newFileStructure) {
        FileValueModelType oldFileStructure = fileStructure;
        fileStructure = newFileStructure == null ? FILE_STRUCTURE_EDEFAULT : newFileStructure;
        boolean oldFileStructureESet = fileStructureESet;
        fileStructureESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FILE_TYPE__FILE_STRUCTURE, oldFileStructure, fileStructure, !oldFileStructureESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetFileStructure() {
        FileValueModelType oldFileStructure = fileStructure;
        boolean oldFileStructureESet = fileStructureESet;
        fileStructure = FILE_STRUCTURE_EDEFAULT;
        fileStructureESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.FILE_TYPE__FILE_STRUCTURE, oldFileStructure, FILE_STRUCTURE_EDEFAULT, oldFileStructureESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetFileStructure() {
        return fileStructureESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMimeType(String newMimeType) {
        String oldMimeType = mimeType;
        mimeType = newMimeType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FILE_TYPE__MIME_TYPE, oldMimeType, mimeType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCompression() {
        return compression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCompression(String newCompression) {
        String oldCompression = compression;
        compression = newCompression;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FILE_TYPE__COMPRESSION, oldCompression, compression));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.FILE_TYPE__RANGE_PARAMETERS:
                return basicSetRangeParameters(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.FILE_TYPE__RANGE_PARAMETERS:
                return getRangeParameters();
            case Gml311Package.FILE_TYPE__FILE_NAME:
                return getFileName();
            case Gml311Package.FILE_TYPE__FILE_STRUCTURE:
                return getFileStructure();
            case Gml311Package.FILE_TYPE__MIME_TYPE:
                return getMimeType();
            case Gml311Package.FILE_TYPE__COMPRESSION:
                return getCompression();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Gml311Package.FILE_TYPE__RANGE_PARAMETERS:
                setRangeParameters((RangeParametersType)newValue);
                return;
            case Gml311Package.FILE_TYPE__FILE_NAME:
                setFileName((String)newValue);
                return;
            case Gml311Package.FILE_TYPE__FILE_STRUCTURE:
                setFileStructure((FileValueModelType)newValue);
                return;
            case Gml311Package.FILE_TYPE__MIME_TYPE:
                setMimeType((String)newValue);
                return;
            case Gml311Package.FILE_TYPE__COMPRESSION:
                setCompression((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Gml311Package.FILE_TYPE__RANGE_PARAMETERS:
                setRangeParameters((RangeParametersType)null);
                return;
            case Gml311Package.FILE_TYPE__FILE_NAME:
                setFileName(FILE_NAME_EDEFAULT);
                return;
            case Gml311Package.FILE_TYPE__FILE_STRUCTURE:
                unsetFileStructure();
                return;
            case Gml311Package.FILE_TYPE__MIME_TYPE:
                setMimeType(MIME_TYPE_EDEFAULT);
                return;
            case Gml311Package.FILE_TYPE__COMPRESSION:
                setCompression(COMPRESSION_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Gml311Package.FILE_TYPE__RANGE_PARAMETERS:
                return rangeParameters != null;
            case Gml311Package.FILE_TYPE__FILE_NAME:
                return FILE_NAME_EDEFAULT == null ? fileName != null : !FILE_NAME_EDEFAULT.equals(fileName);
            case Gml311Package.FILE_TYPE__FILE_STRUCTURE:
                return isSetFileStructure();
            case Gml311Package.FILE_TYPE__MIME_TYPE:
                return MIME_TYPE_EDEFAULT == null ? mimeType != null : !MIME_TYPE_EDEFAULT.equals(mimeType);
            case Gml311Package.FILE_TYPE__COMPRESSION:
                return COMPRESSION_EDEFAULT == null ? compression != null : !COMPRESSION_EDEFAULT.equals(compression);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (fileName: ");
        result.append(fileName);
        result.append(", fileStructure: ");
        if (fileStructureESet) result.append(fileStructure); else result.append("<unset>");
        result.append(", mimeType: ");
        result.append(mimeType);
        result.append(", compression: ");
        result.append(compression);
        result.append(')');
        return result.toString();
    }

} //FileTypeImpl
