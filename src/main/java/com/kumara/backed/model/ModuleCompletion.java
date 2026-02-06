package com.kumara.backed.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "module_completions")
public class ModuleCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long moduleId;
}