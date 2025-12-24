package com.ssafy.bablog.batch.failure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface BatchFailureLogMapper {
    void insertFailure(@Param("jobName") String jobName,
                       @Param("stepName") String stepName,
                       @Param("memberId") Long memberId,
                       @Param("targetDate") LocalDate targetDate,
                       @Param("weekStart") LocalDate weekStart,
                       @Param("weekEnd") LocalDate weekEnd,
                       @Param("errorClass") String errorClass,
                       @Param("errorMessage") String errorMessage);
}
