package com.hits.coded.data.modules.interpreter

import com.hits.coded.data.implementations.repositories.interpreter.InterpretArrayBlockRepositoryImplementation
import com.hits.coded.data.implementations.repositories.interpreter.InterpretConditionBlockRepositoryImplementation
import com.hits.coded.data.implementations.repositories.interpreter.InterpretExpressionBlockRepositoryImplementation
import com.hits.coded.data.implementations.repositories.interpreter.InterpretIOBlockRepositoryImplementation
import com.hits.coded.data.implementations.repositories.interpreter.InterpretIfBlockRepositoryImplementation
import com.hits.coded.data.implementations.repositories.interpreter.InterpretLoopBlockRepositoryImplementation
import com.hits.coded.data.implementations.repositories.interpreter.InterpretVariableBlockRepositoryImplementation
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterBlocksUseCases
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretArrayBlockRepository
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretConditionBlockRepository
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretExpressionBlockRepository
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretIOBlockRepository
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretIfBlockRepository
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretLoopBlockRepository
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretVariableBlockRepository
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretArrayBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretConditionUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretExpressionBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretIOBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretIfBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretLoopBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretVariableBlockUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterpreterBlocksModule {
    @Binds
    abstract fun bindInterpretArrayBlockRepository(
        interpretArrayBlockRepository: InterpretArrayBlockRepositoryImplementation
    ): InterpretArrayBlockRepository

    @Binds
    abstract fun bindInterpretConditionBlockRepository(
        interpretConditionBlockRepository: InterpretConditionBlockRepositoryImplementation
    ): InterpretConditionBlockRepository

    @Binds
    abstract fun bindInterpretExpressionBlockRepository(
        interpretExpressionBlockRepository: InterpretExpressionBlockRepositoryImplementation
    ): InterpretExpressionBlockRepository

    @Binds
    abstract fun bindInterpretIfBlockRepository(
        interpretIfBlockRepository: InterpretIfBlockRepositoryImplementation
    ): InterpretIfBlockRepository

    @Binds
    abstract fun bindInterpretIOBlockRepository(
        interpretIOBlockRepository: InterpretIOBlockRepositoryImplementation
    ): InterpretIOBlockRepository

    @Binds
    abstract fun bindInterpretLoopBlockRepository(
        interpretLoopBlockRepository: InterpretLoopBlockRepositoryImplementation
    ): InterpretLoopBlockRepository

    @Binds
    abstract fun bindInterpretVariableBlockRepository(
        interpretVariableBlockRepository: InterpretVariableBlockRepositoryImplementation
    ): InterpretVariableBlockRepository

    companion object {
        @Provides
        @Singleton
        fun provideInterpretBlocksUseCases(
            interpretArrayBlockRepository: InterpretArrayBlockRepository,
            interpretConditionBlockRepository: InterpretConditionBlockRepository,
            interpretExpressionBlockRepository: InterpretExpressionBlockRepository,
            interpretIfBlockRepository: InterpretIfBlockRepository,
            interpretIOBlockRepository: InterpretIOBlockRepository,
            interpretLoopBlockRepository: InterpretLoopBlockRepository,
            interpretVariableBlockRepository: InterpretVariableBlockRepository
        ): InterpreterBlocksUseCases =
            InterpreterBlocksUseCases(
                InterpretArrayBlockUseCase(interpretArrayBlockRepository),
                InterpretConditionUseCase(interpretConditionBlockRepository),
                InterpretExpressionBlockUseCase(interpretExpressionBlockRepository),
                InterpretIfBlockUseCase(interpretIfBlockRepository),
                InterpretIOBlockUseCase(interpretIOBlockRepository),
                InterpretLoopBlockUseCase(interpretLoopBlockRepository),
                InterpretVariableBlockUseCase(interpretVariableBlockRepository)
            )
    }
}