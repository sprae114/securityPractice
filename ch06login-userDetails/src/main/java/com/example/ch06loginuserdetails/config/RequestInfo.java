package com.example.ch06loginuserdetails.config;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInfo {

    private LocalDateTime loginTime;
    private String remoteIp;
    private String sessionId;
}

