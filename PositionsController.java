package com.nsu.controllers;

import com.nsu.data.entities.PagedEntitiesResponse;
import com.nsu.data.entities.employees.Positions;
import com.nsu.service.PositionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/positions")
public class PositionsController {
    @Autowired
    private PositionsService service;

    public PositionsController(PositionsService service) {
        this.service = service;
    }

    @GetMapping
    public PagedEntitiesResponse<Positions> findAllPaged(@RequestParam(name ="page") Optional<Integer> page,
                                                         @RequestParam(name ="rowsPerPage") Optional<Integer> rowsPerPage) {

        return service.findAllPaged(page.get(), rowsPerPage.get());
    }

//    @GetMapping("/{id}")
//    public Positions findById(@PathVariable("id") Integer id) {
//        return service.getById(id);
//    }

    @GetMapping("/headers")
    public List<String> getHeaders() {
        Field[] fields = Positions.class.getDeclaredFields();
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
        return List.of();
    }

    @GetMapping("/filters")
    public List<String> getFilters() {
        return List.of();
    }

//    @GetMapping("/get-by-polyclinicId")
//    public PagedEntitiesResponse<Positions> getCabinetsByPolyclinicId(@RequestParam("polyclinicId") Optional<Integer> id,
//                                                                    @RequestParam("page") Integer page,
//                                                                    @RequestParam("rowsPerPage") Integer rowsPerPage) {
//        if (id.isPresent()) {
//            return service.getCabinetsByPolyclinicId(id.get(), page, rowsPerPage);
//        }
//        else return service.findAllPaged(page, rowsPerPage);
//    }
//
//    @PostMapping
//    public Positions create(@Valid @RequestBody Positions cabinet){
//        return service.create(cabinet);
//    }
//
//    @PutMapping("/{id}")
//    public Positions update(@PathVariable Integer id,@Valid  @RequestBody Positions cabinet){
//        return service.update(id, cabinet);
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") Integer id){
//        service.deleteById(id);
//        return "Deleted";
//    }
}
