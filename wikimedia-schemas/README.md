# Wikimedia Schemas

Central repository for Apache Avro schemas for the Wikimedia Lab project. This module provides a single source of truth for all event schemas transmitted through Kafka between producer, transform, and consumer services.

## Description

This Maven module centralizes:

- **Avro Schemas** (.avsc) defined in JSON format
- **Auto-generated Java classes** from schemas
- **Automatic schema registration** to Confluent Schema Registry
- **Schema versioning** and compatibility control

## Project Structure

```
wikimedia-schemas/
├── pom.xml
├── README.md
└── src/main/avro/
    └── wikimedia/
        ├── wikimedia-raw-event.avsc          # Raw Wikimedia events
        ├── wikimedia-filtered-event.avsc     # Filtered and processed events
        └── wikimedia-analysis-event.avsc     # Events with applied analysis
```

## Available Schemas

### 1. WikimediaRawEvent
**Topic**: `wikimedia.raw.events`  
**Description**: Raw events received directly from Wikimedia API, produced by `producer-wikimedia`.

**Namespace**: `com.example.wikimedia.avro`

### 2. WikimediaFilteredEvent
**Topic**: `wikimedia.filtered.events`  
**Description**: Filtered and transformed events, produced by `transform-wikimedia`.

**Namespace**: `com.example.wikimedia.avro`

### 3. WikimediaRawEvent (Analysis)
**Topic**: `wikimedia.analysis.events`  
**Description**: Events enriched with sentiment analysis, consumed by `consumer-wikimedia`.

**Namespace**: `com.example.wikimedia.avro`

## Installation and Build

### Prerequisites

- Java 21+
- Maven 3.6+
- Schema Registry running at `http://localhost:8085`

### Build the module

```bash
cd wikimedia-schemas
mvn clean install
```

This command:
1. Generates Java classes from .avsc schemas
2. Compiles the module
3. Registers schemas in Schema Registry (during `install` phase)
4. Installs the artifact to local Maven repository

### Generate classes only (without registering schemas)

```bash
mvn clean compile
```

### Register schemas manually

```bash
mvn kafka-schema-registry:register
```

## Usage in Other Modules

### Add as dependency

In the parent module's `pom.xml`, add to `<dependencyManagement>`:

```xml
<dependency>
    <groupId>com.learn</groupId>
    <artifactId>wikimedia-schemas</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

In the specific module (infrastructure/kafka):

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>wikimedia-schemas</artifactId>
</dependency>
```

### Use generated classes

```java
import com.example.wikimedia.avro.WikimediaRawEvent;
import com.example.wikimedia.avro.WikimediaFilteredEvent;
import com.example.wikimedia.avro.Meta;

// Create an event
WikimediaRawEvent event = WikimediaRawEvent.newBuilder()
    .setId(123L)
    .setType("edit")
    .setUser("john_doe")
    .setTitle("Wikipedia Article")
    // ... more fields
    .build();

// Send to Kafka
kafkaTemplate.send("wikimedia.raw.events", event);
```

## Kafka Configuration with Avro

### Producer Configuration

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    properties:
      schema.registry.url: http://localhost:8085
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: io.confluent.kafka.serializers.KafkaAvroSerializer
```

### Consumer Configuration

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    properties:
      schema.registry.url: http://localhost:8085
    consumer:
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: io.confluent.kafka.serializers.KafkaAvroDeserializer
      properties:
        specific.avro.reader: true  # Important: use generated specific classes
```

## Configured Maven Plugins

### 1. avro-maven-plugin
Generates Java classes from .avsc schemas during the `generate-sources` phase.

**Configuration**:
- **sourceDirectory**: `src/main/avro`
- **outputDirectory**: `target/generated-sources/avro`
- **stringType**: `String` (instead of `CharSequence`)
- **fieldVisibility**: `private` (with getters/setters)

### 2. kafka-schema-registry-maven-plugin
Registers schemas in Schema Registry during the `install` phase.

**Configuration**:
- **schemaRegistryUrls**: `http://localhost:8085`
- **subjects**: Mapping of topics to .avsc files

### 3. build-helper-maven-plugin
Adds generated sources to the project classpath.

## Workflow

### Modify an existing schema

1. Edit the `.avsc` file in `src/main/avro/wikimedia/`
2. Verify compatibility with Schema Registry
3. Build and install:
   ```bash
   mvn clean install
   ```
4. Update services using the schema
5. Rebuild affected services

### Add a new schema

1. Create new `.avsc` file in `src/main/avro/wikimedia/`
2. Define the schema in Avro JSON format
3. Update `pom.xml` to include the new subject in the Schema Registry plugin:
   ```xml
   <subjects>
       <new.topic-value>src/main/avro/wikimedia/new-schema.avsc</new.topic-value>
   </subjects>
   ```
4. Build and install:
   ```bash
   mvn clean install
   ```

## Schema Compatibility

Schema Registry validates compatibility by default using the **BACKWARD** strategy.

### Compatibility strategies:

- **BACKWARD**: New consumers can read old data
- **FORWARD**: Old consumers can read new data
- **FULL**: Bidirectional compatibility
- **NONE**: No compatibility validation

To change the strategy:

```bash
curl -X PUT -H "Content-Type: application/json" \
  --data '{"compatibility": "FULL"}' \
  http://localhost:8085/config/wikimedia.raw.events-value
```

## Troubleshooting

### Error: Schema Registry not accessible

**Problem**: `Failed to register schema: Connection refused`

**Solution**:
1. Verify Schema Registry is running: `docker ps | grep schema-registry`
2. Check the URL configured in pom.xml
3. To skip registration: `mvn clean install -Dskip.schema.registry=true`

### Error: Schema incompatibility

**Problem**: `Schema being registered is incompatible with an earlier schema`

**Solution**:
1. Review the changes made to the schema
2. Ensure changes are compatible (add optional fields, don't remove fields)
3. Or change the compatibility strategy if necessary

### Generated classes not found

**Problem**: `Cannot resolve symbol 'WikimediaRawEvent'`

**Solution**:
1. Verify the module compiled: `mvn clean compile`
2. Refresh project in IDE (IntelliJ: Reload Maven Project)
3. Check that `target/generated-sources/avro` is marked as source

## Benefits of This Approach

✅ **Single Source of Truth**: One place to define schemas  
✅ **Type Safety**: Compile-time typed Java classes  
✅ **Automatic Versioning**: Schema Registry manages versions  
✅ **No Duplication**: All services use the same artifact  
✅ **Early Validation**: Schema errors detected at build time  
✅ **Controlled Evolution**: Guaranteed compatibility between versions  
✅ **CI/CD Ready**: Easy to integrate into pipelines

## Services Using This Module

- **producer-wikimedia**: Produces `WikimediaRawEvent`
- **transform-wikimedia**: Consumes `WikimediaRawEvent`, produces `WikimediaFilteredEvent`
- **consumer-wikimedia**: Consumes `WikimediaFilteredEvent` and `WikimediaRawEvent` (analysis)

## References

- [Apache Avro Documentation](https://avro.apache.org/docs/current/)
- [Confluent Schema Registry](https://docs.confluent.io/platform/current/schema-registry/index.html)
- [Schema Evolution and Compatibility](https://docs.confluent.io/platform/current/schema-registry/avro.html)

## License

This project is part of Wikimedia Lab and is under the same license as the main project.
