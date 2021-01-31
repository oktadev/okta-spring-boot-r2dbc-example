package com.okta.dev.oktar2dbc;

import com.okta.dev.oktar2dbc.database.HeartbeatEntity;
import com.okta.dev.oktar2dbc.database.HeartbeatRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HeartbeatGenerator {

    private final HeartbeatRepository heartbeatRepository;

    @Autowired
    public HeartbeatGenerator(HeartbeatRepository heartbeatRepository) {
        this.heartbeatRepository = heartbeatRepository;
    }

    @Scheduled(fixedRate = 1_000)
    public void generate() {
        HeartbeatEntity heartbeatEntity = new HeartbeatEntity();
        heartbeatEntity.setTimestamp(System.currentTimeMillis());
        heartbeatEntity.setText(randomString());
        heartbeatEntity.setUsername("system");
        heartbeatRepository.save(heartbeatEntity).block();
    }

    private static String randomString() {
        int lower = 'a';
        int upper = 'Z';

        return IntStream.range(0, 10)
                .mapToObj(i -> {
                    double range = upper-lower;
                    char charIdx = ((char)(range * Math.random()));
                    return new String(new char[]{charIdx});
                })
                .collect(Collectors.joining());
    }
}
