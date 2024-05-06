package kcs.funding.fundingboost.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Method Not Allowed Error
    METHOD_NOT_ALLOWED(40500, HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),

    // Not Found Error
    NOT_FOUND_END_POINT(40400, HttpStatus.NOT_FOUND, "존재하지 않는 API 엔드포인트입니다."),
    NOT_FOUND_RESOURCE(40400, HttpStatus.NOT_FOUND, "해당 리소스가 존재하지 않습니다."),
    NOT_FOUND_LOGIN_USER(40401, HttpStatus.NOT_FOUND, "로그인한 사용자가 존재하지 않습니다."),
    NOT_FOUND_AUTHORIZATION_HEADER(40401, HttpStatus.NOT_FOUND, "Authorization 헤더가 존재하지 않습니다."),
    NOT_FOUND_MEMBER(40402, HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),
    NOT_FOUND_SHARED_URL(40403, HttpStatus.NOT_FOUND, "해당 공유 URL이 존재하지 않습니다."),
    NOT_FOUND_FUNDING(40404, HttpStatus.NOT_FOUND, "펀딩이 존재하지 않습니다."),
    NOT_FOUND_ITEM(40405, HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."),
    NOT_FOUND_FUNDING_ITEM(40406, HttpStatus.NOT_FOUND, "펀딩 상품이 존재하지 않습니다."),
    NOT_FOUND_GIFTHUB_ITEM(40407, HttpStatus.NOT_FOUND, "장바구니에 아이템이 존재하지 않습니다."),
    NOT_FOUND_DELIVERY(40408, HttpStatus.NOT_FOUND, "배송지 목록에 존재하지 않습니다."),
    NOT_FOUND_CONTRIBUTOR(40409, HttpStatus.NOT_FOUND, "펀딩한 이력이 존재하지 않습니다."),


    // Invalid Argument Error
    MISSING_REQUEST_PARAMETER(40000, HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    INVALID_ARGUMENT(40001, HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 인자입니다."),
    INVALID_PARAMETER_FORMAT(40002, HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 인자 형식입니다."),
    INVALID_HEADER_ERROR(40003, HttpStatus.BAD_REQUEST, "유효하지 않은 헤더입니다."),
    MISSING_REQUEST_HEADER(40004, HttpStatus.BAD_REQUEST, "필수 요청 헤더가 누락되었습니다."),
    BAD_REQUEST_PARAMETER(40005, HttpStatus.BAD_REQUEST, "잘못된 요청 파라미터입니다."),
    BAD_REQUEST_JSON(40006, HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다."),
    SEARCH_SHORT_LENGTH_ERROR(40007, HttpStatus.BAD_REQUEST, "검색어는 2글자 이상이어야 합니다."),
    INVALID_FUNDING_STATUS(40009, HttpStatus.BAD_REQUEST, "펀딩이 정상적이지 않습니다."),
    INVALID_POINT_LACK(40010, HttpStatus.BAD_REQUEST, "point가 부족합니다."),
    INVALID_FUNDING_MONEY(40011, HttpStatus.BAD_REQUEST, "펀딩 금액 이상을 후원할 수 없습니다"),
    INVALID_FUNDING_OR_PRICE(40012, HttpStatus.BAD_REQUEST, "펀딩에 담긴 상품이 없거나, 상품의 가격이 이상합니다."),
    INVALID_FUNDINGITEM_STATUS(40013, HttpStatus.BAD_REQUEST, "펀딩아이템이 정상적이지 않습니다."),
    ONGOING_FUNDING_ERROR(40014, HttpStatus.BAD_REQUEST, "현재 진행중인 펀딩입니다."),
    INVALID_ACCESS_URL(40015, HttpStatus.BAD_REQUEST, "잘못된 사용자 접근입니다."),
    INVALID_ITEM_QUANTITY(40016, HttpStatus.BAD_REQUEST, "최소 1개 이상의 수량을 선택해야 합니다."),

    // Gone Error
    GONE_SHARED_URL(41001, HttpStatus.GONE, "해당 공유 URL이 만료되었습니다."),

    // Access Denied Error
    ACCESS_DENIED(40300, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_MATCH_AUTH_CODE(40301, HttpStatus.FORBIDDEN, "인증 코드가 일치하지 않습니다."),
    NOT_MATCH_USER(40302, HttpStatus.FORBIDDEN, "해당 사용자가 일치하지 않습니다."),


    // Unauthorized Error
    FAILURE_LOGIN(40100, HttpStatus.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다."),
    EXPIRED_TOKEN_ERROR(40101, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN_ERROR(40102, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_MALFORMED_ERROR(40103, HttpStatus.UNAUTHORIZED, "토큰이 올바르지 않습니다."),
    TOKEN_TYPE_ERROR(40104, HttpStatus.UNAUTHORIZED, "토큰 타입이 일치하지 않거나 비어있습니다."),
    TOKEN_UNSUPPORTED_ERROR(40105, HttpStatus.UNAUTHORIZED, "지원하지않는 토큰입니다."),
    TOKEN_GENERATION_ERROR(40106, HttpStatus.UNAUTHORIZED, "토큰 생성에 실패하였습니다."),
    TOKEN_UNKNOWN_ERROR(40107, HttpStatus.UNAUTHORIZED, "알 수 없는 토큰입니다."),

    // Internal Server Error
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러입니다."),
    UPLOAD_FILE_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패하였습니다."),
    INTERNAL_SAVE_ERROR(50003, HttpStatus.INTERNAL_SERVER_ERROR, "기프트에 아이템 담기 실패"),

    // Funding Error
    ALREADY_EXIST_FUNDING(50002, HttpStatus.INTERNAL_SERVER_ERROR, "펀딩이 이미 존재합니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}

