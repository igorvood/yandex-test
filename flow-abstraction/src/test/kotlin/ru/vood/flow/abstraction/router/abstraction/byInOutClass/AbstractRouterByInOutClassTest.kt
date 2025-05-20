package ru.vood.flow.abstraction.router.abstraction.byInOutClass

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass

class AbstractRouterByInOutClassTest : FunSpec({
    test("OK") {

        val testAbstractRouterByInOutClass = TestAbstractRouterByInOutClass(listOf(T1(), T2()))

        testAbstractRouterByInOutClass.mapData<In_1, Out_1> { In_1 } shouldBe Out_1
        testAbstractRouterByInOutClass.mapData<In_2, Out_2> { In_2 } shouldBe Out_2
        testAbstractRouterByInOutClass.mapData<In_3, Out_3> { In_3 } shouldBe Out_3

    }
})


sealed interface IInTestDto
sealed interface IOutTestDto

sealed interface InTestDto2_3 : IInTestDto
sealed interface OutTestDto2_3 : IOutTestDto

data object In_1 : IInTestDto
data object In_2 : InTestDto2_3
data object In_3 : InTestDto2_3

data object Out_1 : IOutTestDto
data object Out_2 : OutTestDto2_3
data object Out_3 : OutTestDto2_3


data class InOutIdDto<T : IInTestDto, R : IOutTestDto>(
    override val tkClass: KClass<out T>,
    override val rkClass: KClass<out R>
) : IWorkerIdByInOutClass<T, R>

interface ITestWorker<T : IInTestDto, R : IOutTestDto> : IWorkerByInOutClass<T, R, InOutIdDto<T, R>>


open class T1 : ITestWorker<In_1, Out_1> {
    override val workerIds: Set<InOutIdDto<In_1, Out_1>>
        get() = setOf(InOutIdDto(In_1::class, Out_1::class))

    override suspend fun doWork(data: In_1, wId: InOutIdDto<In_1, Out_1>): Out_1 = Out_1
}

class TDunlicate : T1()

class T2 : ITestWorker<InTestDto2_3, OutTestDto2_3> {
    override val workerIds: Set<InOutIdDto<InTestDto2_3, OutTestDto2_3>>
        get() = setOf(InOutIdDto(In_2::class, Out_2::class), InOutIdDto(In_3::class, Out_3::class))

    override suspend fun doWork(data: InTestDto2_3, wId: InOutIdDto<InTestDto2_3, OutTestDto2_3>): OutTestDto2_3 =
        when (data) {
            In_2 -> Out_2
            In_3 -> Out_3
        }
}

class TestAbstractRouterByInOutClass(iWorkerList: List<ITestWorker<out IInTestDto, out IOutTestDto>>) :
    AbstractRouterByInOutClass<IInTestDto, IOutTestDto, InOutIdDto<IInTestDto, IOutTestDto>, ITestWorker<IInTestDto, IOutTestDto>>(
        iWorkerList.filterIsInstance<ITestWorker<IInTestDto, IOutTestDto>>()
    ) {

    suspend inline fun <reified IT : IInTestDto, reified IR : IOutTestDto> mapData(
        noinline data: suspend () -> IT
    ): IR = route<IT, IR>(data) { InOutIdDto(IT::class, IR::class) } as IR
}