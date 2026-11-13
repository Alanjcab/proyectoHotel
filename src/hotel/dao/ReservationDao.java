package hotel.dao;

import hotel.model.Reservation;
import java.time.LocalDate;

/**
 * define las operaciones de persistencia para las reservas
 * hreda las operaciones crud y agrega la consulta para saber si ahy reservas superpuestas.
 */
public interface ReservationDao extends CrudDAO<Reservation, Integer> {

    /**
     * comprueba si una habitacion tiene reservas activas
     * que se superponen con las fechas indicadas
     * @param roomId identificador de la habitacion
     * @param checkIn fecha de ingreso
     * @param checkOut fecha de salida
     * @param excludedReservationId identificador de la reserva que tiene que excluirse de la consulta
     * @return true si existe una reserva superpuesta y false en caso contrario
     */
    boolean hasOverlappingReservation(
            int roomId,
            LocalDate checkIn,
            LocalDate checkOut,
            int excludedReservationId
    );
}
