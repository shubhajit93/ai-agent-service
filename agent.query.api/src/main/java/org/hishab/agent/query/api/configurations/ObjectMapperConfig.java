package org.hishab.agent.query.api.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.hishab.agent.core.exception.AgentQueryException;
import org.hishab.agent.query.api.serializations.ExceptionSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(AgentQueryException.class, new ExceptionSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
