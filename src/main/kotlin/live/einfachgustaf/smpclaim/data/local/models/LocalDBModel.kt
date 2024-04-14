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
    var db: HashMap<@Serializable(with = SerializableLocationSerializer::class) SerialzableLocation, ChunkAccessModel>
)

data class SerialzableLocation(val world: String, val x: Int, val z: Int)

class SerializableLocationSerializer: KSerializer<SerialzableLocation> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Location", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): SerialzableLocation {
        val split = decoder.decodeString().split(";")

        return SerialzableLocation(split[0], split[1].toInt(), split[2].toInt())
    }

    override fun serialize(encoder: Encoder, value: SerialzableLocation) {
        encoder.encodeString("${value.world};${value.x};${value.z}")
    }
}