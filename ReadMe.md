# tk-flights

## О модуле

Тестовое задание для старта трудоустройства № 2.  

Условия задания смотри в [Conditions.md](https://github.com/taker1974/tk-flights/blob/main/Conditions.md).  
Анализ задания смотри в [Analysis.md](https://github.com/taker1974/tk-flights/blob/main/Analysis.md).  

Базовые классы Segment и Flight были доработаны для лучшей тестируемости.  
Базовые классы Segment и Flight не дорабатывались на предмет сортировки сегментов, валидации, вычислениях на вставке и т.п. - ставилась задача фильтрации готовых данных.  

Фильтры - отдельные классы, реализующие функциональный интерфейс FlightFilter с методом process(..).  
process() может принимать произвольные параметры, что позволяет адаптировать логику каждой реализации под специфические задачи.  

В реализации фильтров и при их использовании максимально использованы стримы, что позволяет при необходимости распараллелить обработки. (В этой реализации не распараллеливается фильтр по времени на земле, но возможно простая реализация алгоритма на стримах - см. комментарий в коде.)  

Весь вывод текста использует slf4j + logback-classic. Смотри logback.xml.  
Все новые и изменённые классы покрыты тестами.  
Проект документирован javadoc.  

## Применяемые технологии

Java 21, openjdk,
Stream API, JUnit, JavaDoc,  
Fedora Linux 42, VS Code,
DeepSeek, GigaCode, git.

## Быстрый старт

В корне модуля:

```Bash
$mvn clean package
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
$mvn clean package
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
