package org.avensio.common.persistence.model.twitter;

import lombok.Getter;

public class Message {
    @Getter
    private	String	content;
    public	Message(String	content)	{
        this.content = content;
    }
}
