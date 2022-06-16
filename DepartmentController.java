package com.nsu.controllers;

import com.nsu.data.entities.employees.Division;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.service.DepartmentService;
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
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping
    public PagedEntitiesResponse<Division> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                               @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {
        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = Division.class.getDeclaredFields();
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
                "supervisor"
        );
    }

    @GetMapping("/filters")
    public List<String> getFilters() {
        return List.of();
    }

    @GetMapping("/{id}")
    public Division findById(@PathVariable("id") Integer id){
        return service.getById(id);
    }

    @PostMapping
    public @ResponseBody
    Division createDepartment(@Valid @RequestBody Division division) {
        System.err.println("/division/create " + " " + division);
        return service.create(division);
    }

    @PutMapping("/{id}")
    public Division updateDepartment(@PathVariable Integer id, @Valid @RequestBody Division division) {
        System.err.println("/division/update id " + id + " " + division);
        return service.update(id, division);
    }

    @DeleteMapping(path = "/{divisionId}")
    public void deleteDepartmentById(@PathVariable("divisionId") int id) {
        service.deleteById(id);
    }


}
