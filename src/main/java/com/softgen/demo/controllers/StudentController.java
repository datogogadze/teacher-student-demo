package com.softgen.demo.controllers;

import com.softgen.demo.dtos.StudentDto;
import com.softgen.demo.dtos.responses.CourseListResponse;
import com.softgen.demo.dtos.responses.StudentIdResponse;
import com.softgen.demo.dtos.responses.StudentListResponse;
import com.softgen.demo.services.StudentService;
import java.time.LocalDate;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("students")
public class StudentController {

  private final StudentService studentService;

  @GetMapping
  public StudentListResponse getStudents() {
    return studentService.getStudents();
  }

  @GetMapping("/id/{id}")
  public StudentDto getStudentById(@PathVariable("id") UUID id) {
    return studentService.getStudentById(id);
  }

  @GetMapping("/first_name/{firstName}")
  public StudentListResponse getStudentByFirstName(@PathVariable("firstName") String firstName) {
    return studentService.getStudentByFirstName(firstName);
  }

  @GetMapping("/last_name/{lastName}")
  public StudentListResponse getStudentByLastName(@PathVariable("lastName") String lastName) {
    return studentService.getStudentByLastName(lastName);
  }

  // ალბათ უკეთესი იქნებოდა თუ იმეილს და პირად ნომერს ურლ-ში არ გამოვაჩანდით და Post რექუესთს
  // გამოვიყენებდით, რადგან სენსიტიური ინფორცმაციაა, თუმცა სიმარტივისთვის ამ შემთხვევაში
  // ვამჯობინე ყველა პარამეტრით წამოღება მსგავსი ყოფილიყო და აიდის, სახელის, გვარის მსგავსად Get
  // რექუესთი გამოვიყენე.
  @GetMapping("/email/{email:.+}")
  public StudentDto getStudentByEmail(@PathVariable("email") String email) {
    return studentService.getStudentByEmail(email);
  }

  @GetMapping("/personal_number/{personalNumber}")
  public StudentDto getStudentByPersonalNumber(
      @PathVariable("personalNumber") String personalNumber
                                              ) {
    return studentService.getStudentByPersonalNumber(personalNumber);
  }

  @GetMapping("/birthday/{birthday}")
  public StudentListResponse getStudentByBirthDay(
      @PathVariable("birthday") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate birthday
                                                 ) {
    return studentService.getStudentByBirthday(birthday);
  }

  @PostMapping
  public StudentDto createStudent(@RequestBody @Valid StudentDto studentDto) {
    return studentService.createStudent(studentDto);
  }

  // Patch რექუესთის გაკეთებაც შეიძლებოდა, მაგრამ სიმარტივისთვის იყოს Put.
  @PutMapping("/{id}")
  public StudentDto updateStudent(@PathVariable("id") UUID id,
                                  @RequestBody @Valid StudentDto studentDto
                                 ) {
    studentDto.setId(id);
    return studentService.updateStudent(studentDto);
  }

  @DeleteMapping("/{id}")
  public StudentIdResponse deleteStudentById(@PathVariable("id") UUID id) {
    return studentService.deleteStudentById(id);
  }

  @GetMapping("/{id}/courses")
  public CourseListResponse getStudentCourses(@PathVariable("id") UUID studentId) {
    return studentService.getStudentCourses(studentId);
  }

}
