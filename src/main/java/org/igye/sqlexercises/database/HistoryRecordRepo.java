package org.igye.sqlexercises.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HistoryRecordRepo extends JpaRepository<HistoryRecord, UUID> {
    List<HistoryRecord> findByExerciseIdOrderByDateTimeDesc(String exerciseId);
}
