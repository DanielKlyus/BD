package com.nsu.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nsu.config.ValidationException;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.employees.Division;
import com.nsu.data.entities.project.Project;
import com.nsu.data.entities.staff.StaffEquipment;
import com.nsu.data.entities.staff.TakenStaffEquipment;
import com.nsu.data.repo.TakenStaffEquipmentRepo;
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
public class TakenStaffEquipmentService {

    private final TakenStaffEquipmentRepo takenStaffEquipmentRepo;

    @Autowired
    public TakenStaffEquipmentService(TakenStaffEquipmentRepo takenStaffEquipmentRepo) {

        this.takenStaffEquipmentRepo = takenStaffEquipmentRepo;

    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<TakenStaffEquipment> findAll() {
        return takenStaffEquipmentRepo.findAll();
    }

    public TakenStaffEquipment getById(Integer id) {
        return takenStaffEquipmentRepo.findById(id).get();
    }

    public PagedEntitiesResponse<TakenStaffEquipment> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TakenStaffEquipment> query = builder.createQuery(TakenStaffEquipment.class);
        Root<TakenStaffEquipment> root = query.from(TakenStaffEquipment.class);

        query.orderBy(builder.asc(root.get("id")));
        List<TakenStaffEquipment> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (TakenStaffEquipment entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, takenStaffEquipmentRepo.count());
    }

    public PagedEntitiesResponse<TakenStaffEquipment> findBy(
            Optional<Integer> project,
            Optional<Date> startDate,
            Optional<Date> endDate,
            Integer page,
            Integer rowsPerPage) {

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TakenStaffEquipment> query = builder.createQuery(TakenStaffEquipment.class);
        Root<TakenStaffEquipment> root = query.from(TakenStaffEquipment.class);

        List<Predicate> wherePredicates = new LinkedList<>();

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
        List<TakenStaffEquipment> result = em.createQuery(query.select(root)).setFirstResult(page * rowsPerPage).setMaxResults(rowsPerPage).getResultList();

        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, count);
    }

    public TakenStaffEquipment create(TakenStaffEquipment project) {

        return takenStaffEquipmentRepo.save(project);
    }



    @Transactional
    public void deleteById(Integer id) {
        takenStaffEquipmentRepo.deleteById(id);
    }

    public TakenStaffEquipment update(Integer id, TakenStaffEquipment depart) {

        Optional<TakenStaffEquipment> foundEmpOpt = takenStaffEquipmentRepo.findById(id);
        if(foundEmpOpt.isEmpty()) {
            throw new ValidationException("Taken Staff Equipment  with id ("+id+") not found");
        }

        TakenStaffEquipment foundEmploy = foundEmpOpt.get();

        foundEmploy.setStaffEquipment(depart.getStaffEquipment());
        foundEmploy.setDivision(depart.getDivision());
        foundEmploy.setProject(depart.getProject());
        foundEmploy.setDateStart(depart.getDateStart());
        foundEmploy.setDateEnd(depart.getDateEnd());

        return takenStaffEquipmentRepo.save(foundEmploy);
    }
}
