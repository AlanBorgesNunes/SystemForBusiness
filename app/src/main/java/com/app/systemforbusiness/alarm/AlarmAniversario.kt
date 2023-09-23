package com.app.systemforbusiness.alarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.systemforbusiness.MainActivity
import com.app.systemforbusiness.R
import java.text.SimpleDateFormat
import java.util.*

class AlarmAniversario: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Extrair a data de aniversário do intent extra
        val birthdayDateMillis = intent?.getLongExtra(EXTRA_BIRTHDAY_DATE, 0)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val birthdayDateStr = dateFormat.format(Date(birthdayDateMillis!!))

        // Lógica para lidar com o lembrete quando ele for acionado
        Toast.makeText(context, "Feliz Aniversário em $birthdayDateStr!", Toast.LENGTH_LONG).show()


        val i = Intent(context,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,i,0)

        val buider = NotificationCompat.Builder(context!!,"channelID")
            .setSmallIcon(R.drawable.icon_aniversario)
            .setContentTitle("Rla Doida")
            .setContentText("Cabecuda")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123,buider.build())
    }

    companion object {
        const val EXTRA_BIRTHDAY_DATE = "extra_birthday_date"
    }

}

