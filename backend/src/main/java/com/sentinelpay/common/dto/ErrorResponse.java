package com.sentinelpay.common.dto;

import java.time.Instant;

public record ErrorResponse(
    String errorCode,
    String message,
    String path,
    Instant timeStamp
) {

}
