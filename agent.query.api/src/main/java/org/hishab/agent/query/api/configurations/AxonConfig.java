package org.hishab.agent.query.api.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.axonframework.common.transaction.NoTransactionManager;
import org.axonframework.config.ConfigurerModule;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class AxonConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUrl;

    @Value("${spring.data.mongodb.database}")
    private String database;

//    @Bean
//    @Primary
//    public Serializer defaultSerializer() {
//        ObjectMapper mapper = JacksonSerializer.defaultSerializer().getObjectMapper();
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        mapper.activateDefaultTyping(
//                mapper.getPolymorphicTypeValidator(),
//                ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE
//        );
//
//        return JacksonSerializer.builder()
//                .objectMapper(mapper)
//                .lenientDeserialization()
//                .build();
//    }

//    @Autowired
//    public void configureSerializers(ObjectMapper objectMapper) {
//        objectMapper.activateDefaultTyping(
//                objectMapper.getPolymorphicTypeValidator(),
//                ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT
//        );
//    }

    @Autowired
    public void configureSerializers(ObjectMapper objectMapper) {
        // Use LaissezFaireSubTypeValidator for permissive polymorphic type handling
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT
        );
    }


    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService workerExecutorService() {
        return Executors.newScheduledThreadPool(2);
    }

    @Bean
    public ConfigurerModule eventProcessingCustomizer() {
        return configurer -> configurer
                .eventProcessing()
                .usingPooledStreamingEventProcessors()
                .registerPooledStreamingEventProcessorConfiguration(
                        (c, b) -> b.workerExecutor(workerExecutorService())
                );
    }

//    @Bean(name = "axonEventSerializer")
//    @Primary
//    public Serializer eventSerializer() {
//        ObjectMapper mapper = new ObjectMapper();
//        return JacksonSerializer.builder().objectMapper(mapper).build();
//    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUrl);
    }

    @Bean
    public MongoTemplate axonMongoTemplate(MongoClient mongoClient) {
        // Creating Axon's MongoTemplate targeting a specific database
        return DefaultMongoTemplate.builder()
                .mongoDatabase(mongoClient, database) // Specify the database name
                .build();
    }

    @Bean
    public TokenStore tokenStore(MongoTemplate mongoTemplate,
//                                 @Qualifier("defaultSerializer")
//                                 @Qualifier("axonEventSerializer")
                                 Serializer eventSerializer) {
        return MongoTokenStore.builder()
                .mongoTemplate(mongoTemplate)
                .serializer(eventSerializer)
                .transactionManager(NoTransactionManager.instance())
                .build();
    }
}
