"""
    GeoTools - The Open Source Java GIS Toolkit
    http://geotools.org

    (C) 2024, Open Source Geospatial Foundation (OSGeo)

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation;
    version 2.1 of the License.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
"""

import csv
from pyproj import CRS

output_file = "epsg_proj_definitions.csv"
with open(output_file, mode="w", newline='') as csv_file:
    writer = csv.writer(csv_file)
    writer.writerow(["EPSG Code", "PROJ Definition"])
    
    # Iterate over EPSG codes supported by pyproj
    for epsg_code in range(1, 1000000):
        try:
            crs = CRS.from_epsg(epsg_code)
            proj_string = crs.to_proj4()
            writer.writerow([epsg_code, proj_string])
        except Exception as e:
            continue

print(f"CSV file '{output_file}' generated successfully.")
