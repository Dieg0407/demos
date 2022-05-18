package azth.extensionhook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExtensionHookApplication

fun main(args: Array<String>) {
	runApplication<ExtensionHookApplication>(*args)
}
