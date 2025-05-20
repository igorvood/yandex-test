package ru.vood.flow.abstraction.router.abstraction.enumR

import ru.vood.flow.abstraction.router.abstraction.IRouter
import kotlin.enums.EnumEntries

/**
 * Абстрактный класс маршрутизатора для работы с перечисляемыми значениями (enums),
 * обеспечивающий управление выполнением различных обработчиков (workers) на основе указанных значений.
 *
 * Этот класс обеспечивает проверку наличия соответствующих обработчиков для каждого перечислимого значения
 * и вызывает соответствующий обработчик для указанного значения.
 *
 * @param T Тип входных данных.
 * @param R Тип результата.
 * @param E Перечисление, используемое для управления обработчиками.
 * @param iWorkerList Список зарегистрированных обработчиков.
 * @param eVals Набор допустимых значений перечисления.
 */
abstract class AbstractEnumRouter<T : Any, R : Any, E>
    (
    iWorkerList: List<IEnumWorker<T, R, E>>,
    eVals: EnumEntries<E>,
) : IRouter<
        T,
        R,
        E,
        IEnumWorker<T, R, E>
        >
        where E : Enum<E> {

    /**
     * Проверяет наличие всех необходимых обработчиков для каждого перечислимого значения.
     * Если какой-то обработчик отсутствует, выбрасывается исключение.
     */
    init {
        val workerIEnumWorker = iWorkerList.flatMap { it.workerIds }.map { it.name }.toSet()
        val filter = eVals.filter { !workerIEnumWorker.contains(it.name) }
        require(filter.isEmpty()) {
            """Fot router ${this::class.java.canonicalName} 
            |           Not found worker implementation ${IEnumWorker::class.java.canonicalName} 
            |           for next EnumValues $filter of Enum type ${eVals.first()::class.java.canonicalName}""".trimMargin()
        }
    }


    /**
     * Выполняет обработку данных с использованием соответствующего обработчика,
     * выбранного на основании заданного перечислимого значения.
     *
     * @param data Заготовка данных для обработки.
     * @param enumF Функциональный блок, возвращающий значение перечисления.
     * @return Преобразованное значение типа R.
     */
    suspend inline fun <reified IT : T, reified IR : R> mapData(
        crossinline data: suspend () -> IT,
        enumF: () -> E
    ): R {
        val emun = enumF()
        return data().let { dataDto ->
            routedMap[emun]?.doWork(dataDto, emun)
        } ?: error("For router ${this::class.java.canonicalName} not found worker with Id $emun")
    }
}