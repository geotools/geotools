package org.geotools.coverage.io.impl.range;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public interface Salinity extends Quantity{
    
    public final static Unit<Salinity> PSU = Unit.ONE.alternate("PSU");

}
