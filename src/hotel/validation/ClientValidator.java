package hotel.validation;

import hotel.model.Client;

/**
 * valida los datos de un cliente antes de guardarlo o modificarlo en el sistema
 */
public class ClientValidator {
    /**
     * comprueba que los datos del cliente sean validos
     * @param client cliente que se desea validar
     * @throws IllegalArgumentException si algun dato es invalido
     */
    public void validate(Client client) {
        if (client == null) {
            throw new IllegalArgumentException(
                    "El cliente no puede ser nulo"
            );
        }
        if (client.getName() == null || client.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (client.getLastName() == null || client.getLastName().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }

        if (client.getName().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar los 100 caracteres");
        }

        if (client.getLastName().length() > 150) {
            throw new IllegalArgumentException("El apellido no puede superar los 150 caracteres");
        }

        if (client.getDni() == null || !client.getDni().matches("[0-9]{7,8}")) {
             throw new IllegalArgumentException("El dni debe tener entre 7 y 8 numeros");
        }

        if (client.getPhone() != null && !client.getPhone().isBlank()) {
            if (!client.getPhone().matches("[0-9+ -]{1,20}")) {
                throw new IllegalArgumentException("El telefono es invalido");
            }
        }

        if (client.getEmail() != null && !client.getEmail().isBlank()) {
            if (client.getEmail().length() > 100 ||
                    !client.getEmail().matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException("formato de mail invalido");
            }
        }
    }
}