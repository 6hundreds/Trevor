package featurecontroller

import annotation.BotCallbackData
import annotation.BotCommand
import bot.SmlSalaryBot
import org.telegram.telegrambots.api.objects.Message
import res.AdminStrings
import res.UserStrings
import service.AdminActionsService
import utils.InlineKeyboardFactory
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import kotlin.properties.Delegates

/**
 * Created by sergeyopivalov on 16.11.16.
 */
object AdminActionsController : Controller {

    private var messageWithActions :Message by Delegates.notNull()

    private val bot = Injekt.get<SmlSalaryBot>()
    private val service = Injekt.get<AdminActionsService>()


    //todo hardcode!
    @BotCommand("/actions")
    fun performActionsShow(message: Message) {
        if (!service.isAdmin(message)) {
            bot.performSendMessage(message.chatId, AdminStrings.commandNotAllowed)
            return
        }
        messageWithActions = bot.performSendMessage(message.chatId, AdminStrings.commandsList,
                InlineKeyboardFactory.createAdminKeyboard())
    }

    @BotCallbackData("#allNames")
    fun showAllNames(message: Message) {
        val list = StringBuilder()
        service.getAllUsers()
                .map { user -> "${user.smlName} \n" }
                .forEach { list.append(it) }

        bot.performSendMessage(message.chatId, list.toString())
    }

    //todo sticker factory!!!
    @BotCallbackData("#needHelp")
    fun helpRequest(message: Message) {
        with(service.getHelper().chatId) {
            bot.performSendMessage(this, AdminStrings.helpRequest)
            bot.performSendSticker(this, "BQADAQADch8AAtpxZgcZflwMawhtDQI")
        }
        bot.performSendMessage(message.chatId, AdminStrings.helpGoing)

    }

    @BotCallbackData("#salaryToday")
    fun sendSalaryNotification(message: Message) {
        service.getAllUsers()
                .forEach {
                    bot.performSendMessage(it.chatId, UserStrings.salaryNotification,
                            InlineKeyboardFactory.createUserNotificationKeyboard())
                }
        bot.performEditKeyboard(message.chatId, messageWithActions.messageId,
                InlineKeyboardFactory.createEditedAdminKeyboard())

    }
}