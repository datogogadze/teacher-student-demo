package com.softgen.demo.controllers;

import com.softgen.demo.dtos.CourseDto;
import com.softgen.demo.dtos.responses.CourseIdResponse;
import com.softgen.demo.dtos.responses.CourseListResponse;
import com.softgen.demo.dtos.responses.StudentListResponse;
import com.softgen.demo.dtos.responses.TeacherListResponse;
import com.softgen.demo.services.CourseService;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("courses")
public class CourseController {

  private final CourseService courseService;

  @GetMapping
  public CourseListResponse getCourses() {
    return courseService.getCourses();
  }

  @GetMapping("/id/{id}")
  public CourseDto getCourseById(@PathVariable("id") UUID id) {
    return courseService.getCourseById(id);
  }

  @GetMapping("/name/{name}")
  public CourseListResponse getCourseByName(@PathVariable("name") String firstName) {
    return courseService.getCourseByName(firstName);
  }

  @GetMapping("/number/{number}")
  public CourseDto getCourseByNumber(@PathVariable("number") Integer number) {
    return courseService.getCourseByNumber(number);
  }

  @PostMapping
  public CourseDto createCourse(@RequestBody @Valid CourseDto courseDto) {
    return courseService.createCourse(courseDto);
  }

  // Patch რექუესთის გაკეთებაც შეიძლებოდა, მაგრამ სიმარტივისთვის იყოს Put.
  @PutMapping("/{id}")
  public CourseDto updateCourse(@PathVariable("id") UUID id,
                                @RequestBody @Valid CourseDto courseDto
                               ) {
    courseDto.setId(id);
    return courseService.updateCourse(courseDto);
  }

  @DeleteMapping("/{id}")
  public CourseIdResponse deleteCourseById(@PathVariable("id") UUID id) {
    return courseService.deleteCourseById(id);
  }

  @PostMapping("/{course_id}/student/{student_id}")
  public CourseDto addStudentToCourse(@PathVariable("course_id") UUID courseId,
                                      @PathVariable("student_id") UUID studentId
                                     ) {
    return courseService.addStudentToCourse(courseId, studentId);
  }

  @DeleteMapping("/{course_id}/student/{student_id}")
  public CourseDto removeStudentFromCourse(@PathVariable("course_id") UUID courseId,
                                           @PathVariable("student_id") UUID studentId
                                          ) {
    return courseService.removeStudentFromCourse(courseId, studentId);
  }

  @PostMapping("/{course_id}/teacher/{teacher_id}")
  public CourseDto addTeacherToCourse(@PathVariable("course_id") UUID courseId,
                                      @PathVariable("teacher_id") UUID teacherId
                                     ) {
    return courseService.addTeacherToCourse(courseId, teacherId);
  }

  @DeleteMapping("/{course_id}/teacher/{teacher_id}")
  public CourseDto removeTeacherFromCourse(@PathVariable("course_id") UUID courseId,
                                           @PathVariable("teacher_id") UUID teacherId
                                          ) {
    return courseService.removeTeacherFromCourse(courseId, teacherId);
  }

  @GetMapping("/{id}/students")
  public StudentListResponse getCourseStudents(@PathVariable("id") UUID courseId) {
    return courseService.getCourseStudents(courseId);
  }

  @GetMapping("/{id}/teachers")
  public TeacherListResponse getCourseTeachers(@PathVariable("id") UUID courseId) {
    return courseService.gerCourseTeachers(courseId);
  }
}
