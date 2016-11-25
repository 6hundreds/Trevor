package job

import featurecontroller.BirthdayController
import org.knowm.sundial.Job
import org.knowm.sundial.annotations.CronTrigger

/**
 * Created by sergeyopivalov on 24/11/2016.
 */
@CronTrigger(cron = "0 0 10 ? * MON-FRI")
class BirthdayWeekdayJob : Job() {

    override fun doRun() {
        BirthdayController.checkBirthdays()
        BirthdayController.notifyUsersAboutBirthdays()
    }
}