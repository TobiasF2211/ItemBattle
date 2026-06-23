package net.itemBattle.utils

fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = seconds / 60 % 60
    val remainingSeconds = seconds % 60

    return String.format("%02dh %02dm %02ds", hours, minutes, remainingSeconds)
}