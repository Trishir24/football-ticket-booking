package com.stadium.booking.controller;

import com.stadium.booking.dto.SeatBookingDtos.BookSeatRequest;
import com.stadium.booking.dto.SeatBookingDtos.SeatStatusResponse;
import com.stadium.booking.dto.SeatBookingDtos.TicketResponse;
import com.stadium.booking.service.SeatBookingService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing stadium seat availability, booking, and customer ticket endpoints.
 */
@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatBookingController {

    private final SeatBookingService seatBookingService;

    public SeatBookingController(SeatBookingService seatBookingService) {
        this.seatBookingService = seatBookingService;
    }

    @GetMapping
    public ResponseEntity<List<SeatStatusResponse>> getSeats(
            @RequestParam(required = false) String block
    ) {
        return ResponseEntity.ok(seatBookingService.getSeats(block));
    }

    @PostMapping("/book")
    public ResponseEntity<TicketResponse> bookSeat(@Valid @RequestBody BookSeatRequest request) {
        TicketResponse ticket = seatBookingService.bookSeat(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponse>> getTicketsByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(seatBookingService.getTicketsByEmail(email));
    }
}
