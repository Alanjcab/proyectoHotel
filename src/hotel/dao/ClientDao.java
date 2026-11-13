package hotel.dao;

import hotel.model.Client;

/**
 * define las operaciones de persistencia para los clientes
 * hreda las operaciones CRUD y agrega funcionalidades
 * especificas para la gestion de clientes
 */
public interface ClientDao extends CrudDAO<Client, Integer> {
    /**
     * busca un cliente mediante su dni
     * @param dni documento del cliente
     * @return cliente encontrado o null si no existe
     */
    Client findByDni(String dni);

    /**
     * reactiva un cliente dado de baja logicamente
     * @param id identificador del cliente
     */
    void reactivate(Integer id);
}
