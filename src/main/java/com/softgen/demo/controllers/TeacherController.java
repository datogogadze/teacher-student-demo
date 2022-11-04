package com.softgen.demo.controllers;

import com.softgen.demo.dtos.TeacherDto;
import com.softgen.demo.dtos.responses.CourseListResponse;
import com.softgen.demo.dtos.responses.TeacherIdResponse;
import com.softgen.demo.dtos.responses.TeacherListResponse;
import com.softgen.demo.services.TeacherService;
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
@RequestMapping("teachers")
public class TeacherController {

  private final TeacherService teacherService;

  @GetMapping
  public TeacherListResponse getTeachers() {
    return teacherService.getTeachers();
  }

  @GetMapping("/id/{id}")
  public TeacherDto getTeacherById(@PathVariable("id") UUID id) {
    return teacherService.getTeacherById(id);
  }

  @GetMapping("/first_name/{firstName}")
  public TeacherListResponse getTeacherByFirstName(@PathVariable("firstName") String firstName) {
    return teacherService.getTeacherByFirstName(firstName);
  }

  @GetMapping("/last_name/{lastName}")
  public TeacherListResponse getTeacherByLastName(@PathVariable("lastName") String lastName) {
    return teacherService.getTeacherByLastName(lastName);
  }

  // ალბათ უკეთესი იქნებოდა თუ იმეილს და პირად ნომერს ურლ-ში არ გამოვაჩანდით და Post რექუესთს
  // გამოვიყენებდით, რადგან სენსიტიური ინფორცმაციაა, თუმცა სიმარტივისთვის ამ შემთხვევაში
  // ვამჯობინე ყველა პარამეტრით წამოღება მსგავსი ყოფილიყო და აიდის, სახელის, გვარის მსგავსად Get
  // რექუესთი გამოვიყენე.
  @GetMapping("/email/{email:.+}")
  public TeacherDto getTeacherByEmail(@PathVariable("email") String email) {
    return teacherService.getTeacherByEmail(email);
  }

  @GetMapping("/personal_number/{personalNumber}")
  public TeacherDto getTeacherByPersonalNumber(
      @PathVariable("personalNumber") String personalNumber
                                              ) {
    return teacherService.getTeacherByPersonalNumber(personalNumber);
  }

  @GetMapping("/birthday/{birthday}")
  public TeacherListResponse getTeacherByBirthDay(
      @PathVariable("birthday") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate birthday
                                                 ) {
    return teacherService.getTeacherByBirthday(birthday);
  }

  @PostMapping
  public TeacherDto createTeacher(@RequestBody @Valid TeacherDto teacherDto) {
    return teacherService.createTeacher(teacherDto);
  }

  // Patch რექუესთის გაკეთებაც შეიძლებოდა, მაგრამ სიმარტივისთვის იყოს Put.
  @PutMapping("/{id}")
  public TeacherDto updateTeacher(@PathVariable("id") UUID id,
                                  @RequestBody @Valid TeacherDto teacherDto
                                 ) {
    teacherDto.setId(id);
    return teacherService.updateTeacher(teacherDto);
  }

  @DeleteMapping("/{id}")
  public TeacherIdResponse deleteTeacherById(@PathVariable("id") UUID id) {
    return teacherService.deleteTeacherById(id);
  }

  @GetMapping("/{id}/courses")
  public CourseListResponse getTeacherCourses(@PathVariable("id") UUID teacherId) {
    return teacherService.getTeacherCourses(teacherId);
  }

}
