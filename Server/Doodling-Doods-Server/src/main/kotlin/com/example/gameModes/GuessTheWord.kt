package com.example.gameModes

import com.example.playerManager.PlayerTurnModerator
import com.example.roomManager.Room
import com.example.roomManager.RoomModerator
import kotlinx.coroutines.*

//aka scribble
class GuessTheWord : PlayerTurnModerator() {

    lateinit var customWords: MutableList<String>

    var chosenWords = arrayListOf<String>()

    var noOfRoundsOver = 0

    val zohoProducts = arrayListOf("Creator", "Mail", "Sheet", "Show", "Cliq", "Connect", "Learn", "People", "Recruit", "Writer", "Sign", "Bigin", "Assist", "SalesIq", "Survey", "BackStage", "Payroll", "Project", "DataPrep", "Meeting", "Inventory", "Analytics", "Sprints", "Flow", "Desk", "Campaigns", "Forms", "Survey", "Sites", "PageSense", "Expense", "Checkout", "Work Drive", "Tables", "Catalyst", "Vault", "Site24x7")

    val generalItems = arrayListOf("diary", "bottle", "water", "packet", "chewing gum", "tissue", "watch", "photo", "coin", "brush", "coin", "phone", "umbrella", "pencil", "scissors", "comb", "laptop", "headphone","diary", "bottle", "water", "packet", "chewing gum", "tissue", "watch", "photo", "coin", "brush", "phone", "umbrella", "pencil", "scissors", "comb", "laptop", "headphone", "book", "pen", "paper", "key", "chair", "table", "bed", "lamp", "clock", "computer", "bag", "wallet", "car", "house", "door", "window", "wall", "floor", "ceiling", "light bulb", "ruler", "stapler", "eraser", "sharpener", "calculator", "glasses", "hat", "scarf", "gloves", "jacket", "pants", "shirt", "dress", "shoes", "socks", "underwear", "mirror", "shampoo", "soap", "conditioner", "toothbrush", "toothpaste", "towel", "toilet paper", "napkin", "sponge", "soap dish", "bathmat", "shower curtain", "bathtub", "sink", "toilet", "faucet", "drain", "cabinet", "drawer", "counter", "stove", "oven", "refrigerator", "microwave", "dishwasher", "blender", "toaster", "coffee maker", "kettle", "pot", "pan", "spoon", "spatula", "knife", "serving dish", "fork", "glass", "plate", "bowl", "cup", "mug", "pitcher", "tablecloth", "napkin ring", "placemat", "centerpiece", "painting", "picture frame", "vase", "plant", "lampshade", "rug", "carpet", "curtain", "blind", "shelf", "bookcase", "sofa", "armchair", "loveseat", "ottoman", "coffee table", "end table", "nightstand", "dresser", "bed frame", "mattress", "pillow", "blanket", "sheet", "alarm clock", "radio", "television", "speaker", "computer monitor", "keyboard", "mouse", "printer", "scanner", "camera", "phone charger", "USB cable", "headphone", "power strip", "battery", "light switch", "doorknob", "window handle", "lock", "mailbox", "trash can", "recycling bin", "rake", "shovel", "broom", "dustpan", "mop", "vacuum cleaner", "lawn mower", "hose", "watering can", "seeds", "fertilizer", "plant pot", "toolbox", "hammer", "saw", "screwdriver", "wrench", "nails", "screws", "tape measure", "level", "flashlight", "fire extinguisher", "first aid kit", "bandage", "thermometer", "medicine", "cotton swab", "adhesive bandage", "lighter", "matches", "candle", "lighter fluid", "ashtray", "umbrella", "sunglasses",
        "belt", "wallet chain", "keychain", "jewelry", "necklace", "bracelet", "ring", "earrings", "lighter", "ashtray", "purse", "backpack", "briefcase", "suitcase", "luggage", "travel pillow", "headphones", "speaker", "camera", "phone case", "charger", "power bank", "cable", "adapter", "airplane ticket", "passport", "visa", "itinerary", "boarding pass", "hotel reservation", "confirmation email", "credit card", "debit card", "cash", "traveler's checks", "sunscreen", "sunglasses", "hat", "scarf", "gloves", "jacket", "coat", "sweater", "t-shirt", "jeans", "leggings", "dress", "skirt", "shorts", "pants", "shoes", "boots", "sandals", "sneakers", "watch", "glasses case", "contact lens solution", "fan", "hair dryer", "straightener", "curler", "comb", "brush", "mirror", "makeup", "lotion", "perfume", "cologne", "deodorant", "razor", "shaving cream", "mouthwash", "body wash", "bathrobe", "slippers", "pajamas", "nightgown", "underwear", "bra", "socks",
        "pillowcase", "sheet set", "duvet cover", "comforter", "blanket", "throw pillow", "alarm clock", "radio", "television", "speaker", "computer monitor", "keyboard", "mouse", "printer", "scanner", "camera", "phone charger", "USB cable", "headphone", "power strip", "battery", "light switch", "doorknob", "window handle", "lock", "mailbox", "trash can", "recycling bin", "rake", "shovel", "broom", "dustpan", "mop", "vacuum cleaner", "lawn mower", "hose", "watering can", "gardening tools", "seeds", "fertilizer", "plant pot", "potting soil", "gloves", "hammer", "saw", "screwdriver", "wrench", "nails", "screws", "tape measure", "level", "flashlight", "toolbox", "fire extinguisher", "first aid kit", "bandage", "thermometer", "medicine", "cotton swab", "adhesive bandage", "lighter", "matches", "candle", "lighter fluid", "matches", "ashtray", "umbrella", "sunglasses", "watch band", "belt", "wallet chain", "keychain", "jewelry",  "necklace", "bracelet", "ring", "earrings", "watch", "phone case", "purse", "backpack", "gym bag", "briefcase", "suitcase", "tote bag", "cooler", "picnic basket", "travel mug", "water bottle", "thermos", "lunchbox", "food container", "peeler", "grater", "strainer", "colander", "whisk", "spatula", "rolling pin", "cutting board", "measuring cup", "measuring spoon", "mixing bowl", "baking sheet", "muffin tin", "casserole dish", "casserole lid", "pizza stone", "cookie sheet", "cake pan", "loaf pan", "pie dish", "baking dish", "roasting pan", "aluminum foil", "plastic wrap", "parchment paper", "wax paper", "oven mitt", "pot holder", "dish towel", "kitchen towel", "washcloth", "cleaning cloth", "sponge", "dish soap", "dishwasher soap", "laundry detergent", "fabric softener", "bleach", "cleaning supplies", "disinfectant wipes", "air freshener", "toilet bowl cleaner", "furniture polish", "glass cleaner", "floor cleaner", "dust mop", "feather duster", "vacuum cleaner bags", "vacuum cleaner filters", "light bulbs", "batteries", "trash bags", "recycling bags", "paper towels", "toilet paper", "tissues", "napkins", "aluminum foil", "plastic wrap", "parchment paper", "wax paper", "oven mitt", "pot holder", "dish towel", "kitchen towel", "washcloth", "cleaning cloth", "sponge", "dish soap", "dishwasher soap", "laundry detergent", "fabric softener", "bleach", "cleaning supplies", "disinfectant wipes", "air freshener", "toilet bowl cleaner", "furniture polish", "glass cleaner", "floor cleaner","dust mop", "feather duster", "vacuum cleaner bags", "vacuum cleaner filters", "light bulbs", "batteries", "trash bags", "recycling bags", "paper towels", "toilet paper", "tissues", "napkins", "stapler", "eraser", "sharpener", "calculator", "glasses", "hat", "scarf", "gloves", "jacket", "pants", "shirt", "dress", "shoes", "socks", "underwear", "mirror", "comb", "brush", "shampoo", "soap", "conditioner", "toothbrush", "toothpaste", "towel", "toilet paper", "napkin", "sponge", "soap dish", "bathmat", "shower curtain", "bathtub", "sink", "toilet", "faucet", "drain", "cabinet", "drawer", "counter", "stove", "oven", "refrigerator", "microwave", "dishwasher", "blender", "toaster", "coffee maker", "kettle", "pot", "pan", "spoon", "spatula", "knife", "serving dish", "fork", "glass", "plate", "bowl", "cup", "mug", "pitcher", "tablecloth", "napkin ring", "placemat", "centerpiece", "painting", "picture frame", "vase", "plant", "lampshade"
    )

