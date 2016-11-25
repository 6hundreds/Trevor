import annotation.BotCallbackData
import annotation.BotCommand
import bot.Trevor
import database.DatabaseHelper
import di.*
import featurecontroller.*
import messageprocessor.MethodExecutor
import messageprocessor.MessageProcessor
import org.knowm.sundial.SundialJobScheduler
import org.telegram.telegrambots.TelegramBotsApi
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar

/**
 * Created by sergeyopivalov on 16.11.16.
 */
class Application {
    companion object : InjektMain() {
        @JvmStatic fun main(args: Array<String>) {

            registerController(RegistrationController)
            registerController(AdminActionsController)
            registerController(SalaryController)
            registerController(BirthdayController)

            DatabaseHelper.createDb()

            SundialJobScheduler.startScheduler("job")
            SundialJobScheduler.startJob("BirthdayWeekdayJob")
            SundialJobScheduler.startJob("BirthdayWeekendJob")

            TelegramBotsApi().registerBot(Trevor())
        }

        override fun InjektRegistrar.registerInjectables() {
            importModule(BotModule)
            importModule(RegistrationModule)
            importModule(AdminActionsModule)
            importModule(SalaryModule)
            importModule(BirthdayModule)
        }

        fun registerController(controller: Controller) {
            controller.javaClass.declaredMethods
                    .forEach {
                        if (it.isAnnotationPresent(BotCommand::class.java)) {
                            val command = it.getAnnotation(BotCommand::class.java).command
                            val executor = MethodExecutor(it, controller)
                            MessageProcessor.addCommand(command, executor)
                        }
                        if (it.isAnnotationPresent(BotCallbackData::class.java)) {
                            val callbackData = it.getAnnotation(BotCallbackData::class.java).callbackData
                            val executor = MethodExecutor(it, controller)
                            MessageProcessor.addCallbackData(callbackData, executor)
                        }
                    }
        }
    }
}