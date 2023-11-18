import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Serializer(forClass = Timestamp::class)
object TimestampSerializer : KSerializer<Timestamp> {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    override fun serialize(encoder: Encoder, value: Timestamp) {
        encoder.encodeString(dateFormat.format(value))
    }

    override fun deserialize(decoder: Decoder): Timestamp {
        val dateString = decoder.decodeString()
        val date = dateFormat.parse(dateString)
        return Timestamp(date.time)
    }
}
