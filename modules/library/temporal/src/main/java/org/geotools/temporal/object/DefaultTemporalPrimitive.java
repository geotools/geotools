/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.temporal.object;

import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.RelativePosition;
import org.opengis.temporal.TemporalOrder;
import org.opengis.temporal.TemporalPrimitive;

/**
 * An abstract class that represents a non-decomposed element of geometry or topology of time.
 * 
 * @author Mehdi Sidhoum (Geomatys)
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 * @source $URL$
 */
public abstract class DefaultTemporalPrimitive extends DefaultTemporalObject implements TemporalPrimitive, TemporalOrder, Comparable<TemporalPrimitive> {

    public int compareTo(TemporalPrimitive that) {
		if (that==null)
			throw new IllegalArgumentException("Provided temporal object is null");
		final RelativePosition pos= this.relativePosition(that);
		if(pos==null)
			throw new ClassCastException("The provided object cannot be compared to this one");
		if(pos==RelativePosition.BEFORE)
			return -1;
		if(pos==RelativePosition.AFTER)
			return +1;
		
		if(pos==RelativePosition.EQUALS)
			return 0;
		
		// TODO rethink this since it looks like it is a pretty dirty hack
        if (this instanceof Period && that instanceof Instant||
        		this instanceof Instant && that instanceof Period) {
            if(pos==RelativePosition.ENDED_BY||
            		pos==RelativePosition.BEGUN_BY||
            		pos==RelativePosition.CONTAINS)
            	return 0;
        }		
        
        // TODO rethink this since it looks like it is a pretty dirty hack
        if (this instanceof Period && that instanceof Period) {
            if(pos==RelativePosition.MEETS)
            	return -1;
            if(pos==RelativePosition.BEGINS)
            	return -1;
            if(pos==RelativePosition.BEGUN_BY)
            	return +1;            
            if(pos==RelativePosition.ENDS)
            	return +1;
            if(pos==RelativePosition.ENDED_BY)
            	return -1;            
            if(pos==RelativePosition.OVERLAPS)
            	return -1;
            if(pos==RelativePosition.OVERLAPPED_BY)
            	return +1;   
            if(pos==RelativePosition.DURING||
            		pos==RelativePosition.CONTAINS||
            		pos==RelativePosition.EQUALS)
            	return 0;              
        }

		throw new IllegalStateException("Unable to compare the provided object with this one");
	}


