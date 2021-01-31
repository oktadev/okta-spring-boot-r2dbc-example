package com.okta.dev.oktar2dbc.database;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface HeartbeatRepository extends R2dbcRepository<HeartbeatEntity, Long> {
    Flux<HeartbeatEntity> findByUsername(String username);
}
