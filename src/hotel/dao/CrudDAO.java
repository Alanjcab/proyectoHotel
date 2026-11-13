package hotel.dao;

import java.util.List;

/**
 * define las operaciones crud comunes para las entidades
 * @param <Entity> tipo de entidad que administra el dao
 * @param <ID> tipo de identificador de la entidad
 */
public interface CrudDAO<Entity, ID> {
    /**
     *guarda una nueva entidad en la db
     * @param entity entidad que se quiere guardar
     * @return identificador de la entidad guardada
     */
    ID save(Entity entity);

    /**
     * actualiza una entidad existente
     * @param entity entidad que se quiere modificar
     */
    void update(Entity entity);

    /**
     * busca una entidad mediante su identificador
     * @param id identificador de la entidad
     * @return entidad encontrada o null si no existe
     */
    Entity findById(ID id);

    /**
     *obtiene todas las entidades registradas
     * @return lista de entidades
     */
    List<Entity> findAll();

    /**
     * elimina una entidad mediante su identificador
     * @param id identificador de la entidad
     */
    void delete(ID id);
}
