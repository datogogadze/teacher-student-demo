package com.softgen.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "CourseEntity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course")
public class CourseEntity {

  @Id
  @GeneratedValue
  @Column(
      name = "id",
      updatable = false
  )
  private UUID id;

  @Column(
      name = "name",
      nullable = false,
      length = 64
  )
  private String name;

  @Column(
      name = "number",
      nullable = false
  )
  private Integer number;

  @ManyToMany
  @JoinTable(
      name = "student_course",
      joinColumns = @JoinColumn(name = "course_id"),
      inverseJoinColumns = @JoinColumn(name = "student_id"))
  @JsonIgnore
  Set<StudentEntity> students;

  @ManyToMany
  @JoinTable(
      name = "teacher_course",
      joinColumns = @JoinColumn(name = "course_id"),
      inverseJoinColumns = @JoinColumn(name = "teacher_id"))
  @JsonIgnore
  Set<TeacherEntity> teachers;
}
