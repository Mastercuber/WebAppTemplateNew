package org.avensio.common.persistence.model.security.twitter;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

public class TwitterUser implements Serializable {
    private static final long serialVersionUID = 1482472917L;

    private int uid;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private ArrayList<String> follower;
    @Getter
    @Setter
    private ArrayList<String> following;
    @Getter
    @Setter
    private ArrayList<String> piepIds;
}
