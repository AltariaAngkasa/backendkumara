package com.kumara.backed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kumara.backed.model.Module;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    // Custom query bisa ditambahkan di sini jika diperlukan
}
