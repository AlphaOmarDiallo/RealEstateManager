package com.example.realestatemanager.data.model.autocomplete

data class StructuredFormatting(
    val main_text: String,
    val main_text_matched_substrings: List<MainTextMatchedSubstring>,
    val secondary_text: String
)