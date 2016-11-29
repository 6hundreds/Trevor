package res

/**
 * Created by sergeyopivalov on 17.11.16.
 */
object ButtonLabel {
    val allUsers = "Показать всех пользователей"
    val needHelp = "Помощь друга"
    val salaryToday = "Сегодня зарплата =)"
    val salaryStart = "Начать выдачу зарплаты"
    val salaryList = "Список получающих сегодня"
    val yes = "Да"
    val no = "Нет"
    val going = "Иду"
    val notGoing = "Не иду"
    val gotPaid = "Получил"
    val notGotPaid = "Не получил"
    val deleteUser = "Удалить пользователя"
    val bigRoom = "Большая стеклянная"
    val smallRoom = "Маленькая уютная"
}

object MiscStrings {
    val ok = "Ok"
}

object AdminStrings {
    val commandsList = "Вот возможные действия:"
    val commandNotAllowed = "Вот тебе, именно тебе, такие команды использовать запрещено"
    val helpRequest = "Варя, Тревор на связи. Лене нужна твоя помощь"
    val helpGoing = "Тревор вызвал подмогу, сейчас все будет..."
    val typeChatIdToDelete = "Введи ID пользователя для удаления"
    val userHasBeenDeleted = "Удалил изменника"
}

object UserStrings {
    val alreadyRegistered = "Ну ты ведь зарегестрирован уже..."
    val thereIsNoUsername = "Привет! Ты хочешь получать зарплату, но у тебя не указан username в настройках Telegram. Не надо так. Добавь username и попробуй /reg снова"
    val askPass = "Привет! Ну ладно, ты хочешь получать зарплату. Какой пароль?"
    val registrationComplete = "Регистрация завершена"
    val wrongPass = "Почти правильный пароль"
    val rightPass = "Верный пароль"
    val typeYourName = "Введи свое имя и фамилию. Через пробел. С большой буквы. И без шуточек"
    val typeYourBirthday = "Тревор хочет знать твою дату рождения. Формат : dd.mm.yyyy"
    val incorrectInput = "Ты прислал какую то шляпу..."
    val incorrectBirthday = "Ты втер мне дичь. Введи дату рождения в правильном формате"
    val salaryNotification = "Привет! Сегодня зарплату обещают\nБудешь получать ?"
}

object BirthdayStrings {
    val notification = "Cегодня празднует свой день рождения "
    val notificationWeekend = "На выходных был день рождения у " //todo падеж!!!
}

object SalaryDayStrings {
    val dummy = "..."
    val hasBeenAdded = "Добавил тебя в список получающих"
    val inOtherTime = "Хорошо, в другой раз"
    val noOne = "Пока никто не собирается"
    val quantity = "\nВсего - "
    val yourTurn = "Твоя очередь. У тебя 2 минуты на подтверждение"
    val isGoing = "идет..."
    val turnSkipped = "Ты пропустил свою очередь. Ожидай следующего вызова"
    val complete = "Зарплата выдана всем"
    val moneyReceived = "Деньги получены. Досвидули"

}

object ReservationStrings {
    val chooseRoom = "Выбери переговорную, которую хочешь зарезервировать"
    val reserved = "Зарезервировано"
    val incorrectDate = "Некорректная дата"
    val incorrectDuration = "Не нужно тебе столько..."
    val typeDuration = "Введи адекватную продолжительность резерва"
    val typeDate = "Когда? Формат dd.mm.yyyy hh:MM"
}

