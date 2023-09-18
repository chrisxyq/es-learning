package com.chrisxyq.esjava;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String  name;
    private String  sex;
    private Integer age;
}
