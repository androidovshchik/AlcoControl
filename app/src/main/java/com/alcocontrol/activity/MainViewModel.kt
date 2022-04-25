package com.alcocontrol.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcocontrol.data.AssetRepository
import com.alcocontrol.data.DrinkRepository
import com.alcocontrol.data.Preferences
import com.alcocontrol.model.Alcohol
import com.alcocontrol.model.Dose
import com.alcocontrol.model.Drink
import com.alcocontrol.model.Plan
import com.alcocontrol.util.calculateBAC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: Preferences,
    private val drinkRepository: DrinkRepository,
    private val assetRepository: AssetRepository
) : ViewModel() {

    val doses = MutableSharedFlow<Dose>(Int.MAX_VALUE)

    val plan = MutableLiveData<Plan>()

    val drink = MutableLiveData<Drink>()

    val alcoholDrinks = flow {
        val map = withContext(Dispatchers.IO) {
            drinkRepository.allDrinks
        }
        emit(map)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyMap()
    )

    val alcoholChars = flow {
        val map = withContext(Dispatchers.IO) {
            assetRepository.allChars
        }
        emit(map)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyMap()
    )

    val alcoholKit: Map<Alcohol, Int>
        get() {
            return doses.replayCache.groupBy { it.drink.alcohol }
                .mapValuesTo(EnumMap(Alcohol::class.java), { it.value.size })
        }

    val commonTime: Long
        get() {
            doses.replayCache.minByOrNull { it.time.toEpochSecond(ZoneOffset.UTC) }?.let {
                val now = LocalDateTime.now()
                return Duration.between(it.time, now).seconds
            }
            return 0L
        }

    val commonVolume: Int
        get() {
            return doses.replayCache.sumOf { it.volume }
        }

    val commonPpm: Float
        get() {
            return calculateBAC(preferences.profile!!, preferences.feeling, doses.replayCache)
        }

    suspend fun readText(path: String): String {
        return withContext(Dispatchers.IO) {
            assetRepository.readFile(path)
        }
    }
}