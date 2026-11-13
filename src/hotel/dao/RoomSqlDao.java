package hotel.dao;

import hotel.database.DatabaseConnection;
import hotel.exception.DaoException;
import hotel.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * implementacion de RoomDao usando mysql
 * administra las operaciones de persistencia de habitaciones
 */
public class RoomSqlDao implements RoomDao {
    @Override
    public Integer save(Room room) {

        String sql = "insert into habitacion (numero, tipo, capacidad, precioNoche, estado) " + "values (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, room.getNumber());
            statement.setString(2, room.getType());
            statement.setInt(3, room.getCapacity());
            statement.setBigDecimal(4, room.getPricePerNight());
            statement.setString(5, room.getStatus());

            statement.executeUpdate();

            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    int id = result.getInt(1);
                    room.setIdRoom(id);
                    return id;
                }
            }

            throw new DaoException("No se pudo obtener la habitacin", null);

        } catch (SQLException ex) {
            throw new DaoException("Error al guardar la habitacion", ex);
        }
    }

    @Override
    public void update(Room room) {

        String sql = "update habitacion set numero = ?, tipo = ?, capacidad = ?, " + "precioNoche = ?, estado = ? where idHabitacion = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, room.getNumber());
            statement.setString(2, room.getType());
            statement.setInt(3, room.getCapacity());
            statement.setBigDecimal(4, room.getPricePerNight());
            statement.setString(5, room.getStatus());
            statement.setInt(6, room.getIdRoom());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                throw new DaoException("No se encontro habitación para modificar", null);
            }

        } catch (SQLException ex) {
            throw new DaoException("Error al modificar la habitacion", ex);
        }
    }

    @Override
    public Room findById(Integer id) {

        String sql = "select * from habitacion where idHabitacion = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapRoom(result);
                }
            }

            return null;

        } catch (SQLException ex) {
            throw new DaoException("Error al buscar la habitacion", ex);
        }
    }

    @Override
    public List<Room> findAll() {

        List<Room> rooms = new ArrayList<>();

        String sql = "select * from habitacion order by numero";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                rooms.add(mapRoom(result));
            }

            return rooms;

        } catch (SQLException ex) {
            throw new DaoException("error al listar las habitaciones", ex);
        }
    }

    @Override
    public void delete(Integer id) {

        String sql = "delete from habitacion where idHabitacion = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows == 0) {
                throw new DaoException("No se encontro la habitacion para eliminar", null);
            }

        } catch (SQLException ex) {
            throw new DaoException("Error al eliminar la habitacion.", ex);
        }
    }

    @Override
    public Room findByNumber(int number) {

        String sql = "select * from habitacion where numero = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, number);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapRoom(result);
                }
            }

            return null;

        } catch (SQLException ex) {
            throw new DaoException("Error al buscar la habitacion por numero", ex);
        }
    }

    private Room mapRoom(ResultSet result) throws SQLException {

        return new Room(
                result.getInt("idHabitacion"),
                result.getInt("numero"),
                result.getString("tipo"),
                result.getInt("capacidad"),
                result.getBigDecimal("precioNoche"),
                result.getString("estado")
        );
    }
}
