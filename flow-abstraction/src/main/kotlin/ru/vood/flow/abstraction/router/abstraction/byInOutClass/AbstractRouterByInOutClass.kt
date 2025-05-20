package ru.vood.flow.abstraction.router.abstraction.byInOutClass

import ru.vood.flow.abstraction.router.abstraction.AbstractRouter

/**
 * Абстрактный класс роутера, расширяющий базовый класс [AbstractRouter]. Предназначен для маршрутизации запросов
 * к рабочим объектам, используя специализированные идентификаторы [IWorkerIdByInOutClass].
 *
 * Данный класс предоставляет удобную реализацию механизма маршрутизации, основанную на сопоставлении идентификаторов
 * рабочих объектов с типом входных и выходных данных.
 *
 * @param T Тип входных данных, обрабатываемых рабочими объектами.
 * @param R Тип результата, возвращаемого рабочими объектами.
 * @param wId Специализированный тип идентификатора рабочего объекта, реализуемый интерфейсом [IWorkerIdByInOutClass].
 * @param worker Тип рабочего объекта, реализующего интерфейс [IWorkerByInOutClass].
 */
abstract class AbstractRouterByInOutClass<
        T : Any,
        R : Any,
        wId : IWorkerIdByInOutClass<T, R>,
        worker : IWorkerByInOutClass<T, R, wId>
        >(
    iWorkerList: List<worker>
) : AbstractRouter<T, R, wId, worker>(iWorkerList) {

    /**
     * Маршрутизирует данные к необходимому рабочему объекту на основании переданного идентификатора.
     *
     * @param IT Реализованный тип входных данных.
     * @param IR Реализованный тип возвращаемых результатов.
     * @param data Лямбда-выражение, возвращающее входные данные.
     * @param idF Лямбда-выражение, возвращающее идентификатор рабочего объекта.
     * @return Результат работы соответствующего рабочего объекта.
     * @throws Exception Если рабочий объект с указанным идентификатором не найден.
     */
    suspend inline fun <reified IT : T, reified IR : R> mapData(
        /** Лямбда, возвращающая входные данные*/
        crossinline data: suspend () -> IT,
        /** Лямбда, возвращающая идентификатор рабочего объекта*/
        idF: () -> wId
    ): IR {
        // Получаем идентификатор рабочего объекта
        val validateMapperId = idF()
        return data().let { dataDto ->  // Получаем входные данные
            routedMap[validateMapperId]?.doWork(dataDto, validateMapperId) as IR? // Выполняем операцию рабочим объектом
        }
            ?: error("For router ${this::class.java.canonicalName} not found worker with Id $validateMapperId") // Генерация исключения при отсутствии рабочего объекта
    }
}