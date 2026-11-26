package com.stadium.booking.service;

import com.stadium.booking.dto.SeatBookingDtos.BookSeatRequest;
import com.stadium.booking.dto.SeatBookingDtos.SeatStatusResponse;
import com.stadium.booking.dto.SeatBookingDtos.TicketResponse;
import com.stadium.booking.entity.SeatBooking;
import com.stadium.booking.entity.SeatBooking.BookingStatus;
import com.stadium.booking.exception.SeatAlreadyBookedException;
import com.stadium.booking.repository.SeatBookingRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for querying stadium seat availability, booking seats, and retrieving customer tickets.
 */
@Service
public class SeatBookingService {

    private final SeatBookingRepository seatBookingRepository;

    public SeatBookingService(SeatBookingRepository seatBookingRepository) {
        this.seatBookingRepository = seatBookingRepository;
    }

    /**
     * Returns booked seat statuses across the stadium, optionally filtered by block.
     */
    @Transactional(readOnly = true)
    public List<SeatStatusResponse> getSeats(String block) {
        List<SeatBooking> bookings = (block != null && !block.isBlank())
                ? seatBookingRepository.findByBlockIgnoreCase(block.trim())
                : seatBookingRepository.findAll();

        return bookings.stream()
                .map(SeatStatusResponse::fromEntity)
                .toList();
    }

    /**
     * Books a single seat if it is not already taken, enforcing uniqueness at both service and DB layers.
     */
    @Transactional
    public TicketResponse bookSeat(BookSeatRequest request) {
        String normalizedBlock = request.block().trim().toUpperCase();
        String normalizedName = request.customerName().trim();
        String normalizedEmail = request.customerEmail().trim().toLowerCase();

        if (seatBookingRepository.existsByBlockAndRowNumberAndSeatNumber(
                normalizedBlock, request.rowNumber(), request.seatNumber())) {
            throw new SeatAlreadyBookedException(normalizedBlock, request.rowNumber(), request.seatNumber());
        }

        SeatBooking booking = new SeatBooking(
                generateBookingCode(),
                normalizedBlock,
                request.rowNumber(),
                request.seatNumber(),
                request.price(),
                normalizedName,
                normalizedEmail,
                BookingStatus.BOOKED
        );

        try {
            SeatBooking saved = seatBookingRepository.saveAndFlush(booking);
            return TicketResponse.fromEntity(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new SeatAlreadyBookedException(normalizedBlock, request.rowNumber(), request.seatNumber());
        }
    }

    /**
     * Retrieves all tickets booked for the given customer email, ordered newest first.
     */
    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email query parameter is required");
        }
        return seatBookingRepository.findByCustomerEmailIgnoreCaseOrderByCreatedAtDesc(email.trim())
                .stream()
                .map(TicketResponse::fromEntity)
                .toList();
    }

    private String generateBookingCode() {
        return "STAD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
