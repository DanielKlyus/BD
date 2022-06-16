package com.nsu.service;

import java.util.List;

public interface CrudService<Entity> {

    List<Entity> findAll();
    Entity getById(Integer id);
    Entity create(Entity entity);
    Entity update(Integer id, Entity entity);
    void deleteById(Integer id);
}
