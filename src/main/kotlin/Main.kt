package ru.netology

val limitsPerMonth = 600_000
val limitsPerDay = 150_000

fun main() {
    //За переводы с карты Mastercard комиссия не взимается,
    //          пока не превышен месячный лимит в 75 000 руб. Если лимит превышен,
    //          комиссия составит 0,6% + 20 руб.
    //За переводы с карты Visa комиссия составит 0,75%, минимальная сумма комиссии 35 руб.
    //За переводы с карты Мир комиссия не взимается.
    //Кроме того, введём лимиты на суммы перевода за сутки и за месяц. Максимальная сумма перевода с одной карты:
    //
    //150 000 руб. в сутки
    //600 000 руб. в месяц

    //передавая в функцию:
    //
    //тип карты (по умолчанию Мир);
    //сумму предыдущих переводов в этом месяце (по умолчанию 0 рублей);
    //сумму совершаемого перевода.
    //В случае превышения какого-либо из лимитов операция должна блокироваться.

    commissionCalc ("MC", 0, 0, 150_000)
    commissionCalc ("MC", 0, 0, 700_000)
    commissionCalc (newTransfer = 150_000)
    commissionCalc ("VISA", newTransfer = 100_000)

}

fun commissionCalc(
    cardType: String = "MIR",
    sumMonthTransfers: Int = 0,
    sumDayTransfers: Int = 0,
    newTransfer: Int
): Int {

    when {
        newTransfer + sumDayTransfers > limitsPerDay -> {
            println("Превышен разрешенный лимит в день, операция блокирована")
            return -1
        }

        newTransfer + sumMonthTransfers > limitsPerMonth -> {
            println("Превышен разрешенный лимит в месяц, операция блокирована")
            return -2
        }
    }

    var comission: Int = 0

    when (cardType) {
        "MC" -> comission = when {
            sumMonthTransfers + newTransfer <= 75_000 -> 0
            sumMonthTransfers <= 75_000 -> ((sumMonthTransfers + newTransfer - 75_000)* 0.006 + 20).toInt()
            else -> (newTransfer * 0.006 + 20).toInt()
        }
        "VISA" -> comission = if (newTransfer * 0.0075 < 35) 35 else (newTransfer * 0.0075).toInt()
        "MIR" -> comission = 0
    }

    println(
        "Размер комиссии при сумме перевода $newTransfer рублей по карте $cardType: $comission рублей"
    )
    return comission
}