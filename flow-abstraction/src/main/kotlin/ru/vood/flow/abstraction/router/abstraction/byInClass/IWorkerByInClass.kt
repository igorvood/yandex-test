package ru.vood.flow.abstraction.router.abstraction.byInClass

import ru.vood.flow.abstraction.router.abstraction.IWorker
import ru.vood.flow.abstraction.router.abstraction.IWorkerSingleId

/**
 * Интерфейс рабочего объекта, предназначенного для обработки данных определенного типа и возвращения результата другого типа.
 * Отличительной особенностью является зависимость от специализированного идентификатора [IWorkerIdByInClass],
 * что позволяет применять дополнительные правила проверки и фильтрации данных.
 *
 * @param T Тип входных данных, принимаемых рабочим объектом.
 * @param R Тип результата, возвращаемого рабочим объектом.
 * @param WORKER_ID Специфический тип идентификатора рабочего объекта, унаследованный от [IWorkerIdByInClass].
 */
interface IWorkerByInClass<
        /** Тип входных данных*/
        T : Any,
        /** Тип возвращаемых данных*/
        out R : Any,
        /**Специфичный тип идентификатора*/
        WORKER_ID : IWorkerIdByInClass<T>> : IWorker<T, R, WORKER_ID>

interface IWorkerByInClassSingleId<
        /** Тип входных данных*/
        T : Any,
        /** Тип возвращаемых данных*/
        out R : Any,
        /**Специфичный тип идентификатора*/
        WORKER_ID : IWorkerIdByInClass<T>> : IWorkerByInClass<T, R, WORKER_ID>, IWorkerSingleId<T, R, WORKER_ID>