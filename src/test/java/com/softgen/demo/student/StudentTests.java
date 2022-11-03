package com.softgen.demo.student;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import com.softgen.demo.dtos.StudentDto;
import com.softgen.demo.dtos.responses.StudentIdResponse;
import com.softgen.demo.dtos.responses.StudentListResponse;
import com.softgen.demo.services.impl.StudentServiceImpl;
import java.time.LocalDate;
import javax.persistence.EntityExistsException;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
public class StudentTests {

  @Autowired
  private StudentServiceImpl studentService;

  private StudentDto entity = StudentDto.builder()
                                        .firstName("fname")
                                        .lastName("lname")
                                        .email("email")
                                        .personalNumber("pn")
                                        .birthday(LocalDate.now())
                                        .build();

  @BeforeAll
  public void setUp() {
    StudentDto createdEntity = studentService.createStudent(entity);
    entity.setId(createdEntity.getId());
  }

  @Test
  @Order(1)
  void getAllTest() {
    assertEquals("Check size", 1, studentService.getStudents().getStudents().size());
  }

  @Test
  @Order(2)
  void testCreateFail() {
    Assertions.assertThrows(EntityExistsException.class,
                            () -> studentService.createStudent(entity));
  }

  @Test
  @Order(3)
  void testCreate() {
    StudentDto entity1 = StudentDto.builder()
                                   .firstName("fname1")
                                   .lastName("lname1")
                                   .email("email1")
                                   .personalNumber("pn1")
                                   .birthday(LocalDate.now())
                                   .build();
    StudentDto createdEntity = studentService.createStudent(entity1);
    Assertions.assertEquals("fname1", createdEntity.getFirstName());
  }

  @Test
  @Order(4)
  void testGetById() {
    StudentDto found = studentService.getStudentById(entity.getId());
    Assertions.assertEquals("fname", found.getFirstName());
  }

  @Test
  @Order(5)
  void testGetByFirstName() {
    StudentListResponse found = studentService.getStudentByFirstName(entity.getFirstName());
    Assertions.assertNotEquals(null, found.getStudents());
    Assertions.assertEquals(1, found.getStudents().size());
  }

  @Test
  @Order(6)
  void testGetByLastName() {
    StudentListResponse found = studentService.getStudentByLastName(entity.getLastName());
    Assertions.assertNotEquals(null, found.getStudents());
    Assertions.assertEquals(1, found.getStudents().size());
  }

  @Test
  @Order(7)
  void testGetByEmail() {
    StudentDto found = studentService.getStudentByEmail(entity.getEmail());
    Assertions.assertEquals("fname", found.getFirstName());
  }

  @Test
  @Order(8)
  void testGetByPersonalNumber() {
    StudentDto found = studentService.getStudentByPersonalNumber(entity.getPersonalNumber());
    Assertions.assertEquals("fname", found.getFirstName());
  }

  @Test
  @Order(9)
  void testGetByBirthDay() {
    StudentListResponse found = studentService.getStudentByBirthday(entity.getBirthday());
    Assertions.assertNotEquals(null, found.getStudents());
    Assertions.assertEquals(2, found.getStudents().size());
  }

  @Test
  @Order(10)
  void testUpdate() {
    entity.setFirstName("changed");
    studentService.getStudents();
    StudentDto updated = studentService.updateStudent(entity);
    Assertions.assertEquals("changed", updated.getFirstName());
  }

  @Test
  @Order(11)
  void testDelete() {
    StudentIdResponse deleted = studentService.deleteStudentById(entity.getId());
    Assertions.assertNotEquals(null, deleted.getStudent_id());
    Assertions.assertEquals(entity.getId(), deleted.getStudent_id());
  }

}
