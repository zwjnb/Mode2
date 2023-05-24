package com.example.lib_util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * 倒计时控件
 */
class DownTimer {

   companion object{
      fun downTimer(startTime:Int,start:() ->Unit,completion:() ->Unit,each:(Int) ->Unit,lifecycleCoroutineScope: CoroutineScope){
         flow<Int> {
            for (i in startTime downTo 0){
               withContext(Dispatchers.IO){
                  delay(1000)
               }
               emit(i)
            }
         }.flowOn(Dispatchers.Main)
          .onStart { start.invoke() }
          .onCompletion {
               completion.invoke()
            }
          .onEach {
               each.invoke(it)
            }
          .launchIn(lifecycleCoroutineScope)

      }
   }
}