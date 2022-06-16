package com.nsu.service;

import com.nsu.config.ValidationException;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.staff.StaffEquipment;

import com.nsu.data.repo.StaffEquipmentRepo;

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
public class StaffEquipmentService {

    private final StaffEquipmentRepo staffEquipmentRepo;
    //private final StaffStatusRepo staffStatusRepo;

    @Autowired
    public StaffEquipmentService(
            StaffEquipmentRepo staffEquipmentRepo) {

        this.staffEquipmentRepo = staffEquipmentRepo;
    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<StaffEquipment> findAll() {
        return staffEquipmentRepo.findAll();
    }

    public StaffEquipment getById(Integer id) {
        return staffEquipmentRepo.findById(id).get();
    }

    public PagedEntitiesResponse<StaffEquipment> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<StaffEquipment> query = builder.createQuery(StaffEquipment.class);
        Root<StaffEquipment> root = query.from(StaffEquipment.class);

        query.orderBy(builder.asc(root.get("id")));
        List<StaffEquipment> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (StaffEquipment entity: result) { //dont touch this (lazy init)
            //entity.getMedicalInstitutions().size();
        }
        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, staffEquipmentRepo.count());
    }

    public StaffEquipment create(StaffEquipment staff) {

        return staffEquipmentRepo.save(staff);
    }

    @Transactional
    public void deleteById(Integer id) {
        staffEquipmentRepo.deleteById(id);
    }

    public StaffEquipment update(Integer id, StaffEquipment staff) {

        Optional<StaffEquipment> foundEmpOpt = staffEquipmentRepo.findById(id);
        if(foundEmpOpt.isEmpty()) {
            throw new ValidationException("Staff equipment  with id ("+id+") not found");
        }

        StaffEquipment foundstaff = foundEmpOpt.get();
        foundstaff.setArticle(staff.getArticle());
        foundstaff.setName(staff.getName());
        foundstaff.setType(staff.getType());

        return staffEquipmentRepo.save(foundstaff);
    }
}
