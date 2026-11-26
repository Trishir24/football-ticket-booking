package com.stadium.booking.exception;

/**
 * Thrown when attempting to book a stadium seat that is already reserved or booked.
 */
public class SeatAlreadyBookedException extends RuntimeException {

    public SeatAlreadyBookedException(String block, Integer rowNumber, Integer seatNumber) {
        super(String.format("Seat %s Row %d Seat %d is already booked", block, rowNumber, seatNumber));
    }
}
