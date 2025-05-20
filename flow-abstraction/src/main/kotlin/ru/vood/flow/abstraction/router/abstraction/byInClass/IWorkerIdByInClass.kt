package ru.vood.flow.abstraction.router.abstraction.byInClass

import ru.vood.flow.abstraction.router.abstraction.IWorkerId
import kotlin.reflect.KClass

/**
 * Интерфейс идентификатора рабочего объекта, применяемый в инфраструктуре обработки данных с поддержкой обобщённого типа.
 * Добавляет поддержку безопасного хранения и обращения к классу данных, связанного с рабочим объектом.
 *
 * @param T Общий тип данных, используемых рабочим объектом.
 */
interface IWorkerIdByInClass<out T : Any> : IWorkerId {
    /**
     * Классификатор данных, хранит информацию о классе данных, обрабатываемом рабочим объектом.
     * Используется для безопасной привязки данных к рабочему объекту и предотвращения несоответствия типов.
     */
    val tkClass: KClass<out T>
}