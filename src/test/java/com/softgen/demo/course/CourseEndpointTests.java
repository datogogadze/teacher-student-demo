package com.softgen.demo.course;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softgen.demo.dtos.CourseDto;
import com.softgen.demo.dtos.StudentDto;
import com.softgen.demo.dtos.TeacherDto;
import com.softgen.demo.services.CourseService;
import com.softgen.demo.services.StudentService;
import com.softgen.demo.services.TeacherService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CourseEndpointTests {

  @Autowired
  private CourseService courseService;

  @Autowired
  private StudentService studentService;

  @Autowired
  private TeacherService teacherService;

  private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  @Autowired
  MockMvc mockMvc;

  private CourseDto course = CourseDto.builder()
                                      .name("course5")
                                      .number(5)
                                      .build();

  private CourseDto courseToDelete = CourseDto.builder()
                                              .name("course6")
                                              .number(6)
                                              .build();

  private CourseDto courseToUpdate = CourseDto.builder()
                                              .name("course7")
                                              .number(7)
                                              .build();

  private StudentDto student = StudentDto.builder()
                                         .firstName("student")
                                         .lastName("student")
                                         .email("student@email.com")
                                         .personalNumber("12345678915")
                                         .birthday(LocalDate.now())
                                         .build();

  private StudentDto deleteStudent = StudentDto.builder()
                                               .firstName("deleteStudent")
                                               .lastName("deleteStudent")
                                               .email("deleteStudent@email.com")
                                               .personalNumber("12345678916")
                                               .birthday(LocalDate.now())
                                               .build();

  private TeacherDto teacher = TeacherDto.builder()
                                         .firstName("teacher")
                                         .lastName("teacher")
                                         .email("teacher@email.com")
                                         .personalNumber("12345678917")
                                         .birthday(LocalDate.now())
                                         .build();

  private TeacherDto deleteTeacher = TeacherDto.builder()
                                               .firstName("deleteTeacher")
                                               .lastName("deleteTeacher")
                                               .email("deleteTeacher@email.com")
                                               .personalNumber("12345678918")
                                               .birthday(LocalDate.now())
                                               .build();

  @BeforeAll
  public void setUp() {
    var createdEntity = courseService.createCourse(course);
    course.setId(createdEntity.getId());

    createdEntity = courseService.createCourse(courseToDelete);
    courseToDelete.setId(createdEntity.getId());

    createdEntity = courseService.createCourse(courseToUpdate);
    courseToUpdate.setId(createdEntity.getId());

    var saved = studentService.createStudent(student);
    student.setId(saved.getId());
    saved = studentService.createStudent(deleteStudent);
    deleteStudent.setId(saved.getId());

    var saved1 = teacherService.createTeacher(teacher);
    teacher.setId(saved1.getId());
    saved1 = teacherService.createTeacher(deleteTeacher);
    deleteTeacher.setId(saved1.getId());
  }

  @Test
  void getAllTest() throws Exception {
    this.mockMvc.perform(
            get("/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // at least course and courseToUpdate, maybe courseToDelete too, maybe 4 if
                // create course test is run first.
                .andExpect(jsonPath("$.courses", hasSize(greaterThanOrEqualTo(2))))
                .andReturn();
  }

  @Test
  void testCreate() throws Exception {
    CourseDto course8 = CourseDto.builder()
                                 .name("course8")
                                 .number(8)
                                 .build();
    this.mockMvc.perform(
            post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(course8)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(course8.getName()))
                .andReturn();
  }

  @Test
  void testGetById() throws Exception {
    this.mockMvc.perform(
            get("/courses/id/{id}", course.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andReturn();
  }

  @Test
  void testGetByName() throws Exception {
    this.mockMvc.perform(
            get("/courses/name/{name}", course.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses", hasSize(1)))
                .andReturn();
  }

  @Test
  void testGetByNumber() throws Exception {
    this.mockMvc.perform(
            get("/courses/number/{number}", course.getNumber())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andReturn();
  }

  @Test
  void testUpdate() throws Exception {
    courseToUpdate.setName("changed");
    this.mockMvc.perform(
            put("/courses/{id}", courseToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(courseToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("changed"))
                .andReturn();
  }

  @Test
  void testCoursesStudents() throws Exception {
    courseService.addStudentToCourse(course.getId(), student.getId());
    this.mockMvc.perform(
            get("/courses/{id}/students", course.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andReturn();
    assertTrue(courseService.getCourseStudents(course.getId()).getStudents().stream()
                            .anyMatch(x -> x.getEmail().equals(student.getEmail())));
  }

  @Test
  void testCoursesTeachers() throws Exception {
    courseService.addTeacherToCourse(course.getId(), teacher.getId());
    this.mockMvc.perform(
            get("/courses/{id}/students", course.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andReturn();
    assertTrue(courseService.gerCourseTeachers(course.getId()).getTeachers().stream()
                            .anyMatch(x -> x.getEmail().equals(teacher.getEmail())));
  }

  @Test
  void testDeleteStudentFromCourse() throws Exception {
    courseService.addStudentToCourse(course.getId(), deleteStudent.getId());
    assertTrue(courseService.getCourseStudents(course.getId()).getStudents().stream()
                            .anyMatch(x -> x.getEmail().equals(deleteStudent.getEmail())));
    this.mockMvc.perform(
            delete("/courses/{id}/student/{id}", course.getId(), deleteStudent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk())
                .andReturn();

    assertFalse(courseService.getCourseStudents(course.getId()).getStudents().stream()
                             .anyMatch(x -> x.getEmail().equals(deleteStudent.getEmail())));

  }

  @Test
  void testDeleteTeacherFromCourse() throws Exception {
    courseService.addTeacherToCourse(course.getId(), deleteTeacher.getId());
    assertTrue(courseService.gerCourseTeachers(course.getId()).getTeachers().stream()
                            .anyMatch(x -> x.getEmail().equals(deleteTeacher.getEmail())));
    this.mockMvc.perform(
            delete("/courses/{id}/teacher/{id}", course.getId(), deleteTeacher.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk())
                .andReturn();

    assertFalse(courseService.gerCourseTeachers(course.getId()).getTeachers().stream()
                             .anyMatch(x -> x.getEmail().equals(deleteTeacher.getEmail())));
  }

  @Test
  void testDeleteCourse() throws Exception {
    this.mockMvc.perform(
            delete("/courses/{id}", courseToDelete.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(courseToDelete)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course_id").value(courseToDelete.getId().toString()))
                .andReturn();
  }

}
