package hotel.validation;

import hotel.model.Client;

/* valido los datos del cliente ants de guardar o modificarlo*/

public class ClientValidator {

    public void validate(Client client) {

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
            throw new IllegalArgumentException("El dni debe tener maximo 8 numeros");
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