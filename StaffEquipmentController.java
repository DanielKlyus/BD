package com.nsu.controllers;

import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.staff.StaffEquipment;
import com.nsu.service.StaffEquipmentService;
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
@RequestMapping("/staffEquipment")
public class StaffEquipmentController {

    private final StaffEquipmentService service;

    @Autowired
    public StaffEquipmentController(StaffEquipmentService staffEquipmentService) {
        this.service = staffEquipmentService;
    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = StaffEquipment.class.getDeclaredFields();
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
                "type"
        );
    }

    @GetMapping("/filters")
    public List<String> getFilters() {
        return List.of(
        );
    }

    @GetMapping
    public PagedEntitiesResponse<StaffEquipment> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                              @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {

        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

    @GetMapping("/{id}")
    public StaffEquipment findById(@PathVariable("id") Integer id){
        return service.getById(id);
    }

    @PostMapping
    public @ResponseBody
    StaffEquipment createStaffEquipment(@Valid @RequestBody StaffEquipment staff) {
        System.err.println("/staffEquipment/create " + " " + staff);
        return service.create(staff);
    }

    @PutMapping("/{id}")
    public StaffEquipment updateStaffEquipment(@PathVariable Integer id,@Valid @RequestBody StaffEquipment staff) {
        System.err.println("/staffEquipment/update id " + id + " " + staff);
        return service.update(id, staff);
    }

    @DeleteMapping(path = "/{staffId}")
    public void deleteStaffEquipmentById(@PathVariable("staffId") int id) {
        service.deleteById(id);
    }
}
