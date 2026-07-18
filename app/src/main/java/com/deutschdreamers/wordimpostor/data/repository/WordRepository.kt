package com.deutschdreamers.wordimpostor.data.repository

import com.deutschdreamers.wordimpostor.data.model.Difficulty
import com.deutschdreamers.wordimpostor.data.model.WordCategory
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

    // ── Themed word packs (used when a specific category is chosen; difficulty ignored) ──

    private val animalWords = listOf(
        "Dog", "Cat", "Horse", "Lion", "Tiger", "Bear", "Monkey", "Elephant",
        "Rabbit", "Mouse", "Duck", "Chicken", "Cow", "Pig", "Sheep", "Frog",
        "Snake", "Turtle", "Butterfly", "Bird", "Fish", "Shark", "Whale", "Dolphin",
        "Penguin", "Owl", "Eagle", "Wolf", "Fox", "Deer", "Giraffe", "Zebra",
        "Kangaroo", "Koala", "Panda", "Camel", "Crocodile", "Octopus", "Spider", "Bee"
    )

    private val foodWords = listOf(
        "Pizza", "Bread", "Cheese", "Apple", "Banana", "Orange", "Grape", "Lemon",
        "Mango", "Peach", "Cherry", "Carrot", "Potato", "Tomato", "Onion", "Corn",
        "Soup", "Salad", "Sandwich", "Cookie", "Candy", "Honey", "Coffee", "Tea",
        "Milk", "Rice", "Egg", "Butter", "Chocolate", "Pancake", "Burger", "Pasta",
        "Noodle", "Sushi", "Taco", "Popcorn", "Icecream", "Waffle", "Donut", "Yogurt"
    )

    private val placeWords = listOf(
        "Hospital", "School", "Library", "Museum", "Theater", "Stadium", "Airport",
        "Station", "Market", "Restaurant", "Hotel", "Park", "Bridge", "Tower",
        "Castle", "Temple", "Church", "Beach", "Mountain", "Forest", "Desert",
        "Island", "Village", "City", "Farm", "Zoo", "Bakery", "Factory", "Harbor",
        "Lighthouse", "Cave", "Volcano", "Waterfall", "Canyon", "Palace", "Garden"
    )

    private val sportWords = listOf(
        "Soccer", "Basketball", "Tennis", "Baseball", "Cricket", "Hockey", "Golf",
        "Swimming", "Running", "Cycling", "Boxing", "Wrestling", "Archery", "Skating",
        "Skiing", "Surfing", "Bowling", "Rugby", "Volleyball", "Badminton", "Rowing",
        "Diving", "Fencing", "Judo", "Karate", "Marathon", "Sprint", "Gymnastics",
        "Climbing", "Sailing", "Snowboarding", "Skateboarding", "Handball", "Polo"
    )

    private val scienceWords = listOf(
        "Gravity", "Atom", "Molecule", "Electron", "Planet", "Galaxy", "Comet",
        "Volcano", "Earthquake", "Magnet", "Electricity", "Energy", "Oxygen",
        "Hydrogen", "Cell", "Bacteria", "Virus", "Gene", "Fossil", "Mineral",
        "Crystal", "Rainbow", "Lightning", "Tornado", "Glacier", "Ecosystem",
        "Evolution", "Photosynthesis", "Orbit", "Telescope", "Microscope",
        "Experiment", "Chemical", "Nucleus", "Pressure", "Velocity"
    )

    /**
     * Returns a random secret word. For [WordCategory.MIXED] the [difficulty] selects the pool;
     * for a themed category the difficulty is ignored and the category's own pack is used.
     */
    fun getRandomWord(
        difficulty: Difficulty,
        category: WordCategory = WordCategory.MIXED
    ): String {
        val wordList = when (category) {
            WordCategory.MIXED -> when (difficulty) {
                Difficulty.EASY -> easyWords
                Difficulty.MEDIUM -> mediumWords
                Difficulty.HARD -> hardWords
            }

            WordCategory.ANIMALS -> animalWords
            WordCategory.FOOD -> foodWords
            WordCategory.PLACES -> placeWords
            WordCategory.SPORTS -> sportWords
            WordCategory.SCIENCE -> scienceWords
        }
        return wordList[Random.nextInt(wordList.size)]
    }
}