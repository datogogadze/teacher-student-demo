package com.softgen.demo.student;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;;
import com.softgen.demo.dtos.StudentDto;
import com.softgen.demo.services.StudentService;
import com.softgen.demo.services.impl.StudentServiceImpl;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
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
public class StudentEndpointTests {

  @Autowired
  private StudentService studentService;

  private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  private StudentDto entity = StudentDto.builder()
                                        .firstName("fname")
                                        .lastName("lname")
                                        .email("email@email.com")
                                        .personalNumber("12345678910")
                                        .birthday(LocalDate.now())
                                        .build();
  @Autowired
  MockMvc mockMvc;

  @BeforeAll
  public void setUp() {
    StudentDto createdEntity = studentService.createStudent(entity);
    entity.setId(createdEntity.getId());
  }

  @Test
  @Order(1)
  void getAllTest() throws Exception {
    this.mockMvc.perform(
            get("/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.students", hasSize(1)))
                .andReturn();

  }

  @Test
  @Order(2)
  void testCreateFail() throws Exception {
    this.mockMvc.perform(
            post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(entity)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Already exists"))
                .andReturn();
  }

  @Test
  @Order(3)
  void testCreate() throws Exception {
    StudentDto entity1 = StudentDto.builder()
                                   .firstName("fname1")
                                   .lastName("lname1")
                                   .email("email1@email.com")
                                   .personalNumber("12345678912")
                                   .birthday(LocalDate.now())
                                   .build();
    this.mockMvc.perform(
            post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(entity1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("fname1"))
                .andReturn();
  }

  @Test
  @Order(4)
  void testGetById() throws Exception {
    this.mockMvc.perform(
            get("/students/id/{id}", entity.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(entity.getFirstName()))
                .andReturn();
  }

  @Test
  @Order(5)
  void testGetByFirstName() throws Exception {
    this.mockMvc.perform(
            get("/students/first_name/{first_name}", entity.getFirstName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.students", hasSize(1)))
                .andReturn();
  }

  @Test
  @Order(6)
  void testGetByLastName() throws Exception {
    this.mockMvc.perform(
            get("/students/last_name/{last_name}", entity.getLastName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.students", hasSize(1)))
                .andReturn();
  }

  @Test
  @Order(7)
  void testGetByEmail() throws Exception {
    this.mockMvc.perform(
            get("/students/email/{email}", entity.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(entity.getFirstName()))
                .andReturn();
  }

  @Test
  @Order(8)
  void testGetByPersonalNumber() throws Exception {
    this.mockMvc.perform(
            get("/students/personal_number/{personal_number}", entity.getPersonalNumber())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(entity.getFirstName()))
                .andReturn();
  }

  @Test
  @Order(9)
  void testGetByBirthDay() throws Exception {
    this.mockMvc.perform(
            get("/students/birthday/{birthday}",
                entity.getBirthday().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.students", hasSize(2)))
                .andReturn();
  }

  @Test
  @Order(10)
  void testUpdate() throws Exception {
    entity.setFirstName("changed");
    this.mockMvc.perform(
            put("/students/{id}", entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("changed"))
                .andReturn();
  }

  @Test
  @Order(11)
  void testDelete() throws Exception {
    this.mockMvc.perform(
            delete("/students/{id}", entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student_id").value(entity.getId().toString()))
                .andReturn();
  }

}
