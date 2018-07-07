/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import javax.imageio.stream.ImageInputStreamImpl;
import org.geotools.s3.cache.CacheEntryKey;
import org.geotools.s3.cache.CacheManagement;

/** ImageInputStream implementation that fetches and caches data from S3 */
public class S3ImageInputStreamImpl extends ImageInputStreamImpl {

    private static final Logger LOGGER = Logger.getLogger("S3");
    private final String fileName;
    private final S3Connector connector;
    private String url;
    private String bucket;
    private String key;
    private AmazonS3 s3;
    private long length;
    private int cacheBlockSize;

    private AmazonS3 getS3Client() {
        if (this.s3 == null) {
            s3 = this.connector.getS3Client();
        }

        return this.s3;
    }

    public S3ImageInputStreamImpl(URL input) throws IOException {
        this(input.toString());
    }

    public S3ImageInputStreamImpl(String input) throws IOException {
        this.connector = new S3Connector(input);
        this.url = input;
        String urlWithoutQueryString = input.split("\\?")[0];
        String parts[] = urlWithoutQueryString.split("/");
        if (input.startsWith("http")) {
            this.bucket = parts[parts.length - 2];
            this.key = parts[parts.length - 1];
        } else {
            StringBuilder keyBuilder = new StringBuilder();
            this.bucket = parts[2];
            for (int i = 3; i < parts.length; i++) {
                keyBuilder.append("/").append(parts[i]);
            }
            this.key = keyBuilder.toString();
            /* Strip leading slash */
            this.key = this.key.startsWith("/") ? this.key.substring(1) : this.key;
        }

        AmazonS3 s3Client = this.getS3Client();
        ObjectMetadata meta =
                s3Client.getObjectMetadata(new GetObjectMetadataRequest(this.bucket, this.key));
        this.length = meta.getContentLength();

        this.fileName = nameFromKey(this.key);
        this.cacheBlockSize = CacheManagement.DEFAULT.getCacheConfig().getChunkSizeBytes();
    }

    private String nameFromKey(String key) {
        String[] parts = key.split("/");
        return parts[parts.length - 1];
    }

    private byte[] getFromCache(int block) throws IOException {
        int blockSizeForBlock = this.calculateBlockSizeForBlock(block);
        CacheEntryKey keyForBlock =
                new CacheEntryKey(this.bucket, this.key, block, blockSizeForBlock);
        return CacheManagement.DEFAULT.getChunk(keyForBlock, connector);
    }

    private int calculateBlockSizeForBlock(int block) {
        long offsetInFile = this.cacheBlockSize * block;
        long remainingInFile = this.length - offsetInFile;
        return (int) Math.min(this.cacheBlockSize, remainingInFile);
    }

    private byte readFromCache(int block, int offset) {
        try {
            byte[] byteBlock = this.getFromCache(block);
            return byteBlock[offset];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int read() throws IOException {
        byte rawValue = readRawValue();
        return Byte.toUnsignedInt(rawValue);
    }

    private byte readRawValue() {
        // determine block & offset
        int block = getBlockIndex();
        int offset = getCurrentOffset();
        byte rawValue = readFromCache(block, offset);
        this.streamPos++;
        return rawValue;
    }

    private int getCurrentOffset() {
        return (int) (streamPos % this.cacheBlockSize);
    }

    @Override
    public int read(byte targetBuffer[], int off, int len) throws IOException {

        int readRemaining = len;
        ByteBuffer readBuffer = ByteBuffer.allocate(len);
        while (readRemaining > 0 && streamPos < this.length) {
            int block = getBlockIndex();
            int offset = getCurrentOffset();
            byte[] blockBytes = this.getFromCache(block);
            // block could be longer than what we want to read... or shorter, or longer than the
            // rest of the block
            int bytesToRead = Math.min(readRemaining, blockBytes.length - offset);
            readBuffer.put(blockBytes, offset, bytesToRead);
            readRemaining -= bytesToRead;
            streamPos += bytesToRead;
        }

        ByteBuffer inputByteBuffer = ByteBuffer.wrap(targetBuffer, off, len);
        inputByteBuffer.put(readBuffer.array(), 0, len);

        return len - readRemaining;
    }

    private int getBlockIndex() {
        return (int) Math.floor((double) streamPos / this.cacheBlockSize);
    }

    @Override
    public String readLine() throws IOException {
        throw new IOException("readLine NOT Supported");
    }

    String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }
}
