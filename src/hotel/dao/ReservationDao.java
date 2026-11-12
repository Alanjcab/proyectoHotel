package hotel.dao;

import hotel.model.Reservation;
import java.time.LocalDate;

public interface ReservationDao extends CrudDAO<Reservation, Integer> {

    boolean hasOverlappingReservation(
            int roomId,
            LocalDate checkIn,
            LocalDate checkOut,
            int excludedReservationId
    );
}
