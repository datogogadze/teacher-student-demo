package com.softgen.demo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDto {

  @JsonProperty("id")
  private UUID id;

  @NotNull
  @JsonProperty("first_name")
  @Size(min = 2, max = 64)
  private String firstName;

  @NotNull
  @JsonProperty("last_name")
  @Size(min = 2, max = 64)
  private String lastName;

  @NotNull
  @JsonProperty("personal_number")
  @Pattern(regexp = "^\\d{11}$", message = "should be 11 digits")
  private String personalNumber;

  @JsonProperty("email")
  @NotNull
  @NotEmpty
  @Email
  private String email;

  @NotNull
  @JsonProperty("birthday")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthday;

}
