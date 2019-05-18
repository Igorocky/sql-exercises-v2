package org.igye.sqlexercises.data.repository;

import org.igye.sqlexercises.model.Node;
import org.igye.sqlexercises.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NodeRepository extends JpaRepository<Node, UUID> {
    List<Node> findByOwnerAndParentNodeIsNullOrderByName(User owner);
    Node findByOwnerAndId(User owner, UUID id);
}
