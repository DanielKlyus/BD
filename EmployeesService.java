package com.nsu.service;

import com.nsu.config.ValidationException;
import com.nsu.data.entities.employees.Employees;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.repo.DivisionRepo;
import com.nsu.data.repo.EmployeesRepo;
import com.nsu.data.repo.PositionsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class EmployeesService implements CrudService<Employees> {
    private final EmployeesRepo employRepo;

    @Autowired
    public EmployeesService(EmployeesRepo employRepo) {
        this.employRepo = employRepo;
    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<Employees> findAll() {
        return employRepo.findAll();
    }

    public Employees getById(Integer id) {
        return employRepo.findById(id).get();
    }


    public Employees create(Employees employ) {
        return employRepo.save(employ);
    }

    @Transactional
    public void deleteById(Integer id) {
        employRepo.deleteById(id);
    }

    public PagedEntitiesResponse<Employees> findBy(
                                                Optional<Integer> startAge,
                                                Optional<Integer> endAge,
                                                Integer page,
                                                Integer rowsPerPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Employees> query = builder.createQuery(Employees.class);
        Root<Employees> root = query.from(Employees.class);
        List<Predicate> wherePredicates = new LinkedList<>();

        if (startAge.isPresent() && endAge.isPresent()) {
            System.out.println("Age form " + startAge.get() + ", to " + endAge.get() );
            wherePredicates.add(builder.between(root.get("Age"), startAge.get(), endAge.get()));
        }


        query.orderBy(builder.asc(root.get("id")));

        query.where(builder.and(wherePredicates.toArray(new Predicate[0])));

        long count = em.createQuery(query.select(root)).getResultList().size() ;//pagination  .setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();
        List<Employees> result = em.createQuery(query.select(root)).setFirstResult(page * rowsPerPage).setMaxResults(rowsPerPage).getResultList();

        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, count);
    }


    public Employees update(Integer id, Employees employ) {

        Optional<Employees> foundEmpOpt = employRepo.findById(id);
        if(foundEmpOpt.isEmpty()) {
            throw new ValidationException("Employee with id ("+id+") not found");
        }

        Employees foundEmploy = foundEmpOpt.get();

        foundEmploy.setSurname(employ.getSurname());
        foundEmploy.setName(employ.getName());
        foundEmploy.setPatronymic(employ.getPatronymic());
        foundEmploy.setAge(employ.getAge());
        foundEmploy.setPositions(employ.getPositions());
        foundEmploy.setDivision(employ.getDivision());
        foundEmploy.setSupervisor(employ.getSupervisor());

        return employRepo.save(foundEmploy);
    }

    public PagedEntitiesResponse<Employees> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Employees> query = builder.createQuery(Employees.class);
        Root<Employees> root = query.from(Employees.class);

        query.orderBy(builder.asc(root.get("id")));
        List<Employees> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (Employees entity: result) { //dont touch this (lazy init)

        }

        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, employRepo.count());
    }

    @Transactional
    public void deleteAllBySpecializationId(Integer id) {

    }
}
