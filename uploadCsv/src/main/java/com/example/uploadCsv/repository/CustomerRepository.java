package com.example.uploadCsv.repository;

import com.example.uploadCsv.document.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, Long > {
}
