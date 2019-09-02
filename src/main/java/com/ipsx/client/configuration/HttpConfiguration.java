package com.ipsx.client.configuration;

import com.ipsx.client.entity.Ticket;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Order a package of tickets
 * depends on, the ticket-server is running
 * on error, all technical exceptions result in a flux containing the Error message
 */
@Component
public class HttpConfiguration {

    @Bean
    WebClient webClient(org.springframework.web.reactive.function.client.WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    RouterFunction<ServerResponse> routeTicketResponse(TicketController client) {
        return route()
                .GET("/tickets/values", serverRequest -> {

                    Flux<String> map = client
                            .getTickets()
                            .map(Ticket::formattedTicket)
                            .onErrorResume(throwable -> Flux.just("ERROR - 01, Failed to generate ticket"))
                            .retryBackoff(10, Duration.ofSeconds(1));

                    return ServerResponse.ok().body(map, String.class);
                }).build();
    }
}
