package com.softgen.demo.converters;

import com.softgen.demo.dtos.StudentDto;
import com.softgen.demo.entities.StudentEntity;

public class StudentConverter {

  public static StudentDto toDto(StudentEntity entity) {
    return StudentDto.builder()
                     .id(entity.getId())
                     .firstName(entity.getFirstName())
                     .lastName(entity.getLastName())
                     .personalNumber(entity.getPersonalNumber())
                     .email(entity.getEmail())
                     .birthday(entity.getBirthday())
                     .build();
  }

  public static StudentEntity toEntity(StudentDto dto) {
    return StudentEntity.builder()
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .personalNumber(dto.getPersonalNumber())
                        .email(dto.getEmail())
                        .birthday(dto.getBirthday())
                        .build();
  }
}
