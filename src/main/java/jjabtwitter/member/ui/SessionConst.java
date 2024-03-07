package jjabtwitter.member.ui;

public enum SessionConst {

    SESSION("loginInfo", 30 * Constants.DAY);

    private final String key;
    private final int validatedTime;

    SessionConst(final String key, final int validatedTime) {
        this.key = key;
        this.validatedTime = validatedTime;
    }

    public String getKey() {
        return key;
    }

    public int getValidatedTime() {
        return validatedTime;
    }

    private static class Constants {

        private static final int DAY = 86400;
    }
}
