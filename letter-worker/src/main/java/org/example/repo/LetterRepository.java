package org.example.repo;

import org.example.entities.LetterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LetterRepository extends JpaRepository<LetterEntity, UUID> {}

