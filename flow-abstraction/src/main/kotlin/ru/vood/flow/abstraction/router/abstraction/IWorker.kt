package ru.vood.flow.abstraction.router.abstraction

/**
 * Интерфейс, представляющий рабочую единицу (работник), способную обрабатывать данные заданного типа и возвращать результат другого типа.
 *
 * @param T Входные данные, которые принимает работник.
 * @param R Выходные данные, которые возвращает работник.
 * @param WORKER_ID Тип идентификатора работника.
 */
interface IWorker<
        in T : Any,
        out R : Any,
        WORKER_ID
        > {
    /**
     * Множество идентификаторов, связанных с данным работником.
     * Позволяет одному работнику иметь несколько возможных идентификаторов.
     */
    val workerIds: Set<WORKER_ID>

    /**
     * Функция для выполнения основной работы, которую выполняет работник.
     *
     * @param data Данные, предоставляемые работнику для обработки.
     * @param wId Используемый идентификатор работника, соответствующий запросу.
     * @return Обработанный результат.
     */
    suspend fun doWork(data: T, wId: WORKER_ID): R
}

interface IWorkerSingleId<
        in T : Any,
        out R : Any,
        WORKER_ID
        > : IWorker<T, R, WORKER_ID> {
    val workerId: WORKER_ID

    override val workerIds: Set<WORKER_ID>
        get() = setOf(workerId)

    suspend fun doWork(data: T): R = doWork(data, workerId)
}