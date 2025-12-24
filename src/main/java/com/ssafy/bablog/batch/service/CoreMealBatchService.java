package com.ssafy.bablog.batch.service;

import com.ssafy.bablog.meal.service.MealService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CoreMealBatchService implements MealBatchService {

    private final MealService mealService;

    public CoreMealBatchService(MealService mealService) {
        this.mealService = mealService;
    }

    @Override
    public void createDailyMeals(Long memberId, LocalDate targetDate) {
        mealService.createDailyMeals(memberId, targetDate);
    }
}
