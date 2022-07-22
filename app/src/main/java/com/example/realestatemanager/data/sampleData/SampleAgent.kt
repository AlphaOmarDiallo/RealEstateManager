package com.example.realestatemanager.data.sampleData

import com.example.realestatemanager.data.model.Agent

object SampleAgent {

    private val sampleListAgent = listOf(
        Agent(0, "Alpha", "alpha@realestate.com", null),
        Agent(0, "Maria", "maria@realestate.com", null),
        Agent(0, "Malik", "malik@realestate.com", null),
        Agent(0, "Nael", "nael@realestate.com", null)
    )

    fun getSampleAgentList(): List<Agent> {
        return sampleListAgent
    }
}