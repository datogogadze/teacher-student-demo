package com.softgen.demo.dtos.responses;

import com.softgen.demo.dtos.CourseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseListResponse {

  private List<CourseDto> courses;

}
