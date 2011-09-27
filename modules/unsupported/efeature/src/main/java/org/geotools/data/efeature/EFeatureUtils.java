package org.geotools.data.efeature;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.eclipse.emf.query.statements.WHERE;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.internal.EFeatureContextHelper;
import org.geotools.data.efeature.internal.EFeatureInternal;
import org.geotools.data.efeature.query.EFeatureEncoderException;
import org.geotools.data.efeature.query.EFeatureFilter;
import org.geotools.data.efeature.query.EFeatureQuery;
import org.geotools.data.efeature.query.EObjectConditionEncoder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.Identifier;

import com.vividsolutions.jts.geom.Geometry;

/**
 * {@link EFeature} utility class.
 * <p>
 * 
 * @author kengu
 * 
 *
 * @source $URL$
 */
public class EFeatureUtils {

    /**
     * Check if at least one structure is available for instance construction.
     * <p>
     * 
     * @return <code>true</code> if at least one structure is available for 
     * instance construction.
     */
    public static <T extends EStructureInfo<?>> boolean isAvailable(Map<String, T> eMap) {
        for (EStructureInfo<?> it : eMap.values()) {
            if (it.isAvailable())
                return true;
        }
        return false;
    }

    /**
     * Create a {@link EFeature} status instance
     * 
     * @param source - source which status apply
     * @param type - status type
     * @param message - (optional) message
     * @param cause - (optional) {@link Throwable thowable} or 
     * {@link Throwable#getStackTrace() stack trace} cause
     * @return a new {@link EFeatureStatus} instance
     * @throws IllegalArgumentException If cause is not <code>null</code>,
     * and not a {@link Throwable} or {@link StackTraceElement} array. 
     */
    public static EFeatureStatus newStatus(final Object source, final int type,
            final String message, final Object cause) {
        
        if(!(cause instanceof Throwable || cause instanceof StackTraceElement[] || cause==null) )
        {
            throw new IllegalArgumentException(
                    "Cause can only be Throwable and StackTraceElement[]");
        }
        
        return new EFeatureStatus() {

            @Override
            public int getType() {
                return type;
            }

            @Override
            public Object getSource() {
                return source;
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public Throwable getCause() {
                return (cause instanceof Throwable ? (Throwable)cause : null);
            }
            
            @Override
            public StackTraceElement[] getStackTrace() {
                return (cause instanceof Throwable ? ((Throwable)cause).getStackTrace() : (StackTraceElement[])cause);
            }
            

            @Override
            public boolean isType(int match) {
                return type == match;
            }

            @Override
            public boolean isSuccess() {
                return isType(SUCCESS);
            }

            @Override
            public boolean isWarning() {
                return isType(WARNING);
            }

            @Override
            public boolean isFailure() {
                return isType(FAILURE);
            }

            @Override
            public EFeatureStatus clone(String message) {
                return newStatus(source, type, message, cause);
            }
            
            @Override
            public EFeatureStatus clone(String message, Object cause) {
                return newStatus(source, type, message, cause);
            }

        };
    }
    
    /**
     * Check if given {@link EClassifier classifier} implements 
     * {@link EFeature}, or contains {@link EFeature} compatible data. 
     * @param eClassifier - the classifier to test for compatibility
     * @return <code>true</code> if compatible.
     */
    public static boolean isCompatible(EClassifier eClassifier)
    {
        if(eClassifier instanceof EFeature)
        {
            return true;
        }
        else if(eClassifier instanceof EClass)
        {
            EClass eClass = (EClass)eClassifier;
            for(EAttribute it : eClass.getEAllAttributes())
            {
                //
                // Contains geometry data?
                //
                if (Geometry.class.isAssignableFrom(it.getEAttributeType().getInstanceClass()))
                {
                    return true;
                }
                
            }
        }
        return false;
    }

    /**
     * Get {@link EFeature} folder name from {@link SimpleFeatureType} name
     * <p>
     * {@link SimpleFeatureType} names have the following
     * 
     * <pre>
     * eType=&lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder = {@link EFeature} folder name
     * eFeature = {@link EFeature} class | reference name
     * </pre>
     * 
     * @param eType - given {@link SimpleFeatureType} name
     * @return a {@link EFeature} folder name if {@link #isSimpleFeatureType(String)},
     *         <code>null</code> otherwise.
     */
    public static String toFolderName(String eType) {
        if (!(eType == null || eType.length() == 0)) {
            int i = eType.indexOf(".");
            return (i == -1 ? eType : eType.substring(0, i));
        }
        return null;
    }

    /**
     * Get {@link EFeature} name from {@link SimpleFeatureType} name
     * <p>
     * {@link SimpleFeatureType} names have the following
     * 
     * <pre>
     * eType=&lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder = {@link EFeature} folder name
     * eFeature = {@link EFeature} class | reference name
     * </pre>
     * 
     * @param eType - given {@link SimpleFeatureType} name
     * @return a {@link EFeature} name if {@link #isSimpleFeatureType(String)}, <code>null</code>
     *         otherwise.
     */
    public static String toFeatureName(String eType) {
        if (!(eType == null || eType.length() == 0)) {
            int i = eType.indexOf(".");
            if (i != -1) {
                return eType.substring(i + 1);
            }
        }
        return null;
    }

    /**
     * Check if given {@link EClass} extends given super type
     * 
     * @param eClass - given {@link EClass} instance
     * @param eName - super {@link EClass} name
     * @return <code>true</code> if super {@link EClass} is implemented by given {@link EClass}.
     */
    public static boolean isInstanceOf(EClass eClass, String eName) {
        for (EClass it : eClass.getEAllSuperTypes()) {
            if (it.getName() == eName) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get {@link EPackage} instance from namespace URI.
     * 
     * @param eResource - {@link EPackage} instance
     * @param eNsURI - namespace URI string
     * @return a {@link EPackage} instance if found, <code>null</code> otherwise.
     */
    public static EPackage ePackage(Resource eResource, String eNsURI) {
        EPackage.Registry r = eResource.getResourceSet().getPackageRegistry();
        return r.getEPackage(eNsURI);
    }

    /**
     * Get named {@link EReference} in given class
     * 
     * @param eClass - {@link EClass} instance
     * @return a {@link EReference} instance if found, <code>null</code> otherwise
     */
    public static EReference eGetReference(EClass eClass, String eName) {
        if (eClass != null) {
            for (EReference it : eClass.getEAllReferences()) {
                if (it.getName() == eName) {
                    return it;
                }
            }
        }
        return null;
    }

    /**
     * Get named {@link EAttribute} {@link Map} for given class
     * 
     * @param eClass - {@link EClass} instance
     * @return a named {@link EAttribute} {@link Map}
     */
    public static Map<String, EAttribute> eGetAttributeMap(EClass eClass) {
        Map<String, EAttribute> map = new HashMap<String, EAttribute>();
        if (eClass != null) {
            return eGetAttributeMap(eClass.getEAllAttributes());
        }
        return map;
    }

    /**
     * Get named {@link EAttribute} in given class
     * 
     * @param eClass - {@link EClass} instance
     * @return a {@link EAttribute} instance if found, <code>null</code> otherwise
     */
    public static EAttribute eGetAttribute(EClass eClass, String eName) {
        if (eClass != null) {
            for (EAttribute it : eClass.getEAllAttributes()) {
                if (it.getName() == eName) {
                    return it;
                }
            }
        }
        return null;
    }

    public static Map<String, EAttribute> eGetAttributeMap(EList<EAttribute> list) {
        Map<String, EAttribute> map = new HashMap<String, EAttribute>();
        if (list != null) {
            for (EAttribute it : list) {
                map.put(it.getName(), it);
            }
        }
        return map;
    }

    /**
     * Get an {@link EStructuralFeature} in given object with given name if exists
     * 
     * @param eObject - {@link EObject} instance
     * @param eName - feature name
     * @return an {@link EStructuralFeature} instance if found, null otherwise.
     */
    public static EStructuralFeature eGetStructuralFeature(EObject eObject, String eName,
            String eNsURI) {
        EPackage.Registry eRegistry = eObject.eResource().getResourceSet().getPackageRegistry();
        return eGetStructuralFeature(eRegistry, eObject.eClass(), eName, eNsURI);
    }

    /**
     * Get an {@link EStructuralFeature} in given object with given name if exists
     * 
     * @param eClass - {@link EClass} instance
     * @param eName - element name
     * @param eNsURI - package name space URI specification
     * @return an {@link EStructuralFeature} instance if found, null otherwise.
     */
    public static EStructuralFeature eGetStructuralFeature(EPackage.Registry eRegistry,
            EClass eClass, String eName, String eNsURI) {
        ExtendedMetaData data = new BasicExtendedMetaData(eRegistry) {

            @Override
            protected boolean isFeatureKindSpecific() {
                return false;
            }

            @Override
            protected boolean isFeatureNamespaceMatchingLax() {
                return true;
            }

        };
        return data.getAttribute(eClass, eNsURI, eName);
    }

    /**
     * Get {@link EObject} {@link URI} in containing {@link Resource} instance
     * <p>
     * 
     * @param eObject - {@link EObject} instance
     * @return a {@link URI} instance
     * @see {@link EcoreUtil.getURI}
     */
    public static URI eGetObjectURI(EObject eObject) {
        return EcoreUtil.getURI(eObject);
    }

    /**
     * Create new {@link EObjectCondition} on {@link EClass} instance.
     * <p>
     * Only {@link EObject}s of given {@link EClass} will satisfy this filter.
     * 
     * @param eClass - given {@link EClass} instance
     * @return a {@link EObjectCondition} instance.
     */
    public static EObjectCondition createEClassFilter(final EClass eClass) {
        return new EObjectCondition() {
            @Override
            public boolean isSatisfied(EObject eObject) {
                boolean bFlag = eClass.isInstance(eObject);
                return bFlag;
            }
        };
    }

    /**
     * Convert given GeoTools {@link Filter} to a {@link EFeatureQuery} statement
     * capable of querying {@link EObject} instances from all {@link EClass}es that
     * extends the {@link EFeaturePackage#getEFeature() EFeature class}. 
     * 
     * @param featureType - {@link SimpleFeatureType} instance
     * @param eObjects - {@link TreeIterator} to by queried
     * @param filter - GeoTools {@link Filter filter} instance
     * @return a {@link EFeatureQuery} statement
     * @throws EFeatureEncoderException If filter encoding failed.
     */
    public static EFeatureQuery toEFeatureQuery(
            TreeIterator<EObject> eObjects, Filter filter) 
        throws EFeatureEncoderException {
        //
        // Get prototype structure from internal context
        //
        EFeatureInfo eInfo = EFeatureContextHelper.ePrototype(EFeaturePackage.eINSTANCE.getEFeature());
        //
        // Forward
        //
        return toEFeatureQuery(eInfo,eObjects, filter);
    }
    
    /**
     * Convert GeoTools {@link Filter} to a {@link EFeatureQuery} statement
     * capable of querying {@link EObject} instances from the {@link EClass}
     * defined by given {@link EFeatureInfo structure}.
     * 
     * @param eFeatureInfo - filter on {@link EFeatureInfo} instance
     * @param featureType - {@link SimpleFeatureType} instance
     * @param eObjects - {@link TreeIterator} to by queried
     * @param filter - GeoTools {@link Filter filter} instance
     * @return a {@link EFeatureQuery} statement
     * @throws EFeatureEncoderException If filter encoding failed.
     */
    public static EFeatureQuery toEFeatureQuery(EFeatureInfo eFeatureInfo,
            TreeIterator<EObject> eObjects, Filter filter) throws EFeatureEncoderException {
        //
        // Do sanity checks
        //
        isSane("eFeatureInfo",eFeatureInfo);
        isSane("eObjects",eObjects);
        isSane("filter",filter);
        //
        // Create an EClass filter
        //
        EObjectCondition eClassFilter = createEClassFilter(eFeatureInfo.eClass());
        //
        // Create spatial filter
        //
        EObjectCondition eSpatialFilter = toEObjectCondition(eFeatureInfo, true, filter);
        //
        // Create EMF Query WHERE clause
        //
        WHERE where = new WHERE(eClassFilter.AND(eSpatialFilter));
        //
        // Create EFeature filter
        //
        EFeatureFilter eWhere = new EFeatureFilter(where);
        //
        // Create query instance
        //
        return new EFeatureQuery(eObjects, eWhere);
    }

    public static EObjectCondition toEObjectCondition(
            EFeatureInfo eFeatureInfo,
            boolean looseBBox, Filter filter)
            throws EFeatureEncoderException {
        //
        // Create encoder instance
        //
        EObjectConditionEncoder encoder = new EObjectConditionEncoder(eFeatureInfo, looseBBox);
        //
        // Encode filter
        //
        return encoder.encode(filter);
    }
    
    public static String eGetNsURI(EObject eObject, boolean container) {
        if(eObject instanceof EClass) {
            return eGetNsURI((EClass)eObject);
        }
        else if(eObject instanceof EReference) {
            return eGetNsURI((EReference)eObject,container);
        }
        return eGetNsURI(eObject.eClass());
    }
    
    public static String eGetNsURI(EClass eClass) {
        return eClass.getEPackage().getNsURI();        
    }
    
    public static String eGetNsURI(EReference eReference, boolean container) {
        EClass eClass = (container ? eReference.eContainer().eClass() : eReference.eClass());
        return eClass.getEPackage().getNsURI();        
    }
    

    public static EClass eGetContainingClass(EObject eChild) {
        EReference eReference = (eChild != null ? eChild.eContainmentFeature() : null);
        return (eReference != null ? eReference.getEContainingClass() : null);
    }

    public static StackTraceElement getStackTraceElement(int index, int remove)
    {
        StackTraceElement[] stack = getStackTrace(remove+1);
        return stack[index];
    }
    
    public static StackTraceElement[] getStackTrace(int remove)
    {
        Throwable t = new Throwable();
        t.fillInStackTrace();
        StackTraceElement[] stack = t.getStackTrace();
        StackTraceElement[] current = new StackTraceElement[stack.length-remove-1];
        System.arraycopy(stack, remove+1, current, 0, stack.length-remove-1);
        return current;
    }
    
    public static String getEnclosingMethodName()
    {
        Throwable t = new Throwable();
        t.fillInStackTrace();
        StackTraceElement[] stack = t.getStackTrace();
        return stack[stack.length-2].getMethodName();
    }
    
    public static void isSane(String name, Object object) {
        if(object==null) {
            throw new NullPointerException(name + " can not be null");
        }
    }
    
    public static <T> T[] concat(Class<T> type, T[]... arrays) {
        int count = 0;
        for(T[] it : arrays) count += it.length;
        @SuppressWarnings("unchecked")
        T[] m = (T[])Array.newInstance(type, count);
        if(count>0) {
            count = 0;
            for(T[] it : arrays) {
                System.arraycopy(it, 0, m, count, it.length);
                count += it.length;
            }
        }
        return m;
     }
    
    public static Set<Identifier> toEIDs(Object...eIDs){
        Set<Identifier> eIDSet = new HashSet<Identifier>(eIDs.length);
        for(Object eID : eIDs) {
            eIDSet.add(new FeatureIdImpl(eID.toString()));
        }
        return eIDSet;
    }        
    
    /**
     * Get {@link ESimpleFeature} values in immutable order.
     * </p> 
     * @param eStructure
     * @param eObject
     * @param transaction 
     * @return a ordered list of a{@link ESimpleFeature} values
     */
    public static List<Object> eGetFeatureValues(
            EFeatureInfo eStructure, EObject eObject, Transaction transaction) {
        //
        // Get internal EFeature implementation
        //
        EFeatureInternal eInternal = EFeatureInternal.eInternal(eStructure, eObject);
        //
        // Enter modification mode
        //
        eInternal.enter(transaction);
        //
        // Try to get values
        //        
        try {
            List<Object> eList = new ArrayList<Object>();
            for(EAttribute it : eStructure.eGetAttributeList(true)) {
                eList.add(eObject.eGet(it));
            }
            return Collections.unmodifiableList(eList);
        } finally {
            //
            // Leave modification mode
            //
            eInternal.leave();
        }    
    }
    
    /**
     * Set {@link ESimpleFeature} values in immutable order.
     * </p>
     * @param eStructure
     * @param eObject
     * @param eValues
     */
    public static void eSetFeatureValues(
            EFeatureInfo eStructure, EObject eObject, 
            List<Object> eValues, Transaction transaction) {
        //
        // Get internal EFeature implementation
        //
        EFeatureInternal eInternal = EFeatureInternal.eInternal(eStructure, eObject);
        //
        // Enter modification mode
        //
        eInternal.enter(transaction);
        //
        // Try to set values
        //        
        try {
            int i=0;
            for(EAttribute it : eStructure.eGetAttributeList(true)) {
                eObject.eSet(it,eValues.get(i++));            
            }
        } finally {
            //
            // Leave modification mode
            //
            eInternal.leave();
        }
    }    
    
    /**
     * Convert Feature to list of values
     * @param feature
     * @return a list of feature values
     */
    public static List<Object> toFeatureValues(Feature feature) {
        Collection<Property> properties = feature.getProperties();
        List<Object> values = new ArrayList<Object>(properties.size());
        for(Property it : feature.getProperties()) {
            values.add(it.getValue());
        }
        return values;
    }

    public final static <K,V> Map<K,V> newMap(int...sizes) {
        int size = 0;
        for(int it : sizes) size += it;
        return new HashMap<K, V>(size);
    }

    public final static <K,V> Map<K,V> newMap(Map<K,V> fromMap, int...sizes) {
        int size = fromMap.size();
        for(int it : sizes) size += it;
        return newMap(size);
    }
    
    public final static <V> List<V> newList(int...sizes) {
        int size = 0;
        for(int it : sizes) size += it;
        return new ArrayList<V>(size);
    }
    
    public final static <V> List<V> newList(List<V> fromList, int...sizes) {
        int size = fromList.size();
        for(int it : sizes) size += it;
        return newList(size);
    }

}
