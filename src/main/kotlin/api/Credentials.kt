package api

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

fun getSignature(key: String, secret: String): String {
    val combo = key + secret + Date().time / 1000
    val digest = MessageDigest.getInstance("SHA-256")
    val encodedHash = digest.digest(combo.toByteArray(StandardCharsets.UTF_8))
    return convertBytesToHex(encodedHash)
}

private fun convertBytesToHex(hash: ByteArray): String {
    val hexString = StringBuilder()
    for (b in hash) {
        val hex = Integer.toHexString(0xff and b.toInt())
        if (hex.length == 1) {
            hexString.append('0')
        }
        hexString.append(hex)
    }
    return hexString.toString()
}

fun getCredentials(): Array<String> {
    val key = System.getenv("key").orEmpty()
    val secret = System.getenv("secret").orEmpty()
    return arrayOf(key, secret)
}