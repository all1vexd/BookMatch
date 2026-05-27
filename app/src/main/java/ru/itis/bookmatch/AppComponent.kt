package ru.itis.bookmatch

import dagger.Component
import ru.itis.bookmatch.presentation.MainActivity
import ru.itis.bookmatch.presentation.screens.library.LibraryScreenViewModel
import ru.itis.bookmatch.presentation.screens.login.LoginScreenViewModel
import ru.itis.bookmatch.presentation.screens.mainScreen.MainScreenViewModel
import ru.itis.bookmatch.presentation.screens.profile.ProfileScreenViewModel
import ru.itis.bookmatch.presentation.screens.registration.RegistrationScreenViewModel
import ru.itis.bookmatch.presentation.screens.saved.SavedScreenViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DatabaseModule::class,
    RepositoryModule::class
])
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }

    fun mainScreenViewModelFactory(): MainScreenViewModel.Factory

    fun savedScreenViewModelFactory(): SavedScreenViewModel.Factory

    fun loginScreenViewModel(): LoginScreenViewModel

    fun registrationScreenViewModel(): RegistrationScreenViewModel

    fun libraryScreenViewModelFactory(): LibraryScreenViewModel.Factory

    fun profileScreenViewModelFactory(): ProfileScreenViewModel.Factory
}