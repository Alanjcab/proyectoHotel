package hotel.validation;

import hotel.model.Reservation;
import java.math.BigDecimal;

/**
 * valida los datos de la reserva
 */
public class ReservationValidator {

    /**
     * comprueba que los datos de la reserva sean validos
     * @param reservation reserva que se desea validar
     * @throws IllegalArgumentException si algun dato es invalido
     */
    public void validate(Reservation reservation) {

        if (reservation == null) {
            throw new IllegalArgumentException(
                    "La reserva no puede ser nula"
            );
        }

        if (reservation.getClient() == null || reservation.getClient().getIdClient() <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un cliente valido"
            );
        }

        if (reservation.getRoom() == null || reservation.getRoom().getIdRoom() <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una habitacion valida"
            );
        }

        if (reservation.getCheckIn() == null || reservation.getCheckOut() == null) {
            throw new IllegalArgumentException(
                    "Las fechas de ingreso y salida son obligatorias"
            );
        }

        if (!reservation.getCheckOut().isAfter(reservation.getCheckIn())) {
            throw new IllegalArgumentException(
                    "La fecha de salida debe ser posterior a la de ingreso"
            );
        }

        if (reservation.getStatus() == null || reservation.getStatus().isBlank()) {
            throw new IllegalArgumentException(
                    "El estado de la reserva es obligatorio"
            );
        }

        if (reservation.getStatus().length() > 30) {
            throw new IllegalArgumentException(
                    "El estado no puede superar los 30 caracteres"
            );
        }
        if (!reservation.getStatus().equals("Pendiente") && !reservation.getStatus().equals("Confirmada") && !reservation.getStatus().equals("Cancelada") &&
                !reservation.getStatus().equals("Finalizada")) {
            throw new IllegalArgumentException(
                    "El estado de la reserva no es valido"
            );
        }

        if (reservation.getTotalPrice() == null ||
                reservation.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio total debe ser mayor a cero"
            );
        }

        if (reservation.getTotalPrice().scale() > 2 || reservation.getTotalPrice().compareTo(
                new BigDecimal("99999999.99")) > 0) {
            throw new IllegalArgumentException(
                    "El precio total no puede superar el limite permitido ni tener mas de 2 decimales"
            );
        }
    }
}
