package io.github.vvcogo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class FileReader {

    private FileReader(){
        throw new UnsupportedOperationException("Can not create an instance of FileReader class.");
    }

    public static List<String> readFile(String path) throws FileNotFoundException {
        List<String> result = new ArrayList<>();
        try(Scanner sc = new Scanner(new File(path))) {
            while(sc.hasNextLine())
                result.add(sc.nextLine());
        }
        return result;
    }
}
