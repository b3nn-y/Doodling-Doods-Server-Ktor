import com.example.playerManager.Player
import com.example.roomManager.CreateRoom
import com.example.roomManager.Room
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class RoomModerator {
    private var rooms = hashMapOf<String, Room>()
    private val roomJobs = mutableMapOf<String, Job>()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    fun addRoom(name: String, room: Room) {
        rooms[name] = room
        println(rooms)
        println("Room Added")
        val job = createBackgroundJob(name)
        roomJobs[name] = job

    }

    private fun createBackgroundJob(name: String): Job {
        return coroutineScope.launch {
            while (isActive) {
                try {
                    while (rooms[name]?.players?.size!! > 0){
                        println("$name is running with ${rooms[name]?.players!!.size} players")
                        println(rooms)
                        println(roomJobs)
                        delay(10000)
                    }
                    for (i in 1..10){
                        if (rooms[name]?.players!!.size == 0){
                            println("The room $name has no players and is waiting for players to join in $i/120 seconds, If not the room will be closed")
                            delay(1000)
                        }
                    }

                    if (rooms[name]?.players!!.size == 0){
                        deleteRoom(name)
                        break
                    }
                }
                catch (e:Exception){
                    println(e.message)
                }
            }
        }
    }
    fun getRoom(name: String): Room? {
        return try {
            rooms[name]
        } catch (e: Exception) {
            null
        }
    }

    fun getAllRooms(): HashMap<String, Room> {
        return rooms
    }

    fun deleteRoom(name: String) {
        try {
            println("Room $name is getting destroyed")
            rooms.remove(name)
            roomJobs[name]?.cancel()
            roomJobs.remove(name)
            println("Room $name is destroyed")
        } catch (e: Exception) {
            println("No Room present")
        }
    }
    fun addPlayerToRoom(name: String){
        rooms[name]?.players?.add(Player(name))
    }

}


