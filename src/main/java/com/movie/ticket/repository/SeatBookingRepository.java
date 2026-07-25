package com.movie.ticket.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.movie.ticket.entity.SeatBooking;

@Repository
public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {

    @Query("SELECT sb.seatNumber FROM SeatBooking sb WHERE sb.show.showId = :showId")
    List<String> findBookedSeatsByShowId(@Param("showId") Long showId);

    List<SeatBooking> findByShowShowId(Long showId);
}
