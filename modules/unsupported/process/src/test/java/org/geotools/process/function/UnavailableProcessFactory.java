package org.geotools.process.function;

import java.awt.RenderingHints.Key;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;
import org.geotools.text.Text;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

public class UnavailableProcessFactory implements ProcessFactory {
    final Name NAME = new NameImpl("test", "unavailable");

    @Override
    public Map<Key, ?> getImplementationHints() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean supportsProgress(Name name) {
        return true;
    }

    @Override
    public String getVersion(Name name) {
        if (name.equals(NAME)) return "1.0";
        return null;
    }

    @Override
    public InternationalString getTitle(Name name) {
        if (name.equals(NAME)) return Text.text("Evil");
        return null;
    }

    @Override
    public InternationalString getTitle() {
        return Text.text("Test Factory");
    }

    @Override
    public Map<String, Parameter<?>> getResultInfo(Name name, Map<String, Object> parameters)
            throws IllegalArgumentException {
        if (NAME.equals(name) && (parameters == null || parameters.isEmpty())) {
            return Collections.singletonMap("result", new Parameter<>("result", String.class));
        }
        return null;
    }

    @Override
    public Map<String, Parameter<?>> getParameterInfo(Name name) {
        if (name.equals(NAME)) {
            return Collections.singletonMap("test", new Parameter<>("test", String.class));
        }
        return null;
    }

    @Override
    public Set<Name> getNames() {
        return Collections.singleton(NAME);
    }

    @Override
    public InternationalString getDescription(Name name) {
        return null;
    }

    @Override
    public Process create(Name name) {
        return null;
    }
}
