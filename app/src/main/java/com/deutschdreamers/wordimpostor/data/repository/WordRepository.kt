package com.deutschdreamers.wordimpostor.data.repository

import com.deutschdreamers.wordimpostor.data.model.Difficulty
import kotlin.random.Random

class WordRepository {

    private val easyWords = listOf(
        // Animals
        "Apple", "Dog", "Cat", "Tree", "Ball", "Sun", "Moon", "Water",
        "Fire", "House", "Car", "Book", "Phone", "Chair", "Table", "Door",
        "Window", "Flower", "Bird", "Fish", "Horse", "Cake", "Pizza", "Bread",
        "Milk", "Coffee", "Tea", "Rice", "Egg", "Cheese", "Orange", "Banana",
        "Shirt", "Pants", "Shoes", "Hat", "Rain", "Snow", "Cloud", "Star",
        // More animals
        "Lion", "Tiger", "Bear", "Monkey", "Elephant", "Rabbit", "Mouse", "Duck",
        "Chicken", "Cow", "Pig", "Sheep", "Frog", "Snake", "Turtle", "Butterfly",
        // More fruits & food
        "Grape", "Lemon", "Mango", "Peach", "Pear", "Cherry", "Berry", "Melon",
        "Carrot", "Potato", "Tomato", "Onion", "Pepper", "Corn", "Beans", "Soup",
        "Salad", "Sandwich", "Cookie", "Candy", "Honey", "Sugar", "Salt", "Butter",
        // Common objects
        "Pen", "Pencil", "Paper", "Bag", "Box", "Cup", "Plate", "Spoon",
        "Fork", "Knife", "Bottle", "Glass", "Clock", "Watch", "Key", "Lock",
        "Lamp", "Light", "Fan", "Bed", "Pillow", "Blanket", "Towel", "Soap",
        // Nature & weather
        "Wind", "Storm", "River", "Ocean", "Lake", "Beach", "Sand", "Rock",
        "Mountain", "Hill", "Forest", "Grass", "Leaf", "Branch", "Root", "Seed",
        // More everyday items
        "Toy", "Game", "Radio", "Music", "Song", "Dance", "Paint", "Color"
    )

    private val mediumWords = listOf(
        // Occupations
        "Doctor", "Teacher", "Engineer", "Artist", "Chef", "Pilot", "Lawyer",
        "Musician", "Architect", "Journalist", "Scientist", "Dentist", "Carpenter",
        "Plumber", "Electrician", "Mechanic", "Librarian", "Photographer", "Painter",
        "Sculptor", "Nurse", "Farmer", "Baker", "Tailor", "Barber", "Waiter",
        "Cashier", "Manager", "Designer", "Programmer", "Accountant", "Banker",
        "Soldier", "Firefighter", "Detective", "Judge", "Professor", "Student",
        // Tools & instruments
        "Hammer", "Screwdriver", "Wrench", "Saw", "Drill", "Ladder",
        "Microscope", "Telescope", "Calculator", "Keyboard", "Monitor", "Printer",
        "Scanner", "Compass", "Ruler", "Scissors", "Needle", "Thread", "Brush",
        "Shovel", "Rake", "Axe", "Chisel", "Pliers", "Tongs", "Tweezers",
        // Household appliances
        "Refrigerator", "Microwave", "Dishwasher", "Vacuum", "Iron",
        "Blender", "Toaster", "Oven", "Stove", "Kettle", "Mixer", "Juicer",
        "Heater", "Cooler", "Washer", "Dryer", "Freezer", "Grill", "Steamer",
        // Musical instruments & entertainment
        "Camera", "Guitar", "Piano", "Violin", "Drums", "Flute", "Trumpet",
        "Saxophone", "Clarinet", "Harmonica", "Accordion", "Banjo", "Cello",
        // Sports & activities
        "Soccer", "Basketball", "Tennis", "Baseball", "Cricket", "Hockey",
        "Swimming", "Running", "Cycling", "Hiking", "Camping", "Fishing",
        "Bowling", "Boxing", "Wrestling", "Archery", "Skating", "Skiing",
        // Places & structures
        "Hospital", "School", "Library", "Museum", "Theater", "Stadium",
        "Airport", "Station", "Market", "Restaurant", "Hotel", "Park",
        "Bridge", "Tower", "Castle", "Temple", "Church", "Mosque"
    )

    private val hardWords = listOf(
        // Abstract concepts & virtues
        "Democracy", "Justice", "Freedom", "Courage", "Wisdom", "Patience",
        "Ambition", "Nostalgia", "Serendipity", "Melancholy", "Euphoria",
        "Integrity", "Empathy", "Resilience", "Humility", "Compassion",
        "Perseverance", "Gratitude", "Tolerance", "Dignity", "Honor",
        // Literary & rhetorical terms
        "Paradox", "Irony", "Metaphor", "Symbolism", "Abstract", "Concrete",
        "Allegory", "Hyperbole", "Oxymoron", "Euphemism", "Alliteration",
        "Personification", "Foreshadowing", "Imagery", "Satire", "Parody",
        // Academic fields
        "Hypothesis", "Theory", "Philosophy", "Psychology", "Sociology",
        "Anthropology", "Archaeology", "Astronomy", "Geology", "Biology",
        "Chemistry", "Physics", "Mathematics", "Economics", "Linguistics",
        "Theology", "Ethics", "Aesthetics", "Metaphysics", "Epistemology",
        // Scientific & technical terms
        "Algorithm", "Fractal", "Quantum", "Relativity", "Entropy",
        "Catalyst", "Synthesis", "Analysis", "Equilibrium", "Momentum",
        "Trajectory", "Velocity", "Acceleration", "Wavelength", "Frequency",
        "Spectrum", "Molecule", "Atom", "Nucleus", "Electron",
        // Complex concepts
        "Dialectic", "Dichotomy", "Juxtaposition", "Ambiguity",
        "Conundrum", "Enigma", "Labyrinth", "Anomaly", "Paradigm",
        "Phenomenon", "Hierarchy", "Democracy", "Bureaucracy", "Meritocracy",
        "Symbiosis", "Evolution", "Revolution", "Innovation", "Transformation"
    )

    fun getRandomWord(difficulty: Difficulty): String {
        val wordList = when (difficulty) {
            Difficulty.EASY -> easyWords
            Difficulty.MEDIUM -> mediumWords
            Difficulty.HARD -> hardWords
        }
        return wordList[Random.nextInt(wordList.size)]
    }

}