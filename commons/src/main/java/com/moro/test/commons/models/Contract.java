package com.moro.test.commons.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contract {
    int id;
    String number;
}
