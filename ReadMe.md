# tk-flights

## О модуле

Тестовое задание для старта трудоустройства № 2.  
Условия задания смотри в [Conditions.md]().  
Java, Stream API.

## Применяемые технологии

Java 21, openjdk,
Stream API,  
Lombok,  
JUnit, Testcontainers, Docker, Docker Compose,  
JavaDoc,  
Fedora Linux 42, VS Code,
DeepSeek, GigaCode,  
git, github projects, kanban.

## Быстрый старт

В корне модуля:

```Bash
$mvn clean install
$java -jar target/tk-flights-1.0.0.jar
```

## Подготовка к развёртыванию на узле

**Требуемое ПО**:

- Java >= 21.

Версии ПО могут быть другими. При разработке используется Java 21.  
Нет явных ограничений на использование других версий ПО.

**Java**:

- установить JDK или JRE версии не ниже 21 (разработка ведётся на версии 24: нет явных ограничений на использование других версий Java);
- убедиться в правильности установки, в доступности java, javac, maven (mvn);
- **важно** установить переменную JAVA_HOME (в ~/.bashrc, например):

```Bash
$export JAVA_HOME=/usr/lib/jvm/java-24-openjdk
```

При возникновении сложностей при работе с openjdk-24 можно откатиться на openjdk-21: просто замените во всех pom.xml версии 24 на 21.

## Запуск приложения

Смотри pom.xml. В корне проекта:

```Bash
$mvn clean install
$java -jar target/tk-flights-<ваша.версия.ПО>.jar
```

## Документация

### JavaDoc -> HTML

```Bash
mvn compile javadoc:javadoc
```

## Другое

### Проверка версий зависимостей

Добавьте плагин в pom.xml:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>versions-maven-plugin</artifactId>
            <version>2.16.2</version>
        </plugin>
    </plugins>
</build>
```

Запустите проверку:

```bash
mvn versions:display-dependency-updates
```

Плагин _versions-maven-plugin_ позволяет обновить версии в pom.xml автоматически:

```bash
mvn versions:use-latest-versions
```

После выполнения проверьте изменения в pom.xml.

**Ограничения**
SNAPSHOT-версии: Плагин _versions-maven-plugin_ игнорирует их по умолчанию.  
Используйте флаг -DallowSnapshots=true, чтобы их включить.

Кастомные репозитории: Если артефакт размещен не в Maven Central, убедитесь, что  
репозиторий добавлен в pom.xml/settings.xml.
