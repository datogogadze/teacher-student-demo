package com.softgen.demo.entities;

import java.util.HashSet;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity(name = "StudentEntity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
    name = "student",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"personal_number"}),
                         @UniqueConstraint(columnNames = {"email"})}
)
public class StudentEntity {

  @Id
  @GeneratedValue
  @Column(
      name = "id",
      updatable = false
  )
  private UUID id;

  @Column(
      name = "first_name",
      nullable = false,
      length = 64
  )
  private String firstName;

  @Column(
      name = "last_name",
      nullable = false,
      length = 64
  )
  private String lastName;

  @Column(
      name = "personal_number",
      nullable = false,
      length = 11
  )
  private String personalNumber;

  @Column(
      name = "email",
      nullable = false,
      length = 255
  )
  private String email;

  @Column(
      name = "birthday",
      nullable = false,
      columnDefinition = "DATE"
  )
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthday;

  @ManyToMany(mappedBy = "students")
  Set<CourseEntity> courses = new HashSet<>();

}
