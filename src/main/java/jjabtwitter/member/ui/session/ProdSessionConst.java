package jjabtwitter.member.ui.session;

import org.springframework.stereotype.Component;

@Component
public class ProdSessionConst implements SessionConst {

    public static final String KEY = "loginInfo";
    public static final int VALIDATED_TIME = 30 * Constants.DAY;

    public String getKey() {
        return KEY;
    }

    public int getValidatedTime() {
        return VALIDATED_TIME;
    }

    private static class Constants {
        private static final int DAY = 86400;
    }
}
