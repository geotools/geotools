package org.geotools.imageio.netcdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MultipleBandsDimensionInfo {

    private final List<String> bandsNames = new ArrayList<>();

    MultipleBandsDimensionInfo(String rawBandsNames) {
        Collections.addAll(bandsNames, rawBandsNames.split("\\s*,\\s*"));
    }

    void addBandName(String name) {
        bandsNames.add(name);
    }

    int getNumberOfBands() {
        return bandsNames.size();
    }

    List<String> getBandsNamesInOrder() {
        return bandsNames;
    }
}
