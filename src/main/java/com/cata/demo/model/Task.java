package com.cata.demo.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="t_task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name cannot be null")
    private String name;
    private String description;

    @NotBlank(message = "Status is mandatory")
    @NotNull(message = "Status cannot be null")
    private String status; // IN PROGRESS - COMPLETED - NEW
}
