package com.ejetool.videoai.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejetool.videoai.domain.entity.CreatomateProject;

///
public interface CreatomateProjectRepository extends JpaRepository<CreatomateProject, Integer> {

    Optional<CreatomateProject> findFirstByEnabled(boolean enabled);
    
    default Optional<CreatomateProject> findFirstByEnabled(){
        return this.findFirstByEnabled(true);
    }
}
