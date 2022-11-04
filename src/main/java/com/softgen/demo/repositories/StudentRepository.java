package com.softgen.demo.repositories;

import com.softgen.demo.entities.StudentEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {

  @Query("SELECT student FROM StudentEntity student WHERE student.firstName = ?1")
  List<StudentEntity> findAllByFirstName(String firstName);

  @Query("SELECT student FROM StudentEntity student WHERE student.lastName = ?1")
  List<StudentEntity> findAllByLastName(String lastName);

  @Query("SELECT student FROM StudentEntity student WHERE student.personalNumber = ?1")
  Optional<StudentEntity> findByPersonalNumber(String personalNumber);

  @Query("SELECT student FROM StudentEntity student WHERE student.email = ?1")
  Optional<StudentEntity> findByEmail(String email);

  @Query("SELECT student FROM StudentEntity student WHERE student.birthday = ?1")
  List<StudentEntity> findAllByBirthday(LocalDate birthday);

}
