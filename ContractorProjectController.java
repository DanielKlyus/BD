package com.nsu.controllers;

import com.nsu.data.entities.project.ContractorProject;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.service.ContractorProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/contractorProject")
public class ContractorProjectController {
    @Autowired
    private ContractorProjectService service;

    public ContractorProjectController(ContractorProjectService service) {
        this.service = service;
    }

    @GetMapping
    public PagedEntitiesResponse<ContractorProject> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                               @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {

        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

    @GetMapping("/get-by-otherContractor")
    public PagedEntitiesResponse<ContractorProject> findBy(
            @RequestParam("page") Integer page,
            @RequestParam ("rowsPerPage") Integer rowsPerPage,
            @RequestParam(name = "otherContractor") Optional<Integer> otherContractor
    ) {
        System.err.println("/contractorProject/get-by " + otherContractor);
        return service.findBy(otherContractor, page, rowsPerPage);
    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = ContractorProject.class.getDeclaredFields();
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
                "otherContractor",
                "project"
        );
    }

    @GetMapping("/filters")
    public List<String> getFilters() {
        return List.of(
                "otherContractor"
        );
    }

    @GetMapping("/{id}")
    public ContractorProject findById(@PathVariable("id") Integer id){
        return service.getById(id);
    }

    @PostMapping
    public @ResponseBody
    ContractorProject createProject(@Valid @RequestBody ContractorProject st) {
        System.err.println("/contractorProject/create " + " " + st);
        return service.create(st);
    }

    @PutMapping("/{id}")
    public ContractorProject updateProject(@PathVariable Integer id,@Valid @RequestBody ContractorProject st) {
        System.err.println("/contractorProject/update id " + id + " " + st);
        return service.update(id, st);
    }

    @DeleteMapping(path = "/{stId}")
    public void deleteProjectById(@PathVariable("stId") int id) {
        service.deleteById(id);
    }


}
