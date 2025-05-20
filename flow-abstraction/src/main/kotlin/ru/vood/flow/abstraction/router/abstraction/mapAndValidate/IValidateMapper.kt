package ru.vood.flow.abstraction.router.abstraction.mapAndValidate

import arrow.core.Either
import arrow.core.NonEmptyList
import ru.vood.flow.abstraction.router.abstraction.IWorker

/**
 * Интерфейс для реализации обработчика данных с возможностью проверки и конвертации.
 *
 * Этот интерфейс описывает поведение валидирующего маппера, который получает исходные данные типа `T`,
 * проверяет их и возвращает результат типа `R` или ошибку, представленную типом `ERR`.
 *
 * @param T Тип исходных данных.
 * @param R Тип результата после успешного преобразования.
 * @param ERR Тип ошибок, возникающих при проверке и обработке данных.
 */
interface IValidateMapper<
        T : Any,
        R : Any,
        ERR : IValidateMapperError> :
    IWorker<T, Either<NonEmptyList<ERR>, R>, ValidateMapperId<T, R, ERR>> {

    /**
     * Основной метод для выполнения работы над переданными данными.
     *
     * @param data Данные, подлежащие обработке.
     * @param wId Уникальный идентификатор рабочего элемента.
     * @return Либо успешно преобразованные данные типа `R`, либо список ошибок типа `ERR`.
     */
    override suspend fun doWork(data: T, wId: ValidateMapperId<T, R, ERR>): Either<NonEmptyList<ERR>, R> {
        return handle(data)
    }

    /**
     * Метод для фактической обработки данных.
     *
     * Должен реализовываться конечными пользователями интерфейса для выполнения конкретных операций.
     *
     * @param data Исходные данные для обработки.
     * @return Либо успешный результат типа `R`, либо ошибка типа `ERR`.
     */
    suspend fun handle(data: T): Either<NonEmptyList<ERR>, R>

}