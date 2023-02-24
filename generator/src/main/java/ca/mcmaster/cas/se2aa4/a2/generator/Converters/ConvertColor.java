package ca.mcmaster.cas.se2aa4.a2.generator.Converters;

public class ConvertColor {
    public String convert(float[] colors) {
        float red = colors[0];
        float green = colors[1];
        float blue = colors[2];
        float alpha = colors[3];
        for (float color: colors) {
            if(color>1){
                break;
            }
        }
        return red + "," + green + "," + blue + ","+ alpha;
    }
}