    val softwareDevTerms = arrayListOf("bug", "click",
        "copy", "cut", "delete", "download", "drag and drop", "error", "file", "folder", "icon", "menu", "open", "paste", "print", "refresh", "save", "search", "select", "window", "API", "algorithm", "app", "background", "bar chart", "battery", "bookmark", "browse", "browser", "button", "cable", "calendar", "camera", "cancel", "cell", "chat", "check", "checkbox", "chip", "circuit", "close", "cloud", "code", "collapse", "color", "comment", "confirm", "connect", "connection", "console", "copy", "create", "cursor", "data", "database", "date", "debug", "delete", "desktop", "device", "dialog", "direction", "disk", "display", "document", "download", "drag", "drop", "edit", "email", "enter", "error", "exit", "expand", "export", "favorite", "feedback", "field", "file", "filter", "find", "folder", "font", "form", "forward", "function", "gear", "graph", "grid", "group", "hardware", "hashtag", "header", "help", "history", "home", "icon", "image", "import", "inbox", "increase", "indent", "information", "input", "install", "instruction", "interface", "internet", "key", "keyboard", "language", "layout", "like", "line", "link", "list", "location", "lock", "log", "login", "logout", "loop", "mail", "mainframe", "manage", "map", "margin", "mark", "memory", "menu", "message", "microphone", "minimize", "minus", "mobile", "modal", "model", "modify", "monitor", "mouse", "move", "multiple", "mute", "name", "navigate", "network", "new", "next", "node", "notification", "number", "ok", "online", "open", "operation", "option", "order", "organize", "output", "outbox", "overlay", "overview", "package", "page", "paragraph", "password", "paste", "pause", "pen", "permission", "pie chart", "pin", "play", "plugin", "pointer", "pop-up", "port", "position", "power", "presentation", "preview", "previous", "print", "printer", "private", "process", "profile", "progress", "program", "prompt", "property", "public", "publish", "pull", "push", "radio button")

