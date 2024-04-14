package live.einfachgustaf.smpclaim.data.local.models

data class LocalDBModel(
    var db: HashMap<Pair<Int, Int>, ChunkAccessModel>
)
