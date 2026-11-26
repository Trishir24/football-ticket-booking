package com.stadium.booking.repository;

import com.stadium.booking.entity.SeatBooking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link SeatBooking} records.
 */
@Repository
public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {

    boolean existsByBlockAndRowNumberAndSeatNumber(String block, Integer rowNumber, Integer seatNumber);

    Optional<SeatBooking> findByBlockAndRowNumberAndSeatNumber(String block, Integer rowNumber, Integer seatNumber);

    List<SeatBooking> findByBlockIgnoreCase(String block);

    List<SeatBooking> findByCustomerEmailIgnoreCaseOrderByCreatedAtDesc(String customerEmail);

    Optional<SeatBooking> findByBookingCode(String bookingCode);
}
