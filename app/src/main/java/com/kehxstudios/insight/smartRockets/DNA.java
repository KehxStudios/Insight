package com.kehxstudios.insight.smartRockets;

import com.kehxstudios.insight.tools.Vector2;

import java.util.Random;

/**
 * Created by ReidC on 2017-06-13.
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
        Vector2[] child = new Vector2[geneCount];
        for (int i = 0; i < geneCount; i++) {
            if (random.nextFloat() > 0.5f) {
                child[i] = genes[i];
            } else {
                child[i] = dna.genes[i];
            }
        }
        return new DNA(child);
    }

    public void mutation() {
        for (int i = 0; i < geneCount; i++) {
            if (random.nextFloat() < mutationRate) {
                genes[i] = randomGene();
            }
        }
    }

    public Vector2 randomGene() {
        return new Vector2(random.nextFloat() * geneRange * 2 - geneRange,
                random.nextFloat() * geneRange * 2 - geneRange);
    }

    public Vector2 perlinGene(Vector2 previousGene) {
        return new Vector2(previousGene.x - geneRange / 2 + random.nextFloat() * geneRange,
                previousGene.y - geneRange / 2 + random.nextFloat() * geneRange);
    }

}
