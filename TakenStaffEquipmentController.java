package com.nsu.controllers;

import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.staff.TakenStaffEquipment;
import com.nsu.service.TakenStaffEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/takenStaffEquipment")
public class TakenStaffEquipmentController {
    @Autowired
    private TakenStaffEquipmentService service;

    public TakenStaffEquipmentController(TakenStaffEquipmentService service) {
        this.service = service;
    }

    @GetMapping
    public PagedEntitiesResponse<TakenStaffEquipment> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                       @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {

        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

    @GetMapping("/get-by-project-startDate-endDate")
    public PagedEntitiesResponse<TakenStaffEquipment> findBy(
            @RequestParam("page") Integer page,
            @RequestParam ("rowsPerPage") Integer rowsPerPage,
            @RequestParam(name = "project") Optional<Integer> project,
            @RequestParam(name = "startDate")
            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> startDate,
            @RequestParam(name = "endDate")
            @DateTimeFormat(pattern = "dd.MM.yyyy")   Optional<Date> endDate
    ) {
        System.err.println("/employees/get-by " + project + " "+ startDate + " " + endDate);
        return service.findBy(project, startDate, endDate, page, rowsPerPage);
    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = TakenStaffEquipment.class.getDeclaredFields();
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
        return List.of(
                "project",
                "startDate",
                "endDate");
    }

    @GetMapping("/{id}")
    public TakenStaffEquipment findById(@PathVariable("id") Integer id){
        return service.getById(id);
    }

    @PostMapping
    public @ResponseBody
    TakenStaffEquipment createProject(@Valid @RequestBody TakenStaffEquipment st) {
        System.err.println("/takenStaffEquipment/create " + " " + st);
        return service.create(st);
    }

    @PutMapping("/{id}")
    public TakenStaffEquipment updateProject(@PathVariable Integer id,@Valid @RequestBody TakenStaffEquipment st) {
        System.err.println("/takenStaffEquipment/update id " + id + " " + st);
        return service.update(id, st);
    }

    @DeleteMapping(path = "/{stId}")
    public void deleteProjectById(@PathVariable("stId") int id) {
        service.deleteById(id);
    }


}
