package hotel.dao;

import hotel.database.DatabaseConnection;
import hotel.exception.DaoException;
import hotel.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import hotel.model.Client;
import hotel.model.Room;
import java.time.LocalDate;

public class ReservationSqlDao implements ReservationDao {

    @Override
    public Integer save(Reservation reservation) {

        String sql = "insert into reserva " + "(idCliente, idHabitacion, fechaIngreso, fechaSalida, estado, precioTotal) " + "values (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, reservation.getClient().getIdClient());
            statement.setInt(2, reservation.getRoom().getIdRoom());
            statement.setDate(3, Date.valueOf(reservation.getCheckIn()));
            statement.setDate(4, Date.valueOf(reservation.getCheckOut()));
            statement.setString(5, reservation.getStatus());
            statement.setBigDecimal(6, reservation.getTotalPrice());

            statement.executeUpdate();

            try (ResultSet result = statement.getGeneratedKeys()) {

                if (result.next()) {
                    int id = result.getInt(1);
                    reservation.setIdReservation(id);
                    return id;
                }
            }

            throw new DaoException("No se pudo obtener la reserva", null);

        } catch (SQLException ex) {
            throw new DaoException(
                    "Error al guardar la reserva", ex
            );
        }
    }


    @Override
    public void update(Reservation reservation) {

        String sql = "update reserva set " + "idCliente = ?, " + "idHabitacion = ?, " + "fechaIngreso = ?, " + "fechaSalida = ?, " + "estado = ?, " + "precioTotal = ? " + "WHERE idReserva = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reservation.getClient().getIdClient());
            statement.setInt(2, reservation.getRoom().getIdRoom());
            statement.setDate(3, Date.valueOf(reservation.getCheckIn()));
            statement.setDate(4, Date.valueOf(reservation.getCheckOut()));
            statement.setString(5, reservation.getStatus());
            statement.setBigDecimal(6, reservation.getTotalPrice());
            statement.setInt(7, reservation.getIdReservation());

            /*devuelvo la cantidad de registros afectados por la consulta*/
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DaoException("No se encontro la reserva que se quiere modificar", null);
            }
        } catch (SQLException ex) {
            throw new DaoException(
                    "Error al modificar la reserva", ex
            );
        }
    }



    @Override
    public Reservation findById(Integer id) {

        String sql = "select * from reserva where idReserva = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    ClientDao clientDao = new ClientSqlDao();
                    RoomDao roomDao = new RoomSqlDao();

                    Client client = clientDao.findById(result.getInt("idCliente"));

                    Room room = roomDao.findById(result.getInt("idHabitacion"));

                    return new Reservation( result.getInt("idReserva"),
                            client,
                            room,
                            result.getDate("fechaIngreso").toLocalDate(), /*convierto la fecha recuperada de sql*/
                            result.getDate("fechaSalida").toLocalDate(),
                            result.getString("estado"),
                            result.getBigDecimal("precioTotal")
                    );
                }
            }

            return null;
        } catch (SQLException ex) {
            throw new DaoException(
                    "Error al buscar la reserva", ex
            );
        }
    }



    @Override
    public List<Reservation> findAll() {

        String sql = "select * from reserva";

        List<Reservation> reservations = new ArrayList<>();

        ClientDao clientDao = new ClientSqlDao();
        RoomDao roomDao = new RoomSqlDao();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Client client = clientDao.findById(result.getInt("idCliente"));

                Room room = roomDao.findById(result.getInt("idHabitacion"));

                Reservation reservation = new Reservation(
                        result.getInt("idReserva"),
                        client,
                        room,
                        result.getDate("fechaIngreso").toLocalDate(),
                        result.getDate("fechaSalida").toLocalDate(),
                        result.getString("estado"),
                        result.getBigDecimal("precioTotal")
                );

                reservations.add(reservation);
            }

            return reservations;

        } catch (SQLException ex) {
            throw new DaoException(
                    "Error al listar las reservas", ex
            );
        }
    }



    @Override
    public void delete(Integer id) {

        String sql = "delete from reserva where idReserva = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DaoException("No se encontro la reserva para eliminar", null);
            }

        } catch (SQLException ex) {
            throw new DaoException(
                    "error al eliminar la reserva", ex
            );
        }
    }


    /*por si existen reservas que se suporpnen*/

    @Override
    public boolean hasOverlappingReservation(
            int roomId,
            LocalDate checkIn,
            LocalDate checkOut,
            int excludedReservationId) {

        String sql = "select COUNT(*) from reserva " + "where idHabitacion = ? " +
                "and fechaIngreso < ? " + "and fechaSalida > ? " + "and idReserva <> ? " + "and estado <> 'Cancelada'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, roomId);
            statement.setDate(2, Date.valueOf(checkOut));
            statement.setDate(3, Date.valueOf(checkIn));
            statement.setInt(4, excludedReservationId);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt(1) > 0;
                }
            }

            return false;

        } catch (SQLException ex) {
            throw new DaoException(
                    "Error al verificar la disponibilidad de la habitacion", ex
            );
        }
    }





}
