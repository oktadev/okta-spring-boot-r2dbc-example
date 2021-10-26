package com.okta.dev.oktar2dbc.database;

import lombok.Data;
import org.springframework.data.annotation.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
public class HeartbeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long timestamp;
    private String username;
    private String text;
}
