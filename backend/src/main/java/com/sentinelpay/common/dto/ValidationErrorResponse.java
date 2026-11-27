package com.sentinelpay.common.dto;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponse (
    String errorCode,
    String message,
    Map<String, String> fieldErrors,
    String path,
    Instant timestamp
){
}
