package ru.vood.flow.abstraction.router.abstraction.mapAndValidate

import ru.vood.flow.abstraction.router.abstraction.IWorkerId
import kotlin.reflect.KClass

/**
 * Идентификатор валидирующего маппера, используемый для уникальной идентификации конкретного маппера.
 *
 * Этот класс служит уникальным ключом для определения соответствия между валидирующим маппером и типом данных,
 * которыми он управляет. Используется для регистрации и выбора нужного маппера при выполнении операций преобразования.
 *
 * @property tkClass Классы входных данных.
 * @property rkClass Классы результирующих данных.
 * @property errkClass Классы ошибок, используемых при валидации и обработке данных.
 */
data class ValidateMapperId<out T : Any, out R : Any, out ERR : IValidateMapperError>(
    val tkClass: KClass<out T>,
    val rkClass: KClass<out R>,
    val errkClass: KClass<out ERR>
) : IWorkerId
