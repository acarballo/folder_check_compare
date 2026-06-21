package jfcc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnapshotTest {

    @Test
    void loadSnapshot_recorreFicherosYCarpetasConRutasRelativas(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("a.txt"), "hola a");
        Path sub = Files.createDirectory(tempDir.resolve("sub"));
        Files.writeString(sub.resolve("b.txt"), "hola b");

        Snapshot snapshot = new Snapshot(tempDir.toString());
        snapshot.loadSnapshot();

        // raiz ("") + a.txt + carpeta sub + sub/b.txt
        assertEquals(4, snapshot.getNumElements());
        assertTrue(snapshot.getAll().containsKey("/a.txt"));
        assertTrue(snapshot.getAll().containsKey("/sub"));
        assertTrue(snapshot.getAll().containsKey("/sub/b.txt"));

        assertEquals(FileResume.TYPE_FILE, snapshot.getAll().get("/a.txt").getType());
        assertEquals(FileResume.TYPE_FOLDER, snapshot.getAll().get("/sub").getType());
    }

    @Test
    void loadSnapshot_ignoraCarpetasIndicadasEnFilesIgnored(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("a.txt"), "hola a");
        Path sub = Files.createDirectory(tempDir.resolve("sub"));
        Files.writeString(sub.resolve("b.txt"), "hola b");

        Snapshot snapshot = new Snapshot(tempDir.toString());
        ArrayList<String> ignorados = new ArrayList<>();
        ignorados.add("sub");
        snapshot.setFilesIgnored(ignorados);
        snapshot.loadSnapshot();

        assertEquals(2, snapshot.getNumElements()); // raiz + a.txt
        assertFalse(snapshot.getAll().containsKey("/sub"));
        assertFalse(snapshot.getAll().containsKey("/sub/b.txt"));
    }

    @Test
    void refreshSnapshotCRC32_calculaElCrcDeCadaFichero(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("a.txt"), "hola a\n");

        Snapshot snapshot = new Snapshot(tempDir.toString());
        snapshot.loadSnapshot();
        snapshot.refreshSnapshotCRC32();

        FileResume archivo = snapshot.getAll().get("/a.txt");
        assertEquals(2813435713L, archivo.getCrc32());
    }

    @Test
    void getPath_devuelveLaRutaConLaQueSeCreoElSnapshot() {
        Snapshot snapshot = new Snapshot("/tmp/cualquiera");
        assertEquals("/tmp/cualquiera", snapshot.getPath());
    }
}
