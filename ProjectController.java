package com.nsu.controllers;

import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.project.Project;
import com.nsu.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    public PagedEntitiesResponse<Project> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                       @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {

        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

    @GetMapping("/get-by-project_status-contract-startDate-endDate")
    public PagedEntitiesResponse<Project> findBy(
            @RequestParam("page") Integer page,
            @RequestParam ("rowsPerPage") Integer rowsPerPage,
            @RequestParam(name = "project_status") Optional<Integer> project_status,
            @RequestParam(name = "contract") Optional<Integer> contract,
            @RequestParam(name = "startDate")
            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> startDate,
            @RequestParam(name = "endDate")
            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> endDate
            ) {
        System.err.println("/employees/get-by " + project_status + " " + contract + " "+ startDate + " " + endDate);
        return service.findBy(project_status, contract, startDate, endDate, page, rowsPerPage);
    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = Project.class.getDeclaredFields();
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
                "supervisor",
                "contract",
                "project_status"
        );
    }

    @GetMapping("/filters")
    public List<String> getFilters() {
        return List.of(
                "project_status",
                "contract",
                "startDate",
                "endDate");
    }

    @GetMapping("/get-by-startDate-endDate")
    public Double getProductivity(
            @RequestParam(name = "startDate")
            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> startDate,
            @RequestParam(name = "endDate")
            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> endDate,
            @RequestParam(name ="page")                     Integer page,
            @RequestParam(name ="rowsPerPage")              Integer rowsPerPage){
        return service.getProductivity(startDate, endDate);
    }

    @GetMapping("/{id}")
    public Project findById(@PathVariable("id") Integer id){
        return service.getById(id);
    }

    @PostMapping
    public @ResponseBody
    Project createProject(@Valid @RequestBody Project project) {
        System.err.println("/project/create " + " " + project);
        return service.create(project);
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Integer id,@Valid @RequestBody Project project) {
        System.err.println("/project/update id " + id + " " + project);
        return service.update(id, project);
    }

    @DeleteMapping(path = "/{projectId}")
    public void deleteProjectById(@PathVariable("projectId") int id) {
        service.deleteById(id);
    }


}
