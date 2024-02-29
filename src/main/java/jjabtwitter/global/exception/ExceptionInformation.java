package jjabtwitter.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionInformation {

    // 0번: 서버 내부 예상치 못한 오류
    // ___: 요청 오류

    // 클래스이름_필드명_틀린내용
    // 2___: 회원 관련
    MEMBER_ID_NOT_FOUND(2000, "존재하지 않는 회원입니다."),
    MEMBER_PASSWORD_INVALID(2001, "회원 비밀번호가 잘못되었습니다."),
    MEMBER_NICKNAME_INVALID(2002, "회원 닉네임이 잘못되었습니다."),
    MEMBER_CUSTOM_ID_DUPLICATE(2003, "이미 존재하는 아이디입니다."),
    MEMBER_IS_DELETED(2004, "삭제된 회원입니다."),

    PASSWORD_ENCRYPT_FAIL(2500, "비밀번호 암호화에 실패했습니다.");

    private int code;

    private String message;
}
