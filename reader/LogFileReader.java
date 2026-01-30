package reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

public class LogFileReader {

    public void readFile(String filePath, Consumer<String> lineConsumer) {

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                lineConsumer.accept(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
