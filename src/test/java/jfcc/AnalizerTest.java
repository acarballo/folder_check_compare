package jfcc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalizerTest {

    @Test
    void report_cuentaFicherosPorExtension(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("a.txt"), "a");
        Files.writeString(tempDir.resolve("b.txt"), "b");
        Files.writeString(tempDir.resolve("c.md"), "c");

        Analizer analizer = new Analizer(tempDir.toString());
        analizer.execute();

        String salida = TestConsoleCapture.capture(analizer::report);

        assertTrue(salida.contains("txt: 2"));
        assertTrue(salida.contains("md: 1"));
    }
}
