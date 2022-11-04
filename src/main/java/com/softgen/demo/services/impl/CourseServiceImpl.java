package com.softgen.demo.services.impl;

import com.softgen.demo.converters.CourseConverter;
import com.softgen.demo.converters.StudentConverter;
import com.softgen.demo.converters.TeacherConverter;
import com.softgen.demo.dtos.CourseDto;
import com.softgen.demo.dtos.responses.CourseIdResponse;
import com.softgen.demo.dtos.responses.CourseListResponse;
import com.softgen.demo.dtos.responses.StudentListResponse;
import com.softgen.demo.dtos.responses.TeacherListResponse;
import com.softgen.demo.entities.CourseEntity;
import com.softgen.demo.repositories.CourseRepository;
import com.softgen.demo.repositories.StudentRepository;
import com.softgen.demo.repositories.TeacherRepository;
import com.softgen.demo.services.CourseService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final StudentRepository studentRepository;
  private final TeacherRepository teacherRepository;


  @Override
  public CourseListResponse getCourses() {
    var courses = courseRepository.findAll();
    var collect = courses.stream()
                         .map(CourseConverter::toDto)
                         .collect(Collectors.toList());
    return new CourseListResponse(collect);
  }

  @Override
  public CourseDto getCourseById(UUID id) {
    var entity = courseRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Course with id %s not found", id)));
    return CourseConverter.toDto(entity);
  }

  @Override
  public CourseListResponse getCourseByName(String name) {
    var courses = courseRepository.findAllByName(name);
    var collect = courses.stream()
                         .map(CourseConverter::toDto)
                         .collect(Collectors.toList());
    return new CourseListResponse(collect);
  }

  @Override
  public CourseDto getCourseByNumber(Integer number) {
    var entity = courseRepository.findByNumber(number).orElseThrow(
        () -> new EntityNotFoundException(
            String.format("Course with number %s not found", number)));
    return CourseConverter.toDto(entity);
  }

  @Override
  public CourseDto createCourse(CourseDto courseDto) {
    if (courseRepository.findByNumber(courseDto.getNumber()).isPresent()) {
      throw new EntityExistsException(
          String.format("Course with this number already exists: %s", courseDto.getNumber()));
    }
    var entityWithId = courseRepository.save(CourseConverter.toEntity(courseDto));
    return CourseConverter.toDto(entityWithId);
  }

  @Override
  public CourseIdResponse deleteCourseById(UUID id) {
    if (courseRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Course with this id doesn't exist: %s", id));
    }
    courseRepository.deleteById(id);
    return new CourseIdResponse(id);
  }

  @Override
  public CourseDto updateCourse(CourseDto courseDto) {
    var entity = courseRepository.findById(courseDto.getId());
    if (entity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Course with this id doesn't exist: %s", courseDto.getId()));
    }

    CourseEntity courseEntity = entity.get();

    // თუ ნომერი იცვლება ვამოწმებთ სხვა კურსის მონაცემებს ხომ არ ემთხვევა რადგან ეს ფილდეი
    // უნიკალური უნდა იყოს და ბაზიდან ერორი დაბრუნდება, შეიძლებოდა საერთოდ არ მიგვეცა ამ ფილდის
    // შეცვლის უფლება
    if (!Objects.equals(courseEntity.getNumber(), courseDto.getNumber())) {
      if (courseRepository.findByNumber(courseDto.getNumber()).isPresent()) {
        throw new EntityExistsException(
            String.format("Course with this number already exists: %s", courseDto.getNumber()));
      }
    }
    CourseEntity save = courseRepository.save(CourseConverter.toEntity(courseDto));
    return CourseConverter.toDto(save);
  }

  @Override
  public CourseDto addStudentToCourse(UUID courseId, UUID studentId) {
    var courseEntity = courseRepository.findById(courseId);
    if (courseEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Course with this id doesn't exist: %s", courseId));
    }
    var studentEntity = studentRepository.findById(studentId);
    if (studentEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Student with this id doesn't exist: %s", studentId));
    }
    CourseEntity foundCourse = courseEntity.get();
    foundCourse.addStudent(studentEntity.get());
    courseRepository.save(foundCourse);
    return CourseConverter.toDto(foundCourse);
  }

  @Override
  public CourseDto removeStudentFromCourse(UUID courseId, UUID studentId) {
    var courseEntity = courseRepository.findById(courseId);
    if (courseEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Course with this id doesn't exist: %s", courseId));
    }
    var studentEntity = studentRepository.findById(studentId);
    if (studentEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Student with this id doesn't exist: %s", studentId));
    }
    CourseEntity foundCourse = courseEntity.get();
    foundCourse.removeStudent(studentEntity.get());
    courseRepository.save(foundCourse);
    return CourseConverter.toDto(foundCourse);
  }

  @Override
  public CourseDto addTeacherToCourse(UUID courseId, UUID teacherId) {
    var courseEntity = courseRepository.findById(courseId);
    if (courseEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Course with this id doesn't exist: %s", courseId));
    }
    var teacherEntity = teacherRepository.findById(teacherId);
    if (teacherEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Teacher with this id doesn't exist: %s", teacherId));
    }
    CourseEntity foundCourse = courseEntity.get();
    foundCourse.addTeacher(teacherEntity.get());
    courseRepository.save(foundCourse);
    return CourseConverter.toDto(foundCourse);
  }

  @Override
  public CourseDto removeTeacherFromCourse(UUID courseId, UUID teacherId) {
    var courseEntity = courseRepository.findById(courseId);
    if (courseEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Course with this id doesn't exist: %s", courseId));
    }
    var teacherEntity = teacherRepository.findById(teacherId);
    if (teacherEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Teacher with this id doesn't exist: %s", teacherId));
    }
    CourseEntity foundCourse = courseEntity.get();
    foundCourse.removeTeacher(teacherEntity.get());
    courseRepository.save(foundCourse);
    return CourseConverter.toDto(foundCourse);
  }

  @Override
  public StudentListResponse getCourseStudents(UUID courseId) {
    var courseEntity = courseRepository.findById(courseId);
    if (courseEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Course with this id doesn't exist: %s", courseId));
    }
    return new StudentListResponse(
        courseEntity.get().getStudents()
                    .stream()
                    .map(StudentConverter::toDto)
                    .collect(Collectors.toList()));
  }

  @Override
  public TeacherListResponse gerCourseTeachers(UUID courseId) {
    var courseEntity = courseRepository.findById(courseId);
    if (courseEntity.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Course with this id doesn't exist: %s", courseId));
    }
    return new TeacherListResponse(
        courseEntity.get().getTeachers()
                    .stream()
                    .map(TeacherConverter::toDto)
                    .collect(Collectors.toList()));
  }


}
