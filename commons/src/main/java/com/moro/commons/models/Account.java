package com.moro.commons.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Account {
    int id;
    String number;
    List<Contract> contracts;
}
