package com.softgen.demo.repositories;

import com.softgen.demo.entities.TeacherEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<TeacherEntity, UUID> {
}
