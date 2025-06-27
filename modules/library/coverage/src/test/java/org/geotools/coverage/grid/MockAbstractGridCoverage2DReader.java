/* (c) 2019 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.coverage.grid;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageReadParam;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.coverage.grid.GridEnvelope;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.geometry.GeneralBounds;

/**
 * A Mock Class to access methods for AbstractGridCoverage2DReader class Intially intended for GEOT-6088
 *
 * @author Imran Rajjad
 */
public class MockAbstractGridCoverage2DReader extends AbstractGridCoverage2DReader {

    public void setOetOriginalGridRange(GridEnvelope mockOriginalGridRange) {
        super.originalGridRange = mockOriginalGridRange;
    }

    public void setHighestResolution(double resolutionX, double resolutionY) {
        super.highestRes = new double[2];
        super.highestRes[0] = resolutionX;
        super.highestRes[1] = resolutionY;
    }

    public void setCRS(CoordinateReferenceSystem mockCRC) {
        super.crs = mockCRC;
    }

    @Override
    public Format getFormat() {

        return new Format() {

            @Override
            public ParameterValueGroup getWriteParameters() {

                return null;
            }

            @Override
            public String getVersion() {

                return null;
            }

            @Override
            public String getVendor() {

                return null;
            }

            @Override
            public ParameterValueGroup getReadParameters() {

                return null;
            }

            @Override
            public String getName() {

                return "mock";
            }

            @Override
            public String getDocURL() {

                return null;
            }

            @Override
            public String getDescription() {
                return "mock format used in Junit";
            }
        };
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue... parameters) throws IllegalArgumentException, IOException {
        return new GridCoverageBuilder().getGridCoverage2D();
    }

    @Override
    protected Integer setReadParams(
            OverviewPolicy overviewPolicy,
            ImageReadParam readP,
            GeneralBounds requestedEnvelope,
            Rectangle requestedDim)
            throws IOException, TransformException {
        return super.setReadParams(overviewPolicy, readP, requestedEnvelope, requestedDim);
    }

    @Override
    protected Integer setReadParams(
            String coverageName,
            OverviewPolicy overviewPolicy,
            ImageReadParam readP,
            GeneralBounds requestedEnvelope,
            Rectangle requestedDim)
            throws IOException, TransformException {
        return super.setReadParams(coverageName, overviewPolicy, readP, requestedEnvelope, requestedDim);
    }
}
