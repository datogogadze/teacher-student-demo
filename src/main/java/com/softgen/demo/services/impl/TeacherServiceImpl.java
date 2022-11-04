package com.softgen.demo.services.impl;

import com.softgen.demo.converters.CourseConverter;
import com.softgen.demo.converters.TeacherConverter;
import com.softgen.demo.dtos.TeacherDto;
import com.softgen.demo.dtos.responses.CourseListResponse;
import com.softgen.demo.dtos.responses.TeacherIdResponse;
import com.softgen.demo.dtos.responses.TeacherListResponse;
import com.softgen.demo.entities.TeacherEntity;
import com.softgen.demo.repositories.CourseRepository;
import com.softgen.demo.repositories.TeacherRepository;
import com.softgen.demo.services.TeacherService;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TeacherServiceImpl implements TeacherService {

  private final TeacherRepository teacherRepository;
  private final CourseRepository courseRepository;

  @Override
  public TeacherListResponse getTeachers() {
    var teachers = teacherRepository.findAll();
    var collect = teachers.stream()
                          .map(TeacherConverter::toDto)
                          .collect(Collectors.toList());
    return new TeacherListResponse(collect);
  }

  @Override
  public TeacherDto getTeacherById(UUID id) {
    var entity = teacherRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Teacher with id %s not found", id)));
    return TeacherConverter.toDto(entity);
  }

  @Override
  public TeacherListResponse getTeacherByFirstName(String firstName) {
    var teachers = teacherRepository.findAllByFirstName(firstName);
    var collect = teachers.stream()
                          .map(TeacherConverter::toDto)
                          .collect(Collectors.toList());
    return new TeacherListResponse(collect);
  }

  @Override
  public TeacherListResponse getTeacherByLastName(String lastName) {
    var teachers = teacherRepository.findAllByLastName(lastName);
    var collect = teachers.stream()
                          .map(TeacherConverter::toDto)
                          .collect(Collectors.toList());
    return new TeacherListResponse(collect);
  }

  @Override
  public TeacherDto getTeacherByEmail(String email) {
    var entity = teacherRepository.findByEmail(email).orElseThrow(
        () -> new EntityNotFoundException(String.format("Teacher with email %s not found", email)));
    return TeacherConverter.toDto(entity);
  }

  @Override
  public TeacherDto getTeacherByPersonalNumber(String personalNumber) {
    var entity = teacherRepository.findByPersonalNumber(personalNumber).orElseThrow(
        () -> new EntityNotFoundException(
            String.format("Teacher with personal number %s not found", personalNumber)));
    return TeacherConverter.toDto(entity);
  }

  @Override
  public TeacherListResponse getTeacherByBirthday(LocalDate birthday) {
    var teachers = teacherRepository.findAllByBirthday(birthday);
    var collect = teachers.stream()
                          .map(TeacherConverter::toDto)
                          .collect(Collectors.toList());
    return new TeacherListResponse(collect);
  }

  @Override
  public TeacherDto createTeacher(TeacherDto teacherDto) {
    if (teacherRepository.findByEmail(teacherDto.getEmail()).isPresent()) {
      throw new EntityExistsException(
          String.format("Teacher with this email already exists: %s", teacherDto.getEmail()));
    }
    if (teacherRepository.findByPersonalNumber(teacherDto.getPersonalNumber()).isPresent()) {
      throw new EntityExistsException(
          String.format("Teacher with this personal number already exists: %s",
                        teacherDto.getPersonalNumber()));
    }
    var entityWithId = teacherRepository.save(TeacherConverter.toEntity(teacherDto));
    return TeacherConverter.toDto(entityWithId);
  }

  private void removeTeacherFromCourses(TeacherEntity teacher) {
    for (var course : teacher.getCourses()) {
      course.removeTeacher(teacher);
      courseRepository.save(course);
    }
  }

  @Override
  public TeacherIdResponse deleteTeacherById(UUID id) {
    Optional<TeacherEntity> found = teacherRepository.findById(id);
    if (found.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Teacher with this id doesn't exist: %s", id));
    }
    // მასწავლებლების წაშლა ჯგუფებიდან
    removeTeacherFromCourses(found.get());
    teacherRepository.deleteById(id);
    return new TeacherIdResponse(id);
  }

  @Override
  public TeacherDto updateTeacher(TeacherDto teacherDto) {
    var entity = teacherRepository.findById(teacherDto.getId());
    if (entity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Teacher with this id doesn't exist: %s", teacherDto.getId()));
    }

    TeacherEntity teacherEntity = entity.get();

    // თუ იმეილი ან პირადი ნომერი იცვლება ვამოწმებთ სხვა მასწავლებლის მონაცემებს ხომ არ ემთხვევა
    // რადგან ეს ფილდები უნიკალური უნდა იყოს და ბაზიდან ერორი დაბრუნდება, შეიძლებოდა საერთოდ არ
    // მიგვეცა ამ ფილდების შეცვლის უფლება
    if (!Objects.equals(teacherEntity.getEmail(), teacherDto.getEmail())) {
      if (teacherRepository.findByEmail(teacherDto.getEmail()).isPresent()) {
        throw new EntityExistsException(
            String.format("Teacher with this email already exists: %s", teacherDto.getEmail()));
      }
    }
    if (!Objects.equals(teacherEntity.getPersonalNumber(), teacherDto.getPersonalNumber())) {
      if (teacherRepository.findByPersonalNumber(teacherDto.getPersonalNumber()).isPresent()) {
        throw new EntityExistsException(
            String.format("Teacher with this personal number already exists: %s",
                          teacherDto.getPersonalNumber()));
      }
    }
    TeacherEntity save = teacherRepository.save(TeacherConverter.toEntity(teacherDto));
    return TeacherConverter.toDto(save);
  }

  @Override
  public CourseListResponse getTeacherCourses(UUID teacherId) {
    var teacherEntity = teacherRepository.findById(teacherId);
    if (teacherEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Teacher with this id doesn't exist: %s", teacherId));
    }
    return new CourseListResponse(
        teacherEntity.get().getCourses()
                     .stream()
                     .map(CourseConverter::toDto)
                     .collect(Collectors.toList()));
  }

}
