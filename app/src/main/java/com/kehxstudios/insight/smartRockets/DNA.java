/*******************************************************************************
 * Copyright 2017 See AUTHORS file.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/

package com.kehxstudios.insight.smartRockets;

import com.kehxstudios.insight.tools.Vector2;

import java.util.Random;

/**
 *
 */

public class DNA {

    public static int geneCount = 10;
    public static float geneRange = 3.0f;
    public static float mutationRate = 0.02f;
    public static Random random = new Random();

    public Vector2[] genes;

    public DNA() {
        genes = new Vector2[geneCount];

        for (int i = 0; i < geneCount; i++) {
            if (i == 0) {
                genes[i] = randomGene();
            } else {
                genes[i] = perlinGene(genes[i-1]);
            }
        }
    }

    public DNA(Vector2[] genes) {
        this.genes = genes;
    }

    public DNA crossOver(DNA dna) {
        Vector2[] childGenes = new Vector2[geneCount];
        for (int i = 0; i < geneCount; i++) {
            if (random.nextFloat() > 0.5f) {
                childGenes[i] = genes[i];
            } else {
                childGenes[i] = dna.genes[i];
            }
        }
        DNA child = new DNA(childGenes);
        child.mutation();
        return child;
    }

    private void mutation() {
        for (int i = 0; i < geneCount; i++) {
            if (random.nextFloat() < mutationRate) {
                genes[i] = randomGene();
            }
        }
    }

    private Vector2 randomGene() {
        return new Vector2(random.nextFloat() * geneRange * 2 - geneRange,
                random.nextFloat() * geneRange * 2 - geneRange);
    }

    private Vector2 perlinGene(Vector2 previousGene) {
        return new Vector2(previousGene.x - geneRange / 2 + random.nextFloat() * geneRange,
                previousGene.y - geneRange / 2 + random.nextFloat() * geneRange);
    }

}
