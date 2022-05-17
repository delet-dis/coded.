package com.hits.coded.data.implementations.repositories.interpreterRepositoriyImplementations

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType
import com.hits.coded.data.models.console.useCases.ConsoleUseCases
import com.hits.coded.data.models.interpreter.useCases.ConvertorsUseCases
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretIOBlockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretIOBlockRepositoryImplementation
@Inject constructor(
    private val consoleUseCases: ConsoleUseCases,
    private val convertorsUseCases: ConvertorsUseCases,
    private val currentIdValue: CurrentIdValue
) : InterpretIOBlockRepository() {
    override suspend fun interpretIOBlocks(IO: IOBlockBase): String? {
        IO.id?.let {
            currentIdValue.currentId = it
        }

        return when (IO.ioBlockType) {
            IOBlockType.WRITE -> {
                IO.argument?.let {
                    consoleUseCases.writeToConsoleUseCase.writeOutputToConsole(
                        convertorsUseCases.convertAnyToStringIndulgentlyUseCase.convertAnyToStringIndulgently(
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