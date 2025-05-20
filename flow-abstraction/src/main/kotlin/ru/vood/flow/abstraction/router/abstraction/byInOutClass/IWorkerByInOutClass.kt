package ru.vood.flow.abstraction.router.abstraction.byInOutClass

import ru.vood.flow.abstraction.router.abstraction.IWorker
import ru.vood.flow.abstraction.router.abstraction.IWorkerSingleId

/**
 * Интерфейс рабочего объекта, используемого для обработки входных данных типа [T]
 * и возвращения результата типа [R]. Основное отличие заключается в специализации
 * идентификатора рабочего объекта через интерфейс [IWorkerIdByInOutClass].
 *
 * @param T Общий тип входных данных, принимаемых рабочим объектом.
 * @param R Общий тип результата, возвращаемого рабочим объектом.
 * @param WORKER_ID Специализированный тип идентификатора рабочего объекта, унаследованный от [IWorkerIdByInOutClass].
 */
interface IWorkerByInOutClass<
        T : Any,
        out R : Any,
        WORKER_ID : IWorkerIdByInOutClass<T, R>
        > : IWorker<T, R, WORKER_ID>


interface IWorkerByInOutClassSingleId<
        T : Any,
        out R : Any,
        WORKER_ID : IWorkerIdByInOutClass<T, R>
        > : IWorkerByInOutClass<T, R, WORKER_ID>, IWorkerSingleId<T, R, WORKER_ID>