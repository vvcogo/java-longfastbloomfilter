package io.github.vvcogo;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class DictionaryDownloader {

    public static void download(String url, String output) throws IOException {
        Path outputPath = Path.of(output);
        if (!Files.exists(outputPath)) {
            Files.createFile(outputPath);
        }
        URL dictUrl = new URL(url);
        try (Scanner reader = new Scanner(dictUrl.openStream()); FileWriter writer = new FileWriter(output)) {
            while (reader.hasNextLine()) {
                writer.write(reader.nextLine() + "\n");
            }
        }
    }

}
