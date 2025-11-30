package com.deutschdreamers.wordimpostor.data.repository

import com.deutschdreamers.wordimpostor.data.model.Difficulty
import kotlin.random.Random

class WordRepository {

    private val easyWords = listOf(
        "Apple", "Dog", "Cat", "Tree", "Ball", "Sun", "Moon", "Water",
        "Fire", "House", "Car", "Book", "Phone", "Chair", "Table", "Door",
        "Window", "Flower", "Bird", "Fish", "Horse", "Cake", "Pizza", "Bread",
        "Milk", "Coffee", "Tea", "Rice", "Egg", "Cheese", "Orange", "Banana",
        "Shirt", "Pants", "Shoes", "Hat", "Rain", "Snow", "Cloud", "Star"
    )

    private val mediumWords = listOf(
        "Doctor", "Teacher", "Engineer", "Artist", "Chef", "Pilot", "Lawyer",
        "Musician", "Architect", "Journalist", "Scientist", "Dentist", "Carpenter",
        "Plumber", "Electrician", "Mechanic", "Librarian", "Photographer", "Painter",
        "Sculptor", "Hammer", "Screwdriver", "Wrench", "Saw", "Drill", "Ladder",
        "Microscope", "Telescope", "Calculator", "Keyboard", "Monitor", "Printer",
        "Scanner", "Refrigerator", "Microwave", "Dishwasher", "Vacuum", "Iron",
        "Blender", "Toaster", "Camera", "Guitar", "Piano", "Violin"
    )

    private val hardWords = listOf(
        "Democracy", "Justice", "Freedom", "Courage", "Wisdom", "Patience",
        "Ambition", "Nostalgia", "Serendipity", "Melancholy", "Euphoria",
        "Paradox", "Irony", "Metaphor", "Symbolism", "Abstract", "Concrete",
        "Hypothesis", "Theory", "Philosophy", "Psychology", "Sociology",
        "Anthropology", "Archaeology", "Astronomy", "Geology", "Biology",
        "Chemistry", "Physics", "Mathematics", "Algorithm", "Fractal",
        "Quantum", "Relativity", "Entropy", "Catalyst", "Synthesis",
        "Analysis", "Dialectic", "Dichotomy", "Juxtaposition", "Ambiguity",
        "Conundrum", "Enigma", "Labyrinth"
    )

    fun getRandomWord(difficulty: Difficulty): String {
        val wordList = when (difficulty) {
            Difficulty.EASY -> easyWords
            Difficulty.MEDIUM -> mediumWords
            Difficulty.HARD -> hardWords
        }
        return wordList[Random.nextInt(wordList.size)]
    }

    fun getWordList(difficulty: Difficulty): List<String> {
        return when (difficulty) {
            Difficulty.EASY -> easyWords
            Difficulty.MEDIUM -> mediumWords
            Difficulty.HARD -> hardWords
        }
    }
}