package live.einfachgustaf.smpclaim.data.local.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class LocalDBModel(
    var db: HashMap<@Serializable(with = SerializableIntPairSerializer::class) SerializableIntPair, ChunkAccessModel>
)

data class SerializableIntPair(val key: Int, val value: Int)

class SerializableIntPairSerializer: KSerializer<SerializableIntPair> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Pair", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): SerializableIntPair {
        val split = decoder.decodeString().split(";")

        return SerializableIntPair(split[0].toInt(), split[1].toInt())
    }

    override fun serialize(encoder: Encoder, value: SerializableIntPair) {
        encoder.encodeString("${value.key};${value.value}")
    }
}