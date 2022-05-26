package com.epam.rd.stock.exchange.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertViewDto {

    private String id;

    private String message;

    private LocalDateTime dateTime;

}
