package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    public void save(String path, Map<String, ?> data) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(path))) {
            output.writeObject(new HashMap<>(data));
        }
    }
    // giảm lặp code
    public Map<?, ?> load(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            Object data = input.readObject();
            if (!(data instanceof Map)) {
                throw new IOException("Invalid data format in " + path);
            }
            return (Map<?, ?>) data;
        }
    }
}

