package org.igye.sqlexercises.data.repository;

import org.igye.sqlexercises.model.Paragraph;
import org.igye.sqlexercises.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, UUID> {
    Paragraph findByOwnerAndId(User owner, UUID id);
}
