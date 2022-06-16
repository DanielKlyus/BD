package com.nsu.controllers;

import com.nsu.data.entities.contract.OtherContractor;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.service.OtherContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/otherContractor")
public class OtherContractorController {
    @Autowired
    private OtherContractorService service;

    public OtherContractorController(OtherContractorService service) {
        this.service = service;
    }

    @GetMapping
    public PagedEntitiesResponse<OtherContractor> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                                   @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {

        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

    @GetMapping("/get-by-project-startDate-endDate")
    public PagedEntitiesResponse<OtherContractor> findBy(
            @RequestParam("page") Integer page,
            @RequestParam ("rowsPerPage") Integer rowsPerPage,
            @RequestParam(name = "project") Optional<Integer> project,
            @RequestParam(name = "startDate")
            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> startDate,
            @RequestParam(name = "endDate")
            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> endDate
    ) {
        System.err.println("/otherContractor/get-by " + project + " "+ startDate + " " + endDate);
        return service.findBy(project, startDate, endDate, page, rowsPerPage);
    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = OtherContractor.class.getDeclaredFields();
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
                "staffEquipment",
                "department",
                "project"
        );
    }

    @GetMapping("/filters")
    public List<String> getFilters() {
        return List.of();
    }

    @GetMapping("/{id}")
    public OtherContractor findById(@PathVariable("id") Integer id){
        return service.getById(id);
    }

    @PostMapping
    public @ResponseBody
    OtherContractor createProject(@Valid @RequestBody OtherContractor st) {
        System.err.println("/otherContractor/create " + " " + st);
        return service.create(st);
    }

    @PutMapping("/{id}")
    public OtherContractor updateProject(@PathVariable Integer id,@Valid @RequestBody OtherContractor st) {
        System.err.println("/otherContractor/update id " + id + " " + st);
        return service.update(id, st);
    }

    @DeleteMapping(path = "/{stId}")
    public void deleteProjectById(@PathVariable("stId") int id) {
        service.deleteById(id);
    }


}
