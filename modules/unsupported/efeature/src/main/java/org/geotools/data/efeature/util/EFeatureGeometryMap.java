package org.geotools.data.efeature.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.efeature.EFeatureGeometry;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.internal.EFeatureGeometryDelegate;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Unmodifiable map of {@link EFeatureGeometry} instances.
 * 
 * @author kengu
 * 
 */
public class EFeatureGeometryMap<V extends Geometry> extends
        AbstractMap<String, EFeatureGeometry<V>> {
    private static final long serialVersionUID = 1L;

    private EFeatureGeometryList<V> eList;

    private final Map<String, EFeatureGeometry<V>> eMap;

    private final Class<V> type;

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    public EFeatureGeometryMap(EFeatureInfo eInfo, EObject eObject, Class<V> type) {
        this(toMap(eInfo, eObject, type), type);
    }

    public EFeatureGeometryMap(EFeatureInfo eInfo, EObject eObject, List<EAttribute> eList,
            Class<V> type) {
        this(toMap(eInfo, eObject, eList, type), type);
    }

    public EFeatureGeometryMap(EFeatureGeometryList<V> eList, Class<V> type) {
        this(toMap(eList, type), type);
    }

    public EFeatureGeometryMap(Map<String, EFeatureGeometry<V>> eMap, Class<V> type) {
        this.type = type;
        this.eMap = Collections.unmodifiableMap(eMap);
    }

    // ----------------------------------------------------- 
    //  Map implementation
    // -----------------------------------------------------

    @Override
    public Set<Entry<String, EFeatureGeometry<V>>> entrySet() {
        return eMap.entrySet();
    }

    public EFeatureGeometryList<V> getList() {
        if (eList == null) {
            List<EFeatureGeometry<V>> list = new ArrayList<EFeatureGeometry<V>>(eMap.values());
            eList = new EFeatureGeometryList<V>(list, type);
        }
        return eList;
    }

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------

    public static Map<String, EFeatureGeometry<Geometry>> toMap(EFeatureInfo eFeatureInfo,
            EObject eObject) {
        EClass eClass = eFeatureInfo.eClass();
        if (eClass.isSuperTypeOf(eObject.eClass())) {
            Map<String, EAttribute> eAttrMap = EFeatureUtils.eGetAttributeMap(eClass);
            return toMap(eFeatureInfo, eObject, eAttrMap.values(), Geometry.class);
        }
        return null;
    }

    public static <V extends Geometry> Map<String, EFeatureGeometry<V>> toMap(
            EFeatureInfo eFeatureInfo, EObject eObject, Class<V> type) {
        EClass eClass = eFeatureInfo.eClass();
        if (eClass.isSuperTypeOf(eObject.eClass())) {
            Map<String, EAttribute> eAttrMap = EFeatureUtils.eGetAttributeMap(eClass);
            return toMap(eFeatureInfo, eObject, eAttrMap.values(), type);
        }
        return null;
    }

    public static <V extends Geometry> Map<String, EFeatureGeometry<V>> toMap(
            EFeatureInfo eFeatureInfo, EObject eObject, Collection<EAttribute> eAttributes,
            Class<V> type) {
        Map<String, EFeatureGeometry<V>> map = new HashMap<String, EFeatureGeometry<V>>(
                eAttributes.size());
        for (EAttribute it : eAttributes) {
            String eName = it.getName();
            EFeatureGeometry<V> d = EFeatureGeometryDelegate.create(eObject, it.getName(), type,
                    eFeatureInfo);
            if (d != null) {
                map.put(eName, d);
            }
        }
        return map;

    }

    public static <V extends Geometry> Map<String, EFeatureGeometry<V>> toMap(
            EFeatureGeometryList<V> eList, Class<V> type) {
        Map<String, EFeatureGeometry<V>> eMap = new HashMap<String, EFeatureGeometry<V>>();
        for (EFeatureGeometry<V> it : eList) {
            eMap.put(it.getStructure().eName(), it);
        }
        return eMap;
    }

}
