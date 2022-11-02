package com.softgen.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity(name = "TeacherEntity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "teacher",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"personal_number"}),
                         @UniqueConstraint(columnNames = {"email"})}
)
public class TeacherEntity {

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
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDate birthday;

  @ManyToMany(mappedBy = "teachers",
              fetch = FetchType.LAZY)
  Set<CourseEntity> courses;
}
