package com.example.demorest;


import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

  @Id
    private int uid;
    private String name;
    private String address;
    private int age;


}
