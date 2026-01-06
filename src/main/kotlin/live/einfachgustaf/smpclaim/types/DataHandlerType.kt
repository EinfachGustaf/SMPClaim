package live.einfachgustaf.smpclaim.types

import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.data.local.LocalDataHandler

enum class DataHandlerType(val dataHandler: IDataHandler) {
    LOCAL_JSON(LocalDataHandler())
}