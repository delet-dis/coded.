package com.hits.coded.data.implementations.repositories.interpreter

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ArrayBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ArrayBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.data.models.interpreter.useCases.helpers.InterpreterHelperUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterConverterUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretArrayBlockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretArrayBlockRepositoryImplementation
@Inject constructor(
    private val heapUseCases: HeapUseCases,
    private val interpreterConverterUseCases: InterpreterConverterUseCases,
    private val interpreterHelperUseCases: InterpreterHelperUseCases
) : InterpretArrayBlockRepository() {
    @Throws(InterpreterException::class)
    override suspend fun interpretArrayBlock(block: ArrayBlockBase): Any {
        block.id?.let {
            interpreterHelperUseCases.setCurrentIdVariableUseCase.setCurrentIdVariable(it)
        }

        val storedArray = when (val arrayIdentifier = block.array) {
            is String -> heapUseCases.getVariableUseCase.getVariable(arrayIdentifier)
            is ArrayBlockBase -> (interpretArrayBlock(arrayIdentifier) as? StoredVariable)
            else -> null
        }
            ?: throw InterpreterException(
                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
            )


        if (storedArray.isArray != true)
            throw InterpreterException(
                ExceptionType.TYPE_MISMATCH
            )

        val array = storedArray.value!! as ArrayBase // array in heap -> it has been constructed

        return when (block.arrayBlockType) {
            ArrayBlockType.GET_SIZE -> array.size
            ArrayBlockType.GET_ELEMENT -> array[interpreterConverterUseCases.convertAnyToIntUseCase.convertAnyToInt(
                block.value
            )]
            ArrayBlockType.PUSH -> array.push(block.value)
            ArrayBlockType.POP -> array.pop()
            ArrayBlockType.CONCAT -> array.concat(block.value as? ArrayBase)
        }
    }
}