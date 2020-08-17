package handler

fun handleRequest(input: Map<String, String>): String {
    val value: String = input.getOrDefault("input", "empty")
    return "The result is: $value"
}