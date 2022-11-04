package com.softgen.demo.dtos.responses;

import com.softgen.demo.dtos.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentListResponse {

  private List<StudentDto> students;

}
