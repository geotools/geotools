package org.geotools.data.efeature;

/**
 * Interface for listening to {@link EStructureInfo} changes.
 * 
 * @author kengu, 24. apr. 2011
 * 
 *
 * @source $URL$
 */
public interface EFeatureListener<T> {
    public boolean onChange(T source, int property, Object oldValue, Object newValue);
}
