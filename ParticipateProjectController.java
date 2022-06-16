package com.nsu.controllers;

import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.project.ParticipateProject;
import com.nsu.service.ParticipateProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/participateProject")
public class ParticipateProjectController {
    @Autowired
    private ParticipateProjectService service;

    public ParticipateProjectController(ParticipateProjectService service) {
        this.service = service;
    }

    @GetMapping
    public PagedEntitiesResponse<ParticipateProject> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                                  @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {

        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

    @GetMapping("/get-by-employees-positions-project-startDate-endDate")
    public PagedEntitiesResponse<ParticipateProject> findBy(
            @RequestParam("page") Integer page,
            @RequestParam ("rowsPerPage") Integer rowsPerPage,
            @RequestParam(name = "employees") Optional<Integer> employee,
            @RequestParam(name = "positions") Optional<Integer> position,
            @RequestParam(name = "project") Optional<Integer> project,
            @RequestParam(name = "startDate")
            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> startDate,
            @RequestParam(name = "endDate")
            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> endDate
    ) {
        System.err.println("/employees/get-by " + employee + " " + position + " "+ project + " " + startDate + " " + endDate);
        return service.findBy(employee, position, project, startDate, endDate, page, rowsPerPage);
    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = ParticipateProject.class.getDeclaredFields();
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
                "employees",
                "positions",
                "project"
        );
    }

    @GetMapping("/filters")
    public List<String> getFilters() {
        return List.of(
                "employees",
                "positions",
                "project",
                "startDate",
                "endDate");
    }

    @GetMapping("/{id}")
    public ParticipateProject findById(@PathVariable("id") Integer id){
        return service.getById(id);
    }

    @PostMapping
    public @ResponseBody
    ParticipateProject createProject(@Valid @RequestBody ParticipateProject project) {
        System.err.println("/participateProject/create " + " " + project);
        return service.create(project);
    }

    @PutMapping("/{id}")
    public ParticipateProject updateProject(@PathVariable Integer id,@Valid @RequestBody ParticipateProject project) {
        System.err.println("/participateProject/update id " + id + " " + project);
        return service.update(id, project);
    }

    @DeleteMapping(path = "/{participateProjectId}")
    public void deleteProjectById(@PathVariable("participateProjectId") int id) {
        service.deleteById(id);
    }


}
