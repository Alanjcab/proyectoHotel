package hotel.dao;

import hotel.model.Room;

public interface RoomDao extends CrudDAO<Room, Integer> {
    Room findByNumber(int number);
}