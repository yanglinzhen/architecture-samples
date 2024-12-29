package com.example.android.architecture.blueprints.todoapp

import android.os.Build
import androidx.annotation.RequiresApi

interface State

interface Store<S : State> {
    var state: S
    fun addActionHandler(actionHandler: ActionHandler)
    fun onAction(action: Action<*>)
    fun performAction(action: Action<*>)
}

interface Action<S : State> {
    fun execute(state: S): S
}

class ActionManager {
    private val actionStoreMap = mutableMapOf<Class<out Action<*>>, MutableList<Store<*>>>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun <S : State, T : Action<*>> registerAction(action: Class<T>, vararg store: Store<S>) {
        actionStoreMap.computeIfAbsent(action) { mutableListOf() }.addAll(store)
    }

    fun executeAction(action: Action<*>) {
        println("ActionManager executeAction: $action")
        actionStoreMap[action::class.java]?.forEach {
            it.onAction(action)
        }

    }
}

// Define a concrete State
data class CounterState(val count: Int) : State

// Define a concrete Action
class IncrementAction : Action<CounterState> {
    override fun execute(state: CounterState): CounterState {
        return CounterState(state.count + 1)
    }
}

class DecrementAction : Action<CounterState> {
    override fun execute(state: CounterState): CounterState {
        return CounterState(state.count - 1)
    }
}

// Define a concrete Store
class CounterStore(

    override var state: CounterState

) : Store<CounterState> {
    private var actionHandler: ActionHandler? = null
    override fun addActionHandler(actionHandler: ActionHandler) {
        this.actionHandler = actionHandler
    }

    override fun onAction(action: Action<*>) {
        println("CounterStore onAction: $state")
        println("CounterStore onAction: $action")
    }

    override fun performAction(action: Action<*>) {
        println("CounterStore performAction: $state")
        println("CounterStore performAction: $action")
        actionHandler?.handle(action)
    }
}

interface ComponentMiddleware<T : Action<*>, R : Action<*>> {
    operator fun invoke(action: T, next: (R) -> Unit)
}

class SimpleMiddleware(
    val value1: Int,
    val value2: Int
) : ComponentMiddleware<IncrementAction, DecrementAction> {
    override fun invoke(
        action: IncrementAction,
        next: (DecrementAction) -> Unit
    ) {
        println("Run middleware: $this, action: $action")
        next(DecrementAction())
    }

}

interface ActionHandler {
    fun <T : Action<*>> handle(action: T)
}

class CommonActionHandler(
    private val actionManager: ActionManager
) : ActionHandler {
    private val middlewareList: MutableList<ComponentMiddleware<Action<*>, Action<*>>> by lazy {
        mutableListOf()
    }

    fun <T : ComponentMiddleware<*, *>> applyMiddlewares(vararg middlewares: T) {
        middlewares.filterIsInstance<ComponentMiddleware<Action<*>, Action<*>>>().also {
            middlewareList.addAll(it)
        }
    }

    override fun <T : Action<*>> handle(action: T) {
        println("CommonActionHandler handle: $action")
        when (action) {
            is IncrementAction -> {
                middlewareList.fold(action) { acc, middleware ->
                    middleware(acc) { newAction ->
                        actionManager.executeAction(newAction)
                    }
                    acc
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.N)
fun main() {
    val actionManager = ActionManager()
    val commonActionHandler = CommonActionHandler(actionManager).also {
        it.applyMiddlewares(SimpleMiddleware(1, 2))
    }

    val counterStore = CounterStore(CounterState(0))
    actionManager.registerAction(IncrementAction::class.java, counterStore)

    counterStore.addActionHandler(commonActionHandler)
    counterStore.performAction(IncrementAction())
//    actionManager.executeAction(IncrementAction())

    println(counterStore.state)
}