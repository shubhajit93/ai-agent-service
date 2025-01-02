package org.hishab.agent.query.api.serializations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.hishab.agent.core.exception.AgentException;
import org.hishab.agent.core.exception.AgentQueryException;
import org.hishab.agent.core.exception.BaseException;

import java.io.IOException;

public class ExceptionSerializer extends StdSerializer<BaseException> {

    public ExceptionSerializer() {
        super(BaseException.class);
    }

    @Override
    public void serialize(BaseException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("message", value.getMessage());
        gen.writeStringField("status", "ERROR");

        // Check if the exception is an instance of AgentException or AgentQueryException
        if (value instanceof AgentException agentException) {
            gen.writeObjectField("data", agentException.getData());
            gen.writeObjectField("errorDetails", agentException.getErrorDetails());
        } else if (value instanceof AgentQueryException agentQueryException) {
            gen.writeObjectField("data", agentQueryException.getData());
            gen.writeObjectField("errorDetails", agentQueryException.getErrorDetails());
        }

        gen.writeEndObject();
    }
}
