package com.softgen.demo.repositories;

import com.softgen.demo.entities.CourseEntity;
import com.softgen.demo.entities.TeacherEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {

  @Query("SELECT course FROM CourseEntity course WHERE course.name = ?1")
  List<CourseEntity> findAllByName(String name);

  @Query("SELECT course FROM CourseEntity course WHERE course.number = ?1")
  Optional<CourseEntity> findByNumber(Integer number);
}
