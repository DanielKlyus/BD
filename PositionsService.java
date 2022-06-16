package com.nsu.service;

import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.employees.Positions;
import com.nsu.data.repo.PositionsRepo;
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
public class PositionsService {

    private final PositionsRepo positionsRepo;

    @Autowired
    public PositionsService(
                            PositionsRepo positionsRepo) {

        this.positionsRepo = positionsRepo;

    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;



    public PagedEntitiesResponse<Positions> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Positions> query = builder.createQuery(Positions.class);
        Root<Positions> root = query.from(Positions.class);

        query.orderBy(builder.asc(root.get("id")));
        List<Positions> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (Positions entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, positionsRepo.count());
    }
}
