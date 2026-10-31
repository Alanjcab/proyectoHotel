package hotel.dao;
import hotel.model.Client;

public interface ClientDao extends crudDao<Client, Integer>{
    Client findByDni(String dni);
    void reactivate(Integer id);
}
