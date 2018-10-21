package org.geotools.process.vector;

import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.text.Text;

/**
 * Factory providing a number of processes for working with feature data.
 *
 * <p>Internally this factory makes use of the information provided by the {@link DescribeProcess}
 * annotations to produce the correct process description.
 *
 * @author Jody Garnett (LISAsoft)
 */
public class VectorProcessFactory extends AnnotatedBeanProcessFactory {

    static volatile BeanFactoryRegistry<VectorProcess> registry;

    public static BeanFactoryRegistry<VectorProcess> getRegistry() {
        if (registry == null) {
            synchronized (VectorProcessFactory.class) {
                if (registry == null) {
                    registry = new BeanFactoryRegistry<VectorProcess>(VectorProcess.class);
                }
            }
        }
        return registry;
    }

    public VectorProcessFactory() {
        super(Text.text("Vector processes"), "vec", getRegistry().lookupBeanClasses());
    }
}
