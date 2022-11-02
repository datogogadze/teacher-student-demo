package com.softgen.demo.services.impl;

import com.softgen.demo.converters.StudentConverter;
import com.softgen.demo.dtos.StudentDto;
import com.softgen.demo.dtos.responses.StudentIdResponse;
import com.softgen.demo.entities.StudentEntity;
import com.softgen.demo.repositories.StudentRepository;
import com.softgen.demo.dtos.responses.StudentListResponse;
import com.softgen.demo.services.StudentService;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;

  @Override
  public StudentListResponse getStudents() {
    var students = studentRepository.findAll();
    var collect = students.stream()
                          .map(StudentConverter::toDto)
                          .collect(Collectors.toList());
    return new StudentListResponse(collect);
  }

  @Override
  public StudentDto getStudentById(UUID id) {
    var entity = studentRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Student with id %s not found", id)));
    return StudentConverter.toDto(entity);
  }

  @Override
  public StudentListResponse getStudentByFirstName(String firstName) {
    var students = studentRepository.findAllByFirstName(firstName);
    var collect = students.stream()
                          .map(StudentConverter::toDto)
                          .collect(Collectors.toList());
    return new StudentListResponse(collect);
  }

  @Override
  public StudentListResponse getStudentByLastName(String lastName) {
    var students = studentRepository.findAllByLastName(lastName);
    var collect = students.stream()
                          .map(StudentConverter::toDto)
                          .collect(Collectors.toList());
    return new StudentListResponse(collect);
  }

  @Override
  public StudentDto getStudentByEmail(String email) {
    var entity = studentRepository.findByEmail(email).orElseThrow(
        () -> new EntityNotFoundException(String.format("Student with email %s not found", email)));
    return StudentConverter.toDto(entity);
  }

  @Override
  public StudentDto getStudentByPersonalNumber(String personalNumber) {
    var entity = studentRepository.findByPersonalNumber(personalNumber).orElseThrow(
        () -> new EntityNotFoundException(
            String.format("Student with personal number %s not found", personalNumber)));
    return StudentConverter.toDto(entity);
  }

  @Override
  public StudentListResponse getStudentByBirthday(LocalDate birthday) {
    var students = studentRepository.findAllByBirthday(birthday);
    var collect = students.stream()
                          .map(StudentConverter::toDto)
                          .collect(Collectors.toList());
    return new StudentListResponse(collect);
  }

  @Override
  public StudentDto createStudent(StudentDto studentDto) {
    if (studentRepository.findByEmail(studentDto.getEmail()).isPresent()) {
      throw new EntityExistsException(
          String.format("Student with this email already exists: %s", studentDto.getEmail()));
    }
    if (studentRepository.findByPersonalNumber(studentDto.getPersonalNumber()).isPresent()) {
      throw new EntityExistsException(
          String.format("Student with this personal number already exists: %s",
                        studentDto.getPersonalNumber()));
    }
    var entityWithId = studentRepository.save(StudentConverter.toEntity(studentDto));
    return StudentConverter.toDto(entityWithId);
  }

  @Override
  public StudentIdResponse deleteStudentById(UUID id) {
    if (studentRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Student with this id doesn't exist: %s", id));
    }
    studentRepository.deleteById(id);
    return new StudentIdResponse(id);
  }

  @Override
  public StudentDto updateStudent(StudentDto studentDto) {
    var entity = studentRepository.findById(studentDto.getId());
    if (entity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Student with this id doesn't exist: %s", studentDto.getId()));
    }
    
    StudentEntity studentEntity = entity.get();

    // თუ იმეილი ან პირადი ნომერი იცვლება ვამოწმებთ სხვა სტუდენტის მონაცემებს ხომ არ ემთხვევა
    // რადგან ეს ფილდები უნიკალური უნდა იყოს და ბაზიდან ერორი დაბრუნდება ხომ არ ემთხვევეა,
    // შეიძლებოდა საერთოდ არ ამ ფილდების შეცვლის უფლება
    if (!Objects.equals(studentEntity.getEmail(), studentDto.getEmail())) {
      if (studentRepository.findByEmail(studentDto.getEmail()).isPresent()) {
        throw new EntityExistsException(
            String.format("Student with this email already exists: %s", studentDto.getEmail()));
      }
    }
    if (!Objects.equals(studentEntity.getPersonalNumber(), studentDto.getPersonalNumber())) {
      if (studentRepository.findByPersonalNumber(studentDto.getPersonalNumber()).isPresent()) {
        throw new EntityExistsException(
            String.format("Student with this personal number already exists: %s",
                          studentDto.getPersonalNumber()));
      }
    }
    StudentEntity save = studentRepository.save(StudentConverter.toEntity(studentDto));
    return StudentConverter.toDto(save);
  }

}
