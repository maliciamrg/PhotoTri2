package com.malicia.mrg.app;


import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class Packager {
    public static void packZip(File output, File sources) throws ZipException {
        try {

            //1.0 Create properties for Zip file creation
            Map<String, String> zipSysProps = new HashMap<>();
            zipSysProps.put("create", "true");

            //2.0 Form the URI for the Zip File
            Path zip_path = Paths.get(output.toPath().toString());
            URI zip_uri = new URI("jar:file",
                    zip_path.toUri().getPath(),
                    null);

            //3.0 Create the Zip file specified by the URI
            FileSystem ZipFileSystem =
                    FileSystems.newFileSystem(
                            zip_uri,
                            zipSysProps);
//            System.out.println("Zip File Created: " +
//                    ZipFileSystem.toString());

            //4.0 Get the Path of Source and
            // Destination file and Add to Zip
            //4.1 Create the Source locations
            Path Source1 = Paths.get(sources.toPath().toString());

            //4.2 Create Destination location in Zip
            Path Dest1 = ZipFileSystem.getPath(
                    "/"+ sources.getName());

            //4.3 Copy the Source to Destination
            // (Place File one by one)
            Files.move(
                    Source1,
                    Dest1,
                    StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File Moved to Zip:" + Dest1);

            ZipFileSystem.close();

        } catch (URISyntaxException Ex) {
            System.out.println("URISyntaxException: "
                    + Ex.getMessage());
        } catch (IOException Ex) {
            System.out.println("IO Exception: "
                    + Ex.getMessage());
        }
    }
}