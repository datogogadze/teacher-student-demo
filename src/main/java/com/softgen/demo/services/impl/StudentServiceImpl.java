package com.softgen.demo.services.impl;

import com.softgen.demo.converters.CourseConverter;
import com.softgen.demo.converters.StudentConverter;
import com.softgen.demo.dtos.StudentDto;
import com.softgen.demo.dtos.responses.CourseListResponse;
import com.softgen.demo.dtos.responses.StudentIdResponse;
import com.softgen.demo.entities.StudentEntity;
import com.softgen.demo.repositories.CourseRepository;
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
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;
  private final CourseRepository courseRepository;

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

  private void removeStudentFromCourses(StudentEntity student) {
    for (var course : student.getCourses()) {
      course.removeStudent(student);
      courseRepository.save(course);
    }
  }

  @Override
  public StudentIdResponse deleteStudentById(UUID id) {
    var found = studentRepository.findById(id);
    if (found.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Student with this id doesn't exist: %s", id));
    }
    // ????????????????????????????????? ??????????????? ??????????????????????????????
    removeStudentFromCourses(found.get());
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

    // ?????? ?????????????????? ?????? ?????????????????? ?????????????????? ????????????????????? ??????????????????????????? ???????????? ??????????????????????????? ?????????????????????????????? ????????? ?????? ????????????????????????
    // ?????????????????? ?????? ????????????????????? ??????????????????????????? ???????????? ???????????? ?????? ????????????????????? ??????????????? ??????????????????????????????, ?????????????????????????????? ????????????????????? ??????
    // ????????????????????? ?????? ???????????????????????? ????????????????????? ??????????????????
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

  @Override
  public CourseListResponse getStudentCourses(UUID studentId) {
    var studentEntity = studentRepository.findById(studentId);
    if (studentEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Student with this id doesn't exist: %s", studentId));
    }
    return new CourseListResponse(
        studentEntity.get().getCourses()
                     .stream()
                     .map(CourseConverter::toDto)
                     .collect(Collectors.toList()));
  }


}
