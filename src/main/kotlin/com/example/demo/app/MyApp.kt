package com.example.demo.app

import com.example.demo.neuronalnetwork.NeuronalesNetz
import com.example.demo.neuronalnetwork.TrainingSet
import com.example.demo.view.MainView
import tornadofx.App
import kotlin.random.Random
import kotlin.random.nextInt

class MyApp: App(MainView::class, Styles::class) {

    init {
        for(weightsbetweenlayers in NeuronalesNetz.weights.withIndex()) {
            //println("Layer: ${weightsbetweenlayers.index}")
            for(weightsToNeuronY in weightsbetweenlayers.value.withIndex()) {
                //println("weights from X to ${weightsToNeuronY.index}")
                for(weightXToY in weightsToNeuronY.value.withIndex()) {
                    //println("W(${weightXToY.index},${weightsToNeuronY.index}) = ${weightXToY.value}")
                }
                //println()
            }
            //println()
        }

        println("\n----- Random Weight Distribution -----")
        println("obere grenze: ${1 * 2 * 1/Math.sqrt(NeuronalesNetz.neuronCount.toDouble()) - 1/Math.sqrt(NeuronalesNetz.neuronCount.toDouble())}")
        println("untere grenze: ${0 * 2 * 1/Math.sqrt(NeuronalesNetz.neuronCount.toDouble()) - 1/Math.sqrt(NeuronalesNetz.neuronCount.toDouble())} \n")

        println(NeuronalesNetz.learn(
            Array<TrainingSet>(1000000)
            {
                val a = (Random.nextDouble() / 2) + 0.5
                val b = Random.nextDouble() / 2
                println(a-b)
                TrainingSet(
                    arrayOf(a, b),
                    arrayOf(a-b, 0.5)
                )
            }
        ))

        for(i in 0..99) {
            println(
                NeuronalesNetz.run(
                    arrayOf(
                        Random(i).nextDouble(),
                        Random(i).nextDouble()
                    )
                ).contentToString()
            )
        }

    }

}