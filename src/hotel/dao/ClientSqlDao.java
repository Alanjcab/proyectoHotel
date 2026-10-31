package hotel.dao;

import hotel.database.DatabaseConnection;
import hotel.exception.DaoException;
import hotel.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class ClientSqlDao implements ClientDao {

    @Override
    public Integer save(Client client) {

        String sql = "insert into cliente (nombre, apellido, dni, telefono, email) values (?, ?, ?, ?, ?) ";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, client.getName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getDni());
            statement.setString(4, client.getPhone());
            statement.setString(5, client.getEmail());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DaoException("No se pudo guardar el cliente", null);
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    client.setIdClient(id);
                    return id;
                }
            }

            throw new DaoException("El cliente se guardo pero no se obtuvo su id", null);

        } catch (SQLException e) {
            throw new DaoException("Error al guardar el cliente", e);
        }
    }

    @Override
    public void update(Client client) {

        String sql = "update cliente set nombre = ?, apellido = ?, dni = ?, telefono = ?, email = ? where idCliente = ? and activo = true";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getDni());
            statement.setString(4, client.getPhone());
            statement.setString(5, client.getEmail());
            statement.setInt(6, client.getIdClient());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DaoException("No se encontro un cliente activo para modificar", null);
            }
        } catch (SQLException e) {
            throw new DaoException("Error al modificar el cliente", e);
        }
    }

    @Override
    public Client findById(Integer id) {String sql = " select * from cliente where idCliente = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapClient(resultSet);
                }
            }

            return null;

        } catch (SQLException e) {

            throw new DaoException(
                    "Error al buscar el cliente por id",
                    e
            );
        }
    }

    @Override
    public List<Client> findAll() {

        List<Client> clients = new ArrayList<>();

        String sql = "select * from cliente where activo = true order by apellido, nombre";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Client client = mapClient(resultSet);
                clients.add(client);
            }
            return clients;

        } catch (SQLException e) {
            throw new DaoException("Error al obtener los clientes", e);
        }
    }

    @Override
    public void delete(Integer id) {

        String sql = " update cliente set activo = false where idCliente = ? and activo = true";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DaoException(
                        "No se encontro un cliente activo para dar de baja",
                        null
                );
            }

        } catch (SQLException e) {

            throw new DaoException(
                    "Error al dar de baja el cliente.",
                    e
            );
        }
    }

    @Override
    public Client findByDni(String dni) {

        String sql = "select * from cliente where dni = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, dni);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapClient(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("Error al buscar el cliente por dni", e);
        }
    }

    @Override
    public void reactivate(Integer id) {

        String sql = "update cliente set activo = true where idCliente = ? and activo = false";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DaoException("No se encontro un cliente inactivo para habilitar.", null);
            }

        } catch (SQLException e) {
            throw new DaoException("Error en habilitar el cliente", e);
        }
    }


    private Client mapClient(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getInt("idCliente"),
                resultSet.getString("nombre"),
                resultSet.getString("apellido"),
                resultSet.getString("dni"),
                resultSet.getString("telefono"),
                resultSet.getString("email"),
                resultSet.getBoolean("activo")
        );
    }
}