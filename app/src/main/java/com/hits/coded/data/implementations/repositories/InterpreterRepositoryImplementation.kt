package com.hits.coded.data.implementations.repositories

import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterAuxiliaryUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.domain.repositories.InterpreterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpreterRepositoryImplementation
@Inject constructor(
    private val interpreterAuxiliaryUseCases: InterpreterAuxiliaryUseCases
) : InterpreterRepository() {

    override var currentId = 0
        private set

    @Throws(InterpreterException::class)
    override suspend fun interpretStartBlock(startBlock: StartBlock) {
        startBlock.nestedBlocks?.forEach { nestedBlock ->
            interpreterAuxiliaryUseCases.interpretAnyBlockUseCase.interpretBlock(nestedBlock)
        }
    }
}