package com.nsu.controllers;

import com.nsu.data.entities.contract.Contract;
import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/contract")
public class ContractController {
    @Autowired
    private ContractService service;

    public ContractController(ContractService service) {
        this.service = service;
    }

    @GetMapping
    public PagedEntitiesResponse<Contract> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                       @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {

        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

//    @GetMapping("/get-by-project-startDate-endDate")
//    public PagedEntitiesResponse<Contract> findBy(
//            @RequestParam("page") Integer page,
//            @RequestParam ("rowsPerPage") Integer rowsPerPage,
//            @RequestParam(name = "project_status") Optional<Integer> project_status,
//            @RequestParam(name = "project") Optional<Integer> project,
//            @RequestParam(name = "startDate")
//            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> startDate,
//            @RequestParam(name = "endDate")
//            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> endDate
//    ) {
//        System.err.println("/employees/get-by " + project_status + " " + project + " "+ startDate + " " + endDate);
//        return service.findBy(project, startDate, endDate, page, rowsPerPage);
//    }

//    @GetMapping("/get-by-startDate-endDate")
//    public Double getProductivity(
//            @RequestParam(name = "startDate")
//            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> startDate,
//            @RequestParam(name = "endDate")
//            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> endDate,
//            @RequestParam(name ="page")                     Integer page,
//            @RequestParam(name ="rowsPerPage")              Integer rowsPerPage){
//        return service.getProductivity(startDate, endDate);
//    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = Contract.class.getDeclaredFields();
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
                "client",
                "employees"
        );
    }

    @GetMapping("/filters")
    public List<String> getFilters() {
        return List.of(
               );
    }

    @GetMapping("/{id}")
    public Contract findById(@PathVariable("id") Integer id){
        return service.getById(id);
    }

    @PostMapping
    public @ResponseBody
    Contract createProject(@Valid @RequestBody Contract contr) {
        System.err.println("/contracts/create " + " " + contr);
        return service.create(contr);
    }

    @PutMapping("/{id}")
    public Contract updateProject(@PathVariable Integer id, @Valid @RequestBody Contract contr) {
        System.err.println("/contracts/update id " + id + " " + contr);
        return service.update(id, contr);
    }

    @DeleteMapping(path = "/{contractId}")
    public void deleteProjectById(@PathVariable("contractId") int id) {
        service.deleteById(id);
    }


}
