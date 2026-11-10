package hotel.dao;
import hotel.model.Client;

public interface ClientDao extends CrudDAO<Client, Integer> {
    Client findByDni(String dni);
    void reactivate(Integer id);
}
