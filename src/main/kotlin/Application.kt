import feature.adminactions.AdminActionsController
import feature.adminactions.AdminActionsModule
import annotation.BotCallbackData
import annotation.BotCommand
import feature.birthdays.BirthdayController
import feature.birthdays.BirthdayModule
import bot.BotModule
import bot.Trevor
import database.DatabaseHelper
import feature.base.BaseController
import feature.birthdays.job.BirthdayWeekdayJob
import feature.birthdays.job.BirthdayWeekendJob
import processor.MethodExecutor
import processor.MessageProcessor
import org.knowm.sundial.SundialJobScheduler
import org.telegram.telegrambots.TelegramBotsApi
import feature.registration.RegistrationController
import feature.registration.RegistrationModule
import feature.salary.SalaryController
import feature.salary.SalaryModule
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

            TelegramBotsApi().registerBot(Trevor())

            SundialJobScheduler.startScheduler("feature.birthdays.job")
            SundialJobScheduler.startJob(BirthdayWeekdayJob::class.java.simpleName)
            SundialJobScheduler.startJob(BirthdayWeekendJob::class.java.simpleName)
        }

        override fun InjektRegistrar.registerInjectables() {
            importModule(BotModule)
            importModule(RegistrationModule)
            importModule(AdminActionsModule)
            importModule(SalaryModule)
            importModule(BirthdayModule)
        }

        private fun registerController(controller: BaseController) {
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