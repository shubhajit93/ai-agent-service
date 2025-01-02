package org.hishab.agent.query.api.mapper;

import org.hishab.agent.core.model.tts.Tts;
import org.hishab.agent.query.api.model.tts.TtsDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TtsMapper {
    TtsMapper INSTANCE = Mappers.getMapper(TtsMapper.class);

    Tts toTts(TtsDocument ttsDocument);
}
