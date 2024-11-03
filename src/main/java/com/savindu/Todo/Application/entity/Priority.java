package com.savindu.Todo.Application.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name ="priority")
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name" ,length = 255)
    private String name;

    @Column(name = "code" ,length = 255)
    private String code;
}
