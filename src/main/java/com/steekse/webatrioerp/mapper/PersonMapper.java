package com.steekse.webatrioerp.mapper;

import com.steekse.webatrioerp.dto.PersonDto;
import com.steekse.webatrioerp.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(target = "jobs", source = "jobs")
    PersonDto toDto(Person person);

    @Mapping(target = "jobs", ignore = true)  // Prevents unnecessary job mapping initially
    Person toEntity(PersonDto personDto);
}
