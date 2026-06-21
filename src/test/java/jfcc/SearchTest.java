package jfcc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchTest {

    @Test
    void report_encuentraSoloLosFicherosQueContienenElToken(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("conTodo.txt"), "linea\nTODO: revisar\nfin");
        Files.writeString(tempDir.resolve("sinTodo.txt"), "nada interesante aqui");

        Search search = new Search(tempDir.toString(), "TODO");
        search.execute();

        String salida = TestConsoleCapture.capture(search::report);

        assertTrue(salida.contains("/conTodo.txt"));
        assertFalse(salida.contains("/sinTodo.txt"));
        assertTrue(salida.contains("Find:1"));
    }
}
