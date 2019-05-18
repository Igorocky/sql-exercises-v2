package org.igye.sqlexercises.data.repository;

import org.igye.sqlexercises.model.Image;
import org.igye.sqlexercises.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    Image findByOwnerAndId(User owner, UUID id);
}
