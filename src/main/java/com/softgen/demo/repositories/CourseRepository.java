package com.softgen.demo.repositories;

import com.softgen.demo.entities.CourseEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {
}
