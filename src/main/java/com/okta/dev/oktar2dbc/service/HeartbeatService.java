package com.okta.dev.oktar2dbc.service;

import com.okta.dev.oktar2dbc.database.HeartbeatEntity;
import com.okta.dev.oktar2dbc.database.HeartbeatRepository;
import io.r2dbc.spi.Row;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.CoreSubscriber;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class HeartbeatService {

    private final HeartbeatRepository heartbeatRepository;
    private final DatabaseClient databaseClient;
//    private final Flux<HeartbeatEntity> heartbeatEntityFlux;

    @Autowired
    public HeartbeatService(HeartbeatRepository heartbeatRepository, DatabaseClient databaseClient) {
        this.heartbeatRepository = heartbeatRepository;
//        this.heartbeatEntityFlux = heartbeatRepository.findAll();
        this.databaseClient = databaseClient;
    }

    public Mono<ServerResponse> getAllHeartbeats(ServerRequest serverRequest) {
//        Flux<HeartbeatEntity> flux = databaseClient
//                .sql("SELECT * FROM HEARTBEAT_ENTITY")
//                .map(new Function<Row, HeartbeatEntity>() {
//                    @Override
//                    public HeartbeatEntity apply(Row row) {
//                        System.err.println(row);
//                        return new HeartbeatEntity();
//                    }
//                })
//                .all();

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(heartbeatRepository.findAll().limitRate(1), HeartbeatEntity.class);
    }
}
