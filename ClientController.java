package com.nsu.controllers;

import com.nsu.data.entities.contract.Client;
import com.nsu.data.entities.employees.Employees;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.service.ClientService;
import com.nsu.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService service;

    @Autowired
    public ClientController(ClientService clientService) {
        this.service = clientService;
    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = Client.class.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields){
            if(!Collection.class.isAssignableFrom(field.getType())){
                fieldNames.add(field.getName());
            }
            System.out.println(field.getName());
        }

        return fieldNames;
    }

    @GetMapping("/foreign-keys")
    public List<String> getForeignKeys() {
        return List.of(
        );
    }

    @GetMapping("/filters")
    public List<String> getFilters() {
        return List.of(

        );
    }

    @GetMapping
    public PagedEntitiesResponse<Client> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                         @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {

        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable("id") Integer id){
        return service.getById(id);
    }


    @PostMapping
    public @ResponseBody
    Client createEmployees(@Valid @RequestBody Client employ) {
        System.err.println("/client/create " + " " + employ);
        return service.create(employ);
    }

    @PutMapping("/{id}")
    public Client updateEmployees(@PathVariable Integer id,@Valid @RequestBody Client employ) {
        System.err.println("/client/update id " + id + " " + employ);
        return service.update(id, employ);
    }

    @DeleteMapping(path = "/{clientId}")
    public void deleteEmployeesById(@PathVariable("clientId") int id) {
        service.deleteById(id);
    }
}

