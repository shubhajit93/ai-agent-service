package org.hishab.agent.core.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hishab.agent.core.messages.Message;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class BaseCommand extends Message {
    public BaseCommand(String id) {
        super(id);
    }
}