	/**
     * Returns a value for relative position which are provided by the enumerated data type TM_RelativePosition 
     * and are based on the 13 temporal relationships identified by Allen (1983).
     * @param other TemporalPrimitive
     * @return
     */
    public RelativePosition relativePosition(TemporalPrimitive other) {
        if (this instanceof Instant && other instanceof Instant) {
            Instant timeobject = (Instant) this;
            Instant instantOther = (Instant) other;

            if (timeobject.getPosition().getDate().before(instantOther.getPosition().getDate())) {
                return RelativePosition.BEFORE;
            } else {
                return (timeobject.getPosition().getDate().compareTo(instantOther.getPosition().getDate()) == 0) ? RelativePosition.EQUALS : RelativePosition.AFTER;
            }

        } else {
            if (this instanceof Period && other instanceof Instant) {
                Period timeobject = (Period) this;
                Instant instantarg = (Instant) other;

                if (timeobject.getEnding().getPosition().getDate().before(instantarg.getPosition().getDate())) {
                    return RelativePosition.BEFORE;
                } else {
                    if (timeobject.getEnding().getPosition().getDate().compareTo(instantarg.getPosition().getDate()) == 0) {
                        return RelativePosition.ENDED_BY;
                    } else {
                        if (timeobject.getBeginning().getPosition().getDate().before(instantarg.getPosition().getDate()) &&
                                timeobject.getEnding().getPosition().getDate().after(instantarg.getPosition().getDate())) {
                            return RelativePosition.CONTAINS;
                        } else {
                            return (timeobject.getBeginning().getPosition().getDate().compareTo(instantarg.getPosition().getDate()) == 0) ? RelativePosition.BEGUN_BY : RelativePosition.AFTER;
                        }
                    }
                }
            } else {
                if (this instanceof Instant && other instanceof Period) {
                    Instant timeobject = (Instant) this;
                    Period instantarg = (Period) other;

                    if (instantarg.getEnding().getPosition().getDate().before(timeobject.getPosition().getDate())) {
                        return RelativePosition.AFTER;
                    } else {
                        if (instantarg.getEnding().getPosition().getDate().compareTo(timeobject.getPosition().getDate()) == 0) {
                            return RelativePosition.ENDS;
                        } else {
                            if (instantarg.getBeginning().getPosition().getDate().before(timeobject.getPosition().getDate()) &&
                                    instantarg.getEnding().getPosition().getDate().after(timeobject.getPosition().getDate())) {
                                return RelativePosition.DURING;
                            } else {
                                return (instantarg.getBeginning().getPosition().getDate().compareTo(timeobject.getPosition().getDate()) == 0) ? RelativePosition.BEGINS : RelativePosition.BEFORE;
                            }
                        }
                    }
                } else {
                    if (this instanceof Period && other instanceof Period) {
                        Period timeobject = (Period) this;
                        Period instantarg = (Period) other;

                        if (timeobject.getEnding().getPosition().getDate().before(instantarg.getBeginning().getPosition().getDate())) {
                            return RelativePosition.BEFORE;
                        } else {
                            if (timeobject.getEnding().getPosition().getDate().compareTo(instantarg.getBeginning().getPosition().getDate()) == 0) {
                                return RelativePosition.MEETS;
                            } else {
                                if (timeobject.getBeginning().getPosition().getDate().before(instantarg.getBeginning().getPosition().getDate()) &&
                                        timeobject.getEnding().getPosition().getDate().after(instantarg.getBeginning().getPosition().getDate()) &&
                                        timeobject.getEnding().getPosition().getDate().before(instantarg.getEnding().getPosition().getDate())) {
                                    return RelativePosition.OVERLAPS;
                                } else {
                                    if (timeobject.getBeginning().getPosition().getDate().compareTo(instantarg.getBeginning().getPosition().getDate()) == 0 &&
                                            timeobject.getEnding().getPosition().getDate().before(instantarg.getEnding().getPosition().getDate())) {
                                        return RelativePosition.BEGINS;
                                    } else {
                                        if (timeobject.getBeginning().getPosition().getDate().compareTo(instantarg.getBeginning().getPosition().getDate()) == 0 &&
                                                timeobject.getEnding().getPosition().getDate().after(instantarg.getEnding().getPosition().getDate())) {
                                            return RelativePosition.BEGUN_BY;
                                        } else {
                                            if (timeobject.getBeginning().getPosition().getDate().after(instantarg.getBeginning().getPosition().getDate()) &&
                                                    timeobject.getEnding().getPosition().getDate().before(instantarg.getEnding().getPosition().getDate())) {
                                                return RelativePosition.DURING;
                                            } else {
                                                if (timeobject.getBeginning().getPosition().getDate().before(instantarg.getBeginning().getPosition().getDate()) &&
                                                        timeobject.getEnding().getPosition().getDate().after(instantarg.getEnding().getPosition().getDate())) {
                                                    return RelativePosition.CONTAINS;
                                                } else {
                                                    if (timeobject.getBeginning().getPosition().getDate().compareTo(instantarg.getBeginning().getPosition().getDate()) == 0 &&
                                                            timeobject.getEnding().getPosition().getDate().compareTo(instantarg.getEnding().getPosition().getDate()) == 0) {
                                                        return RelativePosition.EQUALS;
                                                    } else {
                                                        if (timeobject.getBeginning().getPosition().getDate().after(instantarg.getBeginning().getPosition().getDate()) &&
                                                                timeobject.getBeginning().getPosition().getDate().before(instantarg.getEnding().getPosition().getDate()) &&
                                                                timeobject.getEnding().getPosition().getDate().after(instantarg.getEnding().getPosition().getDate())) {
                                                            return RelativePosition.OVERLAPPED_BY;
                                                        } else {
                                                            if (timeobject.getBeginning().getPosition().getDate().after(instantarg.getBeginning().getPosition().getDate()) &&
                                                                    timeobject.getEnding().getPosition().getDate().compareTo(instantarg.getEnding().getPosition().getDate()) == 0) {
                                                                return RelativePosition.ENDS;
                                                            } else {
                                                                if (timeobject.getBeginning().getPosition().getDate().before(instantarg.getBeginning().getPosition().getDate()) &&
                                                                        timeobject.getEnding().getPosition().getDate().compareTo(instantarg.getEnding().getPosition().getDate()) == 0) {
                                                                    return RelativePosition.ENDED_BY;
                                                                } else {
                                                                    return (timeobject.getBeginning().getPosition().getDate().compareTo(instantarg.getEnding().getPosition().getDate()) == 0) ? RelativePosition.MET_BY : RelativePosition.AFTER;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        return null;
                    }
                }
            }
        }
    }
}
