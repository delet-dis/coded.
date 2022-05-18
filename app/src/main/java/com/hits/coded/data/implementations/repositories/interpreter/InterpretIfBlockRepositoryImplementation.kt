package com.hits.coded.data.implementations.repositories.interpreter

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.IfBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IfBlockType
import com.hits.coded.data.models.interpreter.useCases.helpers.InterpreterHelperUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterAuxiliaryUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterConverterUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretIfBlockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretIfBlockRepositoryImplementation
@Inject constructor(
    private val interpreterAuxiliaryUseCases: InterpreterAuxiliaryUseCases,
    private val interpreterConverterUseCases: InterpreterConverterUseCases,
    private val interpreterHelperUseCases: InterpreterHelperUseCases
) : InterpretIfBlockRepository() {
    @Throws(InterpreterException::class)
    override suspend fun interpretIfBlock(ifBlock: IfBlockBase) {
        ifBlock.id?.let {
            interpreterHelperUseCases.setCurrentIdVariableUseCase.setCurrentIdVariable(it)
        }

        if (interpreterConverterUseCases.convertAnyToBooleanUseCase.convertAnyToBoolean(ifBlock.conditionBlock)) {
            ifBlock.nestedBlocks?.let {
                it.forEach { blockBase ->
                    interpreterAuxiliaryUseCases.interpretAnyBlockUseCase.interpretBlock(blockBase)
                }
            }
        } else if (ifBlock.ifBlockType == IfBlockType.IF_WITH_ELSE) {
            ifBlock.elseBlocks?.let {
                it.forEach { blockBase ->
                    interpreterAuxiliaryUseCases.interpretAnyBlockUseCase.interpretBlock(blockBase)
                }
            }
        }
    }
}