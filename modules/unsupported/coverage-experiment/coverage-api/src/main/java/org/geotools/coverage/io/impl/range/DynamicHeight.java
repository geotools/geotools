package org.geotools.coverage.io.impl.range;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public interface DynamicHeight extends Quantity{
        
    public final static Unit<DynamicHeight> DYNAMIC_METER = Unit.ONE.alternate("dynamic meter");

}
