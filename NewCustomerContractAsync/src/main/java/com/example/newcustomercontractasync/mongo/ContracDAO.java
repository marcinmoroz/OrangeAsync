package com.example.newcustomercontractasync.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class ContracDAO {
    @Id
    private String id;

    private String contracNumber;
}
