package org.hishab.agent.query.api.mapper;

import org.hishab.agent.core.model.agent.Agent;
import org.hishab.agent.query.api.model.agent.AgentDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AgentMapper {
    AgentMapper INSTANCE = Mappers.getMapper(AgentMapper.class);

    Agent toAgent(AgentDocument agentDocument);
}
