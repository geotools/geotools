package org.geotools.ysld.validate;

public abstract class StatefulValidator extends YsldValidateHandler {
    
    public StatefulValidator() {
        super();
    }
    
    abstract void reset();
    
}