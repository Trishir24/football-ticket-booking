package com.stadium.booking.dto;

import com.stadium.booking.entity.SeatBooking;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request payload for booking a stadium seat.
 */
public class SeatBookingDtos {

    private SeatBookingDtos() {
    }

    public record BookSeatRequest(
            @NotBlank(message = "Block is required")
            @Size(max = 50, message = "Block must not exceed 50 characters")
            String block,

            @NotNull(message = "Row number is required")
            @Min(value = 1, message = "Row number must be at least 1")
            Integer rowNumber,

            @NotNull(message = "Seat number is required")
            @Min(value = 1, message = "Seat number must be at least 1")
            Integer seatNumber,

            @NotNull(message = "Price is required")
            @DecimalMin(value = "0.0", inclusive = true, message = "Price must be zero or greater")
            BigDecimal price,

            @NotBlank(message = "Customer name is required")
            @Size(max = 100, message = "Customer name must not exceed 100 characters")
            String customerName,

            @NotBlank(message = "Customer email is required")
            @Email(message = "Customer email must be valid")
            @Size(max = 150, message = "Customer email must not exceed 150 characters")
            String customerEmail
    ) {
    }

    /**
     * Public seat availability view returned by GET /api/seats (excludes customer PII).
     */
    public record SeatStatusResponse(
            Long id,
            String block,
            Integer rowNumber,
            Integer seatNumber,
            BigDecimal price,
            SeatBooking.BookingStatus status
    ) {
        public static SeatStatusResponse fromEntity(SeatBooking booking) {
            return new SeatStatusResponse(
                    booking.getId(),
                    booking.getBlock(),
                    booking.getRowNumber(),
                    booking.getSeatNumber(),
                    booking.getPrice(),
                    booking.getStatus()
            );
        }
    }

    /**
     * Full ticket receipt returned after booking or when querying tickets by customer email.
     */
    public record TicketResponse(
            Long id,
            String bookingCode,
            String block,
            Integer rowNumber,
            Integer seatNumber,
            BigDecimal price,
            String customerName,
            String customerEmail,
            SeatBooking.BookingStatus status,
            LocalDateTime createdAt
    ) {
        public static TicketResponse fromEntity(SeatBooking booking) {
            return new TicketResponse(
                    booking.getId(),
                    booking.getBookingCode(),
                    booking.getBlock(),
                    booking.getRowNumber(),
                    booking.getSeatNumber(),
                    booking.getPrice(),
                    booking.getCustomerName(),
                    booking.getCustomerEmail(),
                    booking.getStatus(),
                    booking.getCreatedAt()
            );
        }
    }
}
