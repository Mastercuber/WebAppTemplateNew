package org.avensio.common.persistence.model.twitter;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Post implements Serializable {
    private static final long serialVersionUID = 1482975917L;

    @Setter
    @Getter
    private String uuid;
    @Setter
    @Getter
    private long date;
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String postText;

    public String getDateText() {
        return new SimpleDateFormat("dd.M.yyyy HH:mm:ss").format(date);
    }
}
