package com.bingobucket.client.configuration;

import com.bingobucket.client.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * Traditional HTTTP REST endpoint controller
 * Fetch all the tickets persisted to the database
 * returns, a flux containing ticket data
 */
@Component
@RequiredArgsConstructor
public class TicketController {
    private final WebClient webClient;

    Flux<Ticket> getTickets() {
        return this.webClient
                .get()
                .uri("configuration://localhost:8080/tickets")
                .retrieve()
                .bodyToFlux(Ticket.class);
    }

}
