package com.okta.dev.oktar2dbc.database;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, Long> {
    Flux<UserEntity> findByEmail(String email);
}
