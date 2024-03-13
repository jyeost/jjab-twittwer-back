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
    MEMBER_NOT_FOUND(2000, "존재하지 않는 회원입니다."),
    MEMBER_PASSWORD_INVALID(2001, "회원 비밀번호가 잘못되었습니다."),
    MEMBER_NICKNAME_INVALID(2002, "회원 닉네임이 잘못되었습니다."),
    MEMBER_CUSTOM_ID_INVALID(2003, "회원 아이디가 잘못되었습니다."),
    MEMBER_CUSTOM_ID_DUPLICATE(2004, "이미 존재하는 아이디입니다."),
    MEMBER_IS_DELETED(2005, "삭제된 회원입니다."),

    FOLLOW_ALREADY_EXIST(2100, "이미 팔로우 했습니다."),
    FOLLOW_SELF_INVALID(2101, "스스로를 팔로우 할 수 없습니다."),

    PASSWORD_ENCRYPT_FAIL(2500, "비밀번호 암호화에 실패했습니다."),
    LOGIN_FAIL(2501, "로그인 정보가 일치하지 않습니다."),
    AUTHORIZATION_EMPTY(2502, "인가정보가 비었습니다."),

    // 3___: 이미지 관련
    POST_IMAGE_COUNT_INVALID(3500, "게시글의 사진 개수가 맞지 않습니다."),
    IMAGE_EXTENSION_INVALID(3501, "업로드 불가능한 이미지 확장자 입니다."),
    IMAGE_UPLOAD_FAIL(3502, "이미지 업로드에 실패했습니다."),

    // 4___: 게시글 관련
    POST_CONTENT_LENGTH_INVALID(4000, "게시글의 내용의 길이가 맞지 않습니다.");

    private int code;

    private String message;
}
