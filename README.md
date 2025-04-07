# Migración de Page Object Model a Screenplay Pattern

## 📚 Diferencias Teóricas

### 🏗️ Page Object Model (POM)
**Enfoque:**
- Orientado a la estructura de la UI
- Encapsula elementos y acciones por página
- Métodos representan acciones del usuario

**Limitaciones:**
- Alto acoplamiento con implementación UI
- Difícil reutilización entre páginas
- Validaciones mezcladas con interacciones

### 🎭 Screenplay Pattern
**Enfoque:**
- Modela comportamientos del usuario como actor
- Organiza flujos en tareas e interacciones
- Separa claramente acciones, preguntas y habilidades

**Ventajas:**
- Código más expresivo y mantenible
- Bajo acoplamiento con la UI
- Alta reutilización de componentes
- Reportes más significativos

## 🛠 Diferencias

| Entidad/Concepto       | Page Object Model (POM)                          | Screenplay Pattern                                |
|------------------------|--------------------------------------------------|--------------------------------------------------|
| **Unidad básica**      | Página (Page Object)                             | Actor (Usuario del sistema)                      |
| **Elementos UI**       | Clases Page con WebElements                     | Clases UI con Targets estáticos                  |
| **Acciones**           | Métodos en Page Objects                         | Tasks (tareas compuestas) e Interactions         |
| **Validaciones**       | Métodos en Page Objects/Test                    | Questions (clases separadas)                     |
| **Flujos**             | Step Definitions llamando Page Objects          | Step Definitions delegando en Tasks              |
| **Composición**        | Herencia entre Page Objects                     | Composición de Tasks y Interactions              |
| **Datos de prueba**    | Models separados o parámetros en métodos        | Capacidades del Actor (Abilities)                |
| **Navegación**         | Métodos return Page Objects                     | Tasks que modifican el estado del Actor          |
| **Step Definitions**   | Contienen lógica de llamadas a Page Objects     | Delegan completamente en Tasks y Questions       |
| **Reutilización**      | A nivel de página/metodo                        | A nivel de interacción atómica                   |
| **Reporting**          | Basado en pasos técnicos                        | Semántico (tareas con significado de negocio)    |
| **Relación con UI**    | Directa (1:1 con elementos de pantalla)         | Indirecta (a través de Targets abstractos)       |
| **Manejo de estado**   | Implícito en navegación entre páginas           | Explícito en el Actor y sus Abilities            |
| **Ejecución**          | Secuencia lineal de métodos                     | Flujo declarativo (actor.attemptsTo/should)       |
| **Dependencias**       | BasePage común con utilidades                   | Serenity BDD (opcional pero recomendado)         |
| **Ejemplo de entidad** | `LoginPage.java` con métodos login()            | `LoginTask.java` que usa `LoginUI.java`          |
| **Manejo de errores**  | Try/catch en métodos o tests                    | Patrón AttemptsTo + Fallback Strategies          |
| **Paralelismo**        | Depende de implementación de drivers            | Nativo mediante Actores independientes           |
| **Abstracción**        | Nivel técnico (click, sendKeys)                 | Nivel de negocio (hacer login, completar formulario) |

## 🛠 Configuración con Maven

Agrega al `pom.xml`:

```xml
<dependencies>
    <!-- Serenity Screenplay -->
    <dependency>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-screenplay</artifactId>
        <version>3.6.12</version>
    </dependency>
    <dependency>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-screenplay-webdriver</artifactId>
        <version>3.6.12</version>
    </dependency>
    
    <!-- JUnit y otras dependencias -->
    <dependency>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-junit</artifactId>
        <version>3.6.12</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>net.serenity-bdd.maven.plugins</groupId>
            <artifactId>serenity-maven-plugin</artifactId>
            <version>3.6.12</version>
            <executions>
                <execution>
                    <id>serenity-reports</id>
                    <phase>post-integration-test</phase>
                    <goals>
                        <goal>aggregate</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>