package ru.vood.flow.abstraction.router.abstraction.byInClass

import ru.vood.flow.abstraction.router.abstraction.AbstractRouter

/**
 * Абстрактный класс роутера, расширяющий базовый класс [AbstractRouter], предназначенный для удобной маршрутизации данных по идентификаторам рабочих объектов.
 *
 * Данный класс предоставляет дополнительную функциональность поверх базовой маршрутизации, позволяя гибко управлять обработкой данных в зависимости от конкретного рабочего объекта.
 *
 * @param T Общий тип входных данных, которые будут переданы рабочим объектам.
 * @param R Общий тип результата, возвращаемый рабочими объектами.
 * @param wId Тип идентификатора рабочего объекта, реализуемого интерфейсом [IWorkerIdByInClass].
 * @param worker Тип рабочего объекта, реализованного интерфейсом [IWorkerByInClass].
 */
abstract class AbstractRouterByInClass<
        T : Any,
        R : Any,
        wId : IWorkerIdByInClass<T>,
        worker : IWorkerByInClass<T, R, wId>
        >(
    iWorkerList: List<worker>
) : AbstractRouter<T, R, wId, worker>(iWorkerList) {

    /**
     * Подготавливает и отправляет данные нужному рабочему объекту, основываясь на его идентификаторе.
     *
     * @param IT Реализованный тип входных данных.
     * @param IR Реализованный тип возвращаемых результатов.
     * @param data Лямбда-выражение, которое получает входные данные.
     * @param idF Лямбда-выражение, возвращающее идентификатор рабочего объекта.
     * @return Результат работы рабочего объекта.
     * @throws RuntimeException Если рабочий объект с указанным идентификатором не найден.
     */
    suspend inline fun <reified IT : T, reified IR : R> mapData(
        crossinline data: suspend () -> IT,
        idF: () -> wId
    ): IR {
        // Получаем идентификатор рабочего объекта
        val validateMapperId = idF()
        // Получаем входные данные
        return data().let { dataDto ->
            // Выполняем работу нужным рабочим объектом
            routedMap[validateMapperId]?.doWork(dataDto, validateMapperId) as IR?
        } ?: error("For router ${this::class.java.canonicalName} not found worker with Id $validateMapperId")
    }
}