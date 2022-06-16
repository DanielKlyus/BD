package com.nsu.service;

import com.nsu.config.ValidationException;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.contract.Client;
import com.nsu.data.entities.employees.Employees;
import com.nsu.data.repo.ClientRepo;
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

@Service
@Transactional
public class ClientService implements CrudService<Client> {
    private final ClientRepo clientRepo;

    @Autowired
    public ClientService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<Client> findAll() {
        return clientRepo.findAll();
    }

    public Client getById(Integer id) {
        return clientRepo.findById(id).get();
    }


    public Client create(Client employ) {
        return clientRepo.save(employ);
    }

    @Transactional
    public void deleteById(Integer id) {
        clientRepo.deleteById(id);
    }




    public Client update(Integer id, Client employ) {

        Optional<Client> foundEmpOpt = clientRepo.findById(id);
        if(foundEmpOpt.isEmpty()) {
            throw new ValidationException("Client with id ("+id+") not found");
        }

        Client foundEmploy = foundEmpOpt.get();

        foundEmploy.setMail(employ.getMail());
        foundEmploy.setPhoneNumber(employ.getPhoneNumber());
        foundEmploy.setCompanyName(employ.getCompanyName());

        return clientRepo.save(foundEmploy);
    }

    public PagedEntitiesResponse<Client> findAllPaged(Integer page, Integer rowsOnPage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Client> query = builder.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

        query.orderBy(builder.asc(root.get("id")));
        List<Client> result = em.createQuery(query.select(root)).setFirstResult(page * rowsOnPage).setMaxResults(rowsOnPage).getResultList();

        for (Client entity: result) { //dont touch this (lazy init)

        }

        em.getTransaction().commit();
        em.close();
        return new PagedEntitiesResponse<>(result, clientRepo.count());
    }

    @Transactional
    public void deleteAllBySpecializationId(Integer id) {

    }
}
