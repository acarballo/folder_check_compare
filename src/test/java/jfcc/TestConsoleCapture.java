package jfcc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Utilidad de test para capturar lo que las clases del proyecto escriben en
 * System.out. Casi todas (Analizer, Comparator, Search, Replace) reportan su
 * resultado con System.out.println en vez de devolver un objeto, asi que
 * esta es la unica forma de verificar su comportamiento sin tocar el codigo
 * de produccion.
 */
final class TestConsoleCapture {

    private TestConsoleCapture() {
    }

    static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
