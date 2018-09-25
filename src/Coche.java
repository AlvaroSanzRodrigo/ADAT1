public class Coche {
    private int ID;
    private String marca;
    private String modelo;
    private int cavallaje;
    private String color;

    public Coche() {
        super();
    }

    public Coche(int ID, String marca, String modelo, int cavallaje, String color) {
        this.ID = ID;
        this.marca = marca;
        this.modelo = modelo;
        this.cavallaje = cavallaje;
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCavallaje() {
        return cavallaje;
    }

    public void setCavallaje(int cavallaje) {
        this.cavallaje = cavallaje;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "Coche{" +
                "marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", cavallaje=" + cavallaje +
                ", color='" + color + '\'' +
                '}';
    }
}
