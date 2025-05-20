package ru.vood.flow.abstraction.router.abstraction


/**
 * Абстрактный класс абстрактного роутера, реализующий интерфейс {@link IRouter}. Используется для маршрутизации запросов между рабочими объектами.
 *
 * @param <T> Тип входных данных обрабатываемых рабочим объектом.
 * @param <R> Тип результата обработки рабочего объекта.
 * @param <WORKER_ID> Идентификатор рабочего объекта.
 * @param <I_WORKER> Интерфейс рабочего объекта.
 */
abstract class AbstractRouter<
        T : Any,
        R : Any,
        WORKER_ID : IWorkerId,
        I_WORKER : IWorker<T, R, WORKER_ID>
        >(
    iWorkerList: List<I_WORKER>
) : IRouter<T, R, WORKER_ID, I_WORKER> {

    /**
     * Карта рабочих объектов, доступная для маршрутизации.
     */
    override val routedMap: Map<WORKER_ID, IWorker<T, R, WORKER_ID>> = this.collectAndValidateMeta(iWorkerList)


    /**
     * Метод для маршрутизации данных к соответствующему рабочему объекту.
     *
     * @param <IT> Реализованный тип входных данных.
     * @param <IR> Реализованный тип возвращаемых результатов.
     * @param data Лямбда-функция, предоставляющая данные для обработки.
     * @param idF Лямбда-функция, возвращающая идентификатор рабочего объекта.
     * @return Результат обработки данным рабочим объектом.
     * @throws IllegalStateException Если рабочий объект с указанным идентификатором не найден.
     */
    suspend fun <IT : T, IR : R> route(
        data: suspend () -> IT,
        idF: () -> WORKER_ID
    ): R {
        val workerId = idF()
        val let = data().let { dataDto ->
            routedMap[workerId]?.doWork(dataDto, workerId) as IR?
        } ?: error("For router ${this::class.java.canonicalName} not found worker with Id $workerId")
        return let
    }


}