package com.hits.coded.data.implementations.repositories.interpreter

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.LoopBlockBase
import com.hits.coded.data.models.interpreter.useCases.helpers.InterpreterHelperUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterAuxiliaryUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterConverterUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretLoopBlockRepository
import com.hits.coded.domain.repositories.interpreterRepositories.helpers.StopInterpreter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretLoopBlockRepositoryImplementation
@Inject constructor(
    private val interpreterConverterUseCases: InterpreterConverterUseCases,
    private val interpreterAuxiliaryUseCases: InterpreterAuxiliaryUseCases,
    private val interpreterHelperUseCases: InterpreterHelperUseCases,
    private val stopInterpreter: StopInterpreter
) : InterpretLoopBlockRepository() {
    @Throws(InterpreterException::class)
    override suspend fun interpretLoopBlock(loopBlock: LoopBlockBase) {
        loopBlock.id?.let {
            interpreterHelperUseCases.setCurrentIdVariableUseCase.setCurrentIdVariable(it)
        }

        loopBlock.nestedBlocks?.let {
            while (interpreterConverterUseCases.convertAnyToBooleanUseCase.convertAnyToBoolean(
                    loopBlock.conditionBlock
                )
            ) {
                if (!stopInterpreter.isInterpreting) {
                    break
                }
                it.forEach { blockBase ->
                    interpreterAuxiliaryUseCases.interpretAnyBlockUseCase.interpretBlock(blockBase)
                }
            }
        }
    }
}