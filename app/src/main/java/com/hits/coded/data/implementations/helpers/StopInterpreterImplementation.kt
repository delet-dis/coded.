package com.hits.coded.data.implementations.helpers

import com.hits.coded.domain.repositories.interpreterRepositories.helpers.StopInterpreter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopInterpreterImplementation
@Inject constructor():StopInterpreter(){
    override fun stopInterpreter() {
        this.isInterpreting=false
    }
}