package org.hishab.agent.cmd.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.hishab.agent.cmd.api", "org.hishab.agent.core"})
public class AgentCommandApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgentCommandApplication.class, args);
    }
}