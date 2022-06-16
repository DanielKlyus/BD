package com.nsu.service;

import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.project.ProjectStatus;
import com.nsu.data.repo.ProjectStatusRepo;
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
public class ProjectStatusService {

    private final ProjectStatusRepo projectStatusRepo;

    @Autowired
    public ProjectStatusService(ProjectStatusRepo projectStatusRepo) {

        this.projectStatusRepo = projectStatusRepo;

    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;



    public PagedEntitiesResponse<ProjectStatus> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ProjectStatus> query = builder.createQuery(ProjectStatus.class);
        Root<ProjectStatus> root = query.from(ProjectStatus.class);

        query.orderBy(builder.asc(root.get("id")));
        List<ProjectStatus> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (ProjectStatus entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();


        return new PagedEntitiesResponse<>(result, projectStatusRepo.count());
    }
}
