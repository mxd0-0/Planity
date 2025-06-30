package com.mohammed.planity.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohammed.planity.data.repository.TaskRepositoryImpl
import com.mohammed.planity.domain.repository.TaskRepository
import com.mohammed.planity.domain.use_case.CreateCategoryUseCase
import com.mohammed.planity.domain.use_case.CreateTaskUseCase
import com.mohammed.planity.domain.use_case.DeleteCategoryUseCase
import com.mohammed.planity.domain.use_case.DeleteTaskUseCase
import com.mohammed.planity.domain.use_case.GetCategoriesUseCase
import com.mohammed.planity.domain.use_case.GetTaskByIdUseCase
import com.mohammed.planity.domain.use_case.GetTasksUseCase
import com.mohammed.planity.domain.use_case.GetWeeklyTaskStatsUseCase
import com.mohammed.planity.domain.use_case.MoveTaskToCategoryUseCase
import com.mohammed.planity.domain.use_case.UpdateCategoryOrderUseCase
import com.mohammed.planity.domain.use_case.UpdateTaskUseCase
import com.mohammed.planity.presentation.main.graph.GraphViewModel
import com.mohammed.planity.presentation.main.home.HomeViewModel
import com.mohammed.planity.presentation.main.home.taskinfo.TaskInfoViewModel
import com.mohammed.planity.ui.presentation.category.CategoryViewModel
import com.mohammed.planity.ui.presentation.createtask.CreateTaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // --- SINGLETONS ---
    single<TaskRepository> { TaskRepositoryImpl(firestore = get()) }
    single { Firebase.firestore }

    // --- USE CASES (Factories) ---
    factory { GetTasksUseCase(repository = get()) }
    factory { GetCategoriesUseCase(repository = get()) }
    factory { CreateTaskUseCase(repository = get()) }
    factory { CreateCategoryUseCase(repository = get()) }
    factory { UpdateTaskUseCase(repository = get()) }
    factory { MoveTaskToCategoryUseCase(repository = get()) }
    factory { UpdateCategoryOrderUseCase(repository = get()) }
    factory { GetTaskByIdUseCase(repository = get()) }
    factory { GetWeeklyTaskStatsUseCase(repository = get()) }
    factory { DeleteCategoryUseCase(repository = get()) }

    // --- ADD THIS MISSING LINE ---
    factory { DeleteTaskUseCase(repository = get()) }

    // --- VIEWMODELS ---
    viewModel {
        HomeViewModel(
            getTasksUseCase = get(),
            getCategoriesUseCase = get(),
            moveTaskToCategoryUseCase = get(),
            createTaskUseCase = get()
        )
    }

    viewModel {
        // Now, when Koin tries to build this, it will find the recipes for all three dependencies.
        CategoryViewModel(
            getCategoriesUseCase = get(),
            deleteCategoryUseCase = get(),
            updateCategoryOrderUseCase = get()
        )
    }
    viewModel {
        CreateTaskViewModel(
            createTaskUseCase = get(),
            getCategoriesUseCase = get()
        )
    }
    viewModel {
        CategoryViewModel(
            getCategoriesUseCase = get(),
            deleteCategoryUseCase = get(),
            updateCategoryOrderUseCase = get()
        )
    }
    viewModel {
        GraphViewModel(
            getWeeklyTaskStatsUseCase = get()
        )
    }
    viewModel { params ->
        // This definition is now correct because Koin can find DeleteTaskUseCase
        TaskInfoViewModel(
            getTaskByIdUseCase = get(),
            updateTaskUseCase = get(),
            moveTaskToCategoryUseCase = get(),
            savedStateHandle = params.get()
        )
    }
}