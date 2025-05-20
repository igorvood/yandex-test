package ru.vood.flow.abstraction.web

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

interface IWebClient<T, R> {
    /**
     * Маппер, выполняющий преобразование строки-ответа от сервера в объект необходимого типа.
     */
    val iWebResponseMapper: IWebResponseMapper<R>

    /**
     * Признак включения логирования сетевых взаимодействий клиента.
     */
    val enableLogging: Boolean


    /**
     * Подготавливает спецификацию запроса для отправки серверу.
     *
     * Этот метод предназначен для переопределения в наследниках класса, чтобы настроить конкретные детали каждого запроса.
     *
     * @param rq Запрос, передаваемый в клиент, согласно типу параметра [T].
     * @return Спецификация запроса, готовая к отправке на удалённый сервер.
     */
    fun prepareRequest(rq: T): WebClient.RequestHeadersSpec<*>

    /**
     * Отправляет подготовленный запрос и возвращает обработанный результат в виде ожидаемого типа [R].
     *
     * Выполнение асинхронное благодаря использованию механизма ко-рутин, доступному через библиотеку Coroutine-Reactor.
     *
     * @param rq Запрос, который должен быть отправлен серверу.
     * @return Обработанный результат запроса в виде объекта типа [R], возвращённого маппером.
     */
    suspend fun runRequest(rq: T): R {
        return prepareRequest(rq)
            .exchangeToMono { clRe -> clRe.toEntity(String::class.java) }
            .alsoIfEnabledLog()
            .map(iWebResponseMapper::map)
            .awaitSingle()
    }

    /**
     * Применяет логирование к моно-публикатору, если флаг [enableLogging] установлен в значение `true`.
     *
     * Если логирование отключено, метод вернёт исходный публикацию без изменений.
     *
     * @return Публикация, дополненная возможностью логирования, либо исходная публикация, если логирование отключено.
     */
    private fun <R> Mono<R>.alsoIfEnabledLog(): Mono<R> =
        if (enableLogging) log() else this
}