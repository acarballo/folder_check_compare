package jfcc;

import java.util.Scanner;

/**
 * Punto de entrada del jar. Si se invoca sin argumentos muestra un menu
 * interactivo que guia al usuario por las opciones disponibles y solicita
 * los parametros necesarios. Si se invoca con argumentos delega directamente
 * en la herramienta correspondiente (comportamiento original intacto):
 *
 * <pre>
 *   java -jar jfcc.jar                              → menu interactivo
 *   java -jar jfcc.jar &lt;origen&gt; &lt;destino&gt;           → Comparator directo
 *   java -cp jfcc.jar jfcc.Analizer &lt;carpeta&gt;       → Analizer directo
 *   java -cp jfcc.jar jfcc.Search  &lt;carpeta&gt; &lt;txt&gt;  → Search directo
 *   java -cp jfcc.jar jfcc.Replace &lt;carpeta&gt; &lt;s&gt; &lt;r&gt;→ Replace directo
 * </pre>
 */
public class Main {

    // Codigos ANSI — se desactivan automaticamente si el terminal no los soporta
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN  = "\u001B[32m";
    private static final String DIM    = "\u001B[2m";

    public static void main(String[] args) {
        if (args.length >= 2) {
            // Compatibilidad con el uso original: dos argumentos → Comparator
            Comparator.main(args);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        printBanner();

        while (running) {
            printMenu();
            String choice = prompt(scanner, "Opcion").trim();

            switch (choice) {
                case "1" -> runComparator(scanner);
                case "2" -> runAnalizer(scanner);
                case "3" -> runSearch(scanner);
                case "4" -> runReplace(scanner);
                case "0" -> running = false;
                default  -> warn("Opcion no valida. Introduce un numero del 0 al 4.");
            }

            if (running) {
                System.out.println();
            }
        }

        System.out.println(DIM + "Hasta luego." + RESET);
        scanner.close();
    }

    // -------------------------------------------------------------------------
    // Flujos de cada herramienta
    // -------------------------------------------------------------------------

    private static void runComparator(Scanner sc) {
        title("Comparator — comparar dos carpetas");
        String origen  = prompt(sc, "Carpeta origen ");
        String destino = prompt(sc, "Carpeta destino");
        System.out.println();
        Comparator tool = new Comparator(origen, destino);
        AbstractTool.runTimed(() -> { tool.execute(); tool.report(); });
        offerExport(sc, tool);
    }

    private static void runAnalizer(Scanner sc) {
        title("Analizer — extensiones de ficheros");
        String origen = prompt(sc, "Carpeta");
        System.out.println();
        Analizer tool = new Analizer(origen);
        AbstractTool.runTimed(() -> { tool.execute(); tool.report(); });
        offerExport(sc, tool);
    }

    private static void runSearch(Scanner sc) {
        title("Search — buscar texto en ficheros");
        String origen = prompt(sc, "Carpeta");
        String texto  = prompt(sc, "Texto a buscar");
        System.out.println();
        Search tool = new Search(origen, texto);
        AbstractTool.runTimed(() -> { tool.execute(); tool.report(); });
        offerExport(sc, tool);
    }

    private static void runReplace(Scanner sc) {
        title("Replace — reemplazar texto en ficheros");
        warn("Atencion: esta operacion modifica los ficheros en disco.");
        String origen   = prompt(sc, "Carpeta         ");
        String buscar   = prompt(sc, "Texto a buscar  ");
        String reemplazar = prompt(sc, "Texto nuevo     ");
        System.out.println();
        Replace tool = new Replace(origen, buscar, reemplazar);
        AbstractTool.runTimed(() -> { tool.execute(); tool.report(); });
        offerExport(sc, tool);
    }

    // -------------------------------------------------------------------------
    // Helpers de presentacion
    // -------------------------------------------------------------------------

    private static void printBanner() {
        System.out.println();
        System.out.println(BOLD + CYAN + "  jfcc " + Version.VALUE + RESET
                + DIM + " — Folder Check Compare" + RESET);
        System.out.println();
    }

    private static void printMenu() {
        System.out.println(BOLD + "  Herramientas disponibles" + RESET);
        System.out.println(DIM + "  ─────────────────────────────────────────" + RESET);
        System.out.println("  " + CYAN + "1" + RESET + "  Comparator  " + DIM + "comparar dos carpetas" + RESET);
        System.out.println("  " + CYAN + "2" + RESET + "  Analizer    " + DIM + "contar ficheros por extension" + RESET);
        System.out.println("  " + CYAN + "3" + RESET + "  Search      " + DIM + "buscar texto en ficheros" + RESET);
        System.out.println("  " + CYAN + "4" + RESET + "  Replace     " + DIM + "reemplazar texto en ficheros" + RESET);
        System.out.println("  " + CYAN + "0" + RESET + "  Salir");
        System.out.println(DIM + "  ─────────────────────────────────────────" + RESET);
    }

    private static void title(String text) {
        System.out.println();
        System.out.println(BOLD + GREEN + "  ▶ " + text + RESET);
        System.out.println();
    }

    private static void warn(String text) {
        System.out.println(YELLOW + "  ⚠  " + text + RESET);
    }

    private static String prompt(Scanner sc, String label) {
        System.out.print(BOLD + "  " + label + ": " + RESET);
        return sc.nextLine();
    }

    private static void offerExport(Scanner sc, AbstractTool tool) {
        System.out.println();
        System.out.print(DIM + "  ¿Exportar informe a fichero? [s/N]: " + RESET);
        String answer = sc.nextLine().trim().toLowerCase();
        if (answer.equals("s")) {
            tool.export();
            System.out.println(GREEN + "  Informe exportado." + RESET);
        }
    }

}
