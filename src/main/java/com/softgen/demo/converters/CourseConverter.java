package com.softgen.demo.converters;

import com.softgen.demo.dtos.CourseDto;
import com.softgen.demo.dtos.TeacherDto;
import com.softgen.demo.entities.CourseEntity;
import com.softgen.demo.entities.TeacherEntity;

public class CourseConverter {

  public static CourseDto toDto(CourseEntity entity) {
    return CourseDto.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .number(entity.getNumber())
                    .build();
  }

  public static CourseEntity toEntity(CourseDto dto) {
    return CourseEntity.builder()
                       .id(dto.getId())
                       .name(dto.getName())
                       .number(dto.getNumber())
                       .build();
  }

}
