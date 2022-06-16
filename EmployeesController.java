package com.nsu.controllers;

import com.nsu.data.entities.employees.Employees;
import com.nsu.data.entities.PagedEntitiesResponse;
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
@RequestMapping("/employees")
public class EmployeesController {

    private final EmployeesService service;

    @Autowired
    public EmployeesController(EmployeesService employeesService) {
        this.service = employeesService;
    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = Employees.class.getDeclaredFields();
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
                "department",
                "positions",
                "supervisor"
        );
    }

    @GetMapping("/filters")
    public List<String> getFilters() {
        return List.of(
                "startAge",
                "endAge"
        );
    }

    @GetMapping
    public PagedEntitiesResponse<Employees> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                         @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {

        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

    @GetMapping("/{id}")
    public Employees findById(@PathVariable("id") Integer id){
        return service.getById(id);
    }

    @GetMapping("/get-by-startAge-endAge")
    public PagedEntitiesResponse<Employees> findBy(
            @RequestParam(name = "startAge") Optional<Integer> startAge,
            @RequestParam(name = "endAge") Optional<Integer> endAge,
            @RequestParam("page") Integer page,
            @RequestParam ("rowsPerPage") Integer rowsPerPage) {
        System.err.println("/employees/get-by " + startAge + endAge);
        return service.findBy(startAge, endAge, page, rowsPerPage);
    }

    @PostMapping
    public @ResponseBody
    Employees createEmployees(@Valid @RequestBody Employees employ) {
        System.err.println("/employees/create " + " " + employ);
        return service.create(employ);
    }

    @PutMapping("/{id}")
    public Employees updateEmployees(@PathVariable Integer id,@Valid @RequestBody Employees employ) {
        System.err.println("/employees/update id " + id + " " + employ);
        return service.update(id, employ);
    }

    @DeleteMapping(path = "/{employId}")
    public void deleteEmployeesById(@PathVariable("employId") int id) {
        service.deleteById(id);
    }
}
