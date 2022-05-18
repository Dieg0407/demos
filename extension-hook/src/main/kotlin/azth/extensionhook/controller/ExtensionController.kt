package azth.extensionhook.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger

@RestController
@RequestMapping(path = ["/extension"])
class ExtensionController {
    companion object {
        val log = Logger.getLogger(ExtensionController::class.java.name)
    }

    @PostMapping(path = ["/tracking"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun tracking(
        @RequestBody(required = false) body: String?,
        @RequestHeader(name = "Validation-Token") header: String?
    ): ResponseEntity<Any> {

        log.info(body?:"No body provided")
        return if (header != null && body == null)
            ResponseEntity.ok()
                .header("Validation-Token", header)
                .build()
        else
            ResponseEntity.noContent()
                .build()
    }

}