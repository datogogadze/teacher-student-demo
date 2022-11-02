package com.softgen.demo.converters;

import com.softgen.demo.dtos.TeacherDto;
import com.softgen.demo.entities.TeacherEntity;

public class TeacherConverter {

  public static TeacherDto toDto(TeacherEntity entity) {
    return TeacherDto.builder()
                     .id(entity.getId())
                     .firstName(entity.getFirstName())
                     .lastName(entity.getLastName())
                     .personalNumber(entity.getPersonalNumber())
                     .email(entity.getEmail())
                     .birthday(entity.getBirthday())
                     .build();
  }

  public static TeacherEntity toEntity(TeacherDto dto) {
    return TeacherEntity.builder()
                        .id(dto.getId())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .personalNumber(dto.getPersonalNumber())
                        .email(dto.getEmail())
                        .birthday(dto.getBirthday())
                        .build();
  }

}
