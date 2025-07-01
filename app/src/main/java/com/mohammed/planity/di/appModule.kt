package com.mohammed.planity.di

import com.mohammed.planity.presentation.navigation.RootViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohammed.planity.data.repository.TaskRepositoryImpl
import com.mohammed.planity.data.session.SessionManager
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
import com.mohammed.planity.domain.use_case.SignOutUseCase
import com.mohammed.planity.domain.use_case.UpdateCategoryOrderUseCase
import com.mohammed.planity.domain.use_case.UpdateTaskUseCase
import com.mohammed.planity.presentation.auth.AuthViewModel
import com.mohammed.planity.presentation.main.graph.GraphViewModel
import com.mohammed.planity.presentation.main.home.HomeViewModel
import com.mohammed.planity.presentation.main.home.categoryDialog.CreateCategoryViewModel
import com.mohammed.planity.presentation.main.home.taskinfo.TaskInfoViewModel
import com.mohammed.planity.ui.presentation.category.CategoryViewModel
import com.mohammed.planity.presentation.main.home.createtask.CreateTaskViewModel
import com.mohammed.planity.presentation.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // --- SINGLETONS ---
    single<TaskRepository> { TaskRepositoryImpl(firestore = get(), get()) }
    single { Firebase.firestore }
    single { SessionManager(androidContext()) }

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
    factory { SignOutUseCase(auth = get()) }

    // --- ADD THIS MISSING LINE ---
    factory { DeleteTaskUseCase(repository = get()) }

    // --- VIEWMODELS ---


    viewModel {
        RootViewModel(
            sessionManager = get(),
            auth = get() // Add the new dependency
        )
    }

    viewModel {
        HomeViewModel(
            getTasksUseCase = get(),
            getCategoriesUseCase = get(),
            moveTaskToCategoryUseCase = get(),
            createTaskUseCase = get()
        )
    }
    viewModel { AuthViewModel(auth = get()) }

    single { Firebase.auth }

    viewModel {
        // Now, when Koin tries to build this, it will find the recipes for all three dependencies.
        CategoryViewModel(
            getCategoriesUseCase = get(),
            deleteCategoryUseCase = get(),
            updateCategoryOrderUseCase = get()
        )
    }

viewModel{
    SettingsViewModel(
        auth = get(),
        signOutUseCase = get()
    )
}
    viewModel {
        CreateTaskViewModel(
            createTaskUseCase = get(),
            getCategoriesUseCase = get()
        )
    }
    viewModel {
        CreateCategoryViewModel(
            createCategoryUseCase = get(),
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