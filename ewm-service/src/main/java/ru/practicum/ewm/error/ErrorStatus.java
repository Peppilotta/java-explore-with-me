package ru.practicum.ewm.error;

public enum ErrorStatus {

    E_100_CONTINUE("100 CONTINUE"),
    E_101_SWITCHING_PROTOCOLS("101 SWITCHING_PROTOCOLS"),
    E_102_PROCESSING("102 PROCESSING"),
    E_103_CHECKPOINT("103 CHECKPOINT"),
    E_200_OK("200 OK"),
    E_201_CREATED("201 CREATED"),
    E_202_ACCEPTED("202 ACCEPTED"),
    E_203_NON_AUTHORITATIVE_INFORMATION("203 NON_AUTHORITATIVE_INFORMATION"),
    E_204_NO_CONTENT("204 NO_CONTENT"),
    E_205_RESET_CONTENT("205 RESET_CONTENT"),
    E_206_PARTIAL_CONTENT("206 PARTIAL_CONTENT"),
    E_207_MULTI_STATUS("207 MULTI_STATUS"),
    E_208_ALREADY_REPORTED("208 ALREADY_REPORTED"),
    E_226_IM_USED("226 IM_USED"),
    E_300_MULTIPLE_CHOICES("300 MULTIPLE_CHOICES"),
    E_301_MOVED_PERMANENTLY("301 MOVED_PERMANENTLY"),
    E_302_FOUND("302 FOUND"),
    E_302_MOVED_TEMPORARILY("302 MOVED_TEMPORARILY"),
    E_303_SEE_OTHER("303 SEE_OTHER"),
    E_304_NOT_MODIFIED("304 NOT_MODIFIED"),
    E_305_USE_PROXY("305 USE_PROXY"),
    E_307_TEMPORARY_REDIRECT("307 TEMPORARY_REDIRECT"),
    E_308_PERMANENT_REDIRECT("308 PERMANENT_REDIRECT"),
    E_400_BAD_REQUEST("400 BAD_REQUEST"),
    E_401_UNAUTHORIZED("401 UNAUTHORIZED"),
    E_402_PAYMENT_REQUIRED("402 PAYMENT_REQUIRED"),
    E_403_FORBIDDEN("403 FORBIDDEN"),
    E_404_NOT_FOUND("404 NOT_FOUND"),
    E_405_METHOD_NOT_ALLOWED("405 METHOD_NOT_ALLOWED"),
    E_406_NOT_ACCEPTABLE("406 NOT_ACCEPTABLE"),
    E_407_PROXY_AUTHENTICATION_REQUIRED("407 PROXY_AUTHENTICATION_REQUIRED"),
    E_408_REQUEST_TIMEOUT("408 REQUEST_TIMEOUT"),
    E_409_CONFLICT("409 CONFLICT"),
    E_410_GONE("410 GONE"),
    E_411_LENGTH_REQUIRED("411 LENGTH_REQUIRED"),
    E_412_PRECONDITION_FAILED("412 PRECONDITION_FAILED"),
    E_413_PAYLOAD_TOO_LARGE("413 PAYLOAD_TOO_LARGE"),
    E_413_REQUEST_ENTITY_TOO_LARGE("413 REQUEST_ENTITY_TOO_LARGE"),
    E_414_URI_TOO_LONG("414 URI_TOO_LONG"),
    E_414_REQUEST_URI_TOO_LONG("414 REQUEST_URI_TOO_LONG"),
    E_415_UNSUPPORTED_MEDIA_TYPE("415 UNSUPPORTED_MEDIA_TYPE"),
    E_416_REQUESTED_RANGE_NOT_SATISFIABLE("416 REQUESTED_RANGE_NOT_SATISFIABLE"),
    E_417_EXPECTATION_FAILED("417 EXPECTATION_FAILED"),
    E_418_I_AM_A_TEAPOT("418 I_AM_A_TEAPOT"),
    E_419_INSUFFICIENT_SPACE_ON_RESOURCE("419 INSUFFICIENT_SPACE_ON_RESOURCE"),
    E_420_METHOD_FAILURE("420 METHOD_FAILURE"),
    E_421_DESTINATION_LOCKED("421 DESTINATION_LOCKED"),
    E_422_UNPROCESSABLE_ENTITY("422 UNPROCESSABLE_ENTITY"),
    E_423_LOCKED("423 LOCKED"),
    E_424_FAILED_DEPENDENCY("424 FAILED_DEPENDENCY"),
    E_425_TOO_EARLY("425 TOO_EARLY"),
    E_426_UPGRADE_REQUIRED("426 UPGRADE_REQUIRED"),
    E_428_PRECONDITION_REQUIRED("428 PRECONDITION_REQUIRED"),
    E_429_TOO_MANY_REQUESTS("429 TOO_MANY_REQUESTS"),
    E_431_REQUEST_HEADER_FIELDS_TOO_LARGE("431 REQUEST_HEADER_FIELDS_TOO_LARGE"),
    E_451_UNAVAILABLE_FOR_LEGAL_REASONS("451 UNAVAILABLE_FOR_LEGAL_REASONS"),
    E_500_INTERNAL_SERVER_ERROR("500 INTERNAL_SERVER_ERROR"),
    E_501_NOT_IMPLEMENTED("501 NOT_IMPLEMENTED"),
    E_502_BAD_GATEWAY("502 BAD_GATEWAY"),
    E_503_SERVICE_UNAVAILABLE("503 SERVICE_UNAVAILABLE"),
    E_504_GATEWAY_TIMEOUT("504 GATEWAY_TIMEOUT"),
    E_505_HTTP_VERSION_NOT_SUPPORTED("505 HTTP_VERSION_NOT_SUPPORTED"),
    E_506_VARIANT_ALSO_NEGOTIATES("506 VARIANT_ALSO_NEGOTIATES"),
    E_507_INSUFFICIENT_STORAGE("507 INSUFFICIENT_STORAGE"),
    E_508_LOOP_DETECTED("508 LOOP_DETECTED"),
    E_509_BANDWIDTH_LIMIT_EXCEEDED("509 BANDWIDTH_LIMIT_EXCEEDED"),
    E_510_NOT_EXTENDED("510 NOT_EXTENDED"),
    E_511_NETWORK_AUTHENTICATION_REQUIRED("511 NETWORK_AUTHENTICATION_REQUIRED");

    private final String value;

    ErrorStatus(String value) {
        this.value = value;
    }

    public static ErrorStatus fromValue(String input) {
        for (ErrorStatus b : ErrorStatus.values()) {
            if (b.value.equals(input)) {
                return b;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}