package week3;

import java.util.stream.Stream;

public class ColourSwap {

    enum Colour { BLUE, RED, WHITE }

    private int numCallsToColour = 0;
    private int numCallsToSwap = 0;

    private final Colour[] colours;

    ColourSwap(Colour[] colours) {
        this.colours = colours;
    }

    private Colour colour(int index) { numCallsToColour++; return colours[index]; }
    private void swap(int index1, int index2) {
        numCallsToSwap++;
        Colour aux = colours[index2];
        colours[index2] = colours[index1];
        colours[index1] = aux;
    }

    Colour[] groupColors() {

        for(int i = 0; i < colours.length; i++) {

            Colour currentColour = colours[i];
            int colourChangeIndex = 0;

            for(int j = i + 1; j < colours.length; j++) {
                if(colour(j).name() != currentColour.name()) {
                    if(colourChangeIndex == 0) {
                        colourChangeIndex = j;
                    }
                } else {
                    if(colourChangeIndex != 0) {
                        swap(j, colourChangeIndex);
                        colourChangeIndex = j;
                    }
                }
            }

        }


        return colours;
    }

    public int getNumCallsToColour() {
        return numCallsToColour;
    }

    public int getNumCallsToSwap() {
        return numCallsToSwap;
    }
}

class Main {

    public static void main(String[] args) {

        ColourSwap.Colour[][] coloursTests = new ColourSwap.Colour[][] {
                new ColourSwap.Colour[] {ColourSwap.Colour.BLUE, ColourSwap.Colour.RED, ColourSwap.Colour.WHITE, ColourSwap.Colour.RED},
                new ColourSwap.Colour[] {ColourSwap.Colour.BLUE, ColourSwap.Colour.BLUE, ColourSwap.Colour.BLUE, ColourSwap.Colour.WHITE, ColourSwap.Colour.RED, ColourSwap.Colour.BLUE, ColourSwap.Colour.RED, ColourSwap.Colour.WHITE, ColourSwap.Colour.BLUE, ColourSwap.Colour.RED, ColourSwap.Colour.RED}
        };

        for(ColourSwap.Colour[] colours: coloursTests) {
            //Prints the test case
            StringBuilder strBldr = new StringBuilder();
            Stream.of(colours).forEach(colour -> strBldr.append(colour).append(","));
            System.out.println("test case: " + strBldr.toString());

            ColourSwap colourSwap = new ColourSwap(colours);
            Stream.of(colourSwap.groupColors()).forEach(System.out::println);
            System.out.println("Calls to colour(): " + colourSwap.getNumCallsToColour());
            System.out.println("Calls to swap(): " + colourSwap.getNumCallsToSwap());
        }


    }
}