package com.softgen.demo.dtos.responses;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherIdResponse {

  private UUID teacher_id;
}
