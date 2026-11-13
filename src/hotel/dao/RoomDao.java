package hotel.dao;

import hotel.model.Room;

/**
 * define las operaciones de persistencia para las habitaciones.
 * hereda las operaciones crud y agrega la busquedade habitaciones
 */
public interface RoomDao extends CrudDAO<Room, Integer> {
    /**
     * busca una habitacion mediante su numero.
     * @param number numero de la habitacion
     * @return habitacion encontrada o null si no existe
     */
    Room findByNumber(int number);
}
