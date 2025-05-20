package ru.vood.flow.abstraction.router.abstraction

/**
 * Интерфейс маршрутизатора, используемый для определения общей структуры маршрутизации запросов между рабочими объектами.
 *
 * @param T Тип входных данных, передаваемых рабочим объектам.
 * @param R Тип результата, возвращаемого рабочими объектами.
 * @param WORKER_ID Тип идентификатора рабочего объекта.
 * @param I_WORKER Тип рабочего объекта, реализующего интерфейс [IWorker].
 */
interface IRouter<
        T : Any,
        R : Any,
        WORKER_ID,
        I_WORKER : IWorker<T, R, WORKER_ID>
        > {

    /**
     * Карта рабочих объектов, индексированная по их идентификаторам.
     */
    val routedMap: Map<WORKER_ID, IWorker<T, R, WORKER_ID>>


    /**
     * Метод для сборки и проверки метаданных рабочих объектов перед добавлением их в карту маршрутизации.
     *
     * @param handlers Список рабочих объектов для проверки и добавления в карту.
     * @return Карта рабочих объектов, прошедших проверку.
     * @throws IllegalArgumentException Если обнаружены рабочие объекты с дублирующимися идентификаторами.
     */
    fun collectAndValidateMeta(handlers: List<IWorker<T, R, WORKER_ID>>): Map<WORKER_ID, IWorker<T, R, WORKER_ID>> {
        // Флаттеринг списка рабочих объектов для формирования пар 'идентификатор'->'рабочий'
        val prepared = handlers
            .flatMap { iWorker -> iWorker.workerIds.map { wId -> wId to iWorker } }

        // Группировка по идентификаторам для выявления дубликатов
        val groupByForAssertionError = prepared.groupBy { it.first }
            .filter { it.value.size > 1 }
            .map { it.key to it.value.map { q -> q.second.javaClass.canonicalName to q.first } }
            .toMap()

        // Проверка наличия дублирующих идентификаторов
        require(groupByForAssertionError.isEmpty()) {
            "For router ${this::class.java.canonicalName} found several workers with same Id $groupByForAssertionError"
        }

        // Преобразование подготовленного списка в итоговую карту
        return prepared.toMap()
    }

}