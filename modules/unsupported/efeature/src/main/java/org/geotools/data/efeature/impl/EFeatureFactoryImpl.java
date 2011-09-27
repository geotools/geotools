/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.geotools.data.efeature.DataBuilder;
import org.geotools.data.efeature.DataTypes;
import org.geotools.data.efeature.EFeatureFactory;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Attribute;
import org.opengis.feature.Property;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * @generated
 *
 * @source $URL$
 */
public class EFeatureFactoryImpl extends EFactoryImpl implements EFeatureFactory {
    
    /**
     * Cached {@link Logger} for this class
     */
    protected static final Logger LOGGER = Logging.getLogger(EFeatureFactoryImpl.class);
    
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public static EFeatureFactory initGen() {
        try {
            EFeatureFactory theEFeatureFactory = (EFeatureFactory)EPackage.Registry.INSTANCE.getEFactory("http://geotools.org/data/efeature/efeature.ecore/1.0"); 
            if (theEFeatureFactory != null) {
                return theEFeatureFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new EFeatureFactoryImpl();
    }

    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated NOT
     */
    public static EFeatureFactory init() {
        try {
            EFeatureFactory theEFeatureFactory = (EFeatureFactory)EPackage.Registry.INSTANCE.getEFactory("http://geotools.org/data/efeature/efeature.ecore/1.0"); 
            if (theEFeatureFactory != null) {
                return theEFeatureFactory;
            }
        }
        catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Failed to initialize EFeatureFactory", exception);
        }
        return new EFeatureFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EFeatureFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case EFeaturePackage.PROPERTY:
                return createPropertyFromString(eDataType, initialValue);
            case EFeaturePackage.ATTRIBUTE:
                return createAttributeFromString(eDataType, initialValue);
            case EFeaturePackage.GEOMETRY:
                return createGeometryFromString(eDataType, initialValue);
            case EFeaturePackage.ESTRUCTURAL_FEATURE:
                return createEStructuralFeatureFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case EFeaturePackage.PROPERTY:
                return convertPropertyToString(eDataType, instanceValue);
            case EFeaturePackage.ATTRIBUTE:
                return convertAttributeToString(eDataType, instanceValue);
            case EFeaturePackage.GEOMETRY:
                return convertGeometryToString(eDataType, instanceValue);
            case EFeaturePackage.ESTRUCTURAL_FEATURE:
                return convertEStructuralFeatureToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Property createPropertyFromString(EDataType eDataType, String initialValue) {
        return (Property)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String convertPropertyToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Attribute createAttributeFromString(EDataType eDataType, String initialValue) {
        return (Attribute)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String convertAttributeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Geometry createGeometryFromStringGen(EDataType eDataType, String initialValue) {
        return (Geometry)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated NOT
     */
    public Geometry createGeometryFromString(EDataType eDataType, String initialValue) {
        //
        // Get data type
        //
        Class<?> type = eDataType.getInstanceClass();
        //
        // Only serialize Geometry instances
        //
        if(DataTypes.isGeometry(type)) {
            try {                
                //
                // Convert to Geometry
                //
                return DataBuilder.toGeometry(initialValue);
                
            } catch (ParseException e) {
                //
                // Notify
                //
                LOGGER.log(Level.WARNING, e.getMessage(), e);
                //
                // Try to create an empty geometry of given type
                //
                try {
                    return DataBuilder.toEmptyGeometry(type);
                } catch (ParseException e1) {
                    //
                    // Notify again
                    //
                    LOGGER.log(Level.SEVERE, e1.getMessage(), e1);
                    //
                    // This time, nothing can be done, just return null.
                    //
                    return null;
                }
            }            
        }
        //
        // Forward to default implementation
        //        
        return createGeometryFromStringGen(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String convertGeometryToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EStructuralFeature createEStructuralFeatureFromString(EDataType eDataType,
            String initialValue) {
        return (EStructuralFeature)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String convertEStructuralFeatureToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EFeaturePackage getEFeaturePackage() {
        return (EFeaturePackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static EFeaturePackage getPackage() {
        return EFeaturePackage.eINSTANCE;
    }

} // EFeatureFactoryImpl
