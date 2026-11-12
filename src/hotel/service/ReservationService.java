package hotel.service;

import hotel.model.Reservation;
import hotel.dao.ReservationDao;
import hotel.validation.ReservationValidator;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

/**logica de negocio de las reservas*/
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationValidator reservationValidator;

    /**
     * inicializa el servicio con las dependencis
     * @param reservationDao dap de reservas
     * @param reservationValidator validar reservas
     */
    public ReservationService(
            ReservationDao reservationDao,
            ReservationValidator reservationValidator) {

        this.reservationDao = reservationDao;
        this.reservationValidator = reservationValidator;
    }

    /**
     * calcula el precio total dependiendo las noches de la reserva
     * @param reservation reserva que se calcula
     * @return precio total de la reserva
     */
    public BigDecimal calculateTotalPrice(Reservation reservation) {

        if (reservation == null ||
                reservation.getRoom() == null ||
                reservation.getCheckIn() == null ||
                reservation.getCheckOut() == null) {
            throw new IllegalArgumentException(
                    "Faltan datos para calcular el precio de la reserva"
            );
        }

        long nights = ChronoUnit.DAYS.between(
                reservation.getCheckIn(),
                reservation.getCheckOut()
        );

        if (nights <= 0) {
            throw new IllegalArgumentException(
                    "La fecha de salida debe ser posterior a la de ingreso"
            );
        }

        BigDecimal pricePerNight = reservation.getRoom().getPricePerNight();

        if (pricePerNight == null ||
                pricePerNight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio por noche debe ser mayor a cero"
            );
        }

        return pricePerNight.multiply(BigDecimal.valueOf(nights));
    }


    /**
     * valida y guarda una nueva reserva
     * @param reservation reserva que se quiere guardar
     * @return id de la reserva guardada
     */
    public int saveReservation(Reservation reservation) {

        if (reservation == null) {
            throw new IllegalArgumentException(
                    "La reserva no puede ser nula"
            );
        }

        // calcula el precio total
        BigDecimal totalPrice = calculateTotalPrice(reservation);
        reservation.setTotalPrice(totalPrice);

        // validar los datos de la reserva
        reservationValidator.validate(reservation);

        //comprueba que no existan reservas superpuestas
        boolean isOccupied = reservationDao.hasOverlappingReservation(
                reservation.getRoom().getIdRoom(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                0
        );

        if (isOccupied) {
            throw new IllegalArgumentException(
                    "La habitacion ya tiene una reserva en esas fechas"
            );
        }

        // guardar la reserva en la base de datos
        return reservationDao.save(reservation);
    }

}
