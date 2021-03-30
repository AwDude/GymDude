/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.itdude.gymdude.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import de.itdude.gymdude.di.ViewModelFactory
import de.itdude.gymdude.di.ViewModelKey
import de.itdude.gymdude.viewmodel.ExerciseViewModel
import de.itdude.gymdude.viewmodel.MenuViewModel
import de.itdude.gymdude.viewmodel.SettingsViewModel
import de.itdude.gymdude.viewmodel.WorkoutExerciseViewModel
import de.itdude.gymdude.viewmodel.WorkoutPlansViewModel
import de.itdude.gymdude.viewmodel.WorkoutViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {

	@Binds
	@IntoMap
	@ViewModelKey(MenuViewModel::class)
	abstract fun bindMenuViewModel(menuViewModel: MenuViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(SettingsViewModel::class)
	abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(ExerciseViewModel::class)
	abstract fun bindExerciseViewModel(exerciseViewModel: ExerciseViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(WorkoutPlansViewModel::class)
	abstract fun bindWorkoutPlansViewModel(workoutPlansViewModel: WorkoutPlansViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(WorkoutViewModel::class)
	abstract fun bindWorkoutViewModel(workoutViewModel: WorkoutViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(WorkoutExerciseViewModel::class)
	abstract fun bindWorkoutExerciseViewModel(workoutExerciseViewModel: WorkoutExerciseViewModel): ViewModel

	@Binds
	abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}