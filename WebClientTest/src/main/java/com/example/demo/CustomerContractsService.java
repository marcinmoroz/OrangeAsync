package com.example.demo;

import org.springframework.web.bind.annotation.*;


import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface CustomerContractsService {
    @RequestMapping(method = GET)
    String getCustomerAcount();
}
