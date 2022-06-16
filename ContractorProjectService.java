package com.nsu.service;

import com.nsu.config.ValidationException;
import com.nsu.data.entities.project.ContractorProject;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.project.Project;
import com.nsu.data.repo.ContractorProjectRepo;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ContractorProjectService {

    private final ContractorProjectRepo contractorProjectRepo;

    @Autowired
    public ContractorProjectService(ContractorProjectRepo contractorProjectRepo) {

        this.contractorProjectRepo = contractorProjectRepo;

    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<ContractorProject> findAll() {
        return contractorProjectRepo.findAll();
    }

    public ContractorProject getById(Integer id) {
        return contractorProjectRepo.findById(id).get();
    }

    public PagedEntitiesResponse<ContractorProject> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ContractorProject> query = builder.createQuery(ContractorProject.class);
        Root<ContractorProject> root = query.from(ContractorProject.class);

        query.orderBy(builder.asc(root.get("id")));
        List<ContractorProject> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (ContractorProject entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, contractorProjectRepo.count());
    }

    public PagedEntitiesResponse<ContractorProject> findBy(
            Optional<Integer> otherContractor,
            Integer page,
            Integer rowsPerPage) {

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ContractorProject> query = builder.createQuery(ContractorProject.class);
        Root<ContractorProject> root = query.from(ContractorProject.class);

        List<Predicate> wherePredicates = new LinkedList<>();

        otherContractor.ifPresent(integer -> wherePredicates.add(builder.equal(root.get("otherContractor"), integer)));

        query.orderBy(builder.asc(root.get("id")));

        query.where(builder.and(wherePredicates.toArray(new Predicate[0])));

        long count = em.createQuery(query.select(root)).getResultList().size() ;//pagination  .setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();
        List<ContractorProject> result = em.createQuery(query.select(root)).setFirstResult(page * rowsPerPage).setMaxResults(rowsPerPage).getResultList();

        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, count);
    }

    public ContractorProject create(ContractorProject project) {
        validate(project);
        return contractorProjectRepo.save(project);
    }

    private void validate(ContractorProject a){
        Integer gotProjectId = a.getProject().getId();

        List<ContractorProject> conproj = contractorProjectRepo.findAll();
        boolean contractFound = false;
        for (ContractorProject i : conproj){
            if (i.getId() == gotProjectId) {
                contractFound = true;
                break;
            }
        }
        if (!contractFound) {
            System.err.println("contract not found");
            throw new ValidationException("The ContractorProject with id ("+gotProjectId
                    +") has already ");
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        contractorProjectRepo.deleteById(id);
    }

    public ContractorProject update(Integer id, ContractorProject depart) {

        Optional<ContractorProject> foundEmpOpt = contractorProjectRepo.findById(id);
        if(foundEmpOpt.isEmpty()) {
            throw new ValidationException("Contractor project  with id ("+id+") not found");
        }

        ContractorProject foundEmploy = foundEmpOpt.get();

        foundEmploy.setOtherContractor(depart.getOtherContractor());
        foundEmploy.setProject(depart.getProject());

        return contractorProjectRepo.save(foundEmploy);
    }
}
