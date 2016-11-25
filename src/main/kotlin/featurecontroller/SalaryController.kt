package featurecontroller

import annotation.BotCallbackData
import entity.User
import task.SalaryTask
import org.telegram.telegrambots.api.objects.Message
import res.CallbackData
import res.MiscStrings
import res.SalaryDayStrings
import service.SalaryService
import utils.InlineKeyboardFactory
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by sergeyopivalov on 20.11.16.
 */
object SalaryController : BaseController() {

    private val service = Injekt.get<SalaryService>()
    private var timer = Injekt.get<Timer>()

    private var adminMessage: Message by Delegates.notNull()
    private var currentMessage: Message by Delegates.notNull()
    private var salaryListMessage: Message? = null

    private var currentUser: User? = null
    private var timerTask: TimerTask? = null

    @BotCallbackData(CallbackData.addToSalaryList)
    fun addUserToSalaryList(message: Message) {
        service.addUserToSalaryList(message)
        bot.performEditMessage(message.chatId, message.messageId, SalaryDayStrings.hasBeenAdded, true)
    }

    @BotCallbackData(CallbackData.notAddToSalaryList)
    fun notAddUserToSalaryList(message: Message) {
        bot.performEditMessage(message.chatId, message.messageId, SalaryDayStrings.inOtherTime, true)
    }

    @BotCallbackData(CallbackData.salaryList)
    fun showSalaryList(message: Message) {
        val list = with(service.getAllUsersForSalary()) {
            if (isEmpty()) {
                bot.performSendMessage(message.chatId, SalaryDayStrings.noOne)
                return
            }
            val list = StringBuilder()
            map { user -> "${user.smlName} \n" }
            forEach { list.append(it) }

            list.append("${SalaryDayStrings.quantity} ${this.size}")
            list.toString()
        }

        if (salaryListMessage == null)
            salaryListMessage = bot.performSendMessage(message.chatId, list)
        else
            bot.performEditMessage(message.chatId, salaryListMessage!!.messageId, list)
    }

    @BotCallbackData(CallbackData.goingToGetPaid)
    fun userGoingToGetPaid(message: Message) {
        bot.performEditMessage(message.chatId, message.messageId, MiscStrings.ok, true)
        timerTask?.cancel()
    }

    @BotCallbackData(CallbackData.notGoingToGetPaid)
    fun skipTurn(message: Message) {
        bot.performSendMessage(message.chatId, SalaryDayStrings.turnSkipped)
        notifyNextUser()
    }

    @BotCallbackData(CallbackData.salaryStart)
    fun startSalary(message: Message) {
        adminMessage = bot.performSendMessage(message.chatId, SalaryDayStrings.dummy)
        notifyNextUser()
    }

    @BotCallbackData(CallbackData.gotPaid)
    fun userGetSalary(message: Message) {
        bot.performEditMessage(currentMessage.chatId, currentMessage.messageId, SalaryDayStrings.moneyReceived, true)
        service.deleteUserFromSalaryList(currentUser!!)
        notifyNextUser()
    }

    @BotCallbackData(CallbackData.notGotPaid)
    fun userNotGetSalary(message: Message) {
        notifyUserSkipTurn(currentMessage)
        notifyNextUser()
    }

    fun notifyUserSkipTurn(message: Message) {
        bot.performEditMessage(message.chatId, message.messageId, SalaryDayStrings.turnSkipped)
    }

    fun notifyNextUser() {
        timerTask?.cancel()

        if (service.isListEmpty()) {
            bot.performEditMessage(adminMessage.chatId, adminMessage.messageId, SalaryDayStrings.complete)
            salaryListMessage = null
            return
        }

        currentUser = service.getNextUser(currentUser)
        notifyAdmin()
        currentMessage = inviteUser()

        timerTask = SalaryTask(currentMessage)
        timer.schedule(timerTask, 5000) //todo delay to properties
    }

    private fun notifyAdmin() {
        with(bot) {
            performEditMessage(adminMessage.chatId, adminMessage.messageId,
                    "${currentUser?.smlName} ${SalaryDayStrings.isGoing}")
            performEditKeyboard(adminMessage.chatId, adminMessage.messageId,
                    InlineKeyboardFactory.createUserPaidStatusKeyboard())
        }
    }

    private fun inviteUser(): Message =
            bot.performSendMessage(currentMessage.chatId, SalaryDayStrings.yourTurn,
                    InlineKeyboardFactory.createUserInvitationKeyboard())


}