package ru.vood.flow.abstraction.router.abstraction.mapAndValidate

import arrow.core.Either
import arrow.core.NonEmptyList
import ru.vood.flow.abstraction.router.abstraction.AbstractRouter
import ru.vood.flow.abstraction.router.abstraction.IWorker

/**
 * Абстрактный класс-маршрутизатор для отображения данных между объектами типа `T` и результатом типа `R`.
 *
 * @param T Тип входных данных.
 * @param R Тип результата преобразования.
 * @param ERR Тип ошибок валидатора.
 */
abstract class AbstractEitherMapperRouter<
        /** Входные данные любого типа*/
        T : Any,
        /** Результат преобразований*/
        R : Any,
        /** Ошибки, возникающие при обработке данных*/
        ERR : IValidateMapperError
        >(
    iWorkerList: List<IWorker<T, Either<NonEmptyList<ERR>, R>, ValidateMapperId<T, R, ERR>>>
) : AbstractRouter<
        T,
        Either<NonEmptyList<ERR>, R>,
        ValidateMapperId<T, R, ERR>,
        IWorker<T,
                Either<NonEmptyList<ERR>, R>,
                ValidateMapperId<T, R, ERR>
                >
        >(iWorkerList = iWorkerList) {


    /**
     * Метод, осуществляющий отображение данных с обработкой возможных ошибок.
     *
     * @param IT Реализованный тип входных данных.
     * @param IR Реализованный тип выходного результата.
     * @param IERR Реализованный тип ошибки.
     * @param data Функция-заглушка для получения исходных данных.
     * @return Объект либо с ошибками, либо с успешным результатом преобразования.
     */
    suspend inline fun <reified IT : T, reified IR : R, reified IERR : ERR> mapData(
        noinline data: suspend () -> IT
    ): Either<NonEmptyList<ERR>, R> {
        // Получаем идентификатор для текущего маршрута отображения
        val validateMapperId = ValidateMapperId(IT::class, IR::class, IERR::class)
        // Запускаем обработку данных с использованием найденного работника
        return data().let { dataDto ->
            routedMap[validateMapperId]?.doWork(dataDto, validateMapperId)
        } ?: error("For router ${this::class.java.canonicalName} not found worker with Id $validateMapperId")
    }


}