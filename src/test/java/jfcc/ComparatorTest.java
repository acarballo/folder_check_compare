package jfcc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ComparatorTest {

    @Test
    void report_detectaFicherosModificadosNuevosYBorrados(@TempDir Path tempDir) throws IOException {
        Path origen = Files.createDirectory(tempDir.resolve("origen"));
        Path destino = Files.createDirectory(tempDir.resolve("destino"));

        // presente en ambos, sin cambios
        Files.writeString(origen.resolve("comun.txt"), "contenido v1");
        Files.writeString(destino.resolve("comun.txt"), "contenido v1");

        // mismo nombre, contenido distinto -> modificado
        Files.writeString(origen.resolve("modificado.txt"), "contenido modificado");
        Files.writeString(destino.resolve("modificado.txt"), "contenido original");

        // solo en origen -> nuevo
        Files.writeString(origen.resolve("nuevo.txt"), "solo en origen");

        // solo en destino -> borrado
        Files.writeString(destino.resolve("borrado.txt"), "solo en destino");

        Comparator comparator = new Comparator(origen.toString(), destino.toString());
        comparator.execute();

        String salida = TestConsoleCapture.capture(comparator::report);

        assertTrue(salida.contains("/modificado.txt"));
        assertTrue(salida.contains("modified elements:1"));

        assertTrue(salida.contains("/nuevo.txt"));
        assertTrue(salida.contains("new elements:1"));

        assertTrue(salida.contains("/borrado.txt"));
        assertTrue(salida.contains("deleted elements:1"));
    }

    @Test
    void report_sinDiferencias_noReportaCambios(@TempDir Path tempDir) throws IOException {
        Path origen = Files.createDirectory(tempDir.resolve("origen"));
        Path destino = Files.createDirectory(tempDir.resolve("destino"));

        Files.writeString(origen.resolve("igual.txt"), "mismo contenido");
        Files.writeString(destino.resolve("igual.txt"), "mismo contenido");

        Comparator comparator = new Comparator(origen.toString(), destino.toString());
        comparator.execute();

        String salida = TestConsoleCapture.capture(comparator::report);

        assertTrue(salida.contains("modified elements:0"));
        assertTrue(salida.contains("new elements:0"));
        assertTrue(salida.contains("deleted elements:0"));
    }
}
