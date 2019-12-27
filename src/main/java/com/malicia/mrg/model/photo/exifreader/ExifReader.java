package com.malicia.mrg.model.photo.exifreader;

import java.io.File;
import java.io.IOException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class ExifReader
{
    private ExifReader() {
        throw new IllegalStateException("Utility class");
    }

    public static String printOriDateTime(String filename){
        File file = new File( filename );
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    String tagName = tag.getTagName();
                    String desc = tag.getDescription();
                    if ("Date/Time Original".compareTo(tagName) == 0) {
                            return desc.split(" ")[0].replace(":", "");
                    }
                }
            }
        } catch (ImageProcessingException | IOException e) {
            logger.log("context" , e);
        }
        return "";
    }

}