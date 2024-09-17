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
import argparse

def check_crs_match(input_proj, expected_proj):
    try:
        crs_input = CRS(input_proj)
        crs_expected = CRS(expected_proj)
        return crs_input.equals(crs_expected)
    except Exception as e:
        print(f"Error comparing CRSs: {e}")
        return False

def main(input_file, expected_file, output_file, diff_file):
    # Read the expected CSV file into a dictionary
    number = 0
    expected_crs = {}
    with open(expected_file, 'r') as ef:
        reader = csv.reader(ef)
        header = next(reader)  # Skip the header row
        for row in reader:
            epsg_code = row[0].strip()
            proj_def = row[1].strip()
            expected_crs[epsg_code] = proj_def
            number += 1
    
    # Open output file for writing
    with open(output_file, 'w', newline='') as of, open (diff_file, 'w') as d_of:
        writer = csv.writer(of)
        writer.writerow(['EPSG Code', 'Input PROJ', 'Expected PROJ'])  # Write header
        wrong_number = 0
        # Read the input CSV file and compare with expected CRS
        with open(input_file, 'r') as inf:
            reader = csv.reader(inf)
            header = next(reader)  # Skip the header row
            for row in reader:
                epsg_code = row[0].strip()
                input_proj = row[1].strip()
                
                expected_proj = expected_crs.get(epsg_code)
                
                if expected_proj:
                    # Compare CRSs
                    if not check_crs_match(input_proj, expected_proj):
                        writer.writerow([epsg_code, input_proj, expected_proj])
                        d_of.writelines(epsg_code + '\n')
                        wrong_number += 1

    
    print(f"Unmatched CRS entries written to '{output_file}'.")
    print(f"Unmatched CRS entries {wrong_number} out of {number} total CRSs")
    

def parse_proj(proj_string):
    """
    Parse a projection definition string into a dictionary of key-value pairs.
    Example: "+proj=omerc +datum=NAD83" becomes {'+proj': 'omerc', '+datum': 'NAD83'}
    """
    proj_dict = {}
    pairs = proj_string.split()
    for pair in pairs:
        if '=' in pair:
            key, value = pair.split('=', 1)
            proj_dict[key] = value
    return proj_dict

def find_missing_keys(input_proj, expected_proj):
    """
    Find the key-value pairs that are missing from input_proj compared to expected_proj.
    """
    missing = {}
    for key, value in expected_proj.items():
        if key not in input_proj or input_proj[key] != value:
            missing[key] = value
    return missing

def generate_difference_csv(input_file, output_file):
    """
    Generate an output CSV containing the EPSG code and the differences
    between the input proj definition and the expected proj definition.
    """
    with open(input_file, 'r') as infile, open(output_file, 'w', newline='') as outfile:
        reader = csv.reader(infile)
        writer = csv.writer(outfile)
        next(reader)

        writer.writerow(["EPSG", "MissingKeys", "ExtraKeys"])

        for row in reader:
            epsg_code = row[0]
            input_proj = parse_proj(row[1])
            expected_proj = parse_proj(row[2])

            missing_keys = find_missing_keys(input_proj, expected_proj)
            missing_str = ' '.join(f"{key}={value}" for key, value in missing_keys.items())

            extra_keys = find_missing_keys(expected_proj,input_proj)
            extra_str = ' '.join(f"{key}={value}" for key, value in extra_keys.items())

            # Write the EPSG code and missing keys to the output CSV
            writer.writerow([epsg_code, missing_str, extra_str])

if __name__ == "__main__":
    """
    Compare 2 CSV files containing:
     - GeoTools generated PROJ String definitions
     - PROJ library String definitions generated for EPSG code
    Produce a CSV file only containing EPSG codes that resulted in GeoTools generating a different PROJ CRS.
    CSV will contain EPSG Code, input GeoTools PROJ String and expected PROJ String.
    Produce a secondary file only showing the differences:
    CSV will contain EPSG Code, MissingKeys that are expected by PROJ and have not been
    written by GeoTools; ExtraKeys that are keys produced by GeoTools that are not present in PROJ output.
    Note that differences are only produced if the 2 CRS being built by the PROJ Lib on top of the 2 Proj String
    are not equivalent as result of the check_crs_match function.
    """

    parser = argparse.ArgumentParser(description="Compare PROJ definitions from two CSV files.")
    parser.add_argument("input_file", help="Path to the input CSV file with PROJ definitions.")
    parser.add_argument("expected_file", help="Path to the expected CSV file with PROJ definitions.")
    parser.add_argument("output_file", help="Path to the output CSV file for unmatched CRS entries, containing full String.")
    parser.add_argument("diff_file", help="Path to the output file for unmatched CRS entries, with only differences.")
    
    args = parser.parse_args()
    
    main(args.input_file, args.expected_file, args.output_file, args.diff_file)
    generate_difference_csv(args.output_file, args.diff_file)

