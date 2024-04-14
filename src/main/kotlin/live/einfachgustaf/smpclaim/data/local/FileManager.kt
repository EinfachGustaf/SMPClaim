package live.einfachgustaf.smpclaim.data.local

import java.io.File
import java.nio.file.Path

class FileManager {
    var file: File
    private var content: String

    constructor(path: Path, content: String = "") {
        this.file = path.toFile()
        this.content = content
    }

    constructor(file: File, content: String = "") {
        this.file = file
        this.content = content
    }

    fun setup() {
        if (file.exists()) return

        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeText(content)
    }
}