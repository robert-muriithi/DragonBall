package com.robert.common.dispatcher

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val mainImmediate: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}

class DefaultDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val mainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}

val LocalDispatcherProvider: ProvidableCompositionLocal<DispatcherProvider> =
    staticCompositionLocalOf { DefaultDispatcherProvider() }

