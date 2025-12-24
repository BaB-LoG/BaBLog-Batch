package com.ssafy.bablog.batch.service;

import java.time.LocalDate;

public interface MealBatchService {
    void createDailyMeals(Long memberId, LocalDate targetDate);
}
