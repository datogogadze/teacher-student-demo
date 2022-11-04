package com.softgen.demo.services;

import com.softgen.demo.dtos.CourseDto;
import com.softgen.demo.dtos.responses.CourseIdResponse;
import com.softgen.demo.dtos.responses.CourseListResponse;
import com.softgen.demo.dtos.responses.StudentListResponse;
import com.softgen.demo.dtos.responses.TeacherListResponse;
import java.util.UUID;

public interface CourseService {

  CourseListResponse getCourses();

  CourseDto getCourseById(UUID id);

  CourseListResponse getCourseByName(String name);

  CourseDto getCourseByNumber(Integer number);

  CourseDto createCourse(CourseDto courseDto);

  CourseIdResponse deleteCourseById(UUID id);

  CourseDto updateCourse(CourseDto courseDto);

  CourseDto addStudentToCourse(UUID courseId, UUID studentId);

  CourseDto removeStudentFromCourse(UUID courseId, UUID studentId);

  CourseDto addTeacherToCourse(UUID courseId, UUID teacherId);

  CourseDto removeTeacherFromCourse(UUID courseId, UUID teacherId);

  StudentListResponse getCourseStudents(UUID courseId);

  TeacherListResponse gerCourseTeachers(UUID courseId);

}
