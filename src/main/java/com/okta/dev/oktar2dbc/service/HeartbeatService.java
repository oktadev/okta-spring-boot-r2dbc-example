package com.okta.dev.oktar2dbc.service;

import com.okta.dev.oktar2dbc.database.HeartbeatEntity;
import com.okta.dev.oktar2dbc.database.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HeartbeatService {
    private final HeartbeatRepository heartbeatRepository;

    @Autowired
    public HeartbeatService(HeartbeatRepository heartbeatRepository) {
        this.heartbeatRepository = heartbeatRepository;
    }

    @Scheduled(fixedRate = 1000) // 1 second
    public void create() {
        HeartbeatEntity heartbeatEntity = new HeartbeatEntity();
        heartbeatEntity.setTimestamp(System.currentTimeMillis());
        heartbeatEntity.setText(randomString());
        heartbeatEntity.setUsername("system");
        heartbeatRepository.save(heartbeatEntity)
                .log()
                .then()
                .subscribe();
    }

    private static String randomString() {
        int lower = 'A';
        int upper = 'Z';

        return IntStream.range(0, 10)
                .mapToObj(i -> {
                    double range = upper-lower;
                    char charIdx = ((char)(lower + (range * Math.random())));
                    return String.valueOf(charIdx);
                })
                .collect(Collectors.joining());
    }
}
