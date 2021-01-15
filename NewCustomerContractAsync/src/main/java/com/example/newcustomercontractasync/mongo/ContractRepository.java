package com.example.newcustomercontractasync.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContractRepository extends MongoRepository<ContracDAO, String> {
}
