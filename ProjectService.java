package com.nsu.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nsu.config.ValidationException;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.contract.Contract;
import com.nsu.data.entities.employees.Employees;
import com.nsu.data.entities.project.Project;
import com.nsu.data.entities.project.ProjectStatus;
import com.nsu.data.repo.ProjectRepo;
import com.nsu.data.repo.ProjectStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectStatusRepo projectStatusRepo;

    @Autowired
    public ProjectService(ProjectRepo projectRepo, ProjectStatusRepo projectStatusRepo) {

        this.projectRepo = projectRepo;
        this.projectStatusRepo = projectStatusRepo;
    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<Project> findAll() {
        return projectRepo.findAll();
    }

    public Project getById(Integer id) {
        return projectRepo.findById(id).get();
    }

    public PagedEntitiesResponse<Project> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Project> query = builder.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);

        query.orderBy(builder.asc(root.get("id")));
        List<Project> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (Project entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, projectRepo.count());
    }

    public PagedEntitiesResponse<Project> findBy(
            Optional<Integer> status,
            Optional<Integer> contractId,
            Optional<Date> startDate,
            Optional<Date> endDate,
            Integer page,
            Integer rowsPerPage) {

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Project> query = builder.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);

        List<Predicate> wherePredicates = new LinkedList<>();

        if (status.isPresent()) {
            wherePredicates.add(builder.equal(root.get("project_status"), status.get()));
        }

        if (contractId.isPresent()) {
            wherePredicates.add(builder.equal(root.get("contract"), contractId.get()));
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
        List<Project> result = em.createQuery(query.select(root)).setFirstResult(page * rowsPerPage).setMaxResults(rowsPerPage).getResultList();

        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, count);
    }

    public Double getProductivity(Optional<Date> startDate,
                                  Optional<Date> endDate){
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Project> query = builder.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);

        List<Predicate> wherePredicates = new LinkedList<>();

        long diffInMillies;
        double periodDays;
        if (startDate.isPresent() && endDate.isPresent()){
            wherePredicates.add(builder.between(root.get("dateStart"), startDate.get(), endDate.get()));
            wherePredicates.add(builder.between(root.get("dateEnd"), startDate.get(), endDate.get()));
            diffInMillies = Math.abs(endDate.get().getTime() - startDate.get().getTime());

            periodDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            System.out.println("periodDays " + periodDays);
        } else {
            throw new ValidationException("Please enter period");
        }
        query.where(builder.and(wherePredicates.toArray(new Predicate[0])));
        //query.distinct(true);
        List<Project> result = em.createQuery(query.select(root)).getResultList() ;//pagination.setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();
        double sum = 0;
        for (Project i:result) {
            sum = sum + i.getPrice();
        }

        em.getTransaction().commit();
        em.close();
        double periodMonth = periodDays / 365 * 12;
        return sum/periodMonth;
    }

    public Project create(Project project) {
        return projectRepo.save(project);
    }

    @Transactional
    public void deleteById(Integer id) {
        projectRepo.deleteById(id);
    }

    public Project update(Integer id, Project depart) {

        Optional<Project> foundEmpOpt = projectRepo.findById(id);
        if(foundEmpOpt.isEmpty()) {
            throw new ValidationException("Project with id ("+id+") not found");
        }

        Project foundEmploy = foundEmpOpt.get();

        foundEmploy.setName(depart.getName());
        foundEmploy.setPrice(depart.getPrice());
        foundEmploy.setdateStart(depart.getdateStart());
        foundEmploy.setdateEnd(depart.getdateEnd());
        foundEmploy.setSupervisor(depart.getSupervisor());
        foundEmploy.setContract(depart.getContract());
        foundEmploy.setProject_status(depart.getProject_status());

        return projectRepo.save(foundEmploy);
    }
}
