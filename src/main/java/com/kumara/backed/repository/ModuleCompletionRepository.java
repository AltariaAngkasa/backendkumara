package com.kumara.backed.repository;

import com.kumara.backed.model.ModuleCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ModuleCompletionRepository extends JpaRepository<ModuleCompletion, Long> {
    Optional<ModuleCompletion> findByUserIdAndModuleId(Long userId, Long moduleId);
}