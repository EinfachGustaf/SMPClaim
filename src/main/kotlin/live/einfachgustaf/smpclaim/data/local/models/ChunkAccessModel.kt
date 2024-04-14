package live.einfachgustaf.smpclaim.data.local.models

import kotlinx.serialization.Serializable
import live.einfachgustaf.smpclaim.utils.UUIDSerializer
import java.util.*
import kotlin.collections.ArrayList

@Serializable
data class ChunkAccessModel(
    @Serializable(with = UUIDSerializer::class) val owner: UUID,
    val access: ArrayList<@Serializable(with = UUIDSerializer::class) UUID>
)
