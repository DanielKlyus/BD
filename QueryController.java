package com.nsu.controllers;

import com.nsu.config.ValidationException;
import com.nsu.data.entities.PagedEntitiesResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user-query")
public class QueryController {


    @GetMapping
    public Object tryCustomQuery(@RequestParam("query") String query,
                                 @RequestParam(name ="page") Integer page,
                                 @RequestParam(name ="rowsPerPage") Integer rowsPerPage) {

        if (!query.isEmpty() && !query.split(" ")[0].toLowerCase().equals("select")) {
            throw new ValidationException("Please write 'select' query");
        }

        Connection c;
        Statement stmt;

        List<Object> rows = new ArrayList<>();
        List<Object> row ;
        List<String> headers = new ArrayList<>();
        Integer columnCount = 0;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/pOrg", "postgres", "root");
            c.setAutoCommit(false);
            System.out.println("-- Opened database successfully");
            String sql;


            //--------------- SELECT DATA ------------------
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( query );
            columnCount = rs.getMetaData().getColumnCount();


            for (int i = 0; i < columnCount; i++) {
                String name = rs.getMetaData().getColumnLabel(i+1);
//                if (name.equals("id"))
//                    name =  rs.getMetaData().getColumnLabel(i+1) +"."+ name;
                headers.add(name);
            }


            while ( rs.next() ) {
                row = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    row.add(rs.getObject(i+1));
                }
                rows.add(row);
            }
            rs.close();
            stmt.close();
            c.commit();
            System.out.println("-- Operation SELECT done successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


        int endIndex = Math.min(page * rowsPerPage + rowsPerPage, rows.size());

        return new Response(headers, rows.subList(page * rowsPerPage, endIndex), rows.size());
    }

    @AllArgsConstructor
    @Getter
    @Setter
    class Response {
        private List<String> headers;
        private List<Object> rows;
        private Integer count;
    }


}
