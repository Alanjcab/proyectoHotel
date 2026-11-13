package hotel.service;

import hotel.model.Reservation;
import hotel.dao.ReservationDao;
import hotel.validation.ReservationValidator;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import hotel.dao.RoomSqlDao;
import hotel.model.Room;

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

        if (!"Pendiente".equalsIgnoreCase(reservation.getStatus()) && !"Confirmada".equalsIgnoreCase(reservation.getStatus())) {
            throw new IllegalArgumentException(
                    "Una reserva nueva debe estar pendiente o confirmada"
            );
        }

        // calcula el precio total
        BigDecimal totalPrice = calculateTotalPrice(reservation);
        reservation.setTotalPrice(totalPrice);

        // validar los datos de la reserva
        reservationValidator.validate(reservation);

        RoomSqlDao roomDao = new RoomSqlDao();

        Room room = roomDao.findById(
                reservation.getRoom().getIdRoom()
        );

        if (room == null) {
            throw new IllegalArgumentException(
                    "No se encontro la habitacion."
            );
        }

        if (!"Disponible".equalsIgnoreCase(room.getStatus())) {
            throw new IllegalArgumentException(
                    "La habitacion no esta disponible para reservar."
            );
        }

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


    /**
     * valida y modifica una reserva existente
     * @param reservation reserva que se wuiere modificar
     */
    public void updateReservation(Reservation reservation) {

        if (reservation == null || reservation.getIdReservation() <= 0) {
            throw new IllegalArgumentException(
                    "selleciona una reserva valida"
            );
        }
        Reservation currentReservation = reservationDao.findById(
                reservation.getIdReservation()
        );

        if (currentReservation == null) {
            throw new IllegalArgumentException("No se encontro la reserva");
        }

        if ("Cancelada".equalsIgnoreCase(currentReservation.getStatus())) {
            throw new IllegalArgumentException("No se puede modificar una reserva cancelada");
        }

        if ("Finalizada".equalsIgnoreCase(currentReservation.getStatus())) {
            throw new IllegalArgumentException("No se puede modificar una reserva finalizada");
        }

        //calcular nuevamente el precio total
        BigDecimal totalPrice = calculateTotalPrice(reservation);
        reservation.setTotalPrice(totalPrice);

        //validar los datos
        reservationValidator.validate(reservation);

        //comprobar el estado actual de la habitacion
        RoomSqlDao roomDao = new RoomSqlDao();

        Room room = roomDao.findById(
                reservation.getRoom().getIdRoom()
        );

        if (room == null) {
            throw new IllegalArgumentException(
                    "No se encontro la habitacion"
            );
        }

        if (!"Disponible".equalsIgnoreCase(room.getStatus())) {
            throw new IllegalArgumentException(
                    "La habitacion no esta disponible para reservar"
            );
        }

        // comprobar que no existan reservas superpuestas
        boolean isOccupied = reservationDao.hasOverlappingReservation(
                reservation.getRoom().getIdRoom(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getIdReservation()
        );

        if (isOccupied) {
            throw new IllegalArgumentException("La habitacion ya tiene una reserva en esas fechas");
        }

        if ("Finalizada".equalsIgnoreCase(reservation.getStatus())
                && reservation.getCheckOut().isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "No se puede finalizar una reserva antes de su fecha de salida."
            );
        }
        reservationDao.update(reservation);
    }


    /**
     * cancela una reserva existente sin eliminarla de la db
     * @param reservationId identificador de la reserva
     */
    public void cancelReservation(int reservationId) {

        Reservation reservation = reservationDao.findById(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException("No se encontro la reserva");
        }

        if ("Cancelada".equalsIgnoreCase(reservation.getStatus())) {
            throw new IllegalArgumentException("La reserva se encuentra cancelada");
        }

        if ("Finalizada".equalsIgnoreCase(reservation.getStatus())) {
            throw new IllegalArgumentException("No se puede cancelar una reserva finalizada");
        }

        reservation.setStatus("Cancelada");

        reservationDao.update(reservation);
    }

}
