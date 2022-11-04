package com.softgen.demo.teacher;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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
import com.softgen.demo.dtos.TeacherDto;
import com.softgen.demo.services.CourseService;
import com.softgen.demo.services.TeacherService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
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
public class TeacherEndpointTests {

  @Autowired
  private TeacherService teacherService;

  @Autowired
  private CourseService courseService;

  private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  private TeacherDto entity = TeacherDto.builder()
                                        .firstName("fname")
                                        .lastName("lname")
                                        .email("email@email.com")
                                        .personalNumber("12345678910")
                                        .birthday(LocalDate.now())
                                        .build();

  private TeacherDto entityToDelete = TeacherDto.builder()
                                                .firstName("delete")
                                                .lastName("delete")
                                                .email("delete@email.com")
                                                .personalNumber("12345678912")
                                                .birthday(LocalDate.now())
                                                .build();

  private TeacherDto entityToUpdate = TeacherDto.builder()
                                                .firstName("update")
                                                .lastName("update")
                                                .email("update@email.com")
                                                .personalNumber("12345678913")
                                                .birthday(LocalDate.now())
                                                .build();

  private CourseDto courseEntity = CourseDto.builder()
                                            .name("course3")
                                            .number(3)
                                            .build();

  private CourseDto courseToDelete = CourseDto.builder()
                                              .name("course4")
                                              .number(4)
                                              .build();
  @Autowired
  MockMvc mockMvc;

  @BeforeAll
  public void setUp() {
    var createdEntity = teacherService.createTeacher(entity);
    entity.setId(createdEntity.getId());

    createdEntity = teacherService.createTeacher(entityToDelete);
    entityToDelete.setId(createdEntity.getId());

    createdEntity = teacherService.createTeacher(entityToUpdate);
    entityToUpdate.setId(createdEntity.getId());

    CourseDto saved = courseService.createCourse(courseEntity);
    courseEntity.setId(saved.getId());

    saved = courseService.createCourse(courseToDelete);
    courseToDelete.setId(saved.getId());
  }

  @Test
  void getAllTest() throws Exception {
    this.mockMvc.perform(
            get("/teachers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // at least entity and entityToUpdate, maybe entityToDelete too, maybe 4 if
                // create teacher test is run first.
                .andExpect(jsonPath("$.teachers", hasSize(greaterThanOrEqualTo(2))))
                .andReturn();

  }

  @Test
  void testCreate() throws Exception {
    TeacherDto entity1 = TeacherDto.builder()
                                   .firstName("fname1")
                                   .lastName("lname1")
                                   .email("email1@email.com")
                                   .personalNumber("12345678914")
                                   .birthday(LocalDate.now())
                                   .build();
    this.mockMvc.perform(
            post("/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(entity1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(entity1.getFirstName()))
                .andReturn();
  }

  @Test
  void testGetById() throws Exception {
    this.mockMvc.perform(
            get("/teachers/id/{id}", entity.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(entity.getFirstName()))
                .andReturn();
  }

  @Test
  void testGetByFirstName() throws Exception {
    this.mockMvc.perform(
            get("/teachers/first_name/{first_name}", entity.getFirstName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teachers", hasSize(1)))
                .andReturn();
  }

  @Test
  void testGetByLastName() throws Exception {
    this.mockMvc.perform(
            get("/teachers/last_name/{last_name}", entity.getLastName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teachers", hasSize(1)))
                .andReturn();
  }

  @Test
  void testGetByEmail() throws Exception {
    this.mockMvc.perform(
            get("/teachers/email/{email}", entity.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(entity.getFirstName()))
                .andReturn();
  }

  @Test
  void testGetByPersonalNumber() throws Exception {
    this.mockMvc.perform(
            get("/teachers/personal_number/{personal_number}", entity.getPersonalNumber())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(entity.getFirstName()))
                .andReturn();
  }

  @Test
  void testGetByBirthDay() throws Exception {
    this.mockMvc.perform(
            get("/teachers/birthday/{birthday}",
                entity.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teachers", hasSize(greaterThanOrEqualTo(2))))
                .andReturn();
  }

  @Test
  void testUpdate() throws Exception {
    entityToUpdate.setFirstName("changed");
    this.mockMvc.perform(
            put("/teachers/{id}", entityToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(entityToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("changed"))
                .andReturn();
  }

  @Test
  void testTeacherCourses() throws Exception {
    courseService.addTeacherToCourse(courseEntity.getId(), entity.getId());
    this.mockMvc.perform(
            get("/teachers/{id}/courses", entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk())
                .andReturn();
    assertTrue(teacherService.getTeacherCourses(entity.getId()).getCourses().stream().anyMatch(
        x -> Objects.equals(x.getNumber(), courseEntity.getNumber())));
  }

  @Test
  void testDelete() throws Exception {
    this.mockMvc.perform(
            delete("/teachers/{id}", entityToDelete.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(entityToDelete)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacher_id").value(entityToDelete.getId().toString()))
                .andReturn();
  }
}
