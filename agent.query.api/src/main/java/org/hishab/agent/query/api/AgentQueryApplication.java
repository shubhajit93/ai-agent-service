// File: agentDto.query.api/src/main/java/org/hishab/agentDto/query/api/AgentQueryApplication.java
package org.hishab.agent.query.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

//@EntityScan(basePackageClasses = {TokenEntry.class})
@EntityScan(basePackages = {"org.hishab.agent.query.api.model"})
@SpringBootApplication
@ComponentScan(basePackages = {"org.hishab.agent.query.api", "org.hishab.agent.core"})
public class AgentQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentQueryApplication.class, args);
    }
}