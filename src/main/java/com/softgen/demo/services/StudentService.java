package com.softgen.demo.services;

import com.softgen.demo.dtos.StudentDto;
import com.softgen.demo.dtos.responses.StudentIdResponse;
import com.softgen.demo.dtos.responses.StudentListResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StudentService {

  StudentListResponse getStudents();

  StudentDto getStudentById(UUID id);

  StudentListResponse getStudentByFirstName(String firstName);

  StudentListResponse getStudentByLastName(String lastName);

  StudentDto getStudentByEmail(String email);

  StudentDto getStudentByPersonalNumber(String personalNumber);

  StudentListResponse getStudentByBirthday(LocalDate birthday);

  StudentDto createStudent(StudentDto studentDto);

  StudentIdResponse deleteStudentById(UUID id);

  StudentDto updateStudent(StudentDto studentDto);

}
