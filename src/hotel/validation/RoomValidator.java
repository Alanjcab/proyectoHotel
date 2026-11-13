package hotel.validation;

import hotel.model.Room;

import java.math.BigDecimal;

/**
 * valida los datos de una habitacion antes de guardarla o modificarla en el sistema
 */
public class RoomValidator {
    /**
     * comprueba que los datos de una habitacion sean validos
     * @param room habitacion que se desea validar
     * @throws IllegalArgumentException si algun dato es invalido
     */
    public void validate(Room room) {

        if (room == null) {
            throw new IllegalArgumentException("La habitacion no puede ser nula");
        }

        if (room.getNumber() <= 0) {
            throw new IllegalArgumentException("El numero de habitacion debe ser mayor a cero");
        }

        if (room.getType() == null || room.getType().isBlank()) {
            throw new IllegalArgumentException("debe seleccionar tipo de habitacion");
        }

        if (room.getType().length() > 50) {
            throw new IllegalArgumentException("El tipo no puede superar los 50 caracteres");
        }

        if (room.getCapacity() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero");
        }

        if (room.getPricePerNight() == null ||
                room.getPricePerNight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio por noche debe ser mayor a cero");
        }
        /*devuelv la cantidad de posiciones decimales que tiene un bigdecimal*/
        if (room.getPricePerNight().scale() > 2 ||
                room.getPricePerNight().compareTo(new BigDecimal("99999999.99")) > 0) {
            throw new IllegalArgumentException("El precio debe tener como maximo 2 decimales y respetar el límite permitido");
        }

        if (room.getStatus() == null || room.getStatus().isBlank()) {
            throw new IllegalArgumentException("El estado de la habitacion es obligatorio");
        }

        if (room.getStatus().length() > 30) {
            throw new IllegalArgumentException("El estado no puede superar los 30 caracteres");
        }


        if (!room.getStatus().equals("Disponible") && !room.getStatus().equals("Mantenimiento")) {
            throw new IllegalArgumentException(
                    "El estado de la habitacion no es valido"
            );
        }

    }
}
