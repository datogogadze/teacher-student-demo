package com.softgen.demo.dtos.responses;

import com.softgen.demo.dtos.StudentDto;
import com.softgen.demo.dtos.TeacherDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherListResponse {

  private List<TeacherDto> teachers;
}
