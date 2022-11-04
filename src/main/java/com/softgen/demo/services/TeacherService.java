package com.softgen.demo.services;

import com.softgen.demo.dtos.TeacherDto;
import com.softgen.demo.dtos.responses.CourseListResponse;
import com.softgen.demo.dtos.responses.TeacherIdResponse;
import com.softgen.demo.dtos.responses.TeacherListResponse;
import java.time.LocalDate;
import java.util.UUID;

public interface TeacherService {

  TeacherListResponse getTeachers();

  TeacherDto getTeacherById(UUID id);

  TeacherListResponse getTeacherByFirstName(String firstName);

  TeacherListResponse getTeacherByLastName(String lastName);

  TeacherDto getTeacherByEmail(String email);

  TeacherDto getTeacherByPersonalNumber(String personalNumber);

  TeacherListResponse getTeacherByBirthday(LocalDate birthday);

  TeacherDto createTeacher(TeacherDto teacherDto);

  TeacherIdResponse deleteTeacherById(UUID id);

  TeacherDto updateTeacher(TeacherDto teacherDto);

  CourseListResponse getTeacherCourses(UUID teacherId);

}
