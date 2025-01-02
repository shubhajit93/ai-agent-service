package org.hishab.agent.query.api.mapper;

import org.hishab.agent.core.model.stt.Stt;
import org.hishab.agent.query.api.model.stt.SttDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SttMapper {
    SttMapper INSTANCE = Mappers.getMapper(SttMapper.class);

    Stt toStt(SttDocument sttDocument);
}
