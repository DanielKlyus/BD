package com.nsu.service;

import com.nsu.config.ValidationException;
import com.nsu.data.entities.employees.Division;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.repo.DivisionRepo;
import com.nsu.data.repo.EmployeesRepo;
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
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {

    private final DivisionRepo divisionRepo;
    private final EmployeesRepo employeesRepo;

    @Autowired
    public DepartmentService(
            DivisionRepo divisionRepo, EmployeesRepo employeesRepo) {

        this.divisionRepo = divisionRepo;

        this.employeesRepo = employeesRepo;
    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<Division> findAll() {
        return divisionRepo.findAll();
    }

    public Division getById(Integer id) {
        return divisionRepo.findById(id).get();
    }

    public PagedEntitiesResponse<Division> findAllPaged(Integer page, Integer rowsOnPage) {

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Division> query = builder.createQuery(Division.class);
        Root<Division> root = query.from(Division.class);

        query.orderBy(builder.asc(root.get("id")));
        List<Division> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (Division entity: result) { //dont touch this (lazy init)
        }
        em.getTransaction().commit();
        em.close();

        return new PagedEntitiesResponse<>(result, divisionRepo.count());
    }

    public Division create(Division depart) {
        return divisionRepo.save(depart);
    }


    @Transactional
    public void deleteById(Integer id) {
        divisionRepo.deleteById(id);
    }

    public Division update(Integer id, Division depart) {

        Optional<Division> foundEmpOpt = divisionRepo.findById(id);
        if(foundEmpOpt.isEmpty()) {
            throw new ValidationException("Division with id ("+id+") not found");
        }

        Division foundEmploy = foundEmpOpt.get();

        foundEmploy.setName(depart.getName());
        foundEmploy.setPhonenumber(depart.getPhonenumber());
        foundEmploy.setSupervision(depart.getSupervision());

        return divisionRepo.save(foundEmploy);
    }
}
