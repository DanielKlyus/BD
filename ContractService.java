package com.nsu.service;

import com.nsu.config.ValidationException;
import com.nsu.data.entities.contract.Contract;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.repo.ContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContractService {

    private final ContractRepo contractRepo;


    @Autowired
    public ContractService(ContractRepo contractRepo) {

        this.contractRepo = contractRepo;

    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<Contract> findAll() {
        return contractRepo.findAll();
    }

    public Contract getById(Integer id) {
        return contractRepo.findById(id).get();
    }

    public PagedEntitiesResponse<Contract> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Contract> query = builder.createQuery(Contract.class);
        Root<Contract> root = query.from(Contract.class);

        query.orderBy(builder.asc(root.get("id")));
        List<Contract> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (Contract entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, contractRepo.count());
    }

    public PagedEntitiesResponse<Contract> findBy(
            Optional<Integer> project,
            Optional<Date> startDate,
            Optional<Date> endDate,
            Integer page,
            Integer rowsPerPage) {

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Contract> query = builder.createQuery(Contract.class);
        Root<Contract> root = query.from(Contract.class);

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
        List<Contract> result = em.createQuery(query.select(root)).setFirstResult(page * rowsPerPage).setMaxResults(rowsPerPage).getResultList();

        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, count);
    }

//    public Double getProductivity(Optional<Date> startDate,
//                                  Optional<Date> endDate){
//        EntityManager em = entityManagerFactory.createEntityManager();
//        em.getTransaction().begin();
//        CriteriaBuilder builder = em.getCriteriaBuilder();
//        CriteriaQuery<Contract> query = builder.createQuery(Contract.class);
//        Root<Contract> root = query.from(Contract.class);
//
//        List<Predicate> wherePredicates = new LinkedList<>();
//
//        long diffInMillies;
//        double periodDays;
//        if (startDate.isPresent() && endDate.isPresent()){
//            wherePredicates.add(builder.between(root.get("DateSigning"), startDate.get(), endDate.get()));
//            diffInMillies = Math.abs(endDate.get().getTime() - startDate.get().getTime());
//
//            periodDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
//            System.out.println("periodDays " + periodDays);
//        } else {
//            throw new ValidationException("Please enter period");
//        }
//        EntityManager em1 = entityManagerFactory.createEntityManager();
//        CriteriaBuilder builder1 = em1.getCriteriaBuilder();
//        CriteriaQuery<Project> query1 = builder1.createQuery(Project.class);
//        Root<Project> root1 = query1.from(Project.class);
//        Join<Project, Contract> projectJoin = root1.join("contract");
//        wherePredicates.add(builder.between(projectJoin.get("dateStart"), startDate.get(), endDate.get()));
//        wherePredicates.add(builder.between(projectJoin.get("dateEnd"), startDate.get(), endDate.get()));
//
//
//        query.where(builder.and(wherePredicates.toArray(new Predicate[0])));
//        //query.distinct(true);
//        //List<Contract> result = em.createQuery(query.select(root)).getResultList() ;//pagination.setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();
//        double sum = 0;
////        for (Contract i:result) {
////            sum = sum + i.getPrice();
////        }
//
//        em.getTransaction().commit();
//        em.close();
//        double periodMonth = periodDays / 365 * 12;
//        return sum/periodMonth;
//    }

    public Contract create(Contract project) {
        return contractRepo.save(project);
    }

    @Transactional
    public void deleteById(Integer id) {
        contractRepo.deleteById(id);
    }

    public Contract update(Integer id, Contract depart) {

        Optional<Contract> foundEmpOpt = contractRepo.findById(id);
        if(foundEmpOpt.isEmpty()) {
            throw new ValidationException("Contract with id ("+id+") not found");
        }

        Contract foundEmploy = foundEmpOpt.get();

        foundEmploy.setCompany(depart.getCompany());
        foundEmploy.setEmployees(depart.getEmployees());
        foundEmploy.setDateSigning(depart.getDateSigning());

        return contractRepo.save(foundEmploy);
    }
}
