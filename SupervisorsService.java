package com.nsu.service;

import com.nsu.data.entities.employees.Employees;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.repo.SupervisorsRepo;
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
public class SupervisorsService {

    private final SupervisorsRepo supervisorsRepo;

    @Autowired
    public SupervisorsService(
            SupervisorsRepo supervisorsRepo) {

        this.supervisorsRepo = supervisorsRepo;

    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;



    public PagedEntitiesResponse<Employees> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Employees> query = builder.createQuery(Employees.class);
        Root<Employees> root = query.from(Employees.class);

        query.orderBy(builder.asc(root.get("id")));
        List<Employees> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (Employees entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();


        return new PagedEntitiesResponse<>(result, supervisorsRepo.count());
    }
}
