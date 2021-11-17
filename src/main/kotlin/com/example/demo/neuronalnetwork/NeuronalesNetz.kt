package com.example.demo.neuronalnetwork

import kotlin.random.Random

object NeuronalesNetz {

    val neuronCount = 2;
    val layerCount = 3;

    val learningRate = 0.002

    // Weights zwischen allen layers
    val weights = Array<Array<Array<Double>>>(layerCount-1)
    {
        // Weights zu allen neuronen der nächsten layer
        Array<Array<Double>>(neuronCount)
        {
            // Weights von jedem neuron der ersten schicht zum momentanen der nächsten schicht
            Array<Double>(neuronCount)
            {
                (Random.nextDouble() * 2 * 1/Math.sqrt(neuronCount.toDouble())) - 1/Math.sqrt(neuronCount.toDouble())
            }
        }
    }

    fun run(input: Array<Double>): Array<Double>?
    {
        if(input.size != neuronCount)
            return null

        var layerInput = input
        var layerOutput = Array<Double>(neuronCount){0.0}

        for(layer in 0..layerCount-2) {
            layerOutput = Array<Double>(neuronCount){0.0}
            for (neuronOutput in layerOutput.indices)
            {
                var summedOutput = 0.0
                for (neuronInput in layerInput.indices)
                {
                    summedOutput += layerInput[neuronInput] * weights[layer][neuronOutput][neuronInput]
                }
                layerOutput[neuronOutput] = sigmoid(summedOutput)
            }
            layerInput = layerOutput
        }

        return layerOutput

    }

    fun learn(sets: Array<TrainingSet>) {
        for(set in sets.withIndex())
        {

            // layers
            val neuronValues: Array<Array<Double>> = Array<Array<Double>>(layerCount)
            {
                // single neurons
                Array<Double>(neuronCount)
                {
                    0.0
                }
            }

            var layerInput = set.value.input
            var layerOutput = Array<Double>(neuronCount) { 0.0 }
            neuronValues[0] = layerInput

            for (layer in 0..layerCount - 2)
            {
                layerOutput = Array<Double>(neuronCount) { 0.0 }
                for (neuronOutput in layerOutput.indices)
                {
                    var summedOutput = 0.0
                    for (neuronInput in layerInput.indices)
                    {
                        summedOutput += layerInput[neuronInput] * weights[layer][neuronOutput][neuronInput]
                    }
                    val finalOutput = sigmoid(summedOutput)
                    layerOutput[neuronOutput] = finalOutput
                    neuronValues[layer+1][neuronOutput] = finalOutput
                }
                layerInput = layerOutput
            }

            // Print values
            val output = Array(neuronCount)
            {
                "i$it: ${set.value.input[it]}   ->  "
            }

            for(x in neuronValues.withIndex())
            {
                for(y in x.value.withIndex())
                {
                    output[y.index] = output[y.index] + "${y.value}  -  "
                }
            }

            //for(s in output)
                //println(s)

            // --------------------------------------------------------------------------------------------------------- Backpropagation start
            //println()

            val fehler: Array<Array<Double>> = Array<Array<Double>>(layerCount)
            {
                // single neurons
                Array<Double>(neuronCount)
                {
                    0.0
                }
            }

            for(layer in layerCount-1 downTo 1)
            {
                if(layer == layerCount-1)
                {
                    for(neuron in layerOutput.withIndex())
                        fehler[layer][neuron.index] = (set.value.output[neuron.index] - layerOutput[neuron.index])
                }

                for(neuron in 0..neuronCount-1)
                {
                    val totalWeight = weights[layer-1][neuron].map { Math.abs(it) }.sum()

                    for(lastNeuron in 0..neuronCount-1)
                    {
                        val weightToCurrentNeuron = weights[layer-1][neuron][lastNeuron]
                        fehler[layer-1][lastNeuron] += (weightToCurrentNeuron / totalWeight) * fehler[layer][neuron]
                        //println("totalWeight: $totalWeight    currentWeight: $weightToCurrentNeuron     wt/wc = ${weightToCurrentNeuron/totalWeight}")
                    }
                }
            }


            // Print values
            /*
            println("\nfehler: ")
            for(layer in fehler) {
                println(layer.contentToString())
            }

             */

            // --------------------------------------------------------------------------------------------------------- Backpropagation finished ->   Gewichte anpassen

            for(wlayer in weights.withIndex())
            {
                val layer = wlayer.value

                for(wendNeuron in layer.withIndex())
                {
                    val endneuron = wendNeuron.value

                    for(wfromNeuron in endneuron.withIndex())
                    {
                        val fromNeuron = wfromNeuron.value

                        val currentNeuronValue = neuronValues[wlayer.index+1][wendNeuron.index]
                        val fromNeuronOutput = neuronValues[wlayer.index][wendNeuron.index];
                        val currentFehler = fehler[wlayer.index+1][wendNeuron.index]
                        val fehlerfunction = -(currentFehler) * sigmoid(currentNeuronValue) * (1- sigmoid(currentNeuronValue)) * fromNeuronOutput

                        //println("fehlerfunction: $fehlerfunction")

                        weights[wlayer.index][wendNeuron.index][wfromNeuron.index] = weights[wlayer.index][wendNeuron.index][wfromNeuron.index] - learningRate * fehlerfunction
                    }
                }
            }

            var arrow = "";
            for(i in 0..((set.index.toDouble()/sets.size)*100).toInt())
                arrow += "=="
            arrow += ">"
            var rest = ""
            for(i in 0..(100-(set.index.toDouble()/sets.size)*100).toInt())
                rest += ". "
            print("\r[$arrow$rest]  [${set.index}/${sets.size}]")

        }
    }

    fun sigmoid(X: Double): Double = (1 / (1 + Math.pow(Math.E, -X)))

}