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

## Uso

### Menú interactivo

Si ejecutas el jar sin argumentos, se muestra un menú interactivo que guía
por las opciones disponibles y solicita los parámetros necesarios:

```bash
java -jar target/jfcc.jar
```

```
  jfcc v0.1.0  — Folder Check Compare

  Herramientas disponibles
  ─────────────────────────────────────────
  1  Comparator    comparar dos carpetas
  2  Analizer      contar ficheros por extensión
  3  Search        buscar texto en ficheros
  4  Replace       reemplazar texto en ficheros
  0  Salir
  ─────────────────────────────────────────
  Opcion: 1

  ▶ Comparator — comparar dos carpetas

  Carpeta origen : /proyectos/v1
  Carpeta destino: /proyectos/v2
  ...
  ¿Exportar informe a fichero? [s/N]:
```

Al finalizar cada herramienta ofrece exportar el informe a un fichero `.txt`.

---

### Uso directo por línea de comandos

Las herramientas también se pueden invocar directamente sin pasar por el menú.

#### Comparator — comparar dos carpetas

```bash
java -jar target/jfcc.jar <origen> <destino>
```

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

#### Analizer — analizar el contenido de una carpeta

```bash
java -cp target/jfcc.jar jfcc.Analizer <carpeta>
```

```
Analizer
================================
java: 9
xml: 2
md: 1
```

#### Search — buscar texto en los ficheros de una carpeta

```bash
java -cp target/jfcc.jar jfcc.Search <carpeta> "<texto>"
```

```
Search String [TODO]
================================
/src/Main.java
/src/util/Helper.java
Find:2
```

#### Replace — reemplazar texto en los ficheros de una carpeta

Sustituye todas las ocurrencias en todos los ficheros. **Modifica los
ficheros en disco**, úsalo con precaución.

```bash
java -cp target/jfcc.jar jfcc.Replace <carpeta> "<buscar>" "<reemplazar>"
```

```
Replace String [v1.0]
================================
/src/Main.java
Replaced:1
```

---

## Estructura del proyecto

```
src/
├── main/java/jfcc/
│   ├── Main.java           # menú interactivo (punto de entrada del jar)
│   ├── AbstractTool.java   # clase base con la lógica común
│   ├── Snapshot.java       # escanea una carpeta y calcula CRC32
│   ├── FileResume.java     # representa un fichero o carpeta individual
│   ├── Version.java        # lee la versión del MANIFEST.MF en runtime
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
