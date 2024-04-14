package live.einfachgustaf.smpclaim.types

import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.data.PostgresDataHandler
import live.einfachgustaf.smpclaim.data.local.LocalDataHandler

enum class DataHandlerType(val dataHandler: IDataHandler) {
    POSTGRES(PostgresDataHandler()),
    LOCAL_JSON(LocalDataHandler())
}