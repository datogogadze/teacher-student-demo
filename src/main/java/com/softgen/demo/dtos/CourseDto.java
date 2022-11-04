package com.softgen.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {

  @JsonProperty("id")
  private UUID id;

  @NotNull
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("number")
  private Integer number;

}
