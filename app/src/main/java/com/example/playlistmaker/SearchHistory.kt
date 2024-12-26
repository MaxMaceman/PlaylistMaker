import android.content.SharedPreferences
import com.example.playlistmaker.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    private val gson = Gson()
    private val KEY_SEARCH_HISTORY_LIST = "search_history_list"
    private val MAX_HISTORY_SIZE = 10

    // Получение указанных настроек
    private fun getSharedPrefs(key: String): String? {
        return sharedPrefs.getString(key, null)
    }

    // Преобразование списка треков в JSON
    fun arrayListToJson(arrayList: List<Track>): String {
        return gson.toJson(arrayList)
    }

    // Преобразование JSON строки в список треков
    fun jsonToArrayList(json: String?): ArrayList<Track> {
        return if (json != null) {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }

    // Добавление трека в историю
    fun addSong(track: Track) {
        val trackList = getSearchHistory().toMutableList()

        // Убираем трек, если он уже есть
        trackList.removeIf { it.trackId == track.trackId }

        // Добавляем трек в начало списка
        trackList.add(0, track)

        // Ограничиваем количество треков в истории
        if (trackList.size > MAX_HISTORY_SIZE) {
            trackList.removeAt(trackList.size - 1)
        }

        // Сохраняем обновленный список
        saveSearchHistory(trackList)
    }

    // Получаем историю поиска
    fun getSearchHistory(): List<Track> {
        val jsonHistory = getSharedPrefs(KEY_SEARCH_HISTORY_LIST) ?: return emptyList()
        return jsonToArrayList(jsonHistory)
    }

    // Сохранение истории поиска
    private fun saveSearchHistory(songs: List<Track>) {
        val json = arrayListToJson(songs)
        sharedPrefs.edit().putString(KEY_SEARCH_HISTORY_LIST, json).apply()
    }

    // Очистка истории поиска
    fun clearHistory() {
        sharedPrefs.edit().remove(KEY_SEARCH_HISTORY_LIST).apply()
    }
}
