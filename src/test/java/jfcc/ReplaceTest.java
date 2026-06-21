package jfcc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplaceTest {

    @Test
    void report_sustituyeElTextoEnDiscoYLoReporta(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("notas.txt");
        Files.writeString(file, "TODO: viejo\notra linea");

        Replace replace = new Replace(tempDir.toString(), "viejo", "nuevo");
        replace.execute();

        String salida = TestConsoleCapture.capture(replace::report);

        assertTrue(salida.contains("/notas.txt"));
        assertTrue(salida.contains("Replaced:1"));
        assertEquals("TODO: nuevo\notra linea", Files.readString(file));
    }
}
