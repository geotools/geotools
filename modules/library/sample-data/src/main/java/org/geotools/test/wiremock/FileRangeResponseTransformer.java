/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.test.wiremock;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Provides support for range reads in Wiremock (which does not support range requests natively). Useful to test cloud
 * native data sources with a local Wiremock server
 */
public class FileRangeResponseTransformer implements ResponseDefinitionTransformerV2 {

    public static final String TRANSFORMER_NAME = "file-range-response-transformer";

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        String rangeHeader = serveEvent.getRequest().getHeader("Range");

        File file = (File) serveEvent.getTransformerParameters().get("filePath");

        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            int fileLength = fileBytes.length;

            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                // Parse the range
                String[] ranges = rangeHeader.substring(6).split("-");
                int start = Integer.parseInt(ranges[0]);
                int end = ranges.length > 1 && !ranges[1].isEmpty() ? Integer.parseInt(ranges[1]) : fileLength - 1;
                // the test file is small, so we can't have a range that goes beyond the file length
                if (end > fileLength - 1) end = fileLength - 1;

                // Ensure the range is valid
                if (start >= fileLength || start > end) {
                    return ResponseDefinitionBuilder.responseDefinition()
                            .withStatus(416) // Requested Range Not Satisfiable
                            .withHeaders(
                                    new HttpHeaders(HttpHeader.httpHeader("Content-Range", "bytes */" + fileLength)))
                            .build();
                }

                // Extract the requested range
                byte[] partialContent = java.util.Arrays.copyOfRange(fileBytes, start, end + 1);

                return ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(206) // Partial Content
                        .withHeaders(new HttpHeaders(
                                HttpHeader.httpHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength),
                                HttpHeader.httpHeader("Content-Length", String.valueOf(partialContent.length)),
                                HttpHeader.httpHeader("Content-Type", Files.probeContentType(file.toPath()))))
                        .withBody(partialContent)
                        .build();
            } else {
                // Full file response if no range is specified
                return ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(200)
                        .withHeaders(new HttpHeaders(
                                HttpHeader.httpHeader("Content-Length", String.valueOf(fileLength)),
                                HttpHeader.httpHeader("Content-Type", Files.probeContentType(file.toPath()))))
                        .withBody(fileBytes)
                        .build();
            }
        } catch (IOException e) {
            return ResponseDefinitionBuilder.responseDefinition()
                    .withStatus(500)
                    .withBody("Error reading file: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public String getName() {
        return TRANSFORMER_NAME;
    }

    @Override
    public boolean applyGlobally() {
        return false; // Apply this transformer only to specific mappings
    }
}
