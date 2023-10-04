package com.app.systemforbusiness.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.app.systemforbusiness.R
import com.app.systemforbusiness.alarm.AlarmAniversario
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext


fun View.hide(){
    visibility = View.GONE
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.disable(){
    isEnabled = false
}

fun View.enabled(){
    isEnabled = true
}

fun Fragment.replaceFragment(fragment: Fragment){
    val fragmentManeger = activity?.supportFragmentManager
    val fragmentTransition = fragmentManeger?.beginTransaction()
    fragmentTransition?.replace(R.id.frame_layuot,fragment)
    fragmentTransition?.commit()
}

fun String.formatarNumeroTelefone(numero: String): String{
    val phoneNumberUtil = PhoneNumberUtil.getInstance()
    val phoneNumber = phoneNumberUtil.parse(numero,"BR")
    val formatted = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
    return formatted ?: numero
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.formatarData(data: String): String{
    val format = SimpleDateFormat("yyyy-MM-dd")
    val date: Date? = format.parse(data)
    return date.toString()
}

fun scheduleBirthdayAlarm(birthdayDateMillis: Long){
    val alarmManager = Activity().getSystemService(Context.ALARM_SERVICE)!! as AlarmManager
    val intent = Intent(Activity().baseContext, AlarmAniversario::class.java).apply {
        putExtra("EXTRA_BIRTHDAY_DATE", birthdayDateMillis)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        Activity().baseContext,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Agende o alarme para a data de aniversário
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, birthdayDateMillis, pendingIntent)

}
fun spinnerFun(context: Context, spinner: AutoCompleteTextView, list: List<Any>) {
    val adapterSpinner = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.setAdapter(adapterSpinner)
}

fun convertLongToTime(time: Long): String{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    val utcTimeZoze = TimeZone.getTimeZone("UTC")
    calendar.timeZone = utcTimeZoze
    val dateFormat = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    )
    dateFormat.timeZone = utcTimeZoze
    return dateFormat.format(calendar.time)
}

fun Fragment.toast(msg: String?){
    Toast.makeText(requireContext(),msg,Toast.LENGTH_LONG).show()
}

fun Activity.toast(msg: String?){
    Toast.makeText(applicationContext,msg,Toast.LENGTH_LONG).show()
}

fun Long.toPrettyDate(): String {
    val nowTime = Calendar.getInstance()
    val neededTime = Calendar.getInstance()
    neededTime.timeInMillis = this

    return if (neededTime[Calendar.YEAR] == nowTime[Calendar.YEAR]) {
        if (neededTime[Calendar.MONTH] == nowTime[Calendar.MONTH]) {
            when {
                neededTime[Calendar.DATE] - nowTime[Calendar.DATE] == 1 -> {
                    //here return like "Tomorrow at 12:00"
                    "Amanhã às " +  SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
                }
                nowTime[Calendar.DATE] == neededTime[Calendar.DATE] -> {
                    //here return like "Today at 12:00"
                    " Hoje às " +  SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
                }
                nowTime[Calendar.DATE] - neededTime[Calendar.DATE] == 1 -> {
                    //here return like "Yesterday at 12:00"
                    "Ontem às " +  SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
                }
                else -> {
                    //here return like "May 31, 12:00"
                    SimpleDateFormat("MMMM d", Locale.getDefault()).format(Date(this))
                }
            }
        } else {
            //here return like "May 31, 12:00"
            SimpleDateFormat("MMMM d", Locale.getDefault()).format(Date(this))
        }
    } else {
        //here return like "May 31 2022, 12:00" - it's a different year we need to show it
        SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(Date(this))
    }
}
fun clipBoard(activity: Activity, string: String){
    val clipBoard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    val clip = android.content.ClipData.newPlainText("Texto Copiado!", string)

    clipBoard.setPrimaryClip(clip)
}

fun Context.createDialog(layout: Int, cancelable: Boolean): Dialog {
    val dialog = Dialog(this, android.R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(layout)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(cancelable)
    return dialog
}
fun numberCurrency(
    number: String
): String? {
    val decimalFormat = DecimalFormat("R$ ###,###,##0.00")
    return decimalFormat.format(java.lang.Double.parseDouble(number))
}

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun TextView.setColouredSpan(
    word: String,
    start: Int, end: Int,
    color: Int,
) {
    this.text = word
    val spannableString = SpannableString(text)
    try {
        spannableString.setSpan(
            ForegroundColorSpan(color),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannableString
    } catch (e: IndexOutOfBoundsException) {
        println("$word was not not found in TextView text")
    }
}
