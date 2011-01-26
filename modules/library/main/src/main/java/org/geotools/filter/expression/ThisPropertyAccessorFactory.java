package org.geotools.filter.expression;

import org.geotools.factory.Hints;
import org.opengis.feature.Attribute;

public class ThisPropertyAccessorFactory implements PropertyAccessorFactory {
    
    static final ThisPropertyAccessor THIS_ACCESSOR = new ThisPropertyAccessor();

    public PropertyAccessor createPropertyAccessor(Class type, String xpath,
            Class target, Hints hints) {
        if(".".equals(xpath))
            return THIS_ACCESSOR;
        else
            return null;
    }
    
    static class ThisPropertyAccessor implements PropertyAccessor {

        public boolean canHandle(Object object, String xpath, Class target) {
            return ".".equals(xpath);
        }

        public Object get(Object object, String xpath, Class target)
                throws IllegalArgumentException {
            if(object instanceof Attribute)
                return ((Attribute) object).getValue();
            else
                return object;
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalArgumentException {
            throw new IllegalArgumentException("Can't change the value itself");
        }
        
    }

}
