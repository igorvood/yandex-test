package ru.vood.flow.abstraction.router.abstraction

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking

class AbstractRouterTest : FunSpec({

   test( "should successfully route requests to correct worker") {

            val worker1 = TestWorker(TestWorkerId.WORKER_1)
            val worker2 = TestWorker(TestWorkerId.WORKER_2)
            val router = ConcreteRouter(listOf(worker1, worker2))

            val result1 = router.route<String, String>({ "Hello" }, { TestWorkerId.WORKER_1 })
            val result2 = router.route<String, String>({ "World" }, { TestWorkerId.WORKER_2 })

            result1 shouldBe "Hello processed by WORKER_1"
            result2 shouldBe "World processed by WORKER_2"

    }

    test("should throw exception when no matching worker is found") {
        runBlocking {
            val worker1 = TestWorker(TestWorkerId.WORKER_1)
            val router = ConcreteRouter(listOf(worker1))

            try {
                router.route<String, String>(
                    { "Test Data" },
                    { TestWorkerId.WORKER_2 }) // Рабочий объект с таким идентификатором отсутствует
            } catch (ex: IllegalStateException) {
                ex.message shouldBe "For router ru.vood.flow.abstraction.router.abstraction.ConcreteRouter not found worker with Id WORKER_2"
            }
        }
    }
})

// Тестовая модель WorkerId
enum class TestWorkerId : IWorkerId {
    WORKER_1, WORKER_2
}

// Тестовый рабочий объект
class TestWorker(private val id: TestWorkerId) : IWorker<String, String, TestWorkerId> {
    override val workerIds: Set<TestWorkerId> = setOf(id)

    override suspend fun doWork(data: String, wId: TestWorkerId): String {
        return "$data processed by $wId"
    }
}

// Создаем конкретный класс роутера, расширяя абстрактный класс
class ConcreteRouter(workers: List<TestWorker>) :
    AbstractRouter<String, String, TestWorkerId, TestWorker>(workers)