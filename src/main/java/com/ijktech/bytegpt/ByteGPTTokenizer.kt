package com.ijktech.bytegpt

class ByteGPTTokenizer {
    companion object {
        private const val PAD_ID = 0L
        const val EOS_ID = 1L
        private const val UNK_ID = 2L
        private const val OFFSET = 3L
    }

    fun encode(text: String): LongArray {
        val bytes = text.encodeToByteArray()
        return bytes.map { (it.toInt() and 0xFF).toLong() + OFFSET }.toLongArray() + EOS_ID
    }

    fun decode(ids: LongArray): String {
        // Convert LongArray to List<Long>, then filter and map
        val byteList = ids.toList().mapNotNull { id ->
            when (id) {
                PAD_ID, EOS_ID, UNK_ID -> null
                else -> (id - OFFSET).toInt().toByte()
            }
        }

        // Convert List<Byte> to ByteArray
        return byteList.toByteArray().toString(Charsets.UTF_8)
    }
}
