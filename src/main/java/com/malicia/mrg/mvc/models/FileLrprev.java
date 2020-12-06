package com.malicia.mrg.mvc.models;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

public class FileLrprev {

    private static final byte[] JPEG_Start_bytes = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] JPEG_end_bytes = new byte[]{(byte) 0xFF, (byte) 0xD9};

    private FileLrprev() {
        throw new IllegalStateException("Utility class");
    }

    public static InputStream getLastJpegFromLrprev(File filePreview) throws IOException {
        byte[] fileContent = Files.readAllBytes(filePreview.toPath());

        int fromLast;
        int from=0;
        int to=0;
        do {
            fromLast = from;
            from = indexOf(fileContent, JPEG_Start_bytes, from + 1);
        } while (from > -1 && from-fromLast < 20000);
        to = indexOf(fileContent, JPEG_end_bytes,fromLast+1);

        byte[] jpg = Arrays.copyOfRange(fileContent, fromLast, to);

        return new ByteArrayInputStream(jpg);
    }

    private static int indexOf(byte[] data, byte[] pattern , int start) {
        if (data.length == 0) return -1;

        int[] failure = computeFailure(pattern);
        int j = 0;

        for (int i = start; i < data.length; i++) {
            while (j > 0 && pattern[j] != data[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == data[i]) {
                j++;
            }
            if (j == pattern.length) {
                return i - pattern.length + 1;
            }
        }
        return -1;
    }

    /**
     * Computes the failure function using a boot-strapping process,
     * where the pattern is matched against itself.
     */
    private static int[] computeFailure(byte[] pattern) {
        int[] failure = new int[pattern.length];

        int j = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (j > 0 && pattern[j] != pattern[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == pattern[i]) {
                j++;
            }
            failure[i] = j;
        }

        return failure;
    }

}
