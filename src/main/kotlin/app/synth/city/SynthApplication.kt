package app.synth.city

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SynthApplication

fun main(args: Array<String>) {
	runApplication<SynthApplication>(*args)
}
