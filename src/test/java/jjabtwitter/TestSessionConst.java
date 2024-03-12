package jjabtwitter;

import jjabtwitter.member.ui.session.SessionConst;

public class TestSessionConst implements SessionConst {

    public static final String KEY = "testLoginInfo";
    public static final int VALIDATED_TIME = 30;

    public String getKey() {
        return KEY;
    }

    public int getValidatedTime() {
        return VALIDATED_TIME;
    }
}
