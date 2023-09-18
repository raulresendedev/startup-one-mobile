package br.com.fiap.startupone
import java.util.Calendar
import java.util.Date

data class EventosMarcados(
    val idEventoMarcado: Int,
    val inicio: Date,
    val fim: Date,
    val nome: String,
    val status: Boolean,
    val categoria: String
)

fun createDate(daysToAdd: Int, hours: Int, minutes: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, daysToAdd)
    calendar.set(Calendar.HOUR_OF_DAY, hours)
    calendar.set(Calendar.MINUTE, minutes)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

val eventosMock = listOf(
    EventosMarcados(1, createDate(0, 10, 0), createDate(0, 11, 0), "Comprar mantimentos", true, "Compras"),
    EventosMarcados(2, createDate(0, 13, 0), createDate(0, 14, 30), "Ligar para a mãe", false, "Pessoal"),
    EventosMarcados(3, createDate(1, 15, 5), createDate(1, 17, 10), "Finalizar projeto Kotlin", true, "Trabalho"),
    EventosMarcados(4, createDate(1, 7, 5), createDate(1, 8, 0), "Corrida no parque", false, "Exercício"),
    EventosMarcados(6, createDate(2, 18, 0), createDate(2, 19, 15), "Preparar o jantar", false, "Casa"),
    EventosMarcados(7, createDate(2, 22, 5), createDate(2, 23, 15), "Participar da reunião", true, "Trabalho"),
    EventosMarcados(9, createDate(3, 14, 15), createDate(3, 15, 5), "Visitar o dentista", false, "Saúde"),
    EventosMarcados(10, createDate(3, 16, 5), createDate(3, 17, 0), "Limpar o quarto", true, "Casa"),
    EventosMarcados(11, createDate(4, 20, 10), createDate(4, 21, 0), "Planejar férias", false, "Lazer"),
    EventosMarcados(12, createDate(4, 9, 0), createDate(4, 12, 0), "Estudar Kotlin", true, "Educação")
)
