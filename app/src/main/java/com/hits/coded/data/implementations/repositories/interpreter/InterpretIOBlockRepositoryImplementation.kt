package com.hits.coded.data.implementations.repositories.interpreter

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType
import com.hits.coded.data.models.console.useCases.ConsoleUseCases
import com.hits.coded.data.models.interpreter.useCases.helpers.InterpreterHelperUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterConverterUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretIOBlockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretIOBlockRepositoryImplementation
@Inject constructor(
    private val consoleUseCases: ConsoleUseCases,
    private val interpreterConverterUseCases: InterpreterConverterUseCases,
    private val interpreterHelperUseCases: InterpreterHelperUseCases
) : InterpretIOBlockRepository() {
    @Throws(InterpreterException::class)
    override suspend fun interpretIOBlocks(IO: IOBlockBase): String? {
        IO.id?.let {
            interpreterHelperUseCases.setCurrentIdVariableUseCase.setCurrentIdVariable(it)
        }

        return when (IO.ioBlockType) {
            IOBlockType.WRITE -> {
                IO.argument?.let {
                    consoleUseCases.writeToConsoleUseCase.writeOutputToConsole(
                        interpreterConverterUseCases.convertAnyToStringIndulgentlyUseCase.convertAnyToStringIndulgently(
                            it
                        )
                    )
                }

                null
            }

            IOBlockType.READ -> {
                consoleUseCases.readFromConsoleUseCase.readFromConsole()
            }
        }
    }
}