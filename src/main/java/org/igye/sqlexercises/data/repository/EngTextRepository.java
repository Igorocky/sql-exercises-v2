package org.igye.sqlexercises.data.repository;

import org.igye.sqlexercises.model.EngText;
import org.igye.sqlexercises.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EngTextRepository extends JpaRepository<EngText, UUID> {
    EngText findByOwnerAndId(User owner, UUID id);
}
