package com.nsu.service;

import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.staff.TypeEquipment;
import com.nsu.data.repo.TypeEquipmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TypeEquipmentService {

    private final TypeEquipmentRepo typeEquipmentRepo;

    @Autowired
    public TypeEquipmentService(
            TypeEquipmentRepo typeEquipmentRepo) {

        this.typeEquipmentRepo = typeEquipmentRepo;

    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public TypeEquipment getById(Integer id) {
        return typeEquipmentRepo.findById(id).get();
    }

    public PagedEntitiesResponse<TypeEquipment> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TypeEquipment> query = builder.createQuery(TypeEquipment.class);
        Root<TypeEquipment> root = query.from(TypeEquipment.class);

        query.orderBy(builder.asc(root.get("id")));
        List<TypeEquipment> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (TypeEquipment entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, typeEquipmentRepo.count());
    }
}
