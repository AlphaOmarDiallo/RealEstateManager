package com.example.realestatemanager.data.sampleData

import com.example.realestatemanager.data.model.Agent

object SampleAgent {

    private val sampleListAgent = listOf(
        Agent("kjbdklaj", "Alpha", "alpha@realestate.com", null),
        Agent("bnvkzeljnza", "Maria", "maria@realestate.com", null),
        Agent("ndzkanùka", "Malik", "malik@realestate.com", null),
        Agent("ndzoandzmoa", "Nael", "nael@realestate.com", null)
    )

    fun getSampleAgentList(): List<Agent> {
        return sampleListAgent
    }
}