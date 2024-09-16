package com.steekse.webatrioerp.mapper;

import com.steekse.webatrioerp.dto.JobDto;
import com.steekse.webatrioerp.model.Job;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobMapper {
    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

    JobDto toDto(Job job);

    Job toEntity(JobDto jobDto);
}
