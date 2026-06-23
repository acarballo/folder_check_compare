# folder-check-compare (jfcc)

Herramienta de línea de comandos para comparar y analizar carpetas. Detecta
ficheros modificados, nuevos y eliminados calculando el CRC32 de cada fichero,
y también permite buscar y reemplazar texto de forma masiva.

## Requisitos

- Java 11 o superior
- Maven 3.6 o superior (solo para compilar)

## Compilar

```bash
mvn package
```

Genera `target/jfcc.jar`.

## Herramientas

### Comparator — comparar dos carpetas

Detecta qué ha cambiado entre una carpeta origen y una carpeta destino.

```bash
java -jar target/jfcc.jar <origen> <destino>
```

**Ejemplo:**

```bash
java -jar target/jfcc.jar /proyectos/v1 /proyectos/v2
```

```
Origin Folder:/proyectos/v1
Origin Elements:12
Destination Folder:/proyectos/v2
Destination Elements:13

Check modified elements
=======================
/src/Main.java
modified elements:1

Check new elements (Only Origin)
================================
/src/util/Helper.java
new elements:1

Check deleted elements (Only Destination)
=========================================
/src/LegacyService.java
deleted elements:1
```

---

### Analizer — analizar el contenido de una carpeta

Cuenta cuántos ficheros hay de cada extensión.

```bash
java -cp target/jfcc.jar jfcc.Analizer <carpeta>
```

**Ejemplo:**

```bash
java -cp target/jfcc.jar jfcc.Analizer /proyectos/v1
```

```
Origin Folder:/proyectos/v1
Origin Elements:12

Analizer
================================
java: 9
xml: 2
md: 1
```

---

### Search — buscar texto en los ficheros de una carpeta

Lista todos los ficheros que contienen una cadena de texto.

```bash
java -cp target/jfcc.jar jfcc.Search <carpeta> "<texto>"
```

**Ejemplo:**

```bash
java -cp target/jfcc.jar jfcc.Search /proyectos/v1 "TODO"
```

```
Origin Folder:/proyectos/v1
Origin Elements:12

Search String [TODO]
================================
/src/Main.java
/src/util/Helper.java
Find:2
```

---

### Replace — reemplazar texto en los ficheros de una carpeta

Sustituye todas las ocurrencias de una cadena en todos los ficheros de la carpeta.
**Modifica los ficheros en disco**, úsalo con precaución.

```bash
java -cp target/jfcc.jar jfcc.Replace <carpeta> "<texto_a_buscar>" "<texto_nuevo>"
```

**Ejemplo:**

```bash
java -cp target/jfcc.jar jfcc.Replace /proyectos/v1 "v1.0" "v2.0"
```

```
Origin Folder:/proyectos/v1
Origin Elements:12

Replace String [v1.0]
================================
/src/Main.java
Replaced:1
```

---

### Exportar el informe a fichero

Todas las herramientas pueden volcar su informe a un fichero `.txt` además de
mostrarlo por consola. Para ello, descomenta la línea `export()` en el `main()`
de la clase correspondiente y vuelve a compilar, o instancia la clase
directamente desde tu código:

```java
Comparator comp = new Comparator("/proyectos/v1", "/proyectos/v2");
comp.execute();
comp.export(); // genera report_<timestamp>.txt
```

Los ficheros exportados siguen el patrón `report_<timestamp>.txt`,
`report_analizer_<timestamp>.txt`, `report_search_<timestamp>.txt` y
`report_replace_<timestamp>.txt`.

---

## Estructura del proyecto

```
src/
├── main/java/jfcc/
│   ├── AbstractTool.java   # clase base con la lógica común
│   ├── Snapshot.java       # escanea una carpeta y calcula CRC32
│   ├── FileResume.java     # representa un fichero o carpeta individual
│   ├── Comparator.java     # herramienta: comparar dos carpetas
│   ├── Analizer.java       # herramienta: analizar extensiones
│   ├── Search.java         # herramienta: buscar texto
│   └── Replace.java        # herramienta: reemplazar texto
└── test/java/jfcc/
    ├── FileResumeTest.java
    ├── SnapshotTest.java
    ├── ComparatorTest.java
    ├── AnalizerTest.java
    ├── SearchTest.java
    └── ReplaceTest.java
```

## Ejecutar los tests

```bash
mvn test
```

## Licencia

Apache License 2.0 — ver [LICENSE](LICENSE).
