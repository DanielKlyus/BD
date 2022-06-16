package com.nsu.service;

import com.nsu.config.ValidationException;
import com.nsu.data.entities.contract.OtherContractor;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.repo.OtherContractorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
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
public class OtherContractorService {

    private final OtherContractorRepo otherContractorRepo;

    @Autowired
    public OtherContractorService(OtherContractorRepo otherContractorRepo) {

        this.otherContractorRepo = otherContractorRepo;

    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<OtherContractor> findAll() {
        return otherContractorRepo.findAll();
    }

    public OtherContractor getById(Integer id) {
        return otherContractorRepo.findById(id).get();
    }

    public PagedEntitiesResponse<OtherContractor> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<OtherContractor> query = builder.createQuery(OtherContractor.class);
        Root<OtherContractor> root = query.from(OtherContractor.class);

        query.orderBy(builder.asc(root.get("id")));
        List<OtherContractor> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (OtherContractor entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, otherContractorRepo.count());
    }

    public PagedEntitiesResponse<OtherContractor> findBy(
            Optional<Integer> project,
            Optional<Date> startDate,
            Optional<Date> endDate,
            Integer page,
            Integer rowsPerPage) {

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<OtherContractor> query = builder.createQuery(OtherContractor.class);
        Root<OtherContractor> root = query.from(OtherContractor.class);

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
        List<OtherContractor> result = em.createQuery(query.select(root)).setFirstResult(page * rowsPerPage).setMaxResults(rowsPerPage).getResultList();

        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, count);
    }

    public OtherContractor create(OtherContractor project) {
        return otherContractorRepo.save(project);
    }

    @Transactional
    public void deleteById(Integer id) {
        otherContractorRepo.deleteById(id);
    }

    public OtherContractor update(Integer id, OtherContractor depart) {

        Optional<OtherContractor> foundEmpOpt = otherContractorRepo.findById(id);
        if(foundEmpOpt.isEmpty()) {
            throw new ValidationException("OtherContractor visit with id ("+id+") not found");
        }

        OtherContractor foundEmploy = foundEmpOpt.get();

        foundEmploy.setName(depart.getName());
        foundEmploy.setPhone(depart.getPhone());
        foundEmploy.setMail(depart.getMail());

        return otherContractorRepo.save(foundEmploy);
    }
}
