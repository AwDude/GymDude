/*
 * Copyright (C) 2017 The Android Open Source Project
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

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.itdude.gymdude.ui.fragment.ExerciseFragment
import de.itdude.gymdude.ui.fragment.MenuFragment
import de.itdude.gymdude.ui.fragment.SettingsFragment
import de.itdude.gymdude.ui.fragment.WorkoutExerciseFragment
import de.itdude.gymdude.ui.fragment.WorkoutFragment
import de.itdude.gymdude.ui.fragment.WorkoutPlansFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

	@ContributesAndroidInjector
	abstract fun contributeMenuFragment(): MenuFragment

	@ContributesAndroidInjector
	abstract fun contributeSettingsFragment(): SettingsFragment

	@ContributesAndroidInjector
	abstract fun contributeExerciseFragment(): ExerciseFragment

	@ContributesAndroidInjector
	abstract fun contributeWorkoutPlansFragment(): WorkoutPlansFragment

	@ContributesAndroidInjector
	abstract fun contributeWorkoutFragment(): WorkoutFragment

	@ContributesAndroidInjector
	abstract fun contributeWorkoutExerciseFragment(): WorkoutExerciseFragment

}