    val professions = arrayListOf("Doctor", "Nurse", "Dentist", "Pharmacist", "Therapist", "Surgeon", "Veterinarian",
        "Teacher", "Professor", "Counselor", "Librarian", "Early Childhood Educator", "Special Education Teacher",
        "Accountant", "Marketing Manager", "Sales Representative", "Human Resources Manager", "Financial Analyst", "Entrepreneur",
        "Software Engineer", "Data Scientist", "Web Developer", "Network Engineer", "Electrical Engineer", "Mechanical Engineer",
        "Lawyer", "Judge", "Police Officer", "Firefighter", "Social Worker", "Politician",
        "Journalist", "Writer", "Editor", "Public Relations Specialist", "Graphic Designer", "Videographer",
        "Customer Service Representative", "Sales Associate", "Cashier", "Waiter/Waitress", "Bartender", "Retail Manager",
        "Electrician", "Plumber", "Carpenter", "Welder", "Mechanic", "Construction Worker",
        "Musician", "Actor", "Artist", "Graphic Designer", "Photographer", "Writer",
        "Scientist", "Researcher", "Pilot", "Flight Attendant", "Athlete", "Chef", "Travel Agent")

    private val gameScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var time = 0
    fun playGuessTheWord(room: String) {
        gameScope.launch {
            var roomData = RoomModerator.getRoom(room)
            noOfRounds(4)
            for (i in 1..4) {
                RoomModerator.getRoom(room)?.let { updatePlayerDetails(it.players) }

                if (i!=4){
                    do {
                        val player = getCurrentPlayer()
                        println("Current player in $room is ${player?.name}")
                        if (player != null) {
                            val tempListOfWords = ArrayList<String>()
                            for (i in 1..3){
                                tempListOfWords.add(zohoProducts.random())
                            }
                            RoomModerator.getRoom(room)?.wordList = tempListOfWords
                            RoomModerator.getRoom(room)?.currentPlayer = player
                            RoomModerator.getRoom(room)?.isWordChosen = false
                            RoomModerator.getRoom(room)?.numberOfRoundsOver = i-1
                        if (roomData != null) {
                            RoomModerator.sendUpdatesToEveryoneInARoom(room)
                        }
//                        var wordWaitingTime = 0
//                        while (wordWaitingTime<5){
//                            wordWaitingTime++
//                            println("word123 "+RoomModerator.getRoom(room)?.currentWordToGuess)
//                            delay(1000)
//                        }

                            while (RoomModerator.isWordChosen[room] == false){

                            }

                            RoomModerator.sendUpdatesToEveryoneInARoom(room)

                            while (time < 15) {
                                time++
                                RoomModerator.getRoom(room)?.timer = time
                                if (roomData != null) {
                                    RoomModerator.sendUpdatesToEveryoneInARoom(room)
                                }
                                delay(1000)
                                println("Timer "+time)
                                if (getPlayerName().size == RoomModerator.getRoom(room)?.guessedPlayers?.size){
                                    if (getPlayerName() == RoomModerator.getRoom(room)?.guessedPlayers){
                                        break
                                    }
                                }
                            }
                            time = 0
                            roomData = RoomModerator.getRoom(room)
                            roomData?.cords = ""
                            roomData?.iosCords= arrayListOf()
                            RoomModerator.isWordChosen[room] = false
//                        roomData?.currentWordToGuess = "loading"
                        if (roomData != null) {
                            RoomModerator.updateRoomDataAndSend(room, roomData)
                        }
                        }

                    } while (player != null)
                    println("Round $i over\n")
                    RoomModerator.getRoom(room)?.numberOfRoundsOver = i-1
                    RoomModerator.sendUpdatesToEveryoneInARoom(room)
                }
                else{
                    RoomModerator.getRoom(room)?.gameOver = true
                    RoomModerator.sendUpdatesToEveryoneInARoom(roomName = room)
                }
            }




        }
//        RoomModerator.getRoom(room)?.gameStarted = false



    }

    suspend fun checkIfTheWordIsChosen(room: String, listOfWords: ArrayList<String>): String? {
        delay(5000)
        return RoomModerator.getRoom(room)?.currentWordToGuess
    }
}