import kotlin.random.Random
import java.io.File

fun Crearnumero(cifras: Int, rango: List<Int>): String {
    return rango.shuffled().take(cifras).joinToString("")
}

fun Compara(adivino: String, NumeroSecreto: String): Pair<Int, Int> {
    var aciertos = 0
    var coincidencias = 0
    val Acertados = adivino.toCharArray()
    val Secretos = NumeroSecreto.toCharArray()

    for (i in adivino.indices) {
        if (Acertados[i] == Secretos[i]) {
            aciertos++
            Secretos[i] = 'X'
            Acertados[i] = 'Y'
        }
    }
    for (i in adivino.indices) {
        if (Acertados[i] != 'Y' && Acertados[i] in Secretos) {
            coincidencias++
            val coincidencia = Secretos.indexOf(Acertados[i])
            if (coincidencia != -1) {
                Secretos[coincidencia] = 'X'
            }
        }
    }
    return Pair(aciertos, coincidencias)
}

fun guardarHistorial(historial: List<String>) {
    File("ultimo_historial.txt").writeText(historial.joinToString("\n"))
}

fun verUltimoHistorial(): String {
    val archivo = File("ultimo_historial.txt")
    return if (archivo.exists()) archivo.readText() else "No hay registros de intentos anteriores."
}

fun main() {
    val nIntentos = 12
    val cifras = 4
    val rango = (1..7).toList()
    var NumeroSecreto: String
    var opcion: String

    do {
        NumeroSecreto = Crearnumero(cifras, rango)
        var Intentos = 0
        val historialIntentos = mutableListOf<String>()
        historialIntentos.add("Número Secreto: \u001B[1m$NumeroSecreto\u001B[0m")

        println("----------------------------------------------------------------------")
        println("\u001B[34m\u001B[1m            ¡Adivina el numero!\u001B[0m")
        println("\u001B[32mSe va a generar un numero de $cifras cifras y has de acertar en $nIntentos Intentos.\u001B[0m")
        println("\u001B[33mLos digitos están comprendidos entre el 1 y el 7 sin repetirse.\u001B[0m")
        println("\u001B[33mEmpecemos, introduce tu numero: \u001B[0m")
        println("----------------------------------------------------------------------")
        println("\u001B[34mNumero \u001B[32mAciertos\u001B[0m \u001B[33mCoincidencias \u001B[35mIntentos\u001B[0m")
        println("\u001B[31m----------------------------------------------------------------------\u001B[0m")

        while (Intentos < nIntentos) {
            val adivino = readLine()

            if (adivino != null && adivino.length == cifras && adivino.all { it in '1'..'7' } && adivino.all { it.isDigit() }) {
                val (aciertos, coincidencias) = Compara(adivino.toString(), NumeroSecreto)
                historialIntentos.add("Intento \u001B[35m${Intentos+1}\u001B[0m: \u001B[34m$adivino\u001B[0m - Aciertos: \u001B[32m$aciertos\u001B[0m, Coincidencias: \u001B[33m$coincidencias\u001B[0m")

                if (aciertos == cifras) {
                    println("\u001B[31m\u001B[1m  ¡Has acertado! El numero era:   \u001B[35m\u001B[4m$NumeroSecreto\u001B[0m\n")
                    break
                } else {
                    Intentos++
                    println("\u001B[34m $adivino\t\u001B[32m  $aciertos\u001B[0m\t\u001B[33m        $coincidencias\t\u001B[35m         $Intentos\u001B[0m")
                }
            } else {
                println("Entrada no válida. Asegúrate de introducir $cifras cifras válidas del 1 al 7.")
            }
        }

        if (Intentos >= nIntentos) {
            println("\u001B[33mHas agotado el número de intentos.\nEl numero correcto era:\u001B[0m  \u001B[36m$NumeroSecreto\u001B[0m\n")
        }

        guardarHistorial(historialIntentos)

        // Bucle de opciones
        do {
            println("\u001B[32m¿Qué deseas hacer ahora? Escribe:\n \u001B[33m'Y' para jugar de nuevo\n 'V' para ver el último historial guardado\n 'N' para salir\u001B[0m")
            opcion = readLine() ?: "N"

            when (opcion) {
                "Y" -> break // Salir del bucle de opciones y reiniciar el juego
                "V" -> println("Último historial guardado:\n\u001B[31m------------------------------------------------\u001B[0m\n${verUltimoHistorial()}\n\u001B[31m------------------------------------------------\u001B[0m")
                "N" -> println("Juego finalizado.")
                else -> println("Opción no válida. Intenta nuevamente.")
            }
        } while (opcion != "Y" && opcion != "N")

    } while (opcion == "Y")
}
