package jjabtwitter.member.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoginInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Long> ids;
    private int representativeIndex;

    public LoginInfo(final Long customId) {
        this.ids = new ArrayList<>();
        ids.add(customId);
        representativeIndex = 0;
    }

    public Long getRepresentativeId() {
        return ids.get(representativeIndex);
    }
}
