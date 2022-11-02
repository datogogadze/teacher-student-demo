package com.softgen.demo.repositories;

import com.softgen.demo.entities.TeacherEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepository extends JpaRepository<TeacherEntity, UUID> {

  @Query("SELECT teacher FROM TeacherEntity teacher WHERE teacher.firstName = ?1")
  List<TeacherEntity> findAllByFirstName(String firstName);

  @Query("SELECT teacher FROM TeacherEntity teacher WHERE teacher.lastName = ?1")
  List<TeacherEntity> findAllByLastName(String lastName);

  @Query("SELECT teacher FROM TeacherEntity teacher WHERE teacher.personalNumber = ?1")
  Optional<TeacherEntity> findByPersonalNumber(String personalNumber);

  @Query("SELECT teacher FROM TeacherEntity teacher WHERE teacher.email = ?1")
  Optional<TeacherEntity> findByEmail(String email);

  @Query("SELECT teacher FROM TeacherEntity teacher WHERE teacher.birthday = ?1")
  List<TeacherEntity> findAllByBirthday(LocalDate birthday);

}
