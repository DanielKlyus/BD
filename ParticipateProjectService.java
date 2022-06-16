package com.nsu.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nsu.config.ValidationException;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.employees.Employees;
import com.nsu.data.entities.employees.Positions;
import com.nsu.data.entities.project.ParticipateProject;
import com.nsu.data.entities.project.Project;
import com.nsu.data.repo.ParticipateProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParticipateProjectService {

    private final ParticipateProjectRepo projectRepo;

    @Autowired
    public ParticipateProjectService(ParticipateProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<ParticipateProject> findAll() {
        return projectRepo.findAll();
    }

    public ParticipateProject getById(Integer id) {
        return projectRepo.findById(id).get();
    }

    public PagedEntitiesResponse<ParticipateProject> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ParticipateProject> query = builder.createQuery(ParticipateProject.class);
        Root<ParticipateProject> root = query.from(ParticipateProject.class);

        query.orderBy(builder.asc(root.get("id")));
        List<ParticipateProject> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (ParticipateProject entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, projectRepo.count());
    }

    public PagedEntitiesResponse<ParticipateProject> findBy(
            Optional<Integer> employee,
            Optional<Integer> position,
            Optional<Integer> project,
            Optional<Date> startDate,
            Optional<Date> endDate,
            Integer page,
            Integer rowsPerPage) {

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ParticipateProject> query = builder.createQuery(ParticipateProject.class);
        Root<ParticipateProject> root = query.from(ParticipateProject.class);

        List<Predicate> wherePredicates = new LinkedList<>();

        if (employee.isPresent()) {
            wherePredicates.add(builder.equal(root.get("employees"), employee.get()));
        }

        if (position.isPresent()) {
            wherePredicates.add(builder.equal(root.get("positions"), position.get()));
        }

        if (project.isPresent()) {
            wherePredicates.add(builder.equal(root.get("project"), project.get()));
        }

        if (startDate.isPresent() && endDate.isPresent()) {
            System.out.println("visits form " + startDate.get() + ", to " + endDate.get() );
            wherePredicates.add(builder.between(root.get("dateStart"), startDate.get(), endDate.get()));
        }
        if (startDate.isPresent() && endDate.isPresent()) {
            System.out.println("visits form " + startDate.get() + ", to " + endDate.get() );
            wherePredicates.add(builder.between(root.get("dateEnd"), startDate.get(), endDate.get()));
        }
        query.orderBy(builder.asc(root.get("id")));

        query.where(builder.and(wherePredicates.toArray(new Predicate[0])));

        long count = em.createQuery(query.select(root)).getResultList().size() ;//pagination  .setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();
        List<ParticipateProject> result = em.createQuery(query.select(root)).setFirstResult(page * rowsPerPage).setMaxResults(rowsPerPage).getResultList();

        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, count);
    }

    public ParticipateProject create(ParticipateProject project) {
        return projectRepo.save(project);
    }

    @Transactional
    public void deleteById(Integer id) {
        projectRepo.deleteById(id);
    }

    public ParticipateProject update(Integer id, ParticipateProject depart) {

        Optional<ParticipateProject> foundEmpOpt = projectRepo.findById(id);
        if(foundEmpOpt.isEmpty()) {
            throw new ValidationException("Participate project with id ("+id+") not found");
        }

        ParticipateProject foundEmploy = foundEmpOpt.get();

        foundEmploy.setdateStart(depart.getdateStart());
        foundEmploy.setdateEnd(depart.getdateEnd());
        foundEmploy.setEmployees(depart.getEmployees());
        foundEmploy.setPositions(depart.getPositions());
        foundEmploy.setProject(depart.getProject());

        return projectRepo.save(foundEmploy);
    }
}
