package com.softgen.demo.entities;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import lombok.Setter;

@Entity(name = "CourseEntity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
      nullable = false
  )
  private String name;

  @Column(
      name = "number",
      nullable = false,
      unique = true
  )
  private Integer number;

  @ManyToMany
  @JoinTable(
      name = "student_course",
      joinColumns = @JoinColumn(name = "course_id"),
      inverseJoinColumns = @JoinColumn(name = "student_id"))
  Set<StudentEntity> students;

  // აქ ჩავთვალე რომ ერთ ჯგუფს/კურსს შეიძლება რამდენიმე მასწავლებელი ყავდეს
  @ManyToMany
  @JoinTable(
      name = "teacher_course",
      joinColumns = @JoinColumn(name = "course_id"),
      inverseJoinColumns = @JoinColumn(name = "teacher_id"))
  Set<TeacherEntity> teachers;

  public void addStudent(StudentEntity student) {
    this.students.add(student);
  }

  public void removeStudent(StudentEntity student) {
    this.students.remove(student);
  }

  public void addTeacher(TeacherEntity teacher) {
    this.teachers.add(teacher);
  }

  public void removeTeacher(TeacherEntity teacher) {
    this.teachers.remove(teacher);
  }


}
