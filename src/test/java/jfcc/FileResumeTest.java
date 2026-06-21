package jfcc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.CRC32;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileResumeTest {

    @Test
    void getExtension_devuelveLaExtensionDeUnFichero(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("documento.txt");
        Files.writeString(file, "contenido");

        FileResume resume = new FileResume("documento.txt", FileResume.TYPE_FILE);
        resume.setAbsolutePath(file.toString());

        assertEquals("txt", resume.getExtension());
    }

    @Test
    void getExtension_devuelveNullSiElFicheroNoTieneExtension(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("README");
        Files.writeString(file, "contenido");

        FileResume resume = new FileResume("README", FileResume.TYPE_FILE);
        resume.setAbsolutePath(file.toString());

        assertNull(resume.getExtension());
    }

    @Test
    void getExtension_devuelveNullParaCarpetas() {
        FileResume resume = new FileResume("carpeta", FileResume.TYPE_FOLDER);
        resume.setAbsolutePath("/tmp/carpeta");

        assertNull(resume.getExtension());
    }

    @Test
    void refreshCRC32_calculaElMismoValorQueJavaUtilZipCrc32(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("a.bin");
        byte[] content = "Hola Folder Check Compare".getBytes(StandardCharsets.UTF_8);
        Files.write(file, content);

        FileResume resume = new FileResume("a.bin", FileResume.TYPE_FILE);
        resume.setAbsolutePath(file.toString());
        resume.refreshCRC32();

        CRC32 expected = new CRC32();
        expected.update(content);

        assertEquals(expected.getValue(), resume.getCrc32());
    }

    @Test
    void search_encuentraUnaCadenaPresenteEnElFichero(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("notas.txt");
        Files.writeString(file, "linea 1\nTODO: revisar esto\nlinea 3");

        FileResume resume = new FileResume("notas.txt", FileResume.TYPE_FILE);
        resume.setAbsolutePath(file.toString());

        assertTrue(resume.search("TODO"));
        assertFalse(resume.search("FIXME"));
    }

    @Test
    void replace_sustituyeTextoEnElFicheroYLoPersisteEnDisco(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("config.txt");
        Files.writeString(file, "version=0.1.a5");

        FileResume resume = new FileResume("config.txt", FileResume.TYPE_FILE);
        resume.setAbsolutePath(file.toString());

        boolean replaced = resume.replace("0.1.a5", "0.2.0");

        assertTrue(replaced);
        assertEquals("version=0.2.0", Files.readString(file));
    }
}
