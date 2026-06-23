package jfcc;

/**
 * Proporciona la version de la aplicacion en runtime.
 *
 * El valor proviene de la entrada {@code Implementation-Version} del
 * {@code MANIFEST.MF}, que Maven escribe automaticamente con el valor
 * de {@code <version>} del {@code pom.xml} al ejecutar {@code mvn package}.
 *
 * Fuera del jar (p.ej. durante los tests con {@code mvn test}) el manifiesto
 * no existe, por lo que {@code getImplementationVersion()} devuelve
 * {@code null}; en ese caso se muestra "dev".
 */
final class Version {

    static final String VALUE = resolve();

    private Version() {
    }

    private static String resolve() {
        String v = Version.class.getPackage().getImplementationVersion();
        return v != null ? v : "dev";
    }
}
