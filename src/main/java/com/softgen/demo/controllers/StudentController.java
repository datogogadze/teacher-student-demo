package com.softgen.demo.controllers;

import com.softgen.demo.dtos.StudentDto;
import com.softgen.demo.dtos.responses.StudentIdResponse;
import com.softgen.demo.dtos.responses.StudentListResponse;
import com.softgen.demo.services.StudentService;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
  public StudentDto getStudentById(@PathVariable UUID id) {
    return studentService.getStudentById(id);
  }

  @GetMapping("/first/{firstName}")
  public StudentListResponse getStudentByFirstName(@PathVariable String firstName) {
    return studentService.getStudentByFirstName(firstName);
  }

  @GetMapping("/last/{lastName}")
  public StudentListResponse getStudentByLastName(@PathVariable String lastName) {
    return studentService.getStudentByFirstName(lastName);
  }

  // ალბათ უკეთესი იქნებოდა თუ იმეილს და პირად ნომერს ურლ-ში არ გამოვაჩანდით და პოსტ რექუესთს
  // გამოვიყენებდით, რადგან სენსიტიური ინფორცმაციაა, თუმცა სიმარტივისთვის ამ შემთხვევაში
  // ვამჯობინე ყველა პარამეტრით წამოღება მსგავსი ყოფილიყო და აიდის, სახელის, გვარის მსგავსად გეთ
  // რექუესთი გამოვიყენე.
  @GetMapping("/email/{email}")
  public StudentDto getStudentByEmail(@PathVariable String email) {
    return studentService.getStudentByEmail(email);
  }

  @GetMapping("/pn/{personalNumber}")
  public StudentDto getStudentByPersonalNumber(@PathVariable String personalNumber) {
    return studentService.getStudentByPersonalNumber(personalNumber);
  }

  @PostMapping
  public StudentDto createStudent(@RequestBody @Valid StudentDto studentDto) {
    return studentService.createStudent(studentDto);
  }

  @PutMapping
  public StudentDto updateStudent(@RequestBody @Valid StudentDto studentDto) {
    return studentService.updateStudent(studentDto);
  }

  @DeleteMapping("/{id}")
  public StudentIdResponse deleteStudentById(@PathVariable UUID id) {
    return studentService.deleteStudentById(id);
  }

}
