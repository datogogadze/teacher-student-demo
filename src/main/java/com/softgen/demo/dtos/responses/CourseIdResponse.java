package com.softgen.demo.dtos.responses;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseIdResponse {

  private UUID course_id;
  
}
