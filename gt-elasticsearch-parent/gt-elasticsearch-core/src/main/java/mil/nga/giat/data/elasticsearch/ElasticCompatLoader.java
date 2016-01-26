package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.util.logging.Logging;

public class ElasticCompatLoader {

    protected static final Logger LOGGER = Logging.getLogger(ElasticCompatLoader.class);

    private static final String DEFAULT_ES_COMPAT_VERSION = "2";

    private static final String DEFAULT_ES_CLASS_NAME = ElasticCompat.class.getName() + DEFAULT_ES_COMPAT_VERSION;

    private static final Pattern VERSION_PATTERN = Pattern.compile("version.*?(\\d+)?\\.");

    private static ElasticCompat cachedCompat;

    public synchronized static ElasticCompat getCompat(String classOverride) {
        if (cachedCompat != null) {
            LOGGER.fine("Returning cached Elasticsearch compatibility layer: " + cachedCompat.getClass().getCanonicalName());
            return cachedCompat;
        }

        String className = null;
        if (classOverride != null) {
            className = classOverride;
        } else {
            ClassLoader classLoader = ElasticCompatLoader.class.getClassLoader();
            try (InputStream in = classLoader.getResourceAsStream("es-build.properties"); Scanner s = new Scanner(in)) {
                if (in != null) {
                    s.useDelimiter("\\A");
                    Matcher m = VERSION_PATTERN.matcher(s.next());
                    if (m.find()) {
                        final String majorVersion = m.group(1);
                        className = ElasticCompat.class.getName() + majorVersion;
                    }
                }
            } catch (IOException e) {
            }
            if (className == null) {
                LOGGER.info("Runtime Elasticsearch version could not be detected.  " +
                         "Loading compatibility layer for Elasticsearch version " + DEFAULT_ES_COMPAT_VERSION);
                className = DEFAULT_ES_CLASS_NAME;
            }
        }

        final String errTemplate = " when instantiating Elasticsearch compatibility class " + className;

        ElasticCompat compat;
        try {
            compat = (ElasticCompat) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e.getClass().getSimpleName() + errTemplate, e);
        }
        LOGGER.info("Instantiated Elasticsearch compatibility layer: " + compat.getClass().getCanonicalName());

        return cachedCompat = compat;
    }
}

