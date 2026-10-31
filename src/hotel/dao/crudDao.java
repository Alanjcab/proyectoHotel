package hotel.dao;
import java.util.List;

public interface crudDao <Entity, ID>{
    ID save(Entity entity);
    void update (Entity entity);
    Entity findById (ID id);
    List<Entity> findAll();
    void delete(ID id);
}
