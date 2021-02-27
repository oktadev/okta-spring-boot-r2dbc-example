package com.okta.dev.oktar2dbc;

import com.okta.dev.oktar2dbc.database.UserEntity;
import com.okta.dev.oktar2dbc.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class ApplicationRouter {

    @Value("classpath:pages/index.html")
    private Resource indexHtml;

    @Value("classpath:pages/protected.html")
    private Resource protectedHtml;

    private final UserRepository userRepository;

    @Autowired
    public ApplicationRouter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    @DependsOn("connectionFactoryInitializer")
    public RouterFunction<ServerResponse> route() {
        this.userRepository.findAll().subscribe(userEntity -> System.err.println("DING"));

        UserEntity e1 = new UserEntity();
        e1.setEmail("hurrr");

        return RouterFunctions
                .route(RequestPredicates.GET("/index"), request -> pageResponse(indexHtml))
                .andRoute(RequestPredicates.GET("/"), request -> pageResponse(indexHtml))
                .andRoute(RequestPredicates.GET("/protected"), request -> pageResponse(protectedHtml))
                .andRoute(RequestPredicates.GET("/userList"), request -> {
                    List<UserEntity> users = userRepository.findAll().collectList().block();

                    return ServerResponse
                            .ok()
                            .contentType(MediaType.TEXT_EVENT_STREAM)
                            .body(Flux.fromStream(Stream.of(e1)), UserEntity.class);
                });
    }

    private static Mono<ServerResponse> pageResponse(Resource page) {
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .body(DataBufferUtils.read(page, new DefaultDataBufferFactory(), 4096), DataBuffer.class);
    }
}